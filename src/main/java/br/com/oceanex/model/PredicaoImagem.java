package br.com.oceanex.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import br.com.oceanex.controller.PredicaoImagemController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    @PositiveOrZero(message="{predicaoimagem.taxapredicao.positiveorzero}")
    private Double taxaPredicao;

    @NotBlank(message = "{predicaoimagem.descricaopredicao.notblank}")
    @Size(min = 3, max = 100, message="{predicaoimagem.descricaopredicao.size}")
    private String descricaoPredicao;

    @NotNull(message = "{predicaoimagem.imagem.notnull}")
    @Lob
    private byte[] imagem;

    // Método ToModel   
    public EntityModel<PredicaoImagem> toEntityModel() {
        EntityModel<PredicaoImagem> model = EntityModel.of(this);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PredicaoImagemController.class).show(this.id)).withSelfRel();
        Link allPredicaoImagemsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PredicaoImagemController.class).index()).withRel("allPredicaoImagems");
        model.add(selfLink, allPredicaoImagemsLink);

        return model;
    }
}