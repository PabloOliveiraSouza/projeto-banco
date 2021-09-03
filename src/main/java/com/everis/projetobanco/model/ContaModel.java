package com.everis.projetobanco.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "conta")
public class ContaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "usuario", referencedColumnName = "cpf")
//    public ClienteModel cliente;
    @CPF
    public String cpf;
    @Enumerated(EnumType.ORDINAL)
    public TipoContaEnum tipoConta;
    @NotNull
    public Integer numeroConta;
    @NotNull
    @Max(value = 99)
    public Integer digitoConta;
    @NotNull
    public String agencia;
    @NotNull
    public Double saldoEmConta;

    public Integer qtdsaques ;
}
