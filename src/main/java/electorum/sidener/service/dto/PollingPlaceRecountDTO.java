package electorum.sidener.service.dto;

/**
 * A Extend DTO for the PollingPlace entity.
 */

public class PollingPlaceRecountDTO extends PollingPlaceDTO {

    private Long difference;

    private Double percentageFirstPlace;

    private Double percentageSecondPlace;

    private Boolean countingAssumption;

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
            ", address='" + getAddress() + "'" +
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
            ", difference='" + getDifference() + "'" +
            ", percentageFirstPlace='" + getPercentageFirstPlace() + "'" +
            ", percentageSecondPlace='" + getPercentageSecondPlace() + "'" +
            ", countingAssumption='" + getCountingAssumption() + "'" +
            "}";
    }
}
