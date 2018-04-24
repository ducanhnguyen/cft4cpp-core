package com.fit.gui.testedfunctions;

import com.fit.gui.testreport.object.IProjectReport;

public interface IManageSelectedFunctionsDisplayer {

    void changeState();

    /**
     * Export to Excel file
     */
    void exportExcelFile();

    /**
     * Export to unit test
     */
    void exportUnitTest();

    ContentPanelDisplay getContentPane();

    String getExpectedOutputData();

    /**
     * refresh the main view
     */
    void refresh();

    void restoreDataSave();

    /**
     * Set look and feel for JFrame
     *
     * @param nameLookAndFeel
     */
    void setLookAndFeel(String nameLookAndFeel);

    void setProjectReport(IProjectReport input);

    void writeSaveData();

}