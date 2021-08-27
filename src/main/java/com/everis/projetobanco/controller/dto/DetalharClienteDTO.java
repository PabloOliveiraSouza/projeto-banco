package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DetalharClienteDTO {

    private String nome;
    private String cpf;
    private String endereco;
    private String fone;
    private LocalDateTime datacriacao;

    public DetalharClienteDTO(ClienteModel clienteModel) {
        this.nome = clienteModel.getNome();
        this.cpf = clienteModel.getCpf();
        this.fone = clienteModel.getFone();
        this.endereco = clienteModel.getEndereco();
        this.datacriacao = clienteModel.getDatacriacao();
    }

}
