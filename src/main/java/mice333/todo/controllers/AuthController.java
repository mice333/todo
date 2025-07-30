package mice333.todo.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mice333.todo.models.Role;
import mice333.todo.models.User;
import mice333.todo.repositories.UserRepository;
import mice333.todo.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        return ResponseEntity.ok(jwtUtils.generateToken((UserDetails) authentication.getPrincipal()));
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
        User user = new User();
        Exception e = request.checkPassword();
        if (e != null) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setRole(Role.USER);
        userRepository.save(user);
        return ResponseEntity.ok("регистрация успешно пройдена");
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
            if (!password.equals(repeatPassword)) {
                return new Exception("Пароли не совпадают");
            } else {
                return null;
            }
        }
    }
}

