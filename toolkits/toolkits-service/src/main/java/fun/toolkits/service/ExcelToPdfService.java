package fun.toolkits.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel 转 PDF 服务接口
 */
public interface ExcelToPdfService {

    /**
     * 将 Excel 文件转换为 PDF 文件
     *
     * @param excelBytes Excel 文件的字节数组
     * @return PDF 文件的字节数组
     * @throws IOException 转换过程中发生的 IO 异常
     */
    byte[] convert(byte[] excelBytes) throws IOException;

    /**
     * 将 Excel 输入流转换为 PDF 文件
     *
     * @param excelInputStream Excel 文件输入流
     * @return PDF 文件的字节数组
     * @throws IOException 转换过程中发生的 IO 异常
     */
    byte[] convert(InputStream excelInputStream) throws IOException;

    /**
     * 将 Excel 文件转换为 PDF 文件并保存到指定路径
     *
     * @param excelBytes  Excel 文件的字节数组
     * @param outputPath  PDF 输出文件路径
     * @throws IOException 转换过程中发生的 IO 异常
     */
    void convertAndSave(byte[] excelBytes, String outputPath) throws IOException;
}
