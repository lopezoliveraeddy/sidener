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

    @Column(name = "town")
    private String town;

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

    @Column(name = "president")
    private String president;

    @Column(name = "secretary")
    private String secretary;

    @Column(name = "scrutineer_one")
    private String scrutineerOne;

    @Column(name = "scrutineer_two")
    private String scrutineerTwo;

    @Column(name = "alternate_one")
    private String alternateOne;

    @Column(name = "alternate_two")
    private String alternateTwo;

    @Column(name = "alternate_three")
    private String alternateThree;

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

    @ManyToOne
    private Election election;

    @ManyToOne
    private District district;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTown() {
        return town;
    }

    public PollingPlace town(String town) {
        this.town = town;
        return this;
    }

    public void setTown(String town) {
        this.town = town;
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

    public String getPresident() {
        return president;
    }

    public PollingPlace president(String president) {
        this.president = president;
        return this;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getSecretary() {
        return secretary;
    }

    public PollingPlace secretary(String secretary) {
        this.secretary = secretary;
        return this;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public String getScrutineerOne() {
        return scrutineerOne;
    }

    public PollingPlace scrutineerOne(String scrutineerOne) {
        this.scrutineerOne = scrutineerOne;
        return this;
    }

    public void setScrutineerOne(String scrutineerOne) {
        this.scrutineerOne = scrutineerOne;
    }

    public String getScrutineerTwo() {
        return scrutineerTwo;
    }

    public PollingPlace scrutineerTwo(String scrutineerTwo) {
        this.scrutineerTwo = scrutineerTwo;
        return this;
    }

    public void setScrutineerTwo(String scrutineerTwo) {
        this.scrutineerTwo = scrutineerTwo;
    }

    public String getAlternateOne() {
        return alternateOne;
    }

    public PollingPlace alternateOne(String alternateOne) {
        this.alternateOne = alternateOne;
        return this;
    }

    public void setAlternateOne(String alternateOne) {
        this.alternateOne = alternateOne;
    }

    public String getAlternateTwo() {
        return alternateTwo;
    }

    public PollingPlace alternateTwo(String alternateTwo) {
        this.alternateTwo = alternateTwo;
        return this;
    }

    public void setAlternateTwo(String alternateTwo) {
        this.alternateTwo = alternateTwo;
    }

    public String getAlternateThree() {
        return alternateThree;
    }

    public PollingPlace alternateThree(String alternateThree) {
        this.alternateThree = alternateThree;
        return this;
    }

    public void setAlternateThree(String alternateThree) {
        this.alternateThree = alternateThree;
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

    public Election getElection() {
        return election;
    }

    public PollingPlace election(Election election) {
        this.election = election;
        return this;
    }

    public void setElection(Election election) {
        this.election = election;
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
            ", town='" + getTown() + "'" +
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
            ", president='" + getPresident() + "'" +
            ", secretary='" + getSecretary() + "'" +
            ", scrutineerOne='" + getScrutineerOne() + "'" +
            ", scrutineerTwo='" + getScrutineerTwo() + "'" +
            ", alternateOne='" + getAlternateOne() + "'" +
            ", alternateTwo='" + getAlternateTwo() + "'" +
            ", alternateThree='" + getAlternateThree() + "'" +
            ", published='" + isPublished() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
