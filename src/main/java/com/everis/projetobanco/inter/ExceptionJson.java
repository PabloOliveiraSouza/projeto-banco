package com.everis.projetobanco.inter;

import com.everis.projetobanco.model.ClienteModel;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TransferenciasModel;
import com.everis.projetobanco.repository.ClienteRepository;
import com.everis.projetobanco.repository.ContaRepository;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public interface ExceptionJson {
    public ResponseEntity<JSONObject> erroSemSaldo(double saldo, Integer numeroConta);

    public ResponseEntity<JSONObject> contaNEncontrada(Integer numeroConta);

    public ResponseEntity<JSONObject> SaqueEfetuado(TransferenciasModel model, Integer qtdSaques, double taxa, URI uri);

    public ResponseEntity<TransferenciasModel[]> TranferenciaEfetuado(TransferenciasModel transacaoEntrada, TransferenciasModel transacaoSaida, URI uri);

    public ResponseEntity<JSONObject> contasNEncontrada(Integer saida, Integer entrada, ContaRepository repository);

    public ResponseEntity<JSONObject> depositoEfetuado(TransferenciasModel model, URI uri);

    public ResponseEntity<JSONObject> deleteContaEfetuado(Integer id, Integer numeroConta, String agencia);

    public ResponseEntity<JSONObject> contaPresente(ContaModel conta);

    public ResponseEntity<JSONObject[]> contasPresente(ContaRepository conta, String cpf);

    public ResponseEntity<JSONObject> contaSalvo(ContaModel conta, URI uri);

    public ResponseEntity<JSONObject> deleteClienteEfetuado(Integer id, String nome, String cpf);

    public ResponseEntity<JSONObject> clientePresente(ClienteModel conta);

    public ResponseEntity<JSONObject> clienteSalvo(ClienteModel conta, URI uri);

    public ResponseEntity<JSONObject> clienteNEncontrada(String cpf);

    public boolean clienteExiste(ClienteRepository repository, String cpf);

}
