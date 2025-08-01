package mice333.todo.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mice333.todo.models.Role;
import mice333.todo.models.User;
import mice333.todo.repositories.UserRepository;
import mice333.todo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiry}")
    private int expiry;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        log.info("Пользователь {} входит в систему", request.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            String token = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
            log.info("Токен выдан пользователю {}", request.getUsername());
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setMaxAge(expiry);
            response.addCookie(cookie);
            response.setContentType("text/plain");
            log.info("Токен передан в cookie");
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Логин или пароль введены не правильно");
        }
    }


    record AuthRequest(String username, String password) {

        public Object getUsername() {
            return username;
        }

        public Object getPassword() {
            return password;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistryRequest request) {
        log.info("Проход регистрации POST \"/api/auth/register\"");
        User user = new User();
        Exception e = request.checkPassword();
        if (e != null) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setRole(Role.USER);
        userRepository.save(user);
        return ResponseEntity.ok("Регистрация успешно пройдена");
    }

    record RegistryRequest(String username, String password, String repeatPassword) {

        @Override
        public String username() {
            return username;
        }

        @Override
        public String password() {
            return password;
        }

        public Exception checkPassword() {
            log.info("Проверка пароля пользователя {}", username);
            if (!password.equals(repeatPassword)) {
                return new Exception("Пароли не совпадают");
            } else {
                return null;
            }
        }
    }
}

