package br.com.oceanex.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import br.com.oceanex.controller.BiologoController;
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
public class Biologo {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{biologo.nome.notblank}")
    @Size(min = 3, max = 200, message="{biologo.nome.size}")
    private String nome;

    @NotBlank(message = "{biologo.numeroregistro.notblank}")
    @Size(min = 6, max = 12, message="{biologo.numeroregistro.size}")
    private String numeroRegistro;

    @NotBlank(message = "{biologo.cpf.notblank}")
    @Size(min = 11, max = 14, message="{biologo.cpf.size}")
    private String cpf;

    @NotBlank(message = "{biologo.crbio.notblank}")
    @Size(min = 4, max = 10, message="{biologo.crbio.size}")
    private String crbio;
    /* CRBIO é a sigla/código para o Conselho Regional de Biologia, cujo objetivo é habilitar, valorizar e defender
    a profissão do biólogo, também é responsável por fiscalizar e combater a prática ilegal da profissão.*/

    // Método ToModel   
    public EntityModel<Biologo> toEntityModel() {
        EntityModel<Biologo> model = EntityModel.of(this);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BiologoController.class).show(this.id)).withSelfRel();
        Link allBiologosLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BiologoController.class).index(null)).withRel("allBiologos");
        model.add(selfLink, allBiologosLink);

        return model;
    }
}
