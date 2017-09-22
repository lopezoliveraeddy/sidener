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

import electorum.sidener.domain.enumeration.PollingPlaceType;

/**
 * Entidad Casillas
 */
@ApiModel(description = "Entidad Casillas")
@Entity
@Table(name = "polling_place")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pollingplace")
public class PollingPlace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private PollingPlaceType type;

    @Lob
    @Column(name = "adress")
    private String adress;

    @Column(name = "leftover_ballots")
    private Long leftoverBallots;

    /**
     * Total de Boletas Sobrantes
     */
    @ApiModelProperty(value = "Total de Boletas Sobrantes")
    @Column(name = "voting_citizens")
    private Long votingCitizens;

    /**
     * Total de Ciudadanos Votantes
     */
    @ApiModelProperty(value = "Total de Ciudadanos Votantes")
    @Column(name = "exctracted_ballots")
    private Long exctractedBallots;

    /**
     * Total de Boletas Extraídas
     */
    @ApiModelProperty(value = "Total de Boletas Extraídas")
    @Column(name = "not_registered")
    private Long notRegistered;

    /**
     * Total de Votos a Candidatos NO Registrados
     */
    @ApiModelProperty(value = "Total de Votos a Candidatos NO Registrados")
    @Column(name = "null_votes")
    private Long nullVotes;

    /**
     * Total de Votos Nulos
     */
    @ApiModelProperty(value = "Total de Votos Nulos")
    @Column(name = "total_votes")
    private Long totalVotes;

    /**
     * Total de Votos
     */
    @ApiModelProperty(value = "Total de Votos")
    @Column(name = "electoral_roll")
    private Long electoralRoll;

    /**
     * Ciudadanos en la Lista Nominal
     */
    @ApiModelProperty(value = "Ciudadanos en la Lista Nominal")
    @Lob
    @Column(name = "observations")
    private String observations;

    /**
     * Datos Sistema
     */
    @ApiModelProperty(value = "Datos Sistema")
    @Column(name = "published")
    private Boolean published;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "updated")
    private ZonedDateTime updated;

    /**
     * OK
     */
    @ApiModelProperty(value = "OK")
    @ManyToOne
    private District district;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PollingPlace name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PollingPlaceType getType() {
        return type;
    }

    public PollingPlace type(PollingPlaceType type) {
        this.type = type;
        return this;
    }

    public void setType(PollingPlaceType type) {
        this.type = type;
    }

    public String getAdress() {
        return adress;
    }

    public PollingPlace adress(String adress) {
        this.adress = adress;
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Long getLeftoverBallots() {
        return leftoverBallots;
    }

    public PollingPlace leftoverBallots(Long leftoverBallots) {
        this.leftoverBallots = leftoverBallots;
        return this;
    }

    public void setLeftoverBallots(Long leftoverBallots) {
        this.leftoverBallots = leftoverBallots;
    }

    public Long getVotingCitizens() {
        return votingCitizens;
    }

    public PollingPlace votingCitizens(Long votingCitizens) {
        this.votingCitizens = votingCitizens;
        return this;
    }

    public void setVotingCitizens(Long votingCitizens) {
        this.votingCitizens = votingCitizens;
    }

    public Long getExctractedBallots() {
        return exctractedBallots;
    }

    public PollingPlace exctractedBallots(Long exctractedBallots) {
        this.exctractedBallots = exctractedBallots;
        return this;
    }

    public void setExctractedBallots(Long exctractedBallots) {
        this.exctractedBallots = exctractedBallots;
    }

    public Long getNotRegistered() {
        return notRegistered;
    }

    public PollingPlace notRegistered(Long notRegistered) {
        this.notRegistered = notRegistered;
        return this;
    }

    public void setNotRegistered(Long notRegistered) {
        this.notRegistered = notRegistered;
    }

    public Long getNullVotes() {
        return nullVotes;
    }

    public PollingPlace nullVotes(Long nullVotes) {
        this.nullVotes = nullVotes;
        return this;
    }

    public void setNullVotes(Long nullVotes) {
        this.nullVotes = nullVotes;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public PollingPlace totalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
        return this;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Long getElectoralRoll() {
        return electoralRoll;
    }

    public PollingPlace electoralRoll(Long electoralRoll) {
        this.electoralRoll = electoralRoll;
        return this;
    }

    public void setElectoralRoll(Long electoralRoll) {
        this.electoralRoll = electoralRoll;
    }

    public String getObservations() {
        return observations;
    }

    public PollingPlace observations(String observations) {
        this.observations = observations;
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Boolean isPublished() {
        return published;
    }

    public PollingPlace published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public PollingPlace created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public PollingPlace updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public District getDistrict() {
        return district;
    }

    public PollingPlace district(District district) {
        this.district = district;
        return this;
    }

    public void setDistrict(District district) {
        this.district = district;
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
        PollingPlace pollingPlace = (PollingPlace) o;
        if (pollingPlace.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pollingPlace.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PollingPlace{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", adress='" + getAdress() + "'" +
            ", leftoverBallots='" + getLeftoverBallots() + "'" +
            ", votingCitizens='" + getVotingCitizens() + "'" +
            ", exctractedBallots='" + getExctractedBallots() + "'" +
            ", notRegistered='" + getNotRegistered() + "'" +
            ", nullVotes='" + getNullVotes() + "'" +
            ", totalVotes='" + getTotalVotes() + "'" +
            ", electoralRoll='" + getElectoralRoll() + "'" +
            ", observations='" + getObservations() + "'" +
            ", published='" + isPublished() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
