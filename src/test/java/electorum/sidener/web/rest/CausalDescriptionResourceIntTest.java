package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.CausalDescription;
import electorum.sidener.repository.CausalDescriptionRepository;
import electorum.sidener.service.CausalDescriptionService;
import electorum.sidener.repository.search.CausalDescriptionSearchRepository;
import electorum.sidener.service.dto.CausalDescriptionDTO;
import electorum.sidener.service.mapper.CausalDescriptionMapper;
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
 * Test class for the CausalDescriptionResource REST controller.
 *
 * @see CausalDescriptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class CausalDescriptionResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private CausalDescriptionRepository causalDescriptionRepository;

    @Autowired
    private CausalDescriptionMapper causalDescriptionMapper;

    @Autowired
    private CausalDescriptionService causalDescriptionService;

    @Autowired
    private CausalDescriptionSearchRepository causalDescriptionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCausalDescriptionMockMvc;

    private CausalDescription causalDescription;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CausalDescriptionResource causalDescriptionResource = new CausalDescriptionResource(causalDescriptionService);
        this.restCausalDescriptionMockMvc = MockMvcBuilders.standaloneSetup(causalDescriptionResource)
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
    public static CausalDescription createEntity(EntityManager em) {
        CausalDescription causalDescription = new CausalDescription()
            .text(DEFAULT_TEXT);
        return causalDescription;
    }

    @Before
    public void initTest() {
        causalDescriptionSearchRepository.deleteAll();
        causalDescription = createEntity(em);
    }

    @Test
    @Transactional
    public void createCausalDescription() throws Exception {
        int databaseSizeBeforeCreate = causalDescriptionRepository.findAll().size();

        // Create the CausalDescription
        CausalDescriptionDTO causalDescriptionDTO = causalDescriptionMapper.toDto(causalDescription);
        restCausalDescriptionMockMvc.perform(post("/api/causal-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(causalDescriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the CausalDescription in the database
        List<CausalDescription> causalDescriptionList = causalDescriptionRepository.findAll();
        assertThat(causalDescriptionList).hasSize(databaseSizeBeforeCreate + 1);
        CausalDescription testCausalDescription = causalDescriptionList.get(causalDescriptionList.size() - 1);
        assertThat(testCausalDescription.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the CausalDescription in Elasticsearch
        CausalDescription causalDescriptionEs = causalDescriptionSearchRepository.findOne(testCausalDescription.getId());
        assertThat(causalDescriptionEs).isEqualToComparingFieldByField(testCausalDescription);
    }

    @Test
    @Transactional
    public void createCausalDescriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = causalDescriptionRepository.findAll().size();

        // Create the CausalDescription with an existing ID
        causalDescription.setId(1L);
        CausalDescriptionDTO causalDescriptionDTO = causalDescriptionMapper.toDto(causalDescription);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCausalDescriptionMockMvc.perform(post("/api/causal-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(causalDescriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CausalDescription in the database
        List<CausalDescription> causalDescriptionList = causalDescriptionRepository.findAll();
        assertThat(causalDescriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCausalDescriptions() throws Exception {
        // Initialize the database
        causalDescriptionRepository.saveAndFlush(causalDescription);

        // Get all the causalDescriptionList
        restCausalDescriptionMockMvc.perform(get("/api/causal-descriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(causalDescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getCausalDescription() throws Exception {
        // Initialize the database
        causalDescriptionRepository.saveAndFlush(causalDescription);

        // Get the causalDescription
        restCausalDescriptionMockMvc.perform(get("/api/causal-descriptions/{id}", causalDescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(causalDescription.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCausalDescription() throws Exception {
        // Get the causalDescription
        restCausalDescriptionMockMvc.perform(get("/api/causal-descriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCausalDescription() throws Exception {
        // Initialize the database
        causalDescriptionRepository.saveAndFlush(causalDescription);
        causalDescriptionSearchRepository.save(causalDescription);
        int databaseSizeBeforeUpdate = causalDescriptionRepository.findAll().size();

        // Update the causalDescription
        CausalDescription updatedCausalDescription = causalDescriptionRepository.findOne(causalDescription.getId());
        updatedCausalDescription
            .text(UPDATED_TEXT);
        CausalDescriptionDTO causalDescriptionDTO = causalDescriptionMapper.toDto(updatedCausalDescription);

        restCausalDescriptionMockMvc.perform(put("/api/causal-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(causalDescriptionDTO)))
            .andExpect(status().isOk());

        // Validate the CausalDescription in the database
        List<CausalDescription> causalDescriptionList = causalDescriptionRepository.findAll();
        assertThat(causalDescriptionList).hasSize(databaseSizeBeforeUpdate);
        CausalDescription testCausalDescription = causalDescriptionList.get(causalDescriptionList.size() - 1);
        assertThat(testCausalDescription.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the CausalDescription in Elasticsearch
        CausalDescription causalDescriptionEs = causalDescriptionSearchRepository.findOne(testCausalDescription.getId());
        assertThat(causalDescriptionEs).isEqualToComparingFieldByField(testCausalDescription);
    }

    @Test
    @Transactional
    public void updateNonExistingCausalDescription() throws Exception {
        int databaseSizeBeforeUpdate = causalDescriptionRepository.findAll().size();

        // Create the CausalDescription
        CausalDescriptionDTO causalDescriptionDTO = causalDescriptionMapper.toDto(causalDescription);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCausalDescriptionMockMvc.perform(put("/api/causal-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(causalDescriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the CausalDescription in the database
        List<CausalDescription> causalDescriptionList = causalDescriptionRepository.findAll();
        assertThat(causalDescriptionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCausalDescription() throws Exception {
        // Initialize the database
        causalDescriptionRepository.saveAndFlush(causalDescription);
        causalDescriptionSearchRepository.save(causalDescription);
        int databaseSizeBeforeDelete = causalDescriptionRepository.findAll().size();

        // Get the causalDescription
        restCausalDescriptionMockMvc.perform(delete("/api/causal-descriptions/{id}", causalDescription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean causalDescriptionExistsInEs = causalDescriptionSearchRepository.exists(causalDescription.getId());
        assertThat(causalDescriptionExistsInEs).isFalse();

        // Validate the database is empty
        List<CausalDescription> causalDescriptionList = causalDescriptionRepository.findAll();
        assertThat(causalDescriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCausalDescription() throws Exception {
        // Initialize the database
        causalDescriptionRepository.saveAndFlush(causalDescription);
        causalDescriptionSearchRepository.save(causalDescription);

        // Search the causalDescription
        restCausalDescriptionMockMvc.perform(get("/api/_search/causal-descriptions?query=id:" + causalDescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(causalDescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CausalDescription.class);
        CausalDescription causalDescription1 = new CausalDescription();
        causalDescription1.setId(1L);
        CausalDescription causalDescription2 = new CausalDescription();
        causalDescription2.setId(causalDescription1.getId());
        assertThat(causalDescription1).isEqualTo(causalDescription2);
        causalDescription2.setId(2L);
        assertThat(causalDescription1).isNotEqualTo(causalDescription2);
        causalDescription1.setId(null);
        assertThat(causalDescription1).isNotEqualTo(causalDescription2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CausalDescriptionDTO.class);
        CausalDescriptionDTO causalDescriptionDTO1 = new CausalDescriptionDTO();
        causalDescriptionDTO1.setId(1L);
        CausalDescriptionDTO causalDescriptionDTO2 = new CausalDescriptionDTO();
        assertThat(causalDescriptionDTO1).isNotEqualTo(causalDescriptionDTO2);
        causalDescriptionDTO2.setId(causalDescriptionDTO1.getId());
        assertThat(causalDescriptionDTO1).isEqualTo(causalDescriptionDTO2);
        causalDescriptionDTO2.setId(2L);
        assertThat(causalDescriptionDTO1).isNotEqualTo(causalDescriptionDTO2);
        causalDescriptionDTO1.setId(null);
        assertThat(causalDescriptionDTO1).isNotEqualTo(causalDescriptionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(causalDescriptionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(causalDescriptionMapper.fromId(null)).isNull();
    }
}
