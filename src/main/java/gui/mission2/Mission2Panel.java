package gui.mission2;

import algorithms.mission2.DijkstraResult;
import algorithms.mission2.DijkstraSolver;
import gui.theme.CardPanel;
import gui.theme.FelineTheme;
import gui.theme.RoundedButton;
import io.mission2.Mission2OutputFormatter;
import io.mission2.Mission2Parser;
import samples.SampleInputs;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public final class Mission2Panel extends JPanel {

    private final JTextArea inputArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final GraphCanvas graphCanvas = new GraphCanvas();

    private final JComboBox<String> caseSelector = new JComboBox<>(new String[]{
            "Caso 1: arista directa",
            "Caso 2: ruta de 2 saltos",
            "Caso 3: ruta con varios nodos"
    });

    public Mission2Panel() {
        super(new BorderLayout(10, 10));
        FelineTheme.stylePanel(this);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        outputArea.setEditable(false);
        outputArea.setRows(8);
        FelineTheme.styleTextArea(inputArea);
        FelineTheme.styleTextArea(outputArea);
        FelineTheme.styleComboBox(caseSelector);

        caseSelector.addActionListener(e -> loadSelectedCase());

        RoundedButton solveButton = new RoundedButton("Resolver");
        solveButton.addActionListener(e -> solve());

        JPanel buttonsPanel = new CardPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 12));
        buttonsPanel.add(caseSelector);
        buttonsPanel.add(solveButton);

        JScrollPane inputScroll = new JScrollPane(inputArea);
        FelineTheme.styleScrollPane(inputScroll);
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        FelineTheme.stylePanel(leftPanel);
        leftPanel.add(inputScroll, BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JScrollPane outputScroll = new JScrollPane(outputArea);
        FelineTheme.styleScrollPane(outputScroll);
        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputScroll, graphCanvas);
        rightSplit.setResizeWeight(0.3);
        rightSplit.setBorder(null);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplit);
        mainSplit.setResizeWeight(0.4);
        mainSplit.setBorder(null);

        add(mainSplit, BorderLayout.CENTER);
    }

    private void loadSelectedCase() {
        String text = switch (caseSelector.getSelectedIndex()) {
            case 1 -> SampleInputs.MISSION_2_CASE_2;
            case 2 -> SampleInputs.MISSION_2_CASE_3;
            default -> SampleInputs.MISSION_2_CASE_1;
        };
        inputArea.setText(text);
        solve();
    }

    private void solve() {
        try {
            Mission2Parser parser = new Mission2Parser();
            List<Mission2Parser.TestCase> cases = parser.parse(inputArea.getText());

            DijkstraSolver solver = new DijkstraSolver();
            Mission2OutputFormatter formatter = new Mission2OutputFormatter();

            StringBuilder output = new StringBuilder();
            int caseNumber = 1;
            for (Mission2Parser.TestCase testCase : cases) {
                DijkstraResult result = solver.solve(testCase.graph(), testCase.source(), testCase.destination());
                output.append(formatter.format(caseNumber, result)).append("\n");
                graphCanvas.showResult(testCase.graph(), testCase.source(), testCase.destination(),
                        result.path(), result.reachable());
                graphCanvas.animateNinaAlongPath();
                caseNumber++;
            }

            outputArea.setText(output.toString());
        } catch (Exception e) {
            outputArea.setText("Error al leer el input: " + e.getMessage());
        }
    }
}