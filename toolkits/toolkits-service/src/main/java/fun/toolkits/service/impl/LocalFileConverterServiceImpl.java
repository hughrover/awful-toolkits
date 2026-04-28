package fun.toolkits.service.impl;

import fun.toolkits.service.ExcelToPdfService;
import fun.toolkits.service.LocalFileConverterService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 本地文件转换服务实现
 */
@Service
public class LocalFileConverterServiceImpl implements LocalFileConverterService {

    private final ExcelToPdfService excelToPdfService;

    public LocalFileConverterServiceImpl(ExcelToPdfService excelToPdfService) {
        this.excelToPdfService = excelToPdfService;
    }

    @Override
    public File convert(File excelFile) throws IOException {
        validateExcelFile(excelFile);

        String pdfPath = getPdfFilePath(excelFile.getAbsolutePath());
        excelToPdfService.convertAndSave(Files.readAllBytes(excelFile.toPath()), pdfPath);

        return new File(pdfPath);
    }

    @Override
    public File convert(String excelFilePath) throws IOException {
        return convert(new File(excelFilePath));
    }

    @Override
    public File convert(String excelFilePath, String pdfFilePath) throws IOException {
        File excelFile = new File(excelFilePath);
        validateExcelFile(excelFile);

        excelToPdfService.convertAndSave(Files.readAllBytes(excelFile.toPath()), pdfFilePath);

        return new File(pdfFilePath);
    }

    @Override
    public List<File> convertDirectory(File directory) throws IOException {
        validateDirectory(directory);

        List<File> pdfFiles = new ArrayList<>();

        try (Stream<Path> paths = Files.list(directory.toPath())) {
            paths.filter(this::isExcelFile)
                 .map(Path::toFile)
                 .forEach(excelFile -> {
                     try {
                         File pdfFile = convert(excelFile);
                         pdfFiles.add(pdfFile);
                     } catch (IOException e) {
                         throw new RuntimeException("转换文件失败: " + excelFile.getName(), e);
                     }
                 });
        }

        return pdfFiles;
    }

    @Override
    public List<File> convertDirectory(String directoryPath) throws IOException {
        return convertDirectory(new File(directoryPath));
    }

    @Override
    public List<File> convertDirectoryRecursively(File directory) throws IOException {
        validateDirectory(directory);

        List<File> pdfFiles = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(directory.toPath())) {
            paths.filter(Files::isRegularFile)
                 .filter(this::isExcelFile)
                 .map(Path::toFile)
                 .forEach(excelFile -> {
                     try {
                         File pdfFile = convert(excelFile);
                         pdfFiles.add(pdfFile);
                     } catch (IOException e) {
                         throw new RuntimeException("转换文件失败: " + excelFile.getName(), e);
                     }
                 });
        }

        return pdfFiles;
    }

    @Override
    public List<File> convertDirectoryRecursively(String directoryPath) throws IOException {
        return convertDirectoryRecursively(new File(directoryPath));
    }

    /**
     * 验证 Excel 文件是否存在且可读
     */
    private void validateExcelFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("文件不存在: " + file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new IOException("不是有效的文件: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IOException("文件不可读: " + file.getAbsolutePath());
        }
        if (!isExcelFile(file.toPath())) {
            throw new IOException("不是 Excel 文件: " + file.getAbsolutePath());
        }
    }

    /**
     * 验证目录是否存在且可读
     */
    private void validateDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            throw new IOException("目录不存在: " + directory.getAbsolutePath());
        }
        if (!directory.isDirectory()) {
            throw new IOException("不是有效的目录: " + directory.getAbsolutePath());
        }
        if (!directory.canRead()) {
            throw new IOException("目录不可读: " + directory.getAbsolutePath());
        }
    }

    /**
     * 判断是否为 Excel 文件
     */
    private boolean isExcelFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        return fileName.endsWith(".xlsx") || fileName.endsWith(".xls");
    }

    /**
     * 根据 Excel 文件路径生成对应的 PDF 文件路径
     */
    private String getPdfFilePath(String excelFilePath) {
        if (excelFilePath.toLowerCase().endsWith(".xlsx")) {
            return excelFilePath.substring(0, excelFilePath.length() - 5) + ".pdf";
        } else if (excelFilePath.toLowerCase().endsWith(".xls")) {
            return excelFilePath.substring(0, excelFilePath.length() - 4) + ".pdf";
        } else {
            return excelFilePath + ".pdf";
        }
    }

    /**
     * 主函数：将本地 Excel 文件转换为 PDF 文件
     * 使用方式：java LocalFileConverterServiceImpl <excel文件路径> [pdf文件路径]
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("使用方式：java LocalFileConverterServiceImpl <excel文件路径> [pdf文件路径]");
            System.out.println("示例：java LocalFileConverterServiceImpl /path/to/data.xlsx");
            System.out.println("      java LocalFileConverterServiceImpl /path/to/data.xlsx /path/to/output.pdf");
            System.exit(1);
        }

        String excelPath = args[0];
        String pdfPath = args.length > 1 ? args[1] : null;

        ExcelToPdfService excelToPdfService = new ExcelToPdfServiceImpl();
        LocalFileConverterService converter = new LocalFileConverterServiceImpl(excelToPdfService);

        try {
            File pdfFile;
            if (pdfPath != null && !pdfPath.isEmpty()) {
                pdfFile = converter.convert(excelPath, pdfPath);
            } else {
                pdfFile = converter.convert(excelPath);
            }

            System.out.println("转换成功！");
            System.out.println("Excel 文件: " + excelPath);
            System.out.println("PDF 文件:   " + pdfFile.getAbsolutePath());
            System.out.println("文件大小:   " + pdfFile.length() + " 字节");
        } catch (IOException e) {
            System.err.println("转换失败: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
