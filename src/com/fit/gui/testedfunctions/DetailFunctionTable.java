package com.fit.gui.testedfunctions;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.fit.config.AbstractSetting;

/**
 * @author Duong Td
 */
public class DetailFunctionTable extends JTable {

    private static final long serialVersionUID = 1L;
    private final String KEY_NAME_SAVE = "DETAIL_FUNCTION_TABLE_COLUMNS_SIZE";
    private DefaultTableModel tableModel;
    private double[] columnsSize;

    public DetailFunctionTable(Vector<Object> colNames) {
        super();
        Font font = getTableHeader().getFont();
        getTableHeader().setFont(font.deriveFont(IContentPanelDisplay.FONT_SIZE));
        DefaultTableModel tableModel = new DefaultTableModel(colNames, 0);
        setTableModel(tableModel);

        setCellSelectionEnabled(false);
        setRowSelectionAllowed(true);

        setContructorForTable();
    }

    public double[] getColumnsSize() {
        return columnsSize;
    }

    /**
     * Set size of columns - which saved before
     */
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
        this.repaint();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void removeAllRow() {
        for (int fi = tableModel.getRowCount() - 1; fi >= 0; fi--)
            tableModel.removeRow(fi);
        tableModel.fireTableDataChanged();
    }

    public void addRow(Object[] rowDt) {
        tableModel.addRow(new Vector<>(Arrays.asList(rowDt)));
        tableModel.fireTableDataChanged();
    }

    public void addRow(Vector<Object> rowDt) {
        tableModel.addRow(rowDt);
        tableModel.fireTableDataChanged();
    }

    /**
     * Restore size of columns by variable
     */
    public void restoreColumnsSize(double[] columnsSize) {
        // for (int fi = 0; fi < columnsSize.length; fi++) {
        // System.out.println(columnsSize[fi]);
        // }
        System.out.println("");
        double tableWidth = getWidth();
        int columnCount = getColumnCount();
        boolean check = true;
        for (int fi = 0; fi < columnCount; fi++)
            if (columnsSize[fi] >= 1 || columnsSize[fi] <= 0)
                check = false;
        if (check)
            for (int fi = 0; fi < columnCount; fi++) {
                int temp = (int) Math.round(columnsSize[fi] * tableWidth);
                getColumnModel().getColumn(fi).setPreferredWidth(temp);
            }
    }

    /**
     * Save size of columns by variable
     *
     * @return array of Table's columns size
     */
    public double[] saveColumnsSize() {
        double tableWidth = getWidth();
        int columnCount = getColumnCount();
        double[] columnsSize = new double[columnCount];
        for (int fi = 0; fi < columnCount; fi++)
            columnsSize[fi] = getColumnModel().getColumn(fi).getWidth() / tableWidth;
        return columnsSize;
    }

    /**
     * Save size of columns
     */
    public void saveColumnsSizeWhenClose() {
        double tableWidth = getWidth();
        int columnCount = getColumnCount();
        double[] newColumnsSize = new double[columnCount];
        // ghi lại
        String value = "";
        for (int fi = 0; fi < columnCount; fi++) {
            newColumnsSize[fi] = getColumnModel().getColumn(fi).getWidth() / tableWidth;
            value += newColumnsSize[fi] + ",";
        }
        AbstractSetting.setValue(KEY_NAME_SAVE, value.substring(0, value.length() - 1));
    }

    /**
     * Set something for Table
     */
    public void setContructorForTable() {
        setOpaque(true);
        setBackground(Color.WHITE);
        getTableHeader().setResizingAllowed(true);
        setRowSelectionAllowed(true);

        TableCellRenderer tableCellRenderer = new MyTableCellRenderer();
        TextAreaEditor textEditor = new TextAreaEditor();

        TableColumnModel tableColModel = getColumnModel();
        for (int fi = 0; fi < getModel().getColumnCount(); fi++) {
            tableColModel.getColumn(fi).setCellRenderer(tableCellRenderer);
            tableColModel.getColumn(fi).setCellEditor(textEditor);
            tableColModel.getColumn(fi).setMaxWidth(100000);
        }
    }

    public void setTableModel(DefaultTableModel model) {
        tableModel = model;
        setModel(tableModel);
        for (int fi = 0; fi < getColumnCount(); fi++) {
            TableColumn tableColumn1 = getTableHeader().getColumnModel().getColumn(fi);

            tableColumn1.setHeaderValue(IContentPanelDisplay.DETAIL_TABLE_HEADER.get(fi));
        }

        setContructorForTable();
    }
}
