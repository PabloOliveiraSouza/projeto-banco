package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoContaEnum;


public class DetalharContaDTO {
    public Integer nConta;
    public Integer dConta;
    public double Saldo;
    public String agencia;
    public String cpf;
    public TipoContaEnum tipoConta;

    public DetalharContaDTO(ContaModel contaModel) {
        this.agencia = contaModel.getAgencia();
        this.cpf = contaModel.getCpf();
        this.dConta = contaModel.getdConta();
        this.nConta = contaModel.getnConta();
        this.Saldo = contaModel.getSaldo();
        this.tipoConta = contaModel.getTipoConta();
    }
}
