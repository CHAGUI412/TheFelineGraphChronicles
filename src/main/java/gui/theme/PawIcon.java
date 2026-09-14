package gui.theme;

import javax.swing.Icon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Ícono de huella de gato, dibujado a mano (sin archivos de imagen
 * externos), usado en las pestañas de cada misión.
 */
public final class PawIcon implements Icon {

    private static final int SIZE = 16;

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(FelineTheme.ACCENT_GOLD);

        g2.fillOval(x + 5, y + 8, 6, 6);
        g2.fillOval(x + 1, y + 3, 4, 5);
        g2.fillOval(x + 6, y + 1, 4, 5);
        g2.fillOval(x + 11, y + 3, 4, 5);

        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }
}