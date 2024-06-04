package br.com.oceanex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{usuario.nome.notblank}")
    @Size(min = 3, max = 200, message="{usuario.nome.size}")
    private String nome;

    @NotBlank(message = "{usuario.senha.notblank}")
    @Size(min = 3, message="{usuario.senha.size}")
    private String senha;

}
