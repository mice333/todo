package mice333.todo.models;

public record AuthRequest(String username, String password) {

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }
}
