package fun.toolkits.service;

import fun.toolkits.service.impl.ExcelToPdfServiceImpl;
import fun.toolkits.service.impl.LocalFileConverterServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 本地文件转换服务测试
 */
@DisplayName("本地文件转换服务测试")
class LocalFileConverterServiceTest {

    private LocalFileConverterService localFileConverterService;
    private ExcelToPdfService excelToPdfService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        excelToPdfService = new ExcelToPdfServiceImpl();
        localFileConverterService = new LocalFileConverterServiceImpl(excelToPdfService);
    }

    @AfterEach
    void tearDown() throws IOException {
        // 清理测试生成的 PDF 文件
        try (var paths = Files.walk(tempDir)) {
            paths.filter(path -> path.toString().endsWith(".pdf"))
                 .forEach(path -> {
                     try {
                         Files.deleteIfExists(path);
                     } catch (IOException e) {
                         // 忽略删除失败
                     }
                 });
        }
    }

    /**
     * 创建测试用 Excel 文件
     */
    private File createTestExcelFile(String fileName) throws IOException {
        Path filePath = tempDir.resolve(fileName);
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("测试Sheet");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("姓名");
            headerRow.createCell(1).setCellValue("年龄");

            org.apache.poi.ss.usermodel.Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("张三");
            dataRow.createCell(1).setCellValue(25);

            Files.write(filePath, outputStream.toByteArray());
            workbook.write(Files.newOutputStream(filePath));
            return filePath.toFile();
        }
    }

    /**
     * 创建测试用 Excel 文件 (.xls 格式)
     */
    private File createTestExcelFileXls(String fileName) throws IOException {
        Path filePath = tempDir.resolve(fileName);
        try (org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("测试Sheet");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("姓名");
            headerRow.createCell(1).setCellValue("年龄");

            org.apache.poi.ss.usermodel.Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("李四");
            dataRow.createCell(1).setCellValue(30);

            workbook.write(Files.newOutputStream(filePath));
            return filePath.toFile();
        }
    }

    @Test
    @DisplayName("测试转换单个 Excel 文件 (.xlsx)")
    void testConvertSingleExcelFile() throws IOException {
        File excelFile = createTestExcelFile("test.xlsx");

        File pdfFile = localFileConverterService.convert(excelFile);

        assertNotNull(pdfFile, "PDF 文件不应为 null");
        assertTrue(pdfFile.exists(), "PDF 文件应已创建");
        assertTrue(pdfFile.getName().startsWith("test"), "PDF 文件名应以 Excel 文件名开头");
        assertTrue(pdfFile.getName().endsWith(".pdf"), "PDF 文件应使用 .pdf 扩展名");
        assertEquals(excelFile.getParentFile(), pdfFile.getParentFile(), "PDF 文件应在同一目录下");
        assertTrue(pdfFile.length() > 0, "PDF 文件应有内容");
    }

    @Test
    @DisplayName("测试转换单个 Excel 文件 (.xls)")
    void testConvertSingleExcelFileXls() throws IOException {
        File excelFile = createTestExcelFileXls("test.xls");

        File pdfFile = localFileConverterService.convert(excelFile);

        assertNotNull(pdfFile, "PDF 文件不应为 null");
        assertTrue(pdfFile.exists(), "PDF 文件应已创建");
        assertTrue(pdfFile.getName().startsWith("test"), "PDF 文件名应以 Excel 文件名开头");
        assertTrue(pdfFile.getName().endsWith(".pdf"), "PDF 文件应使用 .pdf 扩展名");
    }

    @Test
    @DisplayName("测试通过文件路径转换 Excel 文件")
    void testConvertByFilePath() throws IOException {
        File excelFile = createTestExcelFile("test2.xlsx");
        String excelFilePath = excelFile.getAbsolutePath();

        File pdfFile = localFileConverterService.convert(excelFilePath);

        assertNotNull(pdfFile, "PDF 文件不应为 null");
        assertTrue(pdfFile.exists(), "PDF 文件应已创建");
        assertTrue(pdfFile.getName().endsWith(".pdf"), "PDF 文件应使用 .pdf 扩展名");
    }

    @Test
    @DisplayName("测试转换到指定 PDF 路径")
    void testConvertToSpecifiedPath() throws IOException {
        File excelFile = createTestExcelFile("test3.xlsx");
        String pdfPath = tempDir.resolve("custom_name.pdf").toString();

        File pdfFile = localFileConverterService.convert(excelFile.getAbsolutePath(), pdfPath);

        assertNotNull(pdfFile, "PDF 文件不应为 null");
        assertTrue(pdfFile.exists(), "PDF 文件应已创建");
        assertEquals("custom_name.pdf", pdfFile.getName(), "PDF 文件名应与指定路径一致");
    }

    @Test
    @DisplayName("测试批量转换目录下的 Excel 文件")
    void testConvertDirectory() throws IOException {
        createTestExcelFile("file1.xlsx");
        createTestExcelFile("file2.xlsx");
        createTestExcelFile("file3.xlsx");

        List<File> pdfFiles = localFileConverterService.convertDirectory(tempDir.toFile());

        assertNotNull(pdfFiles, "PDF 文件列表不应为 null");
        assertEquals(3, pdfFiles.size(), "应转换 3 个 Excel 文件");

        for (File pdfFile : pdfFiles) {
            assertTrue(pdfFile.exists(), "PDF 文件应已创建: " + pdfFile.getName());
            assertTrue(pdfFile.getName().endsWith(".pdf"), "PDF 文件应使用 .pdf 扩展名");
        }
    }

    @Test
    @DisplayName("测试通过目录路径批量转换")
    void testConvertDirectoryByPath() throws IOException {
        createTestExcelFile("dir_file1.xlsx");
        createTestExcelFile("dir_file2.xlsx");

        List<File> pdfFiles = localFileConverterService.convertDirectory(tempDir.toAbsolutePath().toString());

        assertNotNull(pdfFiles, "PDF 文件列表不应为 null");
        assertEquals(2, pdfFiles.size(), "应转换 2 个 Excel 文件");
    }

    @Test
    @DisplayName("测试递归转换目录及子目录下的 Excel 文件")
    void testConvertDirectoryRecursively() throws IOException {
        // 创建主目录文件
        createTestExcelFile("main_file1.xlsx");
        createTestExcelFile("main_file2.xlsx");

        // 创建子目录并添加文件
        Path subDir1 = Files.createDirectories(tempDir.resolve("subdir1"));
        Path subDir2 = Files.createDirectories(tempDir.resolve("subdir2"));

        createTestExcelFileInDirectory(subDir1, "sub1_file1.xlsx");
        createTestExcelFileInDirectory(subDir2, "sub2_file1.xlsx");
        createTestExcelFileInDirectory(subDir2, "sub2_file2.xlsx");

        List<File> pdfFiles = localFileConverterService.convertDirectoryRecursively(tempDir.toFile());

        assertNotNull(pdfFiles, "PDF 文件列表不应为 null");
        assertEquals(5, pdfFiles.size(), "应转换 5 个 Excel 文件（包括子目录）");
    }

    @Test
    @DisplayName("测试转换空目录")
    void testConvertEmptyDirectory() throws IOException {
        List<File> pdfFiles = localFileConverterService.convertDirectory(tempDir.toFile());

        assertNotNull(pdfFiles, "PDF 文件列表不应为 null");
        assertTrue(pdfFiles.isEmpty(), "空目录不应生成任何 PDF 文件");
    }

    @Test
    @DisplayName("测试只转换 Excel 文件，忽略其他文件")
    void testConvertOnlyExcelFiles() throws IOException {
        createTestExcelFile("excel1.xlsx");
        createTestExcelFile("excel2.xls");
        createNonExcelFile("text.txt");
        createNonExcelFile("data.json");

        List<File> pdfFiles = localFileConverterService.convertDirectory(tempDir.toFile());

        assertNotNull(pdfFiles, "PDF 文件列表不应为 null");
        assertEquals(2, pdfFiles.size(), "应只转换 2 个 Excel 文件");
    }

    @Test
    @DisplayName("测试转换不存在的文件")
    void testConvertNonExistentFile() {
        File nonExistentFile = new File(tempDir.resolve("non_existent.xlsx").toString());

        assertThrows(IOException.class, () -> localFileConverterService.convert(nonExistentFile),
                "转换不存在的文件应抛出 IOException");
    }

    @Test
    @DisplayName("测试转换不存在的目录")
    void testConvertNonExistentDirectory() {
        File nonExistentDir = new File(tempDir.resolve("non_existent_dir").toString());

        assertThrows(IOException.class, () -> localFileConverterService.convertDirectory(nonExistentDir),
                "转换不存在的目录应抛出 IOException");
    }

    @Test
    @DisplayName("测试大文件名转换")
    void testConvertWithLongFileName() throws IOException {
        String longFileName = "this_is_a_very_long_file_name_for_testing_purposes_only_123456789.xlsx";
        File excelFile = createTestExcelFile(longFileName);

        File pdfFile = localFileConverterService.convert(excelFile);

        assertNotNull(pdfFile, "PDF 文件不应为 null");
        assertTrue(pdfFile.exists(), "PDF 文件应已创建");
        assertTrue(pdfFile.getName().startsWith("this_is_a_very_long"), "PDF 文件名应正确");
    }

    @Test
    @DisplayName("测试中文文件名转换")
    void testConvertWithChineseFileName() throws IOException {
        File excelFile = createTestExcelFile("测试文件.xlsx");

        File pdfFile = localFileConverterService.convert(excelFile);

        assertNotNull(pdfFile, "PDF 文件不应为 null");
        assertTrue(pdfFile.exists(), "PDF 文件应已创建");
        assertTrue(pdfFile.getName().startsWith("测试文件"), "PDF 文件名应正确");
    }

    /**
     * 在指定目录创建测试 Excel 文件
     */
    private File createTestExcelFileInDirectory(Path directory, String fileName) throws IOException {
        Path filePath = directory.resolve(fileName);
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("测试Sheet");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("名称");
            headerRow.createCell(1).setCellValue("值");

            org.apache.poi.ss.usermodel.Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("测试数据");
            dataRow.createCell(1).setCellValue(100);

            workbook.write(Files.newOutputStream(filePath));
            return filePath.toFile();
        }
    }

    /**
     * 创建非 Excel 测试文件
     */
    private void createNonExcelFile(String fileName) throws IOException {
        Path filePath = tempDir.resolve(fileName);
        Files.writeString(filePath, "This is not an Excel file");
    }
}
