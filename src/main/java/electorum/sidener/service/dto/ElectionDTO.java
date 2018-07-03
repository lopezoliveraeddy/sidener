package electorum.sidener.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import electorum.sidener.domain.enumeration.State;
import electorum.sidener.domain.enumeration.Status;
import electorum.sidener.domain.enumeration.RecountDistrictsRule;
import electorum.sidener.domain.enumeration.RecountPollingPlaceRule;

/**
 * A DTO for the Election entity.
 */
public class ElectionDTO implements Serializable {

    private Long id;

    private State state;

    private String periodElection;

    private ZonedDateTime dateElection;

    private Status status;
    //dataBase attributes
    @Lob
    private byte[] dbFile;
    private String dbFileContentType;
    // end database attributes

    //insertUrl attributes
    @Lob
    private byte[] iuFile;
	private String iuFileContentType;
    //end insertUrl attributes

	//incidentSheet attributes
	private byte[] isFile;
	private String isFileContentType;
    //end incidentSheet attributes

	//dayRecord attributes
	private byte[] drFile;
	private String drFileContentType;
    //end dayRecord attributes

	//demandTemplate attributes
	private byte[] dmFile;
	private String dmFileContentType;
    //end demandTemplate attributes

	//recountTemplate attributes
	private byte[] rtFile;
	private String rtFileContentType;
    //end recountTemplate attributes

    private String dataBase;

    private String insetUrl;

    private String incidentSheet;

    private String dayRecord;

    private String demandTemplateUrl;

    private String recountTemplateUrl;

    private RecountDistrictsRule recountDistrictsRule;

    private RecountPollingPlaceRule recountPollingPlaceRule;

    private String nameDemandant;

    private String recountElectoralInstitute;

    private String recountType;

    @Lob
    private String recountFundamentRequest;

    private Boolean published;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;

    private Long electionTypeId;

    private String electionTypeName;

    private Long politicalPartyAssociatedId;

    private String politicalPartyAssociatedName;

    private String politicalPartyAssociatedAcronym;

    private Long coalitionAssociatedId;

    private String coalitionAssociatedName;

    private String coalitionAssociatedAcronym;

    private Long independentCandidateAssociatedId;

    private String independentCandidateAssociatedName;

    private String independentCandidateAssociatedAcronym;

    private String address;

    private String councilPlace;

    private String authPeople;

    private String computeType;

    private String judgeType;

    private Set<PoliticalPartyDTO> politicalParties = new HashSet<>();

    private Set<IndependentCandidateDTO> independentCandidates = new HashSet<>();

    private Set<CoalitionDTO> coalitions = new HashSet<>();

    private Set<UserDTO> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPeriodElection() {
        return periodElection;
    }

    public void setPeriodElection(String periodElection) {
        this.periodElection = periodElection;
    }

    public ZonedDateTime getDateElection() {
        return dateElection;
    }

