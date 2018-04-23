package com.fit.utils.search;

import com.fit.tree.object.DefinitionFunctionNode;
import com.fit.tree.object.INode;

/**
 * Demo a condition
 *
 * @author DucAnh
 */
public class DefinitionFunctionNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof DefinitionFunctionNode)
            return true;
        return false;
    }
}
