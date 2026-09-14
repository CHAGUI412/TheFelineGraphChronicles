package gui.mission2;

import algorithms.mission2.WeightedGraph;
import gui.theme.CatSprites;
import gui.theme.FelineTheme;

import javax.swing.JPanel;
import javax.swing.Timer;
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

public final class GraphCanvas extends JPanel {

    private static final int MAX_DRAWABLE_NODES = 60;
    private static final int NODE_RADIUS = 18;
    private static final int TOTAL_ANIMATION_MS = 2500;
    private static final int TIMER_TICK_MS = 30;

    private WeightedGraph graph;
    private int source;
    private int destination;
    private List<Integer> path = List.of();
    private boolean reachable;
    private boolean hasResult;

    private int[] xs;
    private int[] ys;

    private boolean ninaAnimating;
    private int ninaSegmentIndex;
    private float ninaProgress;
    private Timer animationTimer;

    public GraphCanvas() {
        setBackground(FelineTheme.BACKGROUND);
    }

    public void showResult(WeightedGraph graph, int source, int destination, List<Integer> path, boolean reachable) {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.path = path;
        this.reachable = reachable;
        this.hasResult = true;
        this.ninaAnimating = false;
        repaint();
    }

    public void animateNinaAlongPath() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        if (!hasResult || !reachable || path.size() < 2 || xs == null) {
            ninaAnimating = false;
            return;
        }

        ninaSegmentIndex = 0;
        ninaProgress = 0f;
        ninaAnimating = true;

        int segments = path.size() - 1;
        int segmentDurationMs = Math.max(TIMER_TICK_MS, TOTAL_ANIMATION_MS / segments);
        float progressPerTick = (float) TIMER_TICK_MS / segmentDurationMs;

        animationTimer = new Timer(TIMER_TICK_MS, e -> {
            ninaProgress += progressPerTick;
            if (ninaProgress >= 1f) {
                ninaProgress = 0f;
                ninaSegmentIndex++;
                if (ninaSegmentIndex >= segments) {
                    ninaAnimating = false;
                    animationTimer.stop();
                    repaint();
                    return;
                }
            }
            repaint();
        });
        animationTimer.start();
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
        xs = new int[n];
        ys = new int[n];
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

        for (int node = 0; node < n; node++) {
            for (WeightedGraph.Edge edge : graph.neighbours(node)) {
                if (edge.to() < node) continue;
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

            // El nodo destino NO muestra su número -- ahí va la marca de
            // Claude/fallo en su lugar (se dibuja después, centrada).
            if (i != destination) {
                String label = String.valueOf(i);
                g2.setColor(FelineTheme.TEXT);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, xs[i] - fm.stringWidth(label) / 2, ys[i] + fm.getAscent() / 2 - 2);
            }
        }

        drawLabelAbove(g2, xs[source], ys[source], "START", FelineTheme.ACCENT_GOLD);
        drawLabelAbove(g2, xs[destination], ys[destination], "DESTINATION",
                reachable ? FelineTheme.ACCENT_HERO : FelineTheme.ACCENT_VILLAIN);

        // Marca de Claude (o X de fallo) EN EL CENTRO del nodo destino,
        // en blanco para que se vea bien tanto sobre verde como sobre rojo.
        if (reachable) {
            drawSparkMark(g2, xs[destination], ys[destination], 10, FelineTheme.CLAUDE_ORANGE);
        } else {
            drawFailureMark(g2, xs[destination], ys[destination], FelineTheme.TEXT);
        }

        if (ninaAnimating && ninaSegmentIndex < path.size() - 1) {
            int fromNode = path.get(ninaSegmentIndex);
            int toNode = path.get(ninaSegmentIndex + 1);
            int nx = (int) (xs[fromNode] + (xs[toNode] - xs[fromNode]) * ninaProgress);
            int ny = (int) (ys[fromNode] + (ys[toNode] - ys[fromNode]) * ninaProgress);
            CatSprites.drawAvatar(g2, CatSprites.Character.NINA, nx, ny, NODE_RADIUS * 2);
        }
    }

    private void drawLabelAbove(Graphics2D g2, int x, int y, String text, Color color) {
        g2.setColor(color);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, x - fm.stringWidth(text) / 2, y - NODE_RADIUS - 12);
    }

    private void drawSparkMark(Graphics2D g2, int cx, int cy, int size, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2f));
        int spokes = 8;
        for (int i = 0; i < spokes; i++) {
            double angle = Math.PI * i / (spokes / 2.0);
            int x2 = cx + (int) (size * Math.cos(angle));
            int y2 = cy + (int) (size * Math.sin(angle));
            g2.drawLine(cx, cy, x2, y2);
        }
    }

    private void drawFailureMark(Graphics2D g2, int cx, int cy, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2f));
        int s = 7;
        g2.drawLine(cx - s, cy - s, cx + s, cy + s);
        g2.drawLine(cx - s, cy + s, cx + s, cy - s);
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