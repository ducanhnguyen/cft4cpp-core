package com.fit.utils.search;

import com.fit.tree.object.AbstractFunctionNode;
import com.fit.tree.object.INode;

public class AbstractFunctionNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof AbstractFunctionNode)
            return true;
        return false;
    }
}
