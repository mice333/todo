package mice333.todo.models;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record RegistryRequest(String username, String password, String repeatPassword) {

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
