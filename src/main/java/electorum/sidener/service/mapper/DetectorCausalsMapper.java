package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.DetectorCausalsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DetectorCausals and its DTO DetectorCausalsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DetectorCausalsMapper extends EntityMapper <DetectorCausalsDTO, DetectorCausals> {
    
    
    default DetectorCausals fromId(Long id) {
        if (id == null) {
            return null;
        }
        DetectorCausals detectorCausals = new DetectorCausals();
        detectorCausals.setId(id);
        return detectorCausals;
    }
}
