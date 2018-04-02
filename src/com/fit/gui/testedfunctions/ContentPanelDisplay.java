package com.fit.gui.testedfunctions;

import com.alee.laf.tabbedpane.WebTabbedPane;
import com.fit.config.AbstractSetting;
import com.fit.config.IFunctionConfig;
import com.fit.gui.main.GUIView;
import com.fit.gui.main.ImageConstant;
import com.fit.gui.testedfunctions.ExpectedOutputTableConfig.RowConfig;
import com.fit.gui.testreport.object.*;
import com.fit.testdatagen.ThreadManager;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.IVariableNode;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @author Duong Td
 */
public class ContentPanelDisplay extends JPanel implements MouseListener, IContentPanelDisplay {
    private static final long serialVersionUID = 1L;
    private static final int MAX_CHAR_OF_TEST_PATH = 100;

    /**
     * Singleton pattern
     */
    private static IContentPanelDisplay instance = null;
    double[] detailTableColumnsSizeOld, detailTableColumnsSizeNew;
    // Row selecting in List Function Table
    private int selectingRowListTable = -1;
    // ID row selecting in Detail FunctionTable
    private int selectingRowIdDetailTable = -1;
    private JPanel listFunctionPanel, detailFunctionPanel;
    // 2 table
    private ListFunctionTable listFunctionTable;
    private DetailFunctionTable detailFunctionTable;
    private JPanel testPathAndInputPanel;
    private ExpectedOutputPanel expectedOutputPanel;
    private JTextArea txtTestPath;
    private JTextArea txtInput;
    private JSplitPane splitPanel3;
    private JSplitPane splitPanel;
    private JScrollPane scrollPanelDetail, scrollPanelList;
    private JSplitPane splitPanel2;
    // Other component at the bottom of detail function panel
    private JPanel endDetailPanel;
    private JButton btnExport, btnExportUnitTest;
    private JLabel lblCoverage;
    // Other component at the bottom of list function panel
    private JPanel endListPanel;
    private JProgressBar progressBar;
    private IProjectReport projectReport;
    private MyPopUpMenu myPopUpMenu;
    private WebTabbedPane webTabbedPane;
    private int testPathSelectedNow = -1, testPathSelectedBefore = -1;
    // To save Expected Output Table have data before
    private List<ExpectedOutputTableConfig> listEOTableConfig = new ArrayList<>();
    private Vector<Vector<Vector<Object>>> detailDatas = new Vector<>();
    private int countTestedFunction = 0;
    private int countTotalFunction = 0;
    private ArrayList<Integer> indexFile;
    private ArrayList<Integer> indexFunction;
    private ArrayList<Integer> indexTestPath;
    private List<Boolean> listFunctionsStoppedTesting;
    // selection = 0: select by row
    // selection = 1: select by cell
    private int detailTableSelectionType = 0;

    public ContentPanelDisplay() {
    }

