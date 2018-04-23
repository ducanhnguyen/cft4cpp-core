package com.fit.gui.testedfunctions;

import com.fit.config.AbstractSetting;
import com.fit.testdatagen.testdatainit.VariableTypes;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class ExpectedOutputTable extends JTable {

    public static final int NAME_EXPECTED_OUTPUT_COLUMN = 0;
    public static final int TYPE_COLUMN = 1;
    public static final int DESCRIPTION_COLUMN = 2;
    public static final int EXPECTED_COLUMN = 3;
    public static final int NO_ACTION = -1;
    public static final int EXPAND_ACTION = 0;
    public static final int COLLAPSE_ACTION = 1;
    public static final int SAVE_ACTION = 2;
    private static final long serialVersionUID = 1L;
    private final String KEY_NAME_SAVE = "EXPECTED_OUTPUT_TABLE_COLUMNS_SIZE";
    public EOTableModel tableModel;
    private ExpectedOutputTableConfig tableConfig;
    private EOTableCellRenderer tableCellRenderer;

    private JTextField textEditor;
    private double[] columnsSize;

    public ExpectedOutputTable(EOTableModel eoTableModel) {
        super(eoTableModel);
        tableModel = eoTableModel;
        this.setModel(tableModel);
        setConstructorForTable();
    }

    public ExpectedOutputTable(Vector<Object> colNames) {
        super();
        tableModel = new EOTableModel(colNames);
        this.setModel(tableModel);
        setConstructorForTable();
    }

    public ExpectedOutputTable(Vector<Vector<Object>> data, Vector<Object> colNames) {
        super();
        tableModel = new EOTableModel(colNames, data);
        this.setModel(tableModel);
        setConstructorForTable();
    }

    public void addRow(Object[] rowDt) {
        tableModel.addRow(new Vector<>(Arrays.asList(rowDt)));

    }

    public void addRow(Vector<Object> rowDt) {
        for (int fi = rowDt.size(); fi < tableModel.getColumnCount(); fi++)
            rowDt.add(null);
        tableModel.addRow(rowDt);
    }

    public void addRow(Vector<Object> rowDt, int index) {
        for (int fi = rowDt.size(); fi < tableModel.getColumnCount(); fi++)
            rowDt.add(null);
        tableModel.addRow(rowDt, index);
    }

    public int getActionAt(int row) {
        String data = getValueAt(row, ExpectedOutputTable.TYPE_COLUMN).toString();
        if (tableConfig.getRowConfigs().get(row).isExpanded() && !VariableTypes.isBasic(data))
            return ExpectedOutputTable.COLLAPSE_ACTION;
        else if (tableConfig.getRowConfigs().get(row).isSaveBefore() && !VariableTypes.isBasic(data))
            return ExpectedOutputTable.SAVE_ACTION;
        else if (VariableTypes.isBasic(data))
            return ExpectedOutputTable.NO_ACTION;
        else
            return ExpectedOutputTable.EXPAND_ACTION;
    }

    public TableCellRenderer getTableCellRenderer() {
        return tableCellRenderer;
    }

    public ExpectedOutputTableConfig getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(ExpectedOutputTableConfig tableConfig) {
        this.tableConfig = tableConfig;
    }

    public EOTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(EOTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public JTextField getTextEditor() {
        return textEditor;
    }

    @Override
    public Object getValueAt(int x, int y) {
        return tableModel.getValueAt(x, y);
    }

    public void initializeColumnWidthSize() {
        double tableWidth = getWidth();
        int columnCount = getColumnCount();
        String strSizeSaved = AbstractSetting.getValue(KEY_NAME_SAVE);
        if (strSizeSaved != null && !strSizeSaved.equals("")) {

            String[] columnsSizeInStr = strSizeSaved.split(",");
            if (columnsSizeInStr.length == columnCount)
                for (int fi = 0; fi < columnCount; fi++) {
                    double tempSize = Double.parseDouble(columnsSizeInStr[fi]);
                    int tempWidth = (int) Math.round(tempSize * tableWidth);
                    getColumnModel().getColumn(fi).setPreferredWidth(tempWidth);
                }
        }
    }

    public boolean isContainData(int rowIndex) {
        return !(getValueAt(rowIndex, ExpectedOutputTable.EXPECTED_COLUMN) == null
                || getValueAt(rowIndex, ExpectedOutputTable.EXPECTED_COLUMN).toString()
                .equals(ExpectedOutputPanel.NO_DATA));
    }

    public void removeAllRow() {
        for (int fi = tableModel.getRowCount() - 1; fi >= 0; fi--)
            tableModel.removeRow(fi);
        tableModel.fireTableDataChanged();
        saveColumnsSize();
    }

    public void removeRow(int index) {
        tableModel.removeRow(index);
    }

    public void restoreColumnsSize() {
        double tableWidth = getWidth();
        int columnCount = getColumnCount();
        for (int fi = 0; fi < columnCount; fi++)
            getColumnModel().getColumn(fi).setPreferredWidth((int) (columnsSize[fi] * tableWidth));
    }

    public double[] saveColumnsSize() {
        double tableWidth = getWidth();
        int columnCount = getColumnCount();
        columnsSize = new double[columnCount];
        for (int fi = 0; fi < columnCount; fi++)
            columnsSize[fi] = getColumnModel().getColumn(fi).getWidth() / tableWidth;
        return columnsSize;
    }

    public void saveColumnsSizeWhenClose() {
        double tableWidth = getWidth();
        int columnCount = getColumnCount();
        double[] newColumnsSize = new double[columnCount];
        String value = "";
        for (int fi = 0; fi < columnCount; fi++) {
            newColumnsSize[fi] = getColumnModel().getColumn(fi).getWidth() / tableWidth;
            value += newColumnsSize[fi] + ",";
        }
        AbstractSetting.setValue(KEY_NAME_SAVE, value.substring(0, value.length() - 1));
    }

    public void setConstructorForTable() {
        setOpaque(true);
        setBackground(Color.WHITE);
        getTableHeader().setResizingAllowed(true);
        setRowSelectionAllowed(true);
        setDragEnabled(false);
        getTableHeader().setReorderingAllowed(false);

        tableCellRenderer = new EOTableCellRenderer();
        textEditor = new JTextField();
        TableColumnModel tableColModel = getColumnModel();
        for (int fi = 0; fi < tableModel.getColumnCount(); fi++) {
            tableColModel.getColumn(fi).setCellRenderer(tableCellRenderer);
            tableColModel.getColumn(fi).setCellEditor(new DefaultCellEditor(textEditor));
            tableColModel.getColumn(fi).setMaxWidth(100000);
        }
    }

    /**
     * set new data for table
     *
     * @param rowDts
     */
    public void setData(Vector<Vector<Object>> rowDts) {
        for (int fi = 0; fi < tableModel.getRowCount(); fi++)
            tableModel.removeRow(fi);
        for (int fi = 0; fi < rowDts.size(); fi++)
            this.addRow(rowDts.get(fi));
    }

    public void setModel(EOTableModel dataModel) {
        this.setModel((TableModel) dataModel);
        tableModel = dataModel;
    }

    /**
     * Model for this table
     *
     * @author Duong Td
     */
    public class EOTableModel extends ListFunctionTableModel {

        private static final long serialVersionUID = 1L;

        public EOTableModel() {
            super();
            data = new Vector<>();
        }

        public EOTableModel(EOTableModel other) {
            super();
            columnNames = new Vector<>(other.getColumnNames());
            data = new Vector<>(other.getData());
        }

        public EOTableModel(Vector<Object> colNames) {
            columnNames = colNames;
            data = new Vector<>();
        }

        public EOTableModel(Vector<Object> colNames, Vector<Vector<Object>> d) {
            columnNames = colNames;
            data = d;
        }

        public void addRow(Vector<Object> newRow, int index) {
            data.add(index, newRow);
            fireTableRowsInserted(index, index);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == ExpectedOutputTable.NAME_EXPECTED_OUTPUT_COLUMN || col == ExpectedOutputTable.TYPE_COLUMN)
                return false;
            if (col == ExpectedOutputTable.DESCRIPTION_COLUMN) {
                String variableType = getValueAt(row, ExpectedOutputTable.TYPE_COLUMN).toString();
                if (VariableTypes.isOneLevel(variableType)
                        || VariableTypes.isOneDimension(variableType) && variableType.matches(".*\\[\\s*\\]"))
                    return true;
                else
                    return false;
            } else
                return true;
        }
    }

}
