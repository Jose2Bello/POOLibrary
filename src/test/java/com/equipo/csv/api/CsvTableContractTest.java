package com.equipo.csv.api;

import com.equipo.csv.core.DefaultCsvTable;
import com.equipo.csv.decorator.FilteredTableDecorator;
import com.equipo.csv.decorator.TrimmedTableDecorator;
import com.equipo.csv.decorator.UppercaseTableDecorator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica el principio de sustitucion de Liskov: cualquier implementacion de
 * {@link CsvTable} debe respetar el mismo contrato cuando se usa a traves de la
 * interfaz.
 */
@DisplayName("Contrato de CsvTable (LSP)")
class CsvTableContractTest {

    private static DefaultCsvTable tablaBase() {
        return new DefaultCsvTable(
                List.of(" nombre ", "ciudad"),
                List.of(
                        List.of(" ana ", " Madrid "),
                        List.of("luis", "Bogota")));
    }

    static Stream<CsvTable> tablas() {
        return Stream.of(
                tablaBase(),
                new TrimmedTableDecorator(tablaBase()),
                new UppercaseTableDecorator(tablaBase()),
                new FilteredTableDecorator(tablaBase(), fila -> fila.get(1).contains("Madrid")));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("tablas")
    @DisplayName("rowCount y columnCount nunca son negativos")
    void dimensionesNoNegativas(CsvTable tabla) {
        assertTrue(tabla.rowCount() >= 0);
        assertTrue(tabla.columnCount() >= 0);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("tablas")
    @DisplayName("headers coincide con columnCount")
    void encabezadosCoincidenConColumnas(CsvTable tabla) {
        assertEquals(tabla.columnCount(), tabla.headers().size());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("tablas")
    @DisplayName("cada fila tiene el numero de columnas esperado")
    void filasCoherentes(CsvTable tabla) {
        for (int fila = 0; fila < tabla.rowCount(); fila++) {
            assertEquals(tabla.columnCount(), tabla.row(fila).size());
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("tablas")
    @DisplayName("cell y row devuelven los mismos valores")
    void cellYRowCoinciden(CsvTable tabla) {
        for (int fila = 0; fila < tabla.rowCount(); fila++) {
            for (int columna = 0; columna < tabla.columnCount(); columna++) {
                assertEquals(tabla.row(fila).get(columna), tabla.cell(fila, columna));
            }
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("tablas")
    @DisplayName("los indices fuera de rango fallan igual en todas las implementaciones")
    void indicesFueraDeRango(CsvTable tabla) {
        assertThrows(IndexOutOfBoundsException.class, () -> tabla.row(tabla.rowCount()));
        assertThrows(IndexOutOfBoundsException.class, () -> tabla.cell(0, tabla.columnCount()));
    }
}
