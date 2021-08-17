package com.everis.projetobanco.repository;

import com.everis.projetobanco.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {

    Optional<ClienteModel> findByCpf(String cpf);

}
