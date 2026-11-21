package com.equipo.csv.api;

import java.io.IOException;
import java.io.Reader;

/**
 * Origen de datos CSV.
 *
 * <p>Abstrae de dónde vienen los datos: un archivo, un texto en memoria o
 * cualquier otra fuente. El lector solo depende de esta interfaz, no del tipo
 * concreto.</p>
 */
public interface CsvSource {

    /**
     * Abre el origen y devuelve un lector de caracteres.
     *
     * <p>El llamador es responsable de cerrar el {@link Reader} devuelto.</p>
     *
     * @return lector sobre el contenido CSV
     * @throws IOException si el origen no se puede abrir
     */
    Reader open() throws IOException;

    /**
     * Devuelve una descripción legible del origen, útil en mensajes de error.
     *
     * @return descripción del origen
     */
    default String description() {
        return getClass().getSimpleName();
    }
}
