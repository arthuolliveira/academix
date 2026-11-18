package br.ucsal.academix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ucsal.academix.model.Disciplina;
import br.ucsal.academix.model.Usuario;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findByProfessor(Usuario professor);
}
