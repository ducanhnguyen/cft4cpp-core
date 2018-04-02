package com.fit.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vùng cuộn cho phép kéo thả để cuộn
 */
public class DragScrollPane extends JScrollPane {

    private static final long serialVersionUID = 1L;
    private static Cursor MOVE = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

    private int x, y;

    private MouseAdapter mListener = new MouseAdapter() {

        @Override
        public void mouseDragged(MouseEvent e) {
            DragScrollPane.this.setCursor(DragScrollPane.MOVE);
            int newX = DragScrollPane.this.horizontalScrollBar.getValue() - e.getX() + x;
            int newY = DragScrollPane.this.verticalScrollBar.getValue() - e.getY() + y;

            DragScrollPane.this.horizontalScrollBar.setValue(newX);
            DragScrollPane.this.verticalScrollBar.setValue(newY);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            DragScrollPane.this.setCursor(null);
        }

    };

    public DragScrollPane() {
        Dimension dimen = new Dimension(0, 0);
        int unit = 10;

        verticalScrollBar.setPreferredSize(dimen);
        horizontalScrollBar.setPreferredSize(dimen);
        verticalScrollBar.setUnitIncrement(unit);
        horizontalScrollBar.setUnitIncrement(unit);

        viewport.addChangeListener(e -> DragScrollPane.this.viewport.repaint());
    }

    @Override
    public void setViewportView(Component view) {
        super.setViewportView(view);

        view.removeMouseListener(mListener);
        view.removeMouseMotionListener(mListener);
        view.addMouseListener(mListener);
        view.addMouseMotionListener(mListener);
    }

}
