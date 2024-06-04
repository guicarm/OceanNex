package br.com.oceanex.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.oceanex.model.Biologo;
import br.com.oceanex.model.Email;
import br.com.oceanex.model.FeedBackImagem;
import br.com.oceanex.model.FeedBackPostagem;
import br.com.oceanex.model.Postagem;
import br.com.oceanex.model.PredicaoImagem;
import br.com.oceanex.model.Usuario;
import br.com.oceanex.repository.BiologoRepository;
import br.com.oceanex.repository.EmailRepository;
import br.com.oceanex.repository.FeedBackImagemRepository;
import br.com.oceanex.repository.FeedBackPostagemRepository;
import br.com.oceanex.repository.PostagemRepository;
import br.com.oceanex.repository.PredicaoImagemRepository;
import br.com.oceanex.repository.UsuarioRepository;

@Configuration
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    BiologoRepository biologoRepository;
    
    @Autowired
    EmailRepository emailRepository;
    
    @Autowired
    FeedBackImagemRepository feedBackImagemRepository;
    
    @Autowired
    FeedBackPostagemRepository feedBackPostagemRepository;
    
    @Autowired
    PostagemRepository postagemRepository;
    
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
    

        biologoRepository.saveAll(
            List.of(
                Biologo.builder()
                .id(1L)
                .nome("Guilherme Carneiro")
                .numeroRegistro("123456")
                .cpf("111.111.111-11")
                .crbio("CRBio-01")
                .build(),
                
                Biologo.builder()
                .id(2L)
                .nome("Amorgan Mendes")
                .numeroRegistro("123456789000")
                .cpf("222.222.222-22")
                .crbio("CRBio-02")
                .build()
            )
        );

        
        emailRepository.saveAll(
            List.of(
                Email.builder()
                .id(1L)
                .nomeEmail("guilherme@gmail.com")
                .statusEmail("A")
                .build(),
                
                Email.builder()
                .id(2L)
                .nomeEmail("amorgan@gmail.com")
                .statusEmail("I")
                .build()
            )
        );

        
        feedBackImagemRepository.saveAll(
            List.of(
                FeedBackImagem.builder()
                .id(1L)
                .statusFeedback("like")
                .descricaoFeedback("Ótima imagem!")
                .build(),
                
                FeedBackImagem.builder()
                .id(2L)
                .statusFeedback("deslike")
                .descricaoFeedback("Não gostei muito...")
                .build()
            )
        );

        
        feedBackPostagemRepository.saveAll(
            List.of(
                FeedBackPostagem.builder()
                .id(1L)
                .statusFeedback("like")
                .descricaoFeedback("Excelente postagem! Diz muito sobre minha pesquisa.")
                .build(),
                
                FeedBackPostagem.builder()
                .id(2L)
                .statusFeedback("deslike")
                .descricaoFeedback("Postagem fraca, acho que não soma muito pra o contexto de ajudar o oceano.")
                .build()
            )
        );

        
        postagemRepository.saveAll(
            List.of(
                Postagem.builder()
                .id(1L)
                .titulo("Importância das microalgas")
                .textoPostagem("blablablablabla...")
                .bibliografia("wikipedia.com.br/microalgas-azuis")
                .imagem(null)
                .build(),
                
                Postagem.builder()
                .id(2L)
                .titulo("Lixo nos oceanos")
                .textoPostagem("blablablablabla...")
                .bibliografia("usp.com.br/oceanos-sujos")
                .imagem(null)
                .build()
            )
        );


        
        usuarioRepository.saveAll(
            List.of(
                Usuario.builder()
                .id(1L)
                .nome("Matheus Camargo")
                .senha("vaicorinthia")
                .build(),
                
                Usuario.builder()
                .id(2L)
                .nome("Gustavo Godoi")
                .senha("avanatipalestra")
                .build()
            )
        );
    }
}
