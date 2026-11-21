package com.equipo.csv.core;

import com.equipo.csv.exception.CsvLibraryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FileTablePrinter")
class FileTablePrinterTest {

    @Test
    @DisplayName("escribe el texto en un archivo nuevo")
    void escribeArchivoNuevo(@TempDir Path directorio) throws Exception {
        Path archivo = directorio.resolve("salida.txt");
        FileTablePrinter printer = new FileTablePrinter(archivo);

        printer.print("primera linea");

        assertEquals(
                "primera linea" + System.lineSeparator(),
                Files.readString(archivo));
    }

    @Test
    @DisplayName("agrega el texto sin borrar lo anterior")
    void agregaContenido(@TempDir Path directorio) throws Exception {
        Path archivo = directorio.resolve("salida.txt");
        FileTablePrinter printer = new FileTablePrinter(archivo);

        printer.print("uno");
        printer.print("dos");

        String esperado = String.join(System.lineSeparator(), "uno", "dos") + System.lineSeparator();
        assertEquals(esperado, Files.readString(archivo));
    }

    @Test
    @DisplayName("respeta el charset configurado")
    void respetaCharset(@TempDir Path directorio) throws Exception {
        Path archivo = directorio.resolve("salida.txt");
        FileTablePrinter printer = new FileTablePrinter(archivo, StandardCharsets.ISO_8859_1);

        printer.print("a\u00f1o");

        assertEquals("a\u00f1o" + System.lineSeparator(),
                Files.readString(archivo, StandardCharsets.ISO_8859_1));
    }

    @Test
    @DisplayName("expone la ruta y el charset")
    void exponeConfiguracion(@TempDir Path directorio) {
        Path archivo = directorio.resolve("salida.txt");
        FileTablePrinter printer = new FileTablePrinter(archivo);

        assertEquals(archivo, printer.path());
        assertEquals(StandardCharsets.UTF_8, printer.charset());
    }

    @Test
    @DisplayName("envuelve el error de escritura en CsvLibraryException")
    void envuelveErrorDeEscritura(@TempDir Path directorio) {
        Path archivo = directorio.resolve("carpeta-inexistente").resolve("salida.txt");
        FileTablePrinter printer = new FileTablePrinter(archivo);

        CsvLibraryException error = assertThrows(
                CsvLibraryException.class,
                () -> printer.print("texto"));

        assertTrue(error.getMessage().contains("carpeta-inexistente"));
    }
}
