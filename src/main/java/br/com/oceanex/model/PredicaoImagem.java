package br.com.oceanex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class PredicaoImagem {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "{predicaoimagem.taxapredicao.notnull}")
    @Positive(message="{predicaoimagem.taxapredicao.positive}")
    private Double taxaPredicao;

    @NotBlank(message = "{predicaoimagem.descricaopredicao.notblank}")
    @Size(min = 3, max = 100, message="{predicaoimagem.descricaopredicao.size}")
    private String descricaoPredicao;

    @NotNull(message = "{predicaoimagem.imagem.notnull}")
    @Lob
    private byte[] imagem;
}