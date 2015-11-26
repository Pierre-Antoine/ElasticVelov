package fr.open.pam.velov;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.open.pam.velov.api.JCDecauxClient;
import fr.open.pam.velov.elastic.ElasticItemWriter;
import fr.open.pam.velov.model.JCDecauxStation;
import org.elasticsearch.client.Client;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by pierre-antoine.marc on 18/11/2015.
 */
public class VelovTasklet implements Tasklet {

    private final Logger LOGGER = Logger.getLogger(VelovTasklet.class.getName());
    @Autowired
    private JCDecauxClient jcDecauxClient;

    @Autowired
    private Client elasticClient;

    //TODO: move this to properties
    private static final String INDEX_STATIONS_NAME = "stations";
    private static final String INDEX_TYPE_NAME = "station";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        List<JCDecauxStation> listeStations = jcDecauxClient.getAllStations();

        ElasticItemWriter<JCDecauxStation> jcWriter = new ElasticItemWriter<>();

        //TODO: put this in a constructor?
        jcWriter.setIndexName(INDEX_STATIONS_NAME);
        jcWriter.setTypeName(INDEX_TYPE_NAME);
        jcWriter.setClient(elasticClient);
        jcWriter.setMapper(new ObjectMapper());
        jcWriter.setTimeoutMillis(1000000L);

        jcWriter.afterPropertiesSet();

        try {
            jcWriter.write(listeStations);
        } catch(Exception e){
            LOGGER.warning(String.format("Les documents ne sont pas correctement insérés : %s",e.getMessage()));
        }

        return RepeatStatus.FINISHED;
    }

    public void setJcDecauxClient(JCDecauxClient client) {
        this.jcDecauxClient = client;
    }

    public void setElasticClient(Client elasticClient) {
        this.elasticClient = elasticClient;
    }
}
