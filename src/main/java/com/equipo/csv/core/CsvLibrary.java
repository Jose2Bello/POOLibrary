package com.equipo.csv.core;

import java.nio.file.Path;

/**
 * Fachada de conveniencia para abrir o parsear documentos CSV.
 *
 * <p>Oculta la configuración por defecto del {@link CsvDocumentBuilder} para los
 * casos más comunes. Cuando se necesita otra configuración, se usa el builder
 * directamente.</p>
 */
public final class CsvLibrary {

    private CsvLibrary() {
    }

    /**
     * Abre un archivo CSV con la configuración por defecto.
     *
     * @param path ruta del archivo
     * @return documento construido desde el archivo
     * @throws com.equipo.csv.exception.CsvLibraryException si falla la lectura
     */
    public static CsvDocument open(Path path) {
        return CsvDocument.builder().from(path).build();
    }

    /**
     * Interpreta un CSV recibido como texto.
     *
     * @param csv contenido CSV
     * @return documento construido desde el texto
     */
    public static CsvDocument parse(String csv) {
        return CsvDocument.builder().fromString(csv).build();
    }
}
