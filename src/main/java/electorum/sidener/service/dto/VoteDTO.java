package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Vote entity.
 */
public class VoteDTO implements Serializable {

    private Long id;

    private Long total;

    private Boolean published;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;

    private Long electionId;

    private Long politicalPartyId;

    private String politicalPartyName;

    private Long independentCandidateId;

    private String independentCandidateName;

    private Long coalitionId;

    private String coalitionName;

    private Long pollingPlaceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
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

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getPoliticalPartyId() {
        return politicalPartyId;
    }

    public void setPoliticalPartyId(Long politicalPartyId) {
        this.politicalPartyId = politicalPartyId;
    }

    public String getPoliticalPartyName() {
        return politicalPartyName;
    }

    public void setPoliticalPartyName(String politicalPartyName) {
        this.politicalPartyName = politicalPartyName;
    }

    public Long getIndependentCandidateId() {
        return independentCandidateId;
    }

    public void setIndependentCandidateId(Long independentCandidateId) {
        this.independentCandidateId = independentCandidateId;
    }

    public String getIndependentCandidateName() {
        return independentCandidateName;
    }

    public void setIndependentCandidateName(String independentCandidateName) {
        this.independentCandidateName = independentCandidateName;
    }

    public Long getCoalitionId() {
        return coalitionId;
    }

    public void setCoalitionId(Long coalitionId) {
        this.coalitionId = coalitionId;
    }

    public String getCoalitionName() {
        return coalitionName;
    }

    public void setCoalitionName(String coalitionName) {
        this.coalitionName = coalitionName;
    }

    public Long getPollingPlaceId() {
        return pollingPlaceId;
    }

    public void setPollingPlaceId(Long pollingPlaceId) {
        this.pollingPlaceId = pollingPlaceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VoteDTO voteDTO = (VoteDTO) o;
        if(voteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), voteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
            "id=" + getId() +
            ", total='" + getTotal() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
