package br.com.oceanex.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.oceanex.model.FeedBackImagem;
import br.com.oceanex.repository.FeedBackImagemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("feedbackimagem")
@Slf4j
@Tag(name = "Feedbacks das Imagem")
public class FeedBackImagemController {

    @Autowired // Injeção de Dependência
    FeedBackImagemRepository repository;


    // ========== GET(Listar Feedbacks de imagens) ============
    @GetMapping
    @Operation(
        summary = "Listar feedbacks",
        description = "Retorna um array com todos feedbacks registrados."
    )
    public List<FeedBackImagem> index(){
        return repository.findAll();
    }
 
 
    // ========== POST(Cadastrar Feedback de imagem) ============
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
    public FeedBackImagem create(@RequestBody @Valid FeedBackImagem feedBackImagem){
        log.info("Feedback Cadastrado {}", feedBackImagem);
        return repository.save(feedBackImagem);
    }
 
 
    // ========== GET(Detalhar Feedback de imagem) ============
    @GetMapping("{id}")
    @Operation(
        summary = "Detalhar feedbacks",
        description = "Detalha um feedback especificado através de seu ID."
    )
    public ResponseEntity<FeedBackImagem> show(@PathVariable Long id){
        log.info("buscando feedback com id {}", id);
 
            return repository
                            .findById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }
 

    // ========== DELETE (Excluir Feedback de imagem) ============
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Excluir feedbacks",
        description = "Exclui um feedback especificado através de seu ID."
    )
    public void destroy(@PathVariable Long id){
        log.info("Feedback apagado {}.", id);
 
        verificarSeFeedbackImagemExiste(id);
        repository.deleteById(id);
                   
    }
 
 
    // ========== PUT (Atualizar Feedback de imagem) ============
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar feedbacks",
        description = "Atualiza um feedback especificado através de seu ID."
    )
    public FeedBackImagem update(@PathVariable Long id, @RequestBody FeedBackImagem feedBackImagem){
        log.info("Atualizando usuario {} para {}", id, feedBackImagem);
 
        verificarSeFeedbackImagemExiste(id);
        feedBackImagem.setId(id);
        return repository.save(feedBackImagem);
 
    }
 


  // ==== MÉTODO VERIFICAR SE FEEDBACK DE IMAGEM EXISTE ========
 private void verificarSeFeedbackImagemExiste(Long id) {
                repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                            NOT_FOUND,
                                            "Não existe Feedback de imagem com o ID informado.")
                            );
    }
    
}
