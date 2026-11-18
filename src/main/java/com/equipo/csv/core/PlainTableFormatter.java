package com.equipo.csv.core;

import com.equipo.csv.api.CsvTable;
import com.equipo.csv.api.TableFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Formateador de tabla en texto plano con bordes y columnas alineadas.
 */
public class PlainTableFormatter implements TableFormatter {

    @Override
    public String format(CsvTable table) {
        if (table.columnCount() == 0) {
            return "(tabla vacia)";
        }

        int[] widths = calculateWidths(table);
        String border = buildBorder(widths);
        List<String> lines = new ArrayList<>();

        lines.add(border);

        if (table.hasHeaders()) {
            lines.add(formatRow(table.headers(), widths));
            lines.add(border);
        }

        for (int row = 0; row < table.rowCount(); row++) {
            lines.add(formatRow(table.row(row), widths));
        }

        lines.add(border);
        return String.join(System.lineSeparator(), lines);
    }

    private int[] calculateWidths(CsvTable table) {
        int[] widths = new int[table.columnCount()];

        if (table.hasHeaders()) {
            List<String> headers = table.headers();
            for (int column = 0; column < headers.size(); column++) {
                widths[column] = Math.max(widths[column], headers.get(column).length());
            }
        }

        for (int row = 0; row < table.rowCount(); row++) {
            for (int column = 0; column < table.columnCount(); column++) {
                widths[column] = Math.max(widths[column], table.cell(row, column).length());
            }
        }

        return widths;
    }

    private String buildBorder(int[] widths) {
        StringBuilder border = new StringBuilder("+");
        for (int width : widths) {
            border.append("-".repeat(width + 2)).append('+');
        }
        return border.toString();
    }

    private String formatRow(List<String> values, int[] widths) {
        StringBuilder line = new StringBuilder("|");
        for (int column = 0; column < widths.length; column++) {
            String value = column < values.size() ? values.get(column) : "";
            line.append(' ')
                    .append(padRight(value, widths[column]))
                    .append(" |");
        }
        return line.toString();
    }

    private String padRight(String value, int width) {
        return value + " ".repeat(Math.max(0, width - value.length()));
    }
}
