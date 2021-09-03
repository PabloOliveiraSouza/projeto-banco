package com.everis.projetobanco.controller;

import KafkaProducer.KafkaProducerSaques;
import com.everis.projetobanco.controller.dto.ContaModelDto;
import com.everis.projetobanco.controller.dto.DetalharContaDTO;
import com.everis.projetobanco.controller.dto.TransferenciasModeldto;
import com.everis.projetobanco.inter.ExceptionJson;
import com.everis.projetobanco.inter.impl.ExceptionImpl;
import com.everis.projetobanco.inter.impl.TransferenciaImpl;
import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoOperacaoEnum;
import com.everis.projetobanco.model.TransferenciasModel;
import com.everis.projetobanco.repository.ClienteRepository;
import com.everis.projetobanco.repository.ContaRepository;
import com.everis.projetobanco.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/conta")
public class ContaController extends TransferenciaImpl {

    @Autowired
    private ClienteRepository repository1;
    @Autowired
    private ContaRepository repository;
    @Autowired
    private TransacaoRepository repositorytr;

    private TipoOperacaoEnum TipoSacar = TipoOperacaoEnum.Saque;
    private TipoOperacaoEnum TipoDeposito = TipoOperacaoEnum.Deposito;

    @GetMapping(path = "/")
    public ResponseEntity<?> consultarPorNC(@RequestParam Integer numeroConta) {
        Optional<ContaModel> conta = repository.findByNumeroConta(numeroConta);
        if (conta.isPresent()) {
            return ResponseEntity.ok().body(new DetalharContaDTO(conta.get()));
        }
        ExceptionJson exception = new ExceptionImpl();
        return exception.contaNEncontrada(numeroConta);
    }

    @GetMapping
    public List<?> consultarTodos() {
        if (repository.findAll().isEmpty()) {
            List<String> lista = Arrays.asList(new String[]{"Não existe nenhuma conta!"});
            return lista;
        }
        return repository.findAll();
    }

    @GetMapping("/list")
    public List<ContaModelDto> lista() {
        List<ContaModel> contas = repository.findAll();
        return ContaModelDto.converter(contas);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody ContaModel contaModel, UriComponentsBuilder uriBuilder) {
        Optional<ContaModel> veri = repository.findByNumeroConta(contaModel.getNumeroConta());
        Optional<ClienteModel> vericliente = repository1.findByCpf(contaModel.getCpf());
        if (veri.isPresent()) {
            ExceptionJson exception = new ExceptionImpl();
            return exception.contaPresente(contaModel);
        } else if (vericliente.isPresent()) {
            contaModel.setQtdsaques(0);
            repository.save(contaModel);
            URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(contaModel.getId()).toUri();
            ExceptionJson exception = new ExceptionImpl();
            return exception.contaSalvo(contaModel, uri);
        }
        ExceptionJson exception = new ExceptionImpl();
        return exception.clienteNEncontrada(contaModel.getCpf());
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("numeroConta") Integer numeroConta) {
        Optional<ContaModel> contaModel = repository.findByNumeroConta(numeroConta);
        if (contaModel.isPresent()) {
            repository.delete(contaModel.get());
            ExceptionJson exception = new ExceptionImpl();
            return exception.deleteContaEfetuado(contaModel.get().getId(), contaModel.get().getNumeroConta(), contaModel.get().getAgencia());
        } else {
            ExceptionJson exception = new ExceptionImpl();
            return exception.contaNEncontrada(numeroConta);
        }
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody @Valid ContaModelDto contadto) {
        Optional<ContaModel> busca = repository.findByNumeroConta(contadto.getNumeroConta());
        if (busca.isPresent()) {
            ContaModel contaModel = contadto.atualizar(contadto.getNumeroConta(), repository);
            return ResponseEntity.ok(new ContaModelDto(contaModel));
        }
        ExceptionJson exception = new ExceptionImpl();
        return exception.contaNEncontrada(contadto.getNumeroConta());
    }

    //----------------------------------------------------------------------------TRANSAÇÕES----------------------------------------------------------------------------//
    @GetMapping("/extrato")
    public List<TransferenciasModel> ConsultaExtrato(@RequestParam("numeroConta") Integer numeroConta) {
        return repositorytr.findAllByNumeroConta(numeroConta);
    }

