package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import electorum.sidener.domain.enumeration.ElectionAmbit;

/**
 * A DTO for the ElectionType entity.
 */
public class ElectionTypeDTO implements Serializable {

    private Long id;

    private String name;

    private ElectionAmbit electionAmbit;

    private Boolean published;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElectionAmbit getElectionAmbit() {
        return electionAmbit;
    }

    public void setElectionAmbit(ElectionAmbit electionAmbit) {
        this.electionAmbit = electionAmbit;
    }

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ElectionTypeDTO electionTypeDTO = (ElectionTypeDTO) o;
        if(electionTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), electionTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ElectionTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", electionAmbit='" + getElectionAmbit() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
