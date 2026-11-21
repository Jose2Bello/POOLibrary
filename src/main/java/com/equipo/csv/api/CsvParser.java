package com.equipo.csv.api;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Convierte texto CSV en filas de valores.
 *
 * <p>Es el punto de extensión para cambiar el formato de entrada: otro
 * delimitador, otro tipo de comillas u otro estándar CSV. El lector y el resto
 * de la librería no necesitan cambios cuando se agrega una implementación.</p>
 */
public interface CsvParser {

    /**
     * Lee todo el contenido y devuelve las filas encontradas.
     *
     * <p>No se encarga de cerrar el {@link Reader}. Cada fila conserva el orden
     * de las columnas del texto original.</p>
     *
     * @param reader texto CSV de entrada
     * @return lista de filas; cada fila es una lista de celdas
     * @throws IOException si falla la lectura o el CSV es inválido
     */
    List<List<String>> parse(Reader reader) throws IOException;
}
