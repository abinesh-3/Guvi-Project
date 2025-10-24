package ExpenseTracker.ProjectExpenseTracker.controller;

import ExpenseTracker.ProjectExpenseTracker.dto.RegisterRequest;
import ExpenseTracker.ProjectExpenseTracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private UserService userService;
    
    @Mock
    private Model model;
    
    @Mock
    private HttpServletRequest request;
    
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        String viewName = authController.login();
        assertEquals("login", viewName);
    }

    @Test
    void testRegisterForm() {
        String viewName = authController.registerForm(model);
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("req"), any(RegisterRequest.class));
    }

    @Test
    void testRegister() {
    
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/auth/register"));
        when(request.getRequestURI()).thenReturn("/auth/register");
        when(request.getContextPath()).thenReturn("");
        
        
        String viewName = authController.register(registerRequest, request, model);
        
        
        assertEquals("login", viewName);
        verify(userService).register(eq(registerRequest), eq("http://localhost:8080"));
        verify(model).addAttribute("msg", "Registration successful. Check email to verify.");
    }

    @Test
    void testVerifyWithValidToken() {
    
        String token = "valid-token";
        when(userService.verify(token)).thenReturn(true);
        
        
        String viewName = authController.verify(token, model);
        
        
        assertEquals("login", viewName);
        verify(model).addAttribute("msg", "Verified! Please login");
    }

    @Test
    void testVerifyWithInvalidToken() {
        
        String token = "invalid-token";
        when(userService.verify(token)).thenReturn(false);
        
        
        String viewName = authController.verify(token, model);
        
        
        assertEquals("login", viewName);
        verify(model).addAttribute("msg", "Invalid or expired token");
    }

    @Test
    void testResetForm() {
        String viewName = authController.resetForm();
        assertEquals("reset-password", viewName);
    }

    @Test
    void testReset() {
        
        String email = "test@example.com";
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/auth/reset"));
        when(request.getRequestURI()).thenReturn("/auth/reset");
        when(request.getContextPath()).thenReturn("");
        
        
        String viewName = authController.reset(email, request, model);
        
        
        assertEquals("login", viewName);
        verify(userService).initiateReset(eq(email), eq("http://localhost:8080"));
        verify(model).addAttribute("msg", "If the email exists, a reset link was sent.");
    }

    @Test
    void testConfirmPage() {
        
        String token = "reset-token";
        
        
        String viewName = authController.confirmPage(token, model);
        
        
        assertEquals("reset-password", viewName);
        verify(model).addAttribute("token", token);
    }

    @Test
    void testConfirmWithValidToken() {
        
        String token = "valid-token";
        String password = "newpassword";
        when(userService.resetPassword(token, password)).thenReturn(true);
        
        
        String viewName = authController.confirm(token, password, model);
        
        
        assertEquals("login", viewName);
        verify(model).addAttribute("msg", "Password updated. Login now");
    }

    @Test
    void testConfirmWithInvalidToken() {
        
        String token = "invalid-token";
        String password = "newpassword";
        when(userService.resetPassword(token, password)).thenReturn(false);
        
        
        String viewName = authController.confirm(token, password, model);
        
        assertEquals("login", viewName);
        verify(model).addAttribute("msg", "Invalid or expired token");
    }
}
