package ExpenseTracker.ProjectExpenseTracker.controller;

import ExpenseTracker.ProjectExpenseTracker.model.Budget;
import ExpenseTracker.ProjectExpenseTracker.model.User;
import ExpenseTracker.ProjectExpenseTracker.repository.UserRepository;
import ExpenseTracker.ProjectExpenseTracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller @RequiredArgsConstructor
public class BudgetController {
private final BudgetService budgets;
private final UserRepository users;

@GetMapping("/budgets")
public String list(@AuthenticationPrincipal UserDetails me, Model m) {
User user = users.findByEmail(me.getUsername()).orElseThrow();
m.addAttribute("items", budgets.list(user));
m.addAttribute("item", new Budget());
return "budgets";
}


@PostMapping("/budgets")
public String save(@AuthenticationPrincipal UserDetails me, @Valid Budget b) {
b.setUser(users.findByEmail(me.getUsername()).orElseThrow());
budgets.save(b);
return "redirect:/budgets";
}


@PostMapping("/budgets/{id}/delete")
public String del(@PathVariable Long id) { budgets.delete(id); return "redirect:/budgets"; }
}


