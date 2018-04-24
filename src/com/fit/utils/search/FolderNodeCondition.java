package com.fit.utils.search;

import com.fit.tree.object.FolderNode;
import com.fit.tree.object.INode;

public class FolderNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof FolderNode)
            return true;
        return false;
    }
}
