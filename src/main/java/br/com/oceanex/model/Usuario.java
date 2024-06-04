package br.com.oceanex.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import br.com.oceanex.controller.UsuarioController;
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
public class Usuario extends EntityModel<Usuario> {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{usuario.nome.notblank}")
    @Size(min = 3, max = 200, message="{usuario.nome.size}")
    private String nome;

    @NotBlank(message = "{usuario.senha.notblank}")
    @Size(min = 3, message="{usuario.senha.size}")
    private String senha;

    // Método ToModel   
    public EntityModel<Usuario> toEntityModel() {
        EntityModel<Usuario> model = EntityModel.of(this);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).show(this.id)).withSelfRel();
        Link allUsuariosLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).index(null)).withRel("allUsuarios");
        model.add(selfLink, allUsuariosLink);

        return model;
    }
}
