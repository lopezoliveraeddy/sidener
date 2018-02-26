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

import electorum.sidener.domain.enumeration.State;

/**
 * Entidad Distritos
 */
@ApiModel(description = "Entidad Distritos")
@Entity
@Table(name = "district")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "district")
public class District implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número de Distrito (Decimal)
     */
    @ApiModelProperty(value = "Número de Distrito (Decimal)")
    @Column(name = "decimal_number")
    private Long decimalNumber;

    /**
     * Número de Distrito (Romano)
     */
    @ApiModelProperty(value = "Número de Distrito (Romano)")
    @Column(name = "roman_number")
    private String romanNumber;

    /**
     * Cabecera Distrital
     */
    @ApiModelProperty(value = "Cabecera Distrital")
    @Column(name = "district_head")
    private String districtHead;

    /**
     * Entidad Federativa donde pertenece el Distrito
     */
    @ApiModelProperty(value = "Entidad Federativa donde pertenece el Distrito")
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

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
     * Total Votos
     */
    @ApiModelProperty(value = "Total Votos")
    @Column(name = "total_votes")
    private Long totalVotes;

    /**
     * Ciudadanos en la Lista Nominal
     */
    @ApiModelProperty(value = "Ciudadanos en la Lista Nominal")
    @Column(name = "electoral_roll")
    private Long electoralRoll;

    /**
     * Total de Casillas
     */
    @ApiModelProperty(value = "Total de Casillas")
    @Column(name = "total_polling_places")
    private Long totalPollingPlaces;

    /**
     * Votos Nulos
     */
    @ApiModelProperty(value = "Votos Nulos")
    @Column(name = "null_votes")
    private Long nullVotes;

    @Column(name = "district_won")
    private Boolean districtWon;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @ManyToOne
    private Election election;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDecimalNumber() {
        return decimalNumber;
    }

    public District decimalNumber(Long decimalNumber) {
        this.decimalNumber = decimalNumber;
        return this;
    }

    public void setDecimalNumber(Long decimalNumber) {
        this.decimalNumber = decimalNumber;
    }

    public String getRomanNumber() {
        return romanNumber;
    }

    public District romanNumber(String romanNumber) {
        this.romanNumber = romanNumber;
        return this;
    }

    public void setRomanNumber(String romanNumber) {
        this.romanNumber = romanNumber;
    }

    public String getDistrictHead() {
        return districtHead;
    }

    public District districtHead(String districtHead) {
        this.districtHead = districtHead;
        return this;
    }

    public void setDistrictHead(String districtHead) {
        this.districtHead = districtHead;
    }

    public State getState() {
        return state;
    }

    public District state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getEntityFirstPlace() {
        return entityFirstPlace;
    }

    public District entityFirstPlace(String entityFirstPlace) {
        this.entityFirstPlace = entityFirstPlace;
        return this;
    }

    public void setEntityFirstPlace(String entityFirstPlace) {
        this.entityFirstPlace = entityFirstPlace;
    }

    public Long getTotalFirstPlace() {
        return totalFirstPlace;
    }

    public District totalFirstPlace(Long totalFirstPlace) {
        this.totalFirstPlace = totalFirstPlace;
        return this;
    }

    public void setTotalFirstPlace(Long totalFirstPlace) {
        this.totalFirstPlace = totalFirstPlace;
    }

    public String getEntitySecondPlace() {
        return entitySecondPlace;
    }

    public District entitySecondPlace(String entitySecondPlace) {
        this.entitySecondPlace = entitySecondPlace;
        return this;
    }

    public void setEntitySecondPlace(String entitySecondPlace) {
        this.entitySecondPlace = entitySecondPlace;
    }

    public Long getTotalSecondPlace() {
        return totalSecondPlace;
    }

    public District totalSecondPlace(Long totalSecondPlace) {
        this.totalSecondPlace = totalSecondPlace;
        return this;
    }

    public void setTotalSecondPlace(Long totalSecondPlace) {
        this.totalSecondPlace = totalSecondPlace;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public District totalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
        return this;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Long getElectoralRoll() {
        return electoralRoll;
    }

    public District electoralRoll(Long electoralRoll) {
        this.electoralRoll = electoralRoll;
        return this;
    }

    public void setElectoralRoll(Long electoralRoll) {
        this.electoralRoll = electoralRoll;
    }

    public Long getTotalPollingPlaces() {
        return totalPollingPlaces;
    }

    public District totalPollingPlaces(Long totalPollingPlaces) {
        this.totalPollingPlaces = totalPollingPlaces;
        return this;
    }

    public void setTotalPollingPlaces(Long totalPollingPlaces) {
        this.totalPollingPlaces = totalPollingPlaces;
    }

    public Long getNullVotes() {
        return nullVotes;
    }

    public District nullVotes(Long nullVotes) {
        this.nullVotes = nullVotes;
        return this;
    }

    public void setNullVotes(Long nullVotes) {
        this.nullVotes = nullVotes;
    }

    public Boolean isDistrictWon() {
        return districtWon;
    }

    public District districtWon(Boolean districtWon) {
        this.districtWon = districtWon;
        return this;
    }

    public void setDistrictWon(Boolean districtWon) {
        this.districtWon = districtWon;
    }

    public Boolean isPublished() {
        return published;
    }

    public District published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public District createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public District updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Election getElection() {
        return election;
    }

    public District election(Election election) {
        this.election = election;
        return this;
    }

    public void setElection(Election election) {
        this.election = election;
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
        District district = (District) o;
        if (district.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), district.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "District{" +
            "id=" + getId() +
            ", decimalNumber='" + getDecimalNumber() + "'" +
            ", romanNumber='" + getRomanNumber() + "'" +
            ", districtHead='" + getDistrictHead() + "'" +
            ", state='" + getState() + "'" +
            ", entityFirstPlace='" + getEntityFirstPlace() + "'" +
            ", totalFirstPlace='" + getTotalFirstPlace() + "'" +
            ", entitySecondPlace='" + getEntitySecondPlace() + "'" +
            ", totalSecondPlace='" + getTotalSecondPlace() + "'" +
            ", totalVotes='" + getTotalVotes() + "'" +
            ", electoralRoll='" + getElectoralRoll() + "'" +
            ", totalPollingPlaces='" + getTotalPollingPlaces() + "'" +
            ", nullVotes='" + getNullVotes() + "'" +
            ", districtWon='" + isDistrictWon() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
