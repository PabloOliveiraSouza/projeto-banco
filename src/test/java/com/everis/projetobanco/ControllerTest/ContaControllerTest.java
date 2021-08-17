package com.everis.projetobanco.ControllerTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ContaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void deveSalvarConta() throws Exception {

        URI uri = new URI("/conta");
        String json = "{\"cpf\":\"46432677807\",\"dConta\":\"2\",\"nConta\":\"123123\",\"qtdsaques\":\"0\",\"agencia\":\"4567\",\"Saldo\":\"1000.0\",\"tipoConta\":\"FISICA\"}";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void deveRetornarBadRequestAoTentarSalvarConta() throws Exception {
        URI uri = new URI("/conta");
        String json = "{\"cpf\":\"46437807\",\"dConta\":\"2\",\"nConta\":\"123123\",\"qtdsaques\":\"0\",\"agencia\":\"4567\",\"Saldo\":\"1000.0\",\"tipoConta\":\"FISICA\"}";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveRetornar405AoTentarDeletarContaQueNaoExiste() throws Exception {
        URI uri = new URI("/conta");
        String json = "{\"cpf\":\"46432677807\",\"dConta\":\"2\",\"nConta\":\"123123\",\"qtdsaques\":\"0\",\"agencia\":\"4567\",\"Saldo\":\"1000.0\",\"tipoConta\":\"FISICA\"}";
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(405));

    }
    @Test
    public void deveRetornar200AoDeletarConta() throws Exception {
        URI uri = new URI("/conta");
        String cpf ="{\"46432677807\"}";
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .content(cpf).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(405));

    }
    @Test
    public void deveRetornar200AoAtualizarConta() throws Exception {
        URI uri = new URI("/conta");
        String json = "{\"cpf\":\"46432677807\",\"dConta\":\"2\",\"nConta\":\"123123\",\"qtdsaques\":\"0\",\"agencia\":\"4567\",\"Saldo\":\"1000.0\",\"tipoConta\":\"FISICA\"}";
        mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(405));

    }
    @Test
    public void deveRetornar201AoDepositarConta() throws Exception {

        URI uri = new URI("/conta/operacoes/deposito");
        String json = "{\"cpf\":\"46432677807\",\"valor\":\"200\",\"tipoOperacao\":\"1\"}";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(201));

    }
    @Test
    public void deveRetornar201AoTransferirEntreConta() throws Exception {
        URI uri = new URI("/conta/operacoes/transferir");
        String json = "{\"cpfEntrada\":\"46432677807\",\"cpfSaida\":\"17464916808\",\"valor\":\"200\",\"tipoOperacao\":\"null\"}";
        mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(201));

    }
}
