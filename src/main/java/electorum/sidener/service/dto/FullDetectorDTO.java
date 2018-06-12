package electorum.sidener.service.dto;

public class FullDetectorDTO {
    private Long id;
    private PollingPlaceDTO pollingPlaceDTO;
    private CausalDTO causalDTO;
    private String observations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PollingPlaceDTO getPollingPlaceDTO() {
        return pollingPlaceDTO;
    }

    public void setPollingPlaceDTO(PollingPlaceDTO pollingPlaceDTO) {
        this.pollingPlaceDTO = pollingPlaceDTO;
    }

    public CausalDTO getCausalDTO() {
        return causalDTO;
    }

    public void setCausalDTO(CausalDTO causalDTO) {
        this.causalDTO = causalDTO;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
