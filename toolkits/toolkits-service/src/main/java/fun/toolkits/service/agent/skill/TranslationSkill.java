package fun.toolkits.service.agent.skill;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class TranslationSkill {

    @Bean
    @Description("Translate text from one language to another. Parameters: text (the text to translate), targetLanguage (the language to translate to, e.g., 'Chinese', 'English').")
    public Function<TranslationRequest, String> translateText() {
        return request -> {
            // In a real application, you might call an external translation API.
            // For now, we'll simulate it.
            return String.format("[SKILL: TRANSLATION] Translating '%s' to %s...", request.text(), request.targetLanguage());
        };
    }

    public record TranslationRequest(String text, String targetLanguage) {}
}
