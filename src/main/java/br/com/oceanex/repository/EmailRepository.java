package br.com.oceanex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.oceanex.model.Email;

public interface EmailRepository extends JpaRepository<Email, Long>{
    
}
