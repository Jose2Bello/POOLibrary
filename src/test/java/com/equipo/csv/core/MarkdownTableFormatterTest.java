package com.equipo.csv.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MarkdownTableFormatter")
class MarkdownTableFormatterTest {

    private final MarkdownTableFormatter formatter = new MarkdownTableFormatter();

    @Test
    @DisplayName("genera encabezado, separador y filas")
    void generaTablaMarkdown() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("nombre", "edad"),
                List.of(List.of("Ana", "30"), List.of("Luis", "25")));

        String esperado = String.join(System.lineSeparator(),
                "| nombre | edad |",
                "| ------ | ---- |",
                "| Ana    | 30   |",
                "| Luis   | 25   |");

        assertEquals(esperado, formatter.format(tabla));
    }

    @Test
    @DisplayName("escapa el caracter pipe dentro de una celda")
    void escapaPipes() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("col"),
                List.of(List.of("a|b")));

        String esperado = String.join(System.lineSeparator(),
                "| col  |",
                "| ---- |",
                "| a\\|b |");

        assertEquals(esperado, formatter.format(tabla));
    }

    @Test
    @DisplayName("convierte saltos de linea en espacios")
    void convierteSaltosDeLinea() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("col"),
                List.of(List.of("a\nb")));

        String esperado = String.join(System.lineSeparator(),
                "| col |",
                "| --- |",
                "| a b |");

        assertEquals(esperado, formatter.format(tabla));
    }

    @Test
    @DisplayName("usa un ancho minimo de tres guiones")
    void usaAnchoMinimo() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("a"),
                List.of(List.of("1")));

        String esperado = String.join(System.lineSeparator(),
                "| a   |",
                "| --- |",
                "| 1   |");

        assertEquals(esperado, formatter.format(tabla));
    }

    @Test
    @DisplayName("incluye la fila de encabezado aunque la tabla no la marque")
    void incluyeEncabezadoSiempre() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("Columna 1"),
                List.of(List.of("Ana")),
                false);

        String esperado = String.join(System.lineSeparator(),
                "| Columna 1 |",
                "| --------- |",
                "| Ana       |");

        assertEquals(esperado, formatter.format(tabla));
    }

    @Test
    @DisplayName("avisa cuando la tabla no tiene columnas")
    void tablaSinColumnas() {
        DefaultCsvTable tabla = new DefaultCsvTable(List.of(), List.of());

        assertEquals("(tabla vacia)", formatter.format(tabla));
    }
}
