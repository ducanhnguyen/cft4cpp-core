package com.fit.gui.swing;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;

import javax.swing.*;
import java.awt.event.*;

public class Node<E> extends JLabel {

    private static final long serialVersionUID = 1L;

    private E element;
    private Node<E>[] refer;
    private int x, y;

    protected Node() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setFocusable(true);

        // Implement some mouse drag-drop listener
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Node.this.triggerMouseDrag(e);
                Node.this.getParent().repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Node.this.registerMousePress(e);
                Node.this.requestFocusInWindow();
                if (e.isPopupTrigger())
                    Node.this.openMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    Node.this.openMenu(e);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (code >= 37 && code <= 40) {
                    int x = Node.this.getX();
                    int y = Node.this.getY();
                    int delta = e.isControlDown() ? 10 : 1;

                    // e.consume();
                    switch (code) {
                        case 37: // left arrow
                            Node.this.setLocation(Math.max(x - delta, 0), y);
                            break;
                        case 38: // up arrow
                            Node.this.setLocation(x, Math.max(y - delta, 0));
                            break;
                        case 39: // right arrow
                            Node.this.setLocation(x + delta, y);
                            break;
                        case 40: // bottom arrow
                            Node.this.setLocation(x, y + delta);
                            break;
                    }
                    Node.this.getParent().repaint();
                }
            }
        });
    }

    protected Node(E element) {
        this();
        this.setElement(element);
        String content = String.valueOf(element);

        if (content.length() > Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CFG_NODE_WIDTH))) {
            StringBuilder htmlFormat = new StringBuilder();
            htmlFormat.append("<html><body>");
            htmlFormat.append("<pre>" + content.replaceAll("\t", "    ") + "</pre>");
            htmlFormat.append("</body></html>");
            setToolTipText(htmlFormat.toString());
            content = content.substring(0,
                    Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CFG_NODE_WIDTH)) - 3) + "...";
        }

        setText(content);
    }

    public E getElement() {
        return this.element;
    }

    protected void setElement(E element) {
        this.element = element;
    }

    public Node<E>[] getRefer() {
        return this.refer;
    }

    public void setRefer(Node<E>[] refer) {
        this.refer = refer;
    }

    public boolean isLocationSet() {
        return getX() != 0 || getY() != 0;
    }

    protected void openMenu(MouseEvent e) {
    }

    /**
     * Ghi nhớ thông tin sự kiện ấn chuột xuống
     */
    public void registerMousePress(MouseEvent e) {
        this.x = (int) e.getPoint().getX();
        this.y = (int) e.getPoint().getY();
    }

    @Override
    public String toString() {
        return this.element.toString();
    }

    /**
     * Di chuyển node khi có sự kiện kéo thả chuột
     */
    public void triggerMouseDrag(MouseEvent e) {
        int xx = e.getX();
        int yy = e.getY();
        int newX = Node.this.getX() + xx - this.x;
        int newY = Node.this.getY() + yy - this.y;

        newX = Math.max(newX, 0);
        newY = Math.max(newY, 0);
        this.setLocation(newX, newY);
    }
}
