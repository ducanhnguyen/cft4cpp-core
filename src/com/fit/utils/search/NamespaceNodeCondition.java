package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.NamespaceNode;

public class NamespaceNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof NamespaceNode)
            return true;
        return false;
    }
}
