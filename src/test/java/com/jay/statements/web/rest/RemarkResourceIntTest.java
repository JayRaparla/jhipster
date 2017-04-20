package com.jay.statements.web.rest;

import com.jay.statements.MyappApp;

import com.jay.statements.domain.Remark;
import com.jay.statements.repository.RemarkRepository;
import com.jay.statements.web.rest.errors.ExceptionTranslator;

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

import static com.jay.statements.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RemarkResource REST controller.
 *
 * @see RemarkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class RemarkResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private RemarkRepository remarkRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRemarkMockMvc;

    private Remark remark;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RemarkResource remarkResource = new RemarkResource(remarkRepository);
        this.restRemarkMockMvc = MockMvcBuilders.standaloneSetup(remarkResource)
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
    public static Remark createEntity(EntityManager em) {
        Remark remark = new Remark()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .date(DEFAULT_DATE);
        return remark;
    }

    @Before
    public void initTest() {
        remark = createEntity(em);
    }

    @Test
    @Transactional
    public void createRemark() throws Exception {
        int databaseSizeBeforeCreate = remarkRepository.findAll().size();

        // Create the Remark
        restRemarkMockMvc.perform(post("/api/remarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remark)))
            .andExpect(status().isCreated());

        // Validate the Remark in the database
        List<Remark> remarkList = remarkRepository.findAll();
        assertThat(remarkList).hasSize(databaseSizeBeforeCreate + 1);
        Remark testRemark = remarkList.get(remarkList.size() - 1);
        assertThat(testRemark.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRemark.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testRemark.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createRemarkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = remarkRepository.findAll().size();

        // Create the Remark with an existing ID
        remark.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRemarkMockMvc.perform(post("/api/remarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remark)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Remark> remarkList = remarkRepository.findAll();
        assertThat(remarkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = remarkRepository.findAll().size();
        // set the field null
        remark.setTitle(null);

        // Create the Remark, which fails.

        restRemarkMockMvc.perform(post("/api/remarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remark)))
            .andExpect(status().isBadRequest());

        List<Remark> remarkList = remarkRepository.findAll();
        assertThat(remarkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = remarkRepository.findAll().size();
        // set the field null
        remark.setContent(null);

        // Create the Remark, which fails.

        restRemarkMockMvc.perform(post("/api/remarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remark)))
            .andExpect(status().isBadRequest());

        List<Remark> remarkList = remarkRepository.findAll();
        assertThat(remarkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = remarkRepository.findAll().size();
        // set the field null
        remark.setDate(null);

        // Create the Remark, which fails.

        restRemarkMockMvc.perform(post("/api/remarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remark)))
            .andExpect(status().isBadRequest());

        List<Remark> remarkList = remarkRepository.findAll();
        assertThat(remarkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRemarks() throws Exception {
        // Initialize the database
        remarkRepository.saveAndFlush(remark);

        // Get all the remarkList
        restRemarkMockMvc.perform(get("/api/remarks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remark.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void getRemark() throws Exception {
        // Initialize the database
        remarkRepository.saveAndFlush(remark);

        // Get the remark
        restRemarkMockMvc.perform(get("/api/remarks/{id}", remark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(remark.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingRemark() throws Exception {
        // Get the remark
        restRemarkMockMvc.perform(get("/api/remarks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRemark() throws Exception {
        // Initialize the database
        remarkRepository.saveAndFlush(remark);
        int databaseSizeBeforeUpdate = remarkRepository.findAll().size();

        // Update the remark
        Remark updatedRemark = remarkRepository.findOne(remark.getId());
        updatedRemark
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .date(UPDATED_DATE);

        restRemarkMockMvc.perform(put("/api/remarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRemark)))
            .andExpect(status().isOk());

        // Validate the Remark in the database
        List<Remark> remarkList = remarkRepository.findAll();
        assertThat(remarkList).hasSize(databaseSizeBeforeUpdate);
        Remark testRemark = remarkList.get(remarkList.size() - 1);
        assertThat(testRemark.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRemark.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRemark.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRemark() throws Exception {
        int databaseSizeBeforeUpdate = remarkRepository.findAll().size();

        // Create the Remark

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRemarkMockMvc.perform(put("/api/remarks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remark)))
            .andExpect(status().isCreated());

        // Validate the Remark in the database
        List<Remark> remarkList = remarkRepository.findAll();
        assertThat(remarkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRemark() throws Exception {
        // Initialize the database
        remarkRepository.saveAndFlush(remark);
        int databaseSizeBeforeDelete = remarkRepository.findAll().size();

        // Get the remark
        restRemarkMockMvc.perform(delete("/api/remarks/{id}", remark.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Remark> remarkList = remarkRepository.findAll();
        assertThat(remarkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Remark.class);
    }
}
