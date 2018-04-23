package com.fit.utils.search;

import com.fit.tree.object.INode;

public abstract class SearchCondition implements ISearchCondition {

    /*
     * (non-Javadoc)
     *
     * @see
     * com.fit.utils.search.ISearchCondition#isSatisfiable(com.fit.tree.object.
     * INode)
     */
    @Override
    public abstract boolean isSatisfiable(INode n);
}
