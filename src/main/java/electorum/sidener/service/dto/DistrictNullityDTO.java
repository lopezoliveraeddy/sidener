package electorum.sidener.service.dto;

/**
 * A Extend DTO for the District entity.
 */
public class DistrictNullityDTO extends DistrictDTO {

    @Override
    public String toString() {
        return "DistrictNullityDTO{" +
            "id=" + getId() +
            ", decimalNumber='" + getDecimalNumber() + "'" +
            ", romanNumber='" + getRomanNumber() + "'" +
            ", districtHead='" + getDistrictHead() + "'" +
            ", state='" + getState() + "'" +
            ", totalVotes='" + getTotalVotes() + "'" +
            ", totalPollingPlaces='" + getTotalPollingPlaces() + "'" +
            ", nullVotes='" + getNullVotes() + "'" +
            "}";
    }
}
