package com.fit.utils.search;

import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;

/**
 * Demo a condition
 *
 * @author DucAnh
 */
public class FunctionNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof IFunctionNode)
            return true;
        return false;
    }
}
