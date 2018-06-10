package electorum.sidener.service.dto;
import java.util.List;


public class CausalPollingRelationDTO {
    //        List<DistrictDTO> districtDTOList = new ArrayList<DistrictDTO>();
    private Long id;
    private List<PollingPlaceDTO> pollingPlaceDTOList;
    private String nombreCausal;
    private String typeNumber;

    public String getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PollingPlaceDTO> getPollingPlaceDTOList() {
        return pollingPlaceDTOList;
    }

    public void setPollingPlaceDTOList(List<PollingPlaceDTO> pollingPlaceDTOList) {
        this.pollingPlaceDTOList = pollingPlaceDTOList;
    }

    public String getNombreCausal() {
        return nombreCausal;
    }

    public void setNombreCausal(String nombreCausal) {
        this.nombreCausal = nombreCausal;
    }

    @Override
    public String toString() {
        return "CausalPollingRelationDTO{" +
            "id=" + id +
            ", nombreCausal='" + nombreCausal + '\'' +
            '}';
    }
}
