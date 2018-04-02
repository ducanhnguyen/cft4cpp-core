package com.fit.utils.search;

import com.fit.tree.object.CppFileNode;
import com.fit.tree.object.INode;

public class CppFileNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof CppFileNode)
            return true;
        return false;
    }
}
