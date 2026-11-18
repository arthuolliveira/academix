package br.ucsal.academix.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ucsal.academix.model.Disciplina;
import br.ucsal.academix.model.Inscricao;
import br.ucsal.academix.model.LinhaBoletim;
import br.ucsal.academix.model.Usuario;
import br.ucsal.academix.repository.AulaRepository;
import br.ucsal.academix.repository.FrequenciaRepository;
import br.ucsal.academix.repository.InscricaoRepository;
import jakarta.servlet.http.HttpSession;

 // Telas do perfil DISCENTE.
@Controller
@RequestMapping("/discente")
public class DiscenteController {

    @Autowired private InscricaoRepository inscricaoRepository;
    @Autowired private AulaRepository aulaRepository;
    @Autowired private FrequenciaRepository frequenciaRepository;

    private Usuario discenteLogado(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogado");
        if (u != null && u.getPerfil().equals("DISCENTE")) {
            return u;
        }
        return null;
    }

    @GetMapping("/painel")
    public String painel(HttpSession session) {
        if (discenteLogado(session) == null) {
            return "redirect:/login";
        }
        return "discente/painel";
    }

    // FUNCIONAL 3: Boletim 
    @GetMapping("/boletim")
    public String boletim(HttpSession session, Model model) {

        Usuario aluno = discenteLogado(session);
        if (aluno == null) {
            return "redirect:/login";
        }

        List<Inscricao> inscricoes = inscricaoRepository.findByAluno(aluno);

        List<LinhaBoletim> linhas = new ArrayList<>();

        for (Inscricao inscricao : inscricoes) {
            Disciplina disciplina = inscricao.getDisciplina();

            long totalAulas = aulaRepository.countByDisciplina(disciplina);
            long faltas = frequenciaRepository
                    .countByAlunoAndAula_DisciplinaAndPresenteFalse(aluno, disciplina);

            Double percentualFaltas = null;
            Double percentualPresenca = null;

            if (totalAulas > 0) {
                percentualFaltas = 100.0 * faltas / totalAulas;
                percentualPresenca = 100.0 - percentualFaltas;
            }

            linhas.add(new LinhaBoletim(
                    disciplina.getNome(),
                    inscricao.getNota1(),
                    inscricao.getNota2(),
                    inscricao.getMedia(),
                    totalAulas,
                    faltas,
                    percentualFaltas,
                    percentualPresenca));
        }

        model.addAttribute("linhas", linhas);
        return "discente/boletim";
    }


    @GetMapping("/trabalhos")
    public String trabalhos(HttpSession session) {
        return discenteLogado(session) == null ? "redirect:/login" : "discente/trabalhos";
    }

    @GetMapping("/materiais")
    public String materiais(HttpSession session) {
        return discenteLogado(session) == null ? "redirect:/login" : "discente/materiais";
    }

    @GetMapping("/atestado")
    public String atestado(HttpSession session) {
        return discenteLogado(session) == null ? "redirect:/login" : "discente/atestado";
    }

    @GetMapping("/grade")
    public String grade(HttpSession session) {
        return discenteLogado(session) == null ? "redirect:/login" : "discente/grade";
    }

    @GetMapping("/historico")
    public String historico(HttpSession session) {
        return discenteLogado(session) == null ? "redirect:/login" : "discente/historico";
    }

    @GetMapping("/denuncia")
    public String denuncia(HttpSession session) {
        return discenteLogado(session) == null ? "redirect:/login" : "discente/denuncia";
    }
}
