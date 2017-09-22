package electorum.sidener.repository.search;

import electorum.sidener.domain.Causal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Causal entity.
 */
public interface CausalSearchRepository extends ElasticsearchRepository<Causal, Long> {
}
