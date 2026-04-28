package fun.toolkits.service.agent;

import fun.toolkits.service.LocalFileConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 文件处理节点 - 支持多种文件处理操作
 */
@Component
public class FileProcessingAgentNode extends AbstractAgentNode {

    @Autowired
    private LocalFileConverterService localFileConverterService;

    public FileProcessingAgentNode() {
        super("file-processing", "处理各类文件操作的节点");
    }

    @Override
    public String process(String input, AgentContext context) {
        try {
            // 识别文件类型和处理类型
            FileOperation operation = identifyFileOperation(input);
            String filePath = extractFilePath(input);

            if (filePath == null) {
                return "请您提供文件的完整路径，例如：/path/to/document.xlsx";
            }

            File resultFile = null;

            switch (operation) {
                case EXCEL_TO_PDF:
                    resultFile = localFileConverterService.convert(filePath);
                    return "Excel转PDF成功！PDF文件已保存到: " + resultFile.getAbsolutePath();

                case PDF_TO_EXCEL:
                    // TODO: 实现PDF转Excel
                    return "PDF转Excel功能正在开发中...";

                case IMAGE_OPTIMIZATION:
                    // TODO: 实现图片优化
                    return "图片优化功能正在开发中...";

                case FILE_COMPRESSION:
                    // TODO: 实现文件压缩
                    return "文件压缩功能正在开发中...";

                default:
                    return "暂不支持该文件处理类型，当前支持：Excel转PDF";
            }
        } catch (Exception e) {
            return "文件处理失败: " + e.getMessage();
        }
    }

    /**
     * 识别文件操作类型
     */
    private FileOperation identifyFileOperation(String input) {
        String text = input.toLowerCase();

        if (text.contains("excel") || text.contains("xlsx") || text.contains("xls") ||
            text.contains("表格")) {
            if (text.contains("转pdf") || text.contains("转成pdf") ||
                text.contains("转换pdf")) {
                return FileOperation.EXCEL_TO_PDF;
            }
        }

        if (text.contains("pdf") && text.contains("excel")) {
            return FileOperation.PDF_TO_EXCEL;
        }

        if (text.contains("图片") || text.contains("image") || text.contains("photo")) {
            if (text.contains("压缩") || text.contains("optimize") || text.contains("优化")) {
                return FileOperation.IMAGE_OPTIMIZATION;
            }
        }

        if (text.contains("压缩") || text.contains("compress")) {
            return FileOperation.FILE_COMPRESSION;
        }

        return FileOperation.EXCEL_TO_PDF; // 默认为Excel转PDF
    }

    /**
     * 从用户输入中提取文件路径
     */
    private String extractFilePath(String input) {
        // 使用正则表达式提取可能的文件路径
        String[] parts = input.split("\\s+");
        for (String part : parts) {
            if (part.matches(".+\\.(xlsx?|pdf|docx?|txt|png|jpg|jpeg)$")) {
                return part;
            }
        }
        return null;
    }

    /**
     * 文件操作枚举
     */
    private enum FileOperation {
        EXCEL_TO_PDF,
        PDF_TO_EXCEL,
        IMAGE_OPTIMIZATION,
        FILE_COMPRESSION
    }
}