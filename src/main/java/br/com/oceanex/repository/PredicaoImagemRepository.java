package br.com.oceanex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.oceanex.model.PredicaoImagem;

public interface PredicaoImagemRepository extends JpaRepository<PredicaoImagem, Long>{
    
}
