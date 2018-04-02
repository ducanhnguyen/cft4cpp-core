package com.fit.gui.swing;

import com.fit.utils.UtilsVu;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;

/**
 * Một canvas cuộn để hiển thị nội dung một tập tin
 */
public class FileView extends JScrollPane implements LightTabbedPane.EqualsConstruct, ComponentListener {

    private static final long serialVersionUID = -5837930482904734094L;
    private static Font FONT = new Font("Consolas", Font.PLAIN, 12);
    private File file;
    private JTextArea textPane;

    private IASTFileLocation hl;

    /**
     * Tạo một canvas cuộn từ tập tin tương ứng
     */
    public FileView(File file) {
        this.file = file;
        setBorder(null);
        /**
         * Cài view hiển thị màu code
         */
        textPane = new RSyntaxTextArea();
        ((RSyntaxTextArea) textPane).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        ((RSyntaxTextArea) textPane).setCodeFoldingEnabled(true);

        /**
         *
         */
        textPane.setBorder(null);
        textPane.setBackground(Color.WHITE);
        textPane.setEditable(false);
        textPane.setFont(FileView.FONT);

        textPane.setWrapStyleWord(true);
        textPane.setLineWrap(true);

        DefaultCaret caret = (DefaultCaret) textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        setViewportView(textPane);

        String content;
        try {
            content = UtilsVu.getContentFile(file);
        } catch (IOException e) {
            content = e.getMessage();
        }
        textPane.setText(content);

        textPane.addComponentListener(this);
    }

    private boolean checkShowing() {
        return textPane.isShowing() && textPane.getSize().width > 0;
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (checkShowing() && hl != null) {
            hightLightImidiately(hl);
            hl = null;
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public boolean equalsConstruct(Object... constructItem) {
        return file.equals(constructItem[0]);
    }

    private void hightLightImidiately(IASTFileLocation loc) {
        int off = loc.getNodeOffset(), len = loc.getNodeLength();

        try {
            Rectangle viewRect = textPane.modelToView(off);
            int h = getViewport().getSize().height / 2;
            viewRect.y += h;

            textPane.requestFocusInWindow();
            textPane.scrollRectToVisible(viewRect);
            textPane.setCaretPosition(off);
            textPane.moveCaretPosition(off + len);
        } catch (Exception e) {

        }
    }

    public void setHightLight(IASTFileLocation loc) {
        if (checkShowing())
            hightLightImidiately(loc);
        else
            hl = loc;
    }
}
