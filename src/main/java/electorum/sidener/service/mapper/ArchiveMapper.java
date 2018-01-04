package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.ArchiveDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Archive and its DTO ArchiveDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArchiveMapper extends EntityMapper <ArchiveDTO, Archive> {
    
    
    default Archive fromId(Long id) {
        if (id == null) {
            return null;
        }
        Archive archive = new Archive();
        archive.setId(id);
        return archive;
    }
}
