package fun.toolkits.service.agent.skill;

import fun.toolkits.service.ImageStorageService;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class ImageGenerationSkill {

    @Autowired
    private ImageModel imageModel;

    @Autowired
    private ImageStorageService imageStorageService;

    @Bean
    @Description("Generate an image based on a text prompt. Parameters: prompt (the description of the image to generate).")
    public Function<ImageGenerationRequest, String> generateImage() {
        return request -> {
            try {
                ImageResponse response = imageModel.call(new ImagePrompt(request.prompt()));
                String remoteUrl = response.getResult().getOutput().getUrl();
                String localUrl = imageStorageService.saveImageFromUrl(remoteUrl);
                return String.format("![image](%s)", localUrl);
            } catch (Exception e) {
                return "Failed to generate image: " + e.getMessage();
            }
        };
    }

    public record ImageGenerationRequest(String prompt) {}
}
