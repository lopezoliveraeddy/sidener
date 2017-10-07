package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.PoliticalParty;
import electorum.sidener.repository.PoliticalPartyRepository;
import electorum.sidener.service.PoliticalPartyService;
import electorum.sidener.repository.search.PoliticalPartySearchRepository;
import electorum.sidener.service.dto.PoliticalPartyDTO;
import electorum.sidener.service.mapper.PoliticalPartyMapper;
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
 * Test class for the PoliticalPartyResource REST controller.
 *
 * @see PoliticalPartyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class PoliticalPartyResourceIntTest {

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
    private PoliticalPartyRepository politicalPartyRepository;

    @Autowired
    private PoliticalPartyMapper politicalPartyMapper;

    @Autowired
    private PoliticalPartyService politicalPartyService;

    @Autowired
    private PoliticalPartySearchRepository politicalPartySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPoliticalPartyMockMvc;

    private PoliticalParty politicalParty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PoliticalPartyResource politicalPartyResource = new PoliticalPartyResource(politicalPartyService);
        this.restPoliticalPartyMockMvc = MockMvcBuilders.standaloneSetup(politicalPartyResource)
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
    public static PoliticalParty createEntity(EntityManager em) {
        PoliticalParty politicalParty = new PoliticalParty()
            .name(DEFAULT_NAME)
            .acronym(DEFAULT_ACRONYM)
            .image(DEFAULT_IMAGE)
            .published(DEFAULT_PUBLISHED)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return politicalParty;
    }

    @Before
    public void initTest() {
        politicalPartySearchRepository.deleteAll();
        politicalParty = createEntity(em);
    }

    @Test
    @Transactional
    public void createPoliticalParty() throws Exception {
        int databaseSizeBeforeCreate = politicalPartyRepository.findAll().size();

        // Create the PoliticalParty
        PoliticalPartyDTO politicalPartyDTO = politicalPartyMapper.toDto(politicalParty);
        restPoliticalPartyMockMvc.perform(post("/api/political-parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(politicalPartyDTO)))
            .andExpect(status().isCreated());

        // Validate the PoliticalParty in the database
        List<PoliticalParty> politicalPartyList = politicalPartyRepository.findAll();
        assertThat(politicalPartyList).hasSize(databaseSizeBeforeCreate + 1);
        PoliticalParty testPoliticalParty = politicalPartyList.get(politicalPartyList.size() - 1);
        assertThat(testPoliticalParty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPoliticalParty.getAcronym()).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testPoliticalParty.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPoliticalParty.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testPoliticalParty.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPoliticalParty.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);

        // Validate the PoliticalParty in Elasticsearch
        PoliticalParty politicalPartyEs = politicalPartySearchRepository.findOne(testPoliticalParty.getId());
        assertThat(politicalPartyEs).isEqualToComparingFieldByField(testPoliticalParty);
    }

    @Test
    @Transactional
    public void createPoliticalPartyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = politicalPartyRepository.findAll().size();

        // Create the PoliticalParty with an existing ID
        politicalParty.setId(1L);
        PoliticalPartyDTO politicalPartyDTO = politicalPartyMapper.toDto(politicalParty);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoliticalPartyMockMvc.perform(post("/api/political-parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(politicalPartyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PoliticalParty in the database
        List<PoliticalParty> politicalPartyList = politicalPartyRepository.findAll();
        assertThat(politicalPartyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPoliticalParties() throws Exception {
        // Initialize the database
        politicalPartyRepository.saveAndFlush(politicalParty);

        // Get all the politicalPartyList
        restPoliticalPartyMockMvc.perform(get("/api/political-parties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(politicalParty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getPoliticalParty() throws Exception {
        // Initialize the database
        politicalPartyRepository.saveAndFlush(politicalParty);

        // Get the politicalParty
        restPoliticalPartyMockMvc.perform(get("/api/political-parties/{id}", politicalParty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(politicalParty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.acronym").value(DEFAULT_ACRONYM.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingPoliticalParty() throws Exception {
        // Get the politicalParty
        restPoliticalPartyMockMvc.perform(get("/api/political-parties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePoliticalParty() throws Exception {
        // Initialize the database
        politicalPartyRepository.saveAndFlush(politicalParty);
        politicalPartySearchRepository.save(politicalParty);
        int databaseSizeBeforeUpdate = politicalPartyRepository.findAll().size();

        // Update the politicalParty
        PoliticalParty updatedPoliticalParty = politicalPartyRepository.findOne(politicalParty.getId());
        updatedPoliticalParty
            .name(UPDATED_NAME)
            .acronym(UPDATED_ACRONYM)
            .image(UPDATED_IMAGE)
            .published(UPDATED_PUBLISHED)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        PoliticalPartyDTO politicalPartyDTO = politicalPartyMapper.toDto(updatedPoliticalParty);

        restPoliticalPartyMockMvc.perform(put("/api/political-parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(politicalPartyDTO)))
            .andExpect(status().isOk());

        // Validate the PoliticalParty in the database
        List<PoliticalParty> politicalPartyList = politicalPartyRepository.findAll();
        assertThat(politicalPartyList).hasSize(databaseSizeBeforeUpdate);
        PoliticalParty testPoliticalParty = politicalPartyList.get(politicalPartyList.size() - 1);
        assertThat(testPoliticalParty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPoliticalParty.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testPoliticalParty.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPoliticalParty.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testPoliticalParty.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPoliticalParty.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);

        // Validate the PoliticalParty in Elasticsearch
        PoliticalParty politicalPartyEs = politicalPartySearchRepository.findOne(testPoliticalParty.getId());
        assertThat(politicalPartyEs).isEqualToComparingFieldByField(testPoliticalParty);
    }

    @Test
    @Transactional
    public void updateNonExistingPoliticalParty() throws Exception {
        int databaseSizeBeforeUpdate = politicalPartyRepository.findAll().size();

        // Create the PoliticalParty
        PoliticalPartyDTO politicalPartyDTO = politicalPartyMapper.toDto(politicalParty);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPoliticalPartyMockMvc.perform(put("/api/political-parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(politicalPartyDTO)))
            .andExpect(status().isCreated());

        // Validate the PoliticalParty in the database
        List<PoliticalParty> politicalPartyList = politicalPartyRepository.findAll();
        assertThat(politicalPartyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePoliticalParty() throws Exception {
        // Initialize the database
        politicalPartyRepository.saveAndFlush(politicalParty);
        politicalPartySearchRepository.save(politicalParty);
        int databaseSizeBeforeDelete = politicalPartyRepository.findAll().size();

        // Get the politicalParty
        restPoliticalPartyMockMvc.perform(delete("/api/political-parties/{id}", politicalParty.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean politicalPartyExistsInEs = politicalPartySearchRepository.exists(politicalParty.getId());
        assertThat(politicalPartyExistsInEs).isFalse();

        // Validate the database is empty
        List<PoliticalParty> politicalPartyList = politicalPartyRepository.findAll();
        assertThat(politicalPartyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPoliticalParty() throws Exception {
        // Initialize the database
        politicalPartyRepository.saveAndFlush(politicalParty);
        politicalPartySearchRepository.save(politicalParty);

        // Search the politicalParty
        restPoliticalPartyMockMvc.perform(get("/api/_search/political-parties?query=id:" + politicalParty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(politicalParty.getId().intValue())))
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
        TestUtil.equalsVerifier(PoliticalParty.class);
        PoliticalParty politicalParty1 = new PoliticalParty();
        politicalParty1.setId(1L);
        PoliticalParty politicalParty2 = new PoliticalParty();
        politicalParty2.setId(politicalParty1.getId());
        assertThat(politicalParty1).isEqualTo(politicalParty2);
        politicalParty2.setId(2L);
        assertThat(politicalParty1).isNotEqualTo(politicalParty2);
        politicalParty1.setId(null);
        assertThat(politicalParty1).isNotEqualTo(politicalParty2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoliticalPartyDTO.class);
        PoliticalPartyDTO politicalPartyDTO1 = new PoliticalPartyDTO();
        politicalPartyDTO1.setId(1L);
        PoliticalPartyDTO politicalPartyDTO2 = new PoliticalPartyDTO();
        assertThat(politicalPartyDTO1).isNotEqualTo(politicalPartyDTO2);
        politicalPartyDTO2.setId(politicalPartyDTO1.getId());
        assertThat(politicalPartyDTO1).isEqualTo(politicalPartyDTO2);
        politicalPartyDTO2.setId(2L);
        assertThat(politicalPartyDTO1).isNotEqualTo(politicalPartyDTO2);
        politicalPartyDTO1.setId(null);
        assertThat(politicalPartyDTO1).isNotEqualTo(politicalPartyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(politicalPartyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(politicalPartyMapper.fromId(null)).isNull();
    }
}
