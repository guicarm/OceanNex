package br.com.oceanex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.oceanex.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
