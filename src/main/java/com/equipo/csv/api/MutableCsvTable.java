package com.equipo.csv.api;

import java.util.Collections;
import java.util.List;

/**
 * Contrato de escritura sobre una tabla CSV.
 *
 * <p>Extiende {@link CsvTable} con las operaciones que modifican la tabla. La
 * separación entre lectura y escritura permite que el resto de la librería
 * dependa solo de {@link CsvTable} cuando no necesita modificar datos.</p>
 */
public interface MutableCsvTable extends CsvTable {

    /**
     * Agrega una fila al final de la tabla.
     *
     * @param values valores de la fila; debe tener {@link #columnCount()} elementos
     * @throws IllegalArgumentException si la cantidad de valores no coincide
     * @throws NullPointerException     si la lista o algún valor es {@code null}
     */
    void addRow(List<String> values);

    /**
     * Elimina la fila indicada.
     *
     * @param row índice de fila, desde cero
     * @throws IndexOutOfBoundsException si la fila no existe
     */
    void removeRow(int row);

    /**
     * Agrega una columna con un valor por fila.
     *
     * @param header nombre de la nueva columna
     * @param values valores de la columna; debe tener {@link #rowCount()} elementos
     * @throws IllegalArgumentException si la cantidad de valores no coincide o el
     *                                  encabezado ya existe
     * @throws NullPointerException     si el encabezado o algún valor es {@code null}
     */
    void addColumn(String header, List<String> values);

    /**
     * Elimina la columna indicada de todas las filas.
     *
     * @param column índice de columna, desde cero
     * @throws IndexOutOfBoundsException si la columna no existe
     */
    void removeColumn(int column);

    /**
     * Reemplaza el valor de una celda.
     *
     * @param row    índice de fila, desde cero
     * @param column índice de columna, desde cero
     * @param value  nuevo valor
     * @throws IndexOutOfBoundsException si la fila o la columna no existen
     * @throws NullPointerException      si el valor es {@code null}
     */
    void setCell(int row, int column, String value);

    /**
     * Agrega una columna vacía a todas las filas.
     *
     * @param header nombre de la nueva columna
     * @throws IllegalArgumentException si el encabezado ya existe
     * @throws NullPointerException     si el encabezado es {@code null}
     */
    default void addColumn(String header) {
        addColumn(header, Collections.nCopies(rowCount(), ""));
    }
}
