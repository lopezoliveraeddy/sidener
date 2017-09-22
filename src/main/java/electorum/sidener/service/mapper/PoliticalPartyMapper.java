package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.PoliticalPartyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PoliticalParty and its DTO PoliticalPartyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PoliticalPartyMapper extends EntityMapper <PoliticalPartyDTO, PoliticalParty> {
    
    
    default PoliticalParty fromId(Long id) {
        if (id == null) {
            return null;
        }
        PoliticalParty politicalParty = new PoliticalParty();
        politicalParty.setId(id);
        return politicalParty;
    }
}
