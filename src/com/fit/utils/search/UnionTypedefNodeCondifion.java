package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.UnionTypedefNode;

/**
 * Created by DucToan on 14/07/2017.
 */
public class UnionTypedefNodeCondifion extends SearchCondition {
    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof UnionTypedefNode)
            return true;
        return false;
    }
}
