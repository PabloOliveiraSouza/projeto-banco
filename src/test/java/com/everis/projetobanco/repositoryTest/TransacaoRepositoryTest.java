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

import javax.persistence.criteria.CriteriaBuilder;
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
        Integer cpf = 46807;
        TransferenciasModel tr = new TransferenciasModel();
        tr.setNumeroConta(12345);
        tr.setTipoOperacao(TipoOperacaoEnum.Deposito);
        tr.setValor(1000.0);
        em.persist(tr);
        List<TransferenciasModel> busca = repository.findAllByNumeroConta(cpf);
        Assert.assertNotNull(busca);
        Assert.assertEquals(1,busca.size());

    }
    @Test
    public void naoDeveriaCarregarTransacoesCujaCpfNaoEstejaCadastrado(){
        Integer cpf = 4643;
        List<TransferenciasModel> busca = repository.findAllByNumeroConta(cpf);
        Assert.assertNotNull(busca.isEmpty());
    }

}
