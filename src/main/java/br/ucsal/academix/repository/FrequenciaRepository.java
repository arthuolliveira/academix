package br.ucsal.academix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ucsal.academix.model.Disciplina;
import br.ucsal.academix.model.Frequencia;
import br.ucsal.academix.model.Usuario;

public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {

    long countByAlunoAndAula_DisciplinaAndPresenteFalse(Usuario aluno, Disciplina disciplina);
}
