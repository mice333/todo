package mice333.todo.controllers;

import jakarta.websocket.server.PathParam;
import mice333.todo.models.Task;
import mice333.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<?> showTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return ResponseEntity.ok().body("Список задач пуст");
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/filter/status")
    public ResponseEntity<?> showFilteredTasksByStatus(@RequestParam(name = "completed") boolean status) {
        return ResponseEntity.ok(taskService.filterByStatus(status));
    }

    @GetMapping("/filter/date")
    public ResponseEntity<?> showFilteredTasksByDate() {
        return ResponseEntity.ok(taskService.filterByDate());
    }

    @GetMapping("/filter/priority")
    public ResponseEntity<?> showFilteredTasksByPriority() {
        return ResponseEntity.ok(taskService.filterByPriority());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task, @RequestHeader("Authorization") String token) throws Exception {
        System.out.println(token);
        Task crtdTask = taskService.createTask(task, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(crtdTask);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            Task updTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updTask);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
