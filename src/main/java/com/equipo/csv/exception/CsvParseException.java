package com.equipo.csv.exception;

import java.io.IOException;

/**
 * Error de sintaxis en un archivo CSV.
 */
public class CsvParseException extends IOException {

    public CsvParseException(String message) {
        super(message);
    }
}
