package com.everis.projetobanco.controller;

import KafkaProducer.KafkaProducerSaques;
import com.everis.projetobanco.controller.dto.ContaModelDto;
import com.everis.projetobanco.controller.dto.DetalharContaDTO;
import com.everis.projetobanco.controller.dto.TransferenciasModeldto;
import com.everis.projetobanco.inter.impl.TransferenciaImpl;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoOperacaoEnum;
import com.everis.projetobanco.model.TransferenciasModel;
import com.everis.projetobanco.repository.ContaRepository;
import com.everis.projetobanco.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/conta")
public class ContaController extends TransferenciaImpl {

    @Autowired
    private ContaRepository repository;
    @Autowired
    private TransacaoRepository repositorytr;


    private TipoOperacaoEnum TipoSacar = TipoOperacaoEnum.Saque;
    private TipoOperacaoEnum TipoDeposito = TipoOperacaoEnum.Deposito;

    @GetMapping(path = "/{cpf}")
    public ResponseEntity<DetalharContaDTO> consultarCPF(@PathVariable String cpf) {
        Optional<ContaModel> conta = repository.findByCpf(cpf);
        if (conta.isPresent()) {
            return ResponseEntity.ok().body(new DetalharContaDTO(conta.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<ContaModel> consultarTodos() {
        return repository.findAll();

    }

    @GetMapping("/list")
    public List<ContaModelDto> lista() {
        List<ContaModel> contas = repository.findAll();
        return ContaModelDto.converter(contas);

    }

    @PostMapping
    public ResponseEntity<ContaModel> salvar(@Valid @RequestBody ContaModel contaModel, UriComponentsBuilder uriBuilder) {
        //verificar se a conta já existe
        //Optional<ContaModel> veri = repository.findBynCOnta(contaModel.getnConta());
        Optional<ContaModel> veri = repository.findByCpf(contaModel.getCpf());
        if (veri.isPresent()) {
            return ResponseEntity.badRequest().body(contaModel);
        }
        repository.save(contaModel);
        URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(contaModel.getId()).toUri();
        return ResponseEntity.created(uri).body(contaModel);
    }

    @DeleteMapping(value = "{cpf}")
    public ResponseEntity delete(@PathVariable("cpf") String cpf) {
        Optional<ContaModel> contaModel = repository.findByCpf(cpf);
        if (contaModel.isPresent()) {
            repository.delete(contaModel.get());
            return ResponseEntity.ok().build();
        } else {
            throw new RuntimeException("Conta não encontrado com o cpf " + cpf);
        }
    }

    @PutMapping("{cpf}")
    public ResponseEntity<ContaModelDto> atualizar(@PathVariable("cpf") String cpf, @RequestBody @Valid ContaModelDto conta) {
        Optional<ContaModel> busca = repository.findByCpf(cpf);
        if (busca.isPresent()) {
            ContaModel contaModel = conta.atualizar(cpf, repository);
            return ResponseEntity.ok(new ContaModelDto(contaModel));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/extrato/{cpf}")
    public List<TransferenciasModel> ConsultaExtrato(@PathVariable("cpf") String cpf) {
        return repositorytr.findAllByCpf(cpf);
    }

    @PostMapping("/operacoes/saque")
    public ResponseEntity<TransferenciasModel> salvarTransacaoSaque(@RequestBody @Valid TransferenciasModel model, UriComponentsBuilder uriBuilder) throws ExecutionException, InterruptedException {
        return validacaodesaque(model, uriBuilder);
    }

    @PostMapping("/operacoes/deposito")
    public ResponseEntity<TransferenciasModel> salvarTransacaoDeposito(@RequestBody @Valid TransferenciasModel model, UriComponentsBuilder uriBuilder) {
        if (model.getTipoOperacao().equals(TipoDeposito)) {
            model.setTaxa(getTaxa());
            repositorytr.save(model);
            depositarConta(model);
            URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(model.getId()).toUri();
            return ResponseEntity.created(uri).body(model);
        }
        return ResponseEntity.badRequest().body(model);

    }

    @PutMapping("/operacoes/transferir")
    public ResponseEntity<TransferenciasModel> transferencias(@RequestBody @Valid TransferenciasModeldto model, UriComponentsBuilder uriBuilder, ContaController contaController) {
        TransferenciasModel transacaoEntrada = new TransferenciasModel(model.getId(), model.getCpfEntrada(), model.getValor(), TipoOperacaoEnum.TransferenciaEntrada, getTaxa());
        TransferenciasModel transacaoSaida = new TransferenciasModel(model.getId(), model.getCpfSaida(), model.getValor(), TipoOperacaoEnum.TransferenciaSaida, getTaxa());
        transferirContas(transacaoEntrada);
        transferirContasSaida(transacaoSaida);
        repositorytr.save(transacaoEntrada);
        repositorytr.save(transacaoSaida);
        URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(transacaoSaida.getId()).toUri();
        return ResponseEntity.created(uri).body(transacaoSaida);
    }

    public ResponseEntity<TransferenciasModel> validacaodesaque(@RequestBody @Valid TransferenciasModel model, UriComponentsBuilder uriBuilder) throws ExecutionException, InterruptedException {
        Optional<ContaModel> busca = repository.findByCpf(model.getCpf());
        if (busca.get().getSaldo() <= model.getValor()) {
            return ResponseEntity.badRequest().build();
        }
        sacarConta(model);
        model.setTaxa(getTaxa());
        URI uri = uriBuilder.path("/contas/{id}").buildAndExpand(model.getId()).toUri();
        KafkaProducerSaques ks = new KafkaProducerSaques();
        ks.EnviarDadosClienteSaque(model.getCpf());
        return ResponseEntity.created(uri).body(model);
    }


}





