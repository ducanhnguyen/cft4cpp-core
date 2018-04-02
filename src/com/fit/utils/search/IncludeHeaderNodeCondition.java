package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.IncludeHeaderNode;

public class IncludeHeaderNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof IncludeHeaderNode)
            return true;
        return false;
    }
}
