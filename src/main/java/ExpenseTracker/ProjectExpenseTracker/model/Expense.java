package ExpenseTracker.ProjectExpenseTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(indexes = {
@Index(columnList = "user_id,date"),
@Index(columnList = "category"),
@Index(columnList = "type")
})
public class Expense {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@NotBlank
private String title;


@NotNull
private BigDecimal amount;


@NotBlank
private String category; // e.g., Food, Travel, Rent


@Enumerated(EnumType.STRING)
private ExpenseType type; // EXPENSE / INCOME


private LocalDate date;


private String description;


@ManyToOne(fetch = FetchType.LAZY)
private User user;
}