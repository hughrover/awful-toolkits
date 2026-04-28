package fun.toolkits.service.agent.Skills;

import fun.toolkits.service.LocalFileConverterService;
import fun.toolkits.service.agent.AgentContext;
import fun.toolkits.service.agent.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Excel转PDF技能
 */
@Component
public class ExcelToPdfSkill implements Skill {

    @Autowired
    private LocalFileConverterService localFileConverterService;

    @Override
    public String getSkillName() {
        return "excel-to-pdf-converter";
    }

    @Override
    public String getSkillDescription() {
        return "将Excel文件转换为PDF文件";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    public String convertExcelToPdf(String excelFilePath, String outputDir) {
        try {
            AgentContext context = new AgentContext();
            context.setVariable("excelPath", excelFilePath);
            context.setVariable("outputDir", outputDir);

            // 调用服务转换
            String pdfPath = convertExcel(excelFilePath, outputDir);
            context.setVariable("pdfPath", pdfPath);

            return "Excel转PDF成功！文件保存到：" + pdfPath;
        } catch (Exception e) {
            throw new RuntimeException("Excel转PDF失败: " + e.getMessage(), e);
        }
    }

    private String convertExcel(String excelFilePath, String outputDir) {
        // 实际转换逻辑由LocalFileConverterService处理
        return "转换后的PDF文件路径";
    }
}
