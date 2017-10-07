package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import electorum.sidener.domain.enumeration.State;
import electorum.sidener.domain.enumeration.Status;
import electorum.sidener.domain.enumeration.RecountDistrictsRule;
import electorum.sidener.domain.enumeration.RecountPollingPlaceRule;

/**
 * A DTO for the Election entity.
 */
public class ElectionDTO implements Serializable {

    private Long id;

    private State state;

    private String location;

    private ZonedDateTime date;

    private Status status;

    private String prepUrl;

    private String ballotUrl;

    private String insetUrl;

    private String demandTemplateUrl;

    private String recountTemplateUrl;

    private RecountDistrictsRule recountDistrictsRule;

    private RecountPollingPlaceRule recountPollingPlaceRule;

    private Boolean published;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;

    private Long electionTypeId;

    private String electionTypeName;

    private Long electionPeriodId;

    private String electionPeriodName;

    private Long politicalPartyAsociatedId;

    private Long coalitionAsociatedId;

    private Long independentCandidateAsociatedId;

    private Set<PoliticalPartyDTO> politicalParties = new HashSet<>();

    private Set<IndependentCandidateDTO> independentCandidates = new HashSet<>();

    private Set<CoalitionDTO> coalitions = new HashSet<>();

    private Set<CausalDTO> causals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPrepUrl() {
        return prepUrl;
    }

    public void setPrepUrl(String prepUrl) {
        this.prepUrl = prepUrl;
    }

    public String getBallotUrl() {
        return ballotUrl;
    }

    public void setBallotUrl(String ballotUrl) {
        this.ballotUrl = ballotUrl;
    }

    public String getInsetUrl() {
        return insetUrl;
    }

    public void setInsetUrl(String insetUrl) {
        this.insetUrl = insetUrl;
    }

    public String getDemandTemplateUrl() {
        return demandTemplateUrl;
    }

    public void setDemandTemplateUrl(String demandTemplateUrl) {
        this.demandTemplateUrl = demandTemplateUrl;
    }

    public String getRecountTemplateUrl() {
        return recountTemplateUrl;
    }

    public void setRecountTemplateUrl(String recountTemplateUrl) {
        this.recountTemplateUrl = recountTemplateUrl;
    }

    public RecountDistrictsRule getRecountDistrictsRule() {
        return recountDistrictsRule;
    }

    public void setRecountDistrictsRule(RecountDistrictsRule recountDistrictsRule) {
        this.recountDistrictsRule = recountDistrictsRule;
    }

    public RecountPollingPlaceRule getRecountPollingPlaceRule() {
        return recountPollingPlaceRule;
    }

    public void setRecountPollingPlaceRule(RecountPollingPlaceRule recountPollingPlaceRule) {
        this.recountPollingPlaceRule = recountPollingPlaceRule;
    }

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getElectionTypeId() {
        return electionTypeId;
    }

    public void setElectionTypeId(Long electionTypeId) {
        this.electionTypeId = electionTypeId;
    }

    public String getElectionTypeName() {
        return electionTypeName;
    }

    public void setElectionTypeName(String electionTypeName) {
        this.electionTypeName = electionTypeName;
    }

    public Long getElectionPeriodId() {
        return electionPeriodId;
    }

    public void setElectionPeriodId(Long electionPeriodId) {
        this.electionPeriodId = electionPeriodId;
    }

    public String getElectionPeriodName() {
        return electionPeriodName;
    }

    public void setElectionPeriodName(String electionPeriodName) {
        this.electionPeriodName = electionPeriodName;
    }

    public Long getPoliticalPartyAsociatedId() {
        return politicalPartyAsociatedId;
    }

    public void setPoliticalPartyAsociatedId(Long politicalPartyId) {
        this.politicalPartyAsociatedId = politicalPartyId;
    }

    public Long getCoalitionAsociatedId() {
        return coalitionAsociatedId;
    }

    public void setCoalitionAsociatedId(Long coalitionId) {
        this.coalitionAsociatedId = coalitionId;
    }

    public Long getIndependentCandidateAsociatedId() {
        return independentCandidateAsociatedId;
    }

    public void setIndependentCandidateAsociatedId(Long independentCandidateId) {
        this.independentCandidateAsociatedId = independentCandidateId;
    }

    public Set<PoliticalPartyDTO> getPoliticalParties() {
        return politicalParties;
    }

    public void setPoliticalParties(Set<PoliticalPartyDTO> politicalParties) {
        this.politicalParties = politicalParties;
    }

    public Set<IndependentCandidateDTO> getIndependentCandidates() {
        return independentCandidates;
    }

    public void setIndependentCandidates(Set<IndependentCandidateDTO> independentCandidates) {
        this.independentCandidates = independentCandidates;
    }

    public Set<CoalitionDTO> getCoalitions() {
        return coalitions;
    }

    public void setCoalitions(Set<CoalitionDTO> coalitions) {
        this.coalitions = coalitions;
    }

    public Set<CausalDTO> getCausals() {
        return causals;
    }

    public void setCausals(Set<CausalDTO> causals) {
        this.causals = causals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ElectionDTO electionDTO = (ElectionDTO) o;
        if(electionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), electionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ElectionDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", location='" + getLocation() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", prepUrl='" + getPrepUrl() + "'" +
            ", ballotUrl='" + getBallotUrl() + "'" +
            ", insetUrl='" + getInsetUrl() + "'" +
            ", demandTemplateUrl='" + getDemandTemplateUrl() + "'" +
            ", recountTemplateUrl='" + getRecountTemplateUrl() + "'" +
            ", recountDistrictsRule='" + getRecountDistrictsRule() + "'" +
            ", recountPollingPlaceRule='" + getRecountPollingPlaceRule() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
