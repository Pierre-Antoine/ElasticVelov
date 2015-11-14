package fr.open.pam.velov.elastic.entities;

/**
 * Created by pierre-antoine.marc on 14/11/2015.
 */

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "articles", type = "article", shards = 1, replicas = 0, refreshInterval = "-1", indexStoreType = "memory")
public class Station {
    @Id
    private String id;
}
