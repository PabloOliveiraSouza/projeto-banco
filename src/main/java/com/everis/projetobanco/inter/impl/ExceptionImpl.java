package com.everis.projetobanco.inter.impl;

import com.everis.projetobanco.inter.ExceptionJson;
import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TransferenciasModel;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public class ExceptionImpl implements ExceptionJson {
//exception contas
    @Override
    public ResponseEntity<JSONObject> erroSemSaldo (double saldo,Integer numeroConta){
        JSONObject json = new JSONObject();
        json.put("Message", "Você não tem saldo suficiente para sacar");
        json.put("Conta",numeroConta);
        json.put("Saldo", "Seu saldo atual é: " + saldo);
        return  ResponseEntity.badRequest().body(json);
    }

    @Override
    public ResponseEntity<JSONObject> contaNEncontrada(Integer numeroConta) {
        JSONObject json = new JSONObject();
        json.put("Message", "Conta Não Encontrada");
        json.put("numeroConta", numeroConta);
        return  ResponseEntity.badRequest().body(json);
    }
    @Override
    public ResponseEntity<JSONObject> contasNEncontrada(Integer saida, Integer entrada) {
        JSONObject json = new JSONObject();
        json.put("Message", "Conta Não Encontrada");
        json.put("Numero Conta Transferencia Saída", saida);
        json.put("Numero Conta Transferencia Entrada", entrada);
        return  ResponseEntity.badRequest().body(json);
    }

    @Override
    public ResponseEntity<JSONObject> SaqueEfetuado(TransferenciasModel model, Integer qtdSaques, double taxa, URI uri) {
        JSONObject json = new JSONObject();
        json.put("Comprovante Saque",model);
        json.put("Mensagem","Você possui " + contagem(qtdSaques) + " saques gratuitos, após zerar saques gratuitos será cobrada uma taxa " +
                "adicional de: "+ taxa);
        return  ResponseEntity.created(uri).body(json);
    }
    @Override
    public ResponseEntity<JSONObject> depositoEfetuado(TransferenciasModel model, URI uri) {
        JSONObject json = new JSONObject();
        json.put("Comprovante Deposito",model);
        return  ResponseEntity.created(uri).body(json);
    }

    @Override
    public ResponseEntity<TransferenciasModel[]> TranferenciaEfetuado(TransferenciasModel transacaoEntrada, TransferenciasModel transacaoSaida, URI uri) {
        return ResponseEntity.created(uri).body(new TransferenciasModel[]{transacaoEntrada, transacaoSaida});
    }
    public int contagem(Integer qtdsaques){
        switch (qtdsaques) {
            case 0:
                return 5 ;
            case 1:
                return 4 ;
            case 2:
                return 3 ;
            case 3:
                return 2 ;
            case 4:
                return 1 ;
            default:  return 0 ;
        }
    }
    @Override
    public ResponseEntity<JSONObject> deleteContaEfetuado(Integer id, Integer numeroConta, String agencia) {
        JSONObject json = new JSONObject();
        json.put("message","Delete Efetuado Com sucesso");
        json.put("id",id);
        json.put("numeroConta",numeroConta);
        json.put("agencia",agencia);
        return ResponseEntity.status(400).body(json);
    }
    //exceptions crud
    @Override
    public ResponseEntity<JSONObject> contaPresente(ContaModel conta) {
        JSONObject json = new JSONObject();
        json.put("Message", "Conta já Cadastrada");
        json.put("Conta", conta);
        return ResponseEntity.status(203).body(json);
    }
    @Override
    public ResponseEntity<JSONObject> contaSalvo(ContaModel conta, URI uri) {
        JSONObject json = new JSONObject();
        json.put("Message", "Conta Salva Com Sucesso");
        json.put("Conta", conta);
        return ResponseEntity.created(uri).body(json);
    }
// crud cliente
    @Override
    public ResponseEntity<JSONObject> deleteClienteEfetuado(Integer id, String nome, String cpf) {
        JSONObject json = new JSONObject();
        json.put("message","Delete Efetuado Com sucesso");
        json.put("id",id);
        json.put("nome",nome);
        json.put("cpf",cpf);
        return ResponseEntity.status(400).body(json);
    }

    @Override
    public ResponseEntity<JSONObject> clientePresente(ClienteModel cliente) {
        JSONObject json = new JSONObject();
        json.put("Message", "Cliente já Cadastrada");
        json.put("Conta", cliente);
        return ResponseEntity.status(203).body(json);
    }

    @Override
    public ResponseEntity<JSONObject> clienteSalvo(ClienteModel cliente, URI uri) {
        JSONObject json = new JSONObject();
        json.put("Message", "Cliente Salva Com Sucesso");
        json.put("Cliente", cliente);
        return ResponseEntity.created(uri).body(json);
    }

    @Override
    public ResponseEntity<JSONObject> clienteNEncontrada(String cpf) {
        JSONObject json = new JSONObject();
        json.put("Message", "Cliente Não Encontrada");
        json.put("cpf", cpf);
        return  ResponseEntity.badRequest().body(json);
    }
}
