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

    private static final ZonedDateTime DEFAULT_DATE_ELECTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ELECTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.IN_PROGRESS;

    private static final String DEFAULT_PREP_URL = "AAAAAAAAAA";
    private static final String UPDATED_PREP_URL = "BBBBBBBBBB";

    private static final String DEFAULT_BALLOT_URL = "AAAAAAAAAA";
    private static final String UPDATED_BALLOT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_INSET_URL = "AAAAAAAAAA";
    private static final String UPDATED_INSET_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DEMAND_TEMPLATE_URL = "AAAAAAAAAA";
    private static final String UPDATED_DEMAND_TEMPLATE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_RECOUNT_TEMPLATE_URL = "AAAAAAAAAA";
    private static final String UPDATED_RECOUNT_TEMPLATE_URL = "BBBBBBBBBB";

    private static final RecountDistrictsRule DEFAULT_RECOUNT_DISTRICTS_RULE = RecountDistrictsRule.LESS_1;
    private static final RecountDistrictsRule UPDATED_RECOUNT_DISTRICTS_RULE = RecountDistrictsRule.LESS_EQUAL_1;

    private static final RecountPollingPlaceRule DEFAULT_RECOUNT_POLLING_PLACE_RULE = RecountPollingPlaceRule.LESS_1;
    private static final RecountPollingPlaceRule UPDATED_RECOUNT_POLLING_PLACE_RULE = RecountPollingPlaceRule.LESS_EQUAL_1;

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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
            .dateElection(DEFAULT_DATE_ELECTION)
            .status(DEFAULT_STATUS)
            .prepUrl(DEFAULT_PREP_URL)
            .ballotUrl(DEFAULT_BALLOT_URL)
            .insetUrl(DEFAULT_INSET_URL)
            .demandTemplateUrl(DEFAULT_DEMAND_TEMPLATE_URL)
            .recountTemplateUrl(DEFAULT_RECOUNT_TEMPLATE_URL)
            .recountDistrictsRule(DEFAULT_RECOUNT_DISTRICTS_RULE)
            .recountPollingPlaceRule(DEFAULT_RECOUNT_POLLING_PLACE_RULE)
            .published(DEFAULT_PUBLISHED)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
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
        assertThat(testElection.getDateElection()).isEqualTo(DEFAULT_DATE_ELECTION);
        assertThat(testElection.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testElection.getPrepUrl()).isEqualTo(DEFAULT_PREP_URL);
        assertThat(testElection.getBallotUrl()).isEqualTo(DEFAULT_BALLOT_URL);
        assertThat(testElection.getInsetUrl()).isEqualTo(DEFAULT_INSET_URL);
        assertThat(testElection.getDemandTemplateUrl()).isEqualTo(DEFAULT_DEMAND_TEMPLATE_URL);
        assertThat(testElection.getRecountTemplateUrl()).isEqualTo(DEFAULT_RECOUNT_TEMPLATE_URL);
        assertThat(testElection.getRecountDistrictsRule()).isEqualTo(DEFAULT_RECOUNT_DISTRICTS_RULE);
        assertThat(testElection.getRecountPollingPlaceRule()).isEqualTo(DEFAULT_RECOUNT_POLLING_PLACE_RULE);
        assertThat(testElection.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testElection.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testElection.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);

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
            .andExpect(jsonPath("$.[*].dateElection").value(hasItem(sameInstant(DEFAULT_DATE_ELECTION))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].prepUrl").value(hasItem(DEFAULT_PREP_URL.toString())))
            .andExpect(jsonPath("$.[*].ballotUrl").value(hasItem(DEFAULT_BALLOT_URL.toString())))
            .andExpect(jsonPath("$.[*].insetUrl").value(hasItem(DEFAULT_INSET_URL.toString())))
            .andExpect(jsonPath("$.[*].demandTemplateUrl").value(hasItem(DEFAULT_DEMAND_TEMPLATE_URL.toString())))
            .andExpect(jsonPath("$.[*].recountTemplateUrl").value(hasItem(DEFAULT_RECOUNT_TEMPLATE_URL.toString())))
            .andExpect(jsonPath("$.[*].recountDistrictsRule").value(hasItem(DEFAULT_RECOUNT_DISTRICTS_RULE.toString())))
            .andExpect(jsonPath("$.[*].recountPollingPlaceRule").value(hasItem(DEFAULT_RECOUNT_POLLING_PLACE_RULE.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
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
            .andExpect(jsonPath("$.dateElection").value(sameInstant(DEFAULT_DATE_ELECTION)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.prepUrl").value(DEFAULT_PREP_URL.toString()))
            .andExpect(jsonPath("$.ballotUrl").value(DEFAULT_BALLOT_URL.toString()))
            .andExpect(jsonPath("$.insetUrl").value(DEFAULT_INSET_URL.toString()))
            .andExpect(jsonPath("$.demandTemplateUrl").value(DEFAULT_DEMAND_TEMPLATE_URL.toString()))
            .andExpect(jsonPath("$.recountTemplateUrl").value(DEFAULT_RECOUNT_TEMPLATE_URL.toString()))
            .andExpect(jsonPath("$.recountDistrictsRule").value(DEFAULT_RECOUNT_DISTRICTS_RULE.toString()))
            .andExpect(jsonPath("$.recountPollingPlaceRule").value(DEFAULT_RECOUNT_POLLING_PLACE_RULE.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
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
            .dateElection(UPDATED_DATE_ELECTION)
            .status(UPDATED_STATUS)
            .prepUrl(UPDATED_PREP_URL)
            .ballotUrl(UPDATED_BALLOT_URL)
            .insetUrl(UPDATED_INSET_URL)
            .demandTemplateUrl(UPDATED_DEMAND_TEMPLATE_URL)
            .recountTemplateUrl(UPDATED_RECOUNT_TEMPLATE_URL)
            .recountDistrictsRule(UPDATED_RECOUNT_DISTRICTS_RULE)
            .recountPollingPlaceRule(UPDATED_RECOUNT_POLLING_PLACE_RULE)
            .published(UPDATED_PUBLISHED)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
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
        assertThat(testElection.getDateElection()).isEqualTo(UPDATED_DATE_ELECTION);
        assertThat(testElection.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testElection.getPrepUrl()).isEqualTo(UPDATED_PREP_URL);
        assertThat(testElection.getBallotUrl()).isEqualTo(UPDATED_BALLOT_URL);
        assertThat(testElection.getInsetUrl()).isEqualTo(UPDATED_INSET_URL);
        assertThat(testElection.getDemandTemplateUrl()).isEqualTo(UPDATED_DEMAND_TEMPLATE_URL);
        assertThat(testElection.getRecountTemplateUrl()).isEqualTo(UPDATED_RECOUNT_TEMPLATE_URL);
        assertThat(testElection.getRecountDistrictsRule()).isEqualTo(UPDATED_RECOUNT_DISTRICTS_RULE);
        assertThat(testElection.getRecountPollingPlaceRule()).isEqualTo(UPDATED_RECOUNT_POLLING_PLACE_RULE);
        assertThat(testElection.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testElection.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testElection.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);

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
            .andExpect(jsonPath("$.[*].dateElection").value(hasItem(sameInstant(DEFAULT_DATE_ELECTION))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].prepUrl").value(hasItem(DEFAULT_PREP_URL.toString())))
            .andExpect(jsonPath("$.[*].ballotUrl").value(hasItem(DEFAULT_BALLOT_URL.toString())))
            .andExpect(jsonPath("$.[*].insetUrl").value(hasItem(DEFAULT_INSET_URL.toString())))
            .andExpect(jsonPath("$.[*].demandTemplateUrl").value(hasItem(DEFAULT_DEMAND_TEMPLATE_URL.toString())))
            .andExpect(jsonPath("$.[*].recountTemplateUrl").value(hasItem(DEFAULT_RECOUNT_TEMPLATE_URL.toString())))
            .andExpect(jsonPath("$.[*].recountDistrictsRule").value(hasItem(DEFAULT_RECOUNT_DISTRICTS_RULE.toString())))
            .andExpect(jsonPath("$.[*].recountPollingPlaceRule").value(hasItem(DEFAULT_RECOUNT_POLLING_PLACE_RULE.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
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
