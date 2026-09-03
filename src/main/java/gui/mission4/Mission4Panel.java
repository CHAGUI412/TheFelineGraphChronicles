package gui.mission4;

import algorithms.mission4.KruskalSolver;
import algorithms.mission4.MstResult;
import io.mission4.Mission4OutputFormatter;
import io.mission4.Mission4Parser;
import samples.SampleInputs;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.util.List;

public final class Mission4Panel extends JPanel {

    private final JTextArea inputArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final MstCanvas mstCanvas = new MstCanvas();

    private final JComboBox<String> caseSelector = new JComboBox<>(new String[]{
            "Caso oficial: árbol de 4 nodos",
            "Ejemplo: red desconectada",
            "Ejemplo: red más grande"
    });

    public Mission4Panel() {
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
                new JScrollPane(outputArea), mstCanvas);
        rightSplit.setResizeWeight(0.3);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplit);
        mainSplit.setResizeWeight(0.4);

        add(mainSplit, BorderLayout.CENTER);
    }

    private void loadSelectedCase() {
        String text = switch (caseSelector.getSelectedIndex()) {
            case 1 -> SampleInputs.MISSION_4_DISCONNECTED;
            case 2 -> SampleInputs.MISSION_4_BIGGER;
            default -> SampleInputs.MISSION_4;
        };
        inputArea.setText(text);
        solve();
    }

    private void solve() {
        try {
            Mission4Parser parser = new Mission4Parser();
            List<Mission4Parser.TestCase> cases = parser.parse(inputArea.getText());

            KruskalSolver solver = new KruskalSolver();
            Mission4OutputFormatter formatter = new Mission4OutputFormatter();

            StringBuilder output = new StringBuilder();
            int caseNumber = 1;
            Mission4Parser.TestCase lastCase = null;
            MstResult lastResult = null;

            for (Mission4Parser.TestCase testCase : cases) {
                MstResult result = solver.solve(testCase.nodeCount(), testCase.candidateEdges());
                output.append(formatter.format(caseNumber, result)).append("\n");
                lastCase = testCase;
                lastResult = result;
                caseNumber++;
            }

            outputArea.setText(output.toString());

            if (lastCase != null) {
                mstCanvas.showResult(lastCase.nodeCount(), lastCase.candidateEdges(), lastResult);
            }
        } catch (Exception e) {
            outputArea.setText("Error al leer el input: " + e.getMessage());
        }
    }
}