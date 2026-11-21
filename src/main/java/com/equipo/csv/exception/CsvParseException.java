package com.equipo.csv.exception;

import java.io.IOException;

/**
 * Error de sintaxis en un archivo CSV.
 *
 * <p>Hereda de {@link IOException} para integrarse con el resto de errores de
 * lectura del lector.</p>
 */
public class CsvParseException extends IOException {

    /**
     * Crea la excepción con el detalle del problema.
     *
     * @param message descripción del error de sintaxis
     */
    public CsvParseException(String message) {
        super(message);
    }
}
