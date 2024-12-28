package com.example.TiendaInventario.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Devuelve la vista de inicio de sesión
    }

    @GetMapping("/")
    public String home() {
        return "home"; // Devuelve la vista principal
    }
}
