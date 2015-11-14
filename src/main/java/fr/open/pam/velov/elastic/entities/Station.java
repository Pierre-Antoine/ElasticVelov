package fr.open.pam.velov.elastic.entities;

/**
 * Created by pierre-antoine.marc on 14/11/2015.
 */

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import static org.springframework.data.elasticsearch.annotations.FieldType.String;

@Document(indexName = "stations", type = "stations", shards = 1, replicas = 0, refreshInterval = "-1", indexStoreType = "memory")
public class Station {
    @Id
    private String id;

    @Field(type = String)
    private String name;

    private GeoPoint position;
    private String contractName;
    private String status;
}
