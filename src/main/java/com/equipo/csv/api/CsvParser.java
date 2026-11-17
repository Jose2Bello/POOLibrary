package com.equipo.csv.api;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Convierte texto CSV en filas.
 */
public interface CsvParser {

    List<List<String>> parse(Reader reader) throws IOException;
}
