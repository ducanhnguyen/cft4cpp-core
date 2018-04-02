package com.fit.utils.search;

import com.fit.tree.object.ClassNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.StructNode;

public class ClassvsStructCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ClassNode || n instanceof StructNode)
            return true;
        return false;
    }
}
