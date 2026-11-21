package com.equipo.csv.exception;

/**
 * Error de lectura o configuración de la librería.
 *
 * <p>Es una excepción no comprobada: envuelve los problemas de entrada y salida
 * para que el cliente de la librería no tenga que capturarlos en cada llamada.</p>
 */
public class CsvLibraryException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje y la causa original.
     *
     * @param message descripción del problema
     * @param cause   excepción que originó el error
     */
    public CsvLibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
