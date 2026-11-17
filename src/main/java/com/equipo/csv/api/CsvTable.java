package com.equipo.csv.api;

import java.util.List;

/**
 * Contrato de solo lectura para una tabla CSV.
 */
public interface CsvTable {

    int rowCount();

    int columnCount();

    boolean hasHeaders();

    List<String> headers();

    String cell(int row, int column);

    List<String> row(int row);
}
