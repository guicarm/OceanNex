package br.com.oceanex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.oceanex.model.FeedBackPostagem;

public interface FeedBackPostagemRepository extends JpaRepository<FeedBackPostagem, Long>{
     Page<FeedBackPostagem>findByUsuarioNomeIgnoreCase(String usuarioNome, Pageable pageable);
}
