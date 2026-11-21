package com.equipo.csv.core;

import com.equipo.csv.api.MutableCsvTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Implementación en memoria de una tabla CSV.
 *
 * <p>Valida los datos al construirse y al modificarse: rechaza filas con un
 * número de columnas distinto, valores nulos y encabezados duplicados. Los
 * encabezados vacíos se permiten y no participan en la detección de duplicados,
 * porque el lector los usa para rellenar columnas sin nombre. La comparación de
 * encabezados ignora mayúsculas y espacios en los extremos.</p>
 */
public class DefaultCsvTable implements MutableCsvTable {

    private final List<String> headers;
    private final List<List<String>> rows;
    private final boolean hasHeaders;

    public DefaultCsvTable(List<String> headers, List<List<String>> rows, boolean hasHeaders) {
        this.headers = new ArrayList<>(Objects.requireNonNull(headers, "headers"));
        validateHeaders(this.headers);
        this.rows = new ArrayList<>();
        for (List<String> row : Objects.requireNonNull(rows, "rows")) {
            Objects.requireNonNull(row, "row");
            if (row.size() != this.headers.size()) {
                throw new IllegalArgumentException(
                        "Cada fila debe tener " + this.headers.size() + " columnas");
            }
            this.rows.add(copyValues(row));
        }
        this.hasHeaders = hasHeaders;
    }

    public DefaultCsvTable(List<String> headers, List<List<String>> rows) {
        this(headers, rows, true);
    }

    @Override
    public int rowCount() {
        return rows.size();
    }

    @Override
    public int columnCount() {
        return headers.size();
    }

    @Override
    public boolean hasHeaders() {
        return hasHeaders;
    }

    @Override
    public List<String> headers() {
        return List.copyOf(headers);
    }

    @Override
    public String cell(int row, int column) {
        validateCell(row, column);
        return rows.get(row).get(column);
    }

    @Override
    public List<String> row(int row) {
        validateRow(row);
        return List.copyOf(rows.get(row));
    }

    @Override
    public void addRow(List<String> values) {
        Objects.requireNonNull(values, "values");
        if (values.size() != columnCount()) {
            throw new IllegalArgumentException(
                    "La fila debe tener " + columnCount() + " columnas");
        }
        rows.add(copyValues(values));
    }

    @Override
    public void removeRow(int row) {
        validateRow(row);
        rows.remove(row);
    }

    @Override
    public void addColumn(String header, List<String> values) {
        Objects.requireNonNull(header, "header");
        Objects.requireNonNull(values, "values");
        if (values.size() != rowCount()) {
            throw new IllegalArgumentException(
                    "La columna debe tener " + rowCount() + " valores");
        }
        validateNewHeader(header);

        List<String> copy = copyValues(values);
        headers.add(header);
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).add(copy.get(i));
        }
    }

    @Override
    public void removeColumn(int column) {
        validateColumn(column);
        headers.remove(column);
        for (List<String> row : rows) {
            row.remove(column);
        }
    }

    @Override
    public void setCell(int row, int column, String value) {
        validateCell(row, column);
        rows.get(row).set(column, Objects.requireNonNull(value, "value"));
    }

    private static List<String> copyValues(List<String> values) {
        List<String> copy = new ArrayList<>(values.size());
        for (String value : values) {
            copy.add(Objects.requireNonNull(value, "value"));
        }
        return copy;
    }

    private static String normalizeHeader(String header) {
        return Objects.requireNonNull(header, "header").trim().toLowerCase(Locale.ROOT);
    }

    private static void validateHeaders(List<String> headers) {
        Set<String> seen = new HashSet<>();
        for (String header : headers) {
            String key = normalizeHeader(header);
            if (!key.isEmpty() && !seen.add(key)) {
                throw new IllegalArgumentException("Encabezado duplicado: " + header);
            }
        }
    }

    private void validateNewHeader(String header) {
        String key = normalizeHeader(header);
        if (key.isEmpty()) {
            return;
        }
        for (String existing : headers) {
            if (normalizeHeader(existing).equals(key)) {
                throw new IllegalArgumentException("Encabezado duplicado: " + header);
            }
        }
    }

    private void validateCell(int row, int column) {
        validateRow(row);
        validateColumn(column);
    }

    private void validateRow(int row) {
        if (row < 0 || row >= rowCount()) {
            throw new IndexOutOfBoundsException("Fila fuera de rango: " + row);
        }
    }

    private void validateColumn(int column) {
        if (column < 0 || column >= columnCount()) {
            throw new IndexOutOfBoundsException("Columna fuera de rango: " + column);
        }
    }
}
