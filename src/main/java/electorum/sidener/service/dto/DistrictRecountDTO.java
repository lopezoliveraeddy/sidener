package electorum.sidener.service.dto;

import java.io.Serializable;

/**
 * A Extend DTO for the District entity.
 */
public class DistrictRecountDTO implements Serializable {

    private Long id;
    private Long decimalNumber;
    private String romanNumber;
    private String districtHead;
    private String entityFirstPlace;
    private Long totalFirstPlace;
    private String entitySecondPlace;
    private Long totalSecondPlace;
    private Long totalVotes;
    private Long electoralRoll;
    private Boolean districtWon;
    private Long electionId;

    private Long difference;
    private Double percentageDifference;
    private Double percentageFirstPlace;
    private Double percentageSecondPlace;
    private Double percentageTotalVotes;
    private Boolean countingAssumption;

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
    public Boolean isDistrictWon() {
        return districtWon;
    }
    public void setDistrictWon(Boolean districtWon) {
        this.districtWon = districtWon;
    }

    public Long getElectionId() {
        return electionId;
    }
    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getDifference() {
        return difference;
    }
    public void setDifference(Long difference) {
        this.difference = difference;
    }
    public Double getPercentageDifference() { return percentageDifference; }
    public void setPercentageDifference(Double percentageDifference) { this.percentageDifference = percentageDifference; }
    public Double getPercentageFirstPlace() { return percentageFirstPlace; }
    public void setPercentageFirstPlace(Double percentageFirstPlace) { this.percentageFirstPlace = percentageFirstPlace; }
    public Double getPercentageSecondPlace() { return percentageSecondPlace; }
    public void setPercentageSecondPlace(Double percentageSecondPlace) { this.percentageSecondPlace = percentageSecondPlace; }
    public Double getPercentageTotalVotes() { return percentageTotalVotes; }
    public void setPercentageTotalVotes(Double percentageTotalVotes) { this.percentageTotalVotes = percentageTotalVotes; }
    public Boolean getCountingAssumption() { return countingAssumption; }
    public void setCountingAssumption(Boolean countingAssumption) { this.countingAssumption = countingAssumption; }

    public String toString() {
        return "DistrictRecountDTO{" +
            "id=" + getId() +
            ", decimalNumber='" + getDecimalNumber() + "'" +
            ", romanNumber='" + getRomanNumber() + "'" +
            ", districtHead='" + getDistrictHead() + "'" +
            ", entityFirstPlace='" + getEntityFirstPlace() + "'" +
            ", totalFirstPlace='" + getTotalFirstPlace() + "'" +
            ", entitySecondPlace='" + getEntitySecondPlace() + "'" +
            ", totalSecondPlace='" + getTotalSecondPlace() + "'" +
            ", totalVotes='" + getTotalVotes() + "'" +
            ", electoralRoll='" + getElectoralRoll() + "'" +
            ", districtWon='" + isDistrictWon() + "'" +
            ", difference='" + getDifference() + "'" +
            ", percentageDifference='" + getPercentageDifference() + "'" +
            ", percentageFirstPlace='" + getPercentageFirstPlace() + "'" +
            ", percentageSecondPlace='" + getPercentageSecondPlace() + "'" +
            ", percentageTotalVotes='" + getPercentageTotalVotes() + "'" +
            ", countingAssumption='" + getCountingAssumption() + "'" +
            "}";
    }
}
