package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import electorum.sidener.domain.enumeration.CausalType;

/**
 * A DTO for the Causal entity.
 */
public class CausalDTO implements Serializable {

    private Long id;

    private String name;

    private CausalType type;

    @Lob
    private String description;

    private String color;

    private Boolean published;

    private ZonedDateTime created;

    private ZonedDateTime updated;

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

    public CausalType getType() {
        return type;
    }

    public void setType(CausalType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
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
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", color='" + getColor() + "'" +
            ", published='" + isPublished() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
