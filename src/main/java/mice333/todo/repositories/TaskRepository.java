package mice333.todo.repositories;

import mice333.todo.models.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByisCompleted(boolean isCompleted);
    List<Task> findAllByOrderByCreatedAtAsc();
    List<Task> findAllByOrderByPriorityDesc();
}