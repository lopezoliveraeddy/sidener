package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.ElectionTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ElectionType and its DTO ElectionTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ElectionTypeMapper extends EntityMapper <ElectionTypeDTO, ElectionType> {
    
    
    default ElectionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ElectionType electionType = new ElectionType();
        electionType.setId(id);
        return electionType;
    }
}
