package com.equipo.csv.core;

import com.equipo.csv.api.CsvTable;
import com.equipo.csv.api.TableFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Formateador que exporta la tabla de vuelta a texto CSV.
 *
 * <p>Sigue las reglas de RFC 4180: un valor se entrecomilla cuando contiene el
 * delimitador, comillas o saltos de línea, y las comillas internas se duplican.
 * El delimitador y el separador de línea son configurables.</p>
 *
 * <p>La fila de encabezado solo se escribe cuando la tabla declara tener
 * encabezados, de modo que una tabla sin ellos se exporta únicamente con sus
 * filas de datos.</p>
 */
public class CsvTableFormatter implements TableFormatter {

    private final char delimiter;
    private final String lineSeparator;

    /**
     * Crea el formateador con coma y el separador de línea del sistema.
     */
    public CsvTableFormatter() {
        this(',', System.lineSeparator());
    }

    /**
     * Crea el formateador con un delimitador propio.
     *
     * @param delimiter carácter que separa las columnas
     */
    public CsvTableFormatter(char delimiter) {
        this(delimiter, System.lineSeparator());
    }

    /**
     * Crea el formateador con delimitador y separador de línea propios.
     *
     * @param delimiter     carácter que separa las columnas
     * @param lineSeparator texto usado entre filas
     */
    public CsvTableFormatter(char delimiter, String lineSeparator) {
        this.delimiter = delimiter;
        this.lineSeparator = Objects.requireNonNull(lineSeparator, "lineSeparator");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(CsvTable table) {
        List<String> lines = new ArrayList<>();

        if (table.hasHeaders() && table.columnCount() > 0) {
            lines.add(formatRow(table.headers()));
        }

        for (int row = 0; row < table.rowCount(); row++) {
            lines.add(formatRow(table.row(row)));
        }

        return String.join(lineSeparator, lines);
    }

    private String formatRow(List<String> values) {
        StringBuilder line = new StringBuilder();
        for (int column = 0; column < values.size(); column++) {
            if (column > 0) {
                line.append(delimiter);
            }
            line.append(escape(values.get(column)));
        }
        return line.toString();
    }

    private String escape(String value) {
        boolean needsQuotes = value.indexOf(delimiter) >= 0
                || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0
                || value.indexOf('\r') >= 0;

        if (!needsQuotes) {
            return value;
        }

        return '"' + value.replace("\"", "\"\"") + '"';
    }
}
