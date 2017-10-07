package electorum.sidener.service.dto;


import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the CausalDescription entity.
 */
public class CausalDescriptionDTO implements Serializable {

    private Long id;

    @Lob
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CausalDescriptionDTO causalDescriptionDTO = (CausalDescriptionDTO) o;
        if(causalDescriptionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), causalDescriptionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CausalDescriptionDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            "}";
    }
}
