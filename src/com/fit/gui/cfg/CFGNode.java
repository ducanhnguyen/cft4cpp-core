package com.fit.gui.cfg;

import com.fit.cfg.object.ICfgNode;
import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.gui.swing.Node;
import com.fit.utils.UtilsVu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CFGNode extends Node<ICfgNode> {

    public static final int NORMAL = 0, CONDITION = 1, MARK = 2, PADDING_X = 20, PADDING_Y = 10, MARK_SIZE = 25;
    static final String MORE = "...", NEW_LINE = "<br>";
    private static final long serialVersionUID = 1L;
    private int type;
    private Color borderColor;
    private ArrayList<CFGNode> listDrag;

    public CFGNode(ICfgNode stm) {
        super(stm);
        Dimension size = getPreferredSize();

        if (stm.isCondition()) {
            type = CFGNode.CONDITION;
            size.width += 2 * CFGNode.PADDING_X;
            size.height += 2 * CFGNode.PADDING_Y;
        } else if (stm.isNormalNode()) {
            type = CFGNode.NORMAL;
            size.width += CFGNode.PADDING_X;
            size.height += CFGNode.PADDING_Y;
        } else {
            type = CFGNode.MARK;
            setText(null);
            size.width = size.height = CFGNode.MARK_SIZE;
        }

        this.setSize(size);
        setBorderColor(Color.BLACK);
        setBackground(SystemColor.inactiveCaptionBorder);
    }

    public CFGNode(List<ICfgNode> stmList) {
        setElement(stmList.get(0));
        type = CFGNode.NORMAL;

        StringBuilder txt = new StringBuilder(), real = new StringBuilder();
        boolean overWidth = false, overLine = false, newLine = true;
        int count = 1, width = 0, MAX_LINE = Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CFG_NODE_LINE)),
                MAX_WIDTH = Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CFG_NODE_WIDTH));

        if (MAX_LINE == 0)
            MAX_LINE = Integer.MAX_VALUE;

        for (ICfgNode stm : stmList) {
            String s = String.valueOf(stm);
            real.append(UtilsVu.htmlEscape(s)).append(CFGNode.NEW_LINE);

            if (count <= MAX_LINE) {
                int len = s.length();

                // Đã có node trước đó trên dòng, xem liệu có thêm node mới được
                // không
                if (width > 0)
                    if (stm.shouldDisplayInSameLine() && width + len < MAX_WIDTH) {
                        width += len + 1;
                        txt.append(' ').append(UtilsVu.htmlEscape(s));
                        newLine = false;
                    } else {
                        txt.append(CFGNode.NEW_LINE);
                        newLine = true;
                        count++;
                        width = 0;
                    }

                if (count > MAX_LINE) {
                    overLine = true;
                    continue;
                }

                // Không thêm được, tạo dòng mới cho node
                if (width == 0)
                    if (len <= MAX_WIDTH) {
                        width = len;
                        txt.append(UtilsVu.htmlEscape(s));
                        newLine = false;

                        if (!stm.shouldDisplayInSameLine()) {
                            txt.append(CFGNode.NEW_LINE);
                            newLine = true;
                            count++;
                            width = 0;
                        }
                    } else {
                        txt.append(UtilsVu.htmlEscape(s.substring(0, MAX_WIDTH - CFGNode.MORE.length())))
                                .append(CFGNode.MORE).append(CFGNode.NEW_LINE);
                        newLine = true;
                        count++;
                        width = 0;
                        overWidth = true;
                    }

            } else
                overLine = true;
        }

        if (overLine) {
            if (!newLine)
                txt.append(CFGNode.NEW_LINE);
            txt.append(CFGNode.MORE);
        }

        setText(UtilsVu.htmlCenter(txt.toString()));
        if (overWidth || overLine)
            setToolTipText(UtilsVu.html(real.toString()));

        Dimension size = getPreferredSize();
        size.width += CFGNode.PADDING_X;
        size.height += CFGNode.PADDING_Y;
        this.setSize(size);
        setBorderColor(Color.BLACK);
        setBackground(SystemColor.inactiveCaptionBorder);
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color color) {
        borderColor = color;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean inside(int x, int y) {
        return this.inside(x, y, getWidth(), getHeight());
    }

    public boolean inside(int x, int y, int width, int height) {
        switch (type) {
            case NORMAL:
                return x >= 0 && x < width && y >= 0 && y < height;
            case CONDITION:
                long w21 = width / 2, h21 = height / 2, X = (x - w21) * h21, Y = (y - h21) * w21, Z = w21 * h21;
                return X + Y < Z && X - Y < Z && -X + Y < Z && -X - Y < Z;
            case MARK:
                long w2 = width / 2, h2 = height / 2, W2 = w2 * w2, H2 = h2 * h2;
                return H2 * (x - w2) * (x - w2) + W2 * (y - h2) * (y - h2) <= W2 * H2;
            default:
                return false;
        }
    }

    public boolean isCondition() {
        return getElement().isCondition();
    }

    /**
     * Kiểm tra nút có là nút điều kiện đơn giản <br/>
     * (1 câu lệnh duy nhất tại nhánh, hoặc câu lệnh return) hay không
     */
    public boolean isTrueOnlyCondition() {
        if (!isCondition())
            return false;
        Node<ICfgNode>[] refer = getRefer();
        if (refer.length == 2) {
            CFGNode trueNode = (CFGNode) refer[0], falseNode = (CFGNode) refer[1];

            if (trueNode.isCondition() || trueNode.getElement().isMultipleTarget())
                return false;

            return String.valueOf(trueNode.getElement()).indexOf("return ") == 0 || trueNode.getRefer()[0] == falseNode;
        } else
            return false;
    }

    protected void paint(Graphics2D g, int x, int y, int width, int height) {
        switch (type) {
            case NORMAL:
                g.setColor(getBackground());
                g.fillRect(x, y, width, height);
                g.setColor(getBorderColor());
                g.drawRect(x, y, width, height);
                break;
            case CONDITION:
                int w2 = width / 2, h2 = height / 2;
                int[] X = {0, w2, width, w2}, Y = {h2, 0, h2, height};
                g.setColor(getBackground());
                g.fillPolygon(X, Y, 4);
                g.setColor(getBorderColor());
                g.drawPolygon(X, Y, 4);
                break;
            case MARK:
                g.setColor(getBackground());
                g.fillOval(x, y, width, height);
                g.setColor(getBorderColor());
                g.drawOval(x, y, width, height);

                // Câu lệnh cuối được tô đen
                if (getElement().getTrueNode() == null) {
                    g.setColor(getBorderColor());
                    g.fillOval(x + 3, y + 3, width - 6, height - 6);
                }
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;

            Color oldColor = g2d.getColor();
            this.paint(g2d, 0, 0, getWidth() - 1, getHeight() - 1);
            g2d.setColor(oldColor);
        }
        super.paintComponent(g);
    }

    @Override
    public void triggerMouseDrag(MouseEvent e) {
        super.triggerMouseDrag(e);

        if (!e.isControlDown())
            return;

        // Xử lý nhóm các node khi ấn vào nút điều khiển
        if (isCondition() || getElement().isMultipleTarget()) {
            if (listDrag == null) {
                listDrag = new ArrayList<>();
                CFGCanvas cv = (CFGCanvas) getParent();

                for (Node<ICfgNode> n : cv.getAdapter())
                    if (getElement().contains(n.getElement()))
                        listDrag.add((CFGNode) n);
            }
            for (CFGNode n : listDrag)
                if (n != this)
                    n.triggerSuperMouseDrag(e);
        } // Xử lý nhóm các node thông thường
        else {
            CFGNode next = (CFGNode) getRefer()[0];

            // Nếu node tiếp theo cũng là node thông thường, di chuyển theo
            if (!(next == null || next.isCondition() || next.getElement().isMultipleTarget()))
                next.triggerMouseDrag(e);
        }
    }

    private void triggerSuperMouseDrag(MouseEvent e) {
        super.triggerMouseDrag(e);
    }
}
