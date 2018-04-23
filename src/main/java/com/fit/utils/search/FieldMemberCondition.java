package com.fit.utils.search;

import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.VariableNode;

/**
 * Đại diện thuộc tính trong class, struct
 *
 * @author phibao37
 */
public class FieldMemberCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof VariableNode && !(n.getParent() instanceof FunctionNode))
            return true;
        return false;
    }
}
