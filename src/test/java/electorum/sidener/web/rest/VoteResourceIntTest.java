package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.Vote;
import electorum.sidener.repository.VoteRepository;
import electorum.sidener.service.VoteService;
import electorum.sidener.repository.search.VoteSearchRepository;
import electorum.sidener.service.dto.VoteDTO;
import electorum.sidener.service.mapper.VoteMapper;
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
 * Test class for the VoteResource REST controller.
 *
 * @see VoteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class VoteResourceIntTest {

    private static final Long DEFAULT_TOTAL = 1L;
    private static final Long UPDATED_TOTAL = 2L;

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteSearchRepository voteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVoteMockMvc;

    private Vote vote;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoteResource voteResource = new VoteResource(voteService);
        this.restVoteMockMvc = MockMvcBuilders.standaloneSetup(voteResource)
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
    public static Vote createEntity(EntityManager em) {
        Vote vote = new Vote()
            .total(DEFAULT_TOTAL)
            .published(DEFAULT_PUBLISHED)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return vote;
    }

    @Before
    public void initTest() {
        voteSearchRepository.deleteAll();
        vote = createEntity(em);
    }

    @Test
    @Transactional
    public void createVote() throws Exception {
        int databaseSizeBeforeCreate = voteRepository.findAll().size();

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);
        restVoteMockMvc.perform(post("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isCreated());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeCreate + 1);
        Vote testVote = voteList.get(voteList.size() - 1);
        assertThat(testVote.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testVote.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testVote.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testVote.getUpdated()).isEqualTo(DEFAULT_UPDATED);

        // Validate the Vote in Elasticsearch
        Vote voteEs = voteSearchRepository.findOne(testVote.getId());
        assertThat(voteEs).isEqualToComparingFieldByField(testVote);
    }

    @Test
    @Transactional
    public void createVoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voteRepository.findAll().size();

        // Create the Vote with an existing ID
        vote.setId(1L);
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteMockMvc.perform(post("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVotes() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList
        restVoteMockMvc.perform(get("/api/votes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vote.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }

    @Test
    @Transactional
    public void getVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get the vote
        restVoteMockMvc.perform(get("/api/votes/{id}", vote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vote.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)));
    }

    @Test
    @Transactional
    public void getNonExistingVote() throws Exception {
        // Get the vote
        restVoteMockMvc.perform(get("/api/votes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);
        voteSearchRepository.save(vote);
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();

        // Update the vote
        Vote updatedVote = voteRepository.findOne(vote.getId());
        updatedVote
            .total(UPDATED_TOTAL)
            .published(UPDATED_PUBLISHED)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        VoteDTO voteDTO = voteMapper.toDto(updatedVote);

        restVoteMockMvc.perform(put("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isOk());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
        Vote testVote = voteList.get(voteList.size() - 1);
        assertThat(testVote.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testVote.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testVote.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVote.getUpdated()).isEqualTo(UPDATED_UPDATED);

        // Validate the Vote in Elasticsearch
        Vote voteEs = voteSearchRepository.findOne(testVote.getId());
        assertThat(voteEs).isEqualToComparingFieldByField(testVote);
    }

    @Test
    @Transactional
    public void updateNonExistingVote() throws Exception {
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVoteMockMvc.perform(put("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isCreated());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);
        voteSearchRepository.save(vote);
        int databaseSizeBeforeDelete = voteRepository.findAll().size();

        // Get the vote
        restVoteMockMvc.perform(delete("/api/votes/{id}", vote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean voteExistsInEs = voteSearchRepository.exists(vote.getId());
        assertThat(voteExistsInEs).isFalse();

        // Validate the database is empty
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);
        voteSearchRepository.save(vote);

        // Search the vote
        restVoteMockMvc.perform(get("/api/_search/votes?query=id:" + vote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vote.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vote.class);
        Vote vote1 = new Vote();
        vote1.setId(1L);
        Vote vote2 = new Vote();
        vote2.setId(vote1.getId());
        assertThat(vote1).isEqualTo(vote2);
        vote2.setId(2L);
        assertThat(vote1).isNotEqualTo(vote2);
        vote1.setId(null);
        assertThat(vote1).isNotEqualTo(vote2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoteDTO.class);
        VoteDTO voteDTO1 = new VoteDTO();
        voteDTO1.setId(1L);
        VoteDTO voteDTO2 = new VoteDTO();
        assertThat(voteDTO1).isNotEqualTo(voteDTO2);
        voteDTO2.setId(voteDTO1.getId());
        assertThat(voteDTO1).isEqualTo(voteDTO2);
        voteDTO2.setId(2L);
        assertThat(voteDTO1).isNotEqualTo(voteDTO2);
        voteDTO1.setId(null);
        assertThat(voteDTO1).isNotEqualTo(voteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(voteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(voteMapper.fromId(null)).isNull();
    }
}
