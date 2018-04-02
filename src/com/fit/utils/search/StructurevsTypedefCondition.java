package com.fit.utils.search;

import com.fit.tree.object.*;

public class StructurevsTypedefCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof StructureNode || n instanceof TypedefDeclaration)
            return true;
        else if (n instanceof VariableNode && !(n.getParent() instanceof FunctionNode))
            return true;
        return false;
    }
}
