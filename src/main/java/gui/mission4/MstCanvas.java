package gui.mission4;

import algorithms.mission4.Edge;
import algorithms.mission4.MstResult;
import gui.theme.FelineTheme;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Dibuja la red de la Misión 4: todas las conexiones candidatas en
 * gris, y los cables que forman el MST resaltados en verde-teal.
 * Respeta el límite de 100 intersecciones / 300 cables de la Sección 2.3.
 */
public final class MstCanvas extends JPanel {

    private static final int MAX_NODES = 100;
    private static final int MAX_EDGES = 300;
    private static final int NODE_RADIUS = 16;

    private int nodeCount;
    private List<Edge> candidateEdges = List.of();
    private MstResult result;
    private boolean hasResult;

    public MstCanvas() {
        setBackground(FelineTheme.BACKGROUND);
    }

    public void showResult(int nodeCount, List<Edge> candidateEdges, MstResult result) {
        this.nodeCount = nodeCount;
        this.candidateEdges = candidateEdges;
        this.result = result;
        this.hasResult = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!hasResult) {
            drawCenteredMessage(g2, "Resuelve un caso para ver la red aquí");
            return;
        }
        if (nodeCount > MAX_NODES || candidateEdges.size() > MAX_EDGES) {
            drawCenteredMessage(g2, "Red demasiado grande para dibujar (límite "
                    + MAX_NODES + " nodos / " + MAX_EDGES + " cables)");
            return;
        }

        int n = nodeCount;
        int[] xs = new int[n];
        int[] ys = new int[n];
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = Math.max(40, Math.min(getWidth(), getHeight()) / 2 - 40);
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            xs[i] = cx + (int) (radius * Math.cos(angle));
            ys[i] = cy + (int) (radius * Math.sin(angle));
        }

        Set<Long> mstEdgeKeys = new HashSet<>();
        for (Edge e : result.mstEdges()) {
            mstEdgeKeys.add(edgeKey(e.a(), e.b()));
        }

        for (Edge edge : candidateEdges) {
            boolean inMst = mstEdgeKeys.contains(edgeKey(edge.a(), edge.b()));
            g2.setColor(inMst ? FelineTheme.ACCENT_HERO : FelineTheme.GRID_LINE);
            g2.setStroke(new BasicStroke(inMst ? 3f : 1f));
            g2.drawLine(xs[edge.a()], ys[edge.a()], xs[edge.b()], ys[edge.b()]);

            if (inMst) {
                int midX = (xs[edge.a()] + xs[edge.b()]) / 2;
                int midY = (ys[edge.a()] + ys[edge.b()]) / 2;
                g2.setColor(FelineTheme.TEXT);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.drawString(String.valueOf(edge.cost()), midX, midY);
            }
        }
        g2.setStroke(new BasicStroke(1f));

        for (int i = 0; i < n; i++) {
            g2.setColor(result.connected() ? FelineTheme.ACCENT_HERO : FelineTheme.SURFACE);
            g2.fillOval(xs[i] - NODE_RADIUS, ys[i] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
            g2.setColor(FelineTheme.GRID_LINE);
            g2.drawOval(xs[i] - NODE_RADIUS, ys[i] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            String label = String.valueOf(i + 1); // mostramos 1-indexado, como el input original
            g2.setColor(FelineTheme.TEXT);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, xs[i] - fm.stringWidth(label) / 2, ys[i] + fm.getAscent() / 2 - 2);
        }
    }

    private long edgeKey(int a, int b) {
        int lo = Math.min(a, b);
        int hi = Math.max(a, b);
        return (long) lo * 100000 + hi;
    }

    private void drawCenteredMessage(Graphics2D g2, String message) {
        g2.setColor(FelineTheme.TEXT);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        FontMetrics fm = g2.getFontMetrics();
        int textX = Math.max(10, (getWidth() - fm.stringWidth(message)) / 2);
        g2.drawString(message, textX, getHeight() / 2);
    }
}