package com.fit.gui.testedfunctions;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.fit.config.AbstractSetting;

/**
 * Table list all function tested
 *
 * @author Duong Td
 */
public class ListFunctionTable extends JTable {

    private static final long serialVersionUID = 1L;

    private final String KEY_NAME_SAVE = "LIST_FUNCTION_TABLE_COLUMNS_SIZE";

    public ListFunctionTableModel tableModel;

    public ListFunctionTable(Vector<Object> colNames) {
        super();
        Font font = getTableHeader().getFont();
        getTableHeader().setFont(font.deriveFont(IContentPanelDisplay.FONT_SIZE));
        tableModel = new ListFunctionTableModel(colNames);
        this.setModel(tableModel);
        setConstructorForTable();
    }

    public ListFunctionTable(Vector<Vector<Object>> data, Vector<Object> colNames) {
        super();
        Font font = getTableHeader().getFont();
        getTableHeader().setFont(font.deriveFont(IContentPanelDisplay.FONT_SIZE));
        tableModel = new ListFunctionTableModel(colNames, data);
        this.setModel(tableModel);
        setConstructorForTable();
    }

    public void addRow(Object[] rowDt) {
        tableModel.addRow(new Vector<>(Arrays.asList(rowDt)));
        tableModel.fireTableDataChanged();
    }

    public void addRow(Vector<Object> rowDt) {
        tableModel.addRow(rowDt);
        tableModel.fireTableDataChanged();
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public Object getValueAtXY(int x, int y) {
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

    public void removeAllRow() {
        for (int fi = tableModel.getRowCount() - 1; fi >= 0; fi--)
            tableModel.removeRow(fi);
        tableModel.fireTableDataChanged();
    }

    public void removeRow(int index) {
        tableModel.removeRow(index);
    }

    /**
     * Write size of all columns in this table
     */
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

        TableCellRenderer tableCellRenderer = new MyTableCellRenderer();
        TextAreaEditor textEditor = new TextAreaEditor();

        TableColumnModel tableColModel = getColumnModel();
        for (int fi = 0; fi < tableModel.getColumnCount(); fi++) {
            tableColModel.getColumn(fi).setCellRenderer(tableCellRenderer);
            tableColModel.getColumn(fi).setCellEditor(textEditor);
        }
    }

    public void setData(Vector<Vector<Object>> rowDts) {
        for (int fi = 0; fi < tableModel.getRowCount(); fi++)
            tableModel.removeRow(fi);
        for (int fi = 0; fi < rowDts.size(); fi++)
            this.addRow(rowDts.get(fi));
    }

    public void setModel(ListFunctionTableModel dataModel) {
        this.setModel((TableModel) dataModel);
        tableModel = dataModel;
    }
}
