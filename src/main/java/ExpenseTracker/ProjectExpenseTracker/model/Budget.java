package ExpenseTracker.ProjectExpenseTracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;


@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","category"}))
public class Budget {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@ManyToOne(fetch = FetchType.LAZY)
private User user;


private String category;
private BigDecimal monthlyLimit; // per month
}