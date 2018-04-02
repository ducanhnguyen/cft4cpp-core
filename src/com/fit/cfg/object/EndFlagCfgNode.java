package com.fit.cfg.object;

/**
 * Represent the end node of CFG
 *
 * @author ducanh
 */
public class EndFlagCfgNode extends FlagCfgNode {
    /**
     * Content of the ending node
     */
    public static final String END_FLAG = "End";
    final static String END_NODE_CONTENT = "End";

    public EndFlagCfgNode() {
        setContent(EndFlagCfgNode.END_FLAG);
    }

}
