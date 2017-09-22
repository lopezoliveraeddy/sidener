package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.CoalitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Coalition and its DTO CoalitionDTO.
 */
@Mapper(componentModel = "spring", uses = {PoliticalPartyMapper.class, })
public interface CoalitionMapper extends EntityMapper <CoalitionDTO, Coalition> {
    
    
    default Coalition fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coalition coalition = new Coalition();
        coalition.setId(id);
        return coalition;
    }
}
