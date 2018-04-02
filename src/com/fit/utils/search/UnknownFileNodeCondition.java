package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.UnknowObjectNode;

public class UnknownFileNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof UnknowObjectNode)
            return true;
        return false;
    }
}
