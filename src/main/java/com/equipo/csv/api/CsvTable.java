package com.equipo.csv.api;

import java.util.List;

/**
 * Contrato de solo lectura para una tabla CSV.
 *
 * <p>Es la abstracción que consumen los formateadores, los impresores y los
 * decoradores. Ninguna implementación debe exponer sus estructuras internas ni
 * permitir modificarlas a través de esta interfaz.</p>
 *
 * <p>Los índices de fila y columna empiezan en cero y deben estar dentro del
 * rango válido; fuera de rango se lanza {@link IndexOutOfBoundsException}.</p>
 */
public interface CsvTable {

    /**
     * Devuelve la cantidad de filas de datos, sin contar el encabezado.
     *
     * @return número de filas, nunca negativo
     */
    int rowCount();

    /**
     * Devuelve la cantidad de columnas de la tabla.
     *
     * @return número de columnas, nunca negativo
     */
    int columnCount();

    /**
     * Indica si la primera fila del CSV se interpretó como encabezado.
     *
     * @return {@code true} si la tabla tiene encabezados
     */
    boolean hasHeaders();

    /**
     * Devuelve los nombres de las columnas.
     *
     * <p>La lista tiene exactamente {@link #columnCount()} elementos, incluso
     * cuando {@link #hasHeaders()} es {@code false}; en ese caso contiene
     * nombres generados.</p>
     *
     * @return lista inmutable con los encabezados
     */
    List<String> headers();

    /**
     * Devuelve el valor de una celda.
     *
     * @param row    índice de fila, desde cero
     * @param column índice de columna, desde cero
     * @return valor de la celda, nunca {@code null}
     * @throws IndexOutOfBoundsException si la fila o la columna no existen
     */
    String cell(int row, int column);

    /**
     * Devuelve todos los valores de una fila.
     *
     * @param row índice de fila, desde cero
     * @return lista inmutable con los valores de la fila
     * @throws IndexOutOfBoundsException si la fila no existe
     */
    List<String> row(int row);
}
