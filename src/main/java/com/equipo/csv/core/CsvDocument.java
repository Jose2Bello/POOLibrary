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
 */
public final class CsvDocument {

    private final MutableCsvTable table;
    private final List<TableDecoratorFactory> decorators;
    private final TableFormatter formatter;
    private final TablePrinter printer;

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

    public static CsvDocumentBuilder builder() {
        return new CsvDocumentBuilder();
    }

    public void addRow(List<String> values) {
        table.addRow(values);
    }

    public void removeRow(int row) {
        table.removeRow(row);
    }

    public void addColumn(String header, List<String> values) {
        table.addColumn(header, values);
    }

    public void addColumn(String header) {
        table.addColumn(header);
    }

    public void removeColumn(int column) {
        table.removeColumn(column);
    }

    public void setCell(int row, int column, String value) {
        table.setCell(row, column, value);
    }

    /**
     * Devuelve la vista actual de la tabla con todos los decoradores aplicados.
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

    public void printTable() {
        printer.print(toText());
    }
}
