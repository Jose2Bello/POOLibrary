package com.equipo.csv.api;

import java.io.IOException;
import java.io.Reader;

/**
 * Origen de datos CSV.
 */
public interface CsvSource {

    Reader open() throws IOException;

    default String description() {
        return getClass().getSimpleName();
    }
}
