package br.com.oceanex.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.oceanex.model.Email;
import br.com.oceanex.repository.EmailRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("email")
@Slf4j
@Tag(name = "Emails")
public class EmailController {

    @Autowired
    EmailRepository repository;

    @Autowired
    PagedResourcesAssembler<Email> pagedResourcesAssembler;

    // ========== GET(Listar Emails) ============
    @GetMapping
    @Operation(
        summary = "Listar Emails",
        description = "Retorna um array com todos emails registrados."
    )
    public PagedModel<EntityModel<Email>> index(@ParameterObject Pageable pageable) {
        Page<Email> emails = repository.findAll(pageable);
        return pagedResourcesAssembler.toModel(emails, Email::toEntityModel);
    }

    // ========== POST(Cadastrar Email) ============
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar Emails",
        description = "Cadastra um email especificado através de seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Email criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique o corpo da requisição.")
    })
    public ResponseEntity<EntityModel<Email>> create(@RequestBody @Valid Email email) {
        log.info("Email Cadastrado {}", email);
        repository.save(email);

        EntityModel<Email> entityModel = email.toEntityModel();
        return ResponseEntity
                    .created(entityModel.getRequiredLink("self").toUri())
                    .body(entityModel);
    }

    // ========== GET(Detalhar Email) ============
    @GetMapping("{id}")
    @Operation(
        summary = "Detalhar emails",
        description = "Detalha um email especificado através de seu ID."
    )
    public ResponseEntity<EntityModel<Email>> show(@PathVariable Long id) {
        log.info("buscando email com id {}", id);

        return repository.findById(id)
                .map(email -> ResponseEntity.ok(email.toEntityModel()))
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== DELETE (Excluir Email) ============
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Excluir emails",
        description = "Exclui um email especificado através de seu ID."
    )
    public void destroy(@PathVariable Long id) {
        log.info("Email apagado {}.", id);

        verificarSeEmailExiste(id);
        repository.deleteById(id);
    }

    // ========== PUT (Atualizar Email) ============
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar emails",
        description = "Atualiza um email especificado através de seu ID."
    )
    public ResponseEntity<EntityModel<Email>> update(@PathVariable Long id, @RequestBody @Valid Email email) {
        log.info("Atualizando email {} para {}", id, email);

        verificarSeEmailExiste(id);
        email.setId(id);
        repository.save(email);

        return ResponseEntity.ok(email.toEntityModel());
    }

    // ==== MÉTODO VERIFICAR SE EMAIL EXISTE ========
    private void verificarSeEmailExiste(Long id) {
        repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Não existe email com o ID informado.")
        );
    }
}
