package ExpenseTracker.ProjectExpenseTracker.controller;

import ExpenseTracker.ProjectExpenseTracker.model.Expense;
import ExpenseTracker.ProjectExpenseTracker.repository.ExpenseRepository;
import ExpenseTracker.ProjectExpenseTracker.dto.ExpenseFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseControllerTest {
    @Mock
    private ExpenseRepository expenses;
    @Mock
    private Model model;
    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDashboard() {
        String view = expenseController.dashboard(model);
        assertEquals("dashboard", view);
        verify(model).addAttribute(eq("today"), any(LocalDate.class));
    }

    @Test
    void testList() {
        ExpenseFilter filter = new ExpenseFilter();
        String view = expenseController.list(userDetails, filter, model);
        assertEquals("expenses", view);
    }

    @Test
    void testCreate() {
        Expense expense = new Expense();
        String view = expenseController.create(userDetails, expense);
        assertEquals("redirect:/expenses", view);
    }

    @Test
    void testDelete() {
        Long id = 1L;
        String view = expenseController.delete(id);
        assertEquals("redirect:/expenses", view);
        verify(expenses).deleteById(id);
    }
}
