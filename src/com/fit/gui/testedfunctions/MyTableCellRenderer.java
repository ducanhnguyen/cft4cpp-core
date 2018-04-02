package com.fit.gui.testedfunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Cell renderer for tables
 *
 * @author Duong Td
 */
public class MyTableCellRenderer extends JTextArea implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final Font FONT = new Font("Arial", Font.PLAIN, 13);

    private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();

    @SuppressWarnings("rawtypes")
    private final Map<JTable, Map> cellSizes = new HashMap<>();

    public MyTableCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    @SuppressWarnings("rawtypes")
    protected void addSize(JTable table, int row, int column, int height) {
        @SuppressWarnings("unchecked")
        Map<Integer, Map> rows = cellSizes.get(table);
        if (rows == null)
            cellSizes.put(table, rows = new HashMap<>());
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> rowheights = rows.get(new Integer(row));
        if (rowheights == null)
            rows.put(new Integer(row), rowheights = new HashMap<>());
        rowheights.put(new Integer(column), new Integer(height));
    }

    protected int findMaximumRowSize(JTable table, int row) {
        Map<?, ?> rows = cellSizes.get(table);
        if (rows == null)
            return 0;
        Map<?, ?> rowheights = (Map<?, ?>) rows.get(new Integer(row));
        if (rowheights == null)
            return 0;
        int maximum_height = 0;
        for (Object name2 : rowheights.entrySet()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) name2;
            int cellHeight = ((Integer) entry.getValue()).intValue();
            maximum_height = Math.max(maximum_height, cellHeight);
        }
        return maximum_height;
    }

    protected int findTotalMaximumRowSize(JTable table, int row) {
        int maximum_height = 0;
        Enumeration<?> columns = table.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn tc = (TableColumn) columns.nextElement();
            TableCellRenderer cellRenderer = tc.getCellRenderer();
            if (cellRenderer instanceof MyTableCellRenderer) {
                MyTableCellRenderer tar = (MyTableCellRenderer) cellRenderer;
                maximum_height = Math.max(maximum_height, tar.findMaximumRowSize(table, row));
            }
        }
        return maximum_height;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        adaptee.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
        setForeground(adaptee.getForeground());
        setBackground(adaptee.getBackground());
        setBorder(adaptee.getBorder());
        setFont(MyTableCellRenderer.FONT);
        setText(adaptee.getText());

        // get the size of this cell
        TableColumnModel columnModel = table.getColumnModel();
        try {
            if (columnModel.getColumnCount() > column)
                this.setSize(columnModel.getColumn(column).getWidth(), 100000);
        } catch (Exception e) {

        }
        int height_wanted = (int) getPreferredSize().getHeight();
        addSize(table, row, column, height_wanted);
        height_wanted = findTotalMaximumRowSize(table, row);
        if (height_wanted != table.getRowHeight(row))
            try {
                table.setRowHeight(row, height_wanted);
            } catch (Exception e) {
            }
        return this;
    }
}
