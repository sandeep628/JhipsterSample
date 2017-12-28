package com.beehyv.web.rest;

import com.beehyv.NewApp;

import com.beehyv.domain.Checking;
import com.beehyv.repository.CheckingRepository;
import com.beehyv.web.rest.errors.ExceptionTranslator;

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

import static com.beehyv.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CheckingResource REST controller.
 *
 * @see CheckingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewApp.class)
public class CheckingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCheckingMockMvc;

    private Checking checking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CheckingResource checkingResource = new CheckingResource(checkingRepository);
        this.restCheckingMockMvc = MockMvcBuilders.standaloneSetup(checkingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checking createEntity(EntityManager em) {
        Checking checking = new Checking()
            .name(DEFAULT_NAME);
        return checking;
    }

    @Before
    public void initTest() {
        checking = createEntity(em);
    }

    @Test
    @Transactional
    public void createChecking() throws Exception {
        int databaseSizeBeforeCreate = checkingRepository.findAll().size();

        // Create the Checking
        restCheckingMockMvc.perform(post("/api/checkings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checking)))
            .andExpect(status().isCreated());

        // Validate the Checking in the database
        List<Checking> checkingList = checkingRepository.findAll();
        assertThat(checkingList).hasSize(databaseSizeBeforeCreate + 1);
        Checking testChecking = checkingList.get(checkingList.size() - 1);
        assertThat(testChecking.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCheckingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = checkingRepository.findAll().size();

        // Create the Checking with an existing ID
        checking.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckingMockMvc.perform(post("/api/checkings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checking)))
            .andExpect(status().isBadRequest());

        // Validate the Checking in the database
        List<Checking> checkingList = checkingRepository.findAll();
        assertThat(checkingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCheckings() throws Exception {
        // Initialize the database
        checkingRepository.saveAndFlush(checking);

        // Get all the checkingList
        restCheckingMockMvc.perform(get("/api/checkings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checking.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getChecking() throws Exception {
        // Initialize the database
        checkingRepository.saveAndFlush(checking);

        // Get the checking
        restCheckingMockMvc.perform(get("/api/checkings/{id}", checking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checking.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChecking() throws Exception {
        // Get the checking
        restCheckingMockMvc.perform(get("/api/checkings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChecking() throws Exception {
        // Initialize the database
        checkingRepository.saveAndFlush(checking);
        int databaseSizeBeforeUpdate = checkingRepository.findAll().size();

        // Update the checking
        Checking updatedChecking = checkingRepository.findOne(checking.getId());
        // Disconnect from session so that the updates on updatedChecking are not directly saved in db
        em.detach(updatedChecking);
        updatedChecking
            .name(UPDATED_NAME);

        restCheckingMockMvc.perform(put("/api/checkings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChecking)))
            .andExpect(status().isOk());

        // Validate the Checking in the database
        List<Checking> checkingList = checkingRepository.findAll();
        assertThat(checkingList).hasSize(databaseSizeBeforeUpdate);
        Checking testChecking = checkingList.get(checkingList.size() - 1);
        assertThat(testChecking.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingChecking() throws Exception {
        int databaseSizeBeforeUpdate = checkingRepository.findAll().size();

        // Create the Checking

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCheckingMockMvc.perform(put("/api/checkings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checking)))
            .andExpect(status().isCreated());

        // Validate the Checking in the database
        List<Checking> checkingList = checkingRepository.findAll();
        assertThat(checkingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChecking() throws Exception {
        // Initialize the database
        checkingRepository.saveAndFlush(checking);
        int databaseSizeBeforeDelete = checkingRepository.findAll().size();

        // Get the checking
        restCheckingMockMvc.perform(delete("/api/checkings/{id}", checking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Checking> checkingList = checkingRepository.findAll();
        assertThat(checkingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Checking.class);
        Checking checking1 = new Checking();
        checking1.setId(1L);
        Checking checking2 = new Checking();
        checking2.setId(checking1.getId());
        assertThat(checking1).isEqualTo(checking2);
        checking2.setId(2L);
        assertThat(checking1).isNotEqualTo(checking2);
        checking1.setId(null);
        assertThat(checking1).isNotEqualTo(checking2);
    }
}
