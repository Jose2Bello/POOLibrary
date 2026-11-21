package com.equipo.csv.core;

import com.equipo.csv.api.CsvParser;
import com.equipo.csv.api.CsvReader;
import com.equipo.csv.api.CsvSource;
import com.equipo.csv.api.MutableCsvTable;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Lector CSV que delega el parseo en un {@link CsvParser}.
 *
 * <p>Interpreta la primera fila como encabezado cuando se configura así. Si el
 * CSV no trae encabezado, genera nombres como {@code "Columna 1"}. Las filas con
 * menos columnas que el máximo se rellenan con cadenas vacías para que la tabla
 * siempre sea rectangular.</p>
 *
 * <p>Un CSV vacío produce una tabla sin filas ni columnas. Un CSV de una sola
 * fila con encabezado produce una tabla con columnas y sin filas de datos.</p>
 */
public class DefaultCsvReader implements CsvReader {

    private final CsvParser parser;
    private final boolean firstRowIsHeader;

    /**
     * Crea un lector que interpreta la primera fila como encabezado.
     *
     * @param parser parser encargado de separar el texto
     */
    public DefaultCsvReader(CsvParser parser) {
        this(parser, true);
    }

    /**
     * Crea un lector configurable.
     *
     * @param parser           parser encargado de separar el texto
     * @param firstRowIsHeader {@code true} si la primera fila contiene los
     *                         nombres de las columnas
     */
    public DefaultCsvReader(CsvParser parser, boolean firstRowIsHeader) {
        this.parser = Objects.requireNonNull(parser, "parser");
        this.firstRowIsHeader = firstRowIsHeader;
    }

    /**
     * {@inheritDoc}
     *
     * <p>El lector de la fuente se cierra al terminar.</p>
     */
    @Override
    public MutableCsvTable read(CsvSource source) throws IOException {
        Objects.requireNonNull(source, "source");
        try (Reader reader = source.open()) {
            return buildTable(parser.parse(reader));
        }
    }

    private MutableCsvTable buildTable(List<List<String>> parsed) {
        if (parsed.isEmpty()) {
            return new DefaultCsvTable(List.of(), List.of(), firstRowIsHeader);
        }

        int columns = parsed.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        List<String> headers;
        List<List<String>> data;

        if (firstRowIsHeader) {
            headers = pad(parsed.get(0), columns);
            data = parsed.subList(1, parsed.size()).stream()
                    .map(row -> pad(row, columns))
                    .toList();
        } else {
            headers = new ArrayList<>();
            for (int i = 0; i < columns; i++) {
                headers.add("Columna " + (i + 1));
            }
            data = parsed.stream()
                    .map(row -> pad(row, columns))
                    .toList();
        }

        return new DefaultCsvTable(headers, data, firstRowIsHeader);
    }

    private List<String> pad(List<String> row, int columns) {
        List<String> result = new ArrayList<>(row);
        while (result.size() < columns) {
            result.add("");
        }
        return result;
    }
}
