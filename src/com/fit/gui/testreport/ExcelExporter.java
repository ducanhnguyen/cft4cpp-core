package com.fit.gui.testreport;

import com.fit.gui.testreport.object.IProjectReport;
import com.fit.gui.testreport.object.ISourcecodeFileReport;
import com.fit.gui.testreport.object.ITestedFunctionReport;
import com.fit.gui.testreport.object.ITestpathReport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Export project report to excel
 *
 * @author DucAnh
 */
public class ExcelExporter implements IExcelExporter {

    /**
     * số dòng ô input (default = 3)
     */
    private int numberOfInputLine = 3;

    /**
     * so dong o output (default = 3)
     */
    private int numberOfOutputLine = 3;

    /**
     * Biến đếm số dòng đã ghi
     */
    private int totalLine = 0;

    private int totalInExternal = 0;

    private int extraLineBaseOnInEx = 0;

    private XSSFWorkbook workbook;

    private Sheet sheet;

    private Row row;

    private Cell cell;

    private IProjectReport projectReport;

    private String excelFilePath;

    /**
     * @param projectReport Đối tượng chứa dữ liệu cần xuất ra tệp excel
     * @param outputPath    Đường dẫn lưu tệp excel đầu ra
     * @throws IOException
     */
    public ExcelExporter(IProjectReport projectReport, String outputPath) throws Exception {
        workbook = new XSSFWorkbook();
        this.projectReport = projectReport;
        excelFilePath = outputPath;
    }

    protected CellStyle createStyle(boolean bold, boolean top, boolean bottom, boolean left, boolean right,
                                    boolean alignment, boolean wrap) {
        CellStyle style = getWorkBook().createCellStyle();

        if (bold) {
            Font f = workbook.createFont();
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);
            f.setBold(true);
            style.setFont(f);
        }

        if (top)
            style.setBorderTop(CellStyle.BORDER_THIN);

        if (bottom)
            style.setBorderBottom(CellStyle.BORDER_THIN);

        if (left)
            style.setBorderLeft(CellStyle.BORDER_THIN);

        if (right)
            style.setBorderRight(CellStyle.BORDER_THIN);

        if (alignment)
            style.setAlignment(CellStyle.ALIGN_CENTER);

        if (wrap)
            style.setWrapText(true);

