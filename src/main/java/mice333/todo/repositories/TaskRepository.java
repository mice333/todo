package mice333.todo.repositories;

import mice333.todo.models.Task;
import mice333.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    int countByUser(User user);
    List<Task> findAllByUser(User user);
    List<Task> findByisCompleted(boolean isCompleted);
    List<Task> findByisCompletedAndUser(boolean isCompleted, User user);
    List<Task> findAllByUserOrderByCreatedAtAsc(User user);
    List<Task> findAllByUserOrderByPriorityDesc(User user);

    void deleteAllByUser(User user);
}