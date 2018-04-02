package com.fit.gui.testedfunctions;

import javax.swing.*;

/**
 * Cell editor for Tables
 *
 * @author Duong Td
 */
public class TextAreaEditor extends DefaultCellEditor {

    private static final long serialVersionUID = 1L;

    public TextAreaEditor() {
        super(new JTextField());
        final JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        editorComponent = scrollPane;

        delegate = new DefaultCellEditor.EditorDelegate() {

            private static final long serialVersionUID = 1L;

            @Override
            public Object getCellEditorValue() {
                return textArea.getText();
            }

            @Override
            public void setValue(Object value) {
                textArea.setText(value != null ? value.toString() : "");
            }
        };
    }
}
