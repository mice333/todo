package mice333.todo.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users", schema = "public")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String link = "t.me/";


}
