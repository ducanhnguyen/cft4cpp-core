package com.fit.gui.main;

import javax.swing.*;
import java.awt.*;

public class GuiUtils {

    /**
     * @param imgPath Ex: /image/view-functions2.png
     * @param w
     * @param h
     * @return
     */
    public static ImageIcon getScaledImage(String imgPath, int w, int h) {
        return new ImageIcon(new ImageIcon(GUIView.class.getResource(imgPath)).getImage().getScaledInstance(w, h,
                Image.SCALE_DEFAULT));

    }
}
