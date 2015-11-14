package fr.open.pam.velov.elastic.repository;

import fr.open.pam.velov.elastic.entities.Station;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
/**
 * Created by pierre-antoine.marc on 14/11/2015.
 */
public interface StationRepository extends ElasticsearchRepository<Station, String> {
}
