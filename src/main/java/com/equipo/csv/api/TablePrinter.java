package com.equipo.csv.api;

/**
 * Destino del texto generado por un formateador.
 *
 * <p>Permite enviar la salida a consola, a un archivo o a memoria sin que el
 * documento conozca el destino concreto.</p>
 */
public interface TablePrinter {

    /**
     * Envía el texto al destino configurado.
     *
     * @param text texto ya formateado
     */
    void print(String text);
}
