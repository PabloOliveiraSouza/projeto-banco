package com.everis.projetobanco.controller;

import com.everis.projetobanco.controller.dto.ClienteModelDto;
import com.everis.projetobanco.controller.dto.DetalharClienteDTO;
import com.everis.projetobanco.controller.dto.DetalharContaDTO;
import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.repository.ClienteRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository repository;

    @GetMapping(path = "/")
    public ResponseEntity<?> consultarPorCpf(@RequestParam String cpf) {
        Optional<ClienteModel> cliente = repository.findByCpf(cpf);
        if (cliente.isPresent()) {
            return ResponseEntity.ok().body(cliente);
        }
        String json = "Cliente não encontrada!";
        return ResponseEntity.status(203).body(json);
    }

    @GetMapping
    public List<?> consultarTodos() {
        if (repository.findAll().isEmpty()) {
            List<String> lista = Arrays.asList(new String[]{"Não existe nenhum cliente!"});
            return lista;
        }
        return repository.findAll();
    }

    @GetMapping("/list")
    public List<ClienteModelDto> lista() {
        List<ClienteModel> clientes = repository.findAll();
        return ClienteModelDto.converter(clientes);
    }

    //ajustado
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid ClienteModel clienteModel, UriComponentsBuilder uriBuilder) {
        Optional<ClienteModel> veri = repository.findByCpf(clienteModel.getCpf());
        if (veri.isPresent()) {
            String json = "{\"message: \"Cpf já cadastrado.\"}";
            return ResponseEntity.status(203).body(json);
        }
        repository.save(clienteModel);
        URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(clienteModel.getId()).toUri();
        return ResponseEntity.created(uri).body(clienteModel);
    }

    //ajustado
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("cpf") String cpf) {
        Optional<ClienteModel> cliente = repository.findByCpf(cpf);
        if (cliente.isPresent()) {
            String json = "Cliente Deletado Com sucesso!";
            repository.delete(cliente.get());
            return ResponseEntity.accepted().body(new String[]{json, "ID: " + cliente.get().getId().toString() + ", Nome: " + cliente.get().getNome() + ", CPF: " + cliente.get().getCpf()});
        } else {
            String json = "{\"message: \"Cliente não encontrado!\"}";
            return ResponseEntity.badRequest().body(json);
        }
    }

    //ajustado
    @PutMapping
    @Transactional
    public ResponseEntity<?> atualizar(@RequestParam("cpf") String cpf, @RequestBody @Valid ClienteModelDto cliente) {
        Optional<ClienteModel> busca = repository.findByCpf(cpf);
        if (busca.isPresent()) {
            ClienteModel clienteModel = cliente.atualizar(cpf, repository);
            return ResponseEntity.ok(new ClienteModelDto(clienteModel));
        }
        String json = "{\"message: \"Cliente não encontrado!\"}";
        return ResponseEntity.badRequest().body(json);
    }


}
