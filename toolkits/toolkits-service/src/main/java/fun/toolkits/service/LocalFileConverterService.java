package fun.toolkits.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 本地文件转换服务接口
 */
public interface LocalFileConverterService {

    /**
     * 将指定的 Excel 文件转换为同目录下的 PDF 文件
     *
     * @param excelFile Excel 文件
     * @return 生成的 PDF 文件
     * @throws IOException 转换过程中发生的 IO 异常
     */
    File convert(File excelFile) throws IOException;

    /**
     * 将指定的 Excel 文件转换为同目录下的 PDF 文件
     *
     * @param excelFilePath Excel 文件路径
     * @return 生成的 PDF 文件
     * @throws IOException 转换过程中发生的 IO 异常
     */
    File convert(String excelFilePath) throws IOException;

    /**
     * 将指定的 Excel 文件转换为指定的 PDF 文件
     *
     * @param excelFilePath Excel 文件路径
     * @param pdfFilePath  PDF 文件路径
     * @return 生成的 PDF 文件
     * @throws IOException 转换过程中发生的 IO 异常
     */
    File convert(String excelFilePath, String pdfFilePath) throws IOException;

    /**
     * 批量转换目录下的所有 Excel 文件
     *
     * @param directory 目录
     * @return 转换后的 PDF 文件列表
     * @throws IOException 转换过程中发生的 IO 异常
     */
    List<File> convertDirectory(File directory) throws IOException;

    /**
     * 批量转换目录下的所有 Excel 文件
     *
     * @param directoryPath 目录路径
     * @return 转换后的 PDF 文件列表
     * @throws IOException 转换过程中发生的 IO 异常
     */
    List<File> convertDirectory(String directoryPath) throws IOException;

    /**
     * 递归批量转换目录及其子目录下的所有 Excel 文件
     *
     * @param directory 目录
     * @return 转换后的 PDF 文件列表
     * @throws IOException 转换过程中发生的 IO 异常
     */
    List<File> convertDirectoryRecursively(File directory) throws IOException;

    /**
     * 递归批量转换目录及其子目录下的所有 Excel 文件
     *
     * @param directoryPath 目录路径
     * @return 转换后的 PDF 文件列表
     * @throws IOException 转换过程中发生的 IO 异常
     */
    List<File> convertDirectoryRecursively(String directoryPath) throws IOException;
}
