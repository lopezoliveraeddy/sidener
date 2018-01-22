package electorum.sidener.service.mapper;

import electorum.sidener.domain.*;
import electorum.sidener.service.dto.CoalitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Coalition and its DTO CoalitionDTO.
 */
@Mapper(componentModel = "spring", uses = {ArchiveMapper.class, PoliticalPartyMapper.class, })
public interface CoalitionMapper extends EntityMapper <CoalitionDTO, Coalition> {

    @Mapping(source = "image.id", target = "imageId")
    @Mapping(source = "image.path", target = "imagePath")
    CoalitionDTO toDto(Coalition coalition); 

    @Mapping(source = "imageId", target = "image")
    Coalition toEntity(CoalitionDTO coalitionDTO); 
    default Coalition fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coalition coalition = new Coalition();
        coalition.setId(id);
        return coalition;
    }
}
