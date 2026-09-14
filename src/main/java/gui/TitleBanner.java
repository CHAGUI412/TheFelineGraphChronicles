package gui;

import gui.theme.FelineTheme;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Franja superior con el título temático de la app, más un par de
 * huellas de gato decorativas dibujadas a mano.
 */
public final class TitleBanner extends JPanel {

    private static final int HEIGHT = 56;

    public TitleBanner() {
        setPreferredSize(new Dimension(0, HEIGHT));
        setBackground(FelineTheme.BACKGROUND);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String title = "The Feline Graph Chronicles";
        g2.setFont(FelineTheme.titleFont());
        g2.setColor(FelineTheme.ACCENT_GOLD);
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(title)) / 2;
        g2.drawString(title, textX, HEIGHT / 2 + fm.getAscent() / 2 - 4);

        drawPaw(g2, textX - 34, HEIGHT / 2 - 8);
        drawPaw(g2, textX + fm.stringWidth(title) + 18, HEIGHT / 2 - 8);
    }

    private void drawPaw(Graphics2D g2, int x, int y) {
        g2.setColor(FelineTheme.ACCENT_HERO);
        g2.fillOval(x + 5, y + 8, 12, 11);
        g2.fillOval(x, y, 8, 9);
        g2.fillOval(x + 9, y - 3, 8, 9);
        g2.fillOval(x + 18, y, 8, 9);
    }
}