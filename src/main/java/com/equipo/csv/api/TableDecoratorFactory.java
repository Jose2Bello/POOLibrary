package com.equipo.csv.api;

/**
 * Crea un decorador de tabla.
 */
@FunctionalInterface
public interface TableDecoratorFactory {

    CsvTable decorate(CsvTable table);
}
