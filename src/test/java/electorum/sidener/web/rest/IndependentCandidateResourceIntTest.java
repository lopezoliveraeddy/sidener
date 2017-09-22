package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.IndependentCandidate;
import electorum.sidener.repository.IndependentCandidateRepository;
import electorum.sidener.service.IndependentCandidateService;
import electorum.sidener.repository.search.IndependentCandidateSearchRepository;
import electorum.sidener.service.dto.IndependentCandidateDTO;
import electorum.sidener.service.mapper.IndependentCandidateMapper;
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
 * Test class for the IndependentCandidateResource REST controller.
 *
 * @see IndependentCandidateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class IndependentCandidateResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private IndependentCandidateRepository independentCandidateRepository;

    @Autowired
    private IndependentCandidateMapper independentCandidateMapper;

    @Autowired
    private IndependentCandidateService independentCandidateService;

    @Autowired
    private IndependentCandidateSearchRepository independentCandidateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIndependentCandidateMockMvc;

    private IndependentCandidate independentCandidate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndependentCandidateResource independentCandidateResource = new IndependentCandidateResource(independentCandidateService);
        this.restIndependentCandidateMockMvc = MockMvcBuilders.standaloneSetup(independentCandidateResource)
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
    public static IndependentCandidate createEntity(EntityManager em) {
        IndependentCandidate independentCandidate = new IndependentCandidate()
            .name(DEFAULT_NAME)
            .acronym(DEFAULT_ACRONYM)
            .image(DEFAULT_IMAGE)
            .published(DEFAULT_PUBLISHED)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return independentCandidate;
    }

    @Before
    public void initTest() {
        independentCandidateSearchRepository.deleteAll();
        independentCandidate = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndependentCandidate() throws Exception {
        int databaseSizeBeforeCreate = independentCandidateRepository.findAll().size();

        // Create the IndependentCandidate
        IndependentCandidateDTO independentCandidateDTO = independentCandidateMapper.toDto(independentCandidate);
        restIndependentCandidateMockMvc.perform(post("/api/independent-candidates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(independentCandidateDTO)))
            .andExpect(status().isCreated());

        // Validate the IndependentCandidate in the database
        List<IndependentCandidate> independentCandidateList = independentCandidateRepository.findAll();
        assertThat(independentCandidateList).hasSize(databaseSizeBeforeCreate + 1);
        IndependentCandidate testIndependentCandidate = independentCandidateList.get(independentCandidateList.size() - 1);
        assertThat(testIndependentCandidate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIndependentCandidate.getAcronym()).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testIndependentCandidate.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testIndependentCandidate.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testIndependentCandidate.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testIndependentCandidate.getUpdated()).isEqualTo(DEFAULT_UPDATED);

        // Validate the IndependentCandidate in Elasticsearch
        IndependentCandidate independentCandidateEs = independentCandidateSearchRepository.findOne(testIndependentCandidate.getId());
        assertThat(independentCandidateEs).isEqualToComparingFieldByField(testIndependentCandidate);
    }

    @Test
    @Transactional
    public void createIndependentCandidateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = independentCandidateRepository.findAll().size();

        // Create the IndependentCandidate with an existing ID
        independentCandidate.setId(1L);
        IndependentCandidateDTO independentCandidateDTO = independentCandidateMapper.toDto(independentCandidate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndependentCandidateMockMvc.perform(post("/api/independent-candidates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(independentCandidateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IndependentCandidate in the database
        List<IndependentCandidate> independentCandidateList = independentCandidateRepository.findAll();
        assertThat(independentCandidateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIndependentCandidates() throws Exception {
        // Initialize the database
        independentCandidateRepository.saveAndFlush(independentCandidate);

        // Get all the independentCandidateList
        restIndependentCandidateMockMvc.perform(get("/api/independent-candidates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(independentCandidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }

    @Test
    @Transactional
    public void getIndependentCandidate() throws Exception {
        // Initialize the database
        independentCandidateRepository.saveAndFlush(independentCandidate);

        // Get the independentCandidate
        restIndependentCandidateMockMvc.perform(get("/api/independent-candidates/{id}", independentCandidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(independentCandidate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.acronym").value(DEFAULT_ACRONYM.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingIndependentCandidate() throws Exception {
        // Get the independentCandidate
        restIndependentCandidateMockMvc.perform(get("/api/independent-candidates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndependentCandidate() throws Exception {
        // Initialize the database
        independentCandidateRepository.saveAndFlush(independentCandidate);
        independentCandidateSearchRepository.save(independentCandidate);
        int databaseSizeBeforeUpdate = independentCandidateRepository.findAll().size();

        // Update the independentCandidate
        IndependentCandidate updatedIndependentCandidate = independentCandidateRepository.findOne(independentCandidate.getId());
        updatedIndependentCandidate
            .name(UPDATED_NAME)
            .acronym(UPDATED_ACRONYM)
            .image(UPDATED_IMAGE)
            .published(UPDATED_PUBLISHED)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        IndependentCandidateDTO independentCandidateDTO = independentCandidateMapper.toDto(updatedIndependentCandidate);

        restIndependentCandidateMockMvc.perform(put("/api/independent-candidates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(independentCandidateDTO)))
            .andExpect(status().isOk());

        // Validate the IndependentCandidate in the database
        List<IndependentCandidate> independentCandidateList = independentCandidateRepository.findAll();
        assertThat(independentCandidateList).hasSize(databaseSizeBeforeUpdate);
        IndependentCandidate testIndependentCandidate = independentCandidateList.get(independentCandidateList.size() - 1);
        assertThat(testIndependentCandidate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIndependentCandidate.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testIndependentCandidate.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testIndependentCandidate.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testIndependentCandidate.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testIndependentCandidate.getUpdated()).isEqualTo(UPDATED_UPDATED);

        // Validate the IndependentCandidate in Elasticsearch
        IndependentCandidate independentCandidateEs = independentCandidateSearchRepository.findOne(testIndependentCandidate.getId());
        assertThat(independentCandidateEs).isEqualToComparingFieldByField(testIndependentCandidate);
    }

    @Test
    @Transactional
    public void updateNonExistingIndependentCandidate() throws Exception {
        int databaseSizeBeforeUpdate = independentCandidateRepository.findAll().size();

        // Create the IndependentCandidate
        IndependentCandidateDTO independentCandidateDTO = independentCandidateMapper.toDto(independentCandidate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIndependentCandidateMockMvc.perform(put("/api/independent-candidates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(independentCandidateDTO)))
            .andExpect(status().isCreated());

        // Validate the IndependentCandidate in the database
        List<IndependentCandidate> independentCandidateList = independentCandidateRepository.findAll();
        assertThat(independentCandidateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIndependentCandidate() throws Exception {
        // Initialize the database
        independentCandidateRepository.saveAndFlush(independentCandidate);
        independentCandidateSearchRepository.save(independentCandidate);
        int databaseSizeBeforeDelete = independentCandidateRepository.findAll().size();

        // Get the independentCandidate
        restIndependentCandidateMockMvc.perform(delete("/api/independent-candidates/{id}", independentCandidate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean independentCandidateExistsInEs = independentCandidateSearchRepository.exists(independentCandidate.getId());
        assertThat(independentCandidateExistsInEs).isFalse();

        // Validate the database is empty
        List<IndependentCandidate> independentCandidateList = independentCandidateRepository.findAll();
        assertThat(independentCandidateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIndependentCandidate() throws Exception {
        // Initialize the database
        independentCandidateRepository.saveAndFlush(independentCandidate);
        independentCandidateSearchRepository.save(independentCandidate);

        // Search the independentCandidate
        restIndependentCandidateMockMvc.perform(get("/api/_search/independent-candidates?query=id:" + independentCandidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(independentCandidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndependentCandidate.class);
        IndependentCandidate independentCandidate1 = new IndependentCandidate();
        independentCandidate1.setId(1L);
        IndependentCandidate independentCandidate2 = new IndependentCandidate();
        independentCandidate2.setId(independentCandidate1.getId());
        assertThat(independentCandidate1).isEqualTo(independentCandidate2);
        independentCandidate2.setId(2L);
        assertThat(independentCandidate1).isNotEqualTo(independentCandidate2);
        independentCandidate1.setId(null);
        assertThat(independentCandidate1).isNotEqualTo(independentCandidate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndependentCandidateDTO.class);
        IndependentCandidateDTO independentCandidateDTO1 = new IndependentCandidateDTO();
        independentCandidateDTO1.setId(1L);
        IndependentCandidateDTO independentCandidateDTO2 = new IndependentCandidateDTO();
        assertThat(independentCandidateDTO1).isNotEqualTo(independentCandidateDTO2);
        independentCandidateDTO2.setId(independentCandidateDTO1.getId());
        assertThat(independentCandidateDTO1).isEqualTo(independentCandidateDTO2);
        independentCandidateDTO2.setId(2L);
        assertThat(independentCandidateDTO1).isNotEqualTo(independentCandidateDTO2);
        independentCandidateDTO1.setId(null);
        assertThat(independentCandidateDTO1).isNotEqualTo(independentCandidateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(independentCandidateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(independentCandidateMapper.fromId(null)).isNull();
    }
}
