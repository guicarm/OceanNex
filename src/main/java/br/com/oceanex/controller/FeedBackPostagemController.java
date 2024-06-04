package br.com.oceanex.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.oceanex.model.FeedBackPostagem;
import br.com.oceanex.repository.FeedBackPostagemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("feedbackpostagem")
@Slf4j
@Tag(name = "Feedback das Postagens")
public class FeedBackPostagemController {
    
    @Autowired // Injeção de Dependência
    FeedBackPostagemRepository repository;


        // ========== GET(Listar Feedbacks de postagens com PAGINAÇÃO) ============
    @GetMapping
    @Operation(
        summary = "Listar postagens",
        description = "Retorna um array com todas postagens registradas."
    )
    public Page<FeedBackPostagem> index(
        @ParameterObject @PageableDefault (size = 5, sort = "usuario", direction = Direction.ASC) Pageable pageable,
        @RequestParam(required = false) String usuarioNome
    ){
        return repository.findByUsuarioNomeIgnoreCase(usuarioNome, pageable);
    }
 
    // ========== POST(Cadastrar Feedback de postagem) ============
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar feedbacks",
        description = "Cadastra um feedback especificado através de seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Feedback criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique o corpo da requisição.")
    })
    public FeedBackPostagem create(@RequestBody @Valid FeedBackPostagem feedBackPostagem){
        log.info("Feedback Cadastrado {}", feedBackPostagem);
        return repository.save(feedBackPostagem);
    }
 
 
    // ========== GET(Detalhar Feedback de postagem) ============
    @GetMapping("{id}")
    @Operation(
        summary = "Detalhar feedbacks",
        description = "Detalha um feedback especificado através de seu ID."
    )
    public ResponseEntity<FeedBackPostagem> show(@PathVariable Long id){
        log.info("buscando feedback com id {}", id);
 
            return repository
                            .findById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }
 

    // ========== DELETE (Excluir Feedback de postagem) ============
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Excluir feedbacks",
        description = "Exclui um feedback especificado através de seu ID."
    )
    public void destroy(@PathVariable Long id){
        log.info("Feedback apagado {}.", id);
 
        verificarSeFeedbackPostagemExiste(id);
        repository.deleteById(id);
                   
    }
 
 
    // ========== PUT (Atualizar Feedback de postagem) ============
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar feedbacks",
        description = "Atualiza um feedback especificado através de seu ID."
    )
    public FeedBackPostagem update(@PathVariable Long id, @RequestBody FeedBackPostagem feedBackPostagem){
        log.info("Atualizando usuario {} para {}", id, feedBackPostagem);
 
        verificarSeFeedbackPostagemExiste(id);
        feedBackPostagem.setId(id);
        return repository.save(feedBackPostagem);
 
    }
 


  // ==== MÉTODO VERIFICAR SE FEEDBACK DE IMAGEM EXISTE ========
 private void verificarSeFeedbackPostagemExiste(Long id) {
                repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                            NOT_FOUND,
                                            "Não existe Feedback de postagem com o ID informado.")
                            );
    }
}
