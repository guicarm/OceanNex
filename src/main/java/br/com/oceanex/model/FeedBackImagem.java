package br.com.oceanex.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import br.com.oceanex.controller.FeedBackImagemController;
import br.com.oceanex.validation.TipoStatusFeedback;
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
public class FeedBackImagem {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @TipoStatusFeedback(message = "{feedbackimagem.tipostatusfeedback}")
    @NotBlank(message = "{feedbackimagem.statusfeedback.notblank}")
    private String statusFeedback; // Like ou Deslike

    @Size(min = 3, message="{feedbackimagem.descricaofeedback.size}")
    private String descricaoFeedback; // O usuário poderá enviar uma mensagem em seu feedback dizendo porque gostou ou não do resultado da predição.

    // Método ToModel   
    public EntityModel<FeedBackImagem> toEntityModel() {
        EntityModel<FeedBackImagem> model = EntityModel.of(this);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeedBackImagemController.class).show(this.id)).withSelfRel();
        Link allFeedBackImagemsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeedBackImagemController.class).index(null)).withRel("allFeedBackImagems");
        model.add(selfLink, allFeedBackImagemsLink);

        return model;
    }
}
