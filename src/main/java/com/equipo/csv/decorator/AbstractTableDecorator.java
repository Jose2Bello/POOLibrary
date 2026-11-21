package com.equipo.csv.decorator;

import com.equipo.csv.api.CsvTable;

import java.util.List;
import java.util.Objects;

/**
 * Base para decoradores de solo lectura sobre una tabla CSV.
 *
 * <p>Delega todas las operaciones en la tabla envuelta. Las subclases solo
 * sobrescriben lo que necesitan transformar, de modo que se pueden combinar
 * varios decoradores sin modificar la tabla original.</p>
 */
public abstract class AbstractTableDecorator implements CsvTable {

    /** Tabla envuelta por el decorador. */
    protected final CsvTable delegate;

    /**
     * Crea un decorador sobre otra tabla.
     *
     * @param delegate tabla envuelta
     * @throws NullPointerException si la tabla es {@code null}
     */
    protected AbstractTableDecorator(CsvTable delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public int rowCount() {
        return delegate.rowCount();
    }

    @Override
    public int columnCount() {
        return delegate.columnCount();
    }

    @Override
    public boolean hasHeaders() {
        return delegate.hasHeaders();
    }

    @Override
    public List<String> headers() {
        return delegate.headers();
    }

    @Override
    public String cell(int row, int column) {
        return delegate.cell(row, column);
    }

    @Override
    public List<String> row(int row) {
        return delegate.row(row);
    }
}
