package com.fit.gui.testedfunctions;

import com.fit.config.Paths;
import com.fit.gui.testedfunctions.ExpectedOutputTableConfig.RowConfig;
import com.fit.gui.testreport.object.INameRule;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.*;
import com.fit.utils.Utils;
import com.fit.utils.search.EnumNodeCondition;
import com.fit.utils.search.Search;
import com.fit.utils.search.SearchCondition;
import com.fit.utils.search.TypedefNodeCondition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Panel for user put in Expected Output
 *
 * @author Duong Td
 */
public class ExpectedOutputPanel extends JPanel {
    public static final int DOUBLE_CLICK = 2;
    public static final Object UNSUPPORTED = "unsupported";
    public static final Object NO_DATA = "";
    public static final Object NULL = "NULL";
    public static final String SIZE_MESSAGE = "Type expected size of this variable";
    public static final String INVALID_MESSAGE = "The data is not invalid";
    public static final String SIZE = "size=";

    public static final String WARNING_MESSAGE = "All items in unavailable area are deleted. Are you want to continue?";

    public static final String INVALID_ARRAY_SIZE = "The new size is greater than the limit!";

    public static final int UNSPECIFIED_SIZE = -1;

    private static final long serialVersionUID = 1L;

    private static final String TAB = "        ";

    private static final Vector<Object> COLUMNS_NAME = new Vector<>(
            Arrays.asList("Name Expected Output", "Type", "Description", "Expected Value"));

    private JPanel mainPanel;

    // Panel at bottom have OK button
    private JPanel belowPanel;

    private JButton btnSubmit;
    // Expected Output Table
    private ExpectedOutputTable expectedOutputTable;

    private CellEditorActionListener cellEditorActionListener;

    private TableMouseListener tableMouseListener;

    private String dataCellBefore;

    /**
     * Create the panel.
     */
    public ExpectedOutputPanel() {
        super(new BorderLayout());
        initComponents();
    }

    public ExpectedOutputPanel(List<IVariableNode> expectedNodeTypes) throws Exception {
        super(new BorderLayout());
        setBackground(Color.WHITE);
        initComponents();
        initializeTable(expectedNodeTypes);

        setExpandOrCollapseorRewriteEvent();

        setModifyExpectedEvent();
    }

    private boolean canBeExpandedorCollapsed() {
        return expectedOutputTable.getSelectedColumn() == ExpectedOutputTable.NAME_EXPECTED_OUTPUT_COLUMN
                || expectedOutputTable.getSelectedColumn() == ExpectedOutputTable.TYPE_COLUMN
                || expectedOutputTable.getSelectedColumn() == ExpectedOutputTable.DESCRIPTION_COLUMN;
    }

    /**
     * Collapse children of row
     *
     * @param rowIndex:  parent row
     * @param isSaveData
     */
    public void collapse(int rowIndex, boolean isSaveData) {

        RowConfig rowConfig = expectedOutputTable.getTableConfig().getRowConfigs().get(rowIndex);

        rowConfig.removeAllChildrenData();
        if (isSaveData)
            for (int fi = rowIndex; fi <= rowIndex + rowConfig.getTotalChildrenRow(); fi++) {
                RowConfig currentRowConfig;
                if (fi == rowIndex)
                    currentRowConfig = expectedOutputTable.getTableConfig().new RowConfig(
                            expectedOutputTable.getTableConfig().getRowConfigs().get(fi));
                else
                    currentRowConfig = expectedOutputTable.getTableConfig().getRowConfigs().get(fi);
                currentRowConfig
                        .setEOAttribute(expectedOutputTable.getValueAt(fi, ExpectedOutputTable.DESCRIPTION_COLUMN));
                currentRowConfig.setEOValue(expectedOutputTable.getValueAt(fi, ExpectedOutputTable.EXPECTED_COLUMN));

                rowConfig.addOneChildrenRow(currentRowConfig);
            }

        detectParentRow_Collapse(rowIndex, rowConfig.getTotalChildrenRow());

        for (int fi = rowIndex + rowConfig.getTotalChildrenRow(); fi > rowIndex; fi--)
            expectedOutputTable.removeRow(fi);
        expectedOutputTable.getTableConfig().removeChildrenOfRow(rowIndex, isSaveData);

    }

    public void collapseAll() {
        List<Integer> listRootIndex = getListRootIndex();
        for (int fi = listRootIndex.size() - 1; fi >= 0; fi--)
            collapse(fi, true);
    }

    /**
     * Detect change children size of some row
     *
     * @param rowIndex
     * @param size:    number of children remove
     */
    private void detectParentRow_Collapse(int rowIndex, int size) {
        for (int fi = rowIndex - 1; fi >= 0; fi--) {
            RowConfig rowConfig = expectedOutputTable.getTableConfig().getRowConfigs().get(fi);
            if (fi + rowConfig.getTotalChildrenRow() >= rowIndex)
                rowConfig.setTotalChildrenRow(rowConfig.getTotalChildrenRow() - size);
        }
    }

    /**
     * Detect change children size of some row
     *
     * @param rowIndex
     * @param childrenIncrease: number of children increase
     */
    private void detectParentRow_Expand(int rowIndex, int childrenIncrease) {
        // detect hàng cha thì cần tăng số con lên
        for (int fi = rowIndex - 1; fi >= 0; fi--) {
            RowConfig rowConfig = expectedOutputTable.getTableConfig().getRowConfigs().get(fi);
            if (fi + rowConfig.getTotalChildrenRow() >= rowIndex)
                rowConfig.setTotalChildrenRow(rowConfig.getTotalChildrenRow() + childrenIncrease);
        }
    }

