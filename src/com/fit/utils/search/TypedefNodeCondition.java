package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.TypedefDeclaration;

public class TypedefNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof TypedefDeclaration)
            return true;
        return false;
    }
}
