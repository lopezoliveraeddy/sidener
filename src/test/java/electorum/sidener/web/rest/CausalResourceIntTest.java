package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.Causal;
import electorum.sidener.repository.CausalRepository;
import electorum.sidener.service.CausalService;
import electorum.sidener.repository.search.CausalSearchRepository;
import electorum.sidener.service.dto.CausalDTO;
import electorum.sidener.service.mapper.CausalMapper;
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

import electorum.sidener.domain.enumeration.CausalType;
/**
 * Test class for the CausalResource REST controller.
 *
 * @see CausalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class CausalResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final CausalType DEFAULT_TYPE = CausalType.RECOUNT;
    private static final CausalType UPDATED_TYPE = CausalType.NULLITY;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CausalRepository causalRepository;

    @Autowired
    private CausalMapper causalMapper;

    @Autowired
    private CausalService causalService;

    @Autowired
    private CausalSearchRepository causalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCausalMockMvc;

    private Causal causal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CausalResource causalResource = new CausalResource(causalService);
        this.restCausalMockMvc = MockMvcBuilders.standaloneSetup(causalResource)
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
    public static Causal createEntity(EntityManager em) {
        Causal causal = new Causal()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .color(DEFAULT_COLOR)
            .published(DEFAULT_PUBLISHED)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return causal;
    }

    @Before
    public void initTest() {
        causalSearchRepository.deleteAll();
        causal = createEntity(em);
    }

    @Test
    @Transactional
    public void createCausal() throws Exception {
        int databaseSizeBeforeCreate = causalRepository.findAll().size();

        // Create the Causal
        CausalDTO causalDTO = causalMapper.toDto(causal);
        restCausalMockMvc.perform(post("/api/causals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(causalDTO)))
            .andExpect(status().isCreated());

        // Validate the Causal in the database
        List<Causal> causalList = causalRepository.findAll();
        assertThat(causalList).hasSize(databaseSizeBeforeCreate + 1);
        Causal testCausal = causalList.get(causalList.size() - 1);
        assertThat(testCausal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCausal.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCausal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCausal.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCausal.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testCausal.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCausal.getUpdated()).isEqualTo(DEFAULT_UPDATED);

        // Validate the Causal in Elasticsearch
        Causal causalEs = causalSearchRepository.findOne(testCausal.getId());
        assertThat(causalEs).isEqualToComparingFieldByField(testCausal);
    }

    @Test
    @Transactional
    public void createCausalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = causalRepository.findAll().size();

        // Create the Causal with an existing ID
        causal.setId(1L);
        CausalDTO causalDTO = causalMapper.toDto(causal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCausalMockMvc.perform(post("/api/causals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(causalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Causal in the database
        List<Causal> causalList = causalRepository.findAll();
        assertThat(causalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCausals() throws Exception {
        // Initialize the database
        causalRepository.saveAndFlush(causal);

        // Get all the causalList
        restCausalMockMvc.perform(get("/api/causals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(causal.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }

    @Test
    @Transactional
    public void getCausal() throws Exception {
        // Initialize the database
        causalRepository.saveAndFlush(causal);

        // Get the causal
        restCausalMockMvc.perform(get("/api/causals/{id}", causal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(causal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingCausal() throws Exception {
        // Get the causal
        restCausalMockMvc.perform(get("/api/causals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCausal() throws Exception {
        // Initialize the database
        causalRepository.saveAndFlush(causal);
        causalSearchRepository.save(causal);
        int databaseSizeBeforeUpdate = causalRepository.findAll().size();

        // Update the causal
        Causal updatedCausal = causalRepository.findOne(causal.getId());
        updatedCausal
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .published(UPDATED_PUBLISHED)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        CausalDTO causalDTO = causalMapper.toDto(updatedCausal);

        restCausalMockMvc.perform(put("/api/causals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(causalDTO)))
            .andExpect(status().isOk());

        // Validate the Causal in the database
        List<Causal> causalList = causalRepository.findAll();
        assertThat(causalList).hasSize(databaseSizeBeforeUpdate);
        Causal testCausal = causalList.get(causalList.size() - 1);
        assertThat(testCausal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCausal.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCausal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCausal.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCausal.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testCausal.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCausal.getUpdated()).isEqualTo(UPDATED_UPDATED);

        // Validate the Causal in Elasticsearch
        Causal causalEs = causalSearchRepository.findOne(testCausal.getId());
        assertThat(causalEs).isEqualToComparingFieldByField(testCausal);
    }

    @Test
    @Transactional
    public void updateNonExistingCausal() throws Exception {
        int databaseSizeBeforeUpdate = causalRepository.findAll().size();

        // Create the Causal
        CausalDTO causalDTO = causalMapper.toDto(causal);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCausalMockMvc.perform(put("/api/causals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(causalDTO)))
            .andExpect(status().isCreated());

        // Validate the Causal in the database
        List<Causal> causalList = causalRepository.findAll();
        assertThat(causalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCausal() throws Exception {
        // Initialize the database
        causalRepository.saveAndFlush(causal);
        causalSearchRepository.save(causal);
        int databaseSizeBeforeDelete = causalRepository.findAll().size();

        // Get the causal
        restCausalMockMvc.perform(delete("/api/causals/{id}", causal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean causalExistsInEs = causalSearchRepository.exists(causal.getId());
        assertThat(causalExistsInEs).isFalse();

        // Validate the database is empty
        List<Causal> causalList = causalRepository.findAll();
        assertThat(causalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCausal() throws Exception {
        // Initialize the database
        causalRepository.saveAndFlush(causal);
        causalSearchRepository.save(causal);

        // Search the causal
        restCausalMockMvc.perform(get("/api/_search/causals?query=id:" + causal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(causal.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Causal.class);
        Causal causal1 = new Causal();
        causal1.setId(1L);
        Causal causal2 = new Causal();
        causal2.setId(causal1.getId());
        assertThat(causal1).isEqualTo(causal2);
        causal2.setId(2L);
        assertThat(causal1).isNotEqualTo(causal2);
        causal1.setId(null);
        assertThat(causal1).isNotEqualTo(causal2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CausalDTO.class);
        CausalDTO causalDTO1 = new CausalDTO();
        causalDTO1.setId(1L);
        CausalDTO causalDTO2 = new CausalDTO();
        assertThat(causalDTO1).isNotEqualTo(causalDTO2);
        causalDTO2.setId(causalDTO1.getId());
        assertThat(causalDTO1).isEqualTo(causalDTO2);
        causalDTO2.setId(2L);
        assertThat(causalDTO1).isNotEqualTo(causalDTO2);
        causalDTO1.setId(null);
        assertThat(causalDTO1).isNotEqualTo(causalDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(causalMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(causalMapper.fromId(null)).isNull();
    }
}
