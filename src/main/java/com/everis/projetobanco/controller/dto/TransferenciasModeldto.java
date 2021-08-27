package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.TipoOperacaoEnum;
import com.everis.projetobanco.model.TransferenciasModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TransferenciasModeldto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer Id;
    public Integer numeroConta_Entrada;
    public Integer numeroConta_Saida;
    public double valor;
    public TipoOperacaoEnum tipo;


    public TransferenciasModeldto(Integer id, Integer numeroConta_Entrada, Integer numeroConta_Saida, double valor, TipoOperacaoEnum tipo) {
        Id = id;
        this.numeroConta_Entrada = numeroConta_Entrada;
        this.numeroConta_Saida = numeroConta_Saida;
        this.valor = valor;
        this.tipo = tipo;
    }

    public TransferenciasModeldto() {
    }

}
