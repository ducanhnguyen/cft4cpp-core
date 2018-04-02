package com.fit.utils.search;

import com.fit.tree.object.HeaderNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.SourcecodeFileNode;

public class HeaderNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof HeaderNode || n instanceof SourcecodeFileNode)
            return true;
        return false;
    }
}
