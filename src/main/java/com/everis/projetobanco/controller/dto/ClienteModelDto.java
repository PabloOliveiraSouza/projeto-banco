package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.repository.ClienteRepository;
import lombok.*;
import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ClienteModelDto {

    private String nome;
    private String cpf;
    private String endereco;
    private String fone;


    public ClienteModelDto(ClienteModel clienteModel) {
        this.nome = clienteModel.getNome();
        this.cpf = clienteModel.getCpf();
        this.fone = clienteModel.getFone();
        this.endereco = clienteModel.getEndereco();

    }

    public ClienteModelDto() {
    }


    public static List<ClienteModelDto> converter(List<ClienteModel> clienteModel) {
        return clienteModel.stream().map(ClienteModelDto::new).collect(Collectors.toList());
    }

    public ClienteModel atualizar(String cpf, ClienteRepository clienteRepository) {
        Optional<ClienteModel> clienteModel = clienteRepository.findByCpf(cpf);
        clienteModel.map(alter -> {
            alter.setNome(this.getNome());
            alter.setCpf(this.getCpf());
            alter.setFone(this.getFone());
            alter.setEndereco(this.getEndereco());
            ClienteModel updated = clienteRepository.save(alter);
            return ResponseEntity.ok().body(updated);
        });
        return clienteModel.get();
    }
}

