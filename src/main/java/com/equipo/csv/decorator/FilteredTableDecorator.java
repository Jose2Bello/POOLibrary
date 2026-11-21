package com.equipo.csv.decorator;

import com.equipo.csv.api.CsvTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Oculta las filas que no cumplen un predicado.
 *
 * <p>El predicado se evalúa una sola vez, al construir el decorador, sobre cada
 * fila de la tabla envuelta. Después, {@link #rowCount()}, {@link #row(int)} y
 * {@link #cell(int, int)} trabajan sobre las filas visibles.</p>
 */
public class FilteredTableDecorator extends AbstractTableDecorator {

    private final List<Integer> visibleRows;

    /**
     * Crea el decorador y calcula las filas visibles.
     *
     * @param delegate  tabla original
     * @param predicate condición que deben cumplir las filas para mostrarse
     * @throws NullPointerException si el predicado es {@code null}
     */
    public FilteredTableDecorator(CsvTable delegate, Predicate<List<String>> predicate) {
        super(delegate);
        Objects.requireNonNull(predicate, "predicate");

        this.visibleRows = new ArrayList<>();
        for (int row = 0; row < delegate.rowCount(); row++) {
            if (predicate.test(delegate.row(row))) {
                visibleRows.add(row);
            }
        }
    }

    @Override
    public int rowCount() {
        return visibleRows.size();
    }

    @Override
    public String cell(int row, int column) {
        return delegate.cell(visibleRows.get(row), column);
    }

    @Override
    public List<String> row(int row) {
        return delegate.row(visibleRows.get(row));
    }
}
