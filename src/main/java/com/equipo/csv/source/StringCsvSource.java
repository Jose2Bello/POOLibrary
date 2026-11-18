package com.equipo.csv.source;

import com.equipo.csv.api.CsvSource;

import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

/**
 * Origen CSV basado en un texto en memoria.
 */
public record StringCsvSource(String csv) implements CsvSource {

    public StringCsvSource {
        Objects.requireNonNull(csv, "csv");
    }

    @Override
    public Reader open() {
        return new StringReader(csv);
    }

    @Override
    public String description() {
        return "texto en memoria";
    }
}
