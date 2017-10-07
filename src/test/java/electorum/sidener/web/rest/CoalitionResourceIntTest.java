package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.Coalition;
import electorum.sidener.repository.CoalitionRepository;
import electorum.sidener.service.CoalitionService;
import electorum.sidener.repository.search.CoalitionSearchRepository;
import electorum.sidener.service.dto.CoalitionDTO;
import electorum.sidener.service.mapper.CoalitionMapper;
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
 * Test class for the CoalitionResource REST controller.
 *
 * @see CoalitionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class CoalitionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CoalitionRepository coalitionRepository;

    @Autowired
    private CoalitionMapper coalitionMapper;

    @Autowired
    private CoalitionService coalitionService;

    @Autowired
    private CoalitionSearchRepository coalitionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoalitionMockMvc;

    private Coalition coalition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoalitionResource coalitionResource = new CoalitionResource(coalitionService);
        this.restCoalitionMockMvc = MockMvcBuilders.standaloneSetup(coalitionResource)
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
    public static Coalition createEntity(EntityManager em) {
        Coalition coalition = new Coalition()
            .name(DEFAULT_NAME)
            .acronym(DEFAULT_ACRONYM)
            .image(DEFAULT_IMAGE)
            .published(DEFAULT_PUBLISHED)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return coalition;
    }

    @Before
    public void initTest() {
        coalitionSearchRepository.deleteAll();
        coalition = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoalition() throws Exception {
        int databaseSizeBeforeCreate = coalitionRepository.findAll().size();

        // Create the Coalition
        CoalitionDTO coalitionDTO = coalitionMapper.toDto(coalition);
        restCoalitionMockMvc.perform(post("/api/coalitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coalitionDTO)))
            .andExpect(status().isCreated());

        // Validate the Coalition in the database
        List<Coalition> coalitionList = coalitionRepository.findAll();
        assertThat(coalitionList).hasSize(databaseSizeBeforeCreate + 1);
        Coalition testCoalition = coalitionList.get(coalitionList.size() - 1);
        assertThat(testCoalition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCoalition.getAcronym()).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testCoalition.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCoalition.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testCoalition.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCoalition.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);

        // Validate the Coalition in Elasticsearch
        Coalition coalitionEs = coalitionSearchRepository.findOne(testCoalition.getId());
        assertThat(coalitionEs).isEqualToComparingFieldByField(testCoalition);
    }

    @Test
    @Transactional
    public void createCoalitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coalitionRepository.findAll().size();

        // Create the Coalition with an existing ID
        coalition.setId(1L);
        CoalitionDTO coalitionDTO = coalitionMapper.toDto(coalition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoalitionMockMvc.perform(post("/api/coalitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coalitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coalition in the database
        List<Coalition> coalitionList = coalitionRepository.findAll();
        assertThat(coalitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoalitions() throws Exception {
        // Initialize the database
        coalitionRepository.saveAndFlush(coalition);

        // Get all the coalitionList
        restCoalitionMockMvc.perform(get("/api/coalitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coalition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getCoalition() throws Exception {
        // Initialize the database
        coalitionRepository.saveAndFlush(coalition);

        // Get the coalition
        restCoalitionMockMvc.perform(get("/api/coalitions/{id}", coalition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coalition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.acronym").value(DEFAULT_ACRONYM.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingCoalition() throws Exception {
        // Get the coalition
        restCoalitionMockMvc.perform(get("/api/coalitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoalition() throws Exception {
        // Initialize the database
        coalitionRepository.saveAndFlush(coalition);
        coalitionSearchRepository.save(coalition);
        int databaseSizeBeforeUpdate = coalitionRepository.findAll().size();

        // Update the coalition
        Coalition updatedCoalition = coalitionRepository.findOne(coalition.getId());
        updatedCoalition
            .name(UPDATED_NAME)
            .acronym(UPDATED_ACRONYM)
            .image(UPDATED_IMAGE)
            .published(UPDATED_PUBLISHED)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        CoalitionDTO coalitionDTO = coalitionMapper.toDto(updatedCoalition);

        restCoalitionMockMvc.perform(put("/api/coalitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coalitionDTO)))
            .andExpect(status().isOk());

        // Validate the Coalition in the database
        List<Coalition> coalitionList = coalitionRepository.findAll();
        assertThat(coalitionList).hasSize(databaseSizeBeforeUpdate);
        Coalition testCoalition = coalitionList.get(coalitionList.size() - 1);
        assertThat(testCoalition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoalition.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testCoalition.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCoalition.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testCoalition.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCoalition.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);

        // Validate the Coalition in Elasticsearch
        Coalition coalitionEs = coalitionSearchRepository.findOne(testCoalition.getId());
        assertThat(coalitionEs).isEqualToComparingFieldByField(testCoalition);
    }

    @Test
    @Transactional
    public void updateNonExistingCoalition() throws Exception {
        int databaseSizeBeforeUpdate = coalitionRepository.findAll().size();

        // Create the Coalition
        CoalitionDTO coalitionDTO = coalitionMapper.toDto(coalition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoalitionMockMvc.perform(put("/api/coalitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coalitionDTO)))
            .andExpect(status().isCreated());

        // Validate the Coalition in the database
        List<Coalition> coalitionList = coalitionRepository.findAll();
        assertThat(coalitionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoalition() throws Exception {
        // Initialize the database
        coalitionRepository.saveAndFlush(coalition);
        coalitionSearchRepository.save(coalition);
        int databaseSizeBeforeDelete = coalitionRepository.findAll().size();

        // Get the coalition
        restCoalitionMockMvc.perform(delete("/api/coalitions/{id}", coalition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean coalitionExistsInEs = coalitionSearchRepository.exists(coalition.getId());
        assertThat(coalitionExistsInEs).isFalse();

        // Validate the database is empty
        List<Coalition> coalitionList = coalitionRepository.findAll();
        assertThat(coalitionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCoalition() throws Exception {
        // Initialize the database
        coalitionRepository.saveAndFlush(coalition);
        coalitionSearchRepository.save(coalition);

        // Search the coalition
        restCoalitionMockMvc.perform(get("/api/_search/coalitions?query=id:" + coalition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coalition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coalition.class);
        Coalition coalition1 = new Coalition();
        coalition1.setId(1L);
        Coalition coalition2 = new Coalition();
        coalition2.setId(coalition1.getId());
        assertThat(coalition1).isEqualTo(coalition2);
        coalition2.setId(2L);
        assertThat(coalition1).isNotEqualTo(coalition2);
        coalition1.setId(null);
        assertThat(coalition1).isNotEqualTo(coalition2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoalitionDTO.class);
        CoalitionDTO coalitionDTO1 = new CoalitionDTO();
        coalitionDTO1.setId(1L);
        CoalitionDTO coalitionDTO2 = new CoalitionDTO();
        assertThat(coalitionDTO1).isNotEqualTo(coalitionDTO2);
        coalitionDTO2.setId(coalitionDTO1.getId());
        assertThat(coalitionDTO1).isEqualTo(coalitionDTO2);
        coalitionDTO2.setId(2L);
        assertThat(coalitionDTO1).isNotEqualTo(coalitionDTO2);
        coalitionDTO1.setId(null);
        assertThat(coalitionDTO1).isNotEqualTo(coalitionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(coalitionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(coalitionMapper.fromId(null)).isNull();
    }
}
