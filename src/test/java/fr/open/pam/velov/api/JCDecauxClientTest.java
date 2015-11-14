package fr.open.pam.velov.api;

import fr.open.pam.velov.elastic.entities.Station;
import fr.open.pam.velov.model.JCDecauxStation;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.repositories.Repository;
import org.junit.Test;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.io.IOException;
import java.util.List;

/**
 * Created by pierre-antoine.marc on 12/11/2015.
 */
public class JCDecauxClientTest {

    @Test
    public void testCall() throws IOException {
        JCDecauxClient client = new JCDecauxClient("553697ac71debb821e9cde132cd668471dd2546f");
        List<JCDecauxStation> liste = client.getAllStations();
        System.out.println(liste.size());

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("client.transport.sniff", "true")
                .put("cluster.name", "elasticsearch")
                .build();

        Client elasticClient = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));

        ElasticsearchTemplate template = new ElasticsearchTemplate(elasticClient);

    }


}
