package mice333.todo.services;

import io.jsonwebtoken.Header;
import mice333.todo.models.Task;
import mice333.todo.models.User;
import mice333.todo.repositories.TaskRepository;
import mice333.todo.repositories.UserRepository;
import mice333.todo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public Task createTask(Task task, String token) throws Exception {
        String username = jwtUtils.extractUsername(token.substring(7));
        System.out.println(username);
        User user = userRepository.findByUsername(username).orElseThrow(Exception::new);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

   public Task updateTask(Long id, Task updatedTask) {
        Task oldTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id not found"));

        if (updatedTask.getTitle() != null) {
            oldTask.setTitle(updatedTask.getTitle());
        }
       if (updatedTask.getDescription() != null) {
           oldTask.setDescription(updatedTask.getDescription());
       }
       if (oldTask.isCompleted() != updatedTask.isCompleted()) {
           oldTask.setCompleted(updatedTask.isCompleted());
       }

        return taskRepository.save(oldTask);
   }

   public void deleteTask(Long id) {
        taskRepository.deleteById(id);
   }

   public List<Task> filterByStatus(boolean status) {
        return taskRepository.findByisCompleted(status);
   }

   public List<Task> filterByDate() {
        return taskRepository.findAllByOrderByCreatedAtAsc();
   }

   public List<Task> filterByPriority() {
        return taskRepository.findAllByOrderByPriorityDesc();
   }

   public static String getBearerTokenHeader() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
   }
}
