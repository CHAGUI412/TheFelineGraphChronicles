package gui.theme;

import java.awt.Color;

/**
 * Colores compartidos para el tema visual (gatos héroes vs. Limón).
 * Los usa BoardCanvas (y, más adelante, los canvas de las otras misiones)
 * para que todo el tablero visual comparta la misma paleta.
 */
public final class FelineTheme {

    public static final Color BACKGROUND = new Color(0x14, 0x14, 0x1a);
    public static final Color SURFACE = new Color(0x1e, 0x1e, 0x26);
    public static final Color GRID_LINE = new Color(0x33, 0x33, 0x3d);
    public static final Color ACCENT_HERO = new Color(0x2e, 0xa0, 0x7a);
    public static final Color ACCENT_VILLAIN = new Color(0xb0, 0x2e, 0x3a);
    public static final Color ACCENT_GOLD = new Color(0xef, 0x9f, 0x27);
    public static final Color TEXT = new Color(0xf0, 0xf0, 0xf0);

    private FelineTheme() {
    }

    public static void apply() {
        // TODO: aplicar estilo visual a más componentes Swing más adelante.
    }
}