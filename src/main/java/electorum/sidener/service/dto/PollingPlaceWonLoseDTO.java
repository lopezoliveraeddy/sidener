package electorum.sidener.service.dto;

import java.io.Serializable;

public class PollingPlaceWonLoseDTO implements Serializable {

    private Long pollingPlacesWon;
    private Long pollingPlacesLose;

    public Long getPollingPlacesWon() { return pollingPlacesWon; }
    public void setPollingPlacesWon(Long pollingPlaceWon) { this.pollingPlacesWon = pollingPlaceWon; }

    public Long getPollingPlacesLose() { return pollingPlacesLose; }
    public void setPollingPlacesLose(Long pollingPlacesLose) { this.pollingPlacesLose = pollingPlacesLose; }

    public String toString() {
        return "DistrictRecountDTO{" +
            "pollingPlacesWon=" + getPollingPlacesWon() +
            "pollingPlacesLose=" + getPollingPlacesLose() +
            "}";
    }
}
