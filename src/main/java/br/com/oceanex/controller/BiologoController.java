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
    
    @Autowired // Injeção de Dependência
    BiologoRepository repository;


    // ========== GET(Listar Biólogos) ============
    @GetMapping
    @Operation(
        summary = "Listar biólogos",
        description = "Retorna um array com todos biólogos registrados."
    )
    public List<Biologo> index(){
        return repository.findAll();
    }
 
 
    // ========== POST(Cadastrar Biólogo) ============
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar biólogos",
        description = "Cadastra um biólogo especificado através de seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Biólogo criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique o corpo da requisição.")
    })
    public Biologo create(@RequestBody @Valid Biologo biologo){
        log.info("Biólogo Cadastrado {}", biologo);
        return repository.save(biologo);
    }
 
 
    // ========== GET(Detalhar Biológo) ============
    @GetMapping("{id}")
    @Operation(
        summary = "Detalhar biólogos",
        description = "Detalha um biólogo especificado através de seu ID."
    )
    public ResponseEntity<Biologo> show(@PathVariable Long id){
        log.info("buscando biólogo com id {}", id);
 
            return repository
                            .findById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }
 

    // ========== DELETE (Excluir Biologo) ============
    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Excluir biólogos",
        description = "Exclui um biólogo especificado através de seu ID."
    )
    public void destroy(@PathVariable Long id){
        log.info("Biólogo apagado {}.", id);
 
        verificarSeBiologoExiste(id);
        repository.deleteById(id);
                   
    }
 
 
    // ========== PUT (Atualizar Biologo) ============
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar biólogos",
        description = "Atualiza um biólogo especificado através de seu ID."
    )
    public Biologo update(@PathVariable Long id, @RequestBody Biologo biologo){
        log.info("Atualizando biólogo {} para {}", id, biologo);
 
        verificarSeBiologoExiste(id);
        biologo.setId(id);
        return repository.save(biologo);
 
    }
 


  // ==== MÉTODO VERIFICAR SE BIÓLOGO EXISTE ========
 private void verificarSeBiologoExiste(Long id) {
                repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                            NOT_FOUND,
                                            "Não existe biólogo com o ID informado.")
                            );
    }
}