        return style;
    }

    protected XSSFWorkbook getWorkBook() {
        return workbook;
    }

    @Override
    public void exportToExcel() throws Exception {
        if (projectReport != null && excelFilePath != null) {

            CellStyle _BOLD = createStyle(true, false, false, false, false, false, true);
            CellStyle _BORDER[] = new CellStyle[3];
            _BORDER[0] = createStyle(true, true, true, true, true, true, true);
            _BORDER[1] = createStyle(false, false, true, false, false, false, true);
            _BORDER[2] = createStyle(false, false, false, false, false, false, true);

            // Lấy danh sách tệp
            for (ISourcecodeFileReport sourcecodeFile : projectReport.getSourcecodeFiles()) {

                String nameSheet = sourcecodeFile.getName();
                sheet = workbook.createSheet(nameSheet);

                // style for sheet
                int[] colsWidth = {700, 8000, 3000, 4000, 3000, 3000, 3000, 4000, 4000, 3000, 3000, 3000, 4000, 3000};
                for (int pos = 0; pos < colsWidth.length; pos++)
                    sheet.setColumnWidth(pos, colsWidth[pos]);

                numberOfInputLine = 3;
                numberOfOutputLine = 3;
                totalLine = 0;

                // Lấy danh sách các hàm trong một tệp
                for (ITestedFunctionReport function : sourcecodeFile.getTestedFunctionReports()) {

                    // Thông tin của hàm ở đây
                    row = sheet.createRow(0 + totalLine);
                    cell = row.createCell(0);
                    cell.setCellValue(function.getName());
                    cell.setCellStyle(_BOLD);

                    row = sheet.createRow(1 + totalLine);
                    cell = row.createCell(0);
                    cell.setCellValue("Num of testcases: " + function.getNumTestcase());
                    cell.setCellStyle(_BORDER[2]);

                    row = sheet.createRow(2 + totalLine);
                    cell = row.createCell(0);
                    cell.setCellValue("Coverage: " + function.computeCoverage() + "%");
                    cell.setCellStyle(_BORDER[2]);

                    row = sheet.createRow(3 + totalLine);
                    cell = row.createCell(0);
                    cell.setCellValue("Coverage Critertion: " + function.getCoverageCritertion());
                    cell.setCellStyle(_BORDER[2]);

                    // ... Khởi tạo danh sách tiêu đề cho 1 function
                    row = sheet.createRow(5 + totalLine);
                    cell = row.createCell(0);
                    cell.setCellValue("ID");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(1);
                    cell.setCellValue("Test path");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(2);
                    cell.setCellValue("Input");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(13);
                    cell.setCellValue("Pass");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(3);
                    cell.setCellValue("Expected Output");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(8);
                    cell.setCellValue("Actual Output");
                    cell.setCellStyle(_BORDER[0]);

                    row = sheet.createRow(6 + totalLine);
                    cell = row.createCell(3);
                    cell.setCellValue("Internal/ External Calls");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(7);
                    cell.setCellValue("Expected return/ Exception");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(8);
                    cell.setCellValue("Internal/ External Calls");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(12);
                    cell.setCellValue("Actual return/ Exception");
                    cell.setCellStyle(_BORDER[0]);

                    row = sheet.createRow(7 + totalLine);
                    cell = row.createCell(3);
                    cell.setCellValue("Name");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(4);
                    cell.setCellValue("In");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(5);
                    cell.setCellValue("Out");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(6);
                    cell.setCellValue("Expected return");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(8);
                    cell.setCellValue("Name");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(9);
                    cell.setCellValue("In");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(10);
                    cell.setCellValue("Out");
                    cell.setCellStyle(_BORDER[0]);

                    cell = row.createCell(11);
                    cell.setCellValue("Actual return");
                    cell.setCellStyle(_BORDER[0]);

                    /**
                     * iter: id cho từng testpath totalInput: biến đếm số dòng ở
                     * cột input đã được nhập ; gia tri totalInput se phu thuoc
                     * vao gia tri cua numbersOfOutput. khi numberOfOutputLine
                     * lon hon thi totalinput se cong voi numberOfOutputLine
                     */
                    int iter = 0;
                    int totalInput = 0;

                    // Lấy danh sách test path được lưu trong bảng
                    // Nội dung của bảng
                    // Vòng for 1 chạy các test path
                    for (ITestpathReport testpathReport : function.getTestpaths()) {
                        row = sheet.createRow(8 + totalInput + totalLine);
                        cell = row.createCell(0); // ID
                        cell.setCellValue(iter + 1);
                        cell.setCellStyle(_BORDER[2]);

                        cell = row.createCell(1); // Test path
                        cell.setCellValue(testpathReport.getNormalizedTestpath());
                        cell.setCellStyle(_BORDER[2]);

                        int numberOfOutputLineReal = testpathReport.getActualOutput().getCalls().size();

                        // Vòng for 2 chạy các output, chạy các hàm internal
                        // hoặc
                        // external
                        for (int i = 0; i < numberOfOutputLineReal; i++) {
                            // Khởi tạo dòng đầu tiên của hàm trong file excel
                            if (sheet.getRow(8 + totalInput + totalLine + totalInExternal + i) == null)
                                row = sheet.createRow(8 + totalInput + totalLine + totalInExternal + i);
                            else
                                row = sheet.getRow(8 + totalInput + totalLine + totalInExternal + i);
                            if (testpathReport.getExpectedOutput().getCalls().isEmpty() == false) {
                                cell = row.createCell(3); // expected
                                // Name
                                cell.setCellValue(testpathReport.getExpectedOutput().getCalls().get(i).getName());
                                cell.setCellStyle(_BORDER[2]);

                                cell = row.createCell(8); // actual
                                // Name
                                cell.setCellValue(testpathReport.getActualOutput().getCalls().get(i).getName());
                                cell.setCellStyle(_BORDER[2]);

                                cell = row.createCell(6); // expected
                                // Return
                                cell.setCellValue(
                                        testpathReport.getExpectedOutput().getCalls().get(i).getReturnValue());
                                cell.setCellStyle(_BORDER[2]);

                                cell = row.createCell(11); // expected
                                // Return
                                cell.setCellValue(testpathReport.getActualOutput().getCalls().get(i).getReturnValue());
                                cell.setCellStyle(_BORDER[2]);

                                // In
                                String in = "";
                                // Số lượng danh sách các input của 1 hàm
                                // internal
                                int sizeIn = testpathReport.getActualOutput().getCalls().get(i).getIn().size();
                                // Vòng for 3 chạy liệt kê danh sách các input
                                // của 1
                                // hàm internal
                                for (int j = 0; j < sizeIn; j++) {
                                    if (sheet.getRow(8 + totalInput + totalLine + totalInExternal + i + j) == null)
                                        row = sheet.createRow(8 + totalInput + totalLine + totalInExternal + i + j);
                                    else
                                        row = sheet.getRow(8 + totalInput + totalLine + totalInExternal + i + j);

                                    in = testpathReport.getExpectedOutput().getCalls().get(i).getIn().get(j);
                                    cell = row.createCell(4); // expected
                                    // in
                                    cell.setCellValue(in);
                                    cell.setCellStyle(_BORDER[2]);

                                    in = testpathReport.getActualOutput().getCalls().get(i).getIn().get(j);
                                    cell = row.createCell(9); // actual
                                    // in
                                    cell.setCellValue(in);
                                    cell.setCellStyle(_BORDER[2]);
                                    // End for 3
                                }

                                String out = "";
                                int sizeOut = testpathReport.getActualOutput().getCalls().get(i).getOut().size();
                                // Vòng for 3 chạy liệt kê danh sách các onput
                                // của 1
                                // hàm internal
                                for (int j = 0; j < sizeOut; j++) {
                                    if (sheet.getRow(8 + totalInput + totalLine + totalInExternal + i + j) == null)
                                        row = sheet.createRow(8 + totalInput + totalLine + totalInExternal + i + j);
                                    else
                                        row = sheet.getRow(8 + totalInput + totalLine + totalInExternal + i + j);

                                    out = testpathReport.getExpectedOutput().getCalls().get(i).getOut().get(j);
                                    cell = row.createCell(5); // expected
                                    // out
                                    cell.setCellValue(out);
                                    cell.setCellStyle(_BORDER[2]);

                                    out = testpathReport.getActualOutput().getCalls().get(i).getOut().get(j);
                                    cell = row.createCell(10); // actual
                                    // out
                                    cell.setCellValue(out);
                                    cell.setCellStyle(_BORDER[2]);
                                    // End for 3

                                }

                                // tao border cho tung internal external calls
                                for (int pos = 3; pos < 12; pos++) {
                                    if (pos == 7)
                                        continue;
                                    setStyleForCellRange(8 + totalInput + totalLine + totalInExternal + i,
                                            8 + totalInput + totalLine + totalInExternal + i + sizeIn - 1, pos, pos,
                                            sheet);
                                }

                                // merge o name va o expected/actual return
                                if (sizeIn > 1) {
                                    sheet.addMergedRegion(new CellRangeAddress(
                                            8 + totalInput + totalLine + totalInExternal + i,
                                            8 + totalInput + totalLine + totalInExternal + i + sizeIn - 1, 3, 3)); // expected
                                    // name
                                    sheet.addMergedRegion(new CellRangeAddress(
                                            8 + totalInput + totalLine + totalInExternal + i,
                                            8 + totalInput + totalLine + totalInExternal + i + sizeIn - 1, 6, 6)); // expected
                                    // return
                                    sheet.addMergedRegion(new CellRangeAddress(
                                            8 + totalInput + totalLine + totalInExternal + i,
                                            8 + totalInput + totalLine + totalInExternal + i + sizeIn - 1, 8, 8)); // actual
                                    // name
                                    sheet.addMergedRegion(new CellRangeAddress(
                                            8 + totalInput + totalLine + totalInExternal + i,
                                            8 + totalInput + totalLine + totalInExternal + i + sizeIn - 1, 11, 11)); // actual
                                    // return
                                }
                                // Lưu số lượng input hàm internal trước đó để
                                // tính
                                // vị trí hàm internal sau đó
                                totalInExternal = sizeIn - 1;

                                // Tính tổng tất cả các extra Line sau khi xuất
                                // xong
                                // 1 testpath
                                extraLineBaseOnInEx += totalInExternal;
                                // End if
                            }
                            // End for 2
                        }

                        if (numberOfOutputLineReal + extraLineBaseOnInEx > 3)
                            numberOfOutputLine = numberOfOutputLineReal + extraLineBaseOnInEx;
                        else
                            numberOfOutputLine = 3;

                        extraLineBaseOnInEx = 0;
                        // Trả lại giá trị mặc định totalInExternal
                        totalInExternal = 0;

                        row = sheet.getRow(8 + totalInput + totalLine);

                        cell = row.createCell(7); // Expected return
                        // for test
                        // path
                        cell.setCellValue(testpathReport.getExpectedOutput().getExpectedValues().getInputforUI());
                        cell.setCellStyle(_BORDER[2]);

                        cell = row.createCell(12); // actual return
                        // for test
                        // path
                        cell.setCellValue(testpathReport.getActualOutput().getExpectedValues().getInputforUI());
                        cell.setCellStyle(_BORDER[2]);

                        cell = row.createCell(13); // Pass
                        cell.setCellValue(testpathReport.getPass());
                        cell.setCellStyle(_BORDER[2]);

                        // Danh sách của cột input
                        // Nếu số lượng input> 3 biến numberOfInputLine = input,
                        // ngược lại default là 3
                        int numberOfInputLineReal = testpathReport.getInput().getVariablesForDisplay().size();
                        if (numberOfInputLineReal > 3)
                            numberOfInputLine = numberOfInputLineReal;
                        else
                            numberOfInputLine = 3;

                        for (int pos = 0; pos < numberOfInputLineReal; pos++) {

                            row = sheet.getRow(8 + pos + totalInput + totalLine);
                            if (row == null)
                                row = sheet.createRow(8 + pos + totalInput + totalLine);

                            cell = row.createCell(2); // Input
                            cell.setCellValue(testpathReport.getInput().getVariablesForDisplay().get(pos));
                            cell.setCellStyle(_BORDER[2]);
                        }

                        // merge o testpathReport
                        if (numberOfOutputLine > numberOfInputLine) {
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfOutputLine - 1, 0, 0));
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfOutputLine - 1, 1, 1));
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfOutputLine - 1, 7, 7));
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfOutputLine - 1, 12, 12));
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfOutputLine - 1, 13, 13));

                            for (int pos = 0; pos < 14; pos++)
                                setStyleForCellRange(8 + totalInput + totalLine,
                                        8 + totalInput + totalLine + numberOfOutputLine - 1, pos, pos, sheet);
                        } else {
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfInputLine - 1, 0, 0));
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfInputLine - 1, 1, 1));
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfInputLine - 1, 7, 7));
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfInputLine - 1, 12, 12));
                            sheet.addMergedRegion(new CellRangeAddress(8 + totalInput + totalLine,
                                    8 + totalInput + totalLine + numberOfInputLine - 1, 13, 13));

                            for (int pos = 0; pos < 14; pos++)
                                setStyleForCellRange(8 + totalInput + totalLine,
                                        8 + totalInput + totalLine + numberOfInputLine - 1, pos, pos, sheet);
                        }
                        // Thay đổi giá trị biến
                        if (numberOfOutputLine > numberOfInputLine)
                            totalInput += numberOfOutputLine;
                        else
                            totalInput += numberOfInputLine;
                        iter++;
                        // End for 1 sau khi chạy xong 1 test path
                    }

                    // Merge region, tất cả hàm merge được viết ở đây
                    sheet.addMergedRegion(new CellRangeAddress(0 + totalLine, 0 + totalLine, 0, 10)); // function
                    sheet.addMergedRegion(new CellRangeAddress(1 + totalLine, 1 + totalLine, 0, 1)); // NumTestcase
                    sheet.addMergedRegion(new CellRangeAddress(2 + totalLine, 2 + totalLine, 0, 1)); // Coverage
                    sheet.addMergedRegion(new CellRangeAddress(3 + totalLine, 3 + totalLine, 0, 1)); // CoverageCritertion

                    sheet.addMergedRegion(new CellRangeAddress(5 + totalLine, 7 + totalLine, 0, 0)); // ID
                    setStyleForCellRange(5 + totalLine, 7 + totalLine, 0, 0, sheet);

                    sheet.addMergedRegion(new CellRangeAddress(5 + totalLine, 7 + totalLine, 1, 1)); // Test
                    // path
                    setStyleForCellRange(5 + totalLine, 7 + totalLine, 1, 1, sheet);

                    sheet.addMergedRegion(new CellRangeAddress(5 + totalLine, 7 + totalLine, 2, 2)); // Input
                    setStyleForCellRange(5 + totalLine, 7 + totalLine, 2, 2, sheet);

                    sheet.addMergedRegion(new CellRangeAddress(5 + totalLine, 5 + totalLine, 3, 7)); // Expected
                    // Output
                    setStyleForCellRange(5 + totalLine, 5 + totalLine, 3, 7, sheet);

                    sheet.addMergedRegion(new CellRangeAddress(5 + totalLine, 5 + totalLine, 8, 12)); // Actual
                    // Output
                    setStyleForCellRange(5 + totalLine, 5 + totalLine, 8, 12, sheet);

                    sheet.addMergedRegion(new CellRangeAddress(6 + totalLine, 6 + totalLine, 3, 6)); // Internal
                    // expected
                    sheet.addMergedRegion(new CellRangeAddress(6 + totalLine, 6 + totalLine, 8, 11)); // Internal
                    // actual

                    sheet.addMergedRegion(new CellRangeAddress(6 + totalLine, 7 + totalLine, 7, 7)); // Expected

                    setStyleForCellRange(6 + totalLine, 7 + totalLine, 7, 7, sheet);

                    sheet.addMergedRegion(new CellRangeAddress(6 + totalLine, 7 + totalLine, 12, 12)); // Actual

                    setStyleForCellRange(6 + totalLine, 7 + totalLine, 12, 12, sheet);

                    sheet.addMergedRegion(new CellRangeAddress(5 + totalLine, 7 + totalLine, 13, 13)); // Pass
                    setStyleForCellRange(5 + totalLine, 7 + totalLine, 13, 13, sheet);

                    // Thay đổi giá trị totalLine
                    totalLine += 9;
                    totalLine += totalInput;
                    // End for 0 chạy các hàm
                }

                // End base for chạy danh sách các file
            }

            CellStyle cellstyle = null;

            // Chinh lai fillbackground color cho tat ca cac o
            for (int pos = 0; pos < totalLine; pos++) {
                row = sheet.getRow(pos);
                if (row == null)
                    continue;
                for (int sec = 0; sec < 14; sec++) {
                    cell = row.getCell(sec);
                    if (cell == null)
                        continue;
                    cellstyle = cell.getCellStyle();
                    cellstyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                }
            }

            // Create file system using specific name
            if (new File(excelFilePath).isDirectory())
                excelFilePath += File.separator + "Export.xlsx";

            FileOutputStream out = new FileOutputStream(new File(excelFilePath));
            // write operation workbook using file out object
            workbook.write(out);
            out.close();
        } else
            throw new Exception("Cannot export to excel");
    }

    protected void setStyleForCellRange(int rowBegin, int rowEnd, int colBegin, int colEnd, Sheet sheet) {
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, new CellRangeAddress(rowBegin, rowEnd, colBegin, colEnd),
                sheet, workbook);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, new CellRangeAddress(rowBegin, rowEnd, colBegin, colEnd), sheet,
                workbook);
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, new CellRangeAddress(rowBegin, rowEnd, colBegin, colEnd),
                sheet, workbook);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, new CellRangeAddress(rowBegin, rowEnd, colBegin, colEnd), sheet,
                workbook);
    }

    @Override
    public IProjectReport getProjectReport() {
        return projectReport;
    }

    @Override
    public void setProjectReport(IProjectReport projectReport) {
        this.projectReport = projectReport;
    }

    @Override
    public String getExcelFilePath() {
        return excelFilePath;
    }

    @Override
    public void setExcelFilePath(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }
}
