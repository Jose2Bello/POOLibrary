package com.equipo.csv.core;

import com.equipo.csv.api.TablePrinter;

/**
 * Impresor de tablas en la consola.
 *
 * <p>Escribe el texto recibido en {@link System#out} seguido de un salto de
 * línea.</p>
 */
public class ConsoleTablePrinter implements TablePrinter {

    /**
     * Crea el impresor de consola.
     */
    public ConsoleTablePrinter() {
    }

    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
