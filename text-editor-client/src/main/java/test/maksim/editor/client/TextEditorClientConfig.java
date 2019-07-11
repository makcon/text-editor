package test.maksim.editor.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextEditorClientConfig {

    private final TextEditorClientFactory factory = new TextEditorClientFactory();

    @Bean
    public TextEditorClient contentClient(@Value("${text-editor.service.base.url:http://localhost:8080}") String serviceUrl) {
        return factory.defaultClient(serviceUrl);
    }
}
