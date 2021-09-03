package com.everis.projetobanco.repository;

import com.everis.projetobanco.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {

    Optional<ClienteModel> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    @Query("SELECT  e FROM conta d INNER JOIN usuario e")
    List<ClienteModel> buscaAvançada(String cpf);

}
