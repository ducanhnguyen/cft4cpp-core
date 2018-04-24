package com.fit.gui.testreport;

import com.fit.gui.testreport.object.IProjectReport;

/**
 * Export a project report to excel file
 *
 * @author ducanhnguyen
 */
public interface IExcelExporter extends IExport {

    /**
     * Export project report to excel
     *
     * @throws Exception
     */
    void exportToExcel() throws Exception;

    /**
     * Get the project report
     *
     * @return
     */
    IProjectReport getProjectReport();

    /**
     * Set the project report
     *
     * @param projectReport
     */
    void setProjectReport(IProjectReport projectReport);

    /**
     * Get the path of excel file
     *
     * @return
     */
    String getExcelFilePath();

    /**
     * Set the path of excel file
     *
     * @param excelFilePath
     */
    void setExcelFilePath(String excelFilePath);
}