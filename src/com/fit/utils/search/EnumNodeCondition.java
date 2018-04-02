package com.fit.utils.search;

import com.fit.tree.object.EnumNode;
import com.fit.tree.object.INode;

/**
 * Demo a condition
 *
 * @author DucAnh
 */
public class EnumNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof EnumNode)
            return true;
        return false;
    }
}
