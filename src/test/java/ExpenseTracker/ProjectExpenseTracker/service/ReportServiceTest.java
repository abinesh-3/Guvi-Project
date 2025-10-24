package ExpenseTracker.ProjectExpenseTracker.service;

import ExpenseTracker.ProjectExpenseTracker.model.Expense;
import ExpenseTracker.ProjectExpenseTracker.model.ExpenseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportService();
    }

    @Test
    void testToPdf() {
        Expense expense = new Expense();
        expense.setDate(LocalDate.now());
        expense.setTitle("Test Expense");
        expense.setCategory("Food");
        expense.setType(ExpenseType.EXPENSE);
        expense.setAmount(BigDecimal.valueOf(100));
        List<Expense> items = Arrays.asList(expense);
        byte[] pdf = reportService.toPdf(items);
        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void testToExcel() {
        Expense expense = new Expense();
        expense.setDate(LocalDate.now());
        expense.setTitle("Test Expense");
        expense.setCategory("Food");
        expense.setType(ExpenseType.EXPENSE);
        expense.setAmount(BigDecimal.valueOf(100));
        List<Expense> items = Arrays.asList(expense);
        byte[] excel = reportService.toExcel(items);
        assertNotNull(excel);
        assertTrue(excel.length > 0);
    }
}
