package com.equipo.csv.core;

import com.equipo.csv.api.TablePrinter;
import com.equipo.csv.exception.CsvLibraryException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Impresor que agrega el texto a un archivo.
 *
 * <p>Cada llamada a {@link #print(String)} añade el texto y un salto de línea al
 * final del archivo, creándolo si no existe. El contenido previo no se borra.</p>
 */
public class FileTablePrinter implements TablePrinter {

    private final Path path;
    private final Charset charset;

    /**
     * Crea el impresor escribiendo en UTF-8.
     *
     * @param path archivo de destino
     */
    public FileTablePrinter(Path path) {
        this(path, StandardCharsets.UTF_8);
    }

    /**
     * Crea el impresor con un charset propio.
     *
     * @param path    archivo de destino
     * @param charset codificación de escritura
     */
    public FileTablePrinter(Path path, Charset charset) {
        this.path = Objects.requireNonNull(path, "path");
        this.charset = Objects.requireNonNull(charset, "charset");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void print(String text) {
        Objects.requireNonNull(text, "text");
        try {
            Files.writeString(
                    path,
                    text + System.lineSeparator(),
                    charset,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException exception) {
            throw new CsvLibraryException(
                    "No se pudo escribir en el archivo: " + path, exception);
        }
    }

    /**
     * Devuelve el archivo de destino.
     *
     * @return ruta configurada
     */
    public Path path() {
        return path;
    }

    /**
     * Devuelve el charset usado al escribir.
     *
     * @return charset configurado
     */
    public Charset charset() {
        return charset;
    }
}
