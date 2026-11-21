package com.equipo.csv.source;

import com.equipo.csv.api.CsvSource;

import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

/**
 * Origen CSV basado en un texto en memoria.
 *
 * <p>Adapta un {@link String} al contrato {@link CsvSource}, útil para pruebas y
 * para datos que ya están cargados.</p>
 *
 * @param csv contenido CSV
 */
public record StringCsvSource(String csv) implements CsvSource {

    /**
     * Valida el texto recibido.
     *
     * @throws NullPointerException si el texto es {@code null}
     */
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
