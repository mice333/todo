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
        List<Task> tasks = taskService.getAllTasks(username);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(404).body("tasks are empty");
        }
        return ResponseEntity.ok(tasks);
    }

    @Operation(
            summary = "Получение задачи по уникальному идентификатору"
    )
    @GetMapping("/task/{id}")
    public ResponseEntity<?> showTaskById(@PathVariable Long id) {
        log.info("Отправлен GET по пути \"/tasks/task/{}\"", id);
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @Operation(
            summary = "Получение всех задач с заданным статусом",
            description = "В зависимости от статуса задачи фильтрует задачи"
    )
    @GetMapping("/filter/status")
    public ResponseEntity<?> showFilteredTasksByStatus(@RequestParam @Parameter String username, @RequestParam(name = "completed") @Parameter(description = "Выполнена ли задача?", example = "true") boolean status) {
        log.info("Отправлен GET по пути \"/tasks/filter/status&completed={}\"", status);
        List<Task> tasks = taskService.filterByStatus(username,status);
        if (tasks.isEmpty()) { // TODO: точно ли такая проверка?
            return ResponseEntity.status(404).body("tasks are empty");
        }
        return ResponseEntity.ok(tasks);
    }

    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить список всех задач"
    )
    @GetMapping("/sort/date")
    public ResponseEntity<?> showSortTasksByDate(@RequestParam @Parameter String username) {
        log.info("Отправлен GET по пути \"/tasks/filter/date\"");

        return ResponseEntity.ok(taskService.filterByDate(username));
    }

    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить список всех задач"
    )
    @GetMapping("/sort/priority")
    public ResponseEntity<?> showSortTasksByPriority(@RequestParam @Parameter String username) {
        log.info("Отправлен GET по пути \"/tasks/filter/priority\"");

        return ResponseEntity.ok(taskService.filterByPriority(username));
    }

    @Operation(
            summary = "Создание задачи",
            description = "Позволяет создать задачу"
    )
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task, @RequestParam @Parameter String username) throws Exception {
        log.info("Отправлен POST по пути \"/tasks/create\"");

        Long taskId = taskService.createTask(task, username);
        if (taskId == null) {
            return ResponseEntity.status(403).body("У вас уже 5 задач - выполните их");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(taskId);
    }

    @Operation(
            summary = "Обновление задачи",
            description = "Позволяет обновить задачу"
    )
    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@RequestParam @Parameter String username, @PathVariable Long id, @RequestBody Task task) {
        log.info("Отправлен PUT по пути \"/tasks/task/{}\"", id);

        try {
            Task updTask = taskService.updateTask(username, id, task);
            if (updTask != null) {
                return ResponseEntity.ok(updTask);
            }
            return ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            log.error("");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Обновление статуса задачи",
            description = "Позволяет изменить статус задачи"
    )
    @PatchMapping("/task/{id}")
    public ResponseEntity<?> updateTaskStatus(@RequestParam @Parameter String username, @PathVariable Long id) {
        log.info("Отправлен PATCH по пути \"/tasks/task/{}\"", id);

        try {
            Task updTask = taskService.completeTask(username, id);
            if (updTask != null) {
                return ResponseEntity.ok(updTask);
            }
            return ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            log.error("");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Удаление определённой задачи",
            description = "Позволяет удалить задачу по уникальному идентификатору"
    )
    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@RequestParam @Parameter String username, @PathVariable Long id) {
        log.info("Отправлен DELETE по пути \"/tasks/task/{}\"", id);

        taskService.deleteTask(username, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAllTasks(@RequestParam @Parameter String username) {
        log.info("Отправлен DELETE по пути \"/tasks/delete/\"");

        taskService.deleteAllTasks(username);
        return ResponseEntity.noContent().build();
    }
}
