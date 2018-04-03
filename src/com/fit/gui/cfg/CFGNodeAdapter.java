package com.fit.gui.cfg;

import com.fit.cfg.ICFG;
import com.fit.cfg.object.ConditionCfgNode;
import com.fit.cfg.object.ICfgNode;
import com.fit.gui.swing.Node;
import com.fit.gui.swing.NodeAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CFGNodeAdapter extends NodeAdapter<ICfgNode> {

	private static final long serialVersionUID = 1L;

	private int[] receiveCount;

	@SuppressWarnings("unchecked")
	public CFGNodeAdapter(ICFG cfg) {
		List<ICfgNode> cfgStm = cfg.getAllNodes();
		receiveCount = new int[cfgStm.size()];
		int i = 0;

		// GÃ¡n thá»© tá»± vÃ  reset tráº¡ng thÃ¡i
		for (ICfgNode stm : cfgStm) {
			stm.setVisit(false);
			stm.setId(i++);
		}

		// Ä�áº¿m sá»‘ láº§n má»™t cÃ¢u lá»‡nh nháº­n Ä‘Ã­ch Ä‘áº¿n tá»« cÃ¢u lá»‡nh
		// khÃ¡c
		for (ICfgNode stm : cfgStm)
			if (stm.isMultipleTarget())
				for (ICfgNode target : stm.getListTarget())
					receiveCount[target.getId()]++;
			else {
				if (stm.getTrueNode() != null)
					receiveCount[stm.getTrueNode().getId()]++;
				if (stm instanceof ConditionCfgNode && stm.getFalseNode() != null)
					receiveCount[stm.getFalseNode().getId()]++;
			}

		// Táº¡o cÃ¢u lá»‡nh Ä‘á»“ há»�a Ä‘Æ¡n hoáº·c nhÃ³m
		for (ICfgNode stm : cfgStm) {
			if (stm.isVisited() || !stm.shouldDisplayInCFG())
				continue;
			stm.setVisit(true);

			ICfgNode next = lastInBlock(stm);
			if (next == stm)
				this.add(new CFGNode(stm));
			else {
				ArrayList<ICfgNode> stmList = new ArrayList<>();
				while (stm != next) {
					if (stm.shouldDisplayInCFG())
						stmList.add(stm);
					stm = stm.getTrueNode();
					stm.setVisit(true);
				}
				if (next.shouldDisplayInCFG())
					stmList.add(next);
				this.add(new CFGNode(stmList));
			}
		}

		// LiÃªn káº¿t cÃ¡c cÃ¢u lá»‡nh vá»›i nhau
		for (Node<ICfgNode> node : this) {
			ICfgNode current = node.getElement();

			if (current.isMultipleTarget()) {
				List<ICfgNode> targets = current.getListTarget();
				Node<ICfgNode>[] refer = (Node<ICfgNode>[]) Array.newInstance(node.getClass(), targets.size());
				for (i = 0; i < targets.size(); i++)
					refer[i] = this.getNodeByElement(targets.get(i));
				node.setRefer(refer);
				continue;
			}

			ICfgNode stm = lastInBlock(current);
			Node<ICfgNode> nodeTrue = this.getNodeByElement(next(stm.getTrueNode())),
					nodeFalse = this.getNodeByElement(next(stm.getFalseNode()));

			boolean isCondition = nodeTrue != nodeFalse;
			Node<ICfgNode>[] refer = (Node<ICfgNode>[]) Array.newInstance(node.getClass(), isCondition ? 2 : 1);
			refer[0] = nodeTrue;
			if (isCondition)
				refer[1] = nodeFalse;
			node.setRefer(refer);
		}
	}

	/**
	 * Tráº£ vá»� cÃ¢u lá»‡nh cuá»‘i cÃ¹ng trong khá»‘i cÃ¢u lá»‡nh náº¿u cÃ³ thá»ƒ
	 * nhÃ³m láº¡i
	 */
	private ICfgNode lastInBlock(ICfgNode stm) {
		if (!stm.shouldInBlock())
			return stm;
		ICfgNode next = stm.getTrueNode();

		while (next != null && receiveCount[next.getId()] == 1 // chá»‰ nháº­n
		// tá»« 1
		// cÃ¢u lá»‡nh
				&& next.shouldInBlock()) { // cÃ³ thá»ƒ Ä‘Æ°á»£c nhÃ³m láº¡i
			stm = next;
			next = stm.getTrueNode();
		}
		return stm;
	}

	/**
	 * Tráº£ vá»� cÃ¢u lá»‡nh tiáº¿p theo cáº§n Ä‘Æ°á»£c hiá»ƒn thá»‹, bá»� qua cÃ¡c
	 * dáº¥u ngoáº·c {, }
	 */
	private ICfgNode next(ICfgNode stm) {
		while (stm != null && !stm.shouldDisplayInCFG())
			stm = stm.getTrueNode();
		return stm;
	}
}
