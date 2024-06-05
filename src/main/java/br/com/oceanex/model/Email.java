package br.com.oceanex.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import br.com.oceanex.controller.EmailController;
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
public class Email {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{email.nomeemail.notblank}")
    @Size(min = 3, max = 100, message="{email.nomeemail.size}")
    private String nomeEmail;

    @NotBlank(message = "{email.statusemail.notblank}")
    @Size(min = 1, max = 1, message="{email.statusemail.size}")
    private String statusEmail;
    // A - Ativo
    // I - Inativo

    // Método ToModel   
    public EntityModel<Email> toEntityModel() {
        EntityModel<Email> model = EntityModel.of(this);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailController.class).show(this.id)).withSelfRel();
        Link allEmailsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailController.class).index(null)).withRel("allEmails");
        model.add(selfLink, allEmailsLink);

        return model;
    }
}
