package electorum.sidener.repository.search;

import electorum.sidener.domain.Archive;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Archive entity.
 */
public interface ArchiveSearchRepository extends ElasticsearchRepository<Archive, Long> {
}
