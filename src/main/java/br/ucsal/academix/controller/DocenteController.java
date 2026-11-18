package br.ucsal.academix.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ucsal.academix.model.Aula;
import br.ucsal.academix.model.Disciplina;
import br.ucsal.academix.model.Frequencia;
import br.ucsal.academix.model.Inscricao;
import br.ucsal.academix.model.Usuario;
import br.ucsal.academix.repository.AulaRepository;
import br.ucsal.academix.repository.DisciplinaRepository;
import br.ucsal.academix.repository.FrequenciaRepository;
import br.ucsal.academix.repository.InscricaoRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    @Autowired private DisciplinaRepository disciplinaRepository;
    @Autowired private InscricaoRepository inscricaoRepository;
    @Autowired private AulaRepository aulaRepository;
    @Autowired private FrequenciaRepository frequenciaRepository;

    private Usuario docenteLogado(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogado");
        if (u != null && u.getPerfil().equals("DOCENTE")) {
            return u;
        }
        return null;
    }

    private Disciplina buscarDisciplinaDoProfessor(Long disciplinaId, Usuario professor) {
        Optional<Disciplina> achada = disciplinaRepository.findById(disciplinaId);
        if (achada.isPresent() && achada.get().getProfessor().getId().equals(professor.getId())) {
            return achada.get();
        }
        return null;
    }


    @GetMapping("/painel")
    public String painel(HttpSession session, Model model) {
        Usuario docente = docenteLogado(session);
        if (docente == null) {
            return "redirect:/login";
        }
        model.addAttribute("disciplinas", disciplinaRepository.findByProfessor(docente));
        return "docente/painel";
    }

    // FUNCIONAL 1: Diário de Classe
    @GetMapping("/frequencia")
    public String telaFrequencia(@RequestParam(required = false) Long disciplinaId,
                                 HttpSession session, Model model) {

        Usuario docente = docenteLogado(session);
        if (docente == null) {
            return "redirect:/login";
        }

        model.addAttribute("disciplinas", disciplinaRepository.findByProfessor(docente));

        if (disciplinaId != null) {
            Disciplina disciplina = buscarDisciplinaDoProfessor(disciplinaId, docente);
            if (disciplina != null) {
                model.addAttribute("disciplina", disciplina);
                model.addAttribute("inscricoes", inscricaoRepository.findByDisciplina(disciplina));
                model.addAttribute("aulas", aulaRepository.findByDisciplinaOrderByDataDesc(disciplina));
                model.addAttribute("hoje", LocalDate.now());
            }
        }
        return "docente/frequencia";
    }

    @PostMapping("/frequencia")
    public String salvarFrequencia(@RequestParam Long disciplinaId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                   @RequestParam String conteudo,
                                   @RequestParam Map<String, String> parametros, // todos os campos do formulário
                                   HttpSession session,
                                   RedirectAttributes redirect) {

        Usuario docente = docenteLogado(session);
        if (docente == null) {
            return "redirect:/login";
        }

        Disciplina disciplina = buscarDisciplinaDoProfessor(disciplinaId, docente);
        if (disciplina == null) {
            return "redirect:/docente/frequencia";
        }

        String destino = "redirect:/docente/frequencia?disciplinaId=" + disciplinaId;

        if (conteudo.isBlank()) {
            redirect.addFlashAttribute("erro", "Informe o conteúdo ministrado na aula.");
            return destino;
        }

        Aula aula = new Aula();
        aula.setDisciplina(disciplina);
        aula.setData(data);
        aula.setConteudo(conteudo.trim());
        aulaRepository.save(aula);

        List<Inscricao> inscricoes = inscricaoRepository.findByDisciplina(disciplina);
        for (Inscricao inscricao : inscricoes) {
            String marcado = parametros.get("presenca_" + inscricao.getAluno().getId());

            Frequencia frequencia = new Frequencia();
            frequencia.setAula(aula);
            frequencia.setAluno(inscricao.getAluno());
            frequencia.setPresente(!"F".equals(marcado));
            frequenciaRepository.save(frequencia);
        }

        redirect.addFlashAttribute("mensagem", "Aula registrada com sucesso!");
        return destino;
    }


    @GetMapping("/notas")
    public String telaNotas(@RequestParam(required = false) Long disciplinaId,
                            HttpSession session, Model model) {

        Usuario docente = docenteLogado(session);
        if (docente == null) {
            return "redirect:/login";
        }

        model.addAttribute("disciplinas", disciplinaRepository.findByProfessor(docente));

        if (disciplinaId != null) {
            Disciplina disciplina = buscarDisciplinaDoProfessor(disciplinaId, docente);
            if (disciplina != null) {
                model.addAttribute("disciplina", disciplina);
                model.addAttribute("inscricoes", inscricaoRepository.findByDisciplina(disciplina));
            }
        }
        return "docente/notas";
    }

    @PostMapping("/notas")
    public String salvarNotas(@RequestParam Long disciplinaId,
                              @RequestParam Map<String, String> parametros,
                              HttpSession session,
                              RedirectAttributes redirect) {

        Usuario docente = docenteLogado(session);
        if (docente == null) {
            return "redirect:/login";
        }

        Disciplina disciplina = buscarDisciplinaDoProfessor(disciplinaId, docente);
        if (disciplina == null) {
            return "redirect:/docente/notas";
        }

        String destino = "redirect:/docente/notas?disciplinaId=" + disciplinaId;
        List<Inscricao> inscricoes = inscricaoRepository.findByDisciplina(disciplina);

        try {
            for (Inscricao inscricao : inscricoes) {
                Double nota1 = lerNota(parametros.get("nota1_" + inscricao.getId()));
                Double nota2 = lerNota(parametros.get("nota2_" + inscricao.getId()));

                inscricao.setNota1(nota1);
                inscricao.setNota2(nota2);

                if (nota1 != null && nota2 != null) {
                    inscricao.setMedia((nota1 + nota2) / 2);
                } else {
                    inscricao.setMedia(null);
                }
            }
        } catch (IllegalArgumentException erro) {
 
            redirect.addFlashAttribute("erro", erro.getMessage());
            return destino;
        }

        inscricaoRepository.saveAll(inscricoes);

        redirect.addFlashAttribute("mensagem", "Notas e médias salvas com sucesso!");
        return destino;
    }

    private Double lerNota(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }

        double valor;
        try {
            valor = Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Nota inválida: \"" + texto + "\" não é um número.");
        }

        if (Double.isNaN(valor) || valor < 0.0 || valor > 10.0) {
            throw new IllegalArgumentException(
                    "Nota fora da escala permitida (0.0 a 10.0): " + texto);
        }
        return valor;
    }


    @GetMapping("/materiais")
    public String materiais(HttpSession session) {
        return docenteLogado(session) == null ? "redirect:/login" : "docente/materiais";
    }

    @GetMapping("/correcao")
    public String correcao(HttpSession session) {
        return docenteLogado(session) == null ? "redirect:/login" : "docente/correcao";
    }
}
