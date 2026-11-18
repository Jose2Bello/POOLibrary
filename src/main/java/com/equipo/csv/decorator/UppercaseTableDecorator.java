package com.equipo.csv.decorator;

import com.equipo.csv.api.CsvTable;

import java.util.List;
import java.util.Locale;

/**
 * Convierte celdas y encabezados a mayúsculas.
 */
public class UppercaseTableDecorator extends AbstractTableDecorator {

    public UppercaseTableDecorator(CsvTable delegate) {
        super(delegate);
    }

    @Override
    public String cell(int row, int column) {
        return delegate.cell(row, column).toUpperCase(Locale.ROOT);
    }

    @Override
    public List<String> row(int row) {
        return delegate.row(row).stream()
                .map(value -> value.toUpperCase(Locale.ROOT))
                .toList();
    }

    @Override
    public List<String> headers() {
        return delegate.headers().stream()
                .map(value -> value.toUpperCase(Locale.ROOT))
                .toList();
    }
}
