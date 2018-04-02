package com.fit.utils.search;

import com.fit.tree.object.ConstructorNode;
import com.fit.tree.object.INode;

/**
 * Demo a condition
 *
 * @author DucAnh
 */
public class ConstructorNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ConstructorNode)
            return true;
        return false;
    }
}
