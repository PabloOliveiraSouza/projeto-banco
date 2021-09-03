package com.everis.projetobanco.inter.impl;

import com.everis.projetobanco.inter.ExceptionJson;
import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TransferenciasModel;
import com.everis.projetobanco.repository.ClienteRepository;
import com.everis.projetobanco.repository.ContaRepository;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.net.URI;
import java.util.Optional;

public class ExceptionImpl implements ExceptionJson {
    //exception contas
    @Override
    public ResponseEntity<JSONObject> erroSemSaldo(double saldo, Integer numeroConta) {
        JSONObject json = new JSONObject();
        json.put("message", "Você não tem saldo suficiente para sacar");
        json.put("conta", numeroConta);
        json.put("saldo", "Seu saldo atual é: " + saldo);
        return ResponseEntity.badRequest().body(json);
    }

    @Override
    public ResponseEntity<JSONObject> contaNEncontrada(Integer numeroConta) {
        JSONObject json = new JSONObject();
        json.put("message", "Conta Não Encontrada");
        json.put("numeroConta", numeroConta);
        return ResponseEntity.badRequest().body(json);
    }

    @Override
    public ResponseEntity<JSONObject> contasNEncontrada(Integer saida, Integer entrada, ContaRepository repository) {
        Optional<ContaModel> contaEntrada = repository.findByNumeroConta(entrada);
        Optional<ContaModel> contaSaida = repository.findByNumeroConta(saida);
        //verificação conta entrada
        if (contaEntrada.isEmpty() && contaSaida.isEmpty()) {
            JSONObject json = new JSONObject();
            json.put("message", "Contas Não Encontrada");
            json.put("Numero Conta Transferencia Saída", saida);
            json.put("Numero Conta Transferencia Entrada", entrada);
            return ResponseEntity.badRequest().body(json);
        }else if (contaSaida.isEmpty()){
            JSONObject json = new JSONObject();
            json.put("message", "Conta Não Encontrada");
            json.put("Numero Conta Transferencia Saida", saida);
            return ResponseEntity.badRequest().body(json);
        }else if (contaEntrada.isEmpty()){
            JSONObject json = new JSONObject();
            json.put("message", "Conta Não Encontrada");
            json.put("Numero Conta Transferencia Entrada", entrada);
            return ResponseEntity.badRequest().body(json);
        }
        return null;
    }

    @Override
    public ResponseEntity<JSONObject> SaqueEfetuado(TransferenciasModel model, Integer qtdSaques, double taxa, URI uri) {
        JSONObject json = new JSONObject();
        json.put("Comprovante Saque", model);
        json.put("message", "Você possui " + contagem(qtdSaques) + " saques gratuitos, após zerar saques gratuitos será cobrada uma taxa " +
                "adicional de: " + taxa);
        return ResponseEntity.created(uri).body(json);
    }

    @Override
    public ResponseEntity<JSONObject> depositoEfetuado(TransferenciasModel model, URI uri) {
        JSONObject json = new JSONObject();
        json.put("Comprovante Deposito", model);
        return ResponseEntity.created(uri).body(json);
    }

    @Override
    public ResponseEntity<TransferenciasModel[]> TranferenciaEfetuado(TransferenciasModel transacaoEntrada, TransferenciasModel transacaoSaida, URI uri) {
        return ResponseEntity.created(uri).body(new TransferenciasModel[]{transacaoEntrada, transacaoSaida});
    }

    public int contagem(Integer qtdsaques) {
        switch (qtdsaques) {
            case 0:
                return 5;
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 2;
            case 4:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public ResponseEntity<JSONObject> deleteContaEfetuado(Integer id, Integer numeroConta, String agencia) {
        JSONObject json = new JSONObject();
        json.put("message", "Delete Efetuado Com sucesso");
        json.put("id", id);
        json.put("numeroConta", numeroConta);
        json.put("agencia", agencia);
        return ResponseEntity.status(400).body(json);
    }

    //exceptions crud
    @Override
    public ResponseEntity<JSONObject> contaPresente(ContaModel conta) {
        JSONObject json = new JSONObject();
        json.put("message", "Conta já Cadastrada");
        json.put("conta", conta);
        return ResponseEntity.status(203).body(json);
    }

    @Override
    public ResponseEntity<JSONObject[]> contasPresente(ContaRepository conta, String cpf) {
        JSONObject json1 = new JSONObject();
        JSONObject json2 = new JSONObject();
        json1.put("message", "Existe Contas atreladas a esse usuário");
        json2.put("contas", conta.findAllByCpf(cpf));
        return ResponseEntity.status(203).body(new JSONObject[]{json1, json2});
    }

    @Override
    public ResponseEntity<JSONObject> contaSalvo(ContaModel conta, URI uri) {
        JSONObject json = new JSONObject();
        json.put("message", "Conta Salva Com Sucesso");
        json.put("conta", conta);
        return ResponseEntity.created(uri).body(json);
    }

    // crud cliente
    @Override
    public ResponseEntity<JSONObject> deleteClienteEfetuado(Integer id, String nome, String cpf) {
        JSONObject json = new JSONObject();
        json.put("message", "Delete Efetuado Com sucesso");
        json.put("id", id);
        json.put("nome", nome);
        json.put("cpf", cpf);
        return ResponseEntity.status(400).body(json);
    }

    @Override
    public ResponseEntity<JSONObject> clientePresente(ClienteModel cliente) {
        JSONObject json = new JSONObject();
        json.put("message", "Cliente já Cadastrada");
        json.put("conta", cliente);
        return ResponseEntity.status(203).body(json);
    }

    @Override
    public ResponseEntity<JSONObject> clienteSalvo(ClienteModel cliente, URI uri) {
        JSONObject json = new JSONObject();
        json.put("message", "Cliente Salva Com Sucesso");
        json.put("cliente", cliente);
        return ResponseEntity.created(uri).body(json);
    }

    @Override
    public ResponseEntity<JSONObject> clienteNEncontrada(String cpf) {
        JSONObject json = new JSONObject();
        json.put("message", "Cliente Não Encontrada");
        json.put("cpf", cpf);
        return ResponseEntity.badRequest().body(json);
    }

    @Override
    public boolean clienteExiste(ClienteRepository repository, String cpf){
        if (repository.existsByCpf(cpf)){
            return true;
        }
        return false;
    }
}
