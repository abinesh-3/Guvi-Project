package ExpenseTracker.ProjectExpenseTracker.controller;

import ExpenseTracker.ProjectExpenseTracker.model.Budget;
import ExpenseTracker.ProjectExpenseTracker.model.User;
import ExpenseTracker.ProjectExpenseTracker.repository.UserRepository;
import ExpenseTracker.ProjectExpenseTracker.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BudgetControllerTest {
    @Mock
    private BudgetService budgetService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private Model model;
    
    @Mock
    private UserDetails userDetails;
    
    @InjectMocks
    private BudgetController budgetController;

    private User testUser;
    private Budget testBudget;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        
        
        testBudget = new Budget();
        testBudget.setId(1L);
        testBudget.setAmount(BigDecimal.valueOf(1000));
        testBudget.setCategory("Food");
        testBudget.setUser(testUser);
        

        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
    }

    @Test
    void testList() {
        
        when(budgetService.list(testUser)).thenReturn(Arrays.asList(testBudget));
        
        
        String viewName = budgetController.list(userDetails, model);
        
       
        assertEquals("budgets", viewName);
        verify(model).addAttribute("items", Arrays.asList(testBudget));
        verify(model).addAttribute(eq("item"), any(Budget.class));
        verify(userRepository).findByEmail("test@example.com");
        verify(budgetService).list(testUser);
    }

    @Test
    void testSave() {
        
        Budget newBudget = new Budget();
        newBudget.setAmount(BigDecimal.valueOf(500));
        newBudget.setCategory("Entertainment");
        
        when(budgetService.save(newBudget)).thenReturn(newBudget);
        
       
        String viewName = budgetController.save(userDetails, newBudget);
        
       
        assertEquals("redirect:/budgets", viewName);
        verify(userRepository).findByEmail("test@example.com");
        verify(budgetService).save(newBudget);
        assertEquals(testUser, newBudget.getUser());
    }

    @Test
    void testDelete() {
        
        String viewName = budgetController.del(1L);
        
       
        assertEquals("redirect:/budgets", viewName);
        verify(budgetService).delete(1L);
    }

    @Test
    void testListWithUserNotFound() {
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        
        
        assertThrows(RuntimeException.class, () -> {
            budgetController.list(userDetails, model);
        });
    }

    @Test
    void testSaveWithUserNotFound() {
      
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        Budget newBudget = new Budget();

        assertThrows(RuntimeException.class, () -> {
            budgetController.save(userDetails, newBudget);
        });
    }
}
