package ExpenseTracker.ProjectExpenseTracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MailServiceTest {

    // @Mock
    // private JavaMailSender mailSender;

    // @InjectMocks
    // private MailService mailService;

    // @BeforeEach
    // void setUp() {
    //     MockitoAnnotations.openMocks(this);
    // }

    // @Test
    // void testSend_callsMailSenderWithCorrectMessage() {
    //     String to = "user@example.com";
    //     String subject = "Test Subject";
    //     String text = "Hello from test";

    //     mailService.send(to, subject, text);

    //     ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
    //     verify(mailSender).send(captor.capture());
    //     SimpleMailMessage sent = captor.getValue();
    //     assertArrayEquals(new String[]{to}, sent.getTo());
    //     assertEquals(subject, sent.getSubject());
    //     assertEquals(text, sent.getText());
    // }

}
