package com.equipo.csv.core;

import com.equipo.csv.api.CsvTable;
import com.equipo.csv.api.TableFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Formateador de tabla en sintaxis Markdown.
 *
 * <p>Genera una tabla válida para Markdown, con la fila de encabezado, la línea
 * separadora y una fila por cada registro. Los caracteres {@code |} y los
 * saltos de línea dentro de una celda se escapan para no romper la tabla.</p>
 */
public class MarkdownTableFormatter implements TableFormatter {

    private static final int MIN_WIDTH = 3;

    @Override
    public String format(CsvTable table) {
        if (table.columnCount() == 0) {
            return "(tabla vacia)";
        }

        List<String> headers = escapeAll(table.headers());
        List<List<String>> rows = new ArrayList<>();
        for (int row = 0; row < table.rowCount(); row++) {
            rows.add(escapeAll(table.row(row)));
        }

        int[] widths = calculateWidths(headers, rows);
        List<String> lines = new ArrayList<>();

        lines.add(formatRow(headers, widths));
        lines.add(buildSeparator(widths));

        for (List<String> row : rows) {
            lines.add(formatRow(row, widths));
        }

        return String.join(System.lineSeparator(), lines);
    }

    private List<String> escapeAll(List<String> values) {
        return values.stream()
                .map(this::escape)
                .toList();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace("\r\n", " ")
                .replace('\n', ' ')
                .replace('\r', ' ');
    }

    private int[] calculateWidths(List<String> headers, List<List<String>> rows) {
        int[] widths = new int[headers.size()];

        for (int column = 0; column < headers.size(); column++) {
            widths[column] = Math.max(MIN_WIDTH, headers.get(column).length());
        }

        for (List<String> row : rows) {
            for (int column = 0; column < widths.length; column++) {
                widths[column] = Math.max(widths[column], row.get(column).length());
            }
        }

        return widths;
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

    private String buildSeparator(int[] widths) {
        StringBuilder separator = new StringBuilder("|");
        for (int width : widths) {
            separator.append(' ')
                    .append("-".repeat(width))
                    .append(" |");
        }
        return separator.toString();
    }

    private String padRight(String value, int width) {
        return value + " ".repeat(Math.max(0, width - value.length()));
    }
}
