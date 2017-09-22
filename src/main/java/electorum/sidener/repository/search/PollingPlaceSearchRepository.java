package electorum.sidener.repository.search;

import electorum.sidener.domain.PollingPlace;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PollingPlace entity.
 */
public interface PollingPlaceSearchRepository extends ElasticsearchRepository<PollingPlace, Long> {
}
