package toolkits.boostrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * @author Diego Liu
 * @since 2026/3/18 18:24
 */
@SpringBootApplication
@ComponentScan(basePackages = {"toolkits", "fun.toolkits"})
@MapperScan(basePackages = {"toolkits.**.mapper", "fun.toolkits.**.mapper"})
public class ToolkitsApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ToolkitsApplication.class);
        Environment env = app.run(args).getEnvironment();
        System.out.println("Server is running on port: " + env.getProperty("server.port"));
    }

}
