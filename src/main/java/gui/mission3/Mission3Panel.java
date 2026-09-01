package gui.mission3;

import algorithms.mission3.BellmanFordSolver;
import algorithms.mission3.DirectedWeightedGraph;
import algorithms.mission3.FloydWarshallSolver;
import algorithms.mission3.MaxChurunResult;
import io.mission3.Mission3OutputFormatter;
import io.mission3.Mission3Parser;
import samples.SampleInputs;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.util.List;

public final class Mission3Panel extends JPanel {

    private final JTextArea inputArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final GraphCanvas3 graphCanvas = new GraphCanvas3();
    private final MatrixPanel matrixPanel = new MatrixPanel();

    public Mission3Panel() {
        super(new BorderLayout());
        outputArea.setEditable(false);
        outputArea.setRows(8);

        JButton loadSampleButton = new JButton("Cargar ejemplo");
        loadSampleButton.addActionListener(e -> {
            inputArea.setText(SampleInputs.MISSION_3);
            solve();
        });

        JButton solveButton = new JButton("Resolver");
        solveButton.addActionListener(e -> solve());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(loadSampleButton);
        buttonsPanel.add(solveButton);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JTabbedPane visualTabs = new JTabbedPane();
        visualTabs.addTab("Grafo", graphCanvas);
        visualTabs.addTab("Matriz", matrixPanel);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(outputArea), visualTabs);
        rightSplit.setResizeWeight(0.3);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplit);
        mainSplit.setResizeWeight(0.4);

        add(mainSplit, BorderLayout.CENTER);
    }

    private void solve() {
        try {
            Mission3Parser parser = new Mission3Parser();
            List<Mission3Parser.TestCase> cases = parser.parse(inputArea.getText());

            FloydWarshallSolver floydWarshallSolver = new FloydWarshallSolver();
            BellmanFordSolver bellmanFordSolver = new BellmanFordSolver();
            Mission3OutputFormatter formatter = new Mission3OutputFormatter();

            StringBuilder output = new StringBuilder();
            int caseNumber = 1;
            DirectedWeightedGraph lastGraph = null;
            int lastSource = 0;
            int lastDestination = 0;
            MaxChurunResult lastResult = null;
            MaxChurunResult[][] lastMatrix = null;

            for (Mission3Parser.TestCase testCase : cases) {
                MaxChurunResult[][] matrix = floydWarshallSolver.solveAllPairs(testCase.graph());
                MaxChurunResult floydWarshallResult = matrix[testCase.source()][testCase.destination()];
                MaxChurunResult bellmanFordResult = bellmanFordSolver.solveFromSource(
                        testCase.graph(), testCase.source(), testCase.destination());

                boolean mismatch = floydWarshallResult.status() != bellmanFordResult.status()
                        || (floydWarshallResult.status() == MaxChurunResult.Status.FINITE
                        && floydWarshallResult.value() != bellmanFordResult.value());

                if (mismatch) {
                    output.append("Case #").append(caseNumber)
                            .append(": DESACUERDO entre Floyd-Warshall y Bellman-Ford\n");
                } else {
                    output.append(formatter.format(caseNumber, bellmanFordResult)).append("\n");
                }

                lastGraph = testCase.graph();
                lastSource = testCase.source();
                lastDestination = testCase.destination();
                lastResult = bellmanFordResult;
                lastMatrix = matrix;
                caseNumber++;
            }

            outputArea.setText(output.toString());

            if (lastGraph != null) {
                graphCanvas.showResult(lastGraph, lastSource, lastDestination, lastResult);
                matrixPanel.showMatrix(lastMatrix);
            }
        } catch (Exception e) {
            outputArea.setText("Error al leer el input: " + e.getMessage());
        }
    }
}