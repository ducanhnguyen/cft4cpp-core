package com.fit.utils.search;

import com.fit.tree.object.EnumTypedefNode;
import com.fit.tree.object.INode;

/**
 * Created by DucToan on 14/07/2017.
 */
public class EnumTypedefNodeCondifion extends SearchCondition {
    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof EnumTypedefNode)
            return true;
        return false;
    }
}
