package com.fit.gui.testedfunctions;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Cell Renderer of Expected Output Table
 *
 * @author Duong Td
 */
public class EOTableCellRenderer extends MyTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final String FONT_NAME = "Arial";
    private static final int FONT_SIZE = 13;
    private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();
    private Font FONT = new Font(EOTableCellRenderer.FONT_NAME, Font.PLAIN, EOTableCellRenderer.FONT_SIZE);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        ExpectedOutputTable expectedOutputTable = (ExpectedOutputTable) table;
        int fontStyle = -1;

        if (expectedOutputTable.getTableConfig() == null)
            fontStyle = Font.PLAIN;
        else {
            int action = expectedOutputTable.getActionAt(row);
            if (action == ExpectedOutputTable.EXPAND_ACTION || action == ExpectedOutputTable.SAVE_ACTION)
                fontStyle = Font.BOLD;
            else if (action == ExpectedOutputTable.COLLAPSE_ACTION || action == ExpectedOutputTable.NO_ACTION)
                fontStyle = Font.PLAIN;

        }
        FONT = new Font(EOTableCellRenderer.FONT_NAME, fontStyle, EOTableCellRenderer.FONT_SIZE);
        adaptee.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
        setForeground(adaptee.getForeground());
        setBackground(adaptee.getBackground());
        setBorder(adaptee.getBorder());
        setFont(FONT);
        setText(adaptee.getText());

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
