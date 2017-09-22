package electorum.sidener.repository.search;

import electorum.sidener.domain.Coalition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Coalition entity.
 */
public interface CoalitionSearchRepository extends ElasticsearchRepository<Coalition, Long> {
}
