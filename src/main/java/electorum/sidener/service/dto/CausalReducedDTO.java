package electorum.sidener.service.dto;

import electorum.sidener.domain.enumeration.CausalType;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

public class CausalReducedDTO implements Serializable {

    private Long id;
    @Lob
    private String name;
    private CausalType typeCausal;
    private String color;

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
    public CausalType getTypeCausal() {
        return typeCausal;
    }
    public void setTypeCausal(CausalType typeCausal) {
        this.typeCausal = typeCausal;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
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
        return "CausalReducedDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", typeCausal='" + getTypeCausal() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
