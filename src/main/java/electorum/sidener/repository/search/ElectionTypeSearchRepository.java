package electorum.sidener.repository.search;

import electorum.sidener.domain.ElectionType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ElectionType entity.
 */
public interface ElectionTypeSearchRepository extends ElasticsearchRepository<ElectionType, Long> {
}
