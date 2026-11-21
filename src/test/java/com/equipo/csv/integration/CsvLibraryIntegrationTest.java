package com.equipo.csv.integration;

import com.equipo.csv.core.CsvDocument;
import com.equipo.csv.core.CsvLibrary;
import com.equipo.csv.core.SimpleCsvParser;
import com.equipo.csv.decorator.FilteredTableDecorator;
import com.equipo.csv.decorator.TrimmedTableDecorator;
import com.equipo.csv.decorator.UppercaseTableDecorator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Integracion de la libreria")
class CsvLibraryIntegrationTest {

    private Path recurso(String nombre) throws Exception {
        return Path.of(Objects.requireNonNull(
                getClass().getResource("/" + nombre)).toURI());
    }

    @Test
    @DisplayName("CsvLibrary.open lee un archivo del classpath")
    void abreArchivo() throws Exception {
        CsvDocument documento = CsvLibrary.open(recurso("personas.csv"));

        assertEquals(List.of("nombre", "edad", "ciudad"), documento.view().headers());
        assertEquals(2, documento.view().rowCount());
        assertEquals("Ana", documento.view().cell(0, 0));
        assertEquals("Bogota", documento.view().cell(1, 2));
    }

    @Test
    @DisplayName("CsvLibrary.parse lee un CSV en memoria")
    void parseaTexto() {
        CsvDocument documento = CsvLibrary.parse("a,b\n1,2");

        assertEquals(1, documento.view().rowCount());
        assertEquals("1", documento.view().cell(0, 0));
    }

    @Test
    @DisplayName("flujo completo: recortar, filtrar y convertir a mayusculas")
    void flujoCompleto() {
        CsvDocument documento = CsvDocument.builder()
                .fromString("""
                        nombre , ciudad
                        ana , Madrid
                        luis , Bogota
                        """)
                .decorate(TrimmedTableDecorator::new)
                .decorate(tabla -> new FilteredTableDecorator(
                        tabla,
                        fila -> fila.get(1).equals("Madrid")))
                .decorate(UppercaseTableDecorator::new)
                .build();

        String esperado = String.join(System.lineSeparator(),
                "+--------+--------+",
                "| NOMBRE | CIUDAD |",
                "+--------+--------+",
                "| ANA    | MADRID |",
                "+--------+--------+");

        assertEquals(esperado, documento.toText());
    }

    @Test
    @DisplayName("un parser con otro delimitador se integra sin tocar el nucleo")
    void parserConOtroDelimitador() {
        CsvDocument documento = CsvDocument.builder()
                .fromString("a;b\n1;2")
                .parser(new SimpleCsvParser(';'))
                .build();

        assertEquals(List.of("a", "b"), documento.view().headers());
        assertEquals("2", documento.view().cell(0, 1));
    }

    @Test
    @DisplayName("las ediciones se reflejan en la salida final")
    void edicionesSeReflejanEnLaSalida() {
        CsvDocument documento = CsvDocument.builder()
                .fromString("a,b\n1,2")
                .build();

        documento.addRow(List.of("3", "4"));
        documento.removeColumn(0);

        String esperado = String.join(System.lineSeparator(),
                "+---+",
                "| b |",
                "+---+",
                "| 2 |",
                "| 4 |",
                "+---+");

        assertEquals(esperado, documento.toText());
    }
}
