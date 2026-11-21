package com.equipo.csv.core;

import com.equipo.csv.api.CsvSource;
import com.equipo.csv.api.MutableCsvTable;
import com.equipo.csv.source.StringCsvSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultCsvReader")
class DefaultCsvReaderTest {

    private MutableCsvTable read(String csv, boolean firstRowIsHeader) throws IOException {
        return new DefaultCsvReader(new SimpleCsvParser(), firstRowIsHeader)
                .read(new StringCsvSource(csv));
    }

    @Test
    @DisplayName("toma la primera fila como encabezado")
    void tomaPrimeraFilaComoEncabezado() throws Exception {
        MutableCsvTable tabla = read("nombre,edad\nAna,30", true);

        assertTrue(tabla.hasHeaders());
        assertEquals(List.of("nombre", "edad"), tabla.headers());
        assertEquals(1, tabla.rowCount());
        assertEquals(2, tabla.columnCount());
        assertEquals("Ana", tabla.cell(0, 0));
        assertEquals("30", tabla.cell(0, 1));
    }

    @Test
    @DisplayName("genera encabezados cuando no hay fila de encabezado")
    void generaEncabezados() throws Exception {
        MutableCsvTable tabla = read("Ana,30\nLuis,25", false);

        assertFalse(tabla.hasHeaders());
        assertEquals(List.of("Columna 1", "Columna 2"), tabla.headers());
        assertEquals(2, tabla.rowCount());
        assertEquals("Ana", tabla.cell(0, 0));
    }

    @Test
    @DisplayName("rellena las filas con menos columnas")
    void rellenaFilasDesiguales() throws Exception {
        MutableCsvTable tabla = read("a,b,c\n1", true);

        assertEquals(3, tabla.columnCount());
        assertEquals(List.of("1", "", ""), tabla.row(0));
    }

    @Test
    @DisplayName("devuelve una tabla vacia para un CSV vacio")
    void devuelveTablaVacia() throws Exception {
        MutableCsvTable tabla = read("", true);

        assertEquals(0, tabla.rowCount());
        assertEquals(0, tabla.columnCount());
    }

    @Test
    @DisplayName("deja la tabla sin filas cuando solo hay encabezado")
    void soloEncabezado() throws Exception {
        MutableCsvTable tabla = read("a,b", true);

        assertEquals(2, tabla.columnCount());
        assertEquals(0, tabla.rowCount());
    }

    @Test
    @DisplayName("propaga el error de la fuente")
    void propagaErrorDeLaFuente() {
        CsvSource fuenteRota = () -> {
            throw new IOException("sin acceso");
        };

        IOException error = assertThrows(
                IOException.class,
                () -> new DefaultCsvReader(new SimpleCsvParser()).read(fuenteRota));

        assertEquals("sin acceso", error.getMessage());
    }
}
