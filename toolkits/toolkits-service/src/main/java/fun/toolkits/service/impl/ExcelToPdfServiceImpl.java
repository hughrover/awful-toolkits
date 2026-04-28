package fun.toolkits.service.impl;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import fun.toolkits.service.ExcelToPdfService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Excel 转 PDF 服务实现
 */
@Service
public class ExcelToPdfServiceImpl implements ExcelToPdfService {

    @Override
    public byte[] convert(byte[] excelBytes) throws IOException {
        try (InputStream excelInputStream = new ByteArrayInputStream(excelBytes);
             Workbook workbook = WorkbookFactory.create(excelInputStream)) {
            return convertWorkbook(workbook);
        }
    }

    @Override
    public byte[] convert(InputStream excelInputStream) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(excelInputStream)) {
            return convertWorkbook(workbook);
        }
    }

    @Override
    public void convertAndSave(byte[] excelBytes, String outputPath) throws IOException {
        byte[] pdfBytes = convert(excelBytes);
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(pdfBytes);
        }
    }

    /**
     * 将 Workbook 转换为 PDF
     */
    private byte[] convertWorkbook(Workbook workbook) throws IOException {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(pdfOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // 遍历每个 Sheet
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            // 如果不是第一个 Sheet，添加分页符
            if (sheetIndex > 0) {
                document.add(new Paragraph().setMarginBottom(20));
            }

            // 添加 Sheet 标题
            Paragraph title = new Paragraph(sheet.getSheetName())
                    .setFont(font)
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            // 创建 PDF 表格
            int maxCols = getMaxColumns(sheet);
            Table pdfTable = new Table(UnitValue.createPercentArray(maxCols)).useAllAvailableWidth();

            // 处理合并单元格
            processSheetWithMergedCells(sheet, pdfTable, font);

            document.add(pdfTable);
        }

        document.close();
        return pdfOutputStream.toByteArray();
    }

    /**
     * 获取 Sheet 中最大列数
     */
    private int getMaxColumns(Sheet sheet) {
        int maxCols = 0;
        for (Row row : sheet) {
            if (row.getLastCellNum() > maxCols) {
                maxCols = row.getLastCellNum();
            }
        }
        return maxCols;
    }

    /**
     * 处理带有合并单元格的 Sheet
     */
    private void processSheetWithMergedCells(Sheet sheet, Table pdfTable, PdfFont font) {
        int maxCols = getMaxColumns(sheet);
        int maxRows = sheet.getLastRowNum() + 1;

        // 创建一个标记数组，用于记录哪些单元格已经被合并过
        boolean[][] mergedCells = new boolean[maxRows][maxCols];

        // 处理合并单元格
        for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
            int firstRow = mergedRegion.getFirstRow();
            int lastRow = mergedRegion.getLastRow();
            int firstCol = mergedRegion.getFirstColumn();
            int lastCol = mergedRegion.getLastColumn();

            Row row = sheet.getRow(firstRow);
            if (row == null) {
                continue;
            }
            Cell excelCell = row.getCell(firstCol);
            if (excelCell == null) {
                continue;
            }

            String cellValue = getCellValueAsString(excelCell);

            // 创建合并的单元格
            com.itextpdf.layout.element.Cell pdfCell = new com.itextpdf.layout.element.Cell(lastRow - firstRow + 1, lastCol - firstCol + 1)
                    .add(new Paragraph(cellValue).setFont(font))
                    .setFont(font);

            // 设置边框
            setCellStyle(pdfCell, excelCell);

            pdfTable.addCell(pdfCell);

            // 标记这些单元格已经被合并过
            for (int i = firstRow; i <= lastRow; i++) {
                for (int j = firstCol; j <= lastCol; j++) {
                    mergedCells[i][j] = true;
                }
            }
        }

        // 处理未合并的单元格
        for (int rowIndex = 0; rowIndex < maxRows; rowIndex++) {
            for (int colIndex = 0; colIndex < maxCols; colIndex++) {
                if (mergedCells[rowIndex][colIndex]) {
                    continue;
                }

                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    pdfTable.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("").setFont(font)));
                    continue;
                }

                Cell excelCell = row.getCell(colIndex);
                if (excelCell == null) {
                    pdfTable.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("").setFont(font)));
                    continue;
                }

                String cellValue = getCellValueAsString(excelCell);
                com.itextpdf.layout.element.Cell pdfCell = new com.itextpdf.layout.element.Cell().add(new Paragraph(cellValue).setFont(font));
                setCellStyle(pdfCell, excelCell);
                pdfTable.addCell(pdfCell);
            }
        }
    }

    /**
     * 设置单元格样式
     */
    private void setCellStyle(com.itextpdf.layout.element.Cell pdfCell, Cell excelCell) {
        pdfCell.setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);

        CellStyle cellStyle = excelCell.getCellStyle();

        org.apache.poi.ss.usermodel.Font font = excelCell.getSheet().getWorkbook().createFont();
        if (font.getBold()) {
            pdfCell.setBold();
        }

        // 第一行作为标题行，设置背景色
        if (excelCell.getRowIndex() == 0) {
            pdfCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        }
    }

    /**
     * 获取单元格的值并转换为字符串
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> getFormulaValue(cell);
            case BLANK -> "";
            default -> "";
        };
    }

    /**
     * 获取公式单元格的值
     */
    private String getFormulaValue(Cell cell) {
        CellType resultType = cell.getCachedFormulaResultType();
        return switch (resultType) {
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> cell.getCellFormula();
        };
    }
}
