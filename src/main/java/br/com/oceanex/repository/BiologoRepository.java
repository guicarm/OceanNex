package br.com.oceanex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.oceanex.model.Biologo;

public interface BiologoRepository extends JpaRepository<Biologo, Long> {
    
}
