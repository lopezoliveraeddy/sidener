package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.ElectionPeriodDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ElectionPeriod and its DTO ElectionPeriodDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ElectionPeriodMapper extends EntityMapper <ElectionPeriodDTO, ElectionPeriod> {
    
    
    default ElectionPeriod fromId(Long id) {
        if (id == null) {
            return null;
        }
        ElectionPeriod electionPeriod = new ElectionPeriod();
        electionPeriod.setId(id);
        return electionPeriod;
    }
}
