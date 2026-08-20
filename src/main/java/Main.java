import gui.MainFrame;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación. Simplemente abre la ventana.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}