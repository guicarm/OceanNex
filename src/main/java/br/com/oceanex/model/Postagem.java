package br.com.oceanex.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import br.com.oceanex.controller.PostagemController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class Postagem {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{postagem.titulo.notblank}")
    @Size(min = 3, max = 100, message="{postagem.titulo.size}")
    private String titulo;

    @NotBlank(message = "{postagem.textopostagem.notblank}")
    @Size(min = 1, message="{postagem.textopostagem.size}")
    private String textoPostagem; // Corpo da postagem

    @NotBlank(message = "{postagem.bibliografia.notblank}")
    @Size(min = 1, message="{postagem.bibliografia.size}")
    private String bibliografia; // Links de referências da postagem

    @Lob
    private byte[] imagem;

    @ManyToOne
    private Biologo biologo;

    // Método ToModel   
    public EntityModel<Postagem> toEntityModel() {
        EntityModel<Postagem> model = EntityModel.of(this);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostagemController.class).show(this.id)).withSelfRel();
        Link allPostagemsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostagemController.class).index(null, null)).withRel("allPostagems");
        model.add(selfLink, allPostagemsLink);

        return model;
    }
}
