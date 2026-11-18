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
 */
public class DefaultCsvReader implements CsvReader {

    private final CsvParser parser;
    private final boolean firstRowIsHeader;

    public DefaultCsvReader(CsvParser parser) {
        this(parser, true);
    }

    public DefaultCsvReader(CsvParser parser, boolean firstRowIsHeader) {
        this.parser = Objects.requireNonNull(parser, "parser");
        this.firstRowIsHeader = firstRowIsHeader;
    }

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
