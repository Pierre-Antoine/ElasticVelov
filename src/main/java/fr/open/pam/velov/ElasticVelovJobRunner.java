package fr.open.pam.velov;


import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by pierre-antoine.marc on 24/11/2015.
 */
@ComponentScan
public class ElasticVelovJobRunner {
    public static void main(String[] args) {
        SpringApplication.run(BatchConfiguration.class, args);
    }
}
