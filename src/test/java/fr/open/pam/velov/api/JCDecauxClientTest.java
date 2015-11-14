package fr.open.pam.velov.api;

import fr.open.pam.velov.model.JCDecauxStation;
import org.junit.Test;

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
    }


}
