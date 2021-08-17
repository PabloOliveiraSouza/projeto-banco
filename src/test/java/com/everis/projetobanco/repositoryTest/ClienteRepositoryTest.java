package com.everis.projetobanco.repositoryTest;

import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.repository.ClienteRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ClienteRepositoryTest {
    @Autowired
    private ClienteRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void deveriaCarregarModelAoBuscarPorCpf(){
        LocalDateTime data = LocalDateTime.now();
        List<ContaModel> list = null;
        String cpf = "46432677807";
        ClienteModel cliente = new ClienteModel();
        cliente.setFone("11930855707");
        cliente.setNome("Pablo");
        cliente.setCpf("46432677807");
        cliente.setEndereco("Rua Distrito Federal 108");
        cliente.setDatacriacao(data);
        cliente.setContas(list);
        em.persist(cliente);

        Optional <ClienteModel> busca = repository.findByCpf(cpf);
        Assert.assertNotNull(busca);
        Assert.assertEquals(cpf,busca.get().getCpf());

    }
    @Test
    public void naoDeveriaCarregarUmClienteCujaCpfNaoEstejaCadastrado(){
        ClienteModel cliente = new ClienteModel();
        String cpf = "46432677807";
        Optional <ClienteModel> busca = repository.findByCpf(cpf);
        Assert.assertNotNull(busca.isEmpty());
    }

}
