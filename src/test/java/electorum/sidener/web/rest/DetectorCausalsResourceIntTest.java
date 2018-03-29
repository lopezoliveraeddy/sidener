package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.DetectorCausals;
import electorum.sidener.repository.DetectorCausalsRepository;
import electorum.sidener.service.DetectorCausalsService;
import electorum.sidener.repository.search.DetectorCausalsSearchRepository;
import electorum.sidener.service.dto.DetectorCausalsDTO;
import electorum.sidener.service.mapper.DetectorCausalsMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DetectorCausalsResource REST controller.
 *
 * @see DetectorCausalsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class DetectorCausalsResourceIntTest {

    private static final Long DEFAULT_ID_POLLING_PLACE = 1L;
    private static final Long UPDATED_ID_POLLING_PLACE = 2L;

    private static final Long DEFAULT_ID_CAUSAL = 1L;
    private static final Long UPDATED_ID_CAUSAL = 2L;

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    @Autowired
    private DetectorCausalsRepository detectorCausalsRepository;

    @Autowired
    private DetectorCausalsMapper detectorCausalsMapper;

    @Autowired
    private DetectorCausalsService detectorCausalsService;

    @Autowired
    private DetectorCausalsSearchRepository detectorCausalsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDetectorCausalsMockMvc;

    private DetectorCausals detectorCausals;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DetectorCausalsResource detectorCausalsResource = new DetectorCausalsResource(detectorCausalsService);
        this.restDetectorCausalsMockMvc = MockMvcBuilders.standaloneSetup(detectorCausalsResource)
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
    public static DetectorCausals createEntity(EntityManager em) {
        DetectorCausals detectorCausals = new DetectorCausals()
            .idPollingPlace(DEFAULT_ID_POLLING_PLACE)
            .idCausal(DEFAULT_ID_CAUSAL)
            .observations(DEFAULT_OBSERVATIONS);
        return detectorCausals;
    }

    @Before
    public void initTest() {
        detectorCausalsSearchRepository.deleteAll();
        detectorCausals = createEntity(em);
    }

    @Test
    @Transactional
    public void createDetectorCausals() throws Exception {
        int databaseSizeBeforeCreate = detectorCausalsRepository.findAll().size();

        // Create the DetectorCausals
        DetectorCausalsDTO detectorCausalsDTO = detectorCausalsMapper.toDto(detectorCausals);
        restDetectorCausalsMockMvc.perform(post("/api/detector-causals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detectorCausalsDTO)))
            .andExpect(status().isCreated());

        // Validate the DetectorCausals in the database
        List<DetectorCausals> detectorCausalsList = detectorCausalsRepository.findAll();
        assertThat(detectorCausalsList).hasSize(databaseSizeBeforeCreate + 1);
        DetectorCausals testDetectorCausals = detectorCausalsList.get(detectorCausalsList.size() - 1);
        assertThat(testDetectorCausals.getIdPollingPlace()).isEqualTo(DEFAULT_ID_POLLING_PLACE);
        assertThat(testDetectorCausals.getIdCausal()).isEqualTo(DEFAULT_ID_CAUSAL);
        assertThat(testDetectorCausals.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);

        // Validate the DetectorCausals in Elasticsearch
        DetectorCausals detectorCausalsEs = detectorCausalsSearchRepository.findOne(testDetectorCausals.getId());
        assertThat(detectorCausalsEs).isEqualToComparingFieldByField(testDetectorCausals);
    }

    @Test
    @Transactional
    public void createDetectorCausalsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = detectorCausalsRepository.findAll().size();

        // Create the DetectorCausals with an existing ID
        detectorCausals.setId(1L);
        DetectorCausalsDTO detectorCausalsDTO = detectorCausalsMapper.toDto(detectorCausals);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetectorCausalsMockMvc.perform(post("/api/detector-causals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detectorCausalsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DetectorCausals in the database
        List<DetectorCausals> detectorCausalsList = detectorCausalsRepository.findAll();
        assertThat(detectorCausalsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDetectorCausals() throws Exception {
        // Initialize the database
        detectorCausalsRepository.saveAndFlush(detectorCausals);

        // Get all the detectorCausalsList
        restDetectorCausalsMockMvc.perform(get("/api/detector-causals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detectorCausals.getId().intValue())))
            .andExpect(jsonPath("$.[*].idPollingPlace").value(hasItem(DEFAULT_ID_POLLING_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].idCausal").value(hasItem(DEFAULT_ID_CAUSAL.intValue())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())));
    }

    @Test
    @Transactional
    public void getDetectorCausals() throws Exception {
        // Initialize the database
        detectorCausalsRepository.saveAndFlush(detectorCausals);

        // Get the detectorCausals
        restDetectorCausalsMockMvc.perform(get("/api/detector-causals/{id}", detectorCausals.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(detectorCausals.getId().intValue()))
            .andExpect(jsonPath("$.idPollingPlace").value(DEFAULT_ID_POLLING_PLACE.intValue()))
            .andExpect(jsonPath("$.idCausal").value(DEFAULT_ID_CAUSAL.intValue()))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDetectorCausals() throws Exception {
        // Get the detectorCausals
        restDetectorCausalsMockMvc.perform(get("/api/detector-causals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDetectorCausals() throws Exception {
        // Initialize the database
        detectorCausalsRepository.saveAndFlush(detectorCausals);
        detectorCausalsSearchRepository.save(detectorCausals);
        int databaseSizeBeforeUpdate = detectorCausalsRepository.findAll().size();

        // Update the detectorCausals
        DetectorCausals updatedDetectorCausals = detectorCausalsRepository.findOne(detectorCausals.getId());
        updatedDetectorCausals
            .idPollingPlace(UPDATED_ID_POLLING_PLACE)
            .idCausal(UPDATED_ID_CAUSAL)
            .observations(UPDATED_OBSERVATIONS);
        DetectorCausalsDTO detectorCausalsDTO = detectorCausalsMapper.toDto(updatedDetectorCausals);

        restDetectorCausalsMockMvc.perform(put("/api/detector-causals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detectorCausalsDTO)))
            .andExpect(status().isOk());

        // Validate the DetectorCausals in the database
        List<DetectorCausals> detectorCausalsList = detectorCausalsRepository.findAll();
        assertThat(detectorCausalsList).hasSize(databaseSizeBeforeUpdate);
        DetectorCausals testDetectorCausals = detectorCausalsList.get(detectorCausalsList.size() - 1);
        assertThat(testDetectorCausals.getIdPollingPlace()).isEqualTo(UPDATED_ID_POLLING_PLACE);
        assertThat(testDetectorCausals.getIdCausal()).isEqualTo(UPDATED_ID_CAUSAL);
        assertThat(testDetectorCausals.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);

        // Validate the DetectorCausals in Elasticsearch
        DetectorCausals detectorCausalsEs = detectorCausalsSearchRepository.findOne(testDetectorCausals.getId());
        assertThat(detectorCausalsEs).isEqualToComparingFieldByField(testDetectorCausals);
    }

    @Test
    @Transactional
    public void updateNonExistingDetectorCausals() throws Exception {
        int databaseSizeBeforeUpdate = detectorCausalsRepository.findAll().size();

        // Create the DetectorCausals
        DetectorCausalsDTO detectorCausalsDTO = detectorCausalsMapper.toDto(detectorCausals);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDetectorCausalsMockMvc.perform(put("/api/detector-causals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detectorCausalsDTO)))
            .andExpect(status().isCreated());

        // Validate the DetectorCausals in the database
        List<DetectorCausals> detectorCausalsList = detectorCausalsRepository.findAll();
        assertThat(detectorCausalsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDetectorCausals() throws Exception {
        // Initialize the database
        detectorCausalsRepository.saveAndFlush(detectorCausals);
        detectorCausalsSearchRepository.save(detectorCausals);
        int databaseSizeBeforeDelete = detectorCausalsRepository.findAll().size();

        // Get the detectorCausals
        restDetectorCausalsMockMvc.perform(delete("/api/detector-causals/{id}", detectorCausals.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean detectorCausalsExistsInEs = detectorCausalsSearchRepository.exists(detectorCausals.getId());
        assertThat(detectorCausalsExistsInEs).isFalse();

        // Validate the database is empty
        List<DetectorCausals> detectorCausalsList = detectorCausalsRepository.findAll();
        assertThat(detectorCausalsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDetectorCausals() throws Exception {
        // Initialize the database
        detectorCausalsRepository.saveAndFlush(detectorCausals);
        detectorCausalsSearchRepository.save(detectorCausals);

        // Search the detectorCausals
        restDetectorCausalsMockMvc.perform(get("/api/_search/detector-causals?query=id:" + detectorCausals.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detectorCausals.getId().intValue())))
            .andExpect(jsonPath("$.[*].idPollingPlace").value(hasItem(DEFAULT_ID_POLLING_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].idCausal").value(hasItem(DEFAULT_ID_CAUSAL.intValue())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetectorCausals.class);
        DetectorCausals detectorCausals1 = new DetectorCausals();
        detectorCausals1.setId(1L);
        DetectorCausals detectorCausals2 = new DetectorCausals();
        detectorCausals2.setId(detectorCausals1.getId());
        assertThat(detectorCausals1).isEqualTo(detectorCausals2);
        detectorCausals2.setId(2L);
        assertThat(detectorCausals1).isNotEqualTo(detectorCausals2);
        detectorCausals1.setId(null);
        assertThat(detectorCausals1).isNotEqualTo(detectorCausals2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetectorCausalsDTO.class);
        DetectorCausalsDTO detectorCausalsDTO1 = new DetectorCausalsDTO();
        detectorCausalsDTO1.setId(1L);
        DetectorCausalsDTO detectorCausalsDTO2 = new DetectorCausalsDTO();
        assertThat(detectorCausalsDTO1).isNotEqualTo(detectorCausalsDTO2);
        detectorCausalsDTO2.setId(detectorCausalsDTO1.getId());
        assertThat(detectorCausalsDTO1).isEqualTo(detectorCausalsDTO2);
        detectorCausalsDTO2.setId(2L);
        assertThat(detectorCausalsDTO1).isNotEqualTo(detectorCausalsDTO2);
        detectorCausalsDTO1.setId(null);
        assertThat(detectorCausalsDTO1).isNotEqualTo(detectorCausalsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(detectorCausalsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(detectorCausalsMapper.fromId(null)).isNull();
    }
}
