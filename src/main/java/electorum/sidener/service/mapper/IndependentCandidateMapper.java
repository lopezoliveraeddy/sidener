package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.IndependentCandidateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity IndependentCandidate and its DTO IndependentCandidateDTO.
 */
@Mapper(componentModel = "spring", uses = {ArchiveMapper.class, })
public interface IndependentCandidateMapper extends EntityMapper <IndependentCandidateDTO, IndependentCandidate> {

    @Mapping(source = "image.id", target = "imageId")
    IndependentCandidateDTO toDto(IndependentCandidate independentCandidate); 

    @Mapping(source = "imageId", target = "image")
    IndependentCandidate toEntity(IndependentCandidateDTO independentCandidateDTO); 
    default IndependentCandidate fromId(Long id) {
        if (id == null) {
            return null;
        }
        IndependentCandidate independentCandidate = new IndependentCandidate();
        independentCandidate.setId(id);
        return independentCandidate;
    }
}
