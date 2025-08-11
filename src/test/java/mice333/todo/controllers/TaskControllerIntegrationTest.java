package mice333.todo.controllers;

import mice333.todo.models.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getTaskById_ShouldReturnTask() {
        ResponseEntity<Task> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/tasks/task/43",
                Task.class
        );

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("fdasf",response.getBody().getTitle());
    }

    @Test
    void getTaskByIdWhichNotFound_ShouldReturn404() {
        ResponseEntity<?> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/tasks/task/-1",
                Task.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
