package ExpenseTracker.ProjectExpenseTracker.dto;

import ExpenseTracker.ProjectExpenseTracker.model.ExpenseType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class ExpenseFilter {
private String category; 
private ExpenseType type; 
private BigDecimal minAmount; 
private BigDecimal maxAmount; 
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
private LocalDate from;
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
private LocalDate to;
}