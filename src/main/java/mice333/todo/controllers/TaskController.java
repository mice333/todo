package mice333.todo.controllers;

import lombok.extern.slf4j.Slf4j;
import mice333.todo.models.Task;
import mice333.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<?> showTasks() {
        log.info("Отправлен GET по пути \"/tasks\"");
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return ResponseEntity.ok().body("Список задач пуст");
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/filter/status")
    public ResponseEntity<?> showFilteredTasksByStatus(@RequestParam(name = "completed") boolean status) {
        log.info("Отправлен GET по пути \"/tasks/filter/status&completed={}\"", status);
        return ResponseEntity.ok(taskService.filterByStatus(status));
    }

    @GetMapping("/filter/date")
    public ResponseEntity<?> showFilteredTasksByDate() {
        log.info("Отправлен GET по пути \"/tasks/filter/date\"");

        return ResponseEntity.ok(taskService.filterByDate());
    }

    @GetMapping("/filter/priority")
    public ResponseEntity<?> showFilteredTasksByPriority() {
        log.info("Отправлен GET по пути \"/tasks/filter/priority\"");

        return ResponseEntity.ok(taskService.filterByPriority());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task, @RequestHeader("Authorization") String token) throws Exception {
        log.info("Отправлен POST по пути \"/tasks/create\"");

        Task crtdTask = taskService.createTask(task, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(crtdTask);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        log.info("Отправлен PUT по пути \"/tasks/task/{}\"", id);

        try {
            Task updTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updTask);
        } catch (RuntimeException e) {
            log.error("");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Отправлен DELETE по пути \"/tasks/task/{}\"", id);

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
