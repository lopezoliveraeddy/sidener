package electorum.sidener.repository.search;

import electorum.sidener.domain.DetectorCausals;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DetectorCausals entity.
 */
public interface DetectorCausalsSearchRepository extends ElasticsearchRepository<DetectorCausals, Long> {
}
