package electorum.sidener.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DetectorCausals.
 */
@Entity
@Table(name = "detector_causals")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "detectorcausals")
public class DetectorCausals implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_polling_place")
    private Long idPollingPlace;

    @Column(name = "id_causal")
    private Long idCausal;

    @Column(name = "id_district")
    private Long idDistrict;

    @Lob
    @Column(name = "observations")
    private String observations;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPollingPlace() {
        return idPollingPlace;
    }

    public DetectorCausals idPollingPlace(Long idPollingPlace) {
        this.idPollingPlace = idPollingPlace;
        return this;
    }

    public void setIdPollingPlace(Long idPollingPlace) {
        this.idPollingPlace = idPollingPlace;
    }

    public Long getIdCausal() {
        return idCausal;
    }

    public DetectorCausals idCausal(Long idCausal) {
        this.idCausal = idCausal;
        return this;
    }

    public void setIdCausal(Long idCausal) {
        this.idCausal = idCausal;
    }

    public Long getIdDistrict() {
        return idDistrict;
    }

    public DetectorCausals idDistrict(Long idDistrict) {
        this.idDistrict = idDistrict;
        return this;
    }

    public void setIdDistrict(Long idDistrict) {
        this.idDistrict = idDistrict;
    }

    public String getObservations() {
        return observations;
    }

    public DetectorCausals observations(String observations) {
        this.observations = observations;
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DetectorCausals detectorCausals = (DetectorCausals) o;
        if (detectorCausals.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), detectorCausals.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DetectorCausals{" +
            "id=" + getId() +
            ", idPollingPlace='" + getIdPollingPlace() + "'" +
            ", idCausal='" + getIdCausal() + "'" +
            ", idDistrict='" + getIdDistrict() + "'" +
            ", observations='" + getObservations() + "'" +
            "}";
    }
}
