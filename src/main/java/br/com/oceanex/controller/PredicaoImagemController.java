package br.com.oceanex.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import br.com.oceanex.model.PredicaoImagem;
import br.com.oceanex.repository.PredicaoImagemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("predicaoimagem")
@Slf4j
@Tag(name = "Predições da Imagem")
public class PredicaoImagemController {
    
    @Autowired // Injeção de Dependência
    PredicaoImagemRepository repository;


    // ========== GET(Listar Predições de Imagem) ============
    @GetMapping
    @Operation(
        summary = "Listar Predições",
        description = "Retorna um array com todas as predições feitas."
    )
    public List<EntityModel<PredicaoImagem>> index(){
        List<PredicaoImagem> predicoes = repository.findAll();
        return predicoes.stream()
                .map(predicao -> {
                    Long id = predicao.getId();
                    Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PredicaoImagemController.class).show(id)).withSelfRel();
                    return EntityModel.of(predicao, selfLink);
                })
                .collect(Collectors.toList());
    }
 
 
    // ========== POST(Cadastrar Predição) ============
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar Predições",
        description = "Cadastra uma predição."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Predição criada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique o corpo da requisição.")
    })
    public PredicaoImagem create(@RequestParam("taxaPredicao") Double taxaPredicao,
                                @RequestParam("descricaoPredicao") String descricaoPredicao,
                                @RequestParam("imagem") MultipartFile imagem) throws IOException {
            PredicaoImagem predicaoImagem = PredicaoImagem.builder()
                        .taxaPredicao(taxaPredicao)
                        .descricaoPredicao(descricaoPredicao)
                        .imagem(imagem.getBytes())
                        .build();
    log.info("Predicao Cadastrada {}", predicaoImagem);
    return repository.save(predicaoImagem);
}

 
    // ========== GET(Detalhar Predição) ============
    @GetMapping("{id}")
    @Operation(
        summary = "Detalhar Predições",
        description = "Detalha uma predição especificada através de seu ID."
    )
    public ResponseEntity<EntityModel<PredicaoImagem>> show(@PathVariable Long id){
        log.info("buscando predição com id {}", id);
 
        PredicaoImagem predicao = repository.findById(id)
                                    .orElseThrow(() -> new ResponseStatusException(
                                        NOT_FOUND, "Não existe Predicao com o ID informado."
                                    ));
        
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PredicaoImagemController.class).show(id)).withSelfRel();
        EntityModel<PredicaoImagem> model = EntityModel.of(predicao, selfLink);
        return ResponseEntity.ok(model);
    }
 

    // ========== DELETE (Excluir Predição) ============
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Excluir predições",
        description = "Exclui uma predição especificada através de seu ID."
    )
    public void destroy(@PathVariable Long id){
        log.info("Predição apagada {}.", id);
 
        verificarSePredicaoImagemExiste(id);
        repository.deleteById(id);
                   
    }
 
 
    // ========== PUT (Atualizar Predição) ============
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar predições",
        description = "Atualiza uma predição especificada através de seu ID."
    )
    public PredicaoImagem update(@PathVariable Long id, @RequestBody PredicaoImagem predicaoImagem){
        log.info("Atualizando predição {} para {}", id, predicaoImagem);
 
        verificarSePredicaoImagemExiste(id);
        predicaoImagem.setId(id);
        return repository.save(predicaoImagem);
 
    }
 


  // ==== MÉTODO VERIFICAR SE A PREDIÇÃO EXISTE ========
 private void verificarSePredicaoImagemExiste(Long id) {
                repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                            NOT_FOUND,
                                            "Não existe Predicao com o ID informado.")
                            );
    }


 // ========== GET(Obter Imagem) ============
    @GetMapping("{id}/imagem")
    @Operation(
            summary = "Obter imagem da predição",
            description = "Retorna a imagem da predição especificada através de seu ID."
    )
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        log.info("buscando imagem da predição com id {}", id);

        return repository
                .findById(id)
                .map(predicaoImagem -> {
                    byte[] imagem = predicaoImagem.getImagem();
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"imagem.jpg\"")
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(imagem);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== GET(Listar URLs de Imagens) ============
    @GetMapping("imagens")
    @Operation(
            summary = "Listar URLs de imagens",
            description = "Retorna uma lista de URLs para todas as imagens registradas."
    )
    public List<String> listarUrlsImagem() {
        log.info("listando todas as imagens");
        return repository.findAll().stream()
                .map(predicaoImagem -> "http://localhost:8080/predicaoimagem/" + predicaoImagem.getId() + "/imagem")
                .collect(Collectors.toList());
    }
}
