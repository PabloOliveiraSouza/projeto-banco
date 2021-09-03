package com.everis.projetobanco.controller;

import com.everis.projetobanco.controller.dto.ClienteModelDto;
import com.everis.projetobanco.inter.ExceptionJson;
import com.everis.projetobanco.inter.impl.ExceptionImpl;
import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.repository.ClienteRepository;
import com.everis.projetobanco.repository.ContaRepository;
import org.json.simple.JSONObject;
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
    @Autowired
    private ContaRepository repositoryConta;

    @GetMapping(path = "/")
    public ResponseEntity<?> consultarPorCpf(@RequestParam String cpf) {
        Optional<ClienteModel> cliente = repository.findByCpf(cpf);
        if (cliente.isPresent()) {
            return ResponseEntity.ok().body(cliente);
        }
        ExceptionJson exceptionJson = new ExceptionImpl();
        return exceptionJson.clienteNEncontrada(cpf);
    }
//
        @GetMapping(path = "/teste")
    public List<?> teste(@RequestParam String cpf) {
        //List<List> cliente = repository.buscaAvançada(cpf);
        return repository.buscaAvançada(cpf);
    }
    @GetMapping
    public List<?> consultarTodos() {
        if (repository.findAll().isEmpty()) {
            JSONObject json = new JSONObject();
            json.put("message", "Essa lista está vazia");
            json.put("quantidade", 0);
            List<JSONObject> lista = Arrays.asList(new JSONObject[]{json});
            return lista;
        }
        return repository.findAll();
    }

    @GetMapping("/list")
    public List<ClienteModelDto> lista() {
        List<ClienteModel> clientes = repository.findAll();
        return ClienteModelDto.converter(clientes);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid ClienteModel clienteModel, UriComponentsBuilder uriBuilder) {
        Optional<ClienteModel> veri = repository.findByCpf(clienteModel.getCpf());
        ExceptionJson exceptionJson = new ExceptionImpl();
        if (exceptionJson.clienteExiste(repository, clienteModel.getCpf())) {
            return exceptionJson.clientePresente(veri.get());
        }
        repository.save(clienteModel);
        URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(clienteModel.getId()).toUri();

        return exceptionJson.clienteSalvo(clienteModel, uri);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("cpf") String cpf) {
        Optional<ClienteModel> cliente = repository.findByCpf(cpf);
        List<ContaModel> contas = repositoryConta.findAllByCpf(cpf);
        ExceptionJson exceptionJson = new ExceptionImpl();
        if (contas.isEmpty()) {
            if (exceptionJson.clienteExiste(repository, cpf)) {
                repository.delete(cliente.get());
                return exceptionJson.deleteClienteEfetuado(cliente.get().getId(), cliente.get().getNome(), cliente.get().getCpf());
            } else {
                return exceptionJson.clienteNEncontrada(cpf);
            }
        }
        return exceptionJson.contasPresente(repositoryConta,cpf);
    }


    @PutMapping
    @Transactional
    public ResponseEntity<?> atualizar(@RequestParam("cpf") String cpf, @RequestBody @Valid ClienteModelDto cliente) {
        Optional<ClienteModel> busca = repository.findByCpf(cpf);
        if (busca.isPresent()) {
            ClienteModel clienteModel = cliente.atualizar(cpf, repository);
            return ResponseEntity.ok(new ClienteModelDto(clienteModel));
        }
        ExceptionJson exceptionJson = new ExceptionImpl();
        return exceptionJson.clienteNEncontrada(cpf);
    }
}
