package br.com.oceanex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.oceanex.model.FeedBackPostagem;

public interface FeedBackPostagemRepository extends JpaRepository<FeedBackPostagem, Long>{
    
}
