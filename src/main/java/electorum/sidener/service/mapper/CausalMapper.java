package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.CausalDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Causal and its DTO CausalDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CausalMapper extends EntityMapper <CausalDTO, Causal> {
    
    
    default Causal fromId(Long id) {
        if (id == null) {
            return null;
        }
        Causal causal = new Causal();
        causal.setId(id);
        return causal;
    }
}
