package com.fit.utils.search;

import com.fit.tree.object.INode;
import com.fit.tree.object.SpecialStructTypedefNode;
import com.fit.tree.object.StructNode;
import com.fit.tree.object.StructTypedefNode;

/**
 * Demo a condition
 *
 * @author DucAnh
 */
public class StructNodeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof StructNode || n instanceof SpecialStructTypedefNode || n instanceof StructTypedefNode)
            return true;
        return false;
    }
}
