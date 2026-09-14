package gui.mission3;

import algorithms.mission3.DirectedWeightedGraph;
import algorithms.mission3.MaxChurunResult;
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
import java.util.Random;
import java.util.Set;

/**
 * Dibuja el grafo dirigido de la Misión 3. Los pasadizos envenenados
 * por Limón (peso negativo) se marcan en violeta, con chispitas de
 * "veneno" alrededor. El camino/ciclo resaltado usa verde-teal si es
 * una ruta finita, o dorado si es el ciclo responsable de "Infinite
 * churun!". Minerva recorre el camino (1 vuelta) o el ciclo (varias
 * vueltas, para transmitir la idea de "infinito").
 */
public final class GraphCanvas3 extends JPanel {

    private static final int MAX_DRAWABLE_NODES = 60;
    private static final int NODE_RADIUS = 18;
    private static final int LOOP_DURATION_MS = 2000;
    private static final int TIMER_TICK_MS = 30;
    private static final int CYCLE_LOOPS = 3;
    private static final Color SPARKLE_COLOR = new Color(0xc0, 0x8f, 0xff);

    private DirectedWeightedGraph graph;
    private int source;
    private int destination;
    private MaxChurunResult result;
    private boolean hasResult;

    private int[] xs;
    private int[] ys;

    private boolean minervaAnimating;
    private int minervaSegmentIndex;
    private float minervaProgress;
    private int loopsRemaining;
    private Timer animationTimer;

    public GraphCanvas3() {
        setBackground(FelineTheme.BACKGROUND);
    }

    public void showResult(DirectedWeightedGraph graph, int source, int destination, MaxChurunResult result) {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.result = result;
        this.hasResult = true;
        this.minervaAnimating = false;
        repaint();
    }

    public void animateMinervaAlongPath() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        if (!hasResult || result.status() == MaxChurunResult.Status.UNREACHABLE
                || result.path().size() < 2 || xs == null) {
            minervaAnimating = false;
            return;
        }

        boolean isCycle = result.status() == MaxChurunResult.Status.UNBOUNDED;
        loopsRemaining = isCycle ? CYCLE_LOOPS : 1;

        minervaSegmentIndex = 0;
        minervaProgress = 0f;
        minervaAnimating = true;

        int segments = result.path().size() - 1;
        int segmentDurationMs = Math.max(TIMER_TICK_MS, LOOP_DURATION_MS / Math.max(1, segments));
        float progressPerTick = (float) TIMER_TICK_MS / segmentDurationMs;

