package gui.theme;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

/** Barra de botones tipo "píldora" para elegir la misión activa. */
public final class MissionTabBar extends JPanel {

    private final List<MissionTabButton> buttons = new ArrayList<>();

    public MissionTabBar(String[] labels, IntConsumer onSelect) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setOpaque(false);

        for (int i = 0; i < labels.length; i++) {
            int index = i;
            MissionTabButton button = new MissionTabButton(labels[i]);
            button.setSelectedTab(i == 0);
            button.addActionListener(e -> {
                select(index);
                onSelect.accept(index);
            });
            buttons.add(button);
            add(button);
        }
    }

    private void select(int index) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setSelectedTab(i == index);
        }
    }
}