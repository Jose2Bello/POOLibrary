package com.equipo.csv.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("PlainTableFormatter")
class PlainTableFormatterTest {

    private final PlainTableFormatter formatter = new PlainTableFormatter();

    @Test
    @DisplayName("dibuja encabezado, filas y bordes")
    void dibujaTablaCompleta() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("nombre", "edad"),
                List.of(List.of("Ana", "30"), List.of("Luis", "25")));

        String esperado = String.join(System.lineSeparator(),
                "+--------+------+",
                "| nombre | edad |",
                "+--------+------+",
                "| Ana    | 30   |",
                "| Luis   | 25   |",
                "+--------+------+");

        assertEquals(esperado, formatter.format(tabla));
    }

    @Test
    @DisplayName("omite la fila de encabezado cuando la tabla no la tiene")
    void omiteEncabezado() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("Columna 1", "Columna 2"),
                List.of(List.of("Ana", "30")),
                false);

        String esperado = String.join(System.lineSeparator(),
                "+-----+----+",
                "| Ana | 30 |",
                "+-----+----+");

        assertEquals(esperado, formatter.format(tabla));
    }

    @Test
    @DisplayName("avisa cuando la tabla no tiene columnas")
    void tablaSinColumnas() {
        DefaultCsvTable tabla = new DefaultCsvTable(List.of(), List.of());

        assertEquals("(tabla vacia)", formatter.format(tabla));
    }

    @Test
    @DisplayName("ajusta el ancho a la celda mas larga")
    void ajustaAnchoALaCeldaMasLarga() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("id"),
                List.of(List.of("1"), List.of("12345")));

        String esperado = String.join(System.lineSeparator(),
                "+-------+",
                "| id    |",
                "+-------+",
                "| 1     |",
                "| 12345 |",
                "+-------+");

        assertEquals(esperado, formatter.format(tabla));
    }
}
