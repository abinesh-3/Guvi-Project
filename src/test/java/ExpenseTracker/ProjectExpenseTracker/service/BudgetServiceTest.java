package ExpenseTracker.ProjectExpenseTracker.service;

import ExpenseTracker.ProjectExpenseTracker.model.Budget;
import ExpenseTracker.ProjectExpenseTracker.model.User;
import ExpenseTracker.ProjectExpenseTracker.repository.BudgetRepository;
import ExpenseTracker.ProjectExpenseTracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BudgetServiceTest {
    @Mock
    private BudgetRepository budgets;

    @Mock
    private ExpenseRepository expenses;

    @Mock
    private SimpMessagingTemplate broker;

    @InjectMocks
    private BudgetService budgetService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
    }

    @Test
    void testList() {
        Budget b1 = new Budget();
        Budget b2 = new Budget();
        when(budgets.findByUser(user)).thenReturn(Arrays.asList(b1, b2));
        var list = budgetService.list(user);
        assertEquals(2, list.size());
        verify(budgets).findByUser(user);
    }

    @Test
    void testSave() {
        Budget b = new Budget();
        when(budgets.save(b)).thenReturn(b);
        Budget r = budgetService.save(b);
        assertSame(b, r);
        verify(budgets).save(b);
    }

    @Test
    void testDelete() {
        Long id = 5L;
        budgetService.delete(id);
        verify(budgets).deleteById(id);
    }

    @Test
    void testCheckAndNotify_noBudget() {
        when(budgets.findByUserAndCategory(eq(user), eq("Food"))).thenReturn(Optional.empty());
        budgetService.checkAndNotify(user, "Food");
        verify(budgets).findByUserAndCategory(eq(user), eq("Food"));
        verifyNoInteractions(expenses);
        verifyNoInteractions(broker);
    }

    @Test
    void testCheckAndNotify_under80Percent_noNotification() {
        Budget b = new Budget();
        b.setMonthlyLimit(new BigDecimal("1000"));
        when(budgets.findByUserAndCategory(eq(user), eq("Food"))).thenReturn(Optional.of(b));
        when(expenses.spentInMonth(eq(user), eq("Food"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new BigDecimal("100"));

        budgetService.checkAndNotify(user, "Food");

        verify(budgets).findByUserAndCategory(eq(user), eq("Food"));
        verify(expenses).spentInMonth(eq(user), eq("Food"), any(LocalDate.class), any(LocalDate.class));
        verifyNoInteractions(broker);
    }

    @Test
    void testCheckAndNotify_nearing80Percent_sendsNearingMessage() {
        Budget b = new Budget();
        b.setMonthlyLimit(new BigDecimal("1000"));
        when(budgets.findByUserAndCategory(eq(user), eq("Food"))).thenReturn(Optional.of(b));
        when(expenses.spentInMonth(eq(user), eq("Food"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new BigDecimal("900"));

        budgetService.checkAndNotify(user, "Food");

        verify(broker).convertAndSendToUser(eq(user.getEmail()), eq("/topic/alerts"), contains("Nearing budget"));
    }

    @Test
    void testCheckAndNotify_exceeded_sendsExceededMessage() {
        Budget b = new Budget();
        b.setMonthlyLimit(new BigDecimal("1000"));
        when(budgets.findByUserAndCategory(eq(user), eq("Food"))).thenReturn(Optional.of(b));
        when(expenses.spentInMonth(eq(user), eq("Food"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new BigDecimal("1200"));

        budgetService.checkAndNotify(user, "Food");

        verify(broker).convertAndSendToUser(eq(user.getEmail()), eq("/topic/alerts"), contains("Budget exceeded"));
    }
}
