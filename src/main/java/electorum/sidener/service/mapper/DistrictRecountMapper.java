package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.DistrictDTO;

import electorum.sidener.service.dto.DistrictRecountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity District and its DTO DistrictDTO.
 */
@Mapper(componentModel = "spring", uses = {ElectionMapper.class, })
public interface DistrictRecountMapper extends EntityMapper <DistrictRecountDTO, DistrictDTO> {

    @Mapping(source = "election.id", target = "electionId")
    DistrictDTO toDto(District district);

    @Mapping(source = "electionId", target = "election")
    District toEntity(DistrictDTO districtDTO);
    default District fromId(Long id) {
        if (id == null) {
            return null;
        }
        District district = new District();
        district.setId(id);
        return district;
    }
}
