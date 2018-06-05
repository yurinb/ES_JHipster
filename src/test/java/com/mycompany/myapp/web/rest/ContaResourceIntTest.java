package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.EsJhipsterApp;

import com.mycompany.myapp.domain.Conta;
import com.mycompany.myapp.repository.ContaRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContaResource REST controller.
 *
 * @see ContaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsJhipsterApp.class)
public class ContaResourceIntTest {

    private static final String DEFAULT_CONTA = "AAAAAAAAAA";
    private static final String UPDATED_CONTA = "BBBBBBBBBB";

    private static final Long DEFAULT_VALOR = 1L;
    private static final Long UPDATED_VALOR = 2L;

    private static final Integer DEFAULT_DIA_VENCIMENTO = 1;
    private static final Integer UPDATED_DIA_VENCIMENTO = 2;

    private static final Integer DEFAULT_NUM_PARCELAS = 1;
    private static final Integer UPDATED_NUM_PARCELAS = 2;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContaMockMvc;

    private Conta conta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContaResource contaResource = new ContaResource(contaRepository);
        this.restContaMockMvc = MockMvcBuilders.standaloneSetup(contaResource)
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
    public static Conta createEntity(EntityManager em) {
        Conta conta = new Conta()
            .conta(DEFAULT_CONTA)
            .valor(DEFAULT_VALOR)
            .diaVencimento(DEFAULT_DIA_VENCIMENTO)
            .numParcelas(DEFAULT_NUM_PARCELAS);
        return conta;
    }

    @Before
    public void initTest() {
        conta = createEntity(em);
    }

    @Test
    @Transactional
    public void createConta() throws Exception {
        int databaseSizeBeforeCreate = contaRepository.findAll().size();

        // Create the Conta
        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isCreated());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeCreate + 1);
        Conta testConta = contaList.get(contaList.size() - 1);
        assertThat(testConta.getConta()).isEqualTo(DEFAULT_CONTA);
        assertThat(testConta.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testConta.getDiaVencimento()).isEqualTo(DEFAULT_DIA_VENCIMENTO);
        assertThat(testConta.getNumParcelas()).isEqualTo(DEFAULT_NUM_PARCELAS);
    }

    @Test
    @Transactional
    public void createContaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contaRepository.findAll().size();

        // Create the Conta with an existing ID
        conta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaMockMvc.perform(post("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isBadRequest());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContas() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList
        restContaMockMvc.perform(get("/api/contas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conta.getId().intValue())))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].diaVencimento").value(hasItem(DEFAULT_DIA_VENCIMENTO)))
            .andExpect(jsonPath("$.[*].numParcelas").value(hasItem(DEFAULT_NUM_PARCELAS)));
    }

    @Test
    @Transactional
    public void getConta() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get the conta
        restContaMockMvc.perform(get("/api/contas/{id}", conta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conta.getId().intValue()))
            .andExpect(jsonPath("$.conta").value(DEFAULT_CONTA.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.intValue()))
            .andExpect(jsonPath("$.diaVencimento").value(DEFAULT_DIA_VENCIMENTO))
            .andExpect(jsonPath("$.numParcelas").value(DEFAULT_NUM_PARCELAS));
    }

    @Test
    @Transactional
    public void getNonExistingConta() throws Exception {
        // Get the conta
        restContaMockMvc.perform(get("/api/contas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConta() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);
        int databaseSizeBeforeUpdate = contaRepository.findAll().size();

        // Update the conta
        Conta updatedConta = contaRepository.findOne(conta.getId());
        // Disconnect from session so that the updates on updatedConta are not directly saved in db
        em.detach(updatedConta);
        updatedConta
            .conta(UPDATED_CONTA)
            .valor(UPDATED_VALOR)
            .diaVencimento(UPDATED_DIA_VENCIMENTO)
            .numParcelas(UPDATED_NUM_PARCELAS);

        restContaMockMvc.perform(put("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConta)))
            .andExpect(status().isOk());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeUpdate);
        Conta testConta = contaList.get(contaList.size() - 1);
        assertThat(testConta.getConta()).isEqualTo(UPDATED_CONTA);
        assertThat(testConta.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testConta.getDiaVencimento()).isEqualTo(UPDATED_DIA_VENCIMENTO);
        assertThat(testConta.getNumParcelas()).isEqualTo(UPDATED_NUM_PARCELAS);
    }

    @Test
    @Transactional
    public void updateNonExistingConta() throws Exception {
        int databaseSizeBeforeUpdate = contaRepository.findAll().size();

        // Create the Conta

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContaMockMvc.perform(put("/api/contas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conta)))
            .andExpect(status().isCreated());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConta() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);
        int databaseSizeBeforeDelete = contaRepository.findAll().size();

        // Get the conta
        restContaMockMvc.perform(delete("/api/contas/{id}", conta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conta.class);
        Conta conta1 = new Conta();
        conta1.setId(1L);
        Conta conta2 = new Conta();
        conta2.setId(conta1.getId());
        assertThat(conta1).isEqualTo(conta2);
        conta2.setId(2L);
        assertThat(conta1).isNotEqualTo(conta2);
        conta1.setId(null);
        assertThat(conta1).isNotEqualTo(conta2);
    }
}
