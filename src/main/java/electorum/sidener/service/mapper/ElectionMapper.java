package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.ElectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Election and its DTO ElectionDTO.
 */
@Mapper(componentModel = "spring", uses = {ElectionTypeMapper.class, PoliticalPartyMapper.class, CoalitionMapper.class, IndependentCandidateMapper.class, UserMapper.class, })
public interface ElectionMapper extends EntityMapper <ElectionDTO, Election> {

    @Mapping(source = "electionType.id", target = "electionTypeId")
    @Mapping(source = "electionType.name", target = "electionTypeName")

    @Mapping(source = "politicalPartyAssociated.id", target = "politicalPartyAssociatedId")
    @Mapping(source = "politicalPartyAssociated.name", target = "politicalPartyAssociatedName")
    @Mapping(source = "politicalPartyAssociated.acronym", target = "politicalPartyAssociatedAcronym")


    @Mapping(source = "coalitionAssociated.id", target = "coalitionAssociatedId")
    @Mapping(source = "coalitionAssociated.name", target = "coalitionAssociatedName")
    @Mapping(source = "coalitionAssociated.acronym", target = "coalitionAssociatedAcronym")

    @Mapping(source = "independentCandidateAssociated.id", target = "independentCandidateAssociatedId")
    @Mapping(source = "independentCandidateAssociated.name", target = "independentCandidateAssociatedName")
    @Mapping(source = "independentCandidateAssociated.acronym", target = "independentCandidateAssociatedAcronym")

    ElectionDTO toDto(Election election);

    @Mapping(source = "electionTypeId", target = "electionType")

    @Mapping(source = "politicalPartyAssociatedId", target = "politicalPartyAssociated")

    @Mapping(source = "coalitionAssociatedId", target = "coalitionAssociated")

    @Mapping(source = "independentCandidateAssociatedId", target = "independentCandidateAssociated")
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
