package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.TipoOperacaoEnum;
import com.everis.projetobanco.model.TransferenciasModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TransferenciasModeldto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer Id;
    @CPF
    public String cpfEntrada;
    @CPF
    public String cpfSaida;
    @NotNull
    public double valor;
    public TipoOperacaoEnum tipo;

    public TransferenciasModeldto(Integer id, String cpfEntrada, String cpfSaida, double valor, TipoOperacaoEnum tipo) {
        Id = id;
        this.cpfEntrada = cpfEntrada;
        this.cpfSaida = cpfSaida;
        this.valor = valor;
        this.tipo = tipo;
    }

    public TransferenciasModeldto() {
    }

}
