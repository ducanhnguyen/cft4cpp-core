package com.fit.gui.testedfunctions;

import com.fit.gui.testreport.object.IProjectReport;
import com.fit.gui.testreport.object.ITestedFunctionReport;
import com.fit.tree.object.FunctionNode;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;

public interface IContentPanelDisplay {

    String ID = "ID";
    String FUNCTION = "Function";
    String STATE = "State";
    String TEST_PATH = "Test path";
    String INPUT = "Input";
    String EXPECTED_OUTPUT = "Expected Output/ Exception";
    String PASS = "Pass";
    float FONT_SIZE = 13;
    Vector<Object> DETAIL_TABLE_HEADER = new Vector<>(
            Arrays.asList(IContentPanelDisplay.ID, IContentPanelDisplay.TEST_PATH, IContentPanelDisplay.INPUT,
                    IContentPanelDisplay.EXPECTED_OUTPUT, IContentPanelDisplay.PASS));

    boolean checkResized(double[] a1);

    FunctionNode getFunctionNodeSelectedNow(int selectedRowOfListTable) throws Exception;

    ITestedFunctionReport getFunctionReportSelectedNow(int selectedRowOfListTable) throws Exception;

    DetailFunctionTable getDetailFunctionTable();

    void setDetailFunctionTable(DetailFunctionTable detailFunctionTable);

    ExpectedOutputPanel getExpectedOutputPanel();

    void setExpectedOutputPanel(ExpectedOutputPanel expectedOutputPanel);

    ListFunctionTable getListFunctionTable();

    void setListFunctionTable(ListFunctionTable listFunctionTable);

    int getSelectingRow();

    void setSelectingRow(int selectingRow);

    int getSelectingRowIdDetailTable();

    void setSelectingRowIdDetailTable(int selectingRowIdDetailTable);

    // Insert data into Tables
    void insertData(IProjectReport _projectReport);

    /**
     * Insert data
     *
     * @param data
     */
    void insertToListFunctions(Vector<Object> data);

    void mouseClicked(MouseEvent e);

    void mouseEntered(MouseEvent e);

    void mouseExited(MouseEvent e);

    void mousePressed(MouseEvent e);

    void mouseReleased(MouseEvent e);

    void refresh();

    /**
     * remove function in table
     *
     * @param listIndex
     */
    void removeFunction(int[] listIndex);

    /**
     * resume function in table
     *
     * @param listIndex
     */
    void resumeFunction(int[] listIndex);

    /**
     * Save Expected Output Table of a test path
     */
    void saveEOTableDataOfTestPath();

    /**
     * Set data for detail function Table
     *
     * @param data
     */
    void setDataDetailTable(Vector<Vector<Object>> data);

    void setDividerLocationForSplitPanes();

    void setSizeSaveBeforeForComponents();

    void setSubmitData();

    /**
     * stop function in table
     *
     * @param listIndex
     */
    void stopFunction(int[] listIndex);

    void viewDetailFunction();

    void viewEOTable();

    // View expected output table
    void viewExpectedOutput(int selectingRowID, int testPathIndex) throws Exception;

    //
    void viewTestPathAndInput(int selectingRowID) throws Exception;

    void writeSaveData();

    JButton getBtnExport();

    void setBtnExport(JButton btnExport);

    JButton getBtnExportUnitTest();

    void setBtnExportUnitTest(JButton btnExportUnitTest);

}