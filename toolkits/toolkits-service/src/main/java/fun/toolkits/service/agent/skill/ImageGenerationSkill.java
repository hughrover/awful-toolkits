package fun.toolkits.service.agent.skill;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class ImageGenerationSkill {

    @Bean
    @Description("Generate an image based on a text prompt. Parameters: prompt (the description of the image to generate).")
    public Function<ImageGenerationRequest, String> generateImage() {
        return request -> {
            // In a real application, you might call WanX or DALL-E.
            // For now, we'll simulate it.
            return String.format("[SKILL: IMAGE_GEN] Generating image for prompt: '%s'...", request.prompt());
        };
    }

    public record ImageGenerationRequest(String prompt) {}
}
