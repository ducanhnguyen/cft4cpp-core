package com.fit.tree.object;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import javax.swing.*;
import java.io.File;

public class HeaderNode extends SourcecodeFileNode<IASTTranslationUnit> {
    public static final String HEADER_SIGNALS = ".h";

    public HeaderNode() {
        try {
            Icon ICON_HEADER = new ImageIcon(Node.class.getResource("/image/node/HeaderNode.png"));
            setIcon(ICON_HEADER);
        } catch (Exception e) {
        }
    }

    @Override
    public File getFile() {
        return new File(getAbsolutePath());
    }
}
