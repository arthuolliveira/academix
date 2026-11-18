package br.ucsal.academix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ucsal.academix.model.Aula;
import br.ucsal.academix.model.Disciplina;

public interface AulaRepository extends JpaRepository<Aula, Long> {

    List<Aula> findByDisciplinaOrderByDataDesc(Disciplina disciplina);

    long countByDisciplina(Disciplina disciplina);
}
