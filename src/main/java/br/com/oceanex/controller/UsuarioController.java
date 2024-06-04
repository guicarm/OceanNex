package br.com.oceanex.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

import br.com.oceanex.model.Usuario;
import br.com.oceanex.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("usuario")
@Slf4j
@Tag(name = "Usuários")
public class UsuarioController {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    PagedResourcesAssembler<Usuario> pagedResourcesAssembler;

    // ========== GET(Listar Usuarios) ============
    @GetMapping
    @Operation(
        summary = "Listar Usuários",
        description = "Retorna um array com todos usuários registrados."
    )
    public PagedModel<EntityModel<Usuario>> index(Pageable pageable) {
        Page<Usuario> usuarios = repository.findAll(pageable);
        return pagedResourcesAssembler.toModel(usuarios, Usuario::toEntityModel);
    }

    // ========== POST(Cadastrar Usuario) ============
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar Usuários",
        description = "Cadastra um usuário especificado através de seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique o corpo da requisição.")
    })
    public ResponseEntity<EntityModel<Usuario>> create(@RequestBody @Valid Usuario usuario) {
        log.info("Usuario Cadastrado {}", usuario);
        repository.save(usuario);

        EntityModel<Usuario> entityModel = usuario.toEntityModel();
        return ResponseEntity
                    .created(entityModel.getRequiredLink("self").toUri())
                    .body(entityModel);
    }

    // ========== GET(Detalhar Usuario) ============
    @GetMapping("{id}")
    @Operation(
        summary = "Detalhar usuários",
        description = "Detalha um usuário especificado através de seu ID."
    )
    public ResponseEntity<EntityModel<Usuario>> show(@PathVariable Long id) {
        log.info("buscando usuario com id {}", id);

        return repository.findById(id)
                .map(usuario -> ResponseEntity.ok(usuario.toEntityModel()))
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== DELETE (Excluir Usuario) ============
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Excluir usuários",
        description = "Exclui um usuário especificado através de seu ID."
    )
    public void destroy(@PathVariable Long id) {
        log.info("Usuario apagado {}.", id);

        verificarSeUsuarioExiste(id);
        repository.deleteById(id);
    }

    // ========== PUT (Atualizar Usuario) ============
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar usuários",
        description = "Atualiza um usuário especificado através de seu ID."
    )
    public ResponseEntity<EntityModel<Usuario>> update(@PathVariable Long id, @RequestBody @Valid Usuario usuario) {
        log.info("Atualizando usuario {} para {}", id, usuario);

        verificarSeUsuarioExiste(id);
        usuario.setId(id);
        repository.save(usuario);

        return ResponseEntity.ok(usuario.toEntityModel());
    }

    // ==== MÉTODO VERIFICAR SE USUARIO EXISTE ========
    private void verificarSeUsuarioExiste(Long id) {
        repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Não existe usuario com o ID informado.")
        );
    }
}
