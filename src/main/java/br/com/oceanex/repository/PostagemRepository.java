package br.com.oceanex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.oceanex.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long>{
    Page<Postagem>findByBiologoNomeIgnoreCase(String biologoNome, Pageable pageable);
}
