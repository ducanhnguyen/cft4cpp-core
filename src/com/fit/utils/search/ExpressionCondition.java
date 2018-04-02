package com.fit.utils.search;

import com.fit.testdatagen.se.expression.ExpressionNode;
import com.fit.tree.object.INode;

public class ExpressionCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ExpressionNode)
            return true;
        return false;
    }
}
