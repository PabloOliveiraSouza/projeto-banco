package com.everis.projetobanco.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "transferencias")
public class TransferenciasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public Integer numeroConta;
    @NotNull
    public double valor;
    @NotNull
    private TipoOperacaoEnum tipoOperacao;
    @NotNull
    private double taxa;

    public TransferenciasModel(Integer id, Integer numeroConta, double valor, TipoOperacaoEnum tipoOperacao, double taxa) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.valor = valor;
        this.tipoOperacao = tipoOperacao;
        this.taxa = taxa;
    }

    public TransferenciasModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta) {
        this.numeroConta = numeroConta;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public TipoOperacaoEnum getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacaoEnum tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }
}
