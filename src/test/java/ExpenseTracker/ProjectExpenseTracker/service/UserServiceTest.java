package ExpenseTracker.ProjectExpenseTracker.service;

import ExpenseTracker.ProjectExpenseTracker.dto.RegisterRequest;
import ExpenseTracker.ProjectExpenseTracker.model.User;
import ExpenseTracker.ProjectExpenseTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private MailService mailService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_savesUserWithEncodedPasswordAndToken() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("test@example.com");
        req.setPassword("plainpass");
        when(encoder.encode("plainpass")).thenReturn("encodedpass");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.register(req, "http://app");

        verify(encoder).encode("plainpass");
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("test@example.com", saved.getEmail());
        assertEquals("encodedpass", saved.getPassword());
        assertNotNull(saved.getVerificationToken());
        assertNotNull(saved.getTokenExpiry());
        assertEquals(saved, created);
    }

    @Test
    void verify_returnsFalseWhenTokenNotFound() {
        when(userRepository.findByVerificationToken("nope")).thenReturn(Optional.empty());
        assertFalse(userService.verify("nope"));
        verify(userRepository).findByVerificationToken("nope");
    }

    @Test
    void verify_returnsFalseWhenTokenExpired() {
        User u = new User();
        u.setTokenExpiry(LocalDateTime.now().minusHours(1));
        when(userRepository.findByVerificationToken("tok")).thenReturn(Optional.of(u));
        assertFalse(userService.verify("tok"));
        verify(userRepository).findByVerificationToken("tok");
        verify(userRepository, never()).save(any());
    }

    @Test
    void verify_enablesUserWhenValid() {
        User u = new User();
        u.setTokenExpiry(LocalDateTime.now().plusHours(1));
        u.setEnabled(false);
        when(userRepository.findByVerificationToken("tok")).thenReturn(Optional.of(u));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        assertTrue(userService.verify("tok"));

        assertTrue(u.isEnabled());
        assertNull(u.getVerificationToken());
        assertNull(u.getTokenExpiry());
        verify(userRepository).save(u);
    }

    @Test
    void initiateReset_noopWhenEmailNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        userService.initiateReset("missing@example.com", "http://app");
        verify(userRepository).findByEmail("missing@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void initiateReset_setsResetTokenAndExpiryWhenUserFound() {
        User u = new User();
        when(userRepository.findByEmail("u@example.com")).thenReturn(Optional.of(u));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        userService.initiateReset("u@example.com", "http://app");

        assertNotNull(u.getResetToken());
        assertNotNull(u.getTokenExpiry());
        verify(userRepository).save(u);
    }

    @Test
    void resetPassword_returnsFalseWhenTokenNotFound() {
        when(userRepository.findByResetToken("nope")).thenReturn(Optional.empty());
        assertFalse(userService.resetPassword("nope", "newpass"));
    }

    @Test
    void resetPassword_returnsFalseWhenTokenExpired() {
        User u = new User();
        u.setTokenExpiry(LocalDateTime.now().minusHours(1));
        when(userRepository.findByResetToken("tok")).thenReturn(Optional.of(u));
        assertFalse(userService.resetPassword("tok", "newpass"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void resetPassword_updatesPasswordWhenValid() {
        User u = new User();
        u.setTokenExpiry(LocalDateTime.now().plusHours(1));
        when(userRepository.findByResetToken("tok")).thenReturn(Optional.of(u));
        when(encoder.encode("newpass")).thenReturn("encodedNew");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        assertTrue(userService.resetPassword("tok", "newpass"));
        assertEquals("encodedNew", u.getPassword());
        assertNull(u.getResetToken());
        assertNull(u.getTokenExpiry());
        verify(userRepository).save(u);
    }
}