    @PostMapping("/operacoes/saque")
    public ResponseEntity<?> salvarTransacaoSaque(@RequestBody @Valid TransferenciasModel model, UriComponentsBuilder uriBuilder) throws ExecutionException, InterruptedException {
        return validacaodesaque(model, uriBuilder);
    }

    @PostMapping("/operacoes/deposito")
    public ResponseEntity<?> salvarTransacaoDeposito(@RequestBody @Valid TransferenciasModel model, UriComponentsBuilder uriBuilder) {
        Optional<ContaModel> busca = repository.findByNumeroConta(model.getNumeroConta());
        if (busca.isPresent()) {
            if (model.getTipoOperacao().equals(TipoDeposito)) {
                model.setTaxa(0.0);
                repositorytr.save(model);
                depositarConta(model);
                URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(model.getId()).toUri();
                ExceptionJson exception = new ExceptionImpl();
                return exception.depositoEfetuado(model, uri);
            }
        }
        ExceptionJson exception = new ExceptionImpl();
        return exception.contaNEncontrada(model.getNumeroConta());
    }

    @PutMapping("/operacoes/transferir")
    public ResponseEntity<?> transferencias(@RequestBody @Valid TransferenciasModeldto model, UriComponentsBuilder uriBuilder, ContaController contaController) {
        Optional<ContaModel> buscaEntrada = repository.findByNumeroConta(model.getNumeroConta_Entrada());
        Optional<ContaModel> buscaSaida = repository.findByNumeroConta(model.getNumeroConta_Saida());
        if (buscaEntrada.isPresent() && buscaSaida.isPresent()) {
            if (buscaSaida.get().getSaldoEmConta() >= model.getValor()) {
                TransferenciasModel transacaoEntrada = new TransferenciasModel(model.getId(), model.getNumeroConta_Entrada(), model.getValor(), TipoOperacaoEnum.TransferenciaEntrada, getTaxa());
                TransferenciasModel transacaoSaida = new TransferenciasModel(model.getId(), model.getNumeroConta_Saida(), model.getValor(), TipoOperacaoEnum.TransferenciaSaida, getTaxa());
                transferirContas(transacaoEntrada);
                transferirContasSaida(transacaoSaida);
                repositorytr.save(transacaoEntrada);
                repositorytr.save(transacaoSaida);
                URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(transacaoSaida.getId()).toUri();
                ExceptionJson exception = new ExceptionImpl();
                return exception.TranferenciaEfetuado(transacaoEntrada, transacaoSaida, uri);
            }
            ExceptionJson exception = new ExceptionImpl();
            return exception.erroSemSaldo(buscaSaida.get().getSaldoEmConta(), buscaSaida.get().getNumeroConta());
        }
        ExceptionJson exception = new ExceptionImpl();
        return exception.contasNEncontrada(model.getNumeroConta_Saida(), model.getNumeroConta_Entrada(),repository);
    }


    public ResponseEntity<?> validacaodesaque(@RequestBody @Valid TransferenciasModel model, UriComponentsBuilder uriBuilder) throws ExecutionException, InterruptedException {
        Optional<ContaModel> busca = repository.findByNumeroConta(model.getNumeroConta());
        if (busca.isPresent()) {
            if (busca.get().getSaldoEmConta() <= model.getValor()) {
                ExceptionImpl exception = new ExceptionImpl();
                return exception.erroSemSaldo(busca.get().saldoEmConta, model.getNumeroConta());
            }
            sacarConta(model);
            URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(model.getId()).toUri();
            KafkaProducerSaques ks = new KafkaProducerSaques();
            ks.EnviarDadosClienteSaque(model.getNumeroConta());
            ExceptionJson exception = new ExceptionImpl();
            return exception.SaqueEfetuado(model, busca.get().getQtdsaques(), busca.get().getTipoConta().getTaxa(), uri);
        }
        ExceptionJson exception = new ExceptionImpl();
        return exception.contaNEncontrada(model.getNumeroConta());
    }


}





