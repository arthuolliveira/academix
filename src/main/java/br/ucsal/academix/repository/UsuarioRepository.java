package br.ucsal.academix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ucsal.academix.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByMatriculaOrCpf(String matricula, String cpf);
}
