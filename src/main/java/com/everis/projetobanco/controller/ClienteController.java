package com.everis.projetobanco.controller;

import com.everis.projetobanco.controller.dto.ClienteModelDto;
import com.everis.projetobanco.controller.dto.DetalharClienteDTO;
import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;


    @GetMapping(path = "/{cpf}")
    public ResponseEntity<DetalharClienteDTO> consultarCPF(@PathVariable String cpf) {
        Optional<ClienteModel> cliente = repository.findByCpf(cpf);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(new DetalharClienteDTO(cliente.get()));
        }
        return ResponseEntity.notFound().build();
        }
    @GetMapping
    public List<ClienteModel> consultarTodos() {
        return repository.findAll();
    }
    @GetMapping("/list")
    public List<ClienteModelDto> lista() {
        List<ClienteModel> clientes = repository.findAll();
        return ClienteModelDto.converter(clientes);
    }
    @PostMapping
    public ResponseEntity<ClienteModel> salvar(@RequestBody @Valid ClienteModel clienteModel, UriComponentsBuilder uriBuilder) {
        Optional<ClienteModel> veri = repository.findByCpf(clienteModel.getCpf());
        if (veri.isPresent()){
            return ResponseEntity.badRequest().body(clienteModel);
        }
        repository.save(clienteModel);
        URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(clienteModel.getId()).toUri();
        return ResponseEntity.created(uri).body(clienteModel);
    }

    @DeleteMapping(value = "{cpf}")
    public ResponseEntity <?>delete(@PathVariable("cpf") String cpf) {
        Optional<ClienteModel> cliente = repository.findByCpf(cpf);
        if (cliente.isPresent()) {
            repository.delete(cliente.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{cpf}")
    @Transactional
    public ResponseEntity<ClienteModelDto> atualizar(@PathVariable("cpf") String cpf, @RequestBody @Valid ClienteModelDto cliente) {
        Optional <ClienteModel> busca = repository.findByCpf(cpf);
        if (busca.isPresent()) {
            ClienteModel clienteModel = cliente.atualizar(cpf, repository);
            return ResponseEntity.ok(new ClienteModelDto(clienteModel));
        }
         return  ResponseEntity.notFound().build();
    }


}
