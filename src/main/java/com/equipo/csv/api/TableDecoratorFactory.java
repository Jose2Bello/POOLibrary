package com.equipo.csv.api;

/**
 * Crea un decorador de tabla.
 *
 * <p>Permite guardar la receta de decoración y aplicarla más tarde sobre la
 * vista actual del documento. Es una interfaz funcional, así que se puede usar
 * con referencias a constructores o expresiones lambda.</p>
 */
@FunctionalInterface
public interface TableDecoratorFactory {

    /**
     * Envuelve la tabla con el decorador correspondiente.
     *
     * @param table tabla original
     * @return vista decorada
     */
    CsvTable decorate(CsvTable table);
}
