package electorum.sidener.web.rest;

import electorum.sidener.SidenerApp;

import electorum.sidener.domain.Archive;
import electorum.sidener.repository.ArchiveRepository;
import electorum.sidener.service.ArchiveService;
import electorum.sidener.repository.search.ArchiveSearchRepository;
import electorum.sidener.service.dto.ArchiveDTO;
import electorum.sidener.service.mapper.ArchiveMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import electorum.sidener.domain.enumeration.ArchiveStatus;
/**
 * Test class for the ArchiveResource REST controller.
 *
 * @see ArchiveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SidenerApp.class)
public class ArchiveResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZE_LENGTH = 1L;
    private static final Long UPDATED_SIZE_LENGTH = 2L;

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final ArchiveStatus DEFAULT_STATUS = ArchiveStatus.TEMPORARY;
    private static final ArchiveStatus UPDATED_STATUS = ArchiveStatus.PERMANENT;

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private ArchiveMapper archiveMapper;

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private ArchiveSearchRepository archiveSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArchiveMockMvc;

    private Archive archive;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArchiveResource archiveResource = new ArchiveResource(archiveService);
        this.restArchiveMockMvc = MockMvcBuilders.standaloneSetup(archiveResource)
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
    public static Archive createEntity(EntityManager em) {
        Archive archive = new Archive()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .mimeType(DEFAULT_MIME_TYPE)
            .sizeLength(DEFAULT_SIZE_LENGTH)
            .path(DEFAULT_PATH)
            .status(DEFAULT_STATUS);
        return archive;
    }

    @Before
    public void initTest() {
        archiveSearchRepository.deleteAll();
        archive = createEntity(em);
    }

    @Test
    @Transactional
    public void createArchive() throws Exception {
        int databaseSizeBeforeCreate = archiveRepository.findAll().size();

        // Create the Archive
        ArchiveDTO archiveDTO = archiveMapper.toDto(archive);
        restArchiveMockMvc.perform(post("/api/archives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(archiveDTO)))
            .andExpect(status().isCreated());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeCreate + 1);
        Archive testArchive = archiveList.get(archiveList.size() - 1);
        assertThat(testArchive.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArchive.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArchive.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
        assertThat(testArchive.getSizeLength()).isEqualTo(DEFAULT_SIZE_LENGTH);
        assertThat(testArchive.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testArchive.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Archive in Elasticsearch
        Archive archiveEs = archiveSearchRepository.findOne(testArchive.getId());
        assertThat(archiveEs).isEqualToComparingFieldByField(testArchive);
    }

    @Test
    @Transactional
    public void createArchiveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = archiveRepository.findAll().size();

        // Create the Archive with an existing ID
        archive.setId(1L);
        ArchiveDTO archiveDTO = archiveMapper.toDto(archive);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArchiveMockMvc.perform(post("/api/archives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(archiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllArchives() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);

        // Get all the archiveList
        restArchiveMockMvc.perform(get("/api/archives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archive.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sizeLength").value(hasItem(DEFAULT_SIZE_LENGTH.intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getArchive() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);

        // Get the archive
        restArchiveMockMvc.perform(get("/api/archives/{id}", archive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(archive.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE.toString()))
            .andExpect(jsonPath("$.sizeLength").value(DEFAULT_SIZE_LENGTH.intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArchive() throws Exception {
        // Get the archive
        restArchiveMockMvc.perform(get("/api/archives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArchive() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);
        archiveSearchRepository.save(archive);
        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();

        // Update the archive
        Archive updatedArchive = archiveRepository.findOne(archive.getId());
        updatedArchive
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mimeType(UPDATED_MIME_TYPE)
            .sizeLength(UPDATED_SIZE_LENGTH)
            .path(UPDATED_PATH)
            .status(UPDATED_STATUS);
        ArchiveDTO archiveDTO = archiveMapper.toDto(updatedArchive);

        restArchiveMockMvc.perform(put("/api/archives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(archiveDTO)))
            .andExpect(status().isOk());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
        Archive testArchive = archiveList.get(archiveList.size() - 1);
        assertThat(testArchive.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArchive.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArchive.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testArchive.getSizeLength()).isEqualTo(UPDATED_SIZE_LENGTH);
        assertThat(testArchive.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testArchive.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Archive in Elasticsearch
        Archive archiveEs = archiveSearchRepository.findOne(testArchive.getId());
        assertThat(archiveEs).isEqualToComparingFieldByField(testArchive);
    }

    @Test
    @Transactional
    public void updateNonExistingArchive() throws Exception {
        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();

        // Create the Archive
        ArchiveDTO archiveDTO = archiveMapper.toDto(archive);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArchiveMockMvc.perform(put("/api/archives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(archiveDTO)))
            .andExpect(status().isCreated());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArchive() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);
        archiveSearchRepository.save(archive);
        int databaseSizeBeforeDelete = archiveRepository.findAll().size();

        // Get the archive
        restArchiveMockMvc.perform(delete("/api/archives/{id}", archive.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean archiveExistsInEs = archiveSearchRepository.exists(archive.getId());
        assertThat(archiveExistsInEs).isFalse();

        // Validate the database is empty
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchArchive() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);
        archiveSearchRepository.save(archive);

        // Search the archive
        restArchiveMockMvc.perform(get("/api/_search/archives?query=id:" + archive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archive.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sizeLength").value(hasItem(DEFAULT_SIZE_LENGTH.intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Archive.class);
        Archive archive1 = new Archive();
        archive1.setId(1L);
        Archive archive2 = new Archive();
        archive2.setId(archive1.getId());
        assertThat(archive1).isEqualTo(archive2);
        archive2.setId(2L);
        assertThat(archive1).isNotEqualTo(archive2);
        archive1.setId(null);
        assertThat(archive1).isNotEqualTo(archive2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchiveDTO.class);
        ArchiveDTO archiveDTO1 = new ArchiveDTO();
        archiveDTO1.setId(1L);
        ArchiveDTO archiveDTO2 = new ArchiveDTO();
        assertThat(archiveDTO1).isNotEqualTo(archiveDTO2);
        archiveDTO2.setId(archiveDTO1.getId());
        assertThat(archiveDTO1).isEqualTo(archiveDTO2);
        archiveDTO2.setId(2L);
        assertThat(archiveDTO1).isNotEqualTo(archiveDTO2);
        archiveDTO1.setId(null);
        assertThat(archiveDTO1).isNotEqualTo(archiveDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(archiveMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(archiveMapper.fromId(null)).isNull();
    }
}
