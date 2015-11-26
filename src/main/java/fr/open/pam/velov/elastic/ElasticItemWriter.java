package fr.open.pam.velov.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.List;
import java.util.logging.Logger;


/**
 * @author @obazoud (Olivier Bazoud)
 */

//TODO: cleanup!
public class ElasticItemWriter<T> implements InitializingBean {

    private final Logger LOGGER = Logger.getLogger(ElasticItemWriter.class.getName());

    private Client client;
    private String indexName;

    private String typeName;
    private ObjectMapper mapper;
    private long timeoutMillis;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(client, "client must not be null");
        Assert.notNull(mapper, "mapper must not be null");
        Assert.notNull(indexName, "index name must not be null");
        Assert.notNull(typeName, "type must not be null");
    }

    public void write(List<? extends T> items) throws Exception {
        BulkRequestBuilder bulk = client.prepareBulk();

        for(T item: items) {
            bulk.add(client.prepareIndex(indexName,typeName)
                            .setSource(transform(item)));
        }

        BulkResponse response = bulk.execute().actionGet(timeoutMillis);
        if (response.hasFailures()) {
            throw new RuntimeException(response.buildFailureMessage());
        }

        LOGGER.info("Fermeture de la connexion à ES.");
        client.close();
    }

    protected String transform(T item) throws Exception {
        return mapper.writeValueAsString(item);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }
}