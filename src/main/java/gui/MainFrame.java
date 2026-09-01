package gui;

import gui.mission1.Mission1Panel;
import gui.mission2.Mission2Panel;
import gui.mission3.Mission3Panel;
import gui.mission4.Mission4Panel;
import gui.theme.FelineTheme;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Ventana principal. Por ahora solo tiene la pestaña de la Misión 1;
 * agregamos las demás a medida que las construyamos.
 */
public final class MainFrame extends JFrame {

    public MainFrame() {
        super("The Feline Graph Chronicles");
        FelineTheme.apply();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Misión 1 - Campo minado", new Mission1Panel());
        tabs.addTab("Misión 2 - Dijkstra", new Mission2Panel());
        tabs.addTab("Misión 3 - Comida", new Mission3Panel());
        tabs.addTab("Misión 4 - Kruskal", new Mission4Panel());

        setContentPane(tabs);
    }
}