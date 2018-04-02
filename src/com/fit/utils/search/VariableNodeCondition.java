package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.VariableNode;

public class VariableNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof VariableNode)
            return true;
        return false;
    }
}
