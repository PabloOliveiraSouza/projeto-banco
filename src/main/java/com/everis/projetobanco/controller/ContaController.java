package com.everis.projetobanco.controller;

import KafkaProducer.KafkaProducerSaques;
import com.everis.projetobanco.controller.dto.ContaModelDto;
import com.everis.projetobanco.controller.dto.DetalharContaDTO;
import com.everis.projetobanco.controller.dto.TransferenciasModeldto;
import com.everis.projetobanco.inter.impl.TransferenciaImpl;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoOperacaoEnum;
import com.everis.projetobanco.model.TransferenciasModel;
import com.everis.projetobanco.repository.ClienteRepository;
import com.everis.projetobanco.repository.ContaRepository;
import com.everis.projetobanco.repository.TransacaoRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
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
        String json = "Conta não encontrada!";
        return ResponseEntity.status(203).body(json);
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

    //AJUSTADO
    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody ContaModel contaModel, UriComponentsBuilder uriBuilder) {
        Optional<ContaModel> veri = repository.findByNumeroConta(contaModel.getNumeroConta());
        if (veri.isPresent()) {
            String json = "Conta já Cadastrada com o numero de conta: " + contaModel.getNumeroConta();
            return ResponseEntity.status(203).body(json);
        }
        contaModel.setQtdsaques(0);
        repository.save(contaModel);
        URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(contaModel.getId()).toUri();
        return ResponseEntity.created(uri).body(contaModel);
    }

    //AJUSTADO
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("numeroConta") Integer numeroConta) {
        Optional<ContaModel> contaModel = repository.findByNumeroConta(numeroConta);
        if (contaModel.isPresent()) {
            String json = "Cliente Deletado Com sucesso!";
            repository.delete(contaModel.get());
            return ResponseEntity.accepted().body(new String[]{json, "ID: " + contaModel.get().getId().toString() + ", Nº Conta: " + contaModel.get().getNumeroConta() + ", Agência: " + contaModel.get().getAgencia()});
        } else {
            //verificar
            String json = "{\"message: \"Conta não encontrado!\"}";
            return ResponseEntity.badRequest().body(json);
        }
    }


    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody @Valid ContaModelDto contadto) {
        Optional<ContaModel> busca = repository.findByNumeroConta(contadto.getNumeroConta());
        if (busca.isPresent()) {
            ContaModel contaModel = contadto.atualizar(contadto.getNumeroConta(), repository);
            return ResponseEntity.ok(new ContaModelDto(contaModel));
        }
        String json = "{\"message: \"Conta não encontrado!\"}";
        return ResponseEntity.badRequest().body(json);
    }

    //-----------------------------------------------------------------------------------------------------------------------//
    //------------------TRANSAÇÕES-------------------------------//
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
                return ResponseEntity.created(uri).body(model);
            }
            return ResponseEntity.badRequest().body(model);
        }
        String json = "Conta " + model.getNumeroConta() + " não encontrada!";
        return ResponseEntity.badRequest().body(json);
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
                return ResponseEntity.created(uri).body(new TransferenciasModel[]{transacaoEntrada, transacaoSaida});
            }
            String json = "Sem saldo suficiente na conta " + buscaSaida.get().getNumeroConta() + ", " + "do CPF " + buscaSaida.get().getCpf() + ", Saldo atual de: " + buscaSaida.get().getSaldoEmConta() + "!";
            return ResponseEntity.badRequest().body(json);
        }
        String json = "Contas: " + model.getNumeroConta_Entrada() + " " + model.getNumeroConta_Saida() + " não encontradas!";
        return ResponseEntity.badRequest().body(json);
    }

    public ResponseEntity<?> validacaodesaque(@RequestBody @Valid TransferenciasModel model, UriComponentsBuilder uriBuilder) throws ExecutionException, InterruptedException {
        Optional<ContaModel> busca = repository.findByNumeroConta(model.getNumeroConta());
       // Optional<ContaModel> busca1 = repository.findByNumeroConta(model.getNumeroConta());
        if (busca.isPresent()) {
            if (busca.get().getSaldoEmConta() <= model.getValor()) {
                String json = "Você não tem Saldo suficiente";
                return ResponseEntity.badRequest().body(json);
            }
            sacarConta(model);
            URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(model.getId()).toUri();
            KafkaProducerSaques ks = new KafkaProducerSaques();
            ks.EnviarDadosClienteSaque(model.getNumeroConta());
            return ResponseEntity.created(uri).body(model);
        }
        String json = "Conta não encontrada!";
        return ResponseEntity.badRequest().body(json);
    }


}





