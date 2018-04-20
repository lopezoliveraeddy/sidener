package electorum.sidener.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import electorum.sidener.domain.enumeration.TypePollingPlace;

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

    /**
     * Municipio
     */
    @ApiModelProperty(value = "Municipio")
    @Column(name = "town")
    private String town;

    /**
     * Tipo de Casilla
     */
    @ApiModelProperty(value = "Tipo de Casilla")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_polling_place")
    private TypePollingPlace typePollingPlace;

    /**
     * Número Tipo de Casilla
     */
    @ApiModelProperty(value = "Número Tipo de Casilla")
    @Column(name = "type_number")
    private String typeNumber;

    /**
     * Sección
     */
    @ApiModelProperty(value = "Sección")
    @Column(name = "section")
    private Long section;

    /**
     * Dirección
     */
    @ApiModelProperty(value = "Dirección")
    @Lob
    @Column(name = "address")
    private String address;

    /**
     * Total de Boletas Sobrantes
     */
    @ApiModelProperty(value = "Total de Boletas Sobrantes")
    @Column(name = "leftover_ballots")
    private Long leftoverBallots;

    /**
     * Total de Ciudadanos Votantes
     */
    @ApiModelProperty(value = "Total de Ciudadanos Votantes")
    @Column(name = "voting_citizens")
    private Long votingCitizens;

    /**
     * Total de Boletas Extraídas
     */
    @ApiModelProperty(value = "Total de Boletas Extraídas")
    @Column(name = "extracted_ballots")
    private Long extractedBallots;

    /**
     * Total de Votos a Candidatos NO Registrados
     */
    @ApiModelProperty(value = "Total de Votos a Candidatos NO Registrados")
    @Column(name = "not_registered")
    private Long notRegistered;

    /**
     * Total de Votos Nulos
     */
    @ApiModelProperty(value = "Total de Votos Nulos")
    @Column(name = "null_votes")
    private Long nullVotes;

    /**
     * Total de Votos
     */
    @ApiModelProperty(value = "Total de Votos")
    @Column(name = "total_votes")
    private Long totalVotes;

    /**
     * Ciudadanos en la Lista Nominal
     */
    @ApiModelProperty(value = "Ciudadanos en la Lista Nominal")
    @Column(name = "electoral_roll")
    private Long electoralRoll;

    /**
     * Observaciones
     */
    @ApiModelProperty(value = "Observaciones")
    @Lob
    @Column(name = "observations")
    private String observations;

    /**
     * Presidente
     */
    @ApiModelProperty(value = "Presidente")
    @Column(name = "president")
    private String president;

    /**
     * Secretario
     */
    @ApiModelProperty(value = "Secretario")
    @Column(name = "secretary")
    private String secretary;

    /**
     * Primer Escrutador
     */
    @ApiModelProperty(value = "Primer Escrutador")
    @Column(name = "scrutineer_one")
    private String scrutineerOne;

    /**
     * Segundo Escrutador
     */
    @ApiModelProperty(value = "Segundo Escrutador")
    @Column(name = "scrutineer_two")
    private String scrutineerTwo;

    /**
     * Primer Alternativo
     */
    @ApiModelProperty(value = "Primer Alternativo")
    @Column(name = "alternate_one")
    private String alternateOne;

    /**
     * Segundo Alternativo
     */
    @ApiModelProperty(value = "Segundo Alternativo")
    @Column(name = "alternate_two")
    private String alternateTwo;

    /**
     * Tercer Alternativo
     */
    @ApiModelProperty(value = "Tercer Alternativo")
    @Column(name = "alternate_three")
    private String alternateThree;

    /**
     * Entidad 1er Lugar
     */
    @ApiModelProperty(value = "Entidad 1er Lugar")
    @Column(name = "entity_first_place")
    private String entityFirstPlace;

    /**
     * Total de votos para el Primer Lugar
     */
    @ApiModelProperty(value = "Total de votos para el Primer Lugar")
    @Column(name = "total_first_place")
    private Long totalFirstPlace;

    /**
     * Entidad 2do Lugar
     */
    @ApiModelProperty(value = "Entidad 2do Lugar")
    @Column(name = "entity_second_place")
    private String entitySecondPlace;

    /**
     * Total de votos para el Segundo Lugar
     */
    @ApiModelProperty(value = "Total de votos para el Segundo Lugar")
    @Column(name = "total_second_place")
    private Long totalSecondPlace;

    /**
     * Url Acta de Escrutinio y Cómputo
     */
    @ApiModelProperty(value = "Url Acta de Escrutinio y Cómputo")
    @Column(name = "url_record_count")
    private String urlRecordCount;

    /**
     * Casilla Ganada o Perdida
     */
    @ApiModelProperty(value = "Casilla Ganada o Perdida")
    @Column(name = "polling_place_won")
    private Boolean pollingPlaceWon;

    /**
     * Casilla a impugnar
     */
    @ApiModelProperty(value = "Casilla a impugnar")
    @Column(name = "challenged_polling_place")
    private Boolean challengedPollingPlace;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    /**
     * Acta de Escrutinio y Cómputo
     */
    @ApiModelProperty(value = "Acta de Escrutinio y Cómputo")
    @OneToOne
    @JoinColumn(unique = true)
    private Archive recordCount;

    @ManyToOne
    private Election election;

    @ManyToOne
    private District district;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "polling_place_causals",
               joinColumns = @JoinColumn(name="polling_places_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="causals_id", referencedColumnName="id"))
    private Set<Causal> causals = new HashSet<>();

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

    public TypePollingPlace getTypePollingPlace() {
        return typePollingPlace;
    }

    public PollingPlace typePollingPlace(TypePollingPlace typePollingPlace) {
        this.typePollingPlace = typePollingPlace;
        return this;
    }

    public void setTypePollingPlace(TypePollingPlace typePollingPlace) {
        this.typePollingPlace = typePollingPlace;
    }

    public String getTypeNumber() {
        return typeNumber;
    }

    public PollingPlace typeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
        return this;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }

    public Long getSection() {
        return section;
    }

    public PollingPlace section(Long section) {
        this.section = section;
        return this;
    }

    public void setSection(Long section) {
        this.section = section;
    }

    public String getAddress() {
        return address;
    }

    public PollingPlace address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Long getExtractedBallots() {
        return extractedBallots;
    }

    public PollingPlace extractedBallots(Long extractedBallots) {
        this.extractedBallots = extractedBallots;
        return this;
    }

    public void setExtractedBallots(Long extractedBallots) {
        this.extractedBallots = extractedBallots;
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

    public String getEntityFirstPlace() {
        return entityFirstPlace;
    }

    public PollingPlace entityFirstPlace(String entityFirstPlace) {
        this.entityFirstPlace = entityFirstPlace;
        return this;
    }

    public void setEntityFirstPlace(String entityFirstPlace) {
        this.entityFirstPlace = entityFirstPlace;
    }

    public Long getTotalFirstPlace() {
        return totalFirstPlace;
    }

    public PollingPlace totalFirstPlace(Long totalFirstPlace) {
        this.totalFirstPlace = totalFirstPlace;
        return this;
    }

    public void setTotalFirstPlace(Long totalFirstPlace) {
        this.totalFirstPlace = totalFirstPlace;
    }

    public String getEntitySecondPlace() {
        return entitySecondPlace;
    }

    public PollingPlace entitySecondPlace(String entitySecondPlace) {
        this.entitySecondPlace = entitySecondPlace;
        return this;
    }

    public void setEntitySecondPlace(String entitySecondPlace) {
        this.entitySecondPlace = entitySecondPlace;
    }

    public Long getTotalSecondPlace() {
        return totalSecondPlace;
    }

    public PollingPlace totalSecondPlace(Long totalSecondPlace) {
        this.totalSecondPlace = totalSecondPlace;
        return this;
    }

    public void setTotalSecondPlace(Long totalSecondPlace) {
        this.totalSecondPlace = totalSecondPlace;
    }

    public String getUrlRecordCount() {
        return urlRecordCount;
    }

    public PollingPlace urlRecordCount(String urlRecordCount) {
        this.urlRecordCount = urlRecordCount;
        return this;
    }

    public void setUrlRecordCount(String urlRecordCount) {
        this.urlRecordCount = urlRecordCount;
    }

    public Boolean isPollingPlaceWon() {
        return pollingPlaceWon;
    }

    public PollingPlace pollingPlaceWon(Boolean pollingPlaceWon) {
        this.pollingPlaceWon = pollingPlaceWon;
        return this;
    }

    public void setPollingPlaceWon(Boolean pollingPlaceWon) {
        this.pollingPlaceWon = pollingPlaceWon;
    }

    public Boolean isChallengedPollingPlace() {
        return challengedPollingPlace;
    }

    public PollingPlace challengedPollingPlace(Boolean challengedPollingPlace) {
        this.challengedPollingPlace = challengedPollingPlace;
        return this;
    }

    public void setChallengedPollingPlace(Boolean challengedPollingPlace) {
        this.challengedPollingPlace = challengedPollingPlace;
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

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public PollingPlace createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public PollingPlace updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Archive getRecordCount() {
        return recordCount;
    }

    public PollingPlace recordCount(Archive archive) {
        this.recordCount = archive;
        return this;
    }

    public void setRecordCount(Archive archive) {
        this.recordCount = archive;
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

    public Set<Causal> getCausals() {
        return causals;
    }

    public PollingPlace causals(Set<Causal> causals) {
        this.causals = causals;
        return this;
    }

    public PollingPlace addCausals(Causal causal) {
        this.causals.add(causal);
        return this;
    }

    public PollingPlace removeCausals(Causal causal) {
        this.causals.remove(causal);
        return this;
    }

    public void setCausals(Set<Causal> causals) {
        this.causals = causals;
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
            ", typePollingPlace='" + getTypePollingPlace() + "'" +
            ", typeNumber='" + getTypeNumber() + "'" +
            ", section='" + getSection() + "'" +
            ", address='" + getAddress() + "'" +
            ", leftoverBallots='" + getLeftoverBallots() + "'" +
            ", votingCitizens='" + getVotingCitizens() + "'" +
            ", extractedBallots='" + getExtractedBallots() + "'" +
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
            ", entityFirstPlace='" + getEntityFirstPlace() + "'" +
            ", totalFirstPlace='" + getTotalFirstPlace() + "'" +
            ", entitySecondPlace='" + getEntitySecondPlace() + "'" +
            ", totalSecondPlace='" + getTotalSecondPlace() + "'" +
            ", urlRecordCount='" + getUrlRecordCount() + "'" +
            ", pollingPlaceWon='" + isPollingPlaceWon() + "'" +
            ", challengedPollingPlace='" + isChallengedPollingPlace() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
