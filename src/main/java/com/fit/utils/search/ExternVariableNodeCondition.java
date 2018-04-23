package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.IVariableNode;
import com.fit.tree.object.VariableNode;

/**
 * Represent extern variable, e.g., "extern int MY_MAX_VALUE"
 *
 * @author DucAnh
 */
public class ExternVariableNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof VariableNode && ((IVariableNode) n).isExtern())
            return true;
        return false;
    }
}
