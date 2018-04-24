package com.fit.tree.object;

import javax.swing.*;
import java.io.File;

public class FolderNode extends Node implements IHasFileNode {

    public FolderNode() {
        try {
            Icon ICON_FOLDER = new ImageIcon(Node.class.getResource("/image/node/FolderNode.png"));
            setIcon(ICON_FOLDER);
        } catch (Exception e) {

        }
    }

    @Override
    public File getFile() {
        return new File(getAbsolutePath());
    }
}
