package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DetalharClienteDTO {

    private String nome;
    private String cpf;
    private String endereco;
    private String fone;
    private LocalDateTime datacriacao;
    private List<ContasDTO> contas;

    public DetalharClienteDTO(ClienteModel clienteModel) {
        this.nome = clienteModel.getNome();
        this.cpf = clienteModel.getCpf();
        this.fone = clienteModel.getFone();
        this.endereco = clienteModel.getEndereco();
        this.datacriacao = clienteModel.getDatacriacao();
        this.contas = new ArrayList<>();
        this.contas.addAll(clienteModel.getContas().stream().map(ContasDTO::new).collect(Collectors.toList()));
    }

}
