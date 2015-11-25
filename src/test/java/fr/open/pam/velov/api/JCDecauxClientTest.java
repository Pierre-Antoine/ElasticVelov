package fr.open.pam.velov.api;

import fr.open.pam.velov.model.JCDecauxStation;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pierre-antoine.marc on 12/11/2015.
 */
public class JCDecauxClientTest {

    @Test
    public void testCall() throws IOException {
        JCDecauxClient client = new JCDecauxClient("553697ac71debb821e9cde132cd668471dd2546f");
        List<JCDecauxStation> liste = client.getAllStations();
        //System.out.println(liste.size());


        //TODO: put this in another test class
        Settings settings = Settings.settingsBuilder()
                .put("client.transport.sniff", "true")
                .put("cluster.name", "elasticsearch")
                .put("node.name","White Tiger")
                .build();

        Client elasticClient = TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
                ;
    }


}
