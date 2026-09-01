package gui.mission2;

import algorithms.mission2.DijkstraResult;
import algorithms.mission2.DijkstraSolver;
import io.mission2.Mission2OutputFormatter;
import io.mission2.Mission2Parser;
import samples.SampleInputs;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.util.List;

public final class Mission2Panel extends JPanel {

    private final JTextArea inputArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final GraphCanvas graphCanvas = new GraphCanvas();

    private final JComboBox<String> caseSelector = new JComboBox<>(new String[]{
            "Caso 1: arista directa",
            "Caso 2: ruta de 2 saltos",
            "Caso 3: rutas múltiples"
    });

    public Mission2Panel() {
        super(new BorderLayout());
        outputArea.setEditable(false);
        outputArea.setRows(8);

        caseSelector.addActionListener(e -> loadSelectedCase());

        JButton solveButton = new JButton("Resolver");
        solveButton.addActionListener(e -> solve());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(caseSelector);
        buttonsPanel.add(solveButton);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(outputArea), graphCanvas);
        rightSplit.setResizeWeight(0.3);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplit);
        mainSplit.setResizeWeight(0.4);

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
            Mission2Parser.TestCase lastCase = null;
            DijkstraResult lastResult = null;

            for (Mission2Parser.TestCase testCase : cases) {
                DijkstraResult result = solver.solve(testCase.graph(), testCase.source(), testCase.destination());
                output.append(formatter.format(caseNumber, result)).append("\n");
                lastCase = testCase;
                lastResult = result;
                caseNumber++;
            }

            outputArea.setText(output.toString());

            if (lastCase != null) {
                graphCanvas.showResult(lastCase.graph(), lastCase.source(), lastCase.destination(),
                        lastResult.path(), lastResult.reachable());
            }
        } catch (Exception e) {
            outputArea.setText("Error al leer el input: " + e.getMessage());
        }
    }
}