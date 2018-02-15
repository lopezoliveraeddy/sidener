package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.District;
import electorum.sidener.repository.DistrictRepository;
import electorum.sidener.service.DistrictService;
import electorum.sidener.repository.search.DistrictSearchRepository;
import electorum.sidener.service.dto.DistrictDTO;
import electorum.sidener.service.mapper.DistrictMapper;
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
/**
 * Test class for the DistrictResource REST controller.
 *
 * @see DistrictResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class DistrictResourceIntTest {

    private static final Long DEFAULT_DECIMAL_NUMBER = 1L;
    private static final Long UPDATED_DECIMAL_NUMBER = 2L;

    private static final String DEFAULT_ROMAN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROMAN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT_HEAD = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.AGU;
    private static final State UPDATED_STATE = State.BCN;

    private static final String DEFAULT_ENTITY_FIRST_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_FIRST_PLACE = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_FIRST_PLACE = 1L;
    private static final Long UPDATED_TOTAL_FIRST_PLACE = 2L;

    private static final String DEFAULT_ENTITY_SECOND_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_SECOND_PLACE = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_SECOND_PLACE = 1L;
    private static final Long UPDATED_TOTAL_SECOND_PLACE = 2L;

    private static final Long DEFAULT_TOTAL_VOTES = 1L;
    private static final Long UPDATED_TOTAL_VOTES = 2L;

    private static final Long DEFAULT_ELECTORAL_ROLL = 1L;
    private static final Long UPDATED_ELECTORAL_ROLL = 2L;

    private static final Long DEFAULT_TOTAL_POLLING_PLACES = 1L;
    private static final Long UPDATED_TOTAL_POLLING_PLACES = 2L;

    private static final Long DEFAULT_NULL_VOTES = 1L;
    private static final Long UPDATED_NULL_VOTES = 2L;

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private DistrictMapper districtMapper;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private DistrictSearchRepository districtSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDistrictMockMvc;

    private District district;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DistrictResource districtResource = new DistrictResource(districtService);
        this.restDistrictMockMvc = MockMvcBuilders.standaloneSetup(districtResource)
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
    public static District createEntity(EntityManager em) {
        District district = new District()
            .decimalNumber(DEFAULT_DECIMAL_NUMBER)
            .romanNumber(DEFAULT_ROMAN_NUMBER)
            .districtHead(DEFAULT_DISTRICT_HEAD)
            .state(DEFAULT_STATE)
            .entityFirstPlace(DEFAULT_ENTITY_FIRST_PLACE)
            .totalFirstPlace(DEFAULT_TOTAL_FIRST_PLACE)
            .entitySecondPlace(DEFAULT_ENTITY_SECOND_PLACE)
            .totalSecondPlace(DEFAULT_TOTAL_SECOND_PLACE)
            .totalVotes(DEFAULT_TOTAL_VOTES)
            .electoralRoll(DEFAULT_ELECTORAL_ROLL)
            .totalPollingPlaces(DEFAULT_TOTAL_POLLING_PLACES)
            .nullVotes(DEFAULT_NULL_VOTES)
            .published(DEFAULT_PUBLISHED)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return district;
    }

    @Before
    public void initTest() {
        districtSearchRepository.deleteAll();
        district = createEntity(em);
    }

    @Test
    @Transactional
    public void createDistrict() throws Exception {
        int databaseSizeBeforeCreate = districtRepository.findAll().size();

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);
        restDistrictMockMvc.perform(post("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isCreated());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeCreate + 1);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getDecimalNumber()).isEqualTo(DEFAULT_DECIMAL_NUMBER);
        assertThat(testDistrict.getRomanNumber()).isEqualTo(DEFAULT_ROMAN_NUMBER);
        assertThat(testDistrict.getDistrictHead()).isEqualTo(DEFAULT_DISTRICT_HEAD);
        assertThat(testDistrict.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testDistrict.getEntityFirstPlace()).isEqualTo(DEFAULT_ENTITY_FIRST_PLACE);
        assertThat(testDistrict.getTotalFirstPlace()).isEqualTo(DEFAULT_TOTAL_FIRST_PLACE);
        assertThat(testDistrict.getEntitySecondPlace()).isEqualTo(DEFAULT_ENTITY_SECOND_PLACE);
        assertThat(testDistrict.getTotalSecondPlace()).isEqualTo(DEFAULT_TOTAL_SECOND_PLACE);
        assertThat(testDistrict.getTotalVotes()).isEqualTo(DEFAULT_TOTAL_VOTES);
        assertThat(testDistrict.getElectoralRoll()).isEqualTo(DEFAULT_ELECTORAL_ROLL);
        assertThat(testDistrict.getTotalPollingPlaces()).isEqualTo(DEFAULT_TOTAL_POLLING_PLACES);
        assertThat(testDistrict.getNullVotes()).isEqualTo(DEFAULT_NULL_VOTES);
        assertThat(testDistrict.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testDistrict.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDistrict.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);

        // Validate the District in Elasticsearch
        District districtEs = districtSearchRepository.findOne(testDistrict.getId());
        assertThat(districtEs).isEqualToComparingFieldByField(testDistrict);
    }

    @Test
    @Transactional
    public void createDistrictWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = districtRepository.findAll().size();

        // Create the District with an existing ID
        district.setId(1L);
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistrictMockMvc.perform(post("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDistricts() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList
        restDistrictMockMvc.perform(get("/api/districts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(district.getId().intValue())))
            .andExpect(jsonPath("$.[*].decimalNumber").value(hasItem(DEFAULT_DECIMAL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].romanNumber").value(hasItem(DEFAULT_ROMAN_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].districtHead").value(hasItem(DEFAULT_DISTRICT_HEAD.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].entityFirstPlace").value(hasItem(DEFAULT_ENTITY_FIRST_PLACE.toString())))
            .andExpect(jsonPath("$.[*].totalFirstPlace").value(hasItem(DEFAULT_TOTAL_FIRST_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].entitySecondPlace").value(hasItem(DEFAULT_ENTITY_SECOND_PLACE.toString())))
            .andExpect(jsonPath("$.[*].totalSecondPlace").value(hasItem(DEFAULT_TOTAL_SECOND_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].totalVotes").value(hasItem(DEFAULT_TOTAL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].electoralRoll").value(hasItem(DEFAULT_ELECTORAL_ROLL.intValue())))
            .andExpect(jsonPath("$.[*].totalPollingPlaces").value(hasItem(DEFAULT_TOTAL_POLLING_PLACES.intValue())))
            .andExpect(jsonPath("$.[*].nullVotes").value(hasItem(DEFAULT_NULL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get the district
        restDistrictMockMvc.perform(get("/api/districts/{id}", district.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(district.getId().intValue()))
            .andExpect(jsonPath("$.decimalNumber").value(DEFAULT_DECIMAL_NUMBER.intValue()))
            .andExpect(jsonPath("$.romanNumber").value(DEFAULT_ROMAN_NUMBER.toString()))
            .andExpect(jsonPath("$.districtHead").value(DEFAULT_DISTRICT_HEAD.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.entityFirstPlace").value(DEFAULT_ENTITY_FIRST_PLACE.toString()))
            .andExpect(jsonPath("$.totalFirstPlace").value(DEFAULT_TOTAL_FIRST_PLACE.intValue()))
            .andExpect(jsonPath("$.entitySecondPlace").value(DEFAULT_ENTITY_SECOND_PLACE.toString()))
            .andExpect(jsonPath("$.totalSecondPlace").value(DEFAULT_TOTAL_SECOND_PLACE.intValue()))
            .andExpect(jsonPath("$.totalVotes").value(DEFAULT_TOTAL_VOTES.intValue()))
            .andExpect(jsonPath("$.electoralRoll").value(DEFAULT_ELECTORAL_ROLL.intValue()))
            .andExpect(jsonPath("$.totalPollingPlaces").value(DEFAULT_TOTAL_POLLING_PLACES.intValue()))
            .andExpect(jsonPath("$.nullVotes").value(DEFAULT_NULL_VOTES.intValue()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDistrict() throws Exception {
        // Get the district
        restDistrictMockMvc.perform(get("/api/districts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);
        districtSearchRepository.save(district);
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district
        District updatedDistrict = districtRepository.findOne(district.getId());
        updatedDistrict
            .decimalNumber(UPDATED_DECIMAL_NUMBER)
            .romanNumber(UPDATED_ROMAN_NUMBER)
            .districtHead(UPDATED_DISTRICT_HEAD)
            .state(UPDATED_STATE)
            .entityFirstPlace(UPDATED_ENTITY_FIRST_PLACE)
            .totalFirstPlace(UPDATED_TOTAL_FIRST_PLACE)
            .entitySecondPlace(UPDATED_ENTITY_SECOND_PLACE)
            .totalSecondPlace(UPDATED_TOTAL_SECOND_PLACE)
            .totalVotes(UPDATED_TOTAL_VOTES)
            .electoralRoll(UPDATED_ELECTORAL_ROLL)
            .totalPollingPlaces(UPDATED_TOTAL_POLLING_PLACES)
            .nullVotes(UPDATED_NULL_VOTES)
            .published(UPDATED_PUBLISHED)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        DistrictDTO districtDTO = districtMapper.toDto(updatedDistrict);

        restDistrictMockMvc.perform(put("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getDecimalNumber()).isEqualTo(UPDATED_DECIMAL_NUMBER);
        assertThat(testDistrict.getRomanNumber()).isEqualTo(UPDATED_ROMAN_NUMBER);
        assertThat(testDistrict.getDistrictHead()).isEqualTo(UPDATED_DISTRICT_HEAD);
        assertThat(testDistrict.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDistrict.getEntityFirstPlace()).isEqualTo(UPDATED_ENTITY_FIRST_PLACE);
        assertThat(testDistrict.getTotalFirstPlace()).isEqualTo(UPDATED_TOTAL_FIRST_PLACE);
        assertThat(testDistrict.getEntitySecondPlace()).isEqualTo(UPDATED_ENTITY_SECOND_PLACE);
        assertThat(testDistrict.getTotalSecondPlace()).isEqualTo(UPDATED_TOTAL_SECOND_PLACE);
        assertThat(testDistrict.getTotalVotes()).isEqualTo(UPDATED_TOTAL_VOTES);
        assertThat(testDistrict.getElectoralRoll()).isEqualTo(UPDATED_ELECTORAL_ROLL);
        assertThat(testDistrict.getTotalPollingPlaces()).isEqualTo(UPDATED_TOTAL_POLLING_PLACES);
        assertThat(testDistrict.getNullVotes()).isEqualTo(UPDATED_NULL_VOTES);
        assertThat(testDistrict.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testDistrict.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDistrict.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);

        // Validate the District in Elasticsearch
        District districtEs = districtSearchRepository.findOne(testDistrict.getId());
        assertThat(districtEs).isEqualToComparingFieldByField(testDistrict);
    }

    @Test
    @Transactional
    public void updateNonExistingDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Create the District
        DistrictDTO districtDTO = districtMapper.toDto(district);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDistrictMockMvc.perform(put("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districtDTO)))
            .andExpect(status().isCreated());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);
        districtSearchRepository.save(district);
        int databaseSizeBeforeDelete = districtRepository.findAll().size();

        // Get the district
        restDistrictMockMvc.perform(delete("/api/districts/{id}", district.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean districtExistsInEs = districtSearchRepository.exists(district.getId());
        assertThat(districtExistsInEs).isFalse();

        // Validate the database is empty
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);
        districtSearchRepository.save(district);

        // Search the district
        restDistrictMockMvc.perform(get("/api/_search/districts?query=id:" + district.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(district.getId().intValue())))
            .andExpect(jsonPath("$.[*].decimalNumber").value(hasItem(DEFAULT_DECIMAL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].romanNumber").value(hasItem(DEFAULT_ROMAN_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].districtHead").value(hasItem(DEFAULT_DISTRICT_HEAD.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].entityFirstPlace").value(hasItem(DEFAULT_ENTITY_FIRST_PLACE.toString())))
            .andExpect(jsonPath("$.[*].totalFirstPlace").value(hasItem(DEFAULT_TOTAL_FIRST_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].entitySecondPlace").value(hasItem(DEFAULT_ENTITY_SECOND_PLACE.toString())))
            .andExpect(jsonPath("$.[*].totalSecondPlace").value(hasItem(DEFAULT_TOTAL_SECOND_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].totalVotes").value(hasItem(DEFAULT_TOTAL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].electoralRoll").value(hasItem(DEFAULT_ELECTORAL_ROLL.intValue())))
            .andExpect(jsonPath("$.[*].totalPollingPlaces").value(hasItem(DEFAULT_TOTAL_POLLING_PLACES.intValue())))
            .andExpect(jsonPath("$.[*].nullVotes").value(hasItem(DEFAULT_NULL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(District.class);
        District district1 = new District();
        district1.setId(1L);
        District district2 = new District();
        district2.setId(district1.getId());
        assertThat(district1).isEqualTo(district2);
        district2.setId(2L);
        assertThat(district1).isNotEqualTo(district2);
        district1.setId(null);
        assertThat(district1).isNotEqualTo(district2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DistrictDTO.class);
        DistrictDTO districtDTO1 = new DistrictDTO();
        districtDTO1.setId(1L);
        DistrictDTO districtDTO2 = new DistrictDTO();
        assertThat(districtDTO1).isNotEqualTo(districtDTO2);
        districtDTO2.setId(districtDTO1.getId());
        assertThat(districtDTO1).isEqualTo(districtDTO2);
        districtDTO2.setId(2L);
        assertThat(districtDTO1).isNotEqualTo(districtDTO2);
        districtDTO1.setId(null);
        assertThat(districtDTO1).isNotEqualTo(districtDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(districtMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(districtMapper.fromId(null)).isNull();
    }
}
