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

import electorum.sidener.domain.enumeration.State;

import electorum.sidener.domain.enumeration.Status;

import electorum.sidener.domain.enumeration.RecountDistrictsRule;

import electorum.sidener.domain.enumeration.RecountPollingPlaceRule;

/**
 * Entidad propia de una elección, la cual almacena datos informativos de la elección
 */
@ApiModel(description = "Entidad propia de una elección, la cual almacena datos informativos de la elección")
@Entity
@Table(name = "election")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "election")
public class Election implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Entidad Federativa donde se realiza la Elección
     */
    @ApiModelProperty(value = "Entidad Federativa donde se realiza la Elección")
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    /**
     * Periodo a realizarse la Elección
     */
    @ApiModelProperty(value = "Periodo a realizarse la Elección")
    @Column(name = "period_election")
    private String periodElection;

    /**
     * Fecha de la Elección
     */
    @ApiModelProperty(value = "Fecha de la Elección")
    @Column(name = "date_election")
    private ZonedDateTime dateElection;

    /**
     * Estatus de la Eleccion
     */
    @ApiModelProperty(value = "Estatus de la Eleccion")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    /**
     * Base de Datos del Proceso Electoral (Archivo)
     */
    @ApiModelProperty(value = "Base de Datos del Proceso Electoral (Archivo)")
    @Column(name = "data_base")
    private String dataBase;

    /**
     * Encarte (Archivo)
     */
    @ApiModelProperty(value = "Encarte (Archivo)")
    @Column(name = "inset_url")
    private String insetUrl;

    /**
     * Hoja de Incidentes
     */
    @ApiModelProperty(value = "Hoja de Incidentes")
    @Column(name = "incident_sheet")
    private String incidentSheet;

    /**
     * Acta de la Jornada
     */
    @ApiModelProperty(value = "Acta de la Jornada")
    @Column(name = "day_record")
    private String dayRecord;

    /**
     * Plantilla de Demanda (Archivo)
     */
    @ApiModelProperty(value = "Plantilla de Demanda (Archivo)")
    @Column(name = "demand_template_url")
    private String demandTemplateUrl;

    /**
     * Plantilla de Recuento (Archivo)
     */
    @ApiModelProperty(value = "Plantilla de Recuento (Archivo)")
    @Column(name = "recount_template_url")
    private String recountTemplateUrl;

    /**
     * Regla para recuento de Distritos
     */
    @ApiModelProperty(value = "Regla para recuento de Distritos")
    @Enumerated(EnumType.STRING)
    @Column(name = "recount_districts_rule")
    private RecountDistrictsRule recountDistrictsRule;

    /**
     * Regla para recuento de Casillas
     */
    @ApiModelProperty(value = "Regla para recuento de Casillas")
    @Enumerated(EnumType.STRING)
    @Column(name = "recount_polling_place_rule")
    private RecountPollingPlaceRule recountPollingPlaceRule;

    /**
     * Nombre del Demandante
     */
    @ApiModelProperty(value = "Nombre del Demandante")
    @Column(name = "name_demandant")
    private String nameDemandant;

    /**
     * Instituto electoral
     */
    @ApiModelProperty(value = "Instituto electoral")
    @Column(name = "recount_electoral_institute")
    private String recountElectoralInstitute;

    /**
     * Tipo de Computo
     */
    @ApiModelProperty(value = "Tipo de Computo")
    @Column(name = "recount_type")
    private String recountType;

    /**
     * Fundamento de la Solicitud
     */
    @ApiModelProperty(value = "Fundamento de la Solicitud")
    @Lob
    @Column(name = "recount_fundament_request")
    private String recountFundamentRequest;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @ManyToOne
    private ElectionType electionType;

    @ManyToOne
    private PoliticalParty politicalPartyAsociated;

    @ManyToOne
    private Coalition coalitionAsociated;

    @ManyToOne
    private IndependentCandidate independentCandidateAsociated;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "election_political_parties",
               joinColumns = @JoinColumn(name="elections_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="political_parties_id", referencedColumnName="id"))
    private Set<PoliticalParty> politicalParties = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "election_independent_candidates",
               joinColumns = @JoinColumn(name="elections_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="independent_candidates_id", referencedColumnName="id"))
    private Set<IndependentCandidate> independentCandidates = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "election_coalitions",
               joinColumns = @JoinColumn(name="elections_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="coalitions_id", referencedColumnName="id"))
    private Set<Coalition> coalitions = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "election_user",
               joinColumns = @JoinColumn(name="elections_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="id"))
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public Election state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPeriodElection() {
        return periodElection;
    }

    public Election periodElection(String periodElection) {
        this.periodElection = periodElection;
        return this;
    }

    public void setPeriodElection(String periodElection) {
        this.periodElection = periodElection;
    }

    public ZonedDateTime getDateElection() {
        return dateElection;
    }

    public Election dateElection(ZonedDateTime dateElection) {
        this.dateElection = dateElection;
        return this;
    }

    public void setDateElection(ZonedDateTime dateElection) {
        this.dateElection = dateElection;
    }

    public Status getStatus() {
        return status;
    }

    public Election status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDataBase() {
        return dataBase;
    }

    public Election dataBase(String dataBase) {
        this.dataBase = dataBase;
        return this;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getInsetUrl() {
        return insetUrl;
    }

    public Election insetUrl(String insetUrl) {
        this.insetUrl = insetUrl;
        return this;
    }

    public void setInsetUrl(String insetUrl) {
        this.insetUrl = insetUrl;
    }

    public String getIncidentSheet() {
        return incidentSheet;
    }

    public Election incidentSheet(String incidentSheet) {
        this.incidentSheet = incidentSheet;
        return this;
    }

    public void setIncidentSheet(String incidentSheet) {
        this.incidentSheet = incidentSheet;
    }

    public String getDayRecord() {
        return dayRecord;
    }

    public Election dayRecord(String dayRecord) {
        this.dayRecord = dayRecord;
        return this;
    }

    public void setDayRecord(String dayRecord) {
        this.dayRecord = dayRecord;
    }

    public String getDemandTemplateUrl() {
        return demandTemplateUrl;
    }

    public Election demandTemplateUrl(String demandTemplateUrl) {
        this.demandTemplateUrl = demandTemplateUrl;
        return this;
    }

    public void setDemandTemplateUrl(String demandTemplateUrl) {
        this.demandTemplateUrl = demandTemplateUrl;
    }

    public String getRecountTemplateUrl() {
        return recountTemplateUrl;
    }

    public Election recountTemplateUrl(String recountTemplateUrl) {
        this.recountTemplateUrl = recountTemplateUrl;
        return this;
    }

    public void setRecountTemplateUrl(String recountTemplateUrl) {
        this.recountTemplateUrl = recountTemplateUrl;
    }

    public RecountDistrictsRule getRecountDistrictsRule() {
        return recountDistrictsRule;
    }

    public Election recountDistrictsRule(RecountDistrictsRule recountDistrictsRule) {
        this.recountDistrictsRule = recountDistrictsRule;
        return this;
    }

    public void setRecountDistrictsRule(RecountDistrictsRule recountDistrictsRule) {
        this.recountDistrictsRule = recountDistrictsRule;
    }

    public RecountPollingPlaceRule getRecountPollingPlaceRule() {
        return recountPollingPlaceRule;
    }

    public Election recountPollingPlaceRule(RecountPollingPlaceRule recountPollingPlaceRule) {
        this.recountPollingPlaceRule = recountPollingPlaceRule;
        return this;
    }

    public void setRecountPollingPlaceRule(RecountPollingPlaceRule recountPollingPlaceRule) {
        this.recountPollingPlaceRule = recountPollingPlaceRule;
    }

    public String getNameDemandant() {
        return nameDemandant;
    }

    public Election nameDemandant(String nameDemandant) {
        this.nameDemandant = nameDemandant;
        return this;
    }

    public void setNameDemandant(String nameDemandant) {
        this.nameDemandant = nameDemandant;
    }

    public String getRecountElectoralInstitute() {
        return recountElectoralInstitute;
    }

    public Election recountElectoralInstitute(String recountElectoralInstitute) {
        this.recountElectoralInstitute = recountElectoralInstitute;
        return this;
    }

    public void setRecountElectoralInstitute(String recountElectoralInstitute) {
        this.recountElectoralInstitute = recountElectoralInstitute;
    }

    public String getRecountType() {
        return recountType;
    }

    public Election recountType(String recountType) {
        this.recountType = recountType;
        return this;
    }

    public void setRecountType(String recountType) {
        this.recountType = recountType;
    }

    public String getRecountFundamentRequest() {
        return recountFundamentRequest;
    }

    public Election recountFundamentRequest(String recountFundamentRequest) {
        this.recountFundamentRequest = recountFundamentRequest;
        return this;
    }

    public void setRecountFundamentRequest(String recountFundamentRequest) {
        this.recountFundamentRequest = recountFundamentRequest;
    }

    public Boolean isPublished() {
        return published;
    }

    public Election published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Election createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public Election updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public ElectionType getElectionType() {
        return electionType;
    }

    public Election electionType(ElectionType electionType) {
        this.electionType = electionType;
        return this;
    }

    public void setElectionType(ElectionType electionType) {
        this.electionType = electionType;
    }

    public PoliticalParty getPoliticalPartyAsociated() {
        return politicalPartyAsociated;
    }

    public Election politicalPartyAsociated(PoliticalParty politicalParty) {
        this.politicalPartyAsociated = politicalParty;
        return this;
    }

    public void setPoliticalPartyAsociated(PoliticalParty politicalParty) {
        this.politicalPartyAsociated = politicalParty;
    }

    public Coalition getCoalitionAsociated() {
        return coalitionAsociated;
    }

    public Election coalitionAsociated(Coalition coalition) {
        this.coalitionAsociated = coalition;
        return this;
    }

    public void setCoalitionAsociated(Coalition coalition) {
        this.coalitionAsociated = coalition;
    }

    public IndependentCandidate getIndependentCandidateAsociated() {
        return independentCandidateAsociated;
    }

    public Election independentCandidateAsociated(IndependentCandidate independentCandidate) {
        this.independentCandidateAsociated = independentCandidate;
        return this;
    }

    public void setIndependentCandidateAsociated(IndependentCandidate independentCandidate) {
        this.independentCandidateAsociated = independentCandidate;
    }

    public Set<PoliticalParty> getPoliticalParties() {
        return politicalParties;
    }

    public Election politicalParties(Set<PoliticalParty> politicalParties) {
        this.politicalParties = politicalParties;
        return this;
    }

    public Election addPoliticalParties(PoliticalParty politicalParty) {
        this.politicalParties.add(politicalParty);
        return this;
    }

    public Election removePoliticalParties(PoliticalParty politicalParty) {
        this.politicalParties.remove(politicalParty);
        return this;
    }

    public void setPoliticalParties(Set<PoliticalParty> politicalParties) {
        this.politicalParties = politicalParties;
    }

    public Set<IndependentCandidate> getIndependentCandidates() {
        return independentCandidates;
    }

    public Election independentCandidates(Set<IndependentCandidate> independentCandidates) {
        this.independentCandidates = independentCandidates;
        return this;
    }

    public Election addIndependentCandidates(IndependentCandidate independentCandidate) {
        this.independentCandidates.add(independentCandidate);
        return this;
    }

    public Election removeIndependentCandidates(IndependentCandidate independentCandidate) {
        this.independentCandidates.remove(independentCandidate);
        return this;
    }

    public void setIndependentCandidates(Set<IndependentCandidate> independentCandidates) {
        this.independentCandidates = independentCandidates;
    }

    public Set<Coalition> getCoalitions() {
        return coalitions;
    }

    public Election coalitions(Set<Coalition> coalitions) {
        this.coalitions = coalitions;
        return this;
    }

    public Election addCoalitions(Coalition coalition) {
        this.coalitions.add(coalition);
        return this;
    }

    public Election removeCoalitions(Coalition coalition) {
        this.coalitions.remove(coalition);
        return this;
    }

    public void setCoalitions(Set<Coalition> coalitions) {
        this.coalitions = coalitions;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Election users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Election addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Election removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
        Election election = (Election) o;
        if (election.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), election.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Election{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", periodElection='" + getPeriodElection() + "'" +
            ", dateElection='" + getDateElection() + "'" +
            ", status='" + getStatus() + "'" +
            ", dataBase='" + getDataBase() + "'" +
            ", insetUrl='" + getInsetUrl() + "'" +
            ", incidentSheet='" + getIncidentSheet() + "'" +
            ", dayRecord='" + getDayRecord() + "'" +
            ", demandTemplateUrl='" + getDemandTemplateUrl() + "'" +
            ", recountTemplateUrl='" + getRecountTemplateUrl() + "'" +
            ", recountDistrictsRule='" + getRecountDistrictsRule() + "'" +
            ", recountPollingPlaceRule='" + getRecountPollingPlaceRule() + "'" +
            ", nameDemandant='" + getNameDemandant() + "'" +
            ", recountElectoralInstitute='" + getRecountElectoralInstitute() + "'" +
            ", recountType='" + getRecountType() + "'" +
            ", recountFundamentRequest='" + getRecountFundamentRequest() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
