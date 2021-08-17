package com.everis.projetobanco.repositoryTest;

import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoContaEnum;
import com.everis.projetobanco.repository.ContaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ContaRepositoryTest {
    @Autowired
    private ContaRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void deveriaCarregarContaAoBuscarPorCpf(){
        String cpf = "46432677807";
        ContaModel conta = new ContaModel();
        conta.setAgencia("3333");
        conta.setCpf("46432677807");
        conta.setSaldo(1200.0);
        conta.setTipoConta(TipoContaEnum.FISICA);
        conta.setQtdsaques(0);
        conta.setdConta(4);
        conta.setnConta(1234567);
        em.persist(conta);
        Optional <ContaModel> busca = repository.findByCpf(cpf);
        Assert.assertNotNull(busca);
        Assert.assertEquals(cpf,busca.get().getCpf());

    }
    @Test
    public void naoDeveriaCarregarContaCujaCpfNaoEstejaCadastrado(){
        String cpf = "46432677807";
        Optional <ContaModel> busca = repository.findByCpf(cpf);
        Assert.assertNotNull(busca.isEmpty());
    }

}
