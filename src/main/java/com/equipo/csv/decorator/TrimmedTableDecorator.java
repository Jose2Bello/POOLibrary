package com.equipo.csv.decorator;

import com.equipo.csv.api.CsvTable;

import java.util.List;

/**
 * Elimina espacios al inicio y al final de celdas y encabezados.
 *
 * <p>No modifica la tabla envuelta: solo transforma los valores al leerlos.</p>
 */
public class TrimmedTableDecorator extends AbstractTableDecorator {

    /**
     * Crea el decorador.
     *
     * @param delegate tabla a recortar
     */
    public TrimmedTableDecorator(CsvTable delegate) {
        super(delegate);
    }

    @Override
    public String cell(int row, int column) {
        return delegate.cell(row, column).trim();
    }

    @Override
    public List<String> row(int row) {
        return delegate.row(row).stream()
                .map(String::trim)
                .toList();
    }

    @Override
    public List<String> headers() {
        return delegate.headers().stream()
                .map(String::trim)
                .toList();
    }
}
