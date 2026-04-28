package fun.toolkits.service;

import fun.toolkits.service.impl.ExcelToPdfServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Excel 转 PDF 服务测试
 */
@DisplayName("Excel 转 PDF 服务测试")
class ExcelToPdfServiceTest {

    private final ExcelToPdfService excelToPdfService = new ExcelToPdfServiceImpl();

    @TempDir
    Path tempDir;

    /**
     * 创建测试用 Excel 文件（简单表格）
     */
    private byte[] createSimpleExcel() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("测试Sheet");

            // 创建表头
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("姓名");
            headerRow.createCell(1).setCellValue("年龄");
            headerRow.createCell(2).setCellValue("邮箱");

            // 创建数据行
            org.apache.poi.ss.usermodel.Row dataRow1 = sheet.createRow(1);
            dataRow1.createCell(0).setCellValue("张三");
            dataRow1.createCell(1).setCellValue(25);
            dataRow1.createCell(2).setCellValue("zhangsan@example.com");

            org.apache.poi.ss.usermodel.Row dataRow2 = sheet.createRow(2);
            dataRow2.createCell(0).setCellValue("李四");
            dataRow2.createCell(1).setCellValue(30);
            dataRow2.createCell(2).setCellValue("lisi@example.com");

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 创建测试用 Excel 文件（包含多种数据类型）
     */
    private byte[] createExcelWithMultipleDataTypes() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("多种数据类型");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("类型");
            headerRow.createCell(1).setCellValue("值");

            org.apache.poi.ss.usermodel.Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("字符串");
            row1.createCell(1).setCellValue("Hello World");

            org.apache.poi.ss.usermodel.Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("整数");
            row2.createCell(1).setCellValue(12345);

            org.apache.poi.ss.usermodel.Row row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("小数");
            row3.createCell(1).setCellValue(3.14159);

            org.apache.poi.ss.usermodel.Row row4 = sheet.createRow(4);
            row4.createCell(0).setCellValue("布尔值");
            row4.createCell(1).setCellValue(true);

            org.apache.poi.ss.usermodel.Row row5 = sheet.createRow(5);
            row5.createCell(0).setCellValue("日期");
            row5.createCell(1).setCellValue("2024-01-15");

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 创建测试用 Excel 文件（包含多个 Sheet）
     */
    private byte[] createExcelWithMultipleSheets() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 第一个 Sheet
            org.apache.poi.ss.usermodel.Sheet sheet1 = workbook.createSheet("员工信息");
            org.apache.poi.ss.usermodel.Row headerRow1 = sheet1.createRow(0);
            headerRow1.createCell(0).setCellValue("姓名");
            headerRow1.createCell(1).setCellValue("部门");

            org.apache.poi.ss.usermodel.Row dataRow1 = sheet1.createRow(1);
            dataRow1.createCell(0).setCellValue("张三");
            dataRow1.createCell(1).setCellValue("技术部");

            // 第二个 Sheet
            org.apache.poi.ss.usermodel.Sheet sheet2 = workbook.createSheet("产品列表");
            org.apache.poi.ss.usermodel.Row headerRow2 = sheet2.createRow(0);
            headerRow2.createCell(0).setCellValue("产品名");
            headerRow2.createCell(1).setCellValue("价格");

            org.apache.poi.ss.usermodel.Row dataRow2 = sheet2.createRow(1);
            dataRow2.createCell(0).setCellValue("笔记本");
            dataRow2.createCell(1).setCellValue(5999.0);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 创建测试用 Excel 文件（包含合并单元格）
     */
    private byte[] createExcelWithMergedCells() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("合并单元格测试");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            org.apache.poi.ss.usermodel.Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("标题");
            headerRow.createCell(1).setCellValue("子标题1");
            headerRow.createCell(2).setCellValue("子标题2");

