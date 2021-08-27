package com.everis.projetobanco.repository;

import com.everis.projetobanco.model.ContaModel;
import com.everis.projetobanco.model.TransferenciasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoRepository extends JpaRepository<TransferenciasModel, Integer> {
//    Optional<TransferenciasModel> findByCpf(String cpf);

    List<TransferenciasModel> findAllByNumeroConta(Integer cpf);
}
