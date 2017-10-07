package electorum.sidener.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Entidad Votos
 */
@ApiModel(description = "Entidad Votos")
@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vote")
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total")
    private Long total;

    /**
     * Datos Sistema
     */
    @ApiModelProperty(value = "Datos Sistema")
    @Column(name = "published")
    private Boolean published;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @ManyToOne
    private Election election;

    @ManyToOne
    private PoliticalParty politicalParty;

    @ManyToOne
    private IndependentCandidate independentCandidate;

    @ManyToOne
    private Coalition coalition;

    @ManyToOne
    private PollingPlace pollingPlace;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotal() {
        return total;
    }

    public Vote total(Long total) {
        this.total = total;
        return this;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean isPublished() {
        return published;
    }

    public Vote published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Vote createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public Vote updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Election getElection() {
        return election;
    }

    public Vote election(Election election) {
        this.election = election;
        return this;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public PoliticalParty getPoliticalParty() {
        return politicalParty;
    }

    public Vote politicalParty(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
        return this;
    }

    public void setPoliticalParty(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
    }

    public IndependentCandidate getIndependentCandidate() {
        return independentCandidate;
    }

    public Vote independentCandidate(IndependentCandidate independentCandidate) {
        this.independentCandidate = independentCandidate;
        return this;
    }

    public void setIndependentCandidate(IndependentCandidate independentCandidate) {
        this.independentCandidate = independentCandidate;
    }

    public Coalition getCoalition() {
        return coalition;
    }

    public Vote coalition(Coalition coalition) {
        this.coalition = coalition;
        return this;
    }

    public void setCoalition(Coalition coalition) {
        this.coalition = coalition;
    }

    public PollingPlace getPollingPlace() {
        return pollingPlace;
    }

    public Vote pollingPlace(PollingPlace pollingPlace) {
        this.pollingPlace = pollingPlace;
        return this;
    }

    public void setPollingPlace(PollingPlace pollingPlace) {
        this.pollingPlace = pollingPlace;
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
        Vote vote = (Vote) o;
        if (vote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vote{" +
            "id=" + getId() +
            ", total='" + getTotal() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
