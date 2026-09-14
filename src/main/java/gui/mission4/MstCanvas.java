package gui.mission4;

import algorithms.mission4.Edge;
import algorithms.mission4.MstResult;
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

/**
 * Dibuja la red de la Misión 4. Pola y Minerva recorren, juntas, cada
 * cable del árbol final -- en el mismo orden en que Kruskal los fue
 * seleccionando (del más barato al más caro), como si lo estuvieran
 * reparando en ese orden.
 */
public final class MstCanvas extends JPanel {

    private static final int MAX_NODES = 100;
    private static final int MAX_EDGES = 300;
    private static final int NODE_RADIUS = 16;
    private static final int TOTAL_ANIMATION_MS = 3000;
    private static final int TIMER_TICK_MS = 30;
    private static final int AVATAR_OFFSET = 9;

    private int nodeCount;
    private List<Edge> candidateEdges = List.of();
    private MstResult result;
    private boolean hasResult;

    private int[] xs;
    private int[] ys;

    private boolean heroesAnimating;
    private int edgeIndex;
    private float progress;
    private Timer animationTimer;

    public MstCanvas() {
        setBackground(FelineTheme.BACKGROUND);
    }

    public void showResult(int nodeCount, List<Edge> candidateEdges, MstResult result) {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        this.nodeCount = nodeCount;
        this.candidateEdges = candidateEdges;
        this.result = result;
        this.hasResult = true;
        this.heroesAnimating = false;
        repaint();
    }

    /** Anima a Pola y Minerva recorriendo, juntas, cada cable del árbol final, en orden. */
    public void animateHeroesAlongTree() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        if (!hasResult || !result.connected() || result.mstEdges().isEmpty() || xs == null) {
            heroesAnimating = false;
            return;
        }

        edgeIndex = 0;
        progress = 0f;
        heroesAnimating = true;

        int edgeCount = result.mstEdges().size();
        int segmentDurationMs = Math.max(TIMER_TICK_MS, TOTAL_ANIMATION_MS / edgeCount);
        float progressPerTick = (float) TIMER_TICK_MS / segmentDurationMs;

        animationTimer = new Timer(TIMER_TICK_MS, e -> {
            progress += progressPerTick;
            if (progress >= 1f) {
                progress = 0f;
                edgeIndex++;
                if (edgeIndex >= edgeCount) {
                    heroesAnimating = false;
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
            drawCenteredMessage(g2, "Resuelve un caso para ver la red aquí");
            return;
        }
        if (nodeCount > MAX_NODES || candidateEdges.size() > MAX_EDGES) {
            drawCenteredMessage(g2, "Red demasiado grande para dibujar (límite "
                    + MAX_NODES + " nodos / " + MAX_EDGES + " cables)");
            return;
        }

        int n = nodeCount;
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
                drawWeightLabel(g2, xs[edge.a()], ys[edge.a()], xs[edge.b()], ys[edge.b()], edge.cost());
            }
        }
        g2.setStroke(new BasicStroke(1f));

        for (int i = 0; i < n; i++) {
            g2.setColor(result.connected() ? FelineTheme.ACCENT_HERO : FelineTheme.ACCENT_VILLAIN);
            g2.fillOval(xs[i] - NODE_RADIUS, ys[i] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
            g2.setColor(FelineTheme.GRID_LINE);
            g2.drawOval(xs[i] - NODE_RADIUS, ys[i] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            String label = String.valueOf(i + 1);
            g2.setColor(FelineTheme.TEXT);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, xs[i] - fm.stringWidth(label) / 2, ys[i] + fm.getAscent() / 2 - 2);
        }

        if (!result.connected()) {
            drawCenteredMessage(g2, "Limón cortó demasiados cables");
        }

        // Pola y Minerva, juntas, sobre el cable actual del recorrido.
        if (heroesAnimating && edgeIndex < result.mstEdges().size()) {
            Edge current = result.mstEdges().get(edgeIndex);
            int ax = xs[current.a()];
            int ay = ys[current.a()];
            int bx = xs[current.b()];
            int by = ys[current.b()];
            int midX = (int) (ax + (bx - ax) * progress);
            int midY = (int) (ay + (by - ay) * progress);

            double dx = bx - ax;
            double dy = by - ay;
            double len = Math.sqrt(dx * dx + dy * dy);
            int offsetX = 0;
            int offsetY = 0;
            if (len > 0) {
                offsetX = (int) (-dy / len * AVATAR_OFFSET);
                offsetY = (int) (dx / len * AVATAR_OFFSET);
            }

            CatSprites.drawAvatar(g2, CatSprites.Character.POLA,
                    midX + offsetX, midY + offsetY, NODE_RADIUS * 2 - 6);
            CatSprites.drawAvatar(g2, CatSprites.Character.MINERVA,
                    midX - offsetX, midY - offsetY, NODE_RADIUS * 2 - 6);
        }
    }

    private void drawWeightLabel(Graphics2D g2, int x1, int y1, int x2, int y2, long cost) {
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        int labelX = midX;
        int labelY = midY;
        if (len > 0) {
            int offset = 10;
            labelX = midX + (int) (-dy / len * offset);
            labelY = midY + (int) (dx / len * offset);
        }
        String text = String.valueOf(cost);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2.setColor(FelineTheme.BACKGROUND);
        g2.fillRoundRect(labelX - textWidth / 2 - 3, labelY - textHeight / 2 - 1, textWidth + 6, textHeight + 2, 6, 6);
        g2.setColor(FelineTheme.TEXT);
        g2.drawString(text, labelX - textWidth / 2, labelY + fm.getAscent() / 2 - 2);
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