package fun.toolkits.service.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * 智能体主程序 - 提供命令行交互界面
 */
@Component
public class IntelligentAgentMain implements CommandLineRunner {

    @Autowired
    private IntelligentAgentService intelligentAgentService;

    @Override
    public void run(String... args) throws Exception {
        // 初始化智能体服务
        intelligentAgentService.initialize();

        if (System.console() == null) {
            // 非交互环境（如 Docker），跳过命令行循环
            return;
        }

        System.out.println("🤖 欢迎使用智能助手系统！");
        System.out.println("===================================");
        System.out.println("支持的意图：");
        System.out.println("- 问候：你好、hello");
        System.out.println("- 文件处理：Excel转PDF、文件转换");
        System.out.println("- 用户管理：用户登录、注册");
        System.out.println("- 帮助：如何使用、帮助");
        System.out.println("- 退出：退出、再见");
        System.out.println("===================================");
        System.out.println("请输入您的问题（输入 'exit' 退出）：");

        Scanner scanner = new Scanner(System.in);
        String sessionId = "session-" + System.currentTimeMillis();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                System.out.println("👋 再见！");
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            // 处理用户输入
            try {
                String response = intelligentAgentService.processInput(input, sessionId);
                System.out.println(response);
                System.out.println();
            } catch (Exception e) {
                System.out.println("❌ 错误: " + e.getMessage());
                System.out.println();
            }
        }

        scanner.close();
    }
}