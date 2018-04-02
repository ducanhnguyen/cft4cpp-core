package com.fit.gui.cfg;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.overviewgraph.OverviewCFGGeneration;
import com.fit.gui.swing.DragScrollPane;
import com.fit.gui.swing.LightTabbedPane;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.tree.object.IFunctionNode;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;

public class CFGVisualizer extends DragScrollPane implements
        LightTabbedPane.EqualsConstruct, ComponentListener {

    public static final int LOGIC_CFG = 1, OVERVIEW_CFG = 2;
    private static final long serialVersionUID = 1L;
    private int cfgType = CFGVisualizer.LOGIC_CFG;
    private int cfgLevel;
    private IFunctionNode fn;
    private CFGCanvas canvas;
    private MouseListener nodeListener;

    public CFGVisualizer(IFunctionNode fn, MouseListener nodeListener,
                         int cfgType, int cfgLevel) {
        this.fn = fn;
        this.nodeListener = nodeListener;
        this.cfgType = cfgType;
        this.cfgLevel = cfgLevel;

        canvas = new CFGCanvas(fn);
        setViewportView(canvas);

        setBorder(null);
        addComponentListener(this);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
        if (!canvas.hasAdapter()) {
            ICFG cfg = null;
            try {
                if (cfgType == CFGVisualizer.OVERVIEW_CFG)
                    cfg = new OverviewCFGGeneration(fn, cfgLevel)
                            .getOverviewCFG();
                else if (cfgType == CFGVisualizer.LOGIC_CFG) {
                    FunctionNormalizer norm = fn
                            .normalizeFunctionToDisplayCFG();

                    IFunctionNode clone = (IFunctionNode) fn.clone();
                    clone.setAST(norm.getNormalizedAST());
                    cfg = new CFGGenerationforBranchvsStatementCoverage(clone)
                            .generateCFG();
                }

                if (cfg != null) {
                    CFGNodeAdapter adapter = new CFGNodeAdapter(cfg);
                    canvas.setAdapter(adapter);

                    adapter.forEach(n -> n.addMouseListener(nodeListener));
                } else
                    throw new Exception();

            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(CFGVisualizer.this,
                        "Dont support to display graph for this function",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    @Override
    public boolean equalsConstruct(Object... c) {
        return fn == c[0] && cfgType == (int) c[2] && cfgLevel == (int) c[3];
    }

}
