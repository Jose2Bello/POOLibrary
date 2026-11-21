package com.equipo.csv.decorator;

import com.equipo.csv.api.CsvTable;

import java.util.List;
import java.util.Locale;

/**
 * Convierte celdas y encabezados a mayúsculas.
 *
 * <p>Usa {@link Locale#ROOT} para que el resultado no dependa del idioma del
 * sistema. No modifica la tabla envuelta.</p>
 */
public class UppercaseTableDecorator extends AbstractTableDecorator {

    /**
     * Crea el decorador.
     *
     * @param delegate tabla a convertir
     */
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
