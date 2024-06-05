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

import br.com.oceanex.model.Biologo;
import br.com.oceanex.repository.BiologoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("biologo")
@Slf4j
@Tag(name = "Biólogos")
public class BiologoController {

    @Autowired
    BiologoRepository repository;

    @Autowired
    PagedResourcesAssembler<Biologo> pagedResourcesAssembler;

    // ========== GET(Listar Biólogos) ============
    @GetMapping
    @Operation(
        summary = "Listar Biólogos",
        description = "Retorna um array com todos os biólogos registrados."
    )
    public PagedModel<EntityModel<Biologo>> index(@ParameterObject Pageable pageable) {
        Page<Biologo> biologos = repository.findAll(pageable);
        return pagedResourcesAssembler.toModel(biologos, Biologo::toEntityModel);
    }

    // ========== POST(Cadastrar Biólogo) ============
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar Biólogos",
        description = "Cadastra um biólogo especificado através de seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Biólogo criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique o corpo da requisição.")
    })
    public ResponseEntity<EntityModel<Biologo>> create(@RequestBody @Valid Biologo biologo) {
        log.info("Biólogo Cadastrado {}", biologo);
        repository.save(biologo);

        EntityModel<Biologo> entityModel = biologo.toEntityModel();
        return ResponseEntity
                    .created(entityModel.getRequiredLink("self").toUri())
                    .body(entityModel);
    }

    // ========== GET(Detalhar Biólogo) ============
    @GetMapping("{id}")
    @Operation(
        summary = "Detalhar Biólogos",
        description = "Detalha um biólogo especificado através de seu ID."
    )
    public ResponseEntity<EntityModel<Biologo>> show(@PathVariable Long id) {
        log.info("buscando biólogo com id {}", id);

        return repository.findById(id)
                .map(biologo -> ResponseEntity.ok(biologo.toEntityModel()))
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== DELETE (Excluir Biólogo) ============
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Excluir Biólogos",
        description = "Exclui um biólogo especificado através de seu ID."
    )
    public void destroy(@PathVariable Long id) {
        log.info("Biólogo apagado {}.", id);

        verificarSeBiologoExiste(id);
        repository.deleteById(id);
    }

    // ========== PUT (Atualizar Biólogo) ============
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar Biólogos",
        description = "Atualiza um biólogo especificado através de seu ID."
    )
    public ResponseEntity<EntityModel<Biologo>> update(@PathVariable Long id, @RequestBody @Valid Biologo biologo) {
        log.info("Atualizando biólogo {} para {}", id, biologo);

        verificarSeBiologoExiste(id);
        biologo.setId(id);
        repository.save(biologo);

        return ResponseEntity.ok(biologo.toEntityModel());
    }

    // ==== MÉTODO VERIFICAR SE BIÓLOGO EXISTE ========
    private void verificarSeBiologoExiste(Long id) {
        repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Não existe biólogo com o ID informado.")
        );
    }
}
