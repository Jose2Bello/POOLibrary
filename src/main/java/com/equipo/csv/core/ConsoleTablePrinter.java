package com.equipo.csv.core;

import com.equipo.csv.api.TablePrinter;

/**
 * Impresor de tablas en la consola.
 */
public class ConsoleTablePrinter implements TablePrinter {

    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