    /**
     * Editing Expected Output Value Cell has boolean variable
     *
     * @param textField
     * @param rowSelected
     * @param colSelected
     */
    private void editingForEOCellOfBoolean(JTextField textField, int rowSelected, int colSelected) {
        String newContent = textField.getText();
        if (!newContent.equals("true") && !newContent.equals("false")) {
            JOptionPane.showMessageDialog(getJFrameContainer(), ExpectedOutputPanel.INVALID_MESSAGE);
            textField.setText(dataCellBefore);
        } else {
        }
    }

    /**
     * Editing Expected Output Value Cell has char variable
     *
     * @param textField
     * @param rowSelected
     * @param colSelected
     */
    private void editingForEOCellOfChar(JTextField textField, int rowSelected, int colSelected) {
        String newContent = textField.getText();
        if (newContent.equals(ExpectedOutputPanel.NO_DATA) || newContent.replaceAll("'", "").length() > 1
                || newContent.replaceAll("'", "").equals(ExpectedOutputPanel.NO_DATA)) {
            JOptionPane.showMessageDialog(getJFrameContainer(), ExpectedOutputPanel.INVALID_MESSAGE);
            textField.setText(dataCellBefore);
        } else {
            newContent = newContent.replaceAll("'", "");

            for (int fi = rowSelected - 1; fi >= 0; fi--)
                if (expectedOutputTable.getTableConfig().getRowConfigs().get(fi).getTotalChildrenRow()
                        + fi >= rowSelected) {
                    String parentType = expectedOutputTable.getValueAt(fi, ExpectedOutputTable.TYPE_COLUMN).toString();
                    if (VariableTypes.isOneDimension(parentType) || VariableTypes.isOneLevel(parentType)) {
                        String oldParentData = expectedOutputTable.getValueAt(fi, ExpectedOutputTable.EXPECTED_COLUMN)
                                .toString();
                        oldParentData = oldParentData.replaceAll("\"", "");
                        int indexToChange = rowSelected - fi - 1;
                        String newParentData;
                        if (indexToChange >= oldParentData.length())
                            newParentData = oldParentData + newContent;
                        else
                            newParentData = oldParentData.substring(0, indexToChange) + newContent
                                    + oldParentData.substring(indexToChange + 1);
                        expectedOutputTable.setValueAt("\"" + newParentData + "\"", fi,
                                ExpectedOutputTable.EXPECTED_COLUMN);
                    }
                    break;
                }
            textField.setText("'" + newContent + "'");
        }
    }

