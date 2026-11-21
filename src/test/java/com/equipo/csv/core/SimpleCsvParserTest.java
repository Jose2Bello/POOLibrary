package com.equipo.csv.core;

import com.equipo.csv.exception.CsvParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SimpleCsvParser")
class SimpleCsvParserTest {

    private final SimpleCsvParser parser = new SimpleCsvParser();

    private List<List<String>> parse(String csv) throws Exception {
        return parser.parse(new StringReader(csv));
    }

    @Test
    @DisplayName("separa filas y columnas con comas")
    void separaFilasYColumnas() throws Exception {
        assertEquals(
                List.of(List.of("a", "b", "c"), List.of("1", "2", "3")),
                parse("a,b,c\n1,2,3"));
    }

    @Test
    @DisplayName("acepta comas dentro de comillas")
    void aceptaComasDentroDeComillas() throws Exception {
        assertEquals(List.of(List.of("Bello, Jose", "30")), parse("\"Bello, Jose\",30"));
    }

    @Test
    @DisplayName("interpreta comillas escapadas")
    void interpretaComillasEscapadas() throws Exception {
        assertEquals(List.of(List.of("dijo \"hola\"", "x")), parse("\"dijo \"\"hola\"\"\",x"));
    }

    @Test
    @DisplayName("conserva saltos de linea dentro de comillas")
    void conservaSaltoDeLineaDentroDeComillas() throws Exception {
        assertEquals(List.of(List.of("linea1\nlinea2", "b")), parse("\"linea1\nlinea2\",b"));
    }

    @Test
    @DisplayName("ignora el BOM al inicio")
    void ignoraBom() throws Exception {
        assertEquals(List.of(List.of("a", "b")), parse("\uFEFFa,b"));
    }

    @Test
    @DisplayName("falla con comilla sin cerrar")
    void fallaConComillaSinCerrar() {
        CsvParseException error = assertThrows(
                CsvParseException.class,
                () -> parser.parse(new StringReader("\"a,b")));

        assertTrue(error.getMessage().contains("Comilla sin cerrar"));
    }

    @Test
    @DisplayName("usa otro delimitador cuando se configura")
    void usaOtroDelimitador() throws Exception {
        SimpleCsvParser puntoYComa = new SimpleCsvParser(';');

        assertEquals(
                List.of(List.of("a", "b"), List.of("1", "2")),
                puntoYComa.parse(new StringReader("a;b\n1;2")));
    }

    @Test
    @DisplayName("devuelve una lista vacia para un CSV vacio")
    void devuelveListaVaciaParaCsvVacio() throws Exception {
        assertTrue(parse("").isEmpty());
    }

    @Test
    @DisplayName("ignora lineas vacias intermedias")
    void ignoraLineasVacias() throws Exception {
        assertEquals(List.of(List.of("a"), List.of("b")), parse("a\n\nb"));
    }

    @Test
    @DisplayName("conserva un campo vacio al final")
    void conservaCampoVacioFinal() throws Exception {
        assertEquals(List.of(List.of("a", "")), parse("a,"));
    }

    @Test
    @DisplayName("soporta finales de linea CRLF")
    void soportaCrLf() throws Exception {
        assertEquals(List.of(List.of("a"), List.of("b")), parse("a\r\nb"));
    }

    @Test
    @DisplayName("procesa la ultima fila sin salto de linea")
    void procesaUltimaFilaSinSalto() throws Exception {
        assertEquals(List.of(List.of("a", "b")), parse("a,b"));
    }
}
