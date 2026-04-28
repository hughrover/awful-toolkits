package fun.toolkits.service.agent;

/**
 * 意图枚举
 */
public enum Intent {
    UNKNOWN("未知意图"),
    GREETING("问候"),
    FILE_PROCESSING("文件处理"),
    USER_MANAGEMENT("用户管理"),
    HELP("帮助"),
    EXIT("退出");

    private final String description;

    Intent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Intent fromText(String text) {
        if (text == null || text.isEmpty()) {
            return UNKNOWN;
        }

        text = text.toLowerCase();

        if (text.contains("你好") || text.contains("hello") || text.contains("hi") ||
            text.contains("问候") || text.contains("打招呼")) {
            return GREETING;
        }

        if (text.contains("excel") || text.contains("表格") || text.contains("xlsx") ||
            text.contains("xls") || text.contains("pdf") || text.contains("文件") ||
            text.contains("转换") || text.contains("处理")) {
            return FILE_PROCESSING;
        }

        if (text.contains("用户") || text.contains("user") || text.contains("管理") ||
            text.contains("登录") || text.contains("注册")) {
            return USER_MANAGEMENT;
        }

        if (text.contains("帮助") || text.contains("help") || text.contains("怎么用") ||
            text.contains("如何")) {
            return HELP;
        }

        if (text.contains("退出") || text.contains("exit") || text.contains("结束") ||
            text.contains("再见")) {
            return EXIT;
        }

        return UNKNOWN;
    }
}
