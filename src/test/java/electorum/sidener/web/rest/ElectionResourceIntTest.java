package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.Election;
import electorum.sidener.repository.ElectionRepository;
import electorum.sidener.service.ElectionService;
import electorum.sidener.repository.search.ElectionSearchRepository;
import electorum.sidener.service.dto.ElectionDTO;
import electorum.sidener.service.mapper.ElectionMapper;
import electorum.sidener.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static electorum.sidener.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import electorum.sidener.domain.enumeration.State;
import electorum.sidener.domain.enumeration.Status;
import electorum.sidener.domain.enumeration.RecountDistrictsRule;
import electorum.sidener.domain.enumeration.RecountPollingPlaceRule;
/**
 * Test class for the ElectionResource REST controller.
 *
 * @see ElectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class ElectionResourceIntTest {

    private static final State DEFAULT_STATE = State.AGU;
    private static final State UPDATED_STATE = State.BCN;

    private static final String DEFAULT_PERIOD_ELECTION = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD_ELECTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_ELECTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ELECTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.IN_PROGRESS;

    private static final String DEFAULT_DATA_BASE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_BASE = "BBBBBBBBBB";

    private static final String DEFAULT_INSET_URL = "AAAAAAAAAA";
    private static final String UPDATED_INSET_URL = "BBBBBBBBBB";

    private static final String DEFAULT_INCIDENT_SHEET = "AAAAAAAAAA";
    private static final String UPDATED_INCIDENT_SHEET = "BBBBBBBBBB";

    private static final String DEFAULT_DAY_RECORD = "AAAAAAAAAA";
    private static final String UPDATED_DAY_RECORD = "BBBBBBBBBB";

    private static final String DEFAULT_DEMAND_TEMPLATE_URL = "AAAAAAAAAA";
    private static final String UPDATED_DEMAND_TEMPLATE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_RECOUNT_TEMPLATE_URL = "AAAAAAAAAA";
    private static final String UPDATED_RECOUNT_TEMPLATE_URL = "BBBBBBBBBB";

    private static final RecountDistrictsRule DEFAULT_RECOUNT_DISTRICTS_RULE = RecountDistrictsRule.LESS_1;
    private static final RecountDistrictsRule UPDATED_RECOUNT_DISTRICTS_RULE = RecountDistrictsRule.LESS_EQUAL_1;

    private static final RecountPollingPlaceRule DEFAULT_RECOUNT_POLLING_PLACE_RULE = RecountPollingPlaceRule.LESS_1;
    private static final RecountPollingPlaceRule UPDATED_RECOUNT_POLLING_PLACE_RULE = RecountPollingPlaceRule.LESS_EQUAL_1;

    private static final String DEFAULT_NAME_DEMANDANT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DEMANDANT = "BBBBBBBBBB";

    private static final String DEFAULT_RECOUNT_ELECTORAL_INSTITUTE = "AAAAAAAAAA";
    private static final String UPDATED_RECOUNT_ELECTORAL_INSTITUTE = "BBBBBBBBBB";

    private static final String DEFAULT_RECOUNT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RECOUNT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RECOUNT_FUNDAMENT_REQUEST = "AAAAAAAAAA";
    private static final String UPDATED_RECOUNT_FUNDAMENT_REQUEST = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private ElectionMapper electionMapper;

    @Autowired
    private ElectionService electionService;

    @Autowired
    private ElectionSearchRepository electionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restElectionMockMvc;

    private Election election;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ElectionResource electionResource = new ElectionResource(electionService);
        this.restElectionMockMvc = MockMvcBuilders.standaloneSetup(electionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Election createEntity(EntityManager em) {
        Election election = new Election()
            .state(DEFAULT_STATE)
            .periodElection(DEFAULT_PERIOD_ELECTION)
            .dateElection(DEFAULT_DATE_ELECTION)
            .status(DEFAULT_STATUS)
            .dataBase(DEFAULT_DATA_BASE)
            .insetUrl(DEFAULT_INSET_URL)
            .incidentSheet(DEFAULT_INCIDENT_SHEET)
            .dayRecord(DEFAULT_DAY_RECORD)
            .demandTemplateUrl(DEFAULT_DEMAND_TEMPLATE_URL)
            .recountTemplateUrl(DEFAULT_RECOUNT_TEMPLATE_URL)
            .recountDistrictsRule(DEFAULT_RECOUNT_DISTRICTS_RULE)
            .recountPollingPlaceRule(DEFAULT_RECOUNT_POLLING_PLACE_RULE)
            .nameDemandant(DEFAULT_NAME_DEMANDANT)
            .recountElectoralInstitute(DEFAULT_RECOUNT_ELECTORAL_INSTITUTE)
            .recountType(DEFAULT_RECOUNT_TYPE)
            .recountFundamentRequest(DEFAULT_RECOUNT_FUNDAMENT_REQUEST)
            .published(DEFAULT_PUBLISHED)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .address(DEFAULT_ADDRESS);
        return election;
    }

    @Before
    public void initTest() {
        electionSearchRepository.deleteAll();
        election = createEntity(em);
    }

    @Test
    @Transactional
    public void createElection() throws Exception {
        int databaseSizeBeforeCreate = electionRepository.findAll().size();

        // Create the Election
        ElectionDTO electionDTO = electionMapper.toDto(election);
        restElectionMockMvc.perform(post("/api/elections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionDTO)))
            .andExpect(status().isCreated());

        // Validate the Election in the database
        List<Election> electionList = electionRepository.findAll();
        assertThat(electionList).hasSize(databaseSizeBeforeCreate + 1);
        Election testElection = electionList.get(electionList.size() - 1);
        assertThat(testElection.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testElection.getPeriodElection()).isEqualTo(DEFAULT_PERIOD_ELECTION);
        assertThat(testElection.getDateElection()).isEqualTo(DEFAULT_DATE_ELECTION);
        assertThat(testElection.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testElection.getDataBase()).isEqualTo(DEFAULT_DATA_BASE);
        assertThat(testElection.getInsetUrl()).isEqualTo(DEFAULT_INSET_URL);
        assertThat(testElection.getIncidentSheet()).isEqualTo(DEFAULT_INCIDENT_SHEET);
        assertThat(testElection.getDayRecord()).isEqualTo(DEFAULT_DAY_RECORD);
        assertThat(testElection.getDemandTemplateUrl()).isEqualTo(DEFAULT_DEMAND_TEMPLATE_URL);
        assertThat(testElection.getRecountTemplateUrl()).isEqualTo(DEFAULT_RECOUNT_TEMPLATE_URL);
        assertThat(testElection.getRecountDistrictsRule()).isEqualTo(DEFAULT_RECOUNT_DISTRICTS_RULE);
        assertThat(testElection.getRecountPollingPlaceRule()).isEqualTo(DEFAULT_RECOUNT_POLLING_PLACE_RULE);
        assertThat(testElection.getNameDemandant()).isEqualTo(DEFAULT_NAME_DEMANDANT);
        assertThat(testElection.getRecountElectoralInstitute()).isEqualTo(DEFAULT_RECOUNT_ELECTORAL_INSTITUTE);
        assertThat(testElection.getRecountType()).isEqualTo(DEFAULT_RECOUNT_TYPE);
        assertThat(testElection.getRecountFundamentRequest()).isEqualTo(DEFAULT_RECOUNT_FUNDAMENT_REQUEST);
        assertThat(testElection.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testElection.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testElection.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testElection.getAddress()).isEqualTo(DEFAULT_ADDRESS);

        // Validate the Election in Elasticsearch
        Election electionEs = electionSearchRepository.findOne(testElection.getId());
        assertThat(electionEs).isEqualToComparingFieldByField(testElection);
    }

    @Test
    @Transactional
    public void createElectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = electionRepository.findAll().size();

        // Create the Election with an existing ID
        election.setId(1L);
        ElectionDTO electionDTO = electionMapper.toDto(election);

        // An entity with an existing ID cannot be created, so this API call must fail
        restElectionMockMvc.perform(post("/api/elections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Election in the database
        List<Election> electionList = electionRepository.findAll();
        assertThat(electionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllElections() throws Exception {
        // Initialize the database
        electionRepository.saveAndFlush(election);

        // Get all the electionList
        restElectionMockMvc.perform(get("/api/elections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(election.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].periodElection").value(hasItem(DEFAULT_PERIOD_ELECTION.toString())))
            .andExpect(jsonPath("$.[*].dateElection").value(hasItem(sameInstant(DEFAULT_DATE_ELECTION))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dataBase").value(hasItem(DEFAULT_DATA_BASE.toString())))
            .andExpect(jsonPath("$.[*].insetUrl").value(hasItem(DEFAULT_INSET_URL.toString())))
            .andExpect(jsonPath("$.[*].incidentSheet").value(hasItem(DEFAULT_INCIDENT_SHEET.toString())))
            .andExpect(jsonPath("$.[*].dayRecord").value(hasItem(DEFAULT_DAY_RECORD.toString())))
            .andExpect(jsonPath("$.[*].demandTemplateUrl").value(hasItem(DEFAULT_DEMAND_TEMPLATE_URL.toString())))
            .andExpect(jsonPath("$.[*].recountTemplateUrl").value(hasItem(DEFAULT_RECOUNT_TEMPLATE_URL.toString())))
            .andExpect(jsonPath("$.[*].recountDistrictsRule").value(hasItem(DEFAULT_RECOUNT_DISTRICTS_RULE.toString())))
            .andExpect(jsonPath("$.[*].recountPollingPlaceRule").value(hasItem(DEFAULT_RECOUNT_POLLING_PLACE_RULE.toString())))
            .andExpect(jsonPath("$.[*].nameDemandant").value(hasItem(DEFAULT_NAME_DEMANDANT.toString())))
            .andExpect(jsonPath("$.[*].recountElectoralInstitute").value(hasItem(DEFAULT_RECOUNT_ELECTORAL_INSTITUTE.toString())))
            .andExpect(jsonPath("$.[*].recountType").value(hasItem(DEFAULT_RECOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].recountFundamentRequest").value(hasItem(DEFAULT_RECOUNT_FUNDAMENT_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getElection() throws Exception {
        // Initialize the database
        electionRepository.saveAndFlush(election);

        // Get the election
        restElectionMockMvc.perform(get("/api/elections/{id}", election.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(election.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.periodElection").value(DEFAULT_PERIOD_ELECTION.toString()))
            .andExpect(jsonPath("$.dateElection").value(sameInstant(DEFAULT_DATE_ELECTION)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dataBase").value(DEFAULT_DATA_BASE.toString()))
            .andExpect(jsonPath("$.insetUrl").value(DEFAULT_INSET_URL.toString()))
            .andExpect(jsonPath("$.incidentSheet").value(DEFAULT_INCIDENT_SHEET.toString()))
            .andExpect(jsonPath("$.dayRecord").value(DEFAULT_DAY_RECORD.toString()))
            .andExpect(jsonPath("$.demandTemplateUrl").value(DEFAULT_DEMAND_TEMPLATE_URL.toString()))
            .andExpect(jsonPath("$.recountTemplateUrl").value(DEFAULT_RECOUNT_TEMPLATE_URL.toString()))
            .andExpect(jsonPath("$.recountDistrictsRule").value(DEFAULT_RECOUNT_DISTRICTS_RULE.toString()))
            .andExpect(jsonPath("$.recountPollingPlaceRule").value(DEFAULT_RECOUNT_POLLING_PLACE_RULE.toString()))
            .andExpect(jsonPath("$.nameDemandant").value(DEFAULT_NAME_DEMANDANT.toString()))
            .andExpect(jsonPath("$.recountElectoralInstitute").value(DEFAULT_RECOUNT_ELECTORAL_INSTITUTE.toString()))
            .andExpect(jsonPath("$.recountType").value(DEFAULT_RECOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.recountFundamentRequest").value(DEFAULT_RECOUNT_FUNDAMENT_REQUEST.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingElection() throws Exception {
        // Get the election
        restElectionMockMvc.perform(get("/api/elections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElection() throws Exception {
        // Initialize the database
        electionRepository.saveAndFlush(election);
        electionSearchRepository.save(election);
        int databaseSizeBeforeUpdate = electionRepository.findAll().size();

        // Update the election
        Election updatedElection = electionRepository.findOne(election.getId());
        updatedElection
            .state(UPDATED_STATE)
            .periodElection(UPDATED_PERIOD_ELECTION)
            .dateElection(UPDATED_DATE_ELECTION)
            .status(UPDATED_STATUS)
            .dataBase(UPDATED_DATA_BASE)
            .insetUrl(UPDATED_INSET_URL)
            .incidentSheet(UPDATED_INCIDENT_SHEET)
            .dayRecord(UPDATED_DAY_RECORD)
            .demandTemplateUrl(UPDATED_DEMAND_TEMPLATE_URL)
            .recountTemplateUrl(UPDATED_RECOUNT_TEMPLATE_URL)
            .recountDistrictsRule(UPDATED_RECOUNT_DISTRICTS_RULE)
            .recountPollingPlaceRule(UPDATED_RECOUNT_POLLING_PLACE_RULE)
            .nameDemandant(UPDATED_NAME_DEMANDANT)
            .recountElectoralInstitute(UPDATED_RECOUNT_ELECTORAL_INSTITUTE)
            .recountType(UPDATED_RECOUNT_TYPE)
            .recountFundamentRequest(UPDATED_RECOUNT_FUNDAMENT_REQUEST)
            .published(UPDATED_PUBLISHED)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .address(UPDATED_ADDRESS);
        ElectionDTO electionDTO = electionMapper.toDto(updatedElection);

        restElectionMockMvc.perform(put("/api/elections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionDTO)))
            .andExpect(status().isOk());

        // Validate the Election in the database
        List<Election> electionList = electionRepository.findAll();
        assertThat(electionList).hasSize(databaseSizeBeforeUpdate);
        Election testElection = electionList.get(electionList.size() - 1);
        assertThat(testElection.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testElection.getPeriodElection()).isEqualTo(UPDATED_PERIOD_ELECTION);
        assertThat(testElection.getDateElection()).isEqualTo(UPDATED_DATE_ELECTION);
        assertThat(testElection.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testElection.getDataBase()).isEqualTo(UPDATED_DATA_BASE);
        assertThat(testElection.getInsetUrl()).isEqualTo(UPDATED_INSET_URL);
        assertThat(testElection.getIncidentSheet()).isEqualTo(UPDATED_INCIDENT_SHEET);
        assertThat(testElection.getDayRecord()).isEqualTo(UPDATED_DAY_RECORD);
        assertThat(testElection.getDemandTemplateUrl()).isEqualTo(UPDATED_DEMAND_TEMPLATE_URL);
        assertThat(testElection.getRecountTemplateUrl()).isEqualTo(UPDATED_RECOUNT_TEMPLATE_URL);
        assertThat(testElection.getRecountDistrictsRule()).isEqualTo(UPDATED_RECOUNT_DISTRICTS_RULE);
        assertThat(testElection.getRecountPollingPlaceRule()).isEqualTo(UPDATED_RECOUNT_POLLING_PLACE_RULE);
        assertThat(testElection.getNameDemandant()).isEqualTo(UPDATED_NAME_DEMANDANT);
        assertThat(testElection.getRecountElectoralInstitute()).isEqualTo(UPDATED_RECOUNT_ELECTORAL_INSTITUTE);
        assertThat(testElection.getRecountType()).isEqualTo(UPDATED_RECOUNT_TYPE);
        assertThat(testElection.getRecountFundamentRequest()).isEqualTo(UPDATED_RECOUNT_FUNDAMENT_REQUEST);
        assertThat(testElection.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testElection.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testElection.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testElection.getAddress()).isEqualTo(UPDATED_ADDRESS);

        // Validate the Election in Elasticsearch
        Election electionEs = electionSearchRepository.findOne(testElection.getId());
        assertThat(electionEs).isEqualToComparingFieldByField(testElection);
    }

    @Test
    @Transactional
    public void updateNonExistingElection() throws Exception {
        int databaseSizeBeforeUpdate = electionRepository.findAll().size();

        // Create the Election
        ElectionDTO electionDTO = electionMapper.toDto(election);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restElectionMockMvc.perform(put("/api/elections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionDTO)))
            .andExpect(status().isCreated());

        // Validate the Election in the database
        List<Election> electionList = electionRepository.findAll();
        assertThat(electionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteElection() throws Exception {
        // Initialize the database
        electionRepository.saveAndFlush(election);
        electionSearchRepository.save(election);
        int databaseSizeBeforeDelete = electionRepository.findAll().size();

        // Get the election
        restElectionMockMvc.perform(delete("/api/elections/{id}", election.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean electionExistsInEs = electionSearchRepository.exists(election.getId());
        assertThat(electionExistsInEs).isFalse();

        // Validate the database is empty
        List<Election> electionList = electionRepository.findAll();
        assertThat(electionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchElection() throws Exception {
        // Initialize the database
        electionRepository.saveAndFlush(election);
        electionSearchRepository.save(election);

        // Search the election
        restElectionMockMvc.perform(get("/api/_search/elections?query=id:" + election.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(election.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].periodElection").value(hasItem(DEFAULT_PERIOD_ELECTION.toString())))
            .andExpect(jsonPath("$.[*].dateElection").value(hasItem(sameInstant(DEFAULT_DATE_ELECTION))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dataBase").value(hasItem(DEFAULT_DATA_BASE.toString())))
            .andExpect(jsonPath("$.[*].insetUrl").value(hasItem(DEFAULT_INSET_URL.toString())))
            .andExpect(jsonPath("$.[*].incidentSheet").value(hasItem(DEFAULT_INCIDENT_SHEET.toString())))
            .andExpect(jsonPath("$.[*].dayRecord").value(hasItem(DEFAULT_DAY_RECORD.toString())))
            .andExpect(jsonPath("$.[*].demandTemplateUrl").value(hasItem(DEFAULT_DEMAND_TEMPLATE_URL.toString())))
            .andExpect(jsonPath("$.[*].recountTemplateUrl").value(hasItem(DEFAULT_RECOUNT_TEMPLATE_URL.toString())))
            .andExpect(jsonPath("$.[*].recountDistrictsRule").value(hasItem(DEFAULT_RECOUNT_DISTRICTS_RULE.toString())))
            .andExpect(jsonPath("$.[*].recountPollingPlaceRule").value(hasItem(DEFAULT_RECOUNT_POLLING_PLACE_RULE.toString())))
            .andExpect(jsonPath("$.[*].nameDemandant").value(hasItem(DEFAULT_NAME_DEMANDANT.toString())))
            .andExpect(jsonPath("$.[*].recountElectoralInstitute").value(hasItem(DEFAULT_RECOUNT_ELECTORAL_INSTITUTE.toString())))
            .andExpect(jsonPath("$.[*].recountType").value(hasItem(DEFAULT_RECOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].recountFundamentRequest").value(hasItem(DEFAULT_RECOUNT_FUNDAMENT_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Election.class);
        Election election1 = new Election();
        election1.setId(1L);
        Election election2 = new Election();
        election2.setId(election1.getId());
        assertThat(election1).isEqualTo(election2);
        election2.setId(2L);
        assertThat(election1).isNotEqualTo(election2);
        election1.setId(null);
        assertThat(election1).isNotEqualTo(election2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectionDTO.class);
        ElectionDTO electionDTO1 = new ElectionDTO();
        electionDTO1.setId(1L);
        ElectionDTO electionDTO2 = new ElectionDTO();
        assertThat(electionDTO1).isNotEqualTo(electionDTO2);
        electionDTO2.setId(electionDTO1.getId());
        assertThat(electionDTO1).isEqualTo(electionDTO2);
        electionDTO2.setId(2L);
        assertThat(electionDTO1).isNotEqualTo(electionDTO2);
        electionDTO1.setId(null);
        assertThat(electionDTO1).isNotEqualTo(electionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(electionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(electionMapper.fromId(null)).isNull();
    }
}
