package com.everis.projetobanco.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity(name = "conta")
public class ContaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @NotNull
    public Integer qtdsaques;
    @NotNull
    public Integer nConta;
    @NotNull
    public Integer dConta;
    @NotNull
    public String agencia;
    @NotNull
    public Double Saldo;
    @CPF
    public String cpf;
    @Enumerated(EnumType.ORDINAL)
    public TipoContaEnum tipoConta;

    public ContaModel() {
    }

    public ContaModel(Integer id, Integer qtdsaques, Integer nConta, Integer dConta, Double saldo, String cpf, TipoContaEnum tipoConta, String agencia) {
        this.id = id;
        this.qtdsaques = qtdsaques;
        this.nConta = nConta;
        this.dConta = dConta;
        this.Saldo = saldo;
        this.cpf = cpf;
        this.tipoConta = tipoConta;
        this.agencia = agencia;
    }

    public ContaModel(TransferenciasModel model) {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQtdsaques() {
        return qtdsaques;
    }

    public void setQtdsaques(Integer qtdsaques) {
        this.qtdsaques = qtdsaques;
    }

    public Integer getnConta() {
        return nConta;
    }

    public void setnConta(Integer nConta) {
        this.nConta = nConta;
    }

    public Integer getdConta() {
        return dConta;
    }

    public void setdConta(Integer dConta) {
        this.dConta = dConta;
    }

    public Double getSaldo() {
        return Saldo;
    }

    public void setSaldo(Double saldo) {
        Saldo = saldo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public TipoContaEnum getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoContaEnum tipoConta) {
        this.tipoConta = tipoConta;
    }
}