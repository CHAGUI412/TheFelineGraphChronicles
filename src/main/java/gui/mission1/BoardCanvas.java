package gui.mission1;

import algorithms.mission1.Board;
import algorithms.mission1.Point;
import gui.theme.FelineTheme;

import javax.swing.JPanel;
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
 * Dibuja el tablero de la Misión 1: bombas (rojo, "X"), inicio ("S",
 * dorado) y el camino resaltado (verde-teal), respetando el límite de
 * 50x50 de la Sección 2.3 -- por encima solo muestra un mensaje.
 */
public final class BoardCanvas extends JPanel {

    private static final int MAX_DRAWABLE_SIZE = 50;

    private Board board;
    private Point start;
    private Point destination;
    private List<Point> path = List.of();
    private boolean reachable;
    private boolean hasResult;

    public BoardCanvas() {
        setBackground(FelineTheme.BACKGROUND);
    }

    /** Llamado desde Mission1Panel después de resolver un caso. */
    public void showResult(Board board, Point start, Point destination, List<Point> path, boolean reachable) {
        this.board = board;
        this.start = start;
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
            drawCenteredMessage(g2, "Resuelve un caso para ver el tablero aquí");
            return;
        }
        if (board.rows() > MAX_DRAWABLE_SIZE || board.cols() > MAX_DRAWABLE_SIZE) {
            drawCenteredMessage(g2, "Tablero de " + board.rows() + "x" + board.cols()
                    + " -- demasiado grande para dibujar (límite " + MAX_DRAWABLE_SIZE + "x" + MAX_DRAWABLE_SIZE + ")");
            return;
        }

        int rows = board.rows();
        int cols = board.cols();

        int margin = 12;
        int cellSize = Math.max(4, Math.min(
                (getWidth() - margin * 2) / cols,
                (getHeight() - margin * 2) / rows));

        int offsetX = (getWidth() - cellSize * cols) / 2;
        int offsetY = (getHeight() - cellSize * rows) / 2;

        Set<Point> pathSet = new HashSet<>(path);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = offsetX + c * cellSize;
                int y = offsetY + r * cellSize;
                Point here = new Point(r, c);

                if (board.isBomb(r, c)) {
                    drawCell(g2, x, y, cellSize, FelineTheme.ACCENT_VILLAIN, "X");
                } else if (reachable && pathSet.contains(here)) {
                    drawCell(g2, x, y, cellSize, FelineTheme.ACCENT_HERO, null);
                } else {
                    drawCell(g2, x, y, cellSize, FelineTheme.SURFACE, null);
                }
            }
        }

        // Inicio y destino se dibujan al final, encima, para que su
        // etiqueta nunca quede tapada por el color base de la celda.
        if (start != null) {
            int x = offsetX + start.col() * cellSize;
            int y = offsetY + start.row() * cellSize;
            drawCell(g2, x, y, cellSize, FelineTheme.ACCENT_GOLD, "S");
        }
        if (destination != null) {
            int x = offsetX + destination.col() * cellSize;
            int y = offsetY + destination.row() * cellSize;
            drawCell(g2, x, y, cellSize,
                    reachable ? FelineTheme.ACCENT_HERO : FelineTheme.ACCENT_VILLAIN, "N");
        }
    }

    private void drawCell(Graphics2D g2, int x, int y, int size, Color fill, String label) {
        g2.setColor(fill);
        g2.fillRect(x, y, size, size);
        g2.setColor(FelineTheme.GRID_LINE);
        g2.drawRect(x, y, size, size);

        if (label != null && size >= 12) {
            g2.setColor(FelineTheme.TEXT);
            g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(9, size / 2)));
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (size - fm.stringWidth(label)) / 2;
            int textY = y + (size + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(label, textX, textY);
        }
    }

    private void drawCenteredMessage(Graphics2D g2, String message) {
        g2.setColor(FelineTheme.TEXT);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        FontMetrics fm = g2.getFontMetrics();
        int textX = Math.max(10, (getWidth() - fm.stringWidth(message)) / 2);
        g2.drawString(message, textX, getHeight() / 2);
    }
}