package com.fit.utils.search;

import com.fit.tree.object.ExeNode;
import com.fit.tree.object.INode;

public class ExeNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ExeNode)
            return true;
        return false;
    }
}
