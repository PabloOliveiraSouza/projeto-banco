package com.everis.projetobanco.model;

import lombok.Getter;

@Getter
public enum TipoContaEnum {

    FISICA("FISICA", 5, 10),
    GOVERNAMENTAL("GOVERNAMENTAL", 250, 20),
    JURIDICA("JURIDICA", 50, 10);

    private String descricao;
    private Integer qtdSaques;
    private double taxa;

    TipoContaEnum(String descricao, Integer qtdSaques, double taxa) {
        this.descricao = descricao;
        this.qtdSaques = qtdSaques;
        this.taxa = taxa;
    }

}
