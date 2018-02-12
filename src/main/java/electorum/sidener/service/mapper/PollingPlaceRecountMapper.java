package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.PollingPlaceDTO;

import electorum.sidener.service.dto.PollingPlaceRecountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity PollingPlace and its DTO PollingPlaceDTO.
 */
@Mapper(componentModel = "spring", uses = {ArchiveMapper.class, ElectionMapper.class, DistrictMapper.class, CausalMapper.class, })
public interface PollingPlaceRecountMapper extends EntityMapper <PollingPlaceRecountDTO, PollingPlaceDTO> {

    @Mapping(source = "recordCount.id", target = "recordCountId")
    @Mapping(source = "recordCount.path", target = "recordCountPath")

    @Mapping(source = "election.id", target = "electionId")

    @Mapping(source = "district.id", target = "districtId")
    PollingPlaceDTO toDto(PollingPlace pollingPlace);

    @Mapping(source = "recordCountId", target = "recordCount")

    @Mapping(source = "electionId", target = "election")

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
