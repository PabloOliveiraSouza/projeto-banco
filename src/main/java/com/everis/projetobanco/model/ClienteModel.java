package com.everis.projetobanco.model;

import javassist.expr.NewArray;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "usuario")
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50)
    @NotNull
    @NotEmpty
    private String nome;
    @CPF
    private String cpf;
    @Column(length = 50)
    @NotNull
    @NotEmpty
    @Pattern(regexp = "([\\w\\W]+)\\s(\\d+)", message = "Informe o nome da Rua e o número apenas.")
    private String endereco;
    @Column(length = 20)
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(?:(?:\\+|00)?(55)\\s?)?(?:(?:\\(?[1-9][0-9]\\)?)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})-?(\\d{4}))$", message = "Telefone Invalido")
    private String fone;
    @Column(length = 50)
    private LocalDateTime datacriacao = LocalDateTime.now();
    //@OneToMany(mappedBy = "cpf", orphanRemoval = true, cascade = CascadeType.ALL)
    //private List<ContaModel> contas;


}

