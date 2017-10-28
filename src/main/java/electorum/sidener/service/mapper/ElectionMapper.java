package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.ElectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Election and its DTO ElectionDTO.
 */
@Mapper(componentModel = "spring", uses = {ElectionTypeMapper.class, PoliticalPartyMapper.class, CoalitionMapper.class, IndependentCandidateMapper.class, CausalMapper.class, UserMapper.class, })
public interface ElectionMapper extends EntityMapper <ElectionDTO, Election> {

    @Mapping(source = "electionType.id", target = "electionTypeId")
    @Mapping(source = "electionType.name", target = "electionTypeName")

    @Mapping(source = "politicalPartyAsociated.id", target = "politicalPartyAsociatedId")
    @Mapping(source = "politicalPartyAsociated.name", target = "politicalPartyAsociatedName")

    @Mapping(source = "coalitionAsociated.id", target = "coalitionAsociatedId")
    @Mapping(source = "coalitionAsociated.name", target = "coalitionAsociatedName")

    @Mapping(source = "independentCandidateAsociated.id", target = "independentCandidateAsociatedId")
    @Mapping(source = "independentCandidateAsociated.name", target = "independentCandidateAsociatedName")
    ElectionDTO toDto(Election election); 

    @Mapping(source = "electionTypeId", target = "electionType")

    @Mapping(source = "politicalPartyAsociatedId", target = "politicalPartyAsociated")

    @Mapping(source = "coalitionAsociatedId", target = "coalitionAsociated")

    @Mapping(source = "independentCandidateAsociatedId", target = "independentCandidateAsociated")
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
