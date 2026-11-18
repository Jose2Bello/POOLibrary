package com.equipo.csv.core;

import java.nio.file.Path;

/**
 * Fachada de conveniencia para abrir o parsear documentos CSV.
 */
public final class CsvLibrary {

    private CsvLibrary() {
    }

    public static CsvDocument open(Path path) {
        return CsvDocument.builder().from(path).build();
    }

    public static CsvDocument parse(String csv) {
        return CsvDocument.builder().fromString(csv).build();
    }
}
