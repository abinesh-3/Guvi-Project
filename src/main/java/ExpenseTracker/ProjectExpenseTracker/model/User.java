package ExpenseTracker.ProjectExpenseTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;


@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "users")
public class User {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@Email @Column(unique = true, nullable = false)
private String email;


@NotBlank
private String password;


@Enumerated(EnumType.STRING)
@Builder.Default
private Role role = Role.USER;


@Builder.Default
private boolean enabled = false;

private String verificationToken;
private String resetToken;
private LocalDateTime tokenExpiry;
}

