package br.ucsal.academix;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.ucsal.academix.model.Aula;
import br.ucsal.academix.model.Disciplina;
import br.ucsal.academix.model.Frequencia;
import br.ucsal.academix.model.Inscricao;
import br.ucsal.academix.model.Usuario;
import br.ucsal.academix.repository.AulaRepository;
import br.ucsal.academix.repository.DisciplinaRepository;
import br.ucsal.academix.repository.FrequenciaRepository;
import br.ucsal.academix.repository.InscricaoRepository;
import br.ucsal.academix.repository.UsuarioRepository;


@Component
public class CargaInicial implements CommandLineRunner {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private DisciplinaRepository disciplinaRepository;
    @Autowired private InscricaoRepository inscricaoRepository;
    @Autowired private AulaRepository aulaRepository;
    @Autowired private FrequenciaRepository frequenciaRepository;

    @Override
    public void run(String... args) {

        if (usuarioRepository.count() > 0) {
            return;
        }

        criarUsuario("Gestor Academix", "2026001", "11111111111", "GESTOR");
        Usuario professor = criarUsuario("Prof. Elton", "2026002", "22222222222", "DOCENTE");
        Usuario diogo = criarUsuario("Diogo", "2026003", "33333333333", "DISCENTE");
        Usuario mailson = criarUsuario("Mailson", "2026004", "44444444444", "DISCENTE");


        Disciplina requisitos = criarDisciplina("Engenharia de Requisitos", professor);
        Disciplina bancoDeDados = criarDisciplina("Banco de Dados", professor);


        matricular(diogo, requisitos, 8.0, 7.0);
        matricular(mailson, requisitos, 5.0, 6.0);
        matricular(diogo, bancoDeDados, 9.0, null);   
        matricular(mailson, bancoDeDados, null, null); 


        registrarAula(requisitos, LocalDate.of(2026, 3, 2), "Introdução à Engenharia de Requisitos",
                diogo, true, mailson, true);
        registrarAula(requisitos, LocalDate.of(2026, 3, 9), "Elicitação de requisitos",
                diogo, true, mailson, false);
        registrarAula(requisitos, LocalDate.of(2026, 3, 16), "Requisitos funcionais e não funcionais",
                diogo, false, mailson, false);
        registrarAula(requisitos, LocalDate.of(2026, 3, 23), "Regras de negócio",
                diogo, true, mailson, true);


        System.out.println(">>> Carga inicial concluída: dados de teste inseridos no PostgreSQL.");
    }


    private Usuario criarUsuario(String nome, String matricula, String cpf, String perfil) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setMatricula(matricula);
        u.setCpf(cpf);
        u.setSenha("123");
        u.setPerfil(perfil);
        return usuarioRepository.save(u); 
    }

    private Disciplina criarDisciplina(String nome, Usuario professor) {
        Disciplina d = new Disciplina();
        d.setNome(nome);
        d.setProfessor(professor);
        return disciplinaRepository.save(d);
    }

    private void matricular(Usuario aluno, Disciplina disciplina, Double nota1, Double nota2) {
        Inscricao i = new Inscricao();
        i.setAluno(aluno);
        i.setDisciplina(disciplina);
        i.setNota1(nota1);
        i.setNota2(nota2);
        if (nota1 != null && nota2 != null) {
            i.setMedia((nota1 + nota2) / 2);
        }
        inscricaoRepository.save(i);
    }

    private void registrarAula(Disciplina disciplina, LocalDate data, String conteudo,
                               Usuario aluno1, boolean presente1,
                               Usuario aluno2, boolean presente2) {
        Aula aula = new Aula();
        aula.setDisciplina(disciplina);
        aula.setData(data);
        aula.setConteudo(conteudo);
        aulaRepository.save(aula);

        salvarFrequencia(aula, aluno1, presente1);
        salvarFrequencia(aula, aluno2, presente2);
    }

    private void salvarFrequencia(Aula aula, Usuario aluno, boolean presente) {
        Frequencia f = new Frequencia();
        f.setAula(aula);
        f.setAluno(aluno);
        f.setPresente(presente);
        frequenciaRepository.save(f);
    }
}
