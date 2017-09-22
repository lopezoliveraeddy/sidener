package electorum.sidener.repository.search;

import electorum.sidener.domain.Election;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Election entity.
 */
public interface ElectionSearchRepository extends ElasticsearchRepository<Election, Long> {
}
