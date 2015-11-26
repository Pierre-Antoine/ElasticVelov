package fr.open.pam.velov.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import fr.open.pam.velov.model.JCDecauxStation;
import org.glassfish.jersey.client.ClientConfig;
import jersey.repackaged.com.google.common.base.Function;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by pierre-antoine.marc on 08/11/2015.
 */
public class JCDecauxClient {

    private static String apiKey;

    public final String PATH_URL_STATIONS = "stations";

    private final Logger LOGGER = Logger.getLogger(JCDecauxClient.class.getName());

    /**
     * Constructor
     * @param apiKey
     */
    public JCDecauxClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUri(){
        return "https://api.jcdecaux.com/vls/v1/";
    }

    /**
     * Get URL for stations
     * @return
     */
    private String getStationsUri(){
        return  PATH_URL_STATIONS;
    }

    /**
     * Returns a list of all stations
     * @return
     */
    public List<JCDecauxStation> getAllStations() {

        LOGGER.info("Récupération de la liste de toutes les stations");

        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(getBaseUri());

        String response = service.path(getStationsUri())
                .queryParam("apiKey",apiKey)
                .request().accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        response = response.replace("lng","lon");

        List<JCDecauxStation> listeStations =  null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            listeStations = mapper.readValue(response, TypeFactory.defaultInstance().constructCollectionType(List.class, JCDecauxStation.class));
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        LOGGER.info(String.format("%s documents ont été récupérés",String.valueOf(listeStations.size())));

        return listeStations;
    }
}
