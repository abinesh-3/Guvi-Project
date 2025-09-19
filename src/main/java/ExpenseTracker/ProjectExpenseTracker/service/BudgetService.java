package ExpenseTracker.ProjectExpenseTracker.service;

import ExpenseTracker.ProjectExpenseTracker.model.*;
import ExpenseTracker.ProjectExpenseTracker.repository.*;
import ExpenseTracker.ProjectExpenseTracker.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service @RequiredArgsConstructor
public class BudgetService {
private final BudgetRepository budgets;
private final ExpenseRepository expenses;
private final SimpMessagingTemplate broker;


public List<Budget> list(User user) { return budgets.findByUser(user); }
public Budget save(Budget b) { return budgets.save(b); }
public void delete(Long id) { budgets.deleteById(id); }


public void checkAndNotify(User user, String category) {
budgets.findByUserAndCategory(user, category).ifPresent(b -> {
LocalDate now = LocalDate.now();
BigDecimal spent = expenses.spentInMonth(user, category, DateUtils.startOfMonth(now), DateUtils.endOfMonth(now));
if (spent.compareTo(b.getMonthlyLimit()) >= 0) {
broker.convertAndSendToUser(user.getEmail(), "/topic/alerts", "Budget exceeded for "+category+". Spent: "+spent);
} else if (spent.compareTo(b.getMonthlyLimit().multiply(new BigDecimal("0.8"))) >= 0) {
broker.convertAndSendToUser(user.getEmail(), "/topic/alerts", "Nearing budget for "+category+". Spent: "+spent);
}
});
}
}