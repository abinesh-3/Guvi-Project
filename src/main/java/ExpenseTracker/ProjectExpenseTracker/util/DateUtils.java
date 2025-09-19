package ExpenseTracker.ProjectExpenseTracker.util;


import java.time.LocalDate;


public class DateUtils {
public static LocalDate startOfMonth(LocalDate d) { return d.withDayOfMonth(1); }
public static LocalDate endOfMonth(LocalDate d) { return d.withDayOfMonth(d.lengthOfMonth()); }
}