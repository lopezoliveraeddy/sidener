package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.CausalDescriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CausalDescription and its DTO CausalDescriptionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CausalDescriptionMapper extends EntityMapper <CausalDescriptionDTO, CausalDescription> {
    
    
    default CausalDescription fromId(Long id) {
        if (id == null) {
            return null;
        }
        CausalDescription causalDescription = new CausalDescription();
        causalDescription.setId(id);
        return causalDescription;
    }
}
