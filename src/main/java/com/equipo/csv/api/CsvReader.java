package com.equipo.csv.api;

import java.io.IOException;

/**
 * Carga una tabla mutable desde una fuente CSV.
 */
public interface CsvReader {

    MutableCsvTable read(CsvSource source) throws IOException;
}
