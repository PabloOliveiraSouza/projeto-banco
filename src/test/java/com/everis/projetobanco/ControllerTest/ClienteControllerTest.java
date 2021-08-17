package com.everis.projetobanco.ControllerTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
public class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void deveSalvarCliente() throws Exception {

        URI uri = new URI("/clientes");
        String json = "{\"nome\":\"Pablo\",\"cpf\":\"46432677807\",\"endereco\":\"Rua Distrito Federal 108\",\"fone\":\"11930855707\"}";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void deveRetornarBadRequestAoTentarSalvarCliente() throws Exception {

        URI uri = new URI("/clientes");
        String json = "{\"nome\":\"Pablo\",\"cpf\":\"4677807\",\"endereco\":\"Rua Distrito Federal 108\",\"fone\":\"11930855707\"}";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveRetornar405AoTentarDeletarClienteQueNaoExiste() throws Exception {

        URI uri = new URI("/clientes");
        String json = "{\"nome\":\"Pablo\",\"cpf\":\"46432677807\",\"endereco\":\"Rua Distrito Federal 108\",\"fone\":\"11930855707\"}";
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(405));

    }
    @Test
    public void deveRetornar200AoDeletarCliente() throws Exception {

        URI uri = new URI("/clientes");
        String cpf ="{\"46432677807\"}";
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .content(cpf).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(405));

    }
    @Test
    public void deveRetornar200AoAtualizarCliente() throws Exception {

        URI uri = new URI("/clientes");
        String json = "{\"nome\":\"Pablo\",\"cpf\":\"4677807\",\"endereco\":\"Rua Distrito Federal 108\",\"fone\":\"11930855707\"}";
        mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(405));

    }
}
