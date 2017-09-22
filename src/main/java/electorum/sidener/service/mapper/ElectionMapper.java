package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.ElectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Election and its DTO ElectionDTO.
 */
@Mapper(componentModel = "spring", uses = {StateMapper.class, ElectionTypeMapper.class, ElectionPeriodMapper.class, PoliticalPartyMapper.class, IndependentCandidateMapper.class, CoalitionMapper.class, CausalMapper.class, })
public interface ElectionMapper extends EntityMapper <ElectionDTO, Election> {

    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")

    @Mapping(source = "electionType.id", target = "electionTypeId")
    @Mapping(source = "electionType.name", target = "electionTypeName")

    @Mapping(source = "electionPeriod.id", target = "electionPeriodId")
    @Mapping(source = "electionPeriod.name", target = "electionPeriodName")
    ElectionDTO toDto(Election election); 

    @Mapping(source = "stateId", target = "state")

    @Mapping(source = "electionTypeId", target = "electionType")

    @Mapping(source = "electionPeriodId", target = "electionPeriod")
    Election toEntity(ElectionDTO electionDTO); 
    default Election fromId(Long id) {
        if (id == null) {
            return null;
        }
        Election election = new Election();
        election.setId(id);
        return election;
    }
}