            // 合并第一行前两个单元格
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 1));

            org.apache.poi.ss.usermodel.Row dataRow1 = sheet.createRow(1);
            dataRow1.createCell(0).setCellValue("数据1");
            dataRow1.createCell(1).setCellValue("数据2");
            dataRow1.createCell(2).setCellValue("数据3");

            org.apache.poi.ss.usermodel.Row dataRow2 = sheet.createRow(2);
            dataRow2.createCell(0).setCellValue("数据4");
            dataRow2.createCell(1).setCellValue("数据5");
            dataRow2.createCell(2).setCellValue("数据6");

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 创建测试用 Excel 文件（包含公式）
     */
    private byte[] createExcelWithFormulas() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("公式测试");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("数值A");
            headerRow.createCell(1).setCellValue("数值B");
            headerRow.createCell(2).setCellValue("总和");

            org.apache.poi.ss.usermodel.Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(10);
            dataRow.createCell(1).setCellValue(20);
            org.apache.poi.ss.usermodel.Cell formulaCell = dataRow.createCell(2);
            formulaCell.setCellFormula("A2+B2");

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Test
    @DisplayName("测试简单 Excel 转 PDF")
    void testConvertSimpleExcel() throws IOException {
        byte[] excelBytes = createSimpleExcel();
        byte[] pdfBytes = excelToPdfService.convert(excelBytes);

        assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
        assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
        assertTrue(pdfBytes.length > 1000, "PDF 文件应有合理的大小");
    }

    @Test
    @DisplayName("测试使用输入流转换 Excel")
    void testConvertWithInputStream() throws IOException {
        byte[] excelBytes = createSimpleExcel();
        InputStream inputStream = new ByteArrayInputStream(excelBytes);

        byte[] pdfBytes = excelToPdfService.convert(inputStream);

        assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
        assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
    }

    @Test
    @DisplayName("测试包含多种数据类型的 Excel 转 PDF")
    void testConvertExcelWithMultipleDataTypes() throws IOException {
        byte[] excelBytes = createExcelWithMultipleDataTypes();
        byte[] pdfBytes = excelToPdfService.convert(excelBytes);

        assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
        assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
    }

    @Test
    @DisplayName("测试包含多个 Sheet 的 Excel 转 PDF")
    void testConvertExcelWithMultipleSheets() throws IOException {
        byte[] excelBytes = createExcelWithMultipleSheets();
        byte[] pdfBytes = excelToPdfService.convert(excelBytes);

        assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
        assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
    }

    @Test
    @DisplayName("测试包含合并单元格的 Excel 转 PDF")
    void testConvertExcelWithMergedCells() throws IOException {
        byte[] excelBytes = createExcelWithMergedCells();
        byte[] pdfBytes = excelToPdfService.convert(excelBytes);

        assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
        assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
    }

    @Test
    @DisplayName("测试包含公式的 Excel 转 PDF")
    void testConvertExcelWithFormulas() throws IOException {
        byte[] excelBytes = createExcelWithFormulas();
        byte[] pdfBytes = excelToPdfService.convert(excelBytes);

        assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
        assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
    }

    @Test
    @DisplayName("测试转换并保存到文件")
    void testConvertAndSave() throws IOException {
        byte[] excelBytes = createSimpleExcel();
        Path outputPath = tempDir.resolve("output.pdf");

        excelToPdfService.convertAndSave(excelBytes, outputPath.toString());

        assertTrue(Files.exists(outputPath), "PDF 文件应已创建");
        assertTrue(Files.size(outputPath) > 0, "PDF 文件应有内容");

        byte[] savedPdfBytes = Files.readAllBytes(outputPath);
        assertTrue(savedPdfBytes.length > 0, "保存的 PDF 文件应包含数据");
    }

    @Test
    @DisplayName("测试空 Excel 文件转 PDF")
    void testConvertEmptyExcel() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 创建只有表头的 Sheet
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("空Sheet");
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("标题1");
            headerRow.createCell(1).setCellValue("标题2");

            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            byte[] pdfBytes = excelToPdfService.convert(excelBytes);

            assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
            assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
        }
    }

    @Test
    @DisplayName("测试大文件转换")
    void testConvertLargeExcel() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("大文件测试");

            // 创建表头
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            for (int i = 0; i < 10; i++) {
                headerRow.createCell(i).setCellValue("列" + (i + 1));
            }

            // 创建 100 行数据
            for (int i = 1; i <= 100; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i);
                for (int j = 0; j < 10; j++) {
                    row.createCell(j).setCellValue("数据" + i + "-" + j);
                }
            }

            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            byte[] pdfBytes = excelToPdfService.convert(excelBytes);

            assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
            assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
        }
    }

    @Test
    @DisplayName("测试中文内容转换")
    void testConvertChineseContent() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("中文测试");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("姓名");
            headerRow.createCell(1).setCellValue("城市");
            headerRow.createCell(2).setCellValue("备注");

            org.apache.poi.ss.usermodel.Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("张三");
            dataRow.createCell(1).setCellValue("北京市");
            dataRow.createCell(2).setCellValue("这是一个测试备注");

            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            byte[] pdfBytes = excelToPdfService.convert(excelBytes);

            assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
            assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
        }
    }

    @Test
    @DisplayName("测试不同列数的表格")
    void testConvertDifferentColumnCount() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("不同列数测试");

            org.apache.poi.ss.usermodel.Row row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("1");
            row1.createCell(1).setCellValue("2");
            row1.createCell(2).setCellValue("3");

            org.apache.poi.ss.usermodel.Row row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue("4");

            org.apache.poi.ss.usermodel.Row row3 = sheet.createRow(2);
            row3.createCell(0).setCellValue("5");
            row3.createCell(1).setCellValue("6");

            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            byte[] pdfBytes = excelToPdfService.convert(excelBytes);

            assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
            assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
        }
    }

    @Test
    @DisplayName("测试空单元格处理")
    void testConvertWithEmptyCells() throws IOException {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("空单元格测试");

            org.apache.poi.ss.usermodel.Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("有值1");
            row.createCell(1);
            row.createCell(2).setCellValue("有值2");
            row.createCell(3);

            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            byte[] pdfBytes = excelToPdfService.convert(excelBytes);

            assertNotNull(pdfBytes, "PDF 字节数组不应为 null");
            assertTrue(pdfBytes.length > 0, "PDF 字节数组应包含数据");
        }
    }
}
