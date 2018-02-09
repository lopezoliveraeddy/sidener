package electorum.sidener.service.dto;

/**
 * A Extend DTO for the District entity.
 */
public class DistrictExtDTO extends DistrictDTO {

    private Long difference;

    private Double percentageDifference;

    private Double percentageFirstPlace;

    private Double percentageSecondPlace;

    private Double percentageTotalVotes;

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

    @Override
    public String toString() {
        return "DistrictExtDTO{" +
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
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", difference='" + getDifference() + "'" +
            ", percentageDifference='" + getPercentageDifference() + "'" +
            ", percentageFirstPlace='" + getPercentageFirstPlace() + "'" +
            ", percentageSecondPlace='" + getPercentageSecondPlace() + "'" +
            ", percentageTotalVotes='" + getPercentageTotalVotes() + "'" +
            "}";
    }
}
