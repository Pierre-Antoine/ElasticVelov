package fr.open.pam.velov;

import fr.open.pam.velov.api.JCDecauxClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * Created by pierre-antoine.marc on 08/11/2015.
 */

@Configuration
@EnableBatchProcessing
//@PropertySource("classpath:config/application.properties")
@EnableAutoConfiguration
public class BatchConfiguration {

    private final Logger LOGGER = Logger.getLogger(BatchConfiguration.class.getName());

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Value("${velov.api.key}")
    private String apiKey ;

    @Value("${elastic.node.name}")
    private String nodeName;

    @Value("${elastic.cluster.name}")
    private String clusterName;

    @Value("${elastic.node.host}")
    private String nodeHost;

    @Value("${elastic.node.port}")
    private int nodePort;

    public JCDecauxClient client(){
        return new JCDecauxClient(apiKey);
    }

    public Client elasticClient() {
        LOGGER.info("Instantiation du client ES");
        Settings settings = Settings.settingsBuilder()
                .put("client.transport.sniff", "true")
                .put("cluster.name", clusterName)
                .put("node.name", nodeName)
                .build();

        Client elasticClient = null;
        try {
            elasticClient = TransportClient.builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(nodeHost), nodePort));
        } catch (UnknownHostException e) {
            LOGGER.warning(e.getMessage());
        }

        if(elasticClient == null){
            LOGGER.warning("Client mal instantié !");
        }else{
            LOGGER.info(elasticClient.toString());
        }

        return elasticClient;
    }

    @Bean
    public Job job() {
        return jobs.get("ElasticVelovJob").start(step1(velovTasklet())).build();
    }

    @Bean
    protected Step step1(Tasklet tasklet) {
        return steps.get("step1").tasklet(tasklet).build();
    }

    public Tasklet velovTasklet(){
        VelovTasklet velovTasklet = new VelovTasklet();
        velovTasklet.setJcDecauxClient(client());
        velovTasklet.setElasticClient(elasticClient());
        return velovTasklet;
    }
}
