package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoContaEnum;


public class DetalharContaDTO {
    public Integer numeroConta;
    public Integer digitoConta;
    public double saldoEmConta;
    public String agencia;
    public String cpf;
    public TipoContaEnum tipoConta;

    public DetalharContaDTO(ContaModel contaModel) {
        this.agencia = contaModel.getAgencia();
        this.cpf = contaModel.getCpf();
        this.digitoConta = contaModel.getDigitoConta();
        this.numeroConta = contaModel.getNumeroConta();
        this.saldoEmConta = contaModel.getSaldoEmConta();
        this.tipoConta = contaModel.getTipoConta();
    }
}
