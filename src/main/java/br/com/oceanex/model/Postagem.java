package br.com.oceanex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Postagem {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{postagem.titulo.notblank}")
    @Size(min = 3, max = 100, message="{postagem.titulo.size}")
    private String titulo; // Título da postagem

    @NotBlank(message = "{postagem.textopostagem.notblank}")
    @Size(min = 1, message="{postagem.textopostagem.size}")
    private String textoPostagem; // Corpo da postagem

    @NotBlank(message = "{postagem.bibliografia.notblank}")
    @Size(min = 1, message="{postagem.bibliografia.size}")
    private String bibliografia; // Links de referências da postagem

    @NotNull(message = "{postagem.imagem.notnull}")
    @Lob
    private byte[] imagem;
    // private String imagem;

}
