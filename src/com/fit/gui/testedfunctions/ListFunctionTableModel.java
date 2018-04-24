package com.fit.gui.testedfunctions;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class ListFunctionTableModel extends AbstractTableModel {

    protected static final long serialVersionUID = 1L;

    protected Vector<Object> columnNames;

    protected Vector<Vector<Object>> data;

    public ListFunctionTableModel() {
        super();
        data = new Vector<>();
    }

    public ListFunctionTableModel(Vector<Object> colNames) {
        columnNames = colNames;
        data = new Vector<>();
    }

    public ListFunctionTableModel(Vector<Object> colNames, Vector<Vector<Object>> d) {
        columnNames = colNames;
        data = d;
    }

    public void addRow(Vector<Object> newRow) {
        data.addElement(newRow);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames.get(col).toString();
    }

    public Vector<Object> getColumnNames() {
        return columnNames;
    }

    public Vector<Vector<Object>> getData() {
        return data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data.get(row).get(col);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void removeRow(int row) {
        data.remove(row);
        fireTableDataChanged();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Vector<Object> rowTemp = data.get(row);
        rowTemp.set(col, value);
        data.set(row, rowTemp);
        fireTableCellUpdated(row, col);
    }
}
