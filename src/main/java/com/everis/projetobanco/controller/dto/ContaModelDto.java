package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoContaEnum;
import com.everis.projetobanco.repository.ClienteRepository;
import com.everis.projetobanco.repository.ContaRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ContaModelDto {

    public Integer numeroConta;
    public Integer digitoConta;
    public String agencia;
    public Double saldoEmConta;
    public String cpf;
    public TipoContaEnum tipoConta;

    public ContaModelDto(ContaModel contaModel) {
        //this.nome = contaModel.getCliente().getNome();
        this.agencia = contaModel.getAgencia();
        this.cpf = contaModel.getCpf();
        this.digitoConta = contaModel.getDigitoConta();
        this.numeroConta = contaModel.getNumeroConta();
        this.saldoEmConta = contaModel.getSaldoEmConta();
        this.tipoConta = contaModel.getTipoConta();
    }

    public ContaModelDto() {
    }

    public static List<ContaModelDto> converter(List<ContaModel> contaModels) {
        return contaModels.stream().map(ContaModelDto::new).collect(Collectors.toList());
    }

    public ContaModel atualizar(Integer numeroConta, ContaRepository contaRepository) {
        Optional<ContaModel> contaModel = contaRepository.findByNumeroConta(numeroConta);
        contaModel.map(alter -> {
            alter.setAgencia(this.getAgencia());
            alter.setCpf(this.getCpf());
            alter.setNumeroConta(this.getNumeroConta());
            alter.setDigitoConta(this.getDigitoConta());
            alter.setTipoConta(this.getTipoConta());
            ContaModel updated = contaRepository.save(alter);
            return ResponseEntity.ok().body(updated);
        });
        return contaModel.get();
    }


}
