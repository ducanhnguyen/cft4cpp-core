package com.fit.cfg.object;

/**
 * Represent the begin node of CFG
 *
 * @author ducanh
 */
public class BeginFlagCfgNode extends FlagCfgNode {
    /**
     * Content of the starting node
     */
    public static final String BEGIN_FLAG = "Begin";
    final static String BEGIN_NODE_CONTENT = "Begin";

    public BeginFlagCfgNode() {
        setContent(BeginFlagCfgNode.BEGIN_FLAG);
    }

}
