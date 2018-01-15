package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.PollingPlace;
import electorum.sidener.repository.PollingPlaceRepository;
import electorum.sidener.service.PollingPlaceService;
import electorum.sidener.repository.search.PollingPlaceSearchRepository;
import electorum.sidener.service.dto.PollingPlaceDTO;
import electorum.sidener.service.mapper.PollingPlaceMapper;
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

import electorum.sidener.domain.enumeration.TypePollingPlace;
/**
 * Test class for the PollingPlaceResource REST controller.
 *
 * @see PollingPlaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class PollingPlaceResourceIntTest {

    private static final String DEFAULT_TOWN = "AAAAAAAAAA";
    private static final String UPDATED_TOWN = "BBBBBBBBBB";

    private static final TypePollingPlace DEFAULT_TYPE_POLLING_PLACE = TypePollingPlace.BASIC;
    private static final TypePollingPlace UPDATED_TYPE_POLLING_PLACE = TypePollingPlace.CONTIGUOUS;

    private static final String DEFAULT_TYPE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_LEFTOVER_BALLOTS = 1L;
    private static final Long UPDATED_LEFTOVER_BALLOTS = 2L;

    private static final Long DEFAULT_VOTING_CITIZENS = 1L;
    private static final Long UPDATED_VOTING_CITIZENS = 2L;

    private static final Long DEFAULT_EXCTRACTED_BALLOTS = 1L;
    private static final Long UPDATED_EXCTRACTED_BALLOTS = 2L;

    private static final Long DEFAULT_NOT_REGISTERED = 1L;
    private static final Long UPDATED_NOT_REGISTERED = 2L;

    private static final Long DEFAULT_NULL_VOTES = 1L;
    private static final Long UPDATED_NULL_VOTES = 2L;

    private static final Long DEFAULT_TOTAL_VOTES = 1L;
    private static final Long UPDATED_TOTAL_VOTES = 2L;

    private static final Long DEFAULT_ELECTORAL_ROLL = 1L;
    private static final Long UPDATED_ELECTORAL_ROLL = 2L;

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_PRESIDENT = "AAAAAAAAAA";
    private static final String UPDATED_PRESIDENT = "BBBBBBBBBB";

    private static final String DEFAULT_SECRETARY = "AAAAAAAAAA";
    private static final String UPDATED_SECRETARY = "BBBBBBBBBB";

    private static final String DEFAULT_SCRUTINEER_ONE = "AAAAAAAAAA";
    private static final String UPDATED_SCRUTINEER_ONE = "BBBBBBBBBB";

    private static final String DEFAULT_SCRUTINEER_TWO = "AAAAAAAAAA";
    private static final String UPDATED_SCRUTINEER_TWO = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATE_ONE = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATE_ONE = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATE_TWO = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATE_TWO = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATE_THREE = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATE_THREE = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_COUNT = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_FIRST_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_FIRST_PLACE = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_FIRST_PLACE = 1L;
    private static final Long UPDATED_TOTAL_FIRST_PLACE = 2L;

    private static final String DEFAULT_ENTITY_SECOND_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_SECOND_PLACE = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_SECOND_PLACE = 1L;
    private static final Long UPDATED_TOTAL_SECOND_PLACE = 2L;

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private PollingPlaceRepository pollingPlaceRepository;

    @Autowired
    private PollingPlaceMapper pollingPlaceMapper;

    @Autowired
    private PollingPlaceService pollingPlaceService;

    @Autowired
    private PollingPlaceSearchRepository pollingPlaceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPollingPlaceMockMvc;

    private PollingPlace pollingPlace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PollingPlaceResource pollingPlaceResource = new PollingPlaceResource(pollingPlaceService);
        this.restPollingPlaceMockMvc = MockMvcBuilders.standaloneSetup(pollingPlaceResource)
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
    public static PollingPlace createEntity(EntityManager em) {
        PollingPlace pollingPlace = new PollingPlace()
            .town(DEFAULT_TOWN)
            .typePollingPlace(DEFAULT_TYPE_POLLING_PLACE)
            .typeNumber(DEFAULT_TYPE_NUMBER)
            .section(DEFAULT_SECTION)
            .address(DEFAULT_ADDRESS)
            .leftoverBallots(DEFAULT_LEFTOVER_BALLOTS)
            .votingCitizens(DEFAULT_VOTING_CITIZENS)
            .exctractedBallots(DEFAULT_EXCTRACTED_BALLOTS)
            .notRegistered(DEFAULT_NOT_REGISTERED)
            .nullVotes(DEFAULT_NULL_VOTES)
            .totalVotes(DEFAULT_TOTAL_VOTES)
            .electoralRoll(DEFAULT_ELECTORAL_ROLL)
            .observations(DEFAULT_OBSERVATIONS)
            .president(DEFAULT_PRESIDENT)
            .secretary(DEFAULT_SECRETARY)
            .scrutineerOne(DEFAULT_SCRUTINEER_ONE)
            .scrutineerTwo(DEFAULT_SCRUTINEER_TWO)
            .alternateOne(DEFAULT_ALTERNATE_ONE)
            .alternateTwo(DEFAULT_ALTERNATE_TWO)
            .alternateThree(DEFAULT_ALTERNATE_THREE)
            .recordCount(DEFAULT_RECORD_COUNT)
            .entityFirstPlace(DEFAULT_ENTITY_FIRST_PLACE)
            .totalFirstPlace(DEFAULT_TOTAL_FIRST_PLACE)
            .entitySecondPlace(DEFAULT_ENTITY_SECOND_PLACE)
            .totalSecondPlace(DEFAULT_TOTAL_SECOND_PLACE)
            .published(DEFAULT_PUBLISHED)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return pollingPlace;
    }

    @Before
    public void initTest() {
        pollingPlaceSearchRepository.deleteAll();
        pollingPlace = createEntity(em);
    }

    @Test
    @Transactional
    public void createPollingPlace() throws Exception {
        int databaseSizeBeforeCreate = pollingPlaceRepository.findAll().size();

        // Create the PollingPlace
        PollingPlaceDTO pollingPlaceDTO = pollingPlaceMapper.toDto(pollingPlace);
        restPollingPlaceMockMvc.perform(post("/api/polling-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pollingPlaceDTO)))
            .andExpect(status().isCreated());

        // Validate the PollingPlace in the database
        List<PollingPlace> pollingPlaceList = pollingPlaceRepository.findAll();
        assertThat(pollingPlaceList).hasSize(databaseSizeBeforeCreate + 1);
        PollingPlace testPollingPlace = pollingPlaceList.get(pollingPlaceList.size() - 1);
        assertThat(testPollingPlace.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testPollingPlace.getTypePollingPlace()).isEqualTo(DEFAULT_TYPE_POLLING_PLACE);
        assertThat(testPollingPlace.getTypeNumber()).isEqualTo(DEFAULT_TYPE_NUMBER);
        assertThat(testPollingPlace.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testPollingPlace.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPollingPlace.getLeftoverBallots()).isEqualTo(DEFAULT_LEFTOVER_BALLOTS);
        assertThat(testPollingPlace.getVotingCitizens()).isEqualTo(DEFAULT_VOTING_CITIZENS);
        assertThat(testPollingPlace.getExctractedBallots()).isEqualTo(DEFAULT_EXCTRACTED_BALLOTS);
        assertThat(testPollingPlace.getNotRegistered()).isEqualTo(DEFAULT_NOT_REGISTERED);
        assertThat(testPollingPlace.getNullVotes()).isEqualTo(DEFAULT_NULL_VOTES);
        assertThat(testPollingPlace.getTotalVotes()).isEqualTo(DEFAULT_TOTAL_VOTES);
        assertThat(testPollingPlace.getElectoralRoll()).isEqualTo(DEFAULT_ELECTORAL_ROLL);
        assertThat(testPollingPlace.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
        assertThat(testPollingPlace.getPresident()).isEqualTo(DEFAULT_PRESIDENT);
        assertThat(testPollingPlace.getSecretary()).isEqualTo(DEFAULT_SECRETARY);
        assertThat(testPollingPlace.getScrutineerOne()).isEqualTo(DEFAULT_SCRUTINEER_ONE);
        assertThat(testPollingPlace.getScrutineerTwo()).isEqualTo(DEFAULT_SCRUTINEER_TWO);
        assertThat(testPollingPlace.getAlternateOne()).isEqualTo(DEFAULT_ALTERNATE_ONE);
        assertThat(testPollingPlace.getAlternateTwo()).isEqualTo(DEFAULT_ALTERNATE_TWO);
        assertThat(testPollingPlace.getAlternateThree()).isEqualTo(DEFAULT_ALTERNATE_THREE);
        assertThat(testPollingPlace.getRecordCount()).isEqualTo(DEFAULT_RECORD_COUNT);
        assertThat(testPollingPlace.getEntityFirstPlace()).isEqualTo(DEFAULT_ENTITY_FIRST_PLACE);
        assertThat(testPollingPlace.getTotalFirstPlace()).isEqualTo(DEFAULT_TOTAL_FIRST_PLACE);
        assertThat(testPollingPlace.getEntitySecondPlace()).isEqualTo(DEFAULT_ENTITY_SECOND_PLACE);
        assertThat(testPollingPlace.getTotalSecondPlace()).isEqualTo(DEFAULT_TOTAL_SECOND_PLACE);
        assertThat(testPollingPlace.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testPollingPlace.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPollingPlace.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);

        // Validate the PollingPlace in Elasticsearch
        PollingPlace pollingPlaceEs = pollingPlaceSearchRepository.findOne(testPollingPlace.getId());
        assertThat(pollingPlaceEs).isEqualToComparingFieldByField(testPollingPlace);
    }

    @Test
    @Transactional
    public void createPollingPlaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pollingPlaceRepository.findAll().size();

        // Create the PollingPlace with an existing ID
        pollingPlace.setId(1L);
        PollingPlaceDTO pollingPlaceDTO = pollingPlaceMapper.toDto(pollingPlace);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollingPlaceMockMvc.perform(post("/api/polling-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pollingPlaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PollingPlace in the database
        List<PollingPlace> pollingPlaceList = pollingPlaceRepository.findAll();
        assertThat(pollingPlaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPollingPlaces() throws Exception {
        // Initialize the database
        pollingPlaceRepository.saveAndFlush(pollingPlace);

        // Get all the pollingPlaceList
        restPollingPlaceMockMvc.perform(get("/api/polling-places?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollingPlace.getId().intValue())))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN.toString())))
            .andExpect(jsonPath("$.[*].typePollingPlace").value(hasItem(DEFAULT_TYPE_POLLING_PLACE.toString())))
            .andExpect(jsonPath("$.[*].typeNumber").value(hasItem(DEFAULT_TYPE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].leftoverBallots").value(hasItem(DEFAULT_LEFTOVER_BALLOTS.intValue())))
            .andExpect(jsonPath("$.[*].votingCitizens").value(hasItem(DEFAULT_VOTING_CITIZENS.intValue())))
            .andExpect(jsonPath("$.[*].exctractedBallots").value(hasItem(DEFAULT_EXCTRACTED_BALLOTS.intValue())))
            .andExpect(jsonPath("$.[*].notRegistered").value(hasItem(DEFAULT_NOT_REGISTERED.intValue())))
            .andExpect(jsonPath("$.[*].nullVotes").value(hasItem(DEFAULT_NULL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].totalVotes").value(hasItem(DEFAULT_TOTAL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].electoralRoll").value(hasItem(DEFAULT_ELECTORAL_ROLL.intValue())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].president").value(hasItem(DEFAULT_PRESIDENT.toString())))
            .andExpect(jsonPath("$.[*].secretary").value(hasItem(DEFAULT_SECRETARY.toString())))
            .andExpect(jsonPath("$.[*].scrutineerOne").value(hasItem(DEFAULT_SCRUTINEER_ONE.toString())))
            .andExpect(jsonPath("$.[*].scrutineerTwo").value(hasItem(DEFAULT_SCRUTINEER_TWO.toString())))
            .andExpect(jsonPath("$.[*].alternateOne").value(hasItem(DEFAULT_ALTERNATE_ONE.toString())))
            .andExpect(jsonPath("$.[*].alternateTwo").value(hasItem(DEFAULT_ALTERNATE_TWO.toString())))
            .andExpect(jsonPath("$.[*].alternateThree").value(hasItem(DEFAULT_ALTERNATE_THREE.toString())))
            .andExpect(jsonPath("$.[*].recordCount").value(hasItem(DEFAULT_RECORD_COUNT.toString())))
            .andExpect(jsonPath("$.[*].entityFirstPlace").value(hasItem(DEFAULT_ENTITY_FIRST_PLACE.toString())))
            .andExpect(jsonPath("$.[*].totalFirstPlace").value(hasItem(DEFAULT_TOTAL_FIRST_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].entitySecondPlace").value(hasItem(DEFAULT_ENTITY_SECOND_PLACE.toString())))
            .andExpect(jsonPath("$.[*].totalSecondPlace").value(hasItem(DEFAULT_TOTAL_SECOND_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void getPollingPlace() throws Exception {
        // Initialize the database
        pollingPlaceRepository.saveAndFlush(pollingPlace);

        // Get the pollingPlace
        restPollingPlaceMockMvc.perform(get("/api/polling-places/{id}", pollingPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pollingPlace.getId().intValue()))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN.toString()))
            .andExpect(jsonPath("$.typePollingPlace").value(DEFAULT_TYPE_POLLING_PLACE.toString()))
            .andExpect(jsonPath("$.typeNumber").value(DEFAULT_TYPE_NUMBER.toString()))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.leftoverBallots").value(DEFAULT_LEFTOVER_BALLOTS.intValue()))
            .andExpect(jsonPath("$.votingCitizens").value(DEFAULT_VOTING_CITIZENS.intValue()))
            .andExpect(jsonPath("$.exctractedBallots").value(DEFAULT_EXCTRACTED_BALLOTS.intValue()))
            .andExpect(jsonPath("$.notRegistered").value(DEFAULT_NOT_REGISTERED.intValue()))
            .andExpect(jsonPath("$.nullVotes").value(DEFAULT_NULL_VOTES.intValue()))
            .andExpect(jsonPath("$.totalVotes").value(DEFAULT_TOTAL_VOTES.intValue()))
            .andExpect(jsonPath("$.electoralRoll").value(DEFAULT_ELECTORAL_ROLL.intValue()))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS.toString()))
            .andExpect(jsonPath("$.president").value(DEFAULT_PRESIDENT.toString()))
            .andExpect(jsonPath("$.secretary").value(DEFAULT_SECRETARY.toString()))
            .andExpect(jsonPath("$.scrutineerOne").value(DEFAULT_SCRUTINEER_ONE.toString()))
            .andExpect(jsonPath("$.scrutineerTwo").value(DEFAULT_SCRUTINEER_TWO.toString()))
            .andExpect(jsonPath("$.alternateOne").value(DEFAULT_ALTERNATE_ONE.toString()))
            .andExpect(jsonPath("$.alternateTwo").value(DEFAULT_ALTERNATE_TWO.toString()))
            .andExpect(jsonPath("$.alternateThree").value(DEFAULT_ALTERNATE_THREE.toString()))
            .andExpect(jsonPath("$.recordCount").value(DEFAULT_RECORD_COUNT.toString()))
            .andExpect(jsonPath("$.entityFirstPlace").value(DEFAULT_ENTITY_FIRST_PLACE.toString()))
            .andExpect(jsonPath("$.totalFirstPlace").value(DEFAULT_TOTAL_FIRST_PLACE.intValue()))
            .andExpect(jsonPath("$.entitySecondPlace").value(DEFAULT_ENTITY_SECOND_PLACE.toString()))
            .andExpect(jsonPath("$.totalSecondPlace").value(DEFAULT_TOTAL_SECOND_PLACE.intValue()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingPollingPlace() throws Exception {
        // Get the pollingPlace
        restPollingPlaceMockMvc.perform(get("/api/polling-places/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePollingPlace() throws Exception {
        // Initialize the database
        pollingPlaceRepository.saveAndFlush(pollingPlace);
        pollingPlaceSearchRepository.save(pollingPlace);
        int databaseSizeBeforeUpdate = pollingPlaceRepository.findAll().size();

        // Update the pollingPlace
        PollingPlace updatedPollingPlace = pollingPlaceRepository.findOne(pollingPlace.getId());
        updatedPollingPlace
            .town(UPDATED_TOWN)
            .typePollingPlace(UPDATED_TYPE_POLLING_PLACE)
            .typeNumber(UPDATED_TYPE_NUMBER)
            .section(UPDATED_SECTION)
            .address(UPDATED_ADDRESS)
            .leftoverBallots(UPDATED_LEFTOVER_BALLOTS)
            .votingCitizens(UPDATED_VOTING_CITIZENS)
            .exctractedBallots(UPDATED_EXCTRACTED_BALLOTS)
            .notRegistered(UPDATED_NOT_REGISTERED)
            .nullVotes(UPDATED_NULL_VOTES)
            .totalVotes(UPDATED_TOTAL_VOTES)
            .electoralRoll(UPDATED_ELECTORAL_ROLL)
            .observations(UPDATED_OBSERVATIONS)
            .president(UPDATED_PRESIDENT)
            .secretary(UPDATED_SECRETARY)
            .scrutineerOne(UPDATED_SCRUTINEER_ONE)
            .scrutineerTwo(UPDATED_SCRUTINEER_TWO)
            .alternateOne(UPDATED_ALTERNATE_ONE)
            .alternateTwo(UPDATED_ALTERNATE_TWO)
            .alternateThree(UPDATED_ALTERNATE_THREE)
            .recordCount(UPDATED_RECORD_COUNT)
            .entityFirstPlace(UPDATED_ENTITY_FIRST_PLACE)
            .totalFirstPlace(UPDATED_TOTAL_FIRST_PLACE)
            .entitySecondPlace(UPDATED_ENTITY_SECOND_PLACE)
            .totalSecondPlace(UPDATED_TOTAL_SECOND_PLACE)
            .published(UPDATED_PUBLISHED)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        PollingPlaceDTO pollingPlaceDTO = pollingPlaceMapper.toDto(updatedPollingPlace);

        restPollingPlaceMockMvc.perform(put("/api/polling-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pollingPlaceDTO)))
            .andExpect(status().isOk());

        // Validate the PollingPlace in the database
        List<PollingPlace> pollingPlaceList = pollingPlaceRepository.findAll();
        assertThat(pollingPlaceList).hasSize(databaseSizeBeforeUpdate);
        PollingPlace testPollingPlace = pollingPlaceList.get(pollingPlaceList.size() - 1);
        assertThat(testPollingPlace.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testPollingPlace.getTypePollingPlace()).isEqualTo(UPDATED_TYPE_POLLING_PLACE);
        assertThat(testPollingPlace.getTypeNumber()).isEqualTo(UPDATED_TYPE_NUMBER);
        assertThat(testPollingPlace.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testPollingPlace.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPollingPlace.getLeftoverBallots()).isEqualTo(UPDATED_LEFTOVER_BALLOTS);
        assertThat(testPollingPlace.getVotingCitizens()).isEqualTo(UPDATED_VOTING_CITIZENS);
        assertThat(testPollingPlace.getExctractedBallots()).isEqualTo(UPDATED_EXCTRACTED_BALLOTS);
        assertThat(testPollingPlace.getNotRegistered()).isEqualTo(UPDATED_NOT_REGISTERED);
        assertThat(testPollingPlace.getNullVotes()).isEqualTo(UPDATED_NULL_VOTES);
        assertThat(testPollingPlace.getTotalVotes()).isEqualTo(UPDATED_TOTAL_VOTES);
        assertThat(testPollingPlace.getElectoralRoll()).isEqualTo(UPDATED_ELECTORAL_ROLL);
        assertThat(testPollingPlace.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
        assertThat(testPollingPlace.getPresident()).isEqualTo(UPDATED_PRESIDENT);
        assertThat(testPollingPlace.getSecretary()).isEqualTo(UPDATED_SECRETARY);
        assertThat(testPollingPlace.getScrutineerOne()).isEqualTo(UPDATED_SCRUTINEER_ONE);
        assertThat(testPollingPlace.getScrutineerTwo()).isEqualTo(UPDATED_SCRUTINEER_TWO);
        assertThat(testPollingPlace.getAlternateOne()).isEqualTo(UPDATED_ALTERNATE_ONE);
        assertThat(testPollingPlace.getAlternateTwo()).isEqualTo(UPDATED_ALTERNATE_TWO);
        assertThat(testPollingPlace.getAlternateThree()).isEqualTo(UPDATED_ALTERNATE_THREE);
        assertThat(testPollingPlace.getRecordCount()).isEqualTo(UPDATED_RECORD_COUNT);
        assertThat(testPollingPlace.getEntityFirstPlace()).isEqualTo(UPDATED_ENTITY_FIRST_PLACE);
        assertThat(testPollingPlace.getTotalFirstPlace()).isEqualTo(UPDATED_TOTAL_FIRST_PLACE);
        assertThat(testPollingPlace.getEntitySecondPlace()).isEqualTo(UPDATED_ENTITY_SECOND_PLACE);
        assertThat(testPollingPlace.getTotalSecondPlace()).isEqualTo(UPDATED_TOTAL_SECOND_PLACE);
        assertThat(testPollingPlace.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testPollingPlace.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPollingPlace.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);

        // Validate the PollingPlace in Elasticsearch
        PollingPlace pollingPlaceEs = pollingPlaceSearchRepository.findOne(testPollingPlace.getId());
        assertThat(pollingPlaceEs).isEqualToComparingFieldByField(testPollingPlace);
    }

    @Test
    @Transactional
    public void updateNonExistingPollingPlace() throws Exception {
        int databaseSizeBeforeUpdate = pollingPlaceRepository.findAll().size();

        // Create the PollingPlace
        PollingPlaceDTO pollingPlaceDTO = pollingPlaceMapper.toDto(pollingPlace);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPollingPlaceMockMvc.perform(put("/api/polling-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pollingPlaceDTO)))
            .andExpect(status().isCreated());

        // Validate the PollingPlace in the database
        List<PollingPlace> pollingPlaceList = pollingPlaceRepository.findAll();
        assertThat(pollingPlaceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePollingPlace() throws Exception {
        // Initialize the database
        pollingPlaceRepository.saveAndFlush(pollingPlace);
        pollingPlaceSearchRepository.save(pollingPlace);
        int databaseSizeBeforeDelete = pollingPlaceRepository.findAll().size();

        // Get the pollingPlace
        restPollingPlaceMockMvc.perform(delete("/api/polling-places/{id}", pollingPlace.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pollingPlaceExistsInEs = pollingPlaceSearchRepository.exists(pollingPlace.getId());
        assertThat(pollingPlaceExistsInEs).isFalse();

        // Validate the database is empty
        List<PollingPlace> pollingPlaceList = pollingPlaceRepository.findAll();
        assertThat(pollingPlaceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPollingPlace() throws Exception {
        // Initialize the database
        pollingPlaceRepository.saveAndFlush(pollingPlace);
        pollingPlaceSearchRepository.save(pollingPlace);

        // Search the pollingPlace
        restPollingPlaceMockMvc.perform(get("/api/_search/polling-places?query=id:" + pollingPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollingPlace.getId().intValue())))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN.toString())))
            .andExpect(jsonPath("$.[*].typePollingPlace").value(hasItem(DEFAULT_TYPE_POLLING_PLACE.toString())))
            .andExpect(jsonPath("$.[*].typeNumber").value(hasItem(DEFAULT_TYPE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].leftoverBallots").value(hasItem(DEFAULT_LEFTOVER_BALLOTS.intValue())))
            .andExpect(jsonPath("$.[*].votingCitizens").value(hasItem(DEFAULT_VOTING_CITIZENS.intValue())))
            .andExpect(jsonPath("$.[*].exctractedBallots").value(hasItem(DEFAULT_EXCTRACTED_BALLOTS.intValue())))
            .andExpect(jsonPath("$.[*].notRegistered").value(hasItem(DEFAULT_NOT_REGISTERED.intValue())))
            .andExpect(jsonPath("$.[*].nullVotes").value(hasItem(DEFAULT_NULL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].totalVotes").value(hasItem(DEFAULT_TOTAL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].electoralRoll").value(hasItem(DEFAULT_ELECTORAL_ROLL.intValue())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].president").value(hasItem(DEFAULT_PRESIDENT.toString())))
            .andExpect(jsonPath("$.[*].secretary").value(hasItem(DEFAULT_SECRETARY.toString())))
            .andExpect(jsonPath("$.[*].scrutineerOne").value(hasItem(DEFAULT_SCRUTINEER_ONE.toString())))
            .andExpect(jsonPath("$.[*].scrutineerTwo").value(hasItem(DEFAULT_SCRUTINEER_TWO.toString())))
            .andExpect(jsonPath("$.[*].alternateOne").value(hasItem(DEFAULT_ALTERNATE_ONE.toString())))
            .andExpect(jsonPath("$.[*].alternateTwo").value(hasItem(DEFAULT_ALTERNATE_TWO.toString())))
            .andExpect(jsonPath("$.[*].alternateThree").value(hasItem(DEFAULT_ALTERNATE_THREE.toString())))
            .andExpect(jsonPath("$.[*].recordCount").value(hasItem(DEFAULT_RECORD_COUNT.toString())))
            .andExpect(jsonPath("$.[*].entityFirstPlace").value(hasItem(DEFAULT_ENTITY_FIRST_PLACE.toString())))
            .andExpect(jsonPath("$.[*].totalFirstPlace").value(hasItem(DEFAULT_TOTAL_FIRST_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].entitySecondPlace").value(hasItem(DEFAULT_ENTITY_SECOND_PLACE.toString())))
            .andExpect(jsonPath("$.[*].totalSecondPlace").value(hasItem(DEFAULT_TOTAL_SECOND_PLACE.intValue())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollingPlace.class);
        PollingPlace pollingPlace1 = new PollingPlace();
        pollingPlace1.setId(1L);
        PollingPlace pollingPlace2 = new PollingPlace();
        pollingPlace2.setId(pollingPlace1.getId());
        assertThat(pollingPlace1).isEqualTo(pollingPlace2);
        pollingPlace2.setId(2L);
        assertThat(pollingPlace1).isNotEqualTo(pollingPlace2);
        pollingPlace1.setId(null);
        assertThat(pollingPlace1).isNotEqualTo(pollingPlace2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollingPlaceDTO.class);
        PollingPlaceDTO pollingPlaceDTO1 = new PollingPlaceDTO();
        pollingPlaceDTO1.setId(1L);
        PollingPlaceDTO pollingPlaceDTO2 = new PollingPlaceDTO();
        assertThat(pollingPlaceDTO1).isNotEqualTo(pollingPlaceDTO2);
        pollingPlaceDTO2.setId(pollingPlaceDTO1.getId());
        assertThat(pollingPlaceDTO1).isEqualTo(pollingPlaceDTO2);
        pollingPlaceDTO2.setId(2L);
        assertThat(pollingPlaceDTO1).isNotEqualTo(pollingPlaceDTO2);
        pollingPlaceDTO1.setId(null);
        assertThat(pollingPlaceDTO1).isNotEqualTo(pollingPlaceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pollingPlaceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pollingPlaceMapper.fromId(null)).isNull();
    }
}
