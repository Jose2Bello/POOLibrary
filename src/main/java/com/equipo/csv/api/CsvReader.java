package com.equipo.csv.api;

import java.io.IOException;

/**
 * Carga una tabla mutable desde una fuente CSV.
 *
 * <p>Combina un {@link CsvSource} con un {@link CsvParser} y decide cómo se
 * interpretan los encabezados y las filas desiguales.</p>
 */
public interface CsvReader {

    /**
     * Lee la fuente y construye la tabla correspondiente.
     *
     * @param source origen del CSV
     * @return tabla mutable con los datos leídos
     * @throws IOException si falla la lectura o el contenido es inválido
     */
    MutableCsvTable read(CsvSource source) throws IOException;
}
