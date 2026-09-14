package gui;

import gui.mission1.Mission1Panel;
import gui.mission2.Mission2Panel;
import gui.mission3.Mission3Panel;
import gui.mission4.Mission4Panel;
import gui.theme.FelineTheme;
import gui.theme.MissionTabBar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

public final class MainFrame extends JFrame {

    public MainFrame() {
        super("The Feline Graph Chronicles");
        FelineTheme.apply();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setIconImage(FelineTheme.windowIcon());

        CardLayout cardLayout = new CardLayout();
        JPanel cards = new JPanel(cardLayout);
        FelineTheme.stylePanel(cards);
        cards.add(new Mission1Panel(), "0");
        cards.add(new Mission2Panel(), "1");
        cards.add(new Mission3Panel(), "2");
        cards.add(new Mission4Panel(), "3");

        String[] labels = {
                "Misión 1 - Campo minado",
                "Misión 2 - Dijkstra",
                "Misión 3 - Comida",
                "Misión 4 - Kruskal"
        };
        MissionTabBar tabBar = new MissionTabBar(labels, index -> cardLayout.show(cards, String.valueOf(index)));

        JPanel tabBarWrapper = new JPanel(new BorderLayout());
        FelineTheme.stylePanel(tabBarWrapper);
        tabBarWrapper.add(tabBar, BorderLayout.CENTER);

        JPanel topContainer = new JPanel(new BorderLayout());
        FelineTheme.stylePanel(topContainer);
        topContainer.add(new TitleBanner(), BorderLayout.NORTH);
        topContainer.add(tabBarWrapper, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topContainer, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
    }
}