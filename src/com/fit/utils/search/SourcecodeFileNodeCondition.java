package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.SourcecodeFileNode;

public class SourcecodeFileNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof SourcecodeFileNode)
            return true;
        return false;
    }
}
