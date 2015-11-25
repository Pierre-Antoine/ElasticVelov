package fr.open.pam.velov;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.open.pam.velov.api.JCDecauxClient;
import fr.open.pam.velov.elastic.ElasticItemWriter;
import fr.open.pam.velov.model.JCDecauxStation;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Created by pierre-antoine.marc on 18/11/2015.
 */
public class VelovTasklet implements Tasklet {

    @Autowired
    private JCDecauxClient jcDecauxClient;

    @Autowired
    @Qualifier("elasticClient")
    private Client elasticClient;

    private static final String INDEX_STATIONS_NAME = "stations";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        List<JCDecauxStation> listeStations = jcDecauxClient.getAllStations();

        ElasticItemWriter<JCDecauxStation> jcWriter = new ElasticItemWriter<>();
        jcWriter.setIndexName(INDEX_STATIONS_NAME);
        jcWriter.setClient(elasticClient);
        jcWriter.setMapper(new ObjectMapper());
        jcWriter.setTimeoutMillis(1000000L);

        jcWriter.afterPropertiesSet();

        jcWriter.write(listeStations);

        return RepeatStatus.FINISHED;
    }

    public void setJcDecauxClient(JCDecauxClient client) {
        this.jcDecauxClient = client;
    }

    public void setElasticClient(Client elasticClient) {
        this.elasticClient = elasticClient;
    }
}
