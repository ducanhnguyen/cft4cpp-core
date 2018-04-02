package com.fit.gui.cfg;

import com.fit.cfg.ICFG;
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

        // Gán thứ tự và reset trạng thái
        for (ICfgNode stm : cfgStm) {
            stm.setVisit(false);
            stm.setId(i++);
        }

        // Đếm số lần một câu lệnh nhận đích đến từ câu lệnh khác
        for (ICfgNode stm : cfgStm)
            if (stm.isMultipleTarget())
                for (ICfgNode target : stm.getListTarget())
                    receiveCount[target.getId()]++;
            else {
                if (stm.getTrueNode() != null)
                    receiveCount[stm.getTrueNode().getId()]++;
                if (stm.isCondition() && stm.getFalseNode() != null)
                    receiveCount[stm.getFalseNode().getId()]++;
            }

        // Tạo câu lệnh đồ họa đơn hoặc nhóm
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

        // Liên kết các câu lệnh với nhau
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
     * Trả về câu lệnh cuối cùng trong khối câu lệnh nếu có thể nhóm lại
     */
    private ICfgNode lastInBlock(ICfgNode stm) {
        if (!stm.shouldInBlock())
            return stm;
        ICfgNode next = stm.getTrueNode();

        while (next != null && receiveCount[next.getId()] == 1 // chỉ nhận
                // từ 1
                // câu lệnh
                && next.shouldInBlock()) { // có thể được nhóm lại
            stm = next;
            next = stm.getTrueNode();
        }
        return stm;
    }

    /**
     * Trả về câu lệnh tiếp theo cần được hiển thị, bỏ qua các dấu ngoặc {, }
     */
    private ICfgNode next(ICfgNode stm) {
        while (stm != null && !stm.shouldDisplayInCFG())
            stm = stm.getTrueNode();
        return stm;
    }
}
