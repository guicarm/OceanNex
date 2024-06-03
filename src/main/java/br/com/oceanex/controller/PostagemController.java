package br.com.oceanex.controller;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.oceanex.model.Postagem;
import br.com.oceanex.repository.PostagemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("postagem")
@Slf4j
@Tag(name = "Postagens")
public class PostagemController {

    @Autowired // Injeção de Dependência
    PostagemRepository repository;


    // ========== GET(Listar Postagens) ============
    @GetMapping
    @Operation(
        summary = "Listar postagens",
        description = "Retorna um array com todas postagens registradas."
    )
    public List<Postagem> index(){
        return repository.findAll();
    }
 
 
    // ========== POST(Cadastrar Postagem) ============
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar postagens",
        description = "Cadastra uma postagem especificada através de seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Postagem criada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique o corpo da requisição.")
    })
    public Postagem create(@RequestParam("titulo") String titulo,
                            @RequestParam("textoPostagem") String textoPostagem,
                            @RequestParam("bibliografia") String bibliografia,
                            @RequestParam("imagem") MultipartFile imagem) throws IOException {
            Postagem postagem = Postagem.builder()
                    .titulo(titulo)
                    .textoPostagem(textoPostagem)
                    .bibliografia(bibliografia)
                    .imagem(imagem.getBytes())
                    .build();
            log.info("Postagem Cadastrada {}", postagem);
            return repository.save(postagem);
        }

    //public Postagem create(@RequestBody @Valid Postagem Postagem){
    //    log.info("Postagem Cadastrada {}", Postagem);
    //    return repository.save(Postagem);
    //}
 
    // ========== GET(Detalhar Postagem) ============
    @GetMapping("{id}")
    @Operation(
        summary = "Detalhar postagens",
        description = "Detalha uma postagem especificada através de seu ID."
    )
    public ResponseEntity<Postagem> show(@PathVariable Long id){
        log.info("buscando postagem com id {}", id);
 
            return repository
                            .findById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }
 

    // ========== DELETE (Excluir Postagem) ============
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Excluir postagens",
        description = "Exclui uma postagem especificada através de seu ID."
    )
    public void destroy(@PathVariable Long id){
        log.info("Postagem apagada {}.", id);
 
        verificarSePostagemExiste(id);
        repository.deleteById(id);
                   
    }
 
 
    // ========== PUT (Atualizar Postagem) ============
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar postagens",
        description = "Atualiza uma postagem especificada através de seu ID."
    )
    public Postagem update(@PathVariable Long id, @RequestBody Postagem Postagem){
        log.info("Atualizando Postagem {} para {}", id, Postagem);
 
        verificarSePostagemExiste(id);
        Postagem.setId(id);
        return repository.save(Postagem);
 
    }
 


  // ==== MÉTODO VERIFICAR SE POSTAGEM EXISTE ========
 private void verificarSePostagemExiste(Long id) {
                repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                            NOT_FOUND,
                                            "Não existe postagem com o ID informado.")
                            );
    }
    

    // ========== GET(Obter Imagem) ============
    @GetMapping("{id}/imagem")
    @Operation(
        summary = "Obter imagem da postagem",
        description = "Retorna a imagem da postagem especificada através de seu ID."
    )
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        log.info("buscando imagem da postagem com id {}", id);
        
        return repository
                .findById(id)
                .map(postagem -> {
                    byte[] imagem = postagem.getImagem();
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
                .map(postagem -> "http://localhost:8080/postagem/" + postagem.getId() + "/imagem")
                .collect(Collectors.toList());
    }
}
