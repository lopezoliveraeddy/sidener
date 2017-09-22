package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import electorum.sidener.domain.enumeration.PollingPlaceType;

/**
 * A DTO for the PollingPlace entity.
 */
public class PollingPlaceDTO implements Serializable {

    private Long id;

    private String name;

    private PollingPlaceType type;

    @Lob
    private String adress;

    private Long leftoverBallots;

    private Long votingCitizens;

    private Long exctractedBallots;

    private Long notRegistered;

    private Long nullVotes;

    private Long totalVotes;

    private Long electoralRoll;

    @Lob
    private String observations;

    private Boolean published;

    private ZonedDateTime created;

    private ZonedDateTime updated;

    private Long districtId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PollingPlaceType getType() {
        return type;
    }

    public void setType(PollingPlaceType type) {
        this.type = type;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Long getLeftoverBallots() {
        return leftoverBallots;
    }

    public void setLeftoverBallots(Long leftoverBallots) {
        this.leftoverBallots = leftoverBallots;
    }

    public Long getVotingCitizens() {
        return votingCitizens;
    }

    public void setVotingCitizens(Long votingCitizens) {
        this.votingCitizens = votingCitizens;
    }

    public Long getExctractedBallots() {
        return exctractedBallots;
    }

    public void setExctractedBallots(Long exctractedBallots) {
        this.exctractedBallots = exctractedBallots;
    }

    public Long getNotRegistered() {
        return notRegistered;
    }

    public void setNotRegistered(Long notRegistered) {
        this.notRegistered = notRegistered;
    }

    public Long getNullVotes() {
        return nullVotes;
    }

    public void setNullVotes(Long nullVotes) {
        this.nullVotes = nullVotes;
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

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PollingPlaceDTO pollingPlaceDTO = (PollingPlaceDTO) o;
        if(pollingPlaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pollingPlaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PollingPlaceDTO{" +
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
