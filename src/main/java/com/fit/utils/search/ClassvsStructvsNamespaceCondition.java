package com.fit.utils.search;

import com.fit.tree.object.ClassNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.NamespaceNode;
import com.fit.tree.object.StructNode;

public class ClassvsStructvsNamespaceCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ClassNode || n instanceof StructNode || n instanceof NamespaceNode)
            return true;
        return false;
    }
}
