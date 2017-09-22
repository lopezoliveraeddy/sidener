package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.PollingPlaceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PollingPlace and its DTO PollingPlaceDTO.
 */
@Mapper(componentModel = "spring", uses = {DistrictMapper.class, })
public interface PollingPlaceMapper extends EntityMapper <PollingPlaceDTO, PollingPlace> {

    @Mapping(source = "district.id", target = "districtId")
    PollingPlaceDTO toDto(PollingPlace pollingPlace); 

    @Mapping(source = "districtId", target = "district")
    PollingPlace toEntity(PollingPlaceDTO pollingPlaceDTO); 
    default PollingPlace fromId(Long id) {
        if (id == null) {
            return null;
        }
        PollingPlace pollingPlace = new PollingPlace();
        pollingPlace.setId(id);
        return pollingPlace;
    }
}
