package electorum.sidener.service.dto;

import java.io.Serializable;

public class DistrictWonLoseDTO implements Serializable {

    private Long districtsWon;
    private Long districtsLose;

    public Long getDistrictsWon() { return districtsWon; }
    public void setDistrictsWon(Long districtsWon) { this.districtsWon = districtsWon; }

    public Long getDistrictsLose() { return districtsLose; }
    public void setDistrictsLose(Long districtsLose) { this.districtsLose = districtsLose; }

    public String toString() {
        return "DistrictRecountDTO{" +
            "districtsWon=" + getDistrictsWon() +
            "districtsLose=" + getDistrictsLose() +
            "}";
    }
}
