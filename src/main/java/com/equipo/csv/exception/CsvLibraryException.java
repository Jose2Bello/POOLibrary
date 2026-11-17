package com.equipo.csv.exception;

/**
 * Error de lectura o configuración de la librería.
 */
public class CsvLibraryException extends RuntimeException {

    public CsvLibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
