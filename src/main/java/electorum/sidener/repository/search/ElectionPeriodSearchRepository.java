package electorum.sidener.repository.search;

import electorum.sidener.domain.ElectionPeriod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ElectionPeriod entity.
 */
public interface ElectionPeriodSearchRepository extends ElasticsearchRepository<ElectionPeriod, Long> {
}
