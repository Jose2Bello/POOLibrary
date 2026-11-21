package com.equipo.csv.source;

import com.equipo.csv.api.CsvSource;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Origen CSV basado en un archivo.
 *
 * <p>Adapta un {@link Path} al contrato {@link CsvSource}. Abre el archivo en
 * UTF-8 y delega el cierre en el lector que lo consume.</p>
 *
 * @param path ruta del archivo CSV
 */
public record PathCsvSource(Path path) implements CsvSource {

    /**
     * Valida la ruta recibida.
     *
     * @throws NullPointerException si la ruta es {@code null}
     */
    public PathCsvSource {
        Objects.requireNonNull(path, "path");
    }

    @Override
    public Reader open() throws IOException {
        return Files.newBufferedReader(path, StandardCharsets.UTF_8);
    }

    @Override
    public String description() {
        return path.toString();
    }
}
