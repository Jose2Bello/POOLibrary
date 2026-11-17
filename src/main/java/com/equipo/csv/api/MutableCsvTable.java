package com.equipo.csv.api;

import java.util.Collections;
import java.util.List;

/**
 * Contrato de escritura para una tabla CSV.
 */
public interface MutableCsvTable extends CsvTable {

    void addRow(List<String> values);

    void removeRow(int row);

    void addColumn(String header, List<String> values);

    void removeColumn(int column);

    void setCell(int row, int column, String value);

    /**
     * Agrega una columna vacía a todas las filas.
     */
    default void addColumn(String header) {
        addColumn(header, Collections.nCopies(rowCount(), ""));
    }
}
