package com.equipo.csv.core;

import com.equipo.csv.api.TablePrinter;

import java.util.Objects;

/**
 * Impresor que guarda el texto en memoria.
 *
 * <p>Es útil para pruebas y para encadenar formateadores sin depender de la
 * consola. Cada llamada a {@link #print(String)} añade el texto y un salto de
 * línea al contenido acumulado.</p>
 */
public class MemoryTablePrinter implements TablePrinter {

    private final StringBuilder content = new StringBuilder();
    private final String lineSeparator;

    public MemoryTablePrinter() {
        this(System.lineSeparator());
    }

    public MemoryTablePrinter(String lineSeparator) {
        this.lineSeparator = Objects.requireNonNull(lineSeparator, "lineSeparator");
    }

    @Override
    public void print(String text) {
        Objects.requireNonNull(text, "text");
        content.append(text).append(lineSeparator);
    }

    /**
     * Devuelve todo el texto acumulado.
     *
     * @return contenido impreso hasta el momento
     */
    public String text() {
        return content.toString();
    }

    /**
     * Indica si todavía no se imprimió nada.
     *
     * @return {@code true} si el contenido está vacío
     */
    public boolean isEmpty() {
        return content.isEmpty();
    }

    /**
     * Borra el contenido acumulado.
     */
    public void clear() {
        content.setLength(0);
    }
}
