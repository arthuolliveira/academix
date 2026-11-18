package br.ucsal.academix.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ucsal.academix.model.Usuario;
import br.ucsal.academix.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String telaLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String entrar(@RequestParam String identificacao,
                         @RequestParam String senha,
                         HttpSession session,
                         Model model) {

        Optional<Usuario> encontrado =
                usuarioRepository.findByMatriculaOrCpf(identificacao.trim(), identificacao.trim());

        if (encontrado.isPresent() && encontrado.get().getSenha().equals(senha)) {

            Usuario usuario = encontrado.get();

            session.setAttribute("usuarioLogado", usuario);

            if (usuario.getPerfil().equals("DISCENTE")) {
                return "redirect:/discente/painel";
            }
            if (usuario.getPerfil().equals("DOCENTE")) {
                return "redirect:/docente/painel";
            }
            return "redirect:/gestor/painel"; 
        }

        model.addAttribute("erro", "Credenciais inválidas");
        return "login";
    }

    @GetMapping("/logout")
    public String sair(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }



    @GetMapping("/publico/atestado")
    public String validarAtestado() {
        return "publico/atestado";
    }

    @GetMapping("/publico/denuncia")
    public String denunciaAnonima() {
        return "publico/denuncia";
    }
}
