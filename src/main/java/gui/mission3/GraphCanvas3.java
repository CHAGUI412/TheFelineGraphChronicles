package gui.mission3;

import algorithms.mission3.DirectedWeightedGraph;
import algorithms.mission3.MaxChurunResult;
import gui.theme.FelineTheme;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Dibuja el grafo dirigido de la Misión 3 (con flechas, a diferencia
 * del de Misión 2), resaltando el camino real o, si el resultado es
 * "Infinite churun!", el ciclo responsable en su lugar.
 */
public final class GraphCanvas3 extends JPanel {

    private static final int MAX_DRAWABLE_NODES = 60;
    private static final int NODE_RADIUS = 18;

    private DirectedWeightedGraph graph;
    private int source;
    private int destination;
    private MaxChurunResult result;
    private boolean hasResult;

    public GraphCanvas3() {
        setBackground(FelineTheme.BACKGROUND);
    }

    public void showResult(DirectedWeightedGraph graph, int source, int destination, MaxChurunResult result) {
        this.graph = graph;
        this.source = source;
        this.destination = destination;
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
            drawCenteredMessage(g2, "Resuelve un caso para ver el grafo aquí");
            return;
        }
        if (graph.nodeCount() > MAX_DRAWABLE_NODES) {
            drawCenteredMessage(g2, "Grafo de " + graph.nodeCount()
                    + " nodos -- demasiado grande para dibujar (límite " + MAX_DRAWABLE_NODES + ")");
            return;
        }

        int n = graph.nodeCount();
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

        boolean highlightActive = result.status() != MaxChurunResult.Status.UNREACHABLE;
        Set<Long> highlightedEdges = new HashSet<>();
        List<Integer> path = result.path();
        for (int i = 0; i + 1 < path.size(); i++) {
            highlightedEdges.add(edgeKey(path.get(i), path.get(i + 1)));
        }

        for (DirectedWeightedGraph.Edge edge : graph.allEdges()) {
            boolean onPath = highlightActive && highlightedEdges.contains(edgeKey(edge.from(), edge.to()));
            g2.setColor(onPath ? FelineTheme.ACCENT_HERO : FelineTheme.GRID_LINE);
            g2.setStroke(new BasicStroke(onPath ? 3f : 1.2f));
            drawArrow(g2, xs[edge.from()], ys[edge.from()], xs[edge.to()], ys[edge.to()]);

            int midX = (xs[edge.from()] + xs[edge.to()]) / 2;
            int midY = (ys[edge.from()] + ys[edge.to()]) / 2;
            g2.setColor(FelineTheme.TEXT);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString(String.valueOf(edge.weight()), midX, midY);
        }
        g2.setStroke(new BasicStroke(1f));

        for (int i = 0; i < n; i++) {
            Color fill;
            if (i == source) {
                fill = FelineTheme.ACCENT_GOLD;
            } else if (i == destination) {
                fill = highlightActive ? FelineTheme.ACCENT_HERO : FelineTheme.ACCENT_VILLAIN;
            } else if (highlightActive && path.contains(i)) {
                fill = FelineTheme.ACCENT_HERO;
            } else {
                fill = FelineTheme.SURFACE;
            }
            g2.setColor(fill);
            g2.fillOval(xs[i] - NODE_RADIUS, ys[i] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
            g2.setColor(FelineTheme.GRID_LINE);
            g2.drawOval(xs[i] - NODE_RADIUS, ys[i] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            String label = String.valueOf(i);
            g2.setColor(FelineTheme.TEXT);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, xs[i] - fm.stringWidth(label) / 2, ys[i] + fm.getAscent() / 2 - 2);
        }
    }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length < 1) return;
        double ux = dx / length;
        double uy = dy / length;

        int endX = (int) (x2 - ux * NODE_RADIUS);
        int endY = (int) (y2 - uy * NODE_RADIUS);
        g2.drawLine(x1, y1, endX, endY);

        double arrowLength = 8;
        double arrowAngle = Math.toRadians(25);
        double angle = Math.atan2(dy, dx);
        int ax1 = (int) (endX - arrowLength * Math.cos(angle - arrowAngle));
        int ay1 = (int) (endY - arrowLength * Math.sin(angle - arrowAngle));
        int ax2 = (int) (endX - arrowLength * Math.cos(angle + arrowAngle));
        int ay2 = (int) (endY - arrowLength * Math.sin(angle + arrowAngle));
        g2.drawLine(endX, endY, ax1, ay1);
        g2.drawLine(endX, endY, ax2, ay2);
    }

    private long edgeKey(int a, int b) {
        return (long) a * 100000 + b;
    }

    private void drawCenteredMessage(Graphics2D g2, String message) {
        g2.setColor(FelineTheme.TEXT);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        FontMetrics fm = g2.getFontMetrics();
        int textX = Math.max(10, (getWidth() - fm.stringWidth(message)) / 2);
        g2.drawString(message, textX, getHeight() / 2);
    }
}
