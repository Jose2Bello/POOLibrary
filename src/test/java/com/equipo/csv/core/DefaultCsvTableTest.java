package com.equipo.csv.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultCsvTable")
class DefaultCsvTableTest {

    private DefaultCsvTable tabla() {
        return new DefaultCsvTable(
                List.of("nombre", "edad"),
                List.of(List.of("Ana", "30"), List.of("Luis", "25")));
    }

    @Test
    @DisplayName("expone filas, columnas y celdas")
    void exponeDatos() {
        DefaultCsvTable tabla = tabla();

        assertTrue(tabla.hasHeaders());
        assertEquals(2, tabla.rowCount());
        assertEquals(2, tabla.columnCount());
        assertEquals(List.of("nombre", "edad"), tabla.headers());
        assertEquals(List.of("Ana", "30"), tabla.row(0));
        assertEquals("Luis", tabla.cell(1, 0));
    }

    @Test
    @DisplayName("rechaza filas con un numero de columnas distinto")
    void rechazaFilaConColumnasDistintas() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DefaultCsvTable(List.of("a", "b"), List.of(List.of("1"))));
    }

    @Test
    @DisplayName("headers() devuelve una copia inmutable")
    void headersDevuelveCopia() {
        DefaultCsvTable tabla = tabla();

        assertThrows(UnsupportedOperationException.class, () -> tabla.headers().add("extra"));
        assertEquals(List.of("nombre", "edad"), tabla.headers());
    }

    @Test
    @DisplayName("row() devuelve una copia inmutable")
    void rowDevuelveCopia() {
        DefaultCsvTable tabla = tabla();

        assertThrows(UnsupportedOperationException.class, () -> tabla.row(0).add("extra"));
        assertEquals(List.of("Ana", "30"), tabla.row(0));
    }

    @Test
    @DisplayName("la lista original no comparte estado con la tabla")
    void noComparteEstadoConLaListaOriginal() {
        List<String> encabezados = new ArrayList<>(List.of("a", "b"));
        List<List<String>> filas = new ArrayList<>();
        filas.add(new ArrayList<>(List.of("1", "2")));

        DefaultCsvTable tabla = new DefaultCsvTable(encabezados, filas);
        encabezados.add("c");
        filas.get(0).set(0, "cambiada");

        assertEquals(2, tabla.columnCount());
        assertEquals("1", tabla.cell(0, 0));
    }

    @Test
    @DisplayName("agrega y elimina filas")
    void agregaYEliminaFilas() {
        DefaultCsvTable tabla = tabla();

        tabla.addRow(List.of("Marta", "28"));
        assertEquals(3, tabla.rowCount());
        assertEquals(List.of("Marta", "28"), tabla.row(2));

        tabla.removeRow(0);
        assertEquals(2, tabla.rowCount());
        assertEquals("Luis", tabla.cell(0, 0));
    }

    @Test
    @DisplayName("rechaza agregar filas con columnas incorrectas")
    void rechazaAgregarFilaIncorrecta() {
        DefaultCsvTable tabla = tabla();

        assertThrows(IllegalArgumentException.class, () -> tabla.addRow(List.of("solo")));
    }

    @Test
    @DisplayName("agrega una columna con valores")
    void agregaColumnaConValores() {
        DefaultCsvTable tabla = tabla();

        tabla.addColumn("ciudad", List.of("Madrid", "Bogota"));

        assertEquals(3, tabla.columnCount());
        assertEquals(List.of("nombre", "edad", "ciudad"), tabla.headers());
        assertEquals("Madrid", tabla.cell(0, 2));
        assertEquals("Bogota", tabla.cell(1, 2));
    }

    @Test
    @DisplayName("agrega una columna vacia")
    void agregaColumnaVacia() {
        DefaultCsvTable tabla = tabla();

        tabla.addColumn("notas");

        assertEquals(3, tabla.columnCount());
        assertEquals("", tabla.cell(0, 2));
        assertEquals("", tabla.cell(1, 2));
    }

    @Test
    @DisplayName("rechaza columnas con un numero de valores distinto")
    void rechazaColumnaConValoresIncorrectos() {
        DefaultCsvTable tabla = tabla();

        assertThrows(
                IllegalArgumentException.class,
                () -> tabla.addColumn("ciudad", List.of("Madrid")));
    }

    @Test
    @DisplayName("elimina columnas en todas las filas")
    void eliminaColumna() {
        DefaultCsvTable tabla = tabla();

        tabla.removeColumn(1);

        assertEquals(1, tabla.columnCount());
        assertEquals(List.of("nombre"), tabla.headers());
        assertEquals(List.of("Ana"), tabla.row(0));
        assertEquals(List.of("Luis"), tabla.row(1));
    }

    @Test
    @DisplayName("modifica una celda")
    void modificaCelda() {
        DefaultCsvTable tabla = tabla();

        tabla.setCell(1, 1, "26");

        assertEquals("26", tabla.cell(1, 1));
    }

    @Test
    @DisplayName("valida los indices de fila y columna")
    void validaIndices() {
        DefaultCsvTable tabla = tabla();

        assertThrows(IndexOutOfBoundsException.class, () -> tabla.cell(5, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> tabla.cell(0, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> tabla.row(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> tabla.removeRow(5));
        assertThrows(IndexOutOfBoundsException.class, () -> tabla.removeColumn(5));
        assertThrows(IndexOutOfBoundsException.class, () -> tabla.setCell(0, 5, "x"));
    }

    @Test
    @DisplayName("respeta el indicador de encabezados")
    void respetaIndicadorDeEncabezados() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("Columna 1"),
                List.of(List.of("Ana")),
                false);

        assertFalse(tabla.hasHeaders());
    }

    @Test
    @DisplayName("rechaza encabezados duplicados")
    void rechazaEncabezadosDuplicados() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DefaultCsvTable(
                        List.of("nombre", "nombre"),
                        List.of(List.of("Ana", "Luis"))));
    }

    @Test
    @DisplayName("detecta duplicados ignorando mayusculas y espacios")
    void detectaDuplicadosSinImportarFormato() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DefaultCsvTable(
                        List.of("Nombre", " nombre "),
                        List.of(List.of("Ana", "Luis"))));
    }

    @Test
    @DisplayName("permite encabezados vacios repetidos")
    void permiteEncabezadosVacios() {
        DefaultCsvTable tabla = new DefaultCsvTable(
                List.of("a", "", ""),
                List.of(List.of("1", "2", "3")));

        assertEquals(List.of("a", "", ""), tabla.headers());
    }

    @Test
    @DisplayName("rechaza agregar una columna con encabezado duplicado")
    void rechazaColumnaDuplicada() {
        DefaultCsvTable tabla = tabla();

        assertThrows(IllegalArgumentException.class, () -> tabla.addColumn("NOMBRE"));
    }

    @Test
    @DisplayName("rechaza valores nulos")
    void rechazaValoresNulos() {
        DefaultCsvTable tabla = tabla();

        assertThrows(NullPointerException.class, () -> tabla.addRow(java.util.Arrays.asList("Ana", null)));
        assertThrows(
                NullPointerException.class,
                () -> new DefaultCsvTable(
                        List.of("a", "b"),
                        List.of(java.util.Arrays.asList("1", null))));
        assertThrows(
                NullPointerException.class,
                () -> tabla.addColumn("ciudad", java.util.Arrays.asList("Madrid", null)));
    }

    @Test
    @DisplayName("rechaza un encabezado nulo al agregar una columna")
    void rechazaEncabezadoNulo() {
        DefaultCsvTable tabla = tabla();

        assertThrows(NullPointerException.class, () -> tabla.addColumn(null, List.of("a", "b")));
    }
}
