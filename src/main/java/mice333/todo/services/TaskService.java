package mice333.todo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mice333.todo.models.Task;
import mice333.todo.models.User;
import mice333.todo.repositories.TaskRepository;
import mice333.todo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Task createTask(Task task, String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setLink("t.me/" + username);
            userRepository.save(user);
        }

        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(String username) {
        log.info("Получен список всех задач.");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setLink("t.me/" + username);
            userRepository.save(user);
        }
        return taskRepository.findAllByUser(user);
    }

    public Optional<Task> getTaskById(Long id) {
        log.info("Получена задача с {} id", id);
        return taskRepository.findById(id);
    }

   public Task updateTask(String username, Long id, Task updatedTask) {
        Task oldTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id: " + id + " - not found"));
        if (oldTask.getUser().getUsername().equals(username)) {
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
        return null;
   }

   public void deleteTask(String username, Long id) {
        log.info("Удалена задача с {}", id);
        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getUser().getUsername().equals(username)) {
            taskRepository.deleteById(id);
        }
   }

   public List<Task> filterByStatus(String username,boolean status) {
        log.info("Задачи отсортированы по статусу.\nТекущий статус - {}", status);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setLink("t.me/" + username);
            userRepository.save(user);
        }
        return taskRepository.findByisCompletedAndUser(status, userRepository.findByUsername(username));
   }

   public List<Task> filterByDate(String username) {
        log.info("Задачи отсортированы по дате создания");
       User user = userRepository.findByUsername(username);
       if (user == null) {
           user = new User();
           user.setUsername(username);
           user.setLink("t.me/" + username);
           userRepository.save(user);
       }
        return taskRepository.findAllByUserOrderByCreatedAtAsc(user);
   }

   public List<Task> filterByPriority(String username) {
        log.info("Задачи отсортированы по приоритету");
       User user = userRepository.findByUsername(username);
       if (user == null) {
           user = new User();
           user.setUsername(username);
           user.setLink("t.me/" + username);
           userRepository.save(user);
       }
        return taskRepository.findAllByUserOrderByPriorityDesc(user);
   }
}
