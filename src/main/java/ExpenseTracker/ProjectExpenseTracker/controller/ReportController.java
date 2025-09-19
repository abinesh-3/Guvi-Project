package ExpenseTracker.ProjectExpenseTracker.controller;

import ExpenseTracker.ProjectExpenseTracker.dto.ExpenseFilter;
import ExpenseTracker.ProjectExpenseTracker.model.User;
import ExpenseTracker.ProjectExpenseTracker.repository.UserRepository;
import ExpenseTracker.ProjectExpenseTracker.service.ExpenseService;
import ExpenseTracker.ProjectExpenseTracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller @RequiredArgsConstructor
public class ReportController {
private final ExpenseService expenses;
private final ReportService reports;
private final UserRepository users;


@GetMapping("/reports/pdf")
public ResponseEntity<byte[]> pdf(@AuthenticationPrincipal UserDetails me, ExpenseFilter f) {
User u = users.findByEmail(me.getUsername()).orElseThrow();
byte[] body = reports.toPdf(expenses.list(u,f));
return ResponseEntity.ok()
.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=smartspend.pdf")
.contentType(MediaType.APPLICATION_PDF)
.body(body);
}


@GetMapping("/reports/excel")
public ResponseEntity<byte[]> excel(@AuthenticationPrincipal UserDetails me, ExpenseFilter f) {
User u = users.findByEmail(me.getUsername()).orElseThrow();
byte[] body = reports.toExcel(expenses.list(u,f));
return ResponseEntity.ok()
.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=smartspend.xlsx")
.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
.body(body);
}
}