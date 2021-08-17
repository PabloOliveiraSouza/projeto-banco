package com.everis.projetobanco.inter;

import com.everis.projetobanco.controller.dto.TransferenciasModeldto;
import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TipoOperacaoEnum;
import com.everis.projetobanco.model.TransferenciasModel;
import com.everis.projetobanco.repository.ContaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface TransferenciasInterface {

    public Optional<ResponseEntity<ContaModel>> sacarConta(@RequestBody TransferenciasModel model) throws ExecutionException, InterruptedException;

    public Optional<ResponseEntity<ContaModel>> depositarConta(@RequestBody TransferenciasModel model);

    public Optional<ResponseEntity<ContaModel>> transferirContas(@RequestBody TransferenciasModel model);

    public ResponseEntity extrato(String cpf);

    public String reconhecerTipoConta(String cpf);

}
