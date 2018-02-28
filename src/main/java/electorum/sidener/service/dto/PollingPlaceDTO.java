package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import electorum.sidener.domain.enumeration.TypePollingPlace;

/**
 * A DTO for the PollingPlace entity.
 */
public class PollingPlaceDTO implements Serializable {

    private Long id;

    private String town;

    private TypePollingPlace typePollingPlace;

    private String typeNumber;

    private String section;

    @Lob
    private String address;

    private Long leftoverBallots;

    private Long votingCitizens;

    private Long exctractedBallots;

    private Long notRegistered;

    private Long nullVotes;

    private Long totalVotes;

    private Long electoralRoll;

    @Lob
    private String observations;

    private String president;

    private String secretary;

    private String scrutineerOne;

    private String scrutineerTwo;

    private String alternateOne;

    private String alternateTwo;

    private String alternateThree;

    private String entityFirstPlace;

    private Long totalFirstPlace;

    private String entitySecondPlace;

    private Long totalSecondPlace;

    private String urlRecordCount;

    private Boolean pollingPlaceWon;

    private Boolean published;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;

    private Long recordCountId;

    private String recordCountPath;

    private Long electionId;

    private Long districtId;

    private Set<CausalDTO> causals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public TypePollingPlace getTypePollingPlace() {
        return typePollingPlace;
    }

    public void setTypePollingPlace(TypePollingPlace typePollingPlace) {
        this.typePollingPlace = typePollingPlace;
    }

    public String getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public String getScrutineerOne() {
        return scrutineerOne;
    }

    public void setScrutineerOne(String scrutineerOne) {
        this.scrutineerOne = scrutineerOne;
    }

    public String getScrutineerTwo() {
        return scrutineerTwo;
    }

    public void setScrutineerTwo(String scrutineerTwo) {
        this.scrutineerTwo = scrutineerTwo;
    }

    public String getAlternateOne() {
        return alternateOne;
    }

    public void setAlternateOne(String alternateOne) {
        this.alternateOne = alternateOne;
    }

    public String getAlternateTwo() {
        return alternateTwo;
    }

    public void setAlternateTwo(String alternateTwo) {
        this.alternateTwo = alternateTwo;
    }

    public String getAlternateThree() {
        return alternateThree;
    }

    public void setAlternateThree(String alternateThree) {
        this.alternateThree = alternateThree;
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

    public String getUrlRecordCount() {
        return urlRecordCount;
    }

    public void setUrlRecordCount(String urlRecordCount) {
        this.urlRecordCount = urlRecordCount;
    }

    public Boolean isPollingPlaceWon() {
        return pollingPlaceWon;
    }

    public void setPollingPlaceWon(Boolean pollingPlaceWon) {
        this.pollingPlaceWon = pollingPlaceWon;
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

    public Long getRecordCountId() {
        return recordCountId;
    }

    public void setRecordCountId(Long archiveId) {
        this.recordCountId = archiveId;
    }

    public String getRecordCountPath() {
        return recordCountPath;
    }

    public void setRecordCountPath(String archivePath) {
        this.recordCountPath = archivePath;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
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
            ", town='" + getTown() + "'" +
            ", typePollingPlace='" + getTypePollingPlace() + "'" +
            ", typeNumber='" + getTypeNumber() + "'" +
            ", section='" + getSection() + "'" +
            ", address='" + getAddress() + "'" +
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
            ", entityFirstPlace='" + getEntityFirstPlace() + "'" +
            ", totalFirstPlace='" + getTotalFirstPlace() + "'" +
            ", entitySecondPlace='" + getEntitySecondPlace() + "'" +
            ", totalSecondPlace='" + getTotalSecondPlace() + "'" +
            ", urlRecordCount='" + getUrlRecordCount() + "'" +
            ", pollingPlaceWon='" + isPollingPlaceWon() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
