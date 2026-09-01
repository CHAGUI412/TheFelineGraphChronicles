package gui.mission3;

import algorithms.mission3.MaxChurunResult;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Muestra la matriz N x N de Floyd-Warshall en una tabla desplazable,
 * usando "-" para pares sin ruta e "inf" para pares con máximo
 * ilimitado -- tal como exige la Sección 5 del enunciado.
 */
public final class MatrixPanel extends JScrollPane {

    private final JTable table = new JTable();

    public MatrixPanel() {
        super();
        setViewportView(table);
    }

    public void showMatrix(MaxChurunResult[][] matrix) {
        int n = matrix.length;
        String[] columnNames = new String[n];
        for (int j = 0; j < n; j++) {
            columnNames[j] = String.valueOf(j);
        }

        String[][] data = new String[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = switch (matrix[i][j].status()) {
                    case UNREACHABLE -> "-";
                    case UNBOUNDED -> "inf";
                    case FINITE -> String.valueOf(matrix[i][j].value());
                };
            }
        }
        table.setModel(new DefaultTableModel(data, columnNames));
    }
}
