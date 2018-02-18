package electorum.sidener.service.dto;

import electorum.sidener.domain.enumeration.TypePollingPlace;
import electorum.sidener.service.mapper.CausalReducedMapper;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Extend DTO for the PollingPlace entity.
 */

public class PollingPlaceRecountDTO implements Serializable {


    private Long id;
    private String town;
    private TypePollingPlace typePollingPlace;
    private String typeNumber;
    private String section;
    private Long nullVotes;
    private Long totalVotes;
    private Long electoralRoll;
    private String entityFirstPlace;
    private Long totalFirstPlace;
    private String entitySecondPlace;
    private Long totalSecondPlace;
    private String urlRecordCount;

    private Long electionId;
    private Long districtId;

    private Long difference;
    private Double percentageFirstPlace;
    private Double percentageSecondPlace;
    private Boolean countingAssumption;

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
    public TypePollingPlace getTypePollingPlace() { return typePollingPlace; }
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
    public String getEntityFirstPlace() {
        return entityFirstPlace;
    }
    public void setEntityFirstPlace(String entityFirstPlace) {
        this.entityFirstPlace = entityFirstPlace;
    }
    public Long getTotalFirstPlace() {
        return totalFirstPlace;
    }
    public void setTotalFirstPlace(Long totalFirstPlace) { this.totalFirstPlace = totalFirstPlace; }
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

    public Long getDifference() {
        return difference;
    }
    public void setDifference(Long difference) {
        this.difference = difference;
    }
    public Double getPercentageFirstPlace() { return percentageFirstPlace; }
    public void setPercentageFirstPlace(Double percentageFirstPlace) { this.percentageFirstPlace = percentageFirstPlace; }
    public Double getPercentageSecondPlace() { return percentageSecondPlace; }
    public void setPercentageSecondPlace(Double percentageSecondPlace) { this.percentageSecondPlace = percentageSecondPlace; }
    public Boolean getCountingAssumption() { return countingAssumption; }
    public void setCountingAssumption(Boolean countingAssumption) { this.countingAssumption = countingAssumption; }


    @Override
    public String toString() {
        return "PollingPlaceDTO{" +
            "id=" + getId() +
            ", town='" + getTown() + "'" +
            ", typePollingPlace='" + getTypePollingPlace() + "'" +
            ", typeNumber='" + getTypeNumber() + "'" +
            ", section='" + getSection() + "'" +
            ", nullVotes='" + getNullVotes() + "'" +
            ", totalVotes='" + getTotalVotes() + "'" +
            ", electoralRoll='" + getElectoralRoll() + "'" +
            ", entityFirstPlace='" + getEntityFirstPlace() + "'" +
            ", totalFirstPlace='" + getTotalFirstPlace() + "'" +
            ", entitySecondPlace='" + getEntitySecondPlace() + "'" +
            ", totalSecondPlace='" + getTotalSecondPlace() + "'" +
            ", urlRecordCount='" + getUrlRecordCount() + "'" +
            ", difference='" + getDifference() + "'" +
            ", percentageFirstPlace='" + getPercentageFirstPlace() + "'" +
            ", percentageSecondPlace='" + getPercentageSecondPlace() + "'" +
            ", countingAssumption='" + getCountingAssumption() + "'" +
            "}";
    }
}
