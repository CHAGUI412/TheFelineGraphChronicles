package gui.theme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Botón de misión estilo "píldora", con resalte al pasar el mouse y
 * un halo dorado cuando está seleccionado -- reemplaza a las pestañas
 * nativas de Swing, que no daban ningún feedback visual al interactuar.
 */
public final class MissionTabButton extends JButton {

    private static final int ARC = 22;

    private boolean selectedTab;
    private boolean hovering;

    public MissionTabButton(String text) {
        super(text);
        setIcon(new PawIcon());
        setIconTextGap(8);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(FelineTheme.buttonFont());
        setForeground(FelineTheme.TEXT);
        setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }
        });
    }

    public void setSelectedTab(boolean selected) {
        this.selectedTab = selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color fill;
        if (selectedTab) {
            fill = FelineTheme.ACCENT_HERO;
        } else if (hovering) {
            fill = FelineTheme.SURFACE.brighter();
        } else {
            fill = FelineTheme.SURFACE;
        }

        g2.setColor(fill);
        g2.fillRoundRect(0, 0, w - 1, h - 1, ARC, ARC);

        if (selectedTab) {
            g2.setColor(FelineTheme.ACCENT_GOLD);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, ARC, ARC);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}