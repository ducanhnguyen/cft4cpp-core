package com.fit.utils.search;

import com.fit.tree.object.ClassNode;
import com.fit.tree.object.INode;

public class ClassNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ClassNode)
            return true;
        return false;
    }
}
