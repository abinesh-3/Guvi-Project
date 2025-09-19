package ExpenseTracker.ProjectExpenseTracker.service;

import ExpenseTracker.ProjectExpenseTracker.dto.*;
import ExpenseTracker.ProjectExpenseTracker.model.*;
import ExpenseTracker.ProjectExpenseTracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service @RequiredArgsConstructor
public class ExpenseService {
private final ExpenseRepository repo;


public List<Expense> list(User user, ExpenseFilter f) {
if (f == null) return repo.findByUser(user);
return repo.filter(user, f.getCategory(), f.getType(), f.getMinAmount(), f.getMaxAmount(), f.getFrom(), f.getTo());
}


public Expense save(Expense e) { return repo.save(e); }
public void delete(Long id) { repo.deleteById(id); }
}
