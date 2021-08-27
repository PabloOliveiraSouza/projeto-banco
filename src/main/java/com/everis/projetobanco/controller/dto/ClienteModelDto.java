package com.everis.projetobanco.controller.dto;

import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.repository.ClienteRepository;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ClienteModelDto {
    @NotNull
    @NotEmpty
    private String nome;
    @CPF
    private String cpf;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "([\\w\\W]+)\\s(\\d+)", message = "Informe o nome da Rua e o número apenas.")
    private String endereco;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(?:(?:\\+|00)?(55)\\s?)?(?:(?:\\(?[1-9][0-9]\\)?)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})-?(\\d{4}))$", message = "Telefone Invalido")
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

