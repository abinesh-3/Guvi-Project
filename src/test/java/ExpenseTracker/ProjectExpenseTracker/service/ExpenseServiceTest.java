package ExpenseTracker.ProjectExpenseTracker.service;

import ExpenseTracker.ProjectExpenseTracker.dto.ExpenseFilter;
import ExpenseTracker.ProjectExpenseTracker.model.Expense;
import ExpenseTracker.ProjectExpenseTracker.model.User;
import ExpenseTracker.ProjectExpenseTracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository repo;
    @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testList() {
        User user = new User();
        ExpenseFilter filter = new ExpenseFilter();
        List<Expense> result = expenseService.list(user, filter);
        assertEquals(2, result.size());
        
    }

    @Test
    void testSave() {
        Expense expense = new Expense();
        when(repo.save(expense)).thenReturn(expense);
        Expense result = expenseService.save(expense);
        assertEquals(expense, result);
        verify(repo).save(expense);
    }

    @Test
    void testDelete() {
        Long id = 1L;
        expenseService.delete(id);
        verify(repo).deleteById(id);
    }
}
