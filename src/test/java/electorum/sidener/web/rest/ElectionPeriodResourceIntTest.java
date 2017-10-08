package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.ElectionPeriod;
import electorum.sidener.repository.ElectionPeriodRepository;
import electorum.sidener.service.ElectionPeriodService;
import electorum.sidener.repository.search.ElectionPeriodSearchRepository;
import electorum.sidener.service.dto.ElectionPeriodDTO;
import electorum.sidener.service.mapper.ElectionPeriodMapper;
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

/**
 * Test class for the ElectionPeriodResource REST controller.
 *
 * @see ElectionPeriodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class ElectionPeriodResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ElectionPeriodRepository electionPeriodRepository;

    @Autowired
    private ElectionPeriodMapper electionPeriodMapper;

    @Autowired
    private ElectionPeriodService electionPeriodService;

    @Autowired
    private ElectionPeriodSearchRepository electionPeriodSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restElectionPeriodMockMvc;

    private ElectionPeriod electionPeriod;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ElectionPeriodResource electionPeriodResource = new ElectionPeriodResource(electionPeriodService);
        this.restElectionPeriodMockMvc = MockMvcBuilders.standaloneSetup(electionPeriodResource)
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
    public static ElectionPeriod createEntity(EntityManager em) {
        ElectionPeriod electionPeriod = new ElectionPeriod()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .published(DEFAULT_PUBLISHED)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return electionPeriod;
    }

    @Before
    public void initTest() {
        electionPeriodSearchRepository.deleteAll();
        electionPeriod = createEntity(em);
    }

    @Test
    @Transactional
    public void createElectionPeriod() throws Exception {
        int databaseSizeBeforeCreate = electionPeriodRepository.findAll().size();

        // Create the ElectionPeriod
        ElectionPeriodDTO electionPeriodDTO = electionPeriodMapper.toDto(electionPeriod);
        restElectionPeriodMockMvc.perform(post("/api/election-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionPeriodDTO)))
            .andExpect(status().isCreated());

        // Validate the ElectionPeriod in the database
        List<ElectionPeriod> electionPeriodList = electionPeriodRepository.findAll();
        assertThat(electionPeriodList).hasSize(databaseSizeBeforeCreate + 1);
        ElectionPeriod testElectionPeriod = electionPeriodList.get(electionPeriodList.size() - 1);
        assertThat(testElectionPeriod.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testElectionPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testElectionPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testElectionPeriod.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testElectionPeriod.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testElectionPeriod.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);

        // Validate the ElectionPeriod in Elasticsearch
        ElectionPeriod electionPeriodEs = electionPeriodSearchRepository.findOne(testElectionPeriod.getId());
        assertThat(electionPeriodEs).isEqualToComparingFieldByField(testElectionPeriod);
    }

    @Test
    @Transactional
    public void createElectionPeriodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = electionPeriodRepository.findAll().size();

        // Create the ElectionPeriod with an existing ID
        electionPeriod.setId(1L);
        ElectionPeriodDTO electionPeriodDTO = electionPeriodMapper.toDto(electionPeriod);

        // An entity with an existing ID cannot be created, so this API call must fail
        restElectionPeriodMockMvc.perform(post("/api/election-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionPeriodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ElectionPeriod in the database
        List<ElectionPeriod> electionPeriodList = electionPeriodRepository.findAll();
        assertThat(electionPeriodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllElectionPeriods() throws Exception {
        // Initialize the database
        electionPeriodRepository.saveAndFlush(electionPeriod);

        // Get all the electionPeriodList
        restElectionPeriodMockMvc.perform(get("/api/election-periods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(electionPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getElectionPeriod() throws Exception {
        // Initialize the database
        electionPeriodRepository.saveAndFlush(electionPeriod);

        // Get the electionPeriod
        restElectionPeriodMockMvc.perform(get("/api/election-periods/{id}", electionPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(electionPeriod.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingElectionPeriod() throws Exception {
        // Get the electionPeriod
        restElectionPeriodMockMvc.perform(get("/api/election-periods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElectionPeriod() throws Exception {
        // Initialize the database
        electionPeriodRepository.saveAndFlush(electionPeriod);
        electionPeriodSearchRepository.save(electionPeriod);
        int databaseSizeBeforeUpdate = electionPeriodRepository.findAll().size();

        // Update the electionPeriod
        ElectionPeriod updatedElectionPeriod = electionPeriodRepository.findOne(electionPeriod.getId());
        updatedElectionPeriod
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .published(UPDATED_PUBLISHED)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        ElectionPeriodDTO electionPeriodDTO = electionPeriodMapper.toDto(updatedElectionPeriod);

        restElectionPeriodMockMvc.perform(put("/api/election-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionPeriodDTO)))
            .andExpect(status().isOk());

        // Validate the ElectionPeriod in the database
        List<ElectionPeriod> electionPeriodList = electionPeriodRepository.findAll();
        assertThat(electionPeriodList).hasSize(databaseSizeBeforeUpdate);
        ElectionPeriod testElectionPeriod = electionPeriodList.get(electionPeriodList.size() - 1);
        assertThat(testElectionPeriod.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testElectionPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testElectionPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testElectionPeriod.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testElectionPeriod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testElectionPeriod.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);

        // Validate the ElectionPeriod in Elasticsearch
        ElectionPeriod electionPeriodEs = electionPeriodSearchRepository.findOne(testElectionPeriod.getId());
        assertThat(electionPeriodEs).isEqualToComparingFieldByField(testElectionPeriod);
    }

    @Test
    @Transactional
    public void updateNonExistingElectionPeriod() throws Exception {
        int databaseSizeBeforeUpdate = electionPeriodRepository.findAll().size();

        // Create the ElectionPeriod
        ElectionPeriodDTO electionPeriodDTO = electionPeriodMapper.toDto(electionPeriod);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restElectionPeriodMockMvc.perform(put("/api/election-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionPeriodDTO)))
            .andExpect(status().isCreated());

        // Validate the ElectionPeriod in the database
        List<ElectionPeriod> electionPeriodList = electionPeriodRepository.findAll();
        assertThat(electionPeriodList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteElectionPeriod() throws Exception {
        // Initialize the database
        electionPeriodRepository.saveAndFlush(electionPeriod);
        electionPeriodSearchRepository.save(electionPeriod);
        int databaseSizeBeforeDelete = electionPeriodRepository.findAll().size();

        // Get the electionPeriod
        restElectionPeriodMockMvc.perform(delete("/api/election-periods/{id}", electionPeriod.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean electionPeriodExistsInEs = electionPeriodSearchRepository.exists(electionPeriod.getId());
        assertThat(electionPeriodExistsInEs).isFalse();

        // Validate the database is empty
        List<ElectionPeriod> electionPeriodList = electionPeriodRepository.findAll();
        assertThat(electionPeriodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchElectionPeriod() throws Exception {
        // Initialize the database
        electionPeriodRepository.saveAndFlush(electionPeriod);
        electionPeriodSearchRepository.save(electionPeriod);

        // Search the electionPeriod
        restElectionPeriodMockMvc.perform(get("/api/_search/election-periods?query=id:" + electionPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(electionPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectionPeriod.class);
        ElectionPeriod electionPeriod1 = new ElectionPeriod();
        electionPeriod1.setId(1L);
        ElectionPeriod electionPeriod2 = new ElectionPeriod();
        electionPeriod2.setId(electionPeriod1.getId());
        assertThat(electionPeriod1).isEqualTo(electionPeriod2);
        electionPeriod2.setId(2L);
        assertThat(electionPeriod1).isNotEqualTo(electionPeriod2);
        electionPeriod1.setId(null);
        assertThat(electionPeriod1).isNotEqualTo(electionPeriod2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectionPeriodDTO.class);
        ElectionPeriodDTO electionPeriodDTO1 = new ElectionPeriodDTO();
        electionPeriodDTO1.setId(1L);
        ElectionPeriodDTO electionPeriodDTO2 = new ElectionPeriodDTO();
        assertThat(electionPeriodDTO1).isNotEqualTo(electionPeriodDTO2);
        electionPeriodDTO2.setId(electionPeriodDTO1.getId());
        assertThat(electionPeriodDTO1).isEqualTo(electionPeriodDTO2);
        electionPeriodDTO2.setId(2L);
        assertThat(electionPeriodDTO1).isNotEqualTo(electionPeriodDTO2);
        electionPeriodDTO1.setId(null);
        assertThat(electionPeriodDTO1).isNotEqualTo(electionPeriodDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(electionPeriodMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(electionPeriodMapper.fromId(null)).isNull();
    }
}
