package br.com.oceanex.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import br.com.oceanex.controller.FeedBackPostagemController;
import br.com.oceanex.validation.TipoStatusFeedback;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class FeedBackPostagem {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @TipoStatusFeedback(message = "{feedbackpostagem.tipostatusfeedback}")
    @NotBlank(message = "{feedbackpostagem.statusfeedback.notblank}")
    private String statusFeedback; // Like ou Deslike

    @Size(min = 3, message="{feedbackpostagem.descricaofeedback.size}")
    private String descricaoFeedback; // O usuário poderá deixar um comentário em postagens feitas pelos biólogos da plataforma.

    @ManyToOne
    private Usuario usuario;

    // Método ToModel   
    public EntityModel<FeedBackPostagem> toEntityModel() {
        EntityModel<FeedBackPostagem> model = EntityModel.of(this);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeedBackPostagemController.class).show(this.id)).withSelfRel();
        Link allFeedBackPostagemsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeedBackPostagemController.class).index(null, null)).withRel("allFeedBackPostagems");
        model.add(selfLink, allFeedBackPostagemsLink);

        return model;
    }
}
