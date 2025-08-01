package mice333.todo.controllers;

import jakarta.servlet.http.HttpServletResponse;
import mice333.todo.models.AuthRequest;
import mice333.todo.models.RegistryRequest;
import mice333.todo.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtUtils jwtUtils;


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
        AuthRequest invalidRequest = new AuthRequest("invalidUser", "invalidPassword");
        when(authenticationManager.authenticate(
                    argThat(auth ->
                            "invalidUser".equals(auth.getPrincipal()) &&
                            "invalidPassword".equals(auth.getCredentials()))
                )).thenThrow(new BadCredentialsException(expect));

        ResponseEntity<String> responseEntity = authController.login(invalidRequest, response);

        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals(expect, responseEntity.getBody());
    }

    /**
     * Тестирование регистрации аккаунта с несовпадающими полями: password, repeatPassword
     * @return код ответа 400 с сообщением "Пароли не совпадают"
     * */
    @Test
    void NonEqualsPasswordsForRegisterReturnExceptionMessage() {
        String expect = "Пароли не совпадают";
        RegistryRequest invalidRequest = new RegistryRequest("invalidUser", "invalidPassword", "invalidRepeatPassword");

        ResponseEntity<String> responseEntity = authController.register(invalidRequest);

        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals(expect, responseEntity.getBody());
    }

    /*
    * Вход в аккаунт с верными данными. Проверка subject в токене для подтверждения валидности.
    * */

    /**
     * Вход в аккаунт с верными данными. Вернётся token пользователя.
     *
     * @return статус 200 и token
     * */
    @Test
    void LoginToAccountWithValidCredentialsReturnTokenWithUsername() {
        AuthRequest validRequest = new AuthRequest("correctUser", "correctPassword");
        UserDetails userDetails = mock(UserDetails.class);
        Authentication auth = mock(Authentication.class);

        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(
                argThat(authToken ->
                        "correctUser".equals(authToken.getPrincipal()) &&
                                "correctPassword".equals(authToken.getCredentials())
                )
        )).thenReturn(auth);

        when(jwtUtils.generateToken(userDetails)).thenReturn("valid.token");

        // Act
        ResponseEntity<String> response = authController.login(validRequest, this.response);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("valid.token", response.getBody());
    }
}