        animationTimer = new Timer(TIMER_TICK_MS, e -> {
            minervaProgress += progressPerTick;
            if (minervaProgress >= 1f) {
                minervaProgress = 0f;
                minervaSegmentIndex++;
                if (minervaSegmentIndex >= segments) {
                    loopsRemaining--;
                    if (loopsRemaining <= 0) {
                        minervaAnimating = false;
                        animationTimer.stop();
                        repaint();
                        return;
                    }
                    minervaSegmentIndex = 0;
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

        boolean highlightActive = result.status() != MaxChurunResult.Status.UNREACHABLE;
        boolean isCycleResult = result.status() == MaxChurunResult.Status.UNBOUNDED;
        Color highlightColor = isCycleResult ? FelineTheme.ACCENT_GOLD : FelineTheme.ACCENT_HERO;

        Set<Long> highlightedEdges = new HashSet<>();
        List<Integer> path = result.path();
        for (int i = 0; i + 1 < path.size(); i++) {
            highlightedEdges.add(edgeKey(path.get(i), path.get(i + 1)));
        }

        for (DirectedWeightedGraph.Edge edge : graph.allEdges()) {
            boolean onPath = highlightActive && highlightedEdges.contains(edgeKey(edge.from(), edge.to()));
            boolean poisoned = edge.weight() < 0;
            Color edgeColor;
            if (onPath) {
                edgeColor = highlightColor;
            } else if (poisoned) {
                edgeColor = FelineTheme.POISON_PURPLE;
            } else {
                edgeColor = FelineTheme.GRID_LINE;
            }
            g2.setColor(edgeColor);
            g2.setStroke(new BasicStroke(onPath ? 3f : 1.5f));
            drawArrow(g2, xs[edge.from()], ys[edge.from()], xs[edge.to()], ys[edge.to()]);

            if (poisoned) {
                drawPoisonSparkles(g2, xs[edge.from()], ys[edge.from()], xs[edge.to()], ys[edge.to()]);
            }

            drawWeightLabel(g2, xs[edge.from()], ys[edge.from()], xs[edge.to()], ys[edge.to()], edge.weight());
        }
        g2.setStroke(new BasicStroke(1f));

        for (int i = 0; i < n; i++) {
            Color fill;
            if (i == source) {
                fill = FelineTheme.ACCENT_GOLD;
            } else if (i == destination) {
                fill = highlightActive ? highlightColor : FelineTheme.ACCENT_VILLAIN;
            } else if (highlightActive && path.contains(i)) {
                fill = highlightColor;
            } else {
                fill = FelineTheme.SURFACE;
            }
            g2.setColor(fill);
            g2.fillOval(xs[i] - NODE_RADIUS, ys[i] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
            g2.setColor(FelineTheme.GRID_LINE);
            g2.drawOval(xs[i] - NODE_RADIUS, ys[i] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            if (i != destination) {
                String label = String.valueOf(i);
                g2.setColor(FelineTheme.TEXT);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, xs[i] - fm.stringWidth(label) / 2, ys[i] + fm.getAscent() / 2 - 2);
            }
        }

        if (highlightActive) {
            drawStarMark(g2, xs[destination], ys[destination], 9);
        }

        if (minervaAnimating && minervaSegmentIndex < path.size() - 1) {
            int fromNode = path.get(minervaSegmentIndex);
            int toNode = path.get(minervaSegmentIndex + 1);
            int nx = (int) (xs[fromNode] + (xs[toNode] - xs[fromNode]) * minervaProgress);
            int ny = (int) (ys[fromNode] + (ys[toNode] - ys[fromNode]) * minervaProgress);
            CatSprites.drawAvatar(g2, CatSprites.Character.MINERVA, nx, ny, NODE_RADIUS * 2);
        }
    }

    /**
     * Dibuja pequeñas chispas de "veneno" cerca de la arista, en
     * posiciones fijas (semilla derivada de las coordenadas de la
     * arista) para que no cambien de un repintado a otro.
     */
    private void drawPoisonSparkles(Graphics2D g2, int x1, int y1, int x2, int y2) {
        Random rnd = new Random((long) x1 * 31 + y1 * 37 + x2 * 41 + y2 * 43);
        g2.setColor(SPARKLE_COLOR);
        g2.setStroke(new BasicStroke(1.5f));

        int sparkleCount = 4;
        for (int i = 0; i < sparkleCount; i++) {
            double t = 0.15 + rnd.nextDouble() * 0.7; // evita los extremos, cerca de los nodos
            int baseX = (int) (x1 + (x2 - x1) * t);
            int baseY = (int) (y1 + (y2 - y1) * t);
            int offset = 6;
            int px = baseX + rnd.nextInt(offset * 2 + 1) - offset;
            int py = baseY + rnd.nextInt(offset * 2 + 1) - offset;
            int size = 2 + rnd.nextInt(3);
            g2.drawLine(px - size, py, px + size, py);
            g2.drawLine(px, py - size, px, py + size);
        }
    }

    /**
     * Dibuja el peso de la arista desplazado hacia un lado de la línea
     * (no encima), con un fondito detrás -- así se lee bien aunque la
     * línea sea gruesa y de color (cuando la arista está resaltada).
     */
    private void drawWeightLabel(Graphics2D g2, int x1, int y1, int x2, int y2, long weight) {
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        int labelX = midX;
        int labelY = midY;
        if (len > 0) {
            double perpX = -dy / len;
            double perpY = dx / len;
            int offset = 10;
            labelX = midX + (int) (perpX * offset);
            labelY = midY + (int) (perpY * offset);
        }

        String text = String.valueOf(weight);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        g2.setColor(FelineTheme.BACKGROUND);
        g2.fillRoundRect(labelX - textWidth / 2 - 3, labelY - textHeight / 2 - 1, textWidth + 6, textHeight + 2, 6, 6);

        g2.setColor(FelineTheme.TEXT);
        g2.drawString(text, labelX - textWidth / 2, labelY + fm.getAscent() / 2 - 2);
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

    private void drawStarMark(Graphics2D g2, int cx, int cy, int outerRadius) {
        g2.setColor(FelineTheme.ACCENT_GOLD);
        int points = 5;
        int innerRadius = outerRadius / 2;
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        for (int i = 0; i < points * 2; i++) {
            double angle = Math.PI * i / points - Math.PI / 2;
            int r = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = cx + (int) (r * Math.cos(angle));
            yPoints[i] = cy + (int) (r * Math.sin(angle));
        }
        g2.fillPolygon(xPoints, yPoints, points * 2);
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