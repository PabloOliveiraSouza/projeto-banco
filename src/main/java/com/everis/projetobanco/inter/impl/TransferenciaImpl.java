package com.everis.projetobanco.inter.impl;

import com.everis.projetobanco.inter.TransferenciasInterface;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoContaEnum;
import com.everis.projetobanco.model.TransferenciasModel;
import com.everis.projetobanco.repository.ContaRepository;
import com.everis.projetobanco.repository.TransacaoRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Setter
@Getter
@RestController
public class TransferenciaImpl implements TransferenciasInterface {

    TipoContaEnum tipoconta;
    Double taxa = 0.0;
    Integer qtdsaques = 0;

    @Autowired
    private ContaRepository repository;
    @Autowired
    private TransacaoRepository repositorytr;

    @Override
    public Optional<ResponseEntity<ContaModel>> sacarConta(@RequestBody TransferenciasModel model) {
        reconhecerTipoConta(model.getNumeroConta());
        Optional<ContaModel> conta = repository.findByNumeroConta(model.getNumeroConta());
        return conta.map(record -> {
            if (record.getQtdsaques() > getQtdsaques()) {
                record.setSaldoEmConta(record.getSaldoEmConta() - model.getValor() - getTaxa());
                ContaModel updated = repository.save(record);
                model.setTaxa(getTaxa());
                repositorytr.save(model);
                return ResponseEntity.ok().body(updated);

            } else {
                record.setSaldoEmConta(record.getSaldoEmConta() - model.getValor());
                ContaModel updated = repository.save(record);
                model.setTaxa(0.0);
                repositorytr.save(model);
                return ResponseEntity.ok().body(updated);

            }
        });
    }

    @Override
    public Optional<ResponseEntity<ContaModel>> depositarConta(@RequestBody TransferenciasModel model) {
        Optional<ContaModel> conta = repository.findByNumeroConta(model.getNumeroConta());
        conta.map(record -> {
            record.setSaldoEmConta(record.getSaldoEmConta() + model.getValor());
            ContaModel updated = repository.save(record);
            return ResponseEntity.ok().body(updated);
        });

        return null;
    }


    @Override
    public Optional<ResponseEntity<ContaModel>> transferirContas(@RequestBody TransferenciasModel model) {
        Optional<ContaModel> contaEntrada = repository.findByNumeroConta(model.getNumeroConta());
        contaEntrada.map(record -> {
            record.setSaldoEmConta(record.getSaldoEmConta() + model.getValor());
            ContaModel updated = repository.save(record);
            return ResponseEntity.ok().body(updated);
        });
        return null;
    }

    public Optional<ResponseEntity<ContaModel>> transferirContasSaida(@RequestBody TransferenciasModel mode) {
        Optional<ContaModel> contaSaida = repository.findByNumeroConta(mode.getNumeroConta());
        contaSaida.map(record -> {
            record.setSaldoEmConta(record.getSaldoEmConta() - mode.getValor());
            ContaModel update = repository.save(record);
            return ResponseEntity.ok().body(update);
        });
        return null;
    }

    @Override
    public ResponseEntity extrato(String cpf) {
        return ResponseEntity.ok(cpf);
    }

    @Override
    public String reconhecerTipoConta(Integer numeroConta) {
        Optional<ContaModel> conta = repository.findByNumeroConta(numeroConta);
        conta.map(map -> {
            setTipoconta(map.getTipoConta());
            setTaxa(tipoconta.getTaxa());
            setQtdsaques(tipoconta.getQtdSaques());
            return tipoconta;
        });
        return tipoconta.getDescricao();

    }


}
