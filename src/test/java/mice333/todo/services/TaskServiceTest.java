package mice333.todo.services;

import mice333.todo.models.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskServiceTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void getTaskById_ShouldReturnTask() throws Exception {
        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setTitle("title");
        when(taskService.getTaskById(1L))
                .thenReturn(mockTask);

        mvc.perform(get("/tasks/task/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("title"));
    }

}