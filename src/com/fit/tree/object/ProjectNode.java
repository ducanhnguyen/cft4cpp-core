package com.fit.tree.object;

import javax.swing.*;
import java.io.File;

public class ProjectNode extends Node implements IProjectNode, IHasFileNode {

    public ProjectNode() {
        try {
            Icon ICON_PROJECT = new ImageIcon(Node.class.getResource("/image/node/FolderNode.png"));

            if (ICON_PROJECT != null)
                setIcon(ICON_PROJECT);
        } catch (Exception e) {

        }
    }

    @Override
    public File getFile() {
        return new File(getAbsolutePath());
    }
}
