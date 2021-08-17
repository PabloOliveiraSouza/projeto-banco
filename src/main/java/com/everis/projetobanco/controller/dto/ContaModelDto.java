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
@Setter
public class ContaModelDto {

    public String nome;
    public Integer nConta;
    public Integer dConta;
    public String agencia;
    public Double Saldo;
    public String cpf;
    public TipoContaEnum tipoConta;

    public ContaModelDto(ContaModel contaModel) {
        //this.nome = contaModel.getCliente().getNome();
        this.agencia = contaModel.getAgencia();
        this.cpf = contaModel.getCpf();
        this.dConta = contaModel.getdConta();
        this.nConta = contaModel.getnConta();
        this.Saldo = contaModel.getSaldo();
        this.tipoConta = contaModel.getTipoConta();


    }

    public ContaModelDto() {
    }

    public static List<ContaModelDto> converter(List<ContaModel> contaModels) {
        return contaModels.stream().map(ContaModelDto::new).collect(Collectors.toList());
    }

    public ContaModel atualizar(String cpf, ContaRepository contaRepository) {
        Optional<ContaModel> contaModel = contaRepository.findByCpf(cpf);
        contaModel.map(alter -> {
            alter.setAgencia(this.getAgencia());
            alter.setCpf(this.getCpf());
            alter.setnConta(this.getNConta());
            alter.setdConta(this.getDConta());
            ContaModel updated = contaRepository.save(alter);
            return ResponseEntity.ok().body(updated);
        });
        return null;
    }
}
