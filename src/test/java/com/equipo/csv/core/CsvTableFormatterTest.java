package com.equipo.csv.core;

import com.equipo.csv.api.CsvTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CsvTableFormatter")
class CsvTableFormatterTest {

    private final CsvTableFormatter formatter = new CsvTableFormatter(',', "\n");

    private DefaultCsvTable tabla() {
        return new DefaultCsvTable(
                List.of("nombre", "edad"),
                List.of(List.of("Ana", "30"), List.of("Luis", "25")));
    }

    @Test
    @DisplayName("exporta encabezado y filas separadas por comas")
    void exportaTablaCompleta() {
        assertEquals("nombre,edad\nAna,30\nLuis,25", formatter.format(tabla()));
    }

    @Test
    @DisplayName("respeta un delimitador propio")
    void respetaDelimitador() {
        CsvTableFormatter puntoYComa = new CsvTableFormatter(';', "\n");

        assertEquals("nombre;edad\nAna;30\nLuis;25", puntoYComa.format(tabla()));
    }

    @Test
    @DisplayName("entrecomilla valores con el delimitador")
    void entrecomillaValoresConDelimitador() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("nombre"),
                List.of(List.of("Bello, Jose")));

        assertEquals("nombre\n\"Bello, Jose\"", formatter.format(tabla));
    }

    @Test
    @DisplayName("duplica las comillas internas")
    void duplicaComillasInternas() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("frase"),
                List.of(List.of("dijo \"hola\"")));

        assertEquals("frase\n\"dijo \"\"hola\"\"\"", formatter.format(tabla));
    }

    @Test
    @DisplayName("entrecomilla valores con saltos de linea")
    void entrecomillaSaltosDeLinea() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("texto"),
                List.of(List.of("linea1\nlinea2")));

        assertEquals("texto\n\"linea1\nlinea2\"", formatter.format(tabla));
    }

    @Test
    @DisplayName("omite el encabezado cuando la tabla no lo declara")
    void omiteEncabezado() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("Columna 1", "Columna 2"),
                List.of(List.of("Ana", "30")),
                false);

        assertEquals("Ana,30", formatter.format(tabla));
    }

    @Test
    @DisplayName("devuelve texto vacio para una tabla sin columnas")
    void tablaVacia() {
        DefaultCsvTable tabla = new DefaultCsvTable(List.of(), List.of());

        assertEquals("", formatter.format(tabla));
    }

    @Test
    @DisplayName("solo exporta el encabezado cuando no hay filas")
    void soloEncabezado() {
        DefaultCsvTable tabla = new DefaultCsvTable(List.of("a", "b"), List.of());

        assertEquals("a,b", formatter.format(tabla));
    }

    @Test
    @DisplayName("el texto exportado se puede volver a leer")
    void idaYVuelta() throws Exception {
        String texto = formatter.format(tabla());

        CsvTable recuperada = new DefaultCsvReader(new SimpleCsvParser())
                .read(new com.equipo.csv.source.StringCsvSource(texto));

        assertEquals(tabla().headers(), recuperada.headers());
        assertEquals(2, recuperada.rowCount());
        assertEquals("Ana", recuperada.cell(0, 0));
        assertEquals("25", recuperada.cell(1, 1));
    }
}
