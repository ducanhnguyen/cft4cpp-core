package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.ObjectNode;

public class ObjectNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ObjectNode)
            return true;
        return false;
    }
}
