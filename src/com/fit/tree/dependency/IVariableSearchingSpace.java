package com.fit.tree.dependency;

import com.fit.utils.search.ISearch;

import java.util.List;

/**
 * Get searching space of a node in the structure tree.
 * <p>
 * If the node is function/attribute, the searching space consists of the
 * containing file, and all included file (represented in #include)
 *
 * @author DucAnh
 */
public interface IVariableSearchingSpace extends ISearch {

    /**
     * Get the variable searching space
     *
     * @return
     */
    List<Level> getSpaces();

}