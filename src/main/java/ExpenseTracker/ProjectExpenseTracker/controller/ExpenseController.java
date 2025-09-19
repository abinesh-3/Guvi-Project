package ExpenseTracker.ProjectExpenseTracker.controller;

import ExpenseTracker.ProjectExpenseTracker.dto.ExpenseFilter;
import ExpenseTracker.ProjectExpenseTracker.model.Expense;
import ExpenseTracker.ProjectExpenseTracker.model.ExpenseType;
import ExpenseTracker.ProjectExpenseTracker.model.User;
import ExpenseTracker.ProjectExpenseTracker.repository.UserRepository;
import ExpenseTracker.ProjectExpenseTracker.service.BudgetService;
import ExpenseTracker.ProjectExpenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller @RequiredArgsConstructor
public class ExpenseController {
private final ExpenseService expenses;
private final BudgetService budgets;
private final UserRepository users;


@GetMapping({"/","/dashboard"})
public String dashboard(Model m) { m.addAttribute("today", LocalDate.now()); return "dashboard"; }


@GetMapping("/expenses")
public String list(@AuthenticationPrincipal UserDetails me, ExpenseFilter filter, Model m) {
User user = users.findByEmail(me.getUsername()).orElseThrow();
List<Expense> list = expenses.list(user, filter);
m.addAttribute("items", list);
m.addAttribute("filter", filter);
m.addAttribute("types", ExpenseType.values());
return "expenses";
}


@PostMapping("/expenses")
public String create(@AuthenticationPrincipal UserDetails me, @Valid Expense e) {
User user = users.findByEmail(me.getUsername()).orElseThrow();
e.setUser(user); if (e.getDate()==null) e.setDate(LocalDate.now());
expenses.save(e);
budgets.checkAndNotify(user, e.getCategory());
return "redirect:/expenses";
}


@PostMapping("/expenses/{id}/delete")
public String delete(@PathVariable Long id) {
expenses.delete(id);
return "redirect:/expenses";
}
}