package ExpenseTracker.ProjectExpenseTracker.controller;

import ExpenseTracker.ProjectExpenseTracker.dto.RegisterRequest;
import ExpenseTracker.ProjectExpenseTracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller @RequiredArgsConstructor
public class AuthController {
private final UserService users;


@GetMapping("/")
public String login() { return "login"; }


@GetMapping("/register")
public String registerForm(Model m) { m.addAttribute("req", new RegisterRequest()); return "register"; }


@PostMapping("/register")
public String register(@Valid @ModelAttribute("req") RegisterRequest req, HttpServletRequest http, Model m) {
String appUrl = http.getRequestURL().toString().replace(http.getRequestURI(), http.getContextPath());
users.register(req, appUrl);
m.addAttribute("msg", "Registration successful. Check email to verify.");
return "login";
}


@GetMapping("/verify")
public String verify(@RequestParam String token, Model m) {
boolean ok = users.verify(token);
m.addAttribute("msg", ok?"Verified! Please login":"Invalid or expired token");
return "login";
}


@GetMapping("/reset")
public String resetForm() { return "reset-password"; }


@PostMapping("/reset")
public String reset(@RequestParam String email, HttpServletRequest http, Model m) {
String appUrl = http.getRequestURL().toString().replace(http.getRequestURI(), http.getContextPath());
users.initiateReset(email, appUrl);
m.addAttribute("msg", "If the email exists, a reset link was sent.");
return "login";
}


@GetMapping("/reset/confirm")
public String confirmPage(@RequestParam String token, Model m) { m.addAttribute("token", token); return "reset-password"; }


@PostMapping("/reset/confirm")
public String confirm(@RequestParam String token, @RequestParam String password, Model m) {
boolean ok = users.resetPassword(token, password);
m.addAttribute("msg", ok?"Password updated. Login now":"Invalid or expired token");
return "login";
}
}