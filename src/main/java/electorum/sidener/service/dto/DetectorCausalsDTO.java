package electorum.sidener.service.dto;


import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the DetectorCausals entity.
 */
public class DetectorCausalsDTO implements Serializable {

    private Long id;

    private Long idPollingPlace;

    private Long idCausal;

    private Long idDistrict;

    @Lob
    private String observations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPollingPlace() {
        return idPollingPlace;
    }

    public void setIdPollingPlace(Long idPollingPlace) {
        this.idPollingPlace = idPollingPlace;
    }

    public Long getIdCausal() {
        return idCausal;
    }

    public void setIdCausal(Long idCausal) {
        this.idCausal = idCausal;
    }

    public Long getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(Long idDistrict) {
        this.idDistrict = idDistrict;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DetectorCausalsDTO detectorCausalsDTO = (DetectorCausalsDTO) o;
        if(detectorCausalsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), detectorCausalsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DetectorCausalsDTO{" +
            "id=" + getId() +
            ", idPollingPlace='" + getIdPollingPlace() + "'" +
            ", idCausal='" + getIdCausal() + "'" +
            ", idDistrict='" + getIdDistrict() + "'" +
            ", observations='" + getObservations() + "'" +
            "}";
    }
}
