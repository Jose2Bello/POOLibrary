package com.equipo.csv.core;

import com.equipo.csv.api.CsvReader;
import com.equipo.csv.api.CsvSource;
import com.equipo.csv.api.TableFormatter;
import com.equipo.csv.api.TablePrinter;
import com.equipo.csv.exception.CsvLibraryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CsvDocumentBuilder")
class CsvDocumentBuilderTest {

    @Test
    @DisplayName("construye un documento desde texto")
    void construyeDesdeTexto() {
        CsvDocument documento = CsvDocument.builder()
                .fromString("a,b\n1,2")
                .build();

        assertEquals(1, documento.view().rowCount());
        assertEquals("1", documento.view().cell(0, 0));
    }

    @Test
    @DisplayName("construye un documento desde un archivo")
    void construyeDesdeArchivo(@TempDir Path directorio) throws IOException {
        Path archivo = directorio.resolve("datos.csv");
        Files.writeString(archivo, "a,b\n1,2");

        CsvDocument documento = CsvDocument.builder().from(archivo).build();

        assertEquals(List.of("a", "b"), documento.view().headers());
        assertEquals("2", documento.view().cell(0, 1));
    }

    @Test
    @DisplayName("construye un documento desde una tabla en memoria")
    void construyeDesdeTabla() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("a"),
                List.of(List.of("1")));

        CsvDocument documento = CsvDocument.builder().fromTable(tabla).build();

        assertEquals(1, documento.view().rowCount());
        assertEquals("1", documento.view().cell(0, 0));

        tabla.addRow(List.of("2"));
        assertEquals(2, documento.view().rowCount());
    }

    @Test
    @DisplayName("usa el lector inyectado en lugar del lector por defecto")
    void usaLectorInyectado() {
        CsvReader lector = source -> new DefaultCsvTable(
                List.of("x"),
                List.of(List.of("9")));

        CsvDocument documento = CsvDocument.builder()
                .fromString("ignorado")
                .reader(lector)
                .build();

        assertEquals(List.of("x"), documento.view().headers());
        assertEquals("9", documento.view().cell(0, 0));
    }

    @Test
    @DisplayName("aplica el formatter y el printer configurados")
    void aplicaFormatterYPrinter() {
        TableFormatter formatter = table -> "filas=" + table.rowCount();
        StringBuilder salida = new StringBuilder();
        TablePrinter printer = salida::append;

        CsvDocument documento = CsvDocument.builder()
                .fromString("a\n1\n2")
                .formatter(formatter)
                .printer(printer)
                .build();

        assertEquals("filas=2", documento.toText());

        documento.printTable();
        assertEquals("filas=2", salida.toString());
    }

    @Test
    @DisplayName("falla si no se indica origen")
    void fallaSinOrigen() {
        assertThrows(IllegalStateException.class, () -> CsvDocument.builder().build());
    }

    @Test
    @DisplayName("falla si se combinan fuente y tabla")
    void fallaConFuenteYTabla() {
        DefaultCsvTable tabla = new DefaultCsvTable(List.of("a"), List.of());

        assertThrows(
                IllegalStateException.class,
                () -> CsvDocument.builder().fromString("a\n1").fromTable(tabla).build());
    }

    @Test
    @DisplayName("envuelve los errores de lectura en CsvLibraryException")
    void envuelveErroresDeLectura() {
        CsvSource fuenteRota = () -> {
            throw new IOException("sin acceso");
        };

        CsvLibraryException error = assertThrows(
                CsvLibraryException.class,
                () -> CsvDocument.builder().source(fuenteRota).build());

        assertEquals("sin acceso", error.getCause().getMessage());
    }
}
