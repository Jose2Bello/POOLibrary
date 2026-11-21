package com.equipo.csv.decorator;

import com.equipo.csv.api.CsvTable;
import com.equipo.csv.core.DefaultCsvTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Decoradores de tabla")
class TableDecoratorTest {

    private DefaultCsvTable tabla() {
        return new DefaultCsvTable(
                List.of(" nombre ", "ciudad"),
                List.of(
                        List.of(" ana ", " Madrid "),
                        List.of("luis", "Bogota")));
    }

    @Nested
    @DisplayName("TrimmedTableDecorator")
    class Trimmed {

        @Test
        @DisplayName("recorta celdas, filas y encabezados")
        void recortaValores() {
            CsvTable tabla = new TrimmedTableDecorator(tabla());

            assertEquals(List.of("nombre", "ciudad"), tabla.headers());
            assertEquals("ana", tabla.cell(0, 0));
            assertEquals("Madrid", tabla.cell(0, 1));
            assertEquals(List.of("ana", "Madrid"), tabla.row(0));
        }

        @Test
        @DisplayName("no modifica la tabla original")
        void noModificaOriginal() {
            DefaultCsvTable original = tabla();
            new TrimmedTableDecorator(original).row(0);

            assertEquals(" ana ", original.cell(0, 0));
        }
    }

    @Nested
    @DisplayName("UppercaseTableDecorator")
    class Uppercase {

        @Test
        @DisplayName("convierte celdas, filas y encabezados a mayusculas")
        void convierteAMayusculas() {
            CsvTable tabla = new UppercaseTableDecorator(tabla());

            assertEquals(List.of(" NOMBRE ", "CIUDAD"), tabla.headers());
            assertEquals(" ANA ", tabla.cell(0, 0));
            assertEquals(List.of(" ANA ", " MADRID "), tabla.row(0));
        }
    }

    @Nested
    @DisplayName("FilteredTableDecorator")
    class Filtered {

        @Test
        @DisplayName("oculta las filas que no cumplen el predicado")
        void ocultaFilas() {
            CsvTable tabla = new FilteredTableDecorator(
                    tabla(),
                    fila -> fila.get(1).contains("Madrid"));

            assertEquals(1, tabla.rowCount());
            assertEquals(List.of(" ana ", " Madrid "), tabla.row(0));
            assertEquals(" ana ", tabla.cell(0, 0));
        }

        @Test
        @DisplayName("falla al pedir una fila fuera de la vista")
        void fallaFueraDeLaVista() {
            CsvTable tabla = new FilteredTableDecorator(
                    tabla(),
                    fila -> fila.get(1).contains("Madrid"));

            assertThrows(IndexOutOfBoundsException.class, () -> tabla.row(1));
        }
    }

    @Nested
    @DisplayName("Composicion")
    class Composicion {

        @Test
        @DisplayName("combina recorte y mayusculas")
        void combinaRecorteYMayusculas() {
            CsvTable tabla = new UppercaseTableDecorator(
                    new TrimmedTableDecorator(tabla()));

            assertEquals(List.of("NOMBRE", "CIUDAD"), tabla.headers());
            assertEquals(List.of("ANA", "MADRID"), tabla.row(0));
        }

        @Test
        @DisplayName("el orden de los decoradores cambia el resultado")
        void elOrdenImporta() {
            CsvTable recortaLuegoFiltra = new FilteredTableDecorator(
                    new TrimmedTableDecorator(tabla()),
                    fila -> fila.get(1).equals("Madrid"));

            CsvTable filtraLuegoRecorta = new FilteredTableDecorator(
                    new UppercaseTableDecorator(tabla()),
                    fila -> fila.get(1).equals("Madrid"));

            assertEquals(1, recortaLuegoFiltra.rowCount());
            assertEquals(0, filtraLuegoRecorta.rowCount());
        }

        @Test
        @DisplayName("un decorador no acepta una tabla nula")
        void rechazaDelegadoNulo() {
            assertThrows(NullPointerException.class, () -> new TrimmedTableDecorator(null));
        }
    }
}
