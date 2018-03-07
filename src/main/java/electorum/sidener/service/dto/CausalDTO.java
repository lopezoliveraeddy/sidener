package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import electorum.sidener.domain.enumeration.TypeCausal;
import electorum.sidener.domain.enumeration.SubTypeCausal;

/**
 * A DTO for the Causal entity.
 */
public class CausalDTO implements Serializable {

    private Long id;

    @Lob
    private String name;

    private TypeCausal typeCausal;

    private SubTypeCausal subTypeCausal;

    private String color;

    private Boolean published;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;

    private Set<CausalDescriptionDTO> causalDescriptions = new HashSet<>();

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

    public TypeCausal getTypeCausal() {
        return typeCausal;
    }

    public void setTypeCausal(TypeCausal typeCausal) {
        this.typeCausal = typeCausal;
    }

    public SubTypeCausal getSubTypeCausal() {
        return subTypeCausal;
    }

    public void setSubTypeCausal(SubTypeCausal subTypeCausal) {
        this.subTypeCausal = subTypeCausal;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Set<CausalDescriptionDTO> getCausalDescriptions() {
        return causalDescriptions;
    }

    public void setCausalDescriptions(Set<CausalDescriptionDTO> causalDescriptions) {
        this.causalDescriptions = causalDescriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CausalDTO causalDTO = (CausalDTO) o;
        if(causalDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), causalDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CausalDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", typeCausal='" + getTypeCausal() + "'" +
            ", subTypeCausal='" + getSubTypeCausal() + "'" +
            ", color='" + getColor() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
