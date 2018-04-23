package com.fit.gui.swing;

import com.alee.laf.tabbedpane.WebTabbedPane;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;

/**
 * Đối tượng đồ họa điều khiển các thành phần con theo các tab đóng mở được
 *
 * @author ducvu
 */
public class LightTabbedPane extends WebTabbedPane {

    private static final long serialVersionUID = 1L;
    private ChangeListener opacqueIfEmpty;
    private int maxTab = 0;

    /**
     * Tạo một panel điều khiển với chế độ xuống dòng khi tràn tab
     *
     * @param tabPlacement vị trí đặt vùng điều khiển tab
     */
    public LightTabbedPane(int tabPlacement) {
        this(tabPlacement, JTabbedPane.WRAP_TAB_LAYOUT);
    }

    /**
     * Tạo một panel điều khiển theo tab
     *
     * @param tabPlacement    vị trí đặt vùng điều khiển tab
     * @param tabLayoutPolicy chế độ khi tràn tab
     */
    public LightTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);

        addChangeListener(e -> {
            int index = LightTabbedPane.this.getSelectedIndex();
            Panel tab = index >= 0 ? LightTabbedPane.this.getTabComponentAt(index) : null;

            if (tab != null && tab.changed) {
                tab.changed = false;
                String title = LightTabbedPane.this.getTitleAt(index);
                title = title.substring(0, title.length() - 1);
                LightTabbedPane.this.setTitleAt(index, title);
            }
        });
    }

    protected synchronized void checkMaxTab(int index) {
        if (maxTab <= 0)
            return;
        int max = maxTab - 1;

        for (int i = getTabCount() - 1; i >= 0; i--) {
            if (i == index)
                continue;
            Panel p = getTabComponentAt(i);

            if (p.closeable) {
                max--;
                if (max < 0)
                    p.closeThisTab();
            }
        }
    }

    public void closeAllTab() {
        for (int i = getTabCount() - 1; i >= 0; i--)
            getTabComponentAt(i).closeThisTab();
    }

    public int getMaxTab() {
        return maxTab;
    }

    public void setMaxTab(int maxTab) {
        this.maxTab = maxTab;
    }

    @Override
    public Panel getTabComponentAt(int index) {
        return (Panel) super.getTabComponentAt(index);
    }

    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        Panel panel = new Panel(this, title);
        setTabComponentAt(index, panel);

        checkMaxTab(index);
    }

    /**
     * Thêm một tab nếu nó chưa tồn tại, sau đó chuyển đến tab đó
     *
     * @param constructType dùng để tạo đối tượng nếu tab chưa tồn tại
     * @param construct     đối tượng dùng để so sánh sự tồn tại của tab
     * @return đối tượng đã được lựa chọn
     */
    public Component openTab(String title, Icon icon, String tip, Constructor<?> constructType, Object... construct) {
        EqualsConstruct ec = null, cur;
        int index = getTabCount();
        Component c = null;

        for (int i = 0; i < index; i++) {
            c = this.getComponentAt(i);
            if (c instanceof EqualsConstruct) {
                cur = (EqualsConstruct) c;
                if (cur.equalsConstruct(construct)) {
                    ec = cur;
                    index = i;
                    break;
                }
            }
        }
        if (ec == null)
            try {
                c = (Component) constructType.newInstance(construct);
                this.addTab(title, icon, c, tip);
                index = indexOfComponent(c);
                this.setTabCloseableAt(index, true);
            } catch (Exception e) {

            }
        setSelectedIndex(index);
        return getSelectedComponent();
    }

    public void setOpaqueIfEmpty(boolean flag) {
        if (flag) {
            setOpaque(getTabCount() == 0);
            if (opacqueIfEmpty == null)
                addChangeListener(opacqueIfEmpty = e -> {
                    setOpaque(getTabCount() == 0);
                });
        } else {
            setOpaque(false);
            if (opacqueIfEmpty != null)
                removeChangeListener(opacqueIfEmpty);
        }
    }

    /**
     * Xác nhận rằng nội dung của một tab đã bị thay đổi
     *
     * @param index vị trí tab bị thay đổi nội dung
     */
    public void setTabChangedAt(int index) {
        if (index != getSelectedIndex()) {
            Panel tab = getTabComponentAt(index);
            if (!tab.changed) {
                tab.changed = true;
                setTitleAt(index, getTitleAt(index) + "*");
            }
        }
    }

    /**
     * @see #setTabCloseableAt(Component, boolean)
     */
    public void setTabCloseableAt(Component c, boolean closeable) {
        this.setTabCloseableAt(indexOfComponent(c), closeable);
    }

    /**
     * Thiết đặt chế độ đóng tab cho từng tab
     *
     * @param index     vị trí tab
     * @param closeable <i>Mặc định</i>: <b>false</b> khi đối tượng được tạo<br/>
     *                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *                  &nbsp;&nbsp;&nbsp;đổi thành <b>true</b> nếu muốn tab trở thành
     *                  không đóng được
     */
    public void setTabCloseableAt(int index, boolean closeable) {
        Panel panel = getTabComponentAt(index);
        panel.setCloseable(closeable);
    }

    @Override
    public void setTitleAt(int index, String title) {
        super.setTitleAt(index, title);
        getTabComponentAt(index).setTitleText(title);
    }

    /**
     * Các đối tượng có thể so sánh sự giống nhau qua các thành phần được truyền
     * vào trong hàm khởi tạo
     */
    public static interface EqualsConstruct {

        /**
         * Trả về đúng nếu <b>constructItem</b> bằng các thành phần trong hàm
         * khởi tạo
         */
        public boolean equalsConstruct(Object... constructItem);
    }

    static class LightMouseAdapter implements MouseListener, MouseWheelListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            redispatchToParent(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            redispatchToParent(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            redispatchToParent(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            redispatchToParent(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            redispatchToParent(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            redispatchToParent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            redispatchToParent(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            redispatchToParent(e);
        }

        private void redispatchToParent(MouseEvent e) {
            Component source = (Component) e.getSource(), parent = source.getParent().getParent();

            MouseEvent parentEvent = SwingUtilities.convertMouseEvent(source, e, parent);
            parent.dispatchEvent(parentEvent);

        }
    }

    private static class Panel extends JPanel {

        private static final long serialVersionUID = 1L;
        private static final Color close = new Color(255, 0, 0, 220);
        private static final Color closeLite = new Color(255, 0, 0, 125);

        private LightTabbedPane tab;
        private JLabel lTitle, closeButton, gap;
        private boolean changed = false;
        private boolean closeable = false;

        public Panel(LightTabbedPane tabPane, String title) {
            setOpaque(false);
            setLayout(new BorderLayout());
            tab = tabPane;

            lTitle = new JLabel(title);
            Dimension size = lTitle.getPreferredSize();
            size.setSize(size.getWidth(), 20);
            lTitle.setPreferredSize(size);
            this.add(lTitle, BorderLayout.WEST);

            gap = new JLabel();
            gap.setPreferredSize(new Dimension(5, 20));
            this.add(gap, BorderLayout.CENTER);

            closeButton = new JLabel("x");
            this.add(closeButton, BorderLayout.EAST);
            closeButton.setForeground(Panel.closeLite);
            closeButton.setToolTipText("Close");
            closeButton.setBackground(new Color(0, 0, 0, 0));
            closeButton.setFont(new Font("Tahoma", Font.BOLD, 15));
            closeButton.setOpaque(true);

            closeButton.setHorizontalAlignment(SwingConstants.CENTER);
            closeButton.setPreferredSize(new Dimension(9, 20));

            closeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Panel.this.closeThisTab();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    closeButton.setForeground(Panel.close);
                    Panel.this.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeButton.setForeground(Panel.closeLite);
                    Panel.this.repaint();
                }
            });

            LightMouseAdapter listener = new LightMouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int button = e.getButton();

                    if (button == MouseEvent.BUTTON1)
                        super.mousePressed(e);
                    else if (button == MouseEvent.BUTTON2)
                        Panel.this.closeThisTab();
                }
            };
            addMouseListener(listener);
            addMouseMotionListener(listener);

            setCloseable(closeable);
        }

        private void closeThisTab() {
            if (closeable)
                tab.removeTabAt(tab.indexOfTabComponent(Panel.this));
        }

        private void setCloseable(boolean closeable) {
            this.closeable = closeable;
            closeButton.setVisible(false);
            gap.setVisible(false);
            if (closeable) {
                closeButton.setVisible(true);
                gap.setVisible(true);
            }
        }

        private void setTitleText(String title) {
            lTitle.setPreferredSize(null);
            lTitle.setText(title);
            Dimension size = lTitle.getPreferredSize();
            size.setSize(size.getWidth(), 20);
            lTitle.setPreferredSize(size);
        }
    }
}
