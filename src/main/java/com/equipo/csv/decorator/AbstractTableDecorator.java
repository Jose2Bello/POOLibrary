package com.equipo.csv.decorator;

import com.equipo.csv.api.CsvTable;

import java.util.List;
import java.util.Objects;

/**
 * Base para decoradores de solo lectura sobre una tabla CSV.
 */
public abstract class AbstractTableDecorator implements CsvTable {

    protected final CsvTable delegate;

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