    /**
     * Editing Expected Output Value Cell char array variable
     *
     * @param textField
     * @param rowSelected
     * @param colSelected
     */
    private void editingForEOCellOfCharArray(JTextField textField, int rowSelected, int colSelected) {
        String newContent = textField.getText();
        String type = expectedOutputTable.getValueAt(rowSelected, ExpectedOutputTable.TYPE_COLUMN).toString();

        if (newContent.equals(ExpectedOutputPanel.NULL)) {
            int result = JOptionPane.showConfirmDialog(getJFrameContainer(), ExpectedOutputPanel.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded()) {
                    expectedOutputTable.setValueAt(ExpectedOutputPanel.NULL, rowSelected, colSelected);
                    collapse(rowSelected, false);
                } else
                    ;
            } else
                textField.setText(dataCellBefore);
        } else {
            if (!newContent.startsWith("\""))
                newContent = "\"" + newContent;
            if (!newContent.endsWith("\""))
                newContent += "\"";
            textField.setText(newContent);

            int newSize = newContent.length() - 2;
            /**
             * 2 cases: char[n] or char[]
             */
            // unspecified size - char[]
            if (type.matches(".*\\[\\s*\\]")) {
                expectedOutputTable.setValueAt(newContent, rowSelected, colSelected);
                expectedOutputTable.setValueAt(ExpectedOutputPanel.SIZE + newSize, rowSelected,
                        ExpectedOutputTable.DESCRIPTION_COLUMN);
                if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded())
                    refreshTableByCollapseThenExpand(rowSelected);
            } // specified size - char[n]
            else {
                List<String> indexes = Utils.getIndexOfArray(type);
                int size = Utils.toInt(indexes.get(0).replace(ExpectedOutputPanel.SIZE, ""));
                if (newSize > size) {
                    JOptionPane.showMessageDialog(getJFrameContainer(), ExpectedOutputPanel.INVALID_ARRAY_SIZE);
                    textField.setText(dataCellBefore);
                } else {
                    expectedOutputTable.setValueAt(newContent, rowSelected, colSelected);
                    expectedOutputTable.setValueAt(ExpectedOutputPanel.SIZE + size, rowSelected,
                            ExpectedOutputTable.DESCRIPTION_COLUMN);
                    if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded())
                        refreshTableByCollapseThenExpand(rowSelected);
                }
            }
        }
    }

    private void editingForEOCellOfThrow(JTextField textField, int rowSelected, int colSelected) {
        expectedOutputTable.setValueAt(textField.getText(), rowSelected, ExpectedOutputTable.EXPECTED_COLUMN);
    }

    /**
     * Editing Expected Output Value Cell char pointer variable
     *
     * @param textField
     * @param rowSelected
     * @param colSelected
     */
    private void editingForEOCellOfCharPointer(JTextField textField, int rowSelected, int colSelected) {
        String newContent = textField.getText();
        if (newContent.equals(ExpectedOutputPanel.NULL)) {
            int result = JOptionPane.showConfirmDialog(getJFrameContainer(), ExpectedOutputPanel.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                expectedOutputTable.setValueAt(ExpectedOutputPanel.NO_DATA, rowSelected,
                        ExpectedOutputTable.DESCRIPTION_COLUMN);
                if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded())
                    collapse(rowSelected, false);
                else
                    ;
            } else
                textField.setText(dataCellBefore);
        } else {
            if (!newContent.startsWith("\""))
                newContent = "\"" + newContent;
            if (!newContent.endsWith("\""))
                newContent += "\"";
            textField.setText(newContent);
            expectedOutputTable.setValueAt(ExpectedOutputPanel.SIZE + (newContent.length() - 2), rowSelected,
                    ExpectedOutputTable.DESCRIPTION_COLUMN);
            expectedOutputTable.setValueAt(newContent, rowSelected, colSelected);
            if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded())
                refreshTableByCollapseThenExpand(rowSelected);
            else
                try {
                    expandOneLevelPointerVariableRow(rowSelected, newContent.length() - 2);
                } catch (Exception e) {

                }
        }
    }

    /**
     * Editing Expected Output Value Cell has number Variable: int, short, long,
     * double
     *
     * @param textField
     * @param rowSelected
     * @param colSelected
     */
    private void editingForEOCellOfNumber(JTextField textField, int rowSelected, int colSelected, boolean isInt) {
        String newContent = textField.getText();
        // Detect valid number
        String pattern = "-?\\d+";
        if (!isInt)
            pattern = "-?\\d+(\\.\\d+)?";
        if (!newContent.trim().matches(pattern)) {
            JOptionPane.showMessageDialog(getJFrameContainer(), ExpectedOutputPanel.INVALID_MESSAGE);
            textField.setText(dataCellBefore);
        } else
            for (int fi = rowSelected - 1; fi >= 0; fi--)
                if (expectedOutputTable.getTableConfig().getRowConfigs().get(fi).getTotalChildrenRow()
                        + fi >= rowSelected) {
                    String parentType = expectedOutputTable.getValueAt(fi, ExpectedOutputTable.TYPE_COLUMN).toString();
                    if (VariableTypes.isOneDimension(parentType) || VariableTypes.isOneLevel(parentType)) {
                        String newParentData = "{";
                        for (int se = 0; se < expectedOutputTable.getTableConfig().getRowConfigs().get(fi)
                                .getCountRealChildren(); se++) {
                            int currentRow = fi + se + 1;
                            if (currentRow == rowSelected)
                                newParentData += newContent + ",";
                            else
                                newParentData += expectedOutputTable.getValueAt(se + fi + 1,
                                        ExpectedOutputTable.EXPECTED_COLUMN) + ",";
                        }
                        newParentData = newParentData.substring(0, newParentData.length() - 1);
                        expectedOutputTable.setValueAt(newParentData + "}", fi, ExpectedOutputTable.EXPECTED_COLUMN);
                    }
                    break;
                }
    }

    /**
     * Editing Expected Output Value Cell has number array
     *
     * @param textField
     * @param rowSelected
     * @param colSelected
     */
    private void editingForEOCellOfNumberArray(JTextField textField, int rowSelected, int colSelected) {
        String newContent = textField.getText();
        String type = expectedOutputTable.getValueAt(rowSelected, ExpectedOutputTable.TYPE_COLUMN).toString();

        if (newContent.equals(ExpectedOutputPanel.NULL)) {
            int result = JOptionPane.showConfirmDialog(getJFrameContainer(), ExpectedOutputPanel.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                expectedOutputTable.setValueAt(ExpectedOutputPanel.NULL, rowSelected, colSelected);
                if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded())
                    collapse(rowSelected, false);
                else
                    ;
            } else
                textField.setText(dataCellBefore);
        } else {
            if (!newContent.startsWith("{"))
                newContent = "{" + newContent;
            if (!newContent.endsWith("}"))
                newContent += "}";
            textField.setText(newContent);

            int newSize = newContent.split(",").length;
            /**
             * 2 cases number[n] or number[]
             */
            // Unspecified size
            if (type.matches(".*\\[\\s*\\]")) {
                expectedOutputTable.setValueAt(newContent, rowSelected, colSelected);
                expectedOutputTable.setValueAt(ExpectedOutputPanel.SIZE + newSize, rowSelected,
                        ExpectedOutputTable.DESCRIPTION_COLUMN);
                if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded())
                    refreshTableByCollapseThenExpand(rowSelected);
                else
                    expandUnspecifiedOneDimensionArrayRow(rowSelected);
            } // Specified size
            else {
                List<String> indexes = Utils.getIndexOfArray(type);
                int size = Utils.toInt(indexes.get(0).replace(ExpectedOutputPanel.SIZE, ""));
                if (newSize > size) {
                    JOptionPane.showMessageDialog(getJFrameContainer(), "The new size is greater than the limit!");
                    textField.setText(dataCellBefore);
                } else {
                    expectedOutputTable.setValueAt(newContent, rowSelected, colSelected);
                    expectedOutputTable.setValueAt(ExpectedOutputPanel.SIZE + size, rowSelected,
                            ExpectedOutputTable.DESCRIPTION_COLUMN);
                    if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded())
                        refreshTableByCollapseThenExpand(rowSelected);
                    else
                        expandSpecifiedOneDimensionArrayRow(rowSelected, size);
                }
            }
        }
    }

    /**
     * Editing Expected Output Value Cell number pointer
     *
     * @param textField
     * @param rowSelected
     * @param colSelected
     */
    private void editingForEOCellOfNumberPointer(JTextField textField, int rowSelected, int colSelected) {
        String newContent = textField.getText();
        int newSize = textField.getText().split(",").length;
        if (!newContent.startsWith("{"))
            newContent = "{" + newContent;
        if (!newContent.endsWith("}"))
            newContent += "}";
        textField.setText(newContent);
        expectedOutputTable.setValueAt(ExpectedOutputPanel.SIZE + newSize, rowSelected,
                ExpectedOutputTable.DESCRIPTION_COLUMN);
        expectedOutputTable.setValueAt(newContent, rowSelected, colSelected);
        if (expectedOutputTable.getTableConfig().getRowConfigs().get(rowSelected).isExpanded())
            refreshTableByCollapseThenExpand(rowSelected);
        else
            try {
                expandOneLevelPointerVariableRow(rowSelected, newSize);
            } catch (Exception e) {

            }
    }

    /**
     * Editing Expected Output Value Cell has struct/class or bool pointer
     *
     * @param textField
     * @param rowSelected
     * @param colSelected
     */
    private void editingForEOCellOfStructurePointer(JTextField textField, int rowSelected, int colSelected) {
        String newContent = textField.getText();
        if (newContent.equals(ExpectedOutputPanel.NULL)) {
            int result = JOptionPane.showConfirmDialog(getJFrameContainer(), ExpectedOutputPanel.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                expectedOutputTable.setValueAt("unspecified size", rowSelected, ExpectedOutputTable.DESCRIPTION_COLUMN);
                collapse(rowSelected, false);
            } else
                textField.setText(dataCellBefore);
        }
    }

    /**
     * Expand one row
     *
     * @param selectedRowIndex: the row is selected now
     * @throws Exception
     */
    private void expand(int selectedRowIndex) throws Exception {
        /**
         * There are two cases:
         *
         * 1. Expand row - which have data before
         *
         * 2. Expand row - which haven't data before (new)
         */
        if (expectedOutputTable.getTableConfig().getRowConfigs().get(selectedRowIndex).isSaveBefore())
            loadDataIntoExpandedArea(selectedRowIndex);
        else {

            String type = expectedOutputTable.getValueAt(selectedRowIndex, ExpectedOutputTable.TYPE_COLUMN).toString();

            if (VariableTypes.isOneDimension(type)) {
                if (type.matches(".*\\[\\s*\\]"))
                    expandUnspecifiedOneDimensionArrayRow(selectedRowIndex);
                else
                    expandSpecifiedOneDimensionArrayRow(selectedRowIndex, ExpectedOutputPanel.UNSPECIFIED_SIZE);
            } else if (VariableTypes.isOneLevel(type))
                expandOneLevelPointerVariableRow(selectedRowIndex, -1);
            else if (VariableTypes.isBasic(type)) {
                // nothing to do

            } else if (VariableTypes.getType(type) == VariableTypes.STRUCTURE.SIMPLE_STRUCTURE_REGEX)
                expandBasicStructureVariableRow(selectedRowIndex);
        }

    }

    /**
     * Expand for Basic Structure Variable
     *
     * @param selectedRowIndex
     * @throws Exception
     */
    private void expandBasicStructureVariableRow(int selectedRowIndex) throws Exception {

        expectedOutputTable.setValueAt(ExpectedOutputPanel.NO_DATA, selectedRowIndex,
                ExpectedOutputTable.EXPECTED_COLUMN);

        VariableNode node = (VariableNode) expectedOutputTable.getValueAt(selectedRowIndex, 0);
        String varNodeName = node.getNewType();

        if (node.resolveCoreType() instanceof StructureNode) {

            StructureNode structNode = (StructureNode) node.resolveCoreType();
            List<IVariableNode> varNodes = structNode.getAttributes();

            for (IVariableNode varNode : varNodes) {
                IVariableNode cloneVar = (IVariableNode) varNode.clone();
                cloneVar.setName(ExpectedOutputPanel.TAB + varNodeName + "." + cloneVar.getNewType());

                Vector<Object> rowData = new Vector<>();
                rowData.add(cloneVar);
                rowData.add(cloneVar.getRawType());

                String type = cloneVar.getRawType();
                if (VariableTypes.isOneLevel(type)) {
                    rowData.add(ExpectedOutputPanel.NO_DATA);
                    rowData.add(ExpectedOutputPanel.NULL);

                } else if (VariableTypes.isBasic(type)
                        || VariableTypes.getType(type) == VariableTypes.STRUCTURE.SIMPLE_STRUCTURE_REGEX) {
                    rowData.add(ExpectedOutputPanel.NO_DATA);
                    rowData.add(ExpectedOutputPanel.NO_DATA);
                } else if (VariableTypes.isOneDimension(type)) {
                    if (type.matches(".*\\[\\s*\\]"))
                        rowData.add(ExpectedOutputPanel.NO_DATA);
                    else {
                        String sizeInStr = Utils.getIndexOfArray(type).get(0);
                        int size = Utils.toInt(sizeInStr);
                        rowData.add(ExpectedOutputPanel.SIZE + size);
                    }
                    rowData.add(ExpectedOutputPanel.NO_DATA);
                } else {
                    rowData.add(ExpectedOutputPanel.NO_DATA);
                    rowData.add(ExpectedOutputPanel.UNSUPPORTED);
                }

                expectedOutputTable.addRow(rowData, selectedRowIndex + varNodes.indexOf(varNode) + 1);
            }
            expectedOutputTable.getTableConfig().addChildrenToRow(selectedRowIndex, varNodes.size(), varNodes.size(),
                    false);
            detectParentRow_Expand(selectedRowIndex, varNodes.size());
        }
    }

    /**
     * Expand for One Level Pointer
     *
     * @param selectedRowIndex
     * @throws Exception
     */
    private void expandOneLevelPointerVariableRow(int selectedRowIndex, int size) throws Exception {

        String type = expectedOutputTable.getValueAt(selectedRowIndex, ExpectedOutputTable.TYPE_COLUMN).toString();
        /**
         * There are two cases:
         *
         */
        if (size < 0) {
            String sizeInStr = expectedOutputTable.getValueAt(selectedRowIndex, ExpectedOutputTable.DESCRIPTION_COLUMN)
                    .toString();
            if (sizeInStr.contains(ExpectedOutputPanel.SIZE))
                sizeInStr = sizeInStr.replace(ExpectedOutputPanel.SIZE, "");
            size = Utils.toInt(sizeInStr);
            if (size < 0)
                size = showInputDialog(ExpectedOutputPanel.SIZE_MESSAGE);
            if (size < 0) {
                JOptionPane.showMessageDialog(getJFrameContainer(), ExpectedOutputPanel.INVALID_MESSAGE);
                return;
            }
        }
        if (size >= 0) {
            expectedOutputTable.setValueAt(ExpectedOutputPanel.SIZE + size, selectedRowIndex,
                    ExpectedOutputTable.DESCRIPTION_COLUMN);

            VariableNode parentNode = (VariableNode) expectedOutputTable.getValueAt(selectedRowIndex, 0);
            String tempName = ExpectedOutputPanel.TAB + parentNode.getNewType();

            for (int fi = 0; fi < size; fi++) {
                VariableNode cloneVarNode = new VariableNode();
                cloneVarNode.setName(tempName + "[" + fi + "]");
                cloneVarNode.setCoreType(parentNode.getCoreType());

                String rawType = parentNode.getRawType();
                if (VariableTypes.isOneLevel(rawType))
                    rawType = rawType.substring(0, rawType.length() - 1);
                else if (VariableTypes.isOneDimension(rawType))
                    rawType = rawType.substring(0, rawType.lastIndexOf("["));

                cloneVarNode.setRawType(rawType);
                cloneVarNode.setParent(parentNode.getParent());

                Vector<Object> rowData = new Vector<>();
                rowData.add(cloneVarNode);
                rowData.add(cloneVarNode.getRawType());
                rowData.add(ExpectedOutputPanel.NO_DATA);
                /**
                 *
                 */
                if (VariableTypes.isOneLevel(rawType))
                    rowData.add(ExpectedOutputPanel.NULL);
                else
                    rowData.add(ExpectedOutputPanel.NO_DATA);
                expectedOutputTable.addRow(rowData, selectedRowIndex + 1 + fi);
            }

            // if it has saved data, must re-write for its children
            String data = expectedOutputTable.getValueAt(selectedRowIndex, ExpectedOutputTable.EXPECTED_COLUMN)
                    .toString();
            if (!data.equals(ExpectedOutputPanel.NO_DATA) && !data.equals(ExpectedOutputPanel.NULL)) {
                expectedOutputTable.getTableConfig().removeChildrenOfRow(selectedRowIndex, false);

                if (!type.contains("char")) {
                    data = data.replace("{", "");
                    data = data.replace("}", "");
                    List<String> childrenData = Arrays.asList(data.split(","));
                    for (int fi = 0; fi < childrenData.size(); fi++)
                        expectedOutputTable.setValueAt(childrenData.get(fi), selectedRowIndex + fi + 1,
                                ExpectedOutputTable.EXPECTED_COLUMN);
                } else {
                    data = data.replace("\"", "");
                    for (int fi = 0; fi < data.length(); fi++)
                        expectedOutputTable.setValueAt("'" + data.charAt(fi) + "'", selectedRowIndex + fi + 1,
                                ExpectedOutputTable.EXPECTED_COLUMN);
                }
            } else
                expectedOutputTable.setValueAt(ExpectedOutputPanel.NO_DATA, selectedRowIndex,
                        ExpectedOutputTable.EXPECTED_COLUMN);
            expectedOutputTable.getTableConfig().addChildrenToRow(selectedRowIndex, size, size, true);
            detectParentRow_Expand(selectedRowIndex, size);
        }
    }

    /**
     * Expand Array - have its size
     *
     * @param selectedRowIndex
     * @param size
     */
    private void expandSpecifiedOneDimensionArrayRow(int selectedRowIndex, int size) {

        String type = expectedOutputTable.getValueAt(selectedRowIndex, ExpectedOutputTable.TYPE_COLUMN).toString();

        if (size == ExpectedOutputPanel.UNSPECIFIED_SIZE) {
            String indexInStr = Utils.getIndexOfArray(type).get(0);
            size = Utils.toInt(indexInStr);
        }

        VariableNode parentNode = (VariableNode) expectedOutputTable.getValueAt(selectedRowIndex, 0);
        String tempName = ExpectedOutputPanel.TAB + parentNode.getNewType();

        for (int fi = 0; fi < size; fi++) {
            String rawType = Utils.getNameVariable(parentNode.getRawType());

            VariableNode cloneVarNode = new VariableNode();
            cloneVarNode.setName(tempName + "[" + fi + "]");
            cloneVarNode.setCoreType(parentNode.getCoreType());
            cloneVarNode.setRawType(rawType);
            cloneVarNode.setParent(parentNode.getParent());

            Vector<Object> rowData = new Vector<>();
            rowData.add(cloneVarNode);
            rowData.add(cloneVarNode.getRawType());
            rowData.add(ExpectedOutputPanel.NO_DATA);

            if (VariableTypes.isOneLevel(rawType))
                rowData.add(ExpectedOutputPanel.NULL);
            else
                rowData.add(ExpectedOutputPanel.NO_DATA);
            expectedOutputTable.addRow(rowData, selectedRowIndex + 1 + fi);
        }

        // if it has saved data, must re-write for its children
        String data = expectedOutputTable.getValueAt(selectedRowIndex, ExpectedOutputTable.EXPECTED_COLUMN).toString();
        if (!data.equals(ExpectedOutputPanel.NO_DATA) && !data.equals(ExpectedOutputPanel.NULL)) {
            expectedOutputTable.getTableConfig().removeChildrenOfRow(selectedRowIndex, false);

            if (!type.contains("char")) {
                data = data.replace("{", "");
                data = data.replace("}", "");
                List<String> childrenData = Arrays.asList(data.split(","));
                for (int fi = 0; fi < childrenData.size(); fi++)
                    expectedOutputTable.setValueAt(childrenData.get(fi), selectedRowIndex + fi + 1,
                            ExpectedOutputTable.EXPECTED_COLUMN);
            } else {
                data = data.replace("\"", "");
                for (int fi = 0; fi < data.length(); fi++)
                    expectedOutputTable.setValueAt("'" + data.charAt(fi) + "'", selectedRowIndex + fi + 1,
                            ExpectedOutputTable.EXPECTED_COLUMN);
            }
        } else
            expectedOutputTable.setValueAt(ExpectedOutputPanel.NO_DATA, selectedRowIndex,
                    ExpectedOutputTable.EXPECTED_COLUMN);
        expectedOutputTable.setValueAt(ExpectedOutputPanel.SIZE + size, selectedRowIndex,
                ExpectedOutputTable.DESCRIPTION_COLUMN);
        expectedOutputTable.getTableConfig().getRowConfigs().get(selectedRowIndex).setCountRealChildren(size);

        expectedOutputTable.getTableConfig().addChildrenToRow(selectedRowIndex, size, size, true);
        detectParentRow_Expand(selectedRowIndex, size);
    }

    /**
     * Expand for Array - don't know its size
     *
     * @param selectedRowIndex
     */
    private void expandUnspecifiedOneDimensionArrayRow(int selectedRowIndex) {

        int size = ExpectedOutputPanel.UNSPECIFIED_SIZE;

        String sizeInStr = expectedOutputTable.getValueAt(selectedRowIndex, ExpectedOutputTable.DESCRIPTION_COLUMN)
                .toString();
        sizeInStr = sizeInStr.replace(ExpectedOutputPanel.SIZE, "");
        size = Utils.toInt(sizeInStr);
        if (size < 0)
            size = showInputDialog(ExpectedOutputPanel.SIZE_MESSAGE);
        if (size >= 0)
            expandSpecifiedOneDimensionArrayRow(selectedRowIndex, size);
        else
            JOptionPane.showMessageDialog(getJFrameContainer(), ExpectedOutputPanel.INVALID_MESSAGE);
    }

    public JButton getBtnSubmit() {
        return btnSubmit;
    }

    public ExpectedOutputTable getExpectedOutputTable() {
        return expectedOutputTable;
    }

    public void setExpectedOutputTable(ExpectedOutputTable expectedOutputTable) {
        this.expectedOutputTable = expectedOutputTable;
    }

    private JFrame getJFrameContainer() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    public List<Integer> getListRootIndex() {
        List<Integer> listRootIndex = new ArrayList<>();
        for (int fi = 0; fi < expectedOutputTable.getRowCount(); fi++)
            if (isRoot(fi))
                listRootIndex.add(fi);
        return listRootIndex;
    }

    public ExpectedOutputTableConfig getTableConfig() {
        return expectedOutputTable.getTableConfig();
    }

    public void setTableConfig(ExpectedOutputTableConfig tableConfig) {
        expectedOutputTable.setTableConfig(tableConfig);
    }

    /**
     * Initialie all components of this panel
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        belowPanel = new JPanel();
        belowPanel.setBackground(Color.WHITE);
        this.add(mainPanel, BorderLayout.CENTER);

        btnSubmit = new JButton("Save expected output");
        belowPanel.add(btnSubmit);
        this.add(belowPanel, BorderLayout.PAGE_END);

        // GroupHeaderColumnTable groupHeaderEOTable = new
        // GroupHeaderColumnTable();
        expectedOutputTable = new ExpectedOutputTable(ExpectedOutputPanel.COLUMNS_NAME);
        Font font = expectedOutputTable.getTableHeader().getFont();
        expectedOutputTable.getTableHeader().setFont(font.deriveFont(IContentPanelDisplay.FONT_SIZE));

        JScrollPane scrollPane = new JScrollPane(expectedOutputTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        cellEditorActionListener = new CellEditorActionListener();
        tableMouseListener = new TableMouseListener();
    }

    /**
     * Initialize Expected Output Table
     *
     * @param expectedNodeTypes
     * @throws Exception
     */
    public void initializeTable(List<IVariableNode> expectedNodeTypes) throws Exception {
        int countVars = 0;
        for (IVariableNode var : expectedNodeTypes) {
            IVariableNode cloneNode = (IVariableNode) var.clone();
            cloneNode.setName(INameRule.EXPECTED_OUTPUT_PREFIX + var.getNewType());

            Vector<Object> rowData = new Vector<>();
            rowData.addElement(cloneNode);
            rowData.add(cloneNode.getReducedRawType());

            rowData.add(ExpectedOutputPanel.NO_DATA);
            if (VariableTypes.isOneLevel(var.getRawType()))
                rowData.add(ExpectedOutputPanel.NULL);

            else if (VariableTypes.isBasic(var.getRawType()) || VariableTypes.isOneDimension(var.getRawType())
                    || VariableTypes.getType(var.getRawType()) == VariableTypes.STRUCTURE.SIMPLE_STRUCTURE_REGEX
                    || VariableTypes.isThrow(var.getRawType()))
                rowData.add(ExpectedOutputPanel.NO_DATA);
            else
                rowData.add(ExpectedOutputPanel.UNSUPPORTED);

            expectedOutputTable.addRow(rowData);
            countVars++;
        }
        expectedOutputTable.setTableConfig(new ExpectedOutputTableConfig(countVars));
    }

    /**
     * Detect one row is the root - which not have parent
     *
     * @param rowIndex
     * @return
     */
    public boolean isRoot(int rowIndex) {
        if (expectedOutputTable.getTableConfig() != null && expectedOutputTable.getTableConfig().getRowConfigs() != null
                && expectedOutputTable.getTableConfig().getRowConfigs().size() > 0)
            for (int fi = rowIndex - 1; fi >= 0; fi--) {
                RowConfig rowConfig = expectedOutputTable.getTableConfig().getRowConfigs().get(fi);
                if (fi + rowConfig.getTotalChildrenRow() >= rowIndex)
                    return false;
            }
        return true;
    }

    /**
     * Load saved data into Table
     *
     * @param rowIndex
     * @throws Exception
     */
    public void loadDataIntoExpandedArea(int rowIndex) throws Exception {
        int totalChildrenRow = 0;
        int currentRow = rowIndex;
        RowConfig rowConfig = expectedOutputTable.getTableConfig().getRowConfigs().get(rowIndex);
        if (rowConfig.getRowConfigsChildren() == null || rowConfig.getRowConfigsChildren().size() == 0)
            return;
        int countRealChildren = rowConfig.getRowConfigsChildren().get(0).getCountRealChildren();

        String typeInStr = expectedOutputTable.getValueAt(rowIndex, ExpectedOutputTable.TYPE_COLUMN).toString();
        if (VariableTypes.isOneLevel(typeInStr))
            expandOneLevelPointerVariableRow(rowIndex, -1);
        else if (VariableTypes.isOneDimension(typeInStr)) {
            if (typeInStr.matches(".*\\[\\s*\\]") && countRealChildren == 0)
                expandUnspecifiedOneDimensionArrayRow(rowIndex);
            else
                expandSpecifiedOneDimensionArrayRow(rowIndex, countRealChildren);
        } else if (VariableTypes.isBasic(typeInStr)) {
            // nothing to do
        } else
            expandBasicStructureVariableRow(rowIndex);
        /**
         *
         */
        totalChildrenRow += countRealChildren;
        currentRow += 1;

        while (totalChildrenRow < rowConfig.getRowConfigsChildren().get(0).getTotalChildrenRow()) {
            int countNewChildren = rowConfig.getRowConfigsChildren().get(currentRow - rowIndex).getCountRealChildren();
            if (countNewChildren > 0) {
                if (rowConfig.getRowConfigsChildren().get(currentRow - rowIndex).isArray()) {
                    String type = expectedOutputTable.getValueAt(currentRow, ExpectedOutputTable.TYPE_COLUMN)
                            .toString();
                    if (type.endsWith("*"))
                        expandOneLevelPointerVariableRow(currentRow, countNewChildren);
                    else if (type.endsWith("]"))
                        expandSpecifiedOneDimensionArrayRow(currentRow, countNewChildren);
                    else
                        ;
                } else
                    expandBasicStructureVariableRow(currentRow);
                totalChildrenRow += countNewChildren;
            }
            currentRow++;
        }

        // Re-write data which is put in before
        for (int fi = rowIndex; fi <= rowConfig.getRowConfigsChildren().get(0).getTotalChildrenRow() + rowIndex; fi++) {
            int index = fi - rowIndex;
            RowConfig currentRowConfig = rowConfig.getRowConfigsChildren().get(index);
            expectedOutputTable.getTableConfig().getRowConfigs().set(fi, currentRowConfig);

            expectedOutputTable.setValueAt(currentRowConfig.getEOAttribute(), fi,
                    ExpectedOutputTable.DESCRIPTION_COLUMN);
            if (index == 0 && VariableTypes.getType(typeInStr) == VariableTypes.STRUCTURE.SIMPLE_STRUCTURE_REGEX
                    && (expectedOutputTable.getValueAt(rowIndex, ExpectedOutputTable.EXPECTED_COLUMN)
                    .equals(ExpectedOutputPanel.UNSUPPORTED)
                    || currentRowConfig.getEOValue().equals(ExpectedOutputPanel.UNSUPPORTED)))
                expectedOutputTable.setValueAt(ExpectedOutputPanel.NO_DATA, rowIndex,
                        ExpectedOutputTable.EXPECTED_COLUMN);
            else
                expectedOutputTable.setValueAt(currentRowConfig.getEOValue(), fi, ExpectedOutputTable.EXPECTED_COLUMN);
        }
    }

    /**
     * @param selectedRowIndex
     */
    private void refreshTableByCollapseThenExpand(int selectedRowIndex) {
        collapse(selectedRowIndex, false);
        try {
            expand(selectedRowIndex);
        } catch (Exception e) {

        }
    }

    public void removeAllRows() {
        JTextField textField = expectedOutputTable.getTextEditor();
        textField.removeActionListener(cellEditorActionListener);
        expectedOutputTable.removeMouseListener(tableMouseListener);

        expectedOutputTable.removeAllRow();
    }

    private void setExpandOrCollapseorRewriteEvent() {
        expectedOutputTable.addMouseListener(tableMouseListener);
    }

    public void setExpectedNodeTypes(List<IVariableNode> expectedNodeTypes) throws Exception {
        initializeTable(expectedNodeTypes);

        setExpandOrCollapseorRewriteEvent();

        setModifyExpectedEvent();
    }

    private void setModifyExpectedEvent() {
        JTextField textField = expectedOutputTable.getTextEditor();
        cellEditorActionListener.setTextField(textField);
        textField.addActionListener(cellEditorActionListener);
    }

    private int showInputDialog(String msg) {
        int size = ExpectedOutputPanel.UNSPECIFIED_SIZE;
        String sizeInStr = JOptionPane.showInputDialog(getJFrameContainer(), msg);
        size = Utils.toInt(sizeInStr);
        return size;
    }

    /**
     * Class event listener one cell
     *
     * @author Duong Td
     */
    public class CellEditorActionListener implements ActionListener {

        private JTextField textField;

        public CellEditorActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int colSelected = expectedOutputTable.getSelectedColumn();
            int rowSelected = expectedOutputTable.getSelectedRow();
            String type = expectedOutputTable.getValueAt(rowSelected, ExpectedOutputTable.TYPE_COLUMN).toString();

            fillExpectedOuput(type, colSelected, rowSelected);
        }

        private void fillExpectedOuput(String type, int colSelected, int rowSelected) {
            if (VariableTypes.isNumBasic(type))
                switch (VariableTypes.getType(type)) {
                    /**
                     * Number: float, double
                     */
                    case VariableTypes.BASIC.NUMBER.FLOAT.FLOAT:
                    case VariableTypes.BASIC.NUMBER.FLOAT.DOUBLE:

                    case VariableTypes.BASIC.NUMBER.FLOAT.FLOAT + VariableTypes.REFERENCE:
                    case VariableTypes.BASIC.NUMBER.FLOAT.DOUBLE + VariableTypes.REFERENCE:
                        editingForEOCellOfNumber(textField, rowSelected, colSelected, false);
                        break;

                    /**
                     * Boolean
                     */
                    case VariableTypes.BASIC.NUMBER.INTEGER.BOOL:
                    case VariableTypes.BASIC.NUMBER.INTEGER.BOOL + VariableTypes.REFERENCE:
                        editingForEOCellOfBoolean(textField, rowSelected, colSelected);
                        break;

                    default:
                        editingForEOCellOfNumber(textField, rowSelected, colSelected, true);
                }

            else if (VariableTypes.isChBasic(type))
                editingForEOCellOfChar(textField, rowSelected, colSelected);

            else if (VariableTypes.isChOneDimension(type)) {
                if (colSelected == ExpectedOutputTable.EXPECTED_COLUMN)
                    editingForEOCellOfCharArray(textField, rowSelected, colSelected);

            } else if (VariableTypes.isChOneLevel(type)) {
                if (colSelected == ExpectedOutputTable.EXPECTED_COLUMN)
                    editingForEOCellOfCharPointer(textField, rowSelected, colSelected);

            } else if (VariableTypes.isNumOneDimension(type)) {
                if (colSelected == ExpectedOutputTable.EXPECTED_COLUMN)
                    editingForEOCellOfNumberArray(textField, rowSelected, colSelected);

            } else if (VariableTypes.isNumOneLevel(type)) {
                if (colSelected == ExpectedOutputTable.EXPECTED_COLUMN)
                    editingForEOCellOfNumberPointer(textField, rowSelected, colSelected);

            } else
                switch (VariableTypes.getType(type)) {

                    /**
                     * Structure pointer, structure array unspecified Boolean pointer, boolean array
                     * unspecified
                     */
                    case VariableTypes.STRUCTURE.ONE_LEVEL_STRUCTURE_REGEX:
                    case VariableTypes.STRUCTURE.ONE_DIMENSION_STRUCTURE_REGEX:
                    case VariableTypes.BASIC.NUMBER.INTEGER.BOOL + VariableTypes.ONE_DIMENSION:
                    case VariableTypes.BASIC.NUMBER.INTEGER.BOOL + VariableTypes.ONE_LEVEL:
                        if (colSelected == ExpectedOutputTable.EXPECTED_COLUMN)
                            editingForEOCellOfStructurePointer(textField, rowSelected, colSelected);
                        break;

                    case VariableTypes.THROW:
                        if (colSelected == ExpectedOutputTable.EXPECTED_COLUMN)
                            editingForEOCellOfThrow(textField, rowSelected, colSelected);
                        break;
                    case VariableTypes.STRUCTURE.SIMPLE_STRUCTURE_REGEX:
                        if (colSelected == ExpectedOutputTable.EXPECTED_COLUMN) {
                            ProjectParser parser = new ProjectParser(new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH));

                            List<SearchCondition> conditions = new ArrayList<>();
                            conditions.add(new EnumNodeCondition());
                            conditions.add(new TypedefNodeCondition());

                            List<INode> mydefines = Search.searchNodes(parser.getRootTree(), conditions);

                            for (INode mydefine : mydefines) {
                                if (mydefine.getNewType().equals(type)) {
                                    if (mydefine instanceof EnumNode) {
                                        for (String child : ((EnumNode) mydefine).getAllNameEnumItems()) {
                                            if (child.equals(textField.getText())) {
                                                editingForEOCellOfThrow(textField, rowSelected, colSelected);
                                                return;
                                            }
                                        }
                                    } else if (mydefine instanceof PrimitiveTypedefDeclaration) {
                                        fillExpectedOuput(((PrimitiveTypedefDeclaration) mydefine).getOldType(), colSelected, rowSelected);
                                        return;
                                    }
                                }
                            }
                        }

                    default: {
                        JOptionPane.showMessageDialog(getJFrameContainer(), "This variable " + type + " is not supported");
                        textField.setText(dataCellBefore);
                    }
                }
        }

        public JTextField getTextField() {
            return textField;
        }

        public void setTextField(JTextField textField) {
            this.textField = textField;
        }
    }

    public class TableMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
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
            int rowSelectedNow = expectedOutputTable.getSelectedRow();
            boolean canBeExpandedorCollapsed = canBeExpandedorCollapsed();
            boolean isConsumed = e.isConsumed();
            boolean isEdit = expectedOutputTable.isCellEditable(rowSelectedNow,
                    expectedOutputTable.getSelectedColumn());
            int clickCount = e.getClickCount();

            if (!isConsumed)
                if (clickCount == ExpectedOutputPanel.DOUBLE_CLICK && canBeExpandedorCollapsed
                        && expectedOutputTable.getTableConfig() != null && !isEdit)
                    switch (expectedOutputTable.getActionAt(rowSelectedNow)) {
                        case ExpectedOutputTable.COLLAPSE_ACTION:

                            collapse(rowSelectedNow, true);
                            break;
                        case ExpectedOutputTable.EXPAND_ACTION:

                            try {
                                expand(rowSelectedNow);
                            } catch (Exception e1) {

                            }
                            break;
                        case ExpectedOutputTable.SAVE_ACTION:

                            try {
                                loadDataIntoExpandedArea(rowSelectedNow);
                            } catch (Exception e1) {

                            }
                            break;
                        case ExpectedOutputTable.NO_ACTION:
                            break;
                    }
                else if (clickCount == ExpectedOutputPanel.DOUBLE_CLICK && isEdit)
                    dataCellBefore = expectedOutputTable
                            .getValueAt(rowSelectedNow, expectedOutputTable.getSelectedColumn()).toString();
        }

    }
}
