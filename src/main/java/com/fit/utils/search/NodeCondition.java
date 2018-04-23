package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.Node;

public class NodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof Node)
            return true;
        return false;
    }
}
