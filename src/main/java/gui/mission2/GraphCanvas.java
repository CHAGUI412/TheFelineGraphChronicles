package gui.mission2;

import algorithms.mission2.WeightedGraph;
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
 * Dibuja el grafo de la Misión 2: los nodos en círculo, las aristas con
 * su peso, y el camino más corto resaltado en verde-teal. Respeta el
 * límite de 60 nodos de la Sección 2.3.
 */
public final class GraphCanvas extends JPanel {

    private static final int MAX_DRAWABLE_NODES = 60;
    private static final int NODE_RADIUS = 18;

    private WeightedGraph graph;
    private int source;
    private int destination;
    private List<Integer> path = List.of();
    private boolean reachable;
    private boolean hasResult;

    public GraphCanvas() {
        setBackground(FelineTheme.BACKGROUND);
    }

    public void showResult(WeightedGraph graph, int source, int destination, List<Integer> path, boolean reachable) {
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.path = path;
        this.reachable = reachable;
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

        Set<Long> pathEdges = new HashSet<>();
        for (int i = 0; i + 1 < path.size(); i++) {
            pathEdges.add(edgeKey(path.get(i), path.get(i + 1)));
        }

        // Aristas primero, para que los nodos queden encima.
        for (int node = 0; node < n; node++) {
            for (WeightedGraph.Edge edge : graph.neighbours(node)) {
                if (edge.to() < node) continue; // evita dibujar cada arista 2 veces
                boolean onPath = reachable && pathEdges.contains(edgeKey(node, edge.to()));
                g2.setColor(onPath ? FelineTheme.ACCENT_HERO : FelineTheme.GRID_LINE);
                g2.setStroke(new BasicStroke(onPath ? 3f : 1.5f));
                g2.drawLine(xs[node], ys[node], xs[edge.to()], ys[edge.to()]);

                int midX = (xs[node] + xs[edge.to()]) / 2;
                int midY = (ys[node] + ys[edge.to()]) / 2;
                g2.setColor(FelineTheme.TEXT);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.drawString(String.valueOf(edge.weight()), midX, midY);
            }
        }
        g2.setStroke(new BasicStroke(1f));

        // Nodos encima de las aristas.
        for (int i = 0; i < n; i++) {
            Color fill;
            if (i == source) {
                fill = FelineTheme.ACCENT_GOLD;
            } else if (i == destination) {
                fill = reachable ? FelineTheme.ACCENT_HERO : FelineTheme.ACCENT_VILLAIN;
            } else if (reachable && path.contains(i)) {
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