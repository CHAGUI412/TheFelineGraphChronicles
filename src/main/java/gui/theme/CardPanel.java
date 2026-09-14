package gui.theme;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Panel con fondo redondeado y borde sutil -- le da profundidad frente al fondo oscuro plano. */
public final class CardPanel extends JPanel {

    private static final int ARC = 16;

    public CardPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRoundRect(2, 3, w - 4, h - 4, ARC, ARC);

        g2.setColor(FelineTheme.SURFACE);
        g2.fillRoundRect(0, 0, w - 2, h - 3, ARC, ARC);

        g2.setColor(FelineTheme.GRID_LINE);
        g2.drawRoundRect(0, 0, w - 3, h - 4, ARC, ARC);

        g2.dispose();
        super.paintComponent(g);
    }
}