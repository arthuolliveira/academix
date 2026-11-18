package br.ucsal.academix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ucsal.academix.model.Disciplina;
import br.ucsal.academix.model.Inscricao;
import br.ucsal.academix.model.Usuario;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByDisciplina(Disciplina disciplina);

    List<Inscricao> findByAluno(Usuario aluno);
}
