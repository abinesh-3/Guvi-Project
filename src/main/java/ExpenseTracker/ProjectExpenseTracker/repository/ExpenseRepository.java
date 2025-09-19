package ExpenseTracker.ProjectExpenseTracker.repository;

import ExpenseTracker.ProjectExpenseTracker.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {
List<Expense> findByUser(User user);


@Query("SELECT e FROM Expense e WHERE e.user=:user " +
"AND (:category IS NULL OR e.category=:category) " +
"AND (:type IS NULL OR e.type=:type) " +
"AND (:min IS NULL OR e.amount >= :min) " +
"AND (:max IS NULL OR e.amount <= :max) " +
"AND (:from IS NULL OR e.date >= :from) " +
"AND (:to IS NULL OR e.date <= :to) " +
"ORDER BY e.date DESC")
List<Expense> filter(@Param("user") User user,
@Param("category") String category,
@Param("type") ExpenseType type,
@Param("min") BigDecimal min,
@Param("max") BigDecimal max,
@Param("from") LocalDate from,
@Param("to") LocalDate to);


@Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.user=:user AND e.category=:category AND e.type='EXPENSE' AND e.date BETWEEN :start AND :end")
BigDecimal spentInMonth(@Param("user") User user, @Param("category") String category, @Param("start") LocalDate start, @Param("end") LocalDate end);
}