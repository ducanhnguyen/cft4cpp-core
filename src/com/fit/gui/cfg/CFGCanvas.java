package com.fit.gui.cfg;

import com.fit.cfg.object.ICfgNode;
import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.gui.swing.Canvas;
import com.fit.gui.swing.Node;
import com.fit.gui.swing.NodeAdapter;
import com.fit.tree.object.IFunctionNode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CFGCanvas extends Canvas<ICfgNode> {

    public static final Color DEFAULT = Color.BLACK;
    public static final Color TRUE = new Color(30, 144, 255), FALSE = new Color(50, 205, 50);
    private static final long serialVersionUID = 1L;
    /**
     * Math.asin(10.0/38)
     */
    static double DELTA = 0.26629401711818285;
    private IFunctionNode function;
    private MouseListener notifyAllNodePress = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            for (Node<ICfgNode> node : CFGCanvas.this.adapter)
                ((CFGNode) node).registerMousePress(e);
        }

    };

    public CFGCanvas(IFunctionNode fn) {
        function = fn;
    }

    static int[] getPoint(int x1, int y1, int x2, int y2, boolean left) {
        double anpha = Math.atan2(y2 - y1, x2 - x1), anph6 = anpha * 6 / Math.PI;

        if (left && 5 > anph6 || !left && 0 < anph6 && anph6 <= 1)
            anpha += CFGCanvas.DELTA;
        else
            anpha -= CFGCanvas.DELTA;

        return new int[]{x1 + (int) (38 * Math.cos(anpha)), y1 + (int) (38 * Math.sin(anpha))};
    }

    public IFunctionNode getFunction() {
        return function;
    }

    @Override
    protected void onAddedNode(Node<ICfgNode> node) {
        node.addMouseListener(notifyAllNodePress);
    }

    @Override
    protected void paintCanvas(Graphics g, NodeAdapter<ICfgNode> adapter) {
        int x1, y1, x2, y2, xs, ys;
        Node<ICfgNode>[] refer;
        int d = 12, h = 5, gap = 25;
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(Canvas.NORMAL_STROKE);

        // Duyệt qua các node trên đồ thị
        for (Node<ICfgNode> n1 : adapter) {
            refer = n1.getRefer();
            xs = n1.getX() + n1.getWidth() / 2;
            ys = n1.getY() + n1.getHeight();
            boolean isMultiple = n1.getElement().isMultipleTarget();
            boolean isCondition = ((CFGNode) n1).isCondition();
            int length = isMultiple || isCondition ? refer.length : 1;

            // Duyệt qua các đích đến của các node đang xét
            for (int i = 0; i < length; i++) {
                Node<ICfgNode> n2 = refer[i];
                boolean isTrue = i == 0;
                Color color;
                int[] marks = null;

                // Node cuối cùng không có dích đến
                if (n2 == null)
                    continue;

                if (isCondition)
                    color = isTrue ? CFGCanvas.TRUE : CFGCanvas.FALSE;
                else
                    color = CFGCanvas.DEFAULT;
                g2.setColor(color);

                x1 = xs;
                y1 = ys;
                x2 = n2.getX() + n2.getWidth() / 2;
                y2 = n2.getY();

                // Node 2 ở phía bến phải node 1
                boolean rightSide = x2 > x1;
                int cy1 = n1.getY() + n1.getHeight() / 2, cy2 = n2.getY() + n2.getHeight() / 2;

                // Node 2 ở bên dưới hoặc bằng node 1
                if (cy2 >= cy1) {
                    double angle = Math.atan((y2 - y1) * 1.0 / Math.abs(x2 - x1));

                    // Nếu góc nghiêng ngang nhỏ hơn PI/8, đầu mũi tên chỉ sang
                    // bên
                    if (angle < Math.PI / 8) {
                        x1 = n1.getX() + (rightSide ? n1.getWidth() : 0);
                        y1 = n1.getY() + n1.getHeight() / 2;

                        x2 = n2.getX() + (rightSide ? 0 : n2.getWidth());
                        y2 = y2 + n2.getHeight() / 2;
                    }
                } // Node 2 ở trên, vẽ các đường vuông góc
                else {
                    int nearSide = n2.getX() + (rightSide ? 0 : n2.getWidth()), tmpX;
                    int distance = Math.abs(nearSide - (x1 + n1.getWidth() / 2 * (rightSide ? 1 : -1)));
                    boolean outOfPadding = (n1.getX() + n1.getWidth() < n2.getX()
                            || n2.getX() + n2.getWidth() < n1.getX()) && distance > gap * 2;

                    // Vẽ xuống
                    g2.drawLine(x1, y1, x1, y1 + gap);

                    if (isCondition)
                        marks = CFGCanvas.getPoint(x1, y1, x1, y1 + gap, isTrue);
                    if (outOfPadding)
                        tmpX = x2 + (n2.getWidth() / 2 + gap) * (rightSide ? -1 : 1);
                    else
                        tmpX = n2.getX() + (rightSide ? n2.getWidth() + gap : -gap);

                    g2.drawLine(x1, y1 + gap, tmpX, y1 + gap);
                    x1 = tmpX;
                    y2 = y2 + n2.getHeight() / 2;
                    g2.drawLine(x1, y1 + gap, x1, y2);
                    y1 = y2;
                    x2 = n2.getX() + (rightSide ^ outOfPadding ? n2.getWidth() : 0);
                }

                drawArrowLine(g2, x1, y1, x2, y2, d, h);
                if (isCondition) {
                    if (marks == null)
                        marks = CFGCanvas.getPoint(x1, y1, x2, y2, isTrue);
                    g.drawString(isTrue ? "T" : "F", marks[0] - 3, marks[1] + 5);
                }

            }
        }
        g2.setStroke(oldStroke);
    }

    /**
     * Sắp xếp các node để hiển thị
     *
     * @param adapter danh sách các node, theo thứ tự duyệt chiều sâu nhánh
     *                true->false
     */
    @Override
    protected void parseAdapter(NodeAdapter<ICfgNode> adapter) {
        // Đặt nút đầu tiên ở chính giữa trên cùng
        adapter.get(0).setLocation(getWidth() / 2, Canvas.PADDING_Y);

        // Khoảng cách ngang/dọc giữa trọng tâm các node
        final int MARGIN_X = Integer.parseInt(AbstractSetting.getValue(ISettingv2.CFG_MARGIN_X)),
                MARGIN_Y = Integer.parseInt(AbstractSetting.getValue(ISettingv2.CFG_MARGIN_Y));

        for (Node<ICfgNode> n : adapter) {
            CFGNode node = (CFGNode) n;
            Node<ICfgNode>[] refer = n.getRefer();
            // Trọng tâm dưới của node đang xét
            int x = node.getX() + node.getWidth() / 2;
            int y = node.getY() + node.getHeight();

            // Nếu là nút đa điểm đến, xử lý riêng
            if (n.getElement().isMultipleTarget()) {
                int begin = x - (refer.length - 1) * MARGIN_X / 2;
                int cy = y + MARGIN_Y;

                for (int i = 0; i < refer.length; i++) {
                    CFGNode rNode = (CFGNode) refer[i];
                    if (rNode == null)
                        continue;

                    int cx = begin + i * MARGIN_X - rNode.getWidth() / 2;
                    rNode.setLocation(cx, cy);
                }

                continue;
            }

            boolean isCondition = node.isCondition();
            // Nút có điều kiện TRUE trở đến điều kiện FALSE (thí dụ if không có
            // else)
            // Nhánh true sẽ được vẽ nghiêng, false thằng hàng với cha thay vì
            // đối xứng
            boolean is1StmCondition = node.isTrueOnlyCondition();
            int length = isCondition ? refer.length : 1;

            // Duyệt các nhánh, chỉ 1 lần nếu không rẽ nhánh
            for (int i = 0; i < length; i++) {
                CFGNode rNode = (CFGNode) refer[i];

                // node kết thúc sẽ có các nhánh là null
                if (rNode == null)
                    continue;

                // Nếu node đã được gán vị trí 1 lần, bỏ qua
                if (!rNode.isLocationSet()) {
                    // Vị trí thẳng hàng dọc và ở bên dưới nút cha
                    int cx = x - rNode.getWidth() / 2;
                    int cy = y + MARGIN_Y;

                    if (isCondition)
                        if (is1StmCondition) {
                            if (i == 0)
                                cx -= MARGIN_X;
                            else
                                cy += MARGIN_Y;
                        } // Nếu không, 2 nhánh nằm đối xứng nhau theo chiều dọc
                        else
                            cx += i == 0 ? -MARGIN_X : MARGIN_X;
                    // Gán vị trí cho nhánh
                    rNode.setLocation(cx, cy);
                }
            }
        }
    }

}
