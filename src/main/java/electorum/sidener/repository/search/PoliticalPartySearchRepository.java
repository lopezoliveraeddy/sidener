package electorum.sidener.repository.search;

import electorum.sidener.domain.PoliticalParty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PoliticalParty entity.
 */
public interface PoliticalPartySearchRepository extends ElasticsearchRepository<PoliticalParty, Long> {
}
