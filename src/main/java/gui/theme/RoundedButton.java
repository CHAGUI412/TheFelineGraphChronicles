package gui.theme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Botón con esquinas redondeadas y una sombra sutil -- reemplaza al JButton plano. */
public final class RoundedButton extends JButton {

    private static final int ARC = 14;

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(FelineTheme.TEXT);
        setFont(FelineTheme.buttonFont());
        setBackground(FelineTheme.ACCENT_HERO);
        setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRoundRect(2, 3, w - 4, h - 4, ARC, ARC);

        Color base = getModel().isPressed() ? getBackground().darker() : getBackground();
        g2.setColor(base);
        g2.fillRoundRect(0, 0, w - 2, h - 3, ARC, ARC);

        g2.dispose();
        super.paintComponent(g);
    }
}