    public void setDateElection(ZonedDateTime dateElection) {
        this.dateElection = dateElection;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getInsetUrl() {
        return insetUrl;
    }

    public void setInsetUrl(String insetUrl) {
        this.insetUrl = insetUrl;
    }

    public String getIncidentSheet() {
        return incidentSheet;
    }

    public void setIncidentSheet(String incidentSheet) {
        this.incidentSheet = incidentSheet;
    }

    public String getDayRecord() {
        return dayRecord;
    }

    public void setDayRecord(String dayRecord) {
        this.dayRecord = dayRecord;
    }

    public String getDemandTemplateUrl() {
        return demandTemplateUrl;
    }

    public void setDemandTemplateUrl(String demandTemplateUrl) {
        this.demandTemplateUrl = demandTemplateUrl;
    }

    public String getRecountTemplateUrl() {
        return recountTemplateUrl;
    }

    public void setRecountTemplateUrl(String recountTemplateUrl) {
        this.recountTemplateUrl = recountTemplateUrl;
    }

    public RecountDistrictsRule getRecountDistrictsRule() {
        return recountDistrictsRule;
    }

    public void setRecountDistrictsRule(RecountDistrictsRule recountDistrictsRule) {
        this.recountDistrictsRule = recountDistrictsRule;
    }

    public RecountPollingPlaceRule getRecountPollingPlaceRule() {
        return recountPollingPlaceRule;
    }

    public void setRecountPollingPlaceRule(RecountPollingPlaceRule recountPollingPlaceRule) {
        this.recountPollingPlaceRule = recountPollingPlaceRule;
    }

    public String getNameDemandant() {
        return nameDemandant;
    }

    public void setNameDemandant(String nameDemandant) {
        this.nameDemandant = nameDemandant;
    }

    public String getRecountElectoralInstitute() {
        return recountElectoralInstitute;
    }

    public void setRecountElectoralInstitute(String recountElectoralInstitute) {
        this.recountElectoralInstitute = recountElectoralInstitute;
    }

    public String getRecountType() {
        return recountType;
    }

    public void setRecountType(String recountType) {
        this.recountType = recountType;
    }

    public String getRecountFundamentRequest() {
        return recountFundamentRequest;
    }

    public void setRecountFundamentRequest(String recountFundamentRequest) {
        this.recountFundamentRequest = recountFundamentRequest;
    }

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getElectionTypeId() {
        return electionTypeId;
    }

    public void setElectionTypeId(Long electionTypeId) {
        this.electionTypeId = electionTypeId;
    }

    public String getElectionTypeName() {
        return electionTypeName;
    }

    public void setElectionTypeName(String electionTypeName) {
        this.electionTypeName = electionTypeName;
    }

    public Long getPoliticalPartyAssociatedId() {
        return politicalPartyAssociatedId;
    }

    public void setPoliticalPartyAssociatedId(Long politicalPartyId) {
        this.politicalPartyAssociatedId = politicalPartyId;
    }

    public String getPoliticalPartyAssociatedName() {
        return politicalPartyAssociatedName;
    }

    public void setPoliticalPartyAssociatedName(String politicalPartyName) {
        this.politicalPartyAssociatedName = politicalPartyName;
    }

    public String getPoliticalPartyAssociatedAcronym() {
        return politicalPartyAssociatedAcronym;
    }

    public void setPoliticalPartyAssociatedAcronym(String politicalPartyAcronym) {
        this.politicalPartyAssociatedAcronym = politicalPartyAcronym;
    }

    public Long getCoalitionAssociatedId() {
        return coalitionAssociatedId;
    }

    public void setCoalitionAssociatedId(Long coalitionId) {
        this.coalitionAssociatedId = coalitionId;
    }

    public String getCoalitionAssociatedName() {
        return coalitionAssociatedName;
    }

    public void setCoalitionAssociatedName(String coalitionName) {
        this.coalitionAssociatedName = coalitionName;
    }

    public String getCoalitionAssociatedAcronym() {
        return coalitionAssociatedAcronym;
    }

    public void setCoalitionAssociatedAcronym(String coalitionAcronym) {
        this.coalitionAssociatedAcronym = coalitionAcronym;
    }

    public Long getIndependentCandidateAssociatedId() {
        return independentCandidateAssociatedId;
    }

    public void setIndependentCandidateAssociatedId(Long independentCandidateId) {
        this.independentCandidateAssociatedId = independentCandidateId;
    }

    public String getIndependentCandidateAssociatedAcronym() {
        return independentCandidateAssociatedAcronym;
    }

    public void setIndependentCandidateAssociatedAcronym(String independentCandidateAcronym) {
        this.independentCandidateAssociatedAcronym = independentCandidateAcronym;
    }

    public String getIndependentCandidateAssociatedName() {
        return independentCandidateAssociatedName;
    }

    public void setIndependentCandidateAssociatedName(String independentCandidateName) {
        this.independentCandidateAssociatedName = independentCandidateName;
    }

    public Set<PoliticalPartyDTO> getPoliticalParties() {
        return politicalParties;
    }

    public void setPoliticalParties(Set<PoliticalPartyDTO> politicalParties) {
        this.politicalParties = politicalParties;
    }

    public Set<IndependentCandidateDTO> getIndependentCandidates() {
        return independentCandidates;
    }

    public void setIndependentCandidates(Set<IndependentCandidateDTO> independentCandidates) {
        this.independentCandidates = independentCandidates;
    }

    public Set<CoalitionDTO> getCoalitions() {
        return coalitions;
    }

    public void setCoalitions(Set<CoalitionDTO> coalitions) {
        this.coalitions = coalitions;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }
    // start database setter and getter
    public byte[] getDbFile() {
		return dbFile;
	}

	public void setDbFile(byte[] dbFile) {
		this.dbFile = dbFile;
	}

	public String getDbFileContentType() {
		return dbFileContentType;
	}

	public void setDbFileContentType(String dbFileContentType) {
		this.dbFileContentType = dbFileContentType;
	}
	// end database setter and getter

	// start insetUrl
    public byte[] getIuFile() {
		return iuFile;
	}

	public void setIuFile(byte[] iuFile) {
		this.iuFile = iuFile;
	}

	public String getIuFileContentType() {
		return iuFileContentType;
	}

	public void setIuFileContentType(String iuFileContentType) {
		this.iuFileContentType = iuFileContentType;
	}
	// end insetUrl

	// start incidentSheet
	public byte[] getIsFile() {
		return isFile;
	}

	public void setIsFile(byte[] isFile) {
		this.isFile = isFile;
	}

	public String getIsFileContentType() {
		return isFileContentType;
	}

	public void setIsFileContentType(String isFileContentType) {
		this.isFileContentType = isFileContentType;
	}
	// end incidentSheet

    public byte[] getDrFile() {
		return drFile;
	}

	public void setDrFile(byte[] drFile) {
		this.drFile = drFile;
	}

	public String getDrFileContentType() {
		return drFileContentType;
	}

	public void setDrFileContentType(String drFileContentType) {
		this.drFileContentType = drFileContentType;
	}

	public byte[] getDmFile() {
		return dmFile;
	}

	public void setDmFile(byte[] dmFile) {
		this.dmFile = dmFile;
	}

	public String getDmFileContentType() {
		return dmFileContentType;
	}

	public void setDmFileContentType(String dmFileContentType) {
		this.dmFileContentType = dmFileContentType;
	}

	public byte[] getRtFile() {
		return rtFile;
	}

	public void setRtFile(byte[] rtFile) {
		this.rtFile = rtFile;
	}

	public String getRtFileContentType() {
		return rtFileContentType;
	}

	public void setRtFileContentType(String rtFileContentType) {
		this.rtFileContentType = rtFileContentType;
	}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getPublished() {
        return published;
    }

    public String getCouncilPlace() {
        return councilPlace;
    }

    public void setCouncilPlace(String councilPlace) {
        this.councilPlace = councilPlace;
    }

    public String getAuthPeople() {
        return authPeople;
    }

    public void setAuthPeople(String authPeople) {
        this.authPeople = authPeople;
    }

    public String getComputeType() {
        return computeType;
    }

    public void setComputeType(String computeType) {
        this.computeType = computeType;
    }

    public String getJudgeType() {
        return judgeType;
    }

    public void setJudgeType(String judgeType) {
        this.judgeType = judgeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ElectionDTO electionDTO = (ElectionDTO) o;
        if(electionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), electionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ElectionDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", periodElection='" + getPeriodElection() + "'" +
            ", dateElection='" + getDateElection() + "'" +
            ", status='" + getStatus() + "'" +
            ", dataBase='" + getDataBase() + "'" +
            ", insetUrl='" + getInsetUrl() + "'" +
            ", incidentSheet='" + getIncidentSheet() + "'" +
            ", dayRecord='" + getDayRecord() + "'" +
            ", demandTemplateUrl='" + getDemandTemplateUrl() + "'" +
            ", recountTemplateUrl='" + getRecountTemplateUrl() + "'" +
            ", recountDistrictsRule='" + getRecountDistrictsRule() + "'" +
            ", recountPollingPlaceRule='" + getRecountPollingPlaceRule() + "'" +
            ", nameDemandant='" + getNameDemandant() + "'" +
            ", recountElectoralInstitute='" + getRecountElectoralInstitute() + "'" +
            ", recountType='" + getRecountType() + "'" +
            ", recountFundamentRequest='" + getRecountFundamentRequest() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", address='" + getAddress() + "'" +
            ", councilPlace='" + getCouncilPlace() + "'" +
            ", authPeople='" + getAuthPeople() + "'" +
            ", computeType='" + getComputeType() + "'" +
            ", judgeType='" + getJudgeType() + "'" +
            "}";
    }
}
