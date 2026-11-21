package com.equipo.csv.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MemoryTablePrinter")
class MemoryTablePrinterTest {

    @Test
    @DisplayName("empieza vacio")
    void empiezaVacio() {
        MemoryTablePrinter printer = new MemoryTablePrinter();

        assertTrue(printer.isEmpty());
        assertEquals("", printer.text());
    }

    @Test
    @DisplayName("acumula el texto con saltos de linea")
    void acumulaTexto() {
        MemoryTablePrinter printer = new MemoryTablePrinter();

        printer.print("uno");
        printer.print("dos");

        String esperado = String.join(System.lineSeparator(), "uno", "dos")
                + System.lineSeparator();
        assertEquals(esperado, printer.text());
        assertFalse(printer.isEmpty());
    }

    @Test
    @DisplayName("permite elegir el separador de linea")
    void permiteElegirSeparador() {
        MemoryTablePrinter printer = new MemoryTablePrinter("\n");

        printer.print("uno");
        printer.print("dos");

        assertEquals("uno\ndos\n", printer.text());
    }

    @Test
    @DisplayName("se puede vaciar")
    void sePuedeVaciar() {
        MemoryTablePrinter printer = new MemoryTablePrinter();
        printer.print("algo");

        printer.clear();

        assertTrue(printer.isEmpty());
    }

    @Test
    @DisplayName("se integra con un formateador")
    void seIntegraConFormatter() {
        MemoryTablePrinter printer = new MemoryTablePrinter();
        CsvDocument documento = CsvDocument.builder()
                .fromString("a\n1")
                .printer(printer)
                .build();

        documento.printTable();

        String esperado = String.join(System.lineSeparator(),
                "+---+",
                "| a |",
                "+---+",
                "| 1 |",
                "+---+",
                "");
        assertEquals(esperado, printer.text());
    }
}
