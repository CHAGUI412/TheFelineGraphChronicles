package gui.mission1;

import algorithms.mission1.BfsSolver;
import algorithms.mission1.DfsSolver;
import algorithms.mission1.PathResult;
import io.mission1.Mission1OutputFormatter;
import io.mission1.Mission1Parser;
import samples.SampleInputs;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.util.List;

public final class Mission1Panel extends JPanel {

    private final JTextArea inputArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final BoardCanvas boardCanvas = new BoardCanvas();

    private final JComboBox<String> pathViewSelector = new JComboBox<>(new String[]{
            "Camino BFS", "Camino DFS"
    });

    private Mission1Parser.TestCase lastCase;
    private PathResult lastBfsResult;
    private PathResult lastDfsResult;

    public Mission1Panel() {
        super(new BorderLayout());
        outputArea.setEditable(false);
        outputArea.setRows(8);

        JButton loadSampleButton = new JButton("Cargar ejemplo");
        loadSampleButton.addActionListener(e -> {
            inputArea.setText(SampleInputs.MISSION_1);
            solve();
        });

        JButton solveButton = new JButton("Resolver");
        solveButton.addActionListener(e -> solve());

        pathViewSelector.addActionListener(e -> updateCanvas());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(loadSampleButton);
        buttonsPanel.add(solveButton);
        buttonsPanel.add(pathViewSelector);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(outputArea), boardCanvas);
        rightSplit.setResizeWeight(0.3);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplit);
        mainSplit.setResizeWeight(0.4);

        add(mainSplit, BorderLayout.CENTER);
    }

    private void solve() {
        try {
            Mission1Parser parser = new Mission1Parser();
            List<Mission1Parser.TestCase> cases = parser.parse(inputArea.getText());

            BfsSolver bfsSolver = new BfsSolver();
            DfsSolver dfsSolver = new DfsSolver();
            Mission1OutputFormatter formatter = new Mission1OutputFormatter();

            StringBuilder output = new StringBuilder();
            int caseNumber = 1;
            lastCase = null;
            lastBfsResult = null;
            lastDfsResult = null;

            for (Mission1Parser.TestCase testCase : cases) {
                PathResult bfsResult = bfsSolver.solve(testCase.board(), testCase.start(), testCase.destination());
                PathResult dfsResult = dfsSolver.solve(testCase.board(), testCase.start(), testCase.destination());
                output.append(formatter.format(caseNumber, bfsResult, dfsResult)).append("\n");
                lastCase = testCase;
                lastBfsResult = bfsResult;
                lastDfsResult = dfsResult;
                caseNumber++;
            }

            outputArea.setText(output.toString());
            updateCanvas();
        } catch (Exception e) {
            outputArea.setText("Error al leer el input: " + e.getMessage());
        }
    }

    private void updateCanvas() {
        if (lastCase == null) {
            return;
        }
        PathResult chosen = pathViewSelector.getSelectedIndex() == 1 ? lastDfsResult : lastBfsResult;
        boardCanvas.showResult(lastCase.board(), lastCase.start(), lastCase.destination(),
                chosen.path(), chosen.reachable());
    }
}