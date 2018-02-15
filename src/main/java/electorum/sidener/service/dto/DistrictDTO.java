package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import electorum.sidener.domain.enumeration.State;

/**
 * A DTO for the District entity.
 */
public class DistrictDTO implements Serializable {

    private Long id;

    private Long decimalNumber;

    private String romanNumber;

    private String districtHead;

    private State state;

    private String entityFirstPlace;

    private Long totalFirstPlace;

    private String entitySecondPlace;

    private Long totalSecondPlace;

    private Long totalVotes;

    private Long electoralRoll;

    private Long totalPollingPlaces;

    private Long nullVotes;

    private Boolean published;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;

    private Long electionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDecimalNumber() {
        return decimalNumber;
    }

    public void setDecimalNumber(Long decimalNumber) {
        this.decimalNumber = decimalNumber;
    }

    public String getRomanNumber() {
        return romanNumber;
    }

    public void setRomanNumber(String romanNumber) {
        this.romanNumber = romanNumber;
    }

    public String getDistrictHead() {
        return districtHead;
    }

    public void setDistrictHead(String districtHead) {
        this.districtHead = districtHead;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getEntityFirstPlace() {
        return entityFirstPlace;
    }

    public void setEntityFirstPlace(String entityFirstPlace) {
        this.entityFirstPlace = entityFirstPlace;
    }

    public Long getTotalFirstPlace() {
        return totalFirstPlace;
    }

    public void setTotalFirstPlace(Long totalFirstPlace) {
        this.totalFirstPlace = totalFirstPlace;
    }

    public String getEntitySecondPlace() {
        return entitySecondPlace;
    }

    public void setEntitySecondPlace(String entitySecondPlace) {
        this.entitySecondPlace = entitySecondPlace;
    }

    public Long getTotalSecondPlace() {
        return totalSecondPlace;
    }

    public void setTotalSecondPlace(Long totalSecondPlace) {
        this.totalSecondPlace = totalSecondPlace;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Long getElectoralRoll() {
        return electoralRoll;
    }

    public void setElectoralRoll(Long electoralRoll) {
        this.electoralRoll = electoralRoll;
    }

    public Long getTotalPollingPlaces() {
        return totalPollingPlaces;
    }

    public void setTotalPollingPlaces(Long totalPollingPlaces) {
        this.totalPollingPlaces = totalPollingPlaces;
    }

    public Long getNullVotes() {
        return nullVotes;
    }

    public void setNullVotes(Long nullVotes) {
        this.nullVotes = nullVotes;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DistrictDTO districtDTO = (DistrictDTO) o;
        if(districtDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), districtDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DistrictDTO{" +
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
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
