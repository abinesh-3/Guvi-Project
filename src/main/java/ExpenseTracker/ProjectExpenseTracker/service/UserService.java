package ExpenseTracker.ProjectExpenseTracker.service;

import ExpenseTracker.ProjectExpenseTracker.dto.*;
import ExpenseTracker.ProjectExpenseTracker.model.*;
import ExpenseTracker.ProjectExpenseTracker.repository.*;
import lombok.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service @RequiredArgsConstructor
public class UserService {
private final UserRepository users;
private final PasswordEncoder encoder;
private final MailService mail;


public User register(RegisterRequest req, String appUrl) {
User u = User.builder()
.email(req.getEmail())
.password(encoder.encode(req.getPassword()))
.role(Role.USER)
.enabled(true)// auto verified for deployment railway
.verificationToken(UUID.randomUUID().toString())
.tokenExpiry(LocalDateTime.now().plusHours(24))
.build();
users.save(u);
//emai 
String link = appUrl + "/verify?token=" + u.getVerificationToken();
mail.send(u.getEmail(), "Verify your SmartSpend account", "Click to verify: " + link);
return u;
}


public boolean verify(String token) {
Optional<User> opt = users.findByVerificationToken(token);
if (opt.isEmpty()) return false;
User u = opt.get();
if (u.getTokenExpiry()!=null && u.getTokenExpiry().isBefore(LocalDateTime.now())) return false;
u.setEnabled(true); u.setVerificationToken(null); u.setTokenExpiry(null);
users.save(u); return true;
}


public void initiateReset(String email, String appUrl) {
users.findByEmail(email).ifPresent(u -> {
u.setResetToken(UUID.randomUUID().toString());
u.setTokenExpiry(LocalDateTime.now().plusHours(2));
users.save(u);
// mail.send(email, "Reset your SmartSpend password", appUrl + "/reset/confirm?token="+u.getResetToken());
});
}


public boolean resetPassword(String token, String newPassword) {
Optional<User> opt = users.findByResetToken(token);
if (opt.isEmpty()) return false;
User u = opt.get();
if (u.getTokenExpiry()!=null && u.getTokenExpiry().isBefore(LocalDateTime.now())) return false;
u.setPassword(encoder.encode(newPassword));
u.setResetToken(null); u.setTokenExpiry(null);
users.save(u); return true;
}
}