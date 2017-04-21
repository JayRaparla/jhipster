package com.jay.statements.web.rest;

import com.jay.statements.MyappApp;

import com.jay.statements.domain.Wish;
import com.jay.statements.repository.WishRepository;
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
import java.time.LocalDate;
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
 * Test class for the WishResource REST controller.
 *
 * @see WishResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class WishResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ADDED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADDED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_COMPLETE_BY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COMPLETE_BY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWishMockMvc;

    private Wish wish;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WishResource wishResource = new WishResource(wishRepository);
        this.restWishMockMvc = MockMvcBuilders.standaloneSetup(wishResource)
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
    public static Wish createEntity(EntityManager em) {
        Wish wish = new Wish()
            .description(DEFAULT_DESCRIPTION)
            .addedOn(DEFAULT_ADDED_ON)
            .completeBy(DEFAULT_COMPLETE_BY);
        return wish;
    }

    @Before
    public void initTest() {
        wish = createEntity(em);
    }

    @Test
    @Transactional
    public void createWish() throws Exception {
        int databaseSizeBeforeCreate = wishRepository.findAll().size();

        // Create the Wish
        restWishMockMvc.perform(post("/api/wishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isCreated());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeCreate + 1);
        Wish testWish = wishList.get(wishList.size() - 1);
        assertThat(testWish.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWish.getAddedOn()).isEqualTo(DEFAULT_ADDED_ON);
        assertThat(testWish.getCompleteBy()).isEqualTo(DEFAULT_COMPLETE_BY);
    }

    @Test
    @Transactional
    public void createWishWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wishRepository.findAll().size();

        // Create the Wish with an existing ID
        wish.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishMockMvc.perform(post("/api/wishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWishes() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        // Get all the wishList
        restWishMockMvc.perform(get("/api/wishes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wish.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].addedOn").value(hasItem(DEFAULT_ADDED_ON.toString())))
            .andExpect(jsonPath("$.[*].completeBy").value(hasItem(sameInstant(DEFAULT_COMPLETE_BY))));
    }

    @Test
    @Transactional
    public void getWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        // Get the wish
        restWishMockMvc.perform(get("/api/wishes/{id}", wish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wish.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.addedOn").value(DEFAULT_ADDED_ON.toString()))
            .andExpect(jsonPath("$.completeBy").value(sameInstant(DEFAULT_COMPLETE_BY)));
    }

    @Test
    @Transactional
    public void getNonExistingWish() throws Exception {
        // Get the wish
        restWishMockMvc.perform(get("/api/wishes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();

        // Update the wish
        Wish updatedWish = wishRepository.findOne(wish.getId());
        updatedWish
            .description(UPDATED_DESCRIPTION)
            .addedOn(UPDATED_ADDED_ON)
            .completeBy(UPDATED_COMPLETE_BY);

        restWishMockMvc.perform(put("/api/wishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWish)))
            .andExpect(status().isOk());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
        Wish testWish = wishList.get(wishList.size() - 1);
        assertThat(testWish.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWish.getAddedOn()).isEqualTo(UPDATED_ADDED_ON);
        assertThat(testWish.getCompleteBy()).isEqualTo(UPDATED_COMPLETE_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingWish() throws Exception {
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();

        // Create the Wish

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWishMockMvc.perform(put("/api/wishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isCreated());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);
        int databaseSizeBeforeDelete = wishRepository.findAll().size();

        // Get the wish
        restWishMockMvc.perform(delete("/api/wishes/{id}", wish.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wish.class);
    }
}
