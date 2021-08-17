package com.everis.projetobanco.controller.dto;


import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoContaEnum;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ContasDTO {

    public Integer id;
    public Integer qtdsaques;
    public String agencia;
    public Integer nConta;
    public Integer dConta;
    public double Saldo;
    public TipoContaEnum tipoConta;
    private LocalDateTime datacriacao;

    public ContasDTO(ContaModel contaModel) {
        this.id = contaModel.getId();
        this.qtdsaques = contaModel.getQtdsaques();
        this.nConta = contaModel.getnConta();
        this.dConta = contaModel.getdConta();
        this.Saldo = contaModel.getSaldo();
        this.tipoConta = contaModel.getTipoConta();
        this.agencia = contaModel.getAgencia();
//       this.datacriacao = contaModel.getdatacriacao();
    }

}
