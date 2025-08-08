package mice333.todo.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Todo",
                description = "API списка задач by mice333",
                version = "1.0.0",
                contact = @Contact(
                        name = "mice333",
                        url = "https://github.com/mice333"
                )
        )
)
public class SwaggerConfig {
}
