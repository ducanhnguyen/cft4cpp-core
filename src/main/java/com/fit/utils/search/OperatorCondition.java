package com.fit.utils.search;

import com.fit.testdatagen.se.expression.OperatorNode;
import com.fit.tree.object.INode;

public class OperatorCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof OperatorNode)
            return true;
        return false;
    }
}
