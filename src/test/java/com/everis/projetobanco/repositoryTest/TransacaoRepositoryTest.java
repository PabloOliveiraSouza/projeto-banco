package com.everis.projetobanco.repositoryTest;

import com.everis.projetobanco.model.TipoOperacaoEnum;
import com.everis.projetobanco.model.TransferenciasModel;
import com.everis.projetobanco.repository.TransacaoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TransacaoRepositoryTest {
    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void deveriaCarregarTransacoesAoBuscarPorCpf(){
        String cpf = "46432677807";
        TransferenciasModel tr = new TransferenciasModel();
        tr.setCpf("46432677807");
        tr.setTipoOperacao(TipoOperacaoEnum.Deposito);
        tr.setValor(1000.0);
        em.persist(tr);
        List<TransferenciasModel> busca = repository.findAllByCpf(cpf);
        Assert.assertNotNull(busca);
        Assert.assertEquals(1,busca.size());

    }
    @Test
    public void naoDeveriaCarregarTransacoesCujaCpfNaoEstejaCadastrado(){
        String cpf = "46432677807";
        List<TransferenciasModel> busca = repository.findAllByCpf(cpf);
        Assert.assertNotNull(busca.isEmpty());
    }

}
