package electorum.sidener.repository.search;

import electorum.sidener.domain.IndependentCandidate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IndependentCandidate entity.
 */
public interface IndependentCandidateSearchRepository extends ElasticsearchRepository<IndependentCandidate, Long> {
}
