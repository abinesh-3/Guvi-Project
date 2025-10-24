package ExpenseTracker.ProjectExpenseTracker.controller;

import ExpenseTracker.ProjectExpenseTracker.dto.ExpenseFilter;
import ExpenseTracker.ProjectExpenseTracker.model.User;
import ExpenseTracker.ProjectExpenseTracker.repository.UserRepository;
import ExpenseTracker.ProjectExpenseTracker.service.ExpenseService;
import ExpenseTracker.ProjectExpenseTracker.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportControllerTest {
    @Mock
    private ExpenseService expenses;
    @Mock
    private ReportService reports;
    @Mock
    private UserRepository users;
    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPdf() {
        ExpenseFilter filter = new ExpenseFilter();
        User user = new User();
        byte[] pdfBytes = new byte[]{1,2,3};
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(users.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(expenses.list(user, filter)).thenReturn(null);
        ResponseEntity<byte[]> response = reportController.pdf(userDetails, filter);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(pdfBytes, response.getBody());
        assertTrue(response.getHeaders().get("Content-Disposition").get(0).contains("smartspend.pdf"));
        assertTrue(response.getHeaders().get("Content-Disposition").get(0).contains("smartspend.pdf"));
    }

    @Test
    void testExcel() {
        ExpenseFilter filter = new ExpenseFilter();
        User user = new User();
        byte[] excelBytes = new byte[]{4,5,6};
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(users.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(expenses.list(user, filter)).thenReturn(null);
        ResponseEntity<byte[]> response = reportController.excel(userDetails, filter);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(excelBytes, response.getBody());
        assertTrue(response.getHeaders().get("Content-Disposition").get(0).contains("smartspend.xlsx"));
        assertTrue(response.getHeaders().get("Content-Disposition").get(0).contains("smartspend.xlsx"));
    }
}
