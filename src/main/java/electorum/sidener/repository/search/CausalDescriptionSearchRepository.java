package electorum.sidener.repository.search;

import electorum.sidener.domain.CausalDescription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CausalDescription entity.
 */
public interface CausalDescriptionSearchRepository extends ElasticsearchRepository<CausalDescription, Long> {
}