    public ContentPanelDisplay(int frameWidth, int frameHeight) {
        super(new BorderLayout());

        listFunctionTable = new ListFunctionTable(new Vector<>(Arrays.asList(
                new Object[]{IContentPanelDisplay.ID, IContentPanelDisplay.FUNCTION, IContentPanelDisplay.STATE})));
        listFunctionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        detailFunctionTable = new DetailFunctionTable(IContentPanelDisplay.DETAIL_TABLE_HEADER);

        listFunctionPanel = new JPanel(new BorderLayout());

        detailFunctionPanel = new JPanel(new BorderLayout());

        setColumnWidthListTable();
        setColumnWidthDetailTable();

        scrollPanelList = new JScrollPane(listFunctionTable);
        scrollPanelList.getViewport().setBackground(Color.WHITE);
        listFunctionPanel.add(scrollPanelList, BorderLayout.CENTER);
        scrollPanelDetail = new JScrollPane(detailFunctionTable);
        scrollPanelDetail.getViewport().setBackground(Color.WHITE);

        JLabel lblTestPath = new JLabel("Detail Test path");
        lblTestPath.setFont(lblTestPath.getFont().deriveFont(IContentPanelDisplay.FONT_SIZE));

        txtTestPath = new JTextArea();
        txtTestPath.setWrapStyleWord(true);
        txtTestPath.setLineWrap(true);
        txtTestPath.setFont(txtTestPath.getFont().deriveFont(IContentPanelDisplay.FONT_SIZE));

        JPanel testPathPanel = new JPanel(new BorderLayout());
        testPathPanel.setBackground(Color.WHITE);
        testPathPanel.add(lblTestPath, BorderLayout.PAGE_START);

        testPathPanel.add(new JScrollPane(txtTestPath, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        JLabel lblInput = new JLabel("Input");
        lblInput.setFont(lblInput.getFont().deriveFont(IContentPanelDisplay.FONT_SIZE));

        txtInput = new JTextArea();
        txtInput.setWrapStyleWord(true);
        txtInput.setLineWrap(true);
        txtInput.setFont(txtInput.getFont().deriveFont(IContentPanelDisplay.FONT_SIZE));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(lblInput, BorderLayout.PAGE_START);
        inputPanel.add(new JScrollPane(txtInput, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        splitPanel3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, testPathPanel, inputPanel);
        splitPanel3.setBorder(new LineBorder(Color.GRAY));
        splitPanel3.setDividerSize(2);
        splitPanel3.setMinimumSize(new Dimension(50, 50));
        splitPanel3.setDividerLocation(24 * frameWidth / 100);
        testPathAndInputPanel = new JPanel(new BorderLayout());
        testPathAndInputPanel.setBackground(Color.WHITE);
        testPathAndInputPanel.add(splitPanel3, BorderLayout.CENTER);

        expectedOutputPanel = new ExpectedOutputPanel();

        setSubmitData();

        webTabbedPane = new WebTabbedPane();
        Font font = webTabbedPane.getFont();
        webTabbedPane.setFont(font.deriveFont(IContentPanelDisplay.FONT_SIZE));

        webTabbedPane.addTab("Expexted Output Table", expectedOutputPanel);
        webTabbedPane.addTab("Detail test path & Input", testPathAndInputPanel);

        splitPanel2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPanelDetail, webTabbedPane);
        splitPanel2.setDividerSize(2);
        splitPanel2.setBorder(null);
        splitPanel2.setMinimumSize(new Dimension(50, 50));
        splitPanel2.setDividerLocation(2 * frameHeight / 5);
        detailFunctionPanel.add(splitPanel2, BorderLayout.CENTER);

        setEndListpanel();
        setEndDetailPanel();

        splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listFunctionPanel, detailFunctionPanel);

        listFunctionPanel.setMinimumSize(new Dimension(10, listFunctionPanel.getHeight()));
        detailFunctionPanel.setMinimumSize(new Dimension(10, detailFunctionPanel.getHeight()));

        splitPanel.setDividerSize(3);
        splitPanel.setBorder(null);
        splitPanel.setMinimumSize(new Dimension(50, 50));
        splitPanel.setDividerLocation(1 * frameWidth / 5);

        this.add(splitPanel, BorderLayout.CENTER);

        setEventClickListFunction();

        setEventClickTestPath();

        setPopUpMenu();

    }

    public static IContentPanelDisplay getInstance() {
        if (ContentPanelDisplay.instance == null)
            ContentPanelDisplay.instance = new ContentPanelDisplay();
        return ContentPanelDisplay.instance;
    }

    @Override
    public boolean checkResized(double[] a1) {
        for (double element : a1)
            if (Math.abs(element - 0.2) > 0.02)
                return true;
        return false;
    }

    private int countTestPathInAFunction(int indexFunc) {
        return detailDatas.get(indexFunc).size();
    }

    private void displayExpectedOutputinUI(ExpectedOutputData expectedOutput) {
        int indexID = detailFunctionTable.getSelectedRow();
        detailFunctionTable.setValueAt(expectedOutput.getInputforUI(), indexID, COLUMN.EXPECTED_OUTPUT);
    }

    /**
     * @return
     * @throws Exception
     */
    private ExpectedOutputData getExpectedOutputInEOTable() {
        ExpectedOutputData expectedOutput = new ExpectedOutputData();
        for (int fi = 0; fi < expectedOutputPanel.getExpectedOutputTable().getRowCount(); fi++)
            if (expectedOutputPanel.getExpectedOutputTable().isContainData(fi)) {
                String name = expectedOutputPanel.getExpectedOutputTable()
                        .getValueAt(fi, ExpectedOutputTable.NAME_EXPECTED_OUTPUT_COLUMN).toString().trim();

                String type = expectedOutputPanel.getExpectedOutputTable()
                        .getValueAt(fi, ExpectedOutputTable.TYPE_COLUMN).toString();

                expectedOutputPanel.getExpectedOutputTable().getValueAt(fi, ExpectedOutputTable.DESCRIPTION_COLUMN)
                        .toString();

                String value = expectedOutputPanel.getExpectedOutputTable()
                        .getValueAt(fi, ExpectedOutputTable.EXPECTED_COLUMN).toString();

                if (value.equals(ExpectedOutputPanel.UNSUPPORTED) || value.equals(ExpectedOutputPanel.NO_DATA)) {
                    // nothing to do
                } else if (type.equals(VariableTypes.THROW))
                    expectedOutput.add(new ExceptedExceptionOutputItem(name, type, value));

                else
                    expectedOutput.add(new ExpectedOutputItem(name, type, value));
            }

        return expectedOutput;

    }

    @Override
    public FunctionNode getFunctionNodeSelectedNow(int selectedRowOfListTable) throws Exception {
        ITestedFunctionReport functionSelected = getFunctionReportSelectedNow(selectedRowOfListTable);
        if (!(functionSelected.getFunctionNode() instanceof FunctionNode)) {
            System.err.println("error - return");
            throw new Exception("error when get function");
        }
        FunctionNode functionNode = (FunctionNode) functionSelected.getFunctionNode();
        return functionNode;
    }

    @Override
    public ITestedFunctionReport getFunctionReportSelectedNow(int selectedRowOfListTable) throws Exception {
        int index = 0;
        for (int fi = 0; fi < selectedRowOfListTable; fi++)
            index += countTestPathInAFunction(fi);
        if (indexFile.size() == 0 || indexFunction.size() == 0 || indexFile.size() <= index
                || indexFunction.size() <= index)
            throw new Exception("lỗi khi get function");
        ITestedFunctionReport functionSelected = projectReport.getSourcecodeFiles().get(indexFile.get(index))
                .getTestedFunctionReports().get(indexFunction.get(index));
        return functionSelected;
    }

    @Override
    public DetailFunctionTable getDetailFunctionTable() {
        return detailFunctionTable;
    }

    @Override
    public void setDetailFunctionTable(DetailFunctionTable detailFunctionTable) {
        this.detailFunctionTable = detailFunctionTable;
    }

    @Override
    public ExpectedOutputPanel getExpectedOutputPanel() {
        return expectedOutputPanel;
    }

    @Override
    public void setExpectedOutputPanel(ExpectedOutputPanel expectedOutputPanel) {
        this.expectedOutputPanel = expectedOutputPanel;
    }

    @Override
    public ListFunctionTable getListFunctionTable() {
        return listFunctionTable;
    }

    @Override
    public void setListFunctionTable(ListFunctionTable listFunctionTable) {
        this.listFunctionTable = listFunctionTable;
    }

    @Override
    public int getSelectingRow() {
        return selectingRowListTable;
    }

    @Override
    public void setSelectingRow(int selectingRow) {
        selectingRowListTable = selectingRow;
    }

    @Override
    public int getSelectingRowIdDetailTable() {
        return selectingRowIdDetailTable;
    }

    @Override
    public void setSelectingRowIdDetailTable(int selectingRowIdDetailTable) {
        this.selectingRowIdDetailTable = selectingRowIdDetailTable;
    }

    // Insert data into Tables
    @Override
    public void insertData(IProjectReport _projectReport) {
        // initialize all variable save data
        indexFile = new ArrayList<>();
        indexFunction = new ArrayList<>();
        indexTestPath = new ArrayList<>();

        detailDatas = new Vector<>();
        countTestedFunction = 0;
        countTotalFunction = 0;

        projectReport = _projectReport;

        if (listFunctionsStoppedTesting == null)
            listFunctionsStoppedTesting = new ArrayList<>();

        // Parse Project Report
        ArrayList<SourcecodeFileReport> srcCodes = (ArrayList<SourcecodeFileReport>) projectReport.getSourcecodeFiles();
        for (int fi = 0; fi < srcCodes.size(); fi++) {
            ISourcecodeFileReport srcCode = srcCodes.get(fi);
            ArrayList<ITestedFunctionReport> testFunctions = (ArrayList<ITestedFunctionReport>) srcCode
                    .getTestedFunctionReports();

            for (int se = 0; se < testFunctions.size(); se++) {
                ITestedFunctionReport testFunction = testFunctions.get(se);
                countTotalFunction++;

                Vector<Object> listData = new Vector<>();

                // Insert into List Table
                listData.add(testFunction.getName());
                listData.add(testFunction.getState());
                insertToListFunctions(listData);

                if (testFunction.getState().equalsIgnoreCase("Finished"))
                    countTestedFunction++;

                // All test path of a Function
                ArrayList<ITestpathReport> testPaths = (ArrayList<ITestpathReport>) testFunction.getTestpaths();
                Vector<Vector<Object>> detailPerFunction = new Vector<>();

                for (int th = 0; th < testPaths.size(); th++) {
                    Vector<Object> detailData = new Vector<>();
                    ITestpathReport testPath = testPaths.get(th);
                    detailData.add(testPath.getShortenTestpath());

                    String tempInput = testPath.getInput().getVariablesForDisplay().toString();
                    detailData.add(tempInput.substring(1, tempInput.length() - 1));
                    detailData.add(testPath.getExpectedOutput().getExpectedValues().getInputforUI());
                    detailData.add(testPath.getPass());
                    detailPerFunction.add(detailData);

                    indexFile.add(fi);
                    indexFunction.add(se);
                    indexTestPath.add(th);
                }
                detailDatas.add(detailPerFunction);
            }
        }

        // Update progressbar
        if (countTotalFunction > 0)
            progressBar.setValue(countTestedFunction / countTotalFunction * 100);
        progressBar.setString("Process " + countTestedFunction + "/" + countTotalFunction + "functions");

    }

    @Override
    public void insertToListFunctions(Vector<Object> data) {
        if (data.size() < listFunctionTable.getColumnCount())
            data.insertElementAt(listFunctionTable.getRowCount() + 1, 0);
        listFunctionTable.addRow(data);

        listFunctionsStoppedTesting.add(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() instanceof JTable) {
            JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            int column = source.columnAtPoint(e.getPoint());

            // If List Function Table
            if (e.getComponent() instanceof ListFunctionTable) {
                myPopUpMenu = new MyPopUpMenu(MyPopUpMenu.LIST_FUNCTION_TABLE, -1);
                if (!source.isRowSelected(row)) {
                    source.changeSelection(row, column, false, false);

                    // save column size of Detail Function Table
                    detailFunctionTable.saveColumnsSize();
                    // save Expected Output Table
                    testPathSelectedBefore = testPathSelectedNow;
                    saveEOTableDataOfTestPath();

                    selectingRowIdDetailTable = -1;
                    expectedOutputPanel.removeAllRows();
                    viewDetailFunction();
                }
                boolean isFunctStopTesting = listFunctionsStoppedTesting.get(listFunctionTable.getSelectedRow());
                myPopUpMenu.getStopTesting().setEnabled(!isFunctStopTesting);
                myPopUpMenu.getResumeTesting().setEnabled(isFunctStopTesting);
            } // If Detail Function Table
            else if (e.getComponent() instanceof DetailFunctionTable) {
                // int detailTableSelectionType = detailTableSelectionType;
                myPopUpMenu = new MyPopUpMenu(MyPopUpMenu.DETAIL_FUNCTION_TABLE, detailTableSelectionType);
            }
        } else
            myPopUpMenu = new MyPopUpMenu(MyPopUpMenu.OTHER, -1);
        if (SwingUtilities.isRightMouseButton(e))
            myPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void refresh() {
        try {
            int rowDetailTableSelected = detailFunctionTable.getSelectedRow();
            int colDetailTableSelected = detailFunctionTable.getSelectedColumn();
            int rowListTableSelected = listFunctionTable.getSelectedRow();

            if (detailTableColumnsSizeNew != null && checkResized(detailTableColumnsSizeNew))
                detailTableColumnsSizeOld = detailTableColumnsSizeNew.clone();

            detailTableColumnsSizeNew = detailFunctionTable.saveColumnsSize();

            listFunctionTable.removeAllRow();
            insertData(projectReport);
            listFunctionTable.revalidate();
            listFunctionTable.changeSelection(rowListTableSelected, 0, false, false);
            viewDetailFunction();
            if (rowDetailTableSelected >= 0 && colDetailTableSelected >= 0)
                detailFunctionTable.changeSelection(rowDetailTableSelected, colDetailTableSelected, false, false);

            if (!checkResized(detailTableColumnsSizeNew))
                detailFunctionTable.restoreColumnsSize(detailTableColumnsSizeOld);
            else
                detailFunctionTable.restoreColumnsSize(detailTableColumnsSizeNew);

            detailFunctionTable.revalidate();
        } catch (Exception e) {

        } finally {
        }

    }

    @Override
    public void removeFunction(int[] listIndex) {
        try {
            for (int element : listIndex) {
                FunctionNode functionNode = getFunctionNodeSelectedNow(element);
                ThreadManager.getInstance().remove(functionNode);
                projectReport.removeFunction(functionNode);
                refresh();
            }
            detailFunctionTable.removeAllRow();
            expectedOutputPanel.getExpectedOutputTable().removeAllRow();
        } catch (Exception e) {

        }
    }

    @Override
    public void resumeFunction(int[] listIndex) {
        try {
            for (int element : listIndex) {
                FunctionNode functionNode = getFunctionNodeSelectedNow(element);
                ThreadManager.getInstance().resume(functionNode);
                refresh();
            }
            detailFunctionTable.removeAllRow();
            expectedOutputPanel.getExpectedOutputTable().removeAllRow();
        } catch (Exception e) {

        }
    }

    @Override
    public void saveEOTableDataOfTestPath() {
        if (testPathSelectedBefore >= 0) {
            List<Integer> listRootRowIndexEOTable = expectedOutputPanel.getListRootIndex();
            if (listRootRowIndexEOTable == null || listRootRowIndexEOTable.size() == 0)
                return;
            List<Boolean> listRootRowIsExpanded = new ArrayList<>();
            for (int fi = listRootRowIndexEOTable.size() - 1; fi >= 0; fi--) {
                boolean rowIsExpanded = expectedOutputPanel.getTableConfig().getRowConfigs()
                        .get(listRootRowIndexEOTable.get(fi)).isExpanded();
                listRootRowIsExpanded.add(0, rowIsExpanded);
                int currentRootRow = listRootRowIndexEOTable.get(fi);
                RowConfig rowConfig = expectedOutputPanel.getTableConfig().getRowConfigs().get(currentRootRow);
                if (rowIsExpanded)
                    expectedOutputPanel.collapse(currentRootRow, true);
                else {
                    int countRealChildren = rowConfig.getCountRealChildren();
                    if (rowConfig.getRowConfigsChildren() != null && rowConfig.getRowConfigsChildren().size() > 0)
                        countRealChildren = rowConfig.getRowConfigsChildren().get(0).getCountRealChildren();

                    int countTotalChildren = rowConfig.getTotalChildrenRow();
                    if (rowConfig.getRowConfigsChildren() != null && rowConfig.getRowConfigsChildren().size() > 0)
                        countTotalChildren = rowConfig.getRowConfigsChildren().get(0).getTotalChildrenRow();

                    boolean isArray = rowConfig.isArray();
                    if (rowConfig.getRowConfigsChildren() != null && rowConfig.getRowConfigsChildren().size() > 0)
                        isArray = rowConfig.getRowConfigsChildren().get(0).isArray();

                    rowConfig.setFirstChildren(
                            expectedOutputPanel.getExpectedOutputTable().getValueAt(currentRootRow,
                                    ExpectedOutputTable.DESCRIPTION_COLUMN),
                            expectedOutputPanel.getExpectedOutputTable().getValueAt(currentRootRow,
                                    ExpectedOutputTable.EXPECTED_COLUMN),
                            countRealChildren, countTotalChildren, isArray);
                }
            }
            for (int fi = 0; fi < listRootRowIsExpanded.size(); fi++)
                expectedOutputPanel.getTableConfig().getRowConfigs().get(fi).setExpanded(listRootRowIsExpanded.get(fi));
            ExpectedOutputTableConfig oldEOTableConfig = new ExpectedOutputTableConfig(
                    expectedOutputPanel.getTableConfig());
            if (testPathSelectedBefore >= listEOTableConfig.size()) {
                for (int fi = listEOTableConfig.size(); fi < testPathSelectedBefore; fi++)
                    listEOTableConfig.add(null);
                listEOTableConfig.add(oldEOTableConfig);
            } else
                listEOTableConfig.set(testPathSelectedBefore, oldEOTableConfig);
        }
    }

    private void setColumnWidthDetailTable() {
        int tableSize = detailFunctionTable.getPreferredSize().width;
        detailFunctionTable.getColumnModel().getColumn(0).setPreferredWidth(Math.round(tableSize * 0.005f));
        detailFunctionTable.getColumnModel().getColumn(1).setPreferredWidth(Math.round(tableSize * 0.5f));
        detailFunctionTable.getColumnModel().getColumn(2).setPreferredWidth(Math.round(tableSize * 0.2f));
        detailFunctionTable.getColumnModel().getColumn(3).setPreferredWidth(Math.round(tableSize * 0.15f));
        detailFunctionTable.getColumnModel().getColumn(4).setPreferredWidth(Math.round(tableSize * 0.145f));
    }

    private void setColumnWidthListTable() {
        int tableSize = listFunctionTable.getPreferredSize().width;
        listFunctionTable.getColumnModel().getColumn(0).setPreferredWidth(Math.round(tableSize * 0.12f));
        listFunctionTable.getColumnModel().getColumn(1).setPreferredWidth(Math.round(tableSize * 0.58f));
        listFunctionTable.getColumnModel().getColumn(2).setPreferredWidth(Math.round(tableSize * 0.3f));
    }

    @Override
    public void setDataDetailTable(Vector<Vector<Object>> data) {

        detailFunctionTable.removeAllRow();
        for (int fi = 0; fi < data.size(); fi++) {
            Vector<Object> dataPerRow = new Vector<>(data.get(fi));

            String tempTestpath = dataPerRow.get(COLUMN.TEST_PATH).toString();
            if (tempTestpath.length() > ContentPanelDisplay.MAX_CHAR_OF_TEST_PATH)
                dataPerRow.set(COLUMN.TEST_PATH,
                        tempTestpath.substring(0, ContentPanelDisplay.MAX_CHAR_OF_TEST_PATH) + "...");

            String tempInput = dataPerRow.get(COLUMN.INPUT).toString();
            tempInput = tempInput.replaceAll(", ", "\n");
            dataPerRow.set(COLUMN.INPUT, tempInput);
            detailFunctionTable.addRow(dataPerRow);
        }
    }

    @Override
    public void setDividerLocationForSplitPanes() {
        String spInStr = AbstractSetting.getValue("SPLIT_PANE_DIVIDER_LOCATION");
        if (!spInStr.equals("") && Double.parseDouble(spInStr) > 0)
            splitPanel.setDividerLocation((int) (Double.parseDouble(spInStr) * splitPanel.getWidth()));

        String sp2InStr = AbstractSetting.getValue("SPLIT_PANE2_DIVIDER_LOCATION");
        if (!sp2InStr.equals("") && Double.parseDouble(sp2InStr) > 0)
            splitPanel2.setDividerLocation((int) (Double.parseDouble(sp2InStr) * splitPanel2.getHeight()));

        String sp3InStr = AbstractSetting.getValue("SPLIT_PANE3_DIVIDER_LOCATION");
        if (!sp3InStr.equals("") && Double.parseDouble(sp3InStr) > 0)
            splitPanel3.setDividerLocation((int) (Double.parseDouble(sp3InStr) * splitPanel3.getWidth()));
    }

    private void setEndDetailPanel() {
        endDetailPanel = new JPanel(new BorderLayout());
        endDetailPanel.setBackground(Color.WHITE);

        btnExport = new JButton("Export to excel");
        btnExport.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.EXPORT_EXCEL)));
        btnExportUnitTest = new JButton("Export unit test");
        btnExportUnitTest.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.EXPORT_UNITTEST)));

        JPanel buttonExportPanel = new JPanel(new BorderLayout());
        buttonExportPanel.add(btnExport, BorderLayout.LINE_END);
        buttonExportPanel.add(btnExportUnitTest, BorderLayout.CENTER);

        endDetailPanel.add(buttonExportPanel, BorderLayout.LINE_END);

        lblCoverage = new JLabel("");
        Border border = lblCoverage.getBorder();
        Border margin = new EmptyBorder(0, 0, 0, 50);
        lblCoverage.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCoverage.setBorder(new CompoundBorder(border, margin));
        endDetailPanel.add(lblCoverage, BorderLayout.CENTER);

        detailFunctionPanel.add(endDetailPanel, BorderLayout.PAGE_END);

    }

    private void setEndListpanel() {
        endListPanel = new JPanel(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);

        endListPanel.add(progressBar, BorderLayout.CENTER);
        listFunctionPanel.add(endListPanel, BorderLayout.PAGE_END);
    }

    /**
     * Set Change when click a function in List Function Table
     */
    private void setEventClickListFunction() {
        listFunctionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (selectingRowListTable != listFunctionTable.getSelectedRow()) {
                    selectingRowListTable = listFunctionTable.getSelectedRow();
                    // save column size
                    double[] detailTableColumnsSize = detailFunctionTable.saveColumnsSize();
                    // save Expected Output Table
                    testPathSelectedBefore = testPathSelectedNow;
                    ContentPanelDisplay.this.saveEOTableDataOfTestPath();

                    selectingRowIdDetailTable = -1;
                    expectedOutputPanel.removeAllRows();
                    ContentPanelDisplay.this.viewDetailFunction();

                    detailFunctionTable.restoreColumnsSize(detailTableColumnsSize);
                    detailFunctionTable.revalidate();
                }
            }
        });

        listFunctionTable.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_UP || arg0.getKeyCode() == KeyEvent.VK_DOWN)
                    ContentPanelDisplay.this.viewDetailFunction();
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
        });
    }

    private void setEventClickTestPath() {
        detailFunctionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ContentPanelDisplay.this.viewEOTable();
            }
        });

        detailFunctionTable.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
                    ContentPanelDisplay.this.viewEOTable();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
    }

    private void setPopUpMenu() {
        detailFunctionTable.addMouseListener(this);
        listFunctionTable.addMouseListener(this);
        scrollPanelList.addMouseListener(this);
        scrollPanelDetail.addMouseListener(this);
        txtTestPath.addMouseListener(this);
        txtInput.addMouseListener(this);
    }

    @Override
    public void setSizeSaveBeforeForComponents() {
        setDividerLocationForSplitPanes();
        listFunctionTable.initializeColumnWidthSize();
        detailFunctionTable.initializeColumnWidthSize();
        expectedOutputPanel.getExpectedOutputTable().initializeColumnWidthSize();
    }

    @Override
    public void setSubmitData() {
        expectedOutputPanel.getBtnSubmit().addActionListener(e -> {
            if (expectedOutputPanel.getExpectedOutputTable().isEditing())
                expectedOutputPanel.getExpectedOutputTable().getCellEditor().stopCellEditing();

            ExpectedOutputData expectedOutput = ContentPanelDisplay.this.getExpectedOutputInEOTable();
            ContentPanelDisplay.this.displayExpectedOutputinUI(expectedOutput);

            ITestpathReport currentTestpathReport = ContentPanelDisplay.this.getCurrentTestpathReport();
            currentTestpathReport.getExpectedOutput().setExpectedValues(expectedOutput);
        });
    }

    @Override
    public void stopFunction(int[] listIndex) {
        try {
            for (int element : listIndex) {
                FunctionNode functionNode = getFunctionNodeSelectedNow(element);
                ThreadManager.getInstance().stop(functionNode);
                ProjectReport.getInstance().getFunction(functionNode).setState("stopped");

                refresh();
            }
            detailFunctionTable.removeAllRow();
            expectedOutputPanel.getExpectedOutputTable().removeAllRow();
        } catch (Exception e) {

        }
    }

    private ITestpathReport getCurrentTestpathReport() {
        int index = 0;
        for (int fi = 0; fi < selectingRowListTable; fi++)
            index += countTestPathInAFunction(fi);
        index += detailFunctionTable.getSelectedRow();
        ITestpathReport testpathReport = projectReport.getSourcecodeFiles().get(indexFile.get(index))
                .getTestedFunctionReports().get(indexFunction.get(index)).getTestpaths().get(indexTestPath.get(index));
        return testpathReport;
    }

    @Override
    public void viewDetailFunction() {
        selectingRowListTable = listFunctionTable.getSelectedRow();

        if (selectingRowListTable >= 0) {

            Vector<Vector<Object>> detailDataPerFunction = detailDatas.get(selectingRowListTable);
            for (int fi = 0; fi < detailDataPerFunction.size(); fi++)
                if (detailDataPerFunction.get(fi).size() < detailFunctionTable.getColumnCount())
                    detailDataPerFunction.get(fi).insertElementAt(fi + 1, 0);
            setDataDetailTable(detailDataPerFunction);

            expectedOutputPanel.getExpectedOutputTable().restoreColumnsSize();

            // Branch or statement coverage
            try {
                ITestedFunctionReport functionReport = getFunctionReportSelectedNow(selectingRowListTable);
                FunctionNode functionNode = getFunctionNodeSelectedNow(selectingRowListTable);

                String typeCover = "";
                switch (functionNode.getFunctionConfig().getTypeofCoverage()) {
                    case IFunctionConfig.STATEMENT_COVERAGE:
                        typeCover = "Statement coverage";
                        break;
                    case IFunctionConfig.BRANCH_COVERAGE:
                        typeCover = "Branch coverage";
                        break;
                }
                int codeCover = Math.round(functionReport.computeCoverage());
                lblCoverage.setText(typeCover + ": " + codeCover + "%");
                /*
				 * Add solving time
				 */
                lblCoverage.setText(lblCoverage.getText() + " ("
                        + ProjectReport.getInstance().getFunction(functionNode).getCoverage().getTime() + " s)");
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void viewEOTable() {
        int selectedRowDetailTable = detailFunctionTable.getSelectedRow();

        int realSelectingRowID = selectedRowDetailTable;

        testPathSelectedBefore = testPathSelectedNow;
        testPathSelectedNow = 0;
        for (int fi = 0; fi < selectingRowListTable; fi++)
            testPathSelectedNow += countTestPathInAFunction(fi);
        testPathSelectedNow += realSelectingRowID;

        if (realSelectingRowID != selectingRowIdDetailTable) {
            try {
                viewTestPathAndInput(realSelectingRowID);
                viewExpectedOutput(realSelectingRowID, testPathSelectedNow);
            } catch (Exception e1) {
            }
            selectingRowIdDetailTable = realSelectingRowID;
        }
    }

    // View expected output table
    @Override
    public void viewExpectedOutput(int selectingRowID, int testPathIndex) throws Exception {
        ITestpathReport currentTestPath = projectReport.getSourcecodeFiles().get(indexFile.get(testPathIndex))
                .getTestedFunctionReports().get(indexFunction.get(testPathIndex)).getTestpaths()
                .get(indexTestPath.get(testPathIndex));
        IFunctionNode functionNode = currentTestPath.getFunctionNode();

        saveEOTableDataOfTestPath();

        expectedOutputPanel.removeAllRows();

        // If Expected Output Table not save before
        List<IVariableNode> nodes = functionNode.getExpectedNodeTypes();
        expectedOutputPanel.setExpectedNodeTypes(nodes);
        if (listEOTableConfig == null || listEOTableConfig.size() <= testPathIndex || listEOTableConfig.size() == 0
                || listEOTableConfig.get(testPathIndex) == null)
            ; // If Expected Output Table saved -> re-write
        else {
            expectedOutputPanel.setTableConfig(listEOTableConfig.get(testPathIndex));
            List<Boolean> listRootRowExpandedBefore = new ArrayList<>();
            for (int fi = listEOTableConfig.get(testPathIndex).getRowConfigs().size() - 1; fi >= 0; fi--) {
                RowConfig rowConfig = listEOTableConfig.get(testPathIndex).getRowConfigs().get(fi);
                listRootRowExpandedBefore.add(0, rowConfig.isExpanded());

                expectedOutputPanel.getExpectedOutputTable().setValueAt(
                        rowConfig.getRowConfigsChildren().get(0).getEOAttribute(), fi,
                        ExpectedOutputTable.DESCRIPTION_COLUMN);
                expectedOutputPanel.getExpectedOutputTable().setValueAt(
                        rowConfig.getRowConfigsChildren().get(0).getEOValue(), fi, ExpectedOutputTable.EXPECTED_COLUMN);

                if (rowConfig.getRowConfigsChildren() != null && rowConfig.getRowConfigsChildren().size() > 0
                        && rowConfig.getRowConfigsChildren().get(0).getCountRealChildren() > 0)
                    expectedOutputPanel.loadDataIntoExpandedArea(fi);
            }
            List<Integer> listRootRowIndexEOTable = expectedOutputPanel.getListRootIndex();
            for (int fi = listRootRowExpandedBefore.size() - 1; fi >= 0; fi--)
                if (!listRootRowExpandedBefore.get(fi))
                    expectedOutputPanel.collapse(listRootRowIndexEOTable.get(fi), true);
        }
        expectedOutputPanel.getExpectedOutputTable().clearSelection();
    }

    @Override
    public void viewTestPathAndInput(int selectingRowID) throws Exception {
        txtTestPath.setText(detailDatas.get(selectingRowListTable).get(selectingRowID).get(1).toString());
        txtInput.setText(detailDatas.get(selectingRowListTable).get(selectingRowID).get(2).toString());
    }

    @Override
    public void writeSaveData() {
        // Save divider location of 3 splitPanes
        double temp = (double) splitPanel.getDividerLocation() / (double) splitPanel.getWidth();
        double temp2 = (double) splitPanel2.getDividerLocation() / (double) splitPanel2.getHeight();
        double temp3 = (double) splitPanel3.getDividerLocation() / (double) splitPanel3.getWidth();
        if (temp < 0 || temp2 < 0 || temp3 < 0)
            return;
        AbstractSetting.setValue("SPLIT_PANE_DIVIDER_LOCATION", temp);
        AbstractSetting.setValue("SPLIT_PANE2_DIVIDER_LOCATION", temp2);
        AbstractSetting.setValue("SPLIT_PANE3_DIVIDER_LOCATION", temp3);

        // Save column size of 3 tables
        listFunctionTable.saveColumnsSizeWhenClose();
        detailFunctionTable.saveColumnsSizeWhenClose();
        expectedOutputPanel.getExpectedOutputTable().saveColumnsSizeWhenClose();
    }

    @Override
    public JButton getBtnExport() {
        return btnExport;
    }

    @Override
    public void setBtnExport(JButton btnExport) {
        this.btnExport = btnExport;
    }

    @Override
    public JButton getBtnExportUnitTest() {
        return btnExportUnitTest;
    }

    @Override
    public void setBtnExportUnitTest(JButton btnExportUnitTest) {
        this.btnExportUnitTest = btnExportUnitTest;
    }

    class COLUMN {
        static final int ID = 0;
        static final int TEST_PATH = 1;
        static final int INPUT = 2;
        static final int EXPECTED_OUTPUT = 3;
        static final int PASS = 4;
    }

    /**
     * Menu for right mouse click
     *
     * @author Duong Td
     */
    public class MyPopUpMenu extends JPopupMenu implements ActionListener {

        public static final int LIST_FUNCTION_TABLE = 1, DETAIL_FUNCTION_TABLE = 2, OTHER = 3;
        public static final int SELECT_BY_ROW = 0, SELECT_BY_CELL = 1;
        private static final long serialVersionUID = 1L;
        private JMenuItem refresh;

        private JMenuItem removeFunction, stopTesting, resumeTesting;

        private ArrayList<JRadioButtonMenuItem> selectingType;
        private JMenu selectionMenu;
        private ButtonGroup group;

        public MyPopUpMenu(int forComponent, int detailTableSelectionType) {
            super();
            refresh = new JMenuItem("Refresh");
            refresh.addActionListener(this);
            this.add(refresh);

            switch (forComponent) {

                // For List Function Table
                case LIST_FUNCTION_TABLE:
                    removeFunction = new JMenuItem("Remove");
                    removeFunction.addActionListener(e -> removeFunction(listFunctionTable.getSelectedRows()));
                    this.add(removeFunction);

                    stopTesting = new JMenuItem("Stop");

                    stopTesting.addActionListener(e -> {
                        for (int rowIndex : listFunctionTable.getSelectedRows())
                            listFunctionsStoppedTesting.set(rowIndex, true);
                        /**
                         * terminate Function testing
                         */
                        stopFunction(listFunctionTable.getSelectedRows());

                    });
                    // this.add(this.stopTesting);

                    resumeTesting = new JMenuItem("Resume");

                    resumeTesting.addActionListener(e -> {
                        for (int rowIndex : listFunctionTable.getSelectedRows())
                            listFunctionsStoppedTesting.set(rowIndex, false);

                        /**
                         * resume Function testing
                         */
                        resumeFunction(listFunctionTable.getSelectedRows());

                    });
                    // this.add(this.resumeTesting);
                    break;

                // For Detail Function Table
                case DETAIL_FUNCTION_TABLE:
                    selectionMenu = new JMenu("Selection type");

                    selectingType = new ArrayList<>();
                    selectingType.add(new JRadioButtonMenuItem("By row"));
                    selectingType.add(new JRadioButtonMenuItem("By cell"));

                    selectingType.get(detailTableSelectionType).setSelected(true);

                    group = new ButtonGroup();
                    for (int fi = 0; fi < selectingType.size(); fi++) {
                        selectingType.get(fi).addActionListener(e -> {

                            String temp = e.getActionCommand();
                            if (temp.equalsIgnoreCase("By cell")) {
                                MyPopUpMenu.this.setSelectingType(1);
                            } else if (temp.equalsIgnoreCase("By row"))
                                MyPopUpMenu.this.setSelectingType(0);
                        });
                        group.add(selectingType.get(fi));
                        selectionMenu.add(selectingType.get(fi));
                    }

                    this.add(selectionMenu);
                    break;
                case OTHER:
                    break;

            }

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String temp = e.getActionCommand();
            if (temp.equalsIgnoreCase("Refresh"))
                refresh();
        }

        public JMenuItem getResumeTesting() {
            return resumeTesting;
        }

        public JMenuItem getStopTesting() {
            return stopTesting;
        }

        public void setStopTesting(JMenuItem stopTesting) {
            this.stopTesting = stopTesting;
        }

        // index = 1 : selection by cell
        // index = 0 : selection by row
        public void setSelectingType(int index) {
            if (index == 1) {
                detailFunctionTable.setRowSelectionAllowed(false);
                detailFunctionTable.setCellSelectionEnabled(true);
                detailTableSelectionType = 1;
            } else {
                detailFunctionTable.setCellSelectionEnabled(false);
                detailFunctionTable.setRowSelectionAllowed(true);
                detailTableSelectionType = 0;
            }
        }
    }

}
