package com.everis.projetobanco.repository;


import com.everis.projetobanco.model.ContaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<ContaModel, Integer> {
    Optional<ContaModel> findByCpf(String cpf);
    //Optional<ContaModel> findBynCOnta(Integer nConta);
}
