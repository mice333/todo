package mice333.todo.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthController authController;

    /**
     * Тестирование входа в аккаунт с невалидными данными
     * @return код ответа 400 с сообщением "Логин или пароль введены не правильно"
     */
    @Test
    void BadCredentialsForLoginReturnExceptionMessage() {
        String expect = "Логин или пароль введены не правильно";
        AuthController.AuthRequest invalidRequest = new AuthController.AuthRequest("invalidUser", "invalidPassword");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException(expect));

        ResponseEntity<String> responseEntity = authController.login(invalidRequest, response);

        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals(expect, responseEntity.getBody());
    }
}