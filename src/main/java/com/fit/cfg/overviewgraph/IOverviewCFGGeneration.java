package com.fit.cfg.overviewgraph;

import com.fit.cfg.ICFG;
import interfaces.IGeneration;

public interface IOverviewCFGGeneration extends IGeneration {

    int CFG_LEVEL_ONE = 1;

    int CFG_LEVEL_TWO = 2;

    int FOR_IS_ONE_NODE = 0;

    int FOR_IS_MANY_NODE = 1;

    /**
     * Get the overview CFG
     *
     * @return
     */
    ICFG getOverviewCFG();
}