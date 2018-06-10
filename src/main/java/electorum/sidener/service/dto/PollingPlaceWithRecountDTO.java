package electorum.sidener.service.dto;

import electorum.sidener.domain.enumeration.TypePollingPlace;

public class PollingPlaceWithRecountDTO {
    private Long id;
    private Long section;
    private TypePollingPlace typePollingPlace;
    private String typeNumber;
    private Long nullVotes;
    private Long DiferenceFirstSecond;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSection() {
        return section;
    }

    public void setSection(Long section) {
        this.section = section;
    }

    public void setTypePollingPlace(TypePollingPlace typePollingPlace) {
        this.typePollingPlace = typePollingPlace;
    }

    public TypePollingPlace getTypePollingPlace() {
        return typePollingPlace;
    }


    public String getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }

    public Long getNullVotes() {
        return nullVotes;
    }

    public void setNullVotes(Long nullVotes) {
        this.nullVotes = nullVotes;
    }

    public Long getDiferenceFirstSecond() {
        return DiferenceFirstSecond;
    }

    public void setDiferenceFirstSecond(Long diferenceFirstSecond) {
        DiferenceFirstSecond = diferenceFirstSecond;
    }
}
