package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.ElectionType;
import electorum.sidener.repository.ElectionTypeRepository;
import electorum.sidener.service.ElectionTypeService;
import electorum.sidener.repository.search.ElectionTypeSearchRepository;
import electorum.sidener.service.dto.ElectionTypeDTO;
import electorum.sidener.service.mapper.ElectionTypeMapper;
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
 * Test class for the ElectionTypeResource REST controller.
 *
 * @see ElectionTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class ElectionTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ElectionTypeRepository electionTypeRepository;

    @Autowired
    private ElectionTypeMapper electionTypeMapper;

    @Autowired
    private ElectionTypeService electionTypeService;

    @Autowired
    private ElectionTypeSearchRepository electionTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restElectionTypeMockMvc;

    private ElectionType electionType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ElectionTypeResource electionTypeResource = new ElectionTypeResource(electionTypeService);
        this.restElectionTypeMockMvc = MockMvcBuilders.standaloneSetup(electionTypeResource)
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
    public static ElectionType createEntity(EntityManager em) {
        ElectionType electionType = new ElectionType()
            .name(DEFAULT_NAME)
            .published(DEFAULT_PUBLISHED)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return electionType;
    }

    @Before
    public void initTest() {
        electionTypeSearchRepository.deleteAll();
        electionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createElectionType() throws Exception {
        int databaseSizeBeforeCreate = electionTypeRepository.findAll().size();

        // Create the ElectionType
        ElectionTypeDTO electionTypeDTO = electionTypeMapper.toDto(electionType);
        restElectionTypeMockMvc.perform(post("/api/election-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the ElectionType in the database
        List<ElectionType> electionTypeList = electionTypeRepository.findAll();
        assertThat(electionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ElectionType testElectionType = electionTypeList.get(electionTypeList.size() - 1);
        assertThat(testElectionType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testElectionType.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testElectionType.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testElectionType.getUpdated()).isEqualTo(DEFAULT_UPDATED);

        // Validate the ElectionType in Elasticsearch
        ElectionType electionTypeEs = electionTypeSearchRepository.findOne(testElectionType.getId());
        assertThat(electionTypeEs).isEqualToComparingFieldByField(testElectionType);
    }

    @Test
    @Transactional
    public void createElectionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = electionTypeRepository.findAll().size();

        // Create the ElectionType with an existing ID
        electionType.setId(1L);
        ElectionTypeDTO electionTypeDTO = electionTypeMapper.toDto(electionType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restElectionTypeMockMvc.perform(post("/api/election-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ElectionType in the database
        List<ElectionType> electionTypeList = electionTypeRepository.findAll();
        assertThat(electionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllElectionTypes() throws Exception {
        // Initialize the database
        electionTypeRepository.saveAndFlush(electionType);

        // Get all the electionTypeList
        restElectionTypeMockMvc.perform(get("/api/election-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(electionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }

    @Test
    @Transactional
    public void getElectionType() throws Exception {
        // Initialize the database
        electionTypeRepository.saveAndFlush(electionType);

        // Get the electionType
        restElectionTypeMockMvc.perform(get("/api/election-types/{id}", electionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(electionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingElectionType() throws Exception {
        // Get the electionType
        restElectionTypeMockMvc.perform(get("/api/election-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElectionType() throws Exception {
        // Initialize the database
        electionTypeRepository.saveAndFlush(electionType);
        electionTypeSearchRepository.save(electionType);
        int databaseSizeBeforeUpdate = electionTypeRepository.findAll().size();

        // Update the electionType
        ElectionType updatedElectionType = electionTypeRepository.findOne(electionType.getId());
        updatedElectionType
            .name(UPDATED_NAME)
            .published(UPDATED_PUBLISHED)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        ElectionTypeDTO electionTypeDTO = electionTypeMapper.toDto(updatedElectionType);

        restElectionTypeMockMvc.perform(put("/api/election-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionTypeDTO)))
            .andExpect(status().isOk());

        // Validate the ElectionType in the database
        List<ElectionType> electionTypeList = electionTypeRepository.findAll();
        assertThat(electionTypeList).hasSize(databaseSizeBeforeUpdate);
        ElectionType testElectionType = electionTypeList.get(electionTypeList.size() - 1);
        assertThat(testElectionType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testElectionType.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testElectionType.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testElectionType.getUpdated()).isEqualTo(UPDATED_UPDATED);

        // Validate the ElectionType in Elasticsearch
        ElectionType electionTypeEs = electionTypeSearchRepository.findOne(testElectionType.getId());
        assertThat(electionTypeEs).isEqualToComparingFieldByField(testElectionType);
    }

    @Test
    @Transactional
    public void updateNonExistingElectionType() throws Exception {
        int databaseSizeBeforeUpdate = electionTypeRepository.findAll().size();

        // Create the ElectionType
        ElectionTypeDTO electionTypeDTO = electionTypeMapper.toDto(electionType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restElectionTypeMockMvc.perform(put("/api/election-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electionTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the ElectionType in the database
        List<ElectionType> electionTypeList = electionTypeRepository.findAll();
        assertThat(electionTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteElectionType() throws Exception {
        // Initialize the database
        electionTypeRepository.saveAndFlush(electionType);
        electionTypeSearchRepository.save(electionType);
        int databaseSizeBeforeDelete = electionTypeRepository.findAll().size();

        // Get the electionType
        restElectionTypeMockMvc.perform(delete("/api/election-types/{id}", electionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean electionTypeExistsInEs = electionTypeSearchRepository.exists(electionType.getId());
        assertThat(electionTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<ElectionType> electionTypeList = electionTypeRepository.findAll();
        assertThat(electionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchElectionType() throws Exception {
        // Initialize the database
        electionTypeRepository.saveAndFlush(electionType);
        electionTypeSearchRepository.save(electionType);

        // Search the electionType
        restElectionTypeMockMvc.perform(get("/api/_search/election-types?query=id:" + electionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(electionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectionType.class);
        ElectionType electionType1 = new ElectionType();
        electionType1.setId(1L);
        ElectionType electionType2 = new ElectionType();
        electionType2.setId(electionType1.getId());
        assertThat(electionType1).isEqualTo(electionType2);
        electionType2.setId(2L);
        assertThat(electionType1).isNotEqualTo(electionType2);
        electionType1.setId(null);
        assertThat(electionType1).isNotEqualTo(electionType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectionTypeDTO.class);
        ElectionTypeDTO electionTypeDTO1 = new ElectionTypeDTO();
        electionTypeDTO1.setId(1L);
        ElectionTypeDTO electionTypeDTO2 = new ElectionTypeDTO();
        assertThat(electionTypeDTO1).isNotEqualTo(electionTypeDTO2);
        electionTypeDTO2.setId(electionTypeDTO1.getId());
        assertThat(electionTypeDTO1).isEqualTo(electionTypeDTO2);
        electionTypeDTO2.setId(2L);
        assertThat(electionTypeDTO1).isNotEqualTo(electionTypeDTO2);
        electionTypeDTO1.setId(null);
        assertThat(electionTypeDTO1).isNotEqualTo(electionTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(electionTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(electionTypeMapper.fromId(null)).isNull();
    }
}
