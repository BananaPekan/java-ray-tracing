package banana.pekan.raytracing.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Window {

    public static int width = 750;
    public static int height = 750;

    JFrame frame;
    Panel panel;

    final Map<Integer, Boolean> keys;

    public Window() {
        frame = new JFrame();
        frame.setBounds(0, 0, width, height);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new Panel(width, height);
        Dimension dimension = new Dimension(width, height);
        panel.setMinimumSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setMaximumSize(dimension);

        frame.add(panel);

        keys = Collections.synchronizedMap(new HashMap<>());

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                synchronized (keys) {
                    keys.put(e.getKeyCode(), true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                synchronized (keys) {
                    keys.put(e.getKeyCode(), false);
                }
            }
        });

        frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "black cursor"));

        frame.pack();

        frame.setVisible(true);

        while (true) {
            HashMap<Integer, Boolean> keysCon = new HashMap<>(keys);
            for (int keyCode : keysCon.keySet()) {
                if (keysCon.get(keyCode)) {
                   panel.onKeyDown(keyCode);
                }
            }
            panel.repaint();
        }
    }

}
