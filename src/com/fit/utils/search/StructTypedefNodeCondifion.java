package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.StructTypedefNode;

public class StructTypedefNodeCondifion extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof StructTypedefNode)
            return true;
        return false;
    }
}
