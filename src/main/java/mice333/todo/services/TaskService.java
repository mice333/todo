package mice333.todo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mice333.todo.models.Task;
import mice333.todo.models.User;
import mice333.todo.repositories.TaskRepository;
import mice333.todo.repositories.UserRepository;
import mice333.todo.security.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public Task createTask(Task task, String token) throws Exception {
        String username = jwtUtils.extractUsername(token.substring(7));
        System.out.println(username);
        User user = userRepository.findByUsername(username).orElseThrow(Exception::new);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        log.info("Получен список всех задач.");
        return (List<Task>) taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        log.info("Получена задача с {} id", id);
        return taskRepository.findById(id);
    }

   public Task updateTask(Long id, Task updatedTask) {
        Task oldTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id: " + id + " - not found"));

        if (updatedTask.getTitle() != null) {
            oldTask.setTitle(updatedTask.getTitle());
        }
       if (updatedTask.getDescription() != null) {
           oldTask.setDescription(updatedTask.getDescription());
       }
       if (oldTask.isCompleted() != updatedTask.isCompleted()) {
           oldTask.setCompleted(updatedTask.isCompleted());
       }
       log.info("Обновлена задача с {} id", id);
       return taskRepository.save(oldTask);
   }

   public void deleteTask(Long id) {
        log.info("Удалена задача с {}", id);
        taskRepository.deleteById(id);
   }

   public List<Task> filterByStatus(boolean status) {
        log.info("Задачи отсортированы по статусу.\nТекущий статус - {}", status);
        return taskRepository.findByisCompleted(status);
   }

   public List<Task> filterByDate() {
        log.info("Задачи отсортированы по дате создания");
        return taskRepository.findAllByOrderByCreatedAtAsc();
   }

   public List<Task> filterByPriority() {
        log.info("Задачи отсортированы по приоритету");
        return taskRepository.findAllByOrderByPriorityDesc();
   }
}
