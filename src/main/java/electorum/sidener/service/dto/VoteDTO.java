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

    private ZonedDateTime created;

    private ZonedDateTime updated;

    private Long politicalPartyId;

    private String politicalPartyName;

    private Long independentCandidateId;

    private String independentCandidateName;

    private Long coalitionId;

    private String coalitionName;

    private Long pollingPlaceId;

    private String pollingPlaceName;

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

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
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

    public String getPollingPlaceName() {
        return pollingPlaceName;
    }

    public void setPollingPlaceName(String pollingPlaceName) {
        this.pollingPlaceName = pollingPlaceName;
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
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
