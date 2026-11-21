package com.equipo.csv.core;

import com.equipo.csv.api.CsvTable;
import com.equipo.csv.api.MutableCsvTable;
import com.equipo.csv.api.TableDecoratorFactory;
import com.equipo.csv.api.TableFormatter;
import com.equipo.csv.api.TablePrinter;

import java.util.List;
import java.util.Objects;

/**
 * Objeto principal de la librería. Permite editar, transformar e imprimir una tabla.
 *
 * <p>Actúa como fachada: coordina la tabla, los decoradores, el formateador y el
 * impresor. Las ediciones se aplican sobre la tabla base; los decoradores solo
 * afectan a la vista que se genera al formatear.</p>
 */
public final class CsvDocument {

    private final MutableCsvTable table;
    private final List<TableDecoratorFactory> decorators;
    private final TableFormatter formatter;
    private final TablePrinter printer;

    /**
     * Crea un documento con sus componentes ya construidos.
     *
     * @param table      tabla base, que sigue siendo mutable
     * @param decorators decoradores que se aplican en orden sobre la vista
     * @param formatter  formateador de la vista
     * @param printer    destino de la salida
     * @throws NullPointerException si algún argumento es {@code null}
     */
    public CsvDocument(
            MutableCsvTable table,
            List<TableDecoratorFactory> decorators,
            TableFormatter formatter,
            TablePrinter printer) {
        this.table = Objects.requireNonNull(table, "table");
        this.decorators = List.copyOf(Objects.requireNonNull(decorators, "decorators"));
        this.formatter = Objects.requireNonNull(formatter, "formatter");
        this.printer = Objects.requireNonNull(printer, "printer");
    }

    /**
     * Inicia la construcción de un documento.
     *
     * @return un builder vacío
     */
    public static CsvDocumentBuilder builder() {
        return new CsvDocumentBuilder();
    }

    /**
     * Agrega una fila al final de la tabla base.
     *
     * @param values valores de la fila
     * @throws IllegalArgumentException si la cantidad no coincide con las columnas
     */
    public void addRow(List<String> values) {
        table.addRow(values);
    }

    /**
     * Elimina una fila de la tabla base.
     *
     * @param row índice de fila, desde cero
     * @throws IndexOutOfBoundsException si la fila no existe
     */
    public void removeRow(int row) {
        table.removeRow(row);
    }

    /**
     * Agrega una columna con valores a la tabla base.
     *
     * @param header nombre de la columna
     * @param values un valor por fila
     * @throws IllegalArgumentException si la cantidad no coincide o el nombre ya existe
     */
    public void addColumn(String header, List<String> values) {
        table.addColumn(header, values);
    }

    /**
     * Agrega una columna vacía a la tabla base.
     *
     * @param header nombre de la columna
     * @throws IllegalArgumentException si el nombre ya existe
     */
    public void addColumn(String header) {
        table.addColumn(header);
    }

    /**
     * Elimina una columna de la tabla base.
     *
     * @param column índice de columna, desde cero
     * @throws IndexOutOfBoundsException si la columna no existe
     */
    public void removeColumn(int column) {
        table.removeColumn(column);
    }

    /**
     * Reemplaza el valor de una celda de la tabla base.
     *
     * @param row    índice de fila, desde cero
     * @param column índice de columna, desde cero
     * @param value  nuevo valor
     * @throws IndexOutOfBoundsException si la fila o la columna no existen
     */
    public void setCell(int row, int column, String value) {
        table.setCell(row, column, value);
    }

    /**
     * Devuelve la vista actual de la tabla con todos los decoradores aplicados.
     *
     * @return tabla de solo lectura que refleja el estado actual
     */
    public CsvTable view() {
        CsvTable result = table;
        for (TableDecoratorFactory decorator : decorators) {
            result = decorator.decorate(result);
        }
        return result;
    }

    /**
     * Devuelve el texto de la tabla con el formato configurado.
     *
     * <p>Aplica los decoradores y el {@link TableFormatter}, pero no imprime.</p>
     *
     * @return representación textual de la vista actual
     */
    public String toText() {
        return formatter.format(view());
    }

    /**
     * Imprime la vista actual usando el {@link TablePrinter} configurado.
     */
    public void printTable() {
        printer.print(toText());
    }
}
