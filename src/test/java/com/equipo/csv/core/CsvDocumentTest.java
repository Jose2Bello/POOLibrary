package com.equipo.csv.core;

import com.equipo.csv.api.CsvTable;
import com.equipo.csv.api.TablePrinter;
import com.equipo.csv.decorator.TrimmedTableDecorator;
import com.equipo.csv.decorator.UppercaseTableDecorator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CsvDocument")
class CsvDocumentTest {

    private static final String CSV = """
            nombre,edad,ciudad
            Ana,30,Madrid
            Luis,25,Bogota
            """;

    private CsvDocument documento() {
        return CsvDocument.builder().fromString(CSV).build();
    }

    @Test
    @DisplayName("edita filas")
    void editaFilas() {
        CsvDocument documento = documento();

        documento.addRow(List.of("Marta", "28", "Lima"));
        assertEquals(3, documento.view().rowCount());

        documento.removeRow(0);
        assertEquals(2, documento.view().rowCount());
        assertEquals("Luis", documento.view().cell(0, 0));
    }

    @Test
    @DisplayName("edita columnas")
    void editaColumnas() {
        CsvDocument documento = documento();

        documento.addColumn("pais", List.of("Espana", "Colombia"));
        assertEquals(4, documento.view().columnCount());
        assertEquals("Espana", documento.view().cell(0, 3));

        documento.addColumn("notas");
        assertEquals(5, documento.view().columnCount());

        documento.removeColumn(1);
        assertEquals(4, documento.view().columnCount());
        assertEquals(List.of("nombre", "ciudad", "pais", "notas"), documento.view().headers());
    }

    @Test
    @DisplayName("modifica celdas")
    void modificaCeldas() {
        CsvDocument documento = documento();

        documento.setCell(1, 2, "Quito");

        assertEquals("Quito", documento.view().cell(1, 2));
    }

    @Test
    @DisplayName("aplica decoradores en el orden configurado")
    void aplicaDecoradoresEnOrden() {
        CsvDocument documento = CsvDocument.builder()
                .fromString(CSV)
                .decorate(TrimmedTableDecorator::new)
                .decorate(UppercaseTableDecorator::new)
                .build();

        assertEquals(List.of("NOMBRE", "EDAD", "CIUDAD"), documento.view().headers());
        assertEquals("ANA", documento.view().cell(0, 0));
    }

    @Test
    @DisplayName("la vista refleja los cambios posteriores")
    void vistaReflejaCambios() {
        CsvDocument documento = CsvDocument.builder()
                .fromString(CSV)
                .decorate(UppercaseTableDecorator::new)
                .build();

        documento.addRow(List.of("marta", "28", "lima"));

        assertEquals("MARTA", documento.view().cell(2, 0));
    }

    @Test
    @DisplayName("toText devuelve el texto formateado")
    void toTextDevuelveTexto() {
        CsvDocument documento = CsvDocument.builder()
                .fromString("a,b\n1,2")
                .build();

        String esperado = String.join(System.lineSeparator(),
                "+---+---+",
                "| a | b |",
                "+---+---+",
                "| 1 | 2 |",
                "+---+---+");

        assertEquals(esperado, documento.toText());
    }

    @Test
    @DisplayName("printTable envia el texto al printer configurado")
    void printTableUsaElPrinter() {
        StringBuilder salida = new StringBuilder();
        TablePrinter printer = salida::append;

        CsvDocument documento = CsvDocument.builder()
                .fromString("a,b\n1,2")
                .printer(printer)
                .build();

        documento.printTable();

        assertEquals(documento.toText(), salida.toString());
        assertTrue(salida.toString().contains("| 1 | 2 |"));
    }

    @Test
    @DisplayName("view devuelve una tabla de solo lectura coherente")
    void viewEsCoherente() {
        CsvTable vista = documento().view();

        assertEquals(2, vista.rowCount());
        assertEquals(3, vista.columnCount());
        assertEquals(vista.headers().size(), vista.columnCount());
        assertEquals(vista.row(0).size(), vista.columnCount());
    }
}
