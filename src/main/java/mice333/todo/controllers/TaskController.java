package mice333.todo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import mice333.todo.models.Task;
import mice333.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер задач", description = "Позволяет управлять списком задач")
@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить список всех задач"
    )
    @GetMapping
    public ResponseEntity<?> showTasks(@RequestParam @Parameter String username) {
        log.info("Отправлен GET по пути \"/tasks\"");
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return ResponseEntity.status(404).body("tasks are empty");
        }
        return ResponseEntity.ok(tasks);
    }

    @Operation(
            summary = "Получение всех задач с заданным статусом",
            description = "В зависимости от статуса задачи фильтрует задачи"
    )
    @GetMapping("/filter/status")
    public ResponseEntity<?> showFilteredTasksByStatus(@RequestParam @Parameter String username, @RequestParam(name = "completed") @Parameter(description = "Выполнена ли задача?", example = "true") boolean status) {
        log.info("Отправлен GET по пути \"/tasks/filter/status&completed={}\"", status);
        List<Task> tasks = taskService.filterByStatus(username,status);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(404).body("tasks are empty");
        }
        return ResponseEntity.ok(tasks);
    }

    // TODO: ЭТО НЕ ФИЛЬТРАЦИЯ, ЭТО СОРТИРОВКА
    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить список всех задач"
    )
    @GetMapping("/filter/date")
    public ResponseEntity<?> showFilteredTasksByDate(@RequestParam @Parameter String username) {
        log.info("Отправлен GET по пути \"/tasks/filter/date\"");

        return ResponseEntity.ok(taskService.filterByDate());
    }

    // TODO: ЭТО НЕ ФИЛЬТРАЦИЯ, ЭТО СОРТИРОВКА
    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить список всех задач"
    )
    @GetMapping("/filter/priority")
    public ResponseEntity<?> showFilteredTasksByPriority(@RequestParam @Parameter String username) {
        log.info("Отправлен GET по пути \"/tasks/filter/priority\"");

        return ResponseEntity.ok(taskService.filterByPriority());
    }

    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить список всех задач"
    )
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task, @RequestParam @Parameter String username) throws Exception {
        log.info("Отправлен POST по пути \"/tasks/create\"");

        Task crtdTask = taskService.createTask(task, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(crtdTask);
    }

    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить список всех задач"
    )
    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@RequestParam @Parameter String username, @PathVariable Long id, @RequestBody Task task) {
        log.info("Отправлен PUT по пути \"/tasks/task/{}\"", id);

        try {
            Task updTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updTask);
        } catch (RuntimeException e) {
            log.error("");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить список всех задач"
    )
    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@RequestParam @Parameter String username, @PathVariable Long id) {
        log.info("Отправлен DELETE по пути \"/tasks/task/{}\"", id);

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
