package com.fit.cfg.overviewgraph;

import com.fit.tree.object.IFunctionNode;

import interfaces.IGeneration;

/**
 * Compute the maximum level of the overview CFG
 *
 * @author DucAnh
 */
public interface IOverviewCFGMaxLevelComputation extends IGeneration {
    /**
     * Maximum level of the overview CFG
     */
    int MAXIMUM_LEVEL = 1;

    /**
     * Compute the maximum level of the overview CFG
     */
    void computeMaxLevel();

    /**
     * Get the maximum level of the overview CFG
     *
     * @return
     */
    int getMaxLevel();

    /**
     * Set the maximum level of the overview CFG
     *
     * @param maxLevel
     */
    void setMaxLevel(int maxLevel);

    /**
     * Get the function node corresponding to the overview CFG
     *
     * @return
     */
    IFunctionNode getFunctionNode();

    /**
     * Set the function node corresponding to the overview CFG
     *
     * @param functionNode
     */
    void setFunctionNode(IFunctionNode functionNode);

}