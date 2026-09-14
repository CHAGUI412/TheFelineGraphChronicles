package gui.theme;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class FelineTheme {

    public static final Color BACKGROUND = new Color(0x14, 0x14, 0x1a);
    public static final Color SURFACE = new Color(0x1e, 0x1e, 0x26);
    public static final Color GRID_LINE = new Color(0x33, 0x33, 0x3d);
    public static final Color ACCENT_HERO = new Color(0x2e, 0xa0, 0x7a);
    public static final Color ACCENT_VILLAIN = new Color(0xb0, 0x2e, 0x3a);
    public static final Color ACCENT_GOLD = new Color(0xef, 0x9f, 0x27);
    public static final Color TEXT = new Color(0xf0, 0xf0, 0xf0);
    public static final Color CLAUDE_ORANGE = new Color(0xda, 0x77, 0x56);
    public static final Color POISON_PURPLE = new Color(0x8b, 0x5c, 0xf6);

    // "Segoe UI" es la fuente nativa de Windows 10/11 -- se ve mucho más
    // pulida que "SansSerif" (la fuente lógica genérica de Java). Si no
    // existe (por ejemplo en Mac/Linux), Java cae automáticamente a la
    // fuente por defecto del sistema, sin romper nada.
    private static final String FONT_FAMILY = "Segoe UI";
    private static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 22);
    private static final Font BODY_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);
    private static final Font MONO_FONT = new Font("Consolas", Font.PLAIN, 13);

    private FelineTheme() {
    }

    public static void apply() {
        UIManager.put("control", BACKGROUND);
        UIManager.put("text", TEXT);
        UIManager.put("ComboBox.background", SURFACE);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("ComboBox.selectionBackground", ACCENT_HERO);
        UIManager.put("ComboBox.selectionForeground", TEXT);
        UIManager.put("TabbedPane.selected", SURFACE);
        UIManager.put("TabbedPane.background", BACKGROUND);
        UIManager.put("TabbedPane.foreground", TEXT);
    }

    public static Font titleFont() {
        return TITLE_FONT;
    }

    public static Font buttonFont() {
        return BUTTON_FONT;
    }

    public static void styleTextArea(JTextArea area) {
        area.setBackground(SURFACE);
        area.setForeground(TEXT);
        area.setCaretColor(TEXT);
        area.setFont(MONO_FONT);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND);
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(SURFACE);
        scrollPane.setBorder(new RoundedLineBorder(GRID_LINE, 12));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(SURFACE);
        comboBox.setForeground(TEXT);
        comboBox.setFont(BODY_FONT);
        comboBox.setBorder(new RoundedLineBorder(GRID_LINE, 10));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? ACCENT_HERO : SURFACE);
                c.setForeground(TEXT);
                c.setFont(BODY_FONT);
                return c;
            }
        });
    }

    public static void styleTabbedPane(JTabbedPane tabs) {
        tabs.setBackground(BACKGROUND);
        tabs.setForeground(TEXT);
        tabs.setFont(BUTTON_FONT);
    }

    public static void styleTabsIndividually(JTabbedPane tabs) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.setBackgroundAt(i, SURFACE);
            tabs.setForegroundAt(i, TEXT);
        }
    }

    public static Image windowIcon() {
        int size = 64;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(ACCENT_GOLD);
        g2.fillOval(8, 16, 48, 40);
        g2.fillPolygon(new int[]{12, 22, 20}, new int[]{18, 18, 4}, 3);
        g2.fillPolygon(new int[]{52, 42, 44}, new int[]{18, 18, 4}, 3);

        g2.setColor(BACKGROUND);
        g2.fillOval(20, 32, 6, 6);
        g2.fillOval(38, 32, 6, 6);
        g2.fillOval(29, 42, 6, 5);

        g2.dispose();
        return image;
    }
}