package electorum.sidener.repository.search;

import electorum.sidener.domain.Vote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Vote entity.
 */
public interface VoteSearchRepository extends ElasticsearchRepository<Vote, Long> {
}
