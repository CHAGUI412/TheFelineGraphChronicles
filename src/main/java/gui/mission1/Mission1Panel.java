package gui.mission1;

import algorithms.mission1.BfsSolver;
import algorithms.mission1.DfsSolver;
import algorithms.mission1.PathResult;
import gui.theme.CardPanel;
import gui.theme.FelineTheme;
import gui.theme.RoundedButton;
import io.mission1.Mission1OutputFormatter;
import io.mission1.Mission1Parser;
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

public final class Mission1Panel extends JPanel {

    private final JTextArea inputArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final BoardCanvas boardCanvas = new BoardCanvas();

    private final JComboBox<String> pathViewSelector = new JComboBox<>(new String[]{"Camino BFS", "Camino DFS"});

    private Mission1Parser.TestCase lastCase;
    private PathResult lastBfsResult;
    private PathResult lastDfsResult;

    public Mission1Panel() {
        super(new BorderLayout(10, 10));
        FelineTheme.stylePanel(this);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        outputArea.setEditable(false);
        outputArea.setRows(8);
        FelineTheme.styleTextArea(inputArea);
        FelineTheme.styleTextArea(outputArea);
        FelineTheme.styleComboBox(pathViewSelector);

        RoundedButton loadSampleButton = new RoundedButton("Cargar ejemplo");
        loadSampleButton.addActionListener(e -> {
            inputArea.setText(SampleInputs.MISSION_1);
            solve();
        });

        RoundedButton solveButton = new RoundedButton("Resolver");
        solveButton.addActionListener(e -> solve());

        pathViewSelector.addActionListener(e -> updateCanvas());

        JPanel buttonsPanel = new CardPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 12));
        buttonsPanel.add(loadSampleButton);
        buttonsPanel.add(solveButton);
        buttonsPanel.add(pathViewSelector);

        JScrollPane inputScroll = new JScrollPane(inputArea);
        FelineTheme.styleScrollPane(inputScroll);
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        FelineTheme.stylePanel(leftPanel);
        leftPanel.add(inputScroll, BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JScrollPane outputScroll = new JScrollPane(outputArea);
        FelineTheme.styleScrollPane(outputScroll);
        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputScroll, boardCanvas);
        rightSplit.setResizeWeight(0.3);
        rightSplit.setBorder(null);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplit);
        mainSplit.setResizeWeight(0.4);
        mainSplit.setBorder(null);

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
        boardCanvas.animateCatAlongPath();
    }
}