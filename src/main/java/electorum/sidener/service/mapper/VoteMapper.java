package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.VoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vote and its DTO VoteDTO.
 */
@Mapper(componentModel = "spring", uses = {PoliticalPartyMapper.class, IndependentCandidateMapper.class, CoalitionMapper.class, PollingPlaceMapper.class, })
public interface VoteMapper extends EntityMapper <VoteDTO, Vote> {

    @Mapping(source = "politicalParty.id", target = "politicalPartyId")
    @Mapping(source = "politicalParty.name", target = "politicalPartyName")

    @Mapping(source = "independentCandidate.id", target = "independentCandidateId")
    @Mapping(source = "independentCandidate.name", target = "independentCandidateName")

    @Mapping(source = "coalition.id", target = "coalitionId")
    @Mapping(source = "coalition.name", target = "coalitionName")

    @Mapping(source = "pollingPlace.id", target = "pollingPlaceId")
    @Mapping(source = "pollingPlace.name", target = "pollingPlaceName")
    VoteDTO toDto(Vote vote); 

    @Mapping(source = "politicalPartyId", target = "politicalParty")

    @Mapping(source = "independentCandidateId", target = "independentCandidate")

    @Mapping(source = "coalitionId", target = "coalition")

    @Mapping(source = "pollingPlaceId", target = "pollingPlace")
    Vote toEntity(VoteDTO voteDTO); 
    default Vote fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vote vote = new Vote();
        vote.setId(id);
        return vote;
    }
}
