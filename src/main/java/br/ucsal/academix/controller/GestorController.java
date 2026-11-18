package br.ucsal.academix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ucsal.academix.model.Usuario;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/gestor")
public class GestorController {

    private boolean gestorLogado(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogado");
        return u != null && u.getPerfil().equals("GESTOR");
    }

    @GetMapping("/painel")
    public String painel(HttpSession session) {
        return gestorLogado(session) ? "gestor/painel" : "redirect:/login";
    }

    @GetMapping("/discentes")
    public String discentes(HttpSession session) {
        return gestorLogado(session) ? "gestor/discentes" : "redirect:/login";
    }

    @GetMapping("/alocacao")
    public String alocacao(HttpSession session) {
        return gestorLogado(session) ? "gestor/alocacao" : "redirect:/login";
    }

    @GetMapping("/relatorios")
    public String relatorios(HttpSession session) {
        return gestorLogado(session) ? "gestor/relatorios" : "redirect:/login";
    }
}
