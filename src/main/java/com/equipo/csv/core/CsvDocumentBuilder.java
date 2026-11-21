package com.equipo.csv.core;

import com.equipo.csv.api.CsvParser;
import com.equipo.csv.api.CsvReader;
import com.equipo.csv.api.CsvSource;
import com.equipo.csv.api.MutableCsvTable;
import com.equipo.csv.api.TableDecoratorFactory;
import com.equipo.csv.api.TableFormatter;
import com.equipo.csv.api.TablePrinter;
import com.equipo.csv.exception.CsvLibraryException;
import com.equipo.csv.source.PathCsvSource;
import com.equipo.csv.source.StringCsvSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Construye un {@link CsvDocument} con componentes configurables.
 */
public final class CsvDocumentBuilder {

    private CsvSource source;
    private MutableCsvTable table;
    private CsvReader reader;
    private CsvParser parser = new SimpleCsvParser();
    private TableFormatter formatter = new PlainTableFormatter();
    private TablePrinter printer = new ConsoleTablePrinter();
    private final List<TableDecoratorFactory> decorators = new ArrayList<>();
    private boolean firstRowIsHeader = true;

    public static CsvDocumentBuilder builder() {
        return new CsvDocumentBuilder();
    }

    public CsvDocumentBuilder from(Path path) {
        this.source = new PathCsvSource(path);
        return this;
    }

    public CsvDocumentBuilder fromString(String csv) {
        this.source = new StringCsvSource(csv);
        return this;
    }

    public CsvDocumentBuilder source(CsvSource source) {
        this.source = Objects.requireNonNull(source, "source");
        return this;
    }

    /**
     * Define la tabla ya construida que usará el documento.
     *
     * <p>Al usar este método no se lee ninguna fuente: el documento trabaja
     * directamente sobre la tabla recibida, que sigue siendo mutable.</p>
     *
     * @param table tabla base del documento
     * @return este builder
     */
    public CsvDocumentBuilder fromTable(MutableCsvTable table) {
        this.table = Objects.requireNonNull(table, "table");
        return this;
    }

    public CsvDocumentBuilder parser(CsvParser parser) {
        this.parser = Objects.requireNonNull(parser, "parser");
        return this;
    }

    /**
     * Define un lector propio y omite el {@code DefaultCsvReader} interno.
     *
     * <p>Cuando se usa este método, {@link #parser(CsvParser)} y
     * {@link #firstRowIsHeader(boolean)} dejan de aplicarse, porque el lector
     * recibido es el responsable de construir la tabla.</p>
     *
     * @param reader lector que convertirá la fuente en tabla
     * @return este builder
     */
    public CsvDocumentBuilder reader(CsvReader reader) {
        this.reader = Objects.requireNonNull(reader, "reader");
        return this;
    }

    public CsvDocumentBuilder formatter(TableFormatter formatter) {
        this.formatter = Objects.requireNonNull(formatter, "formatter");
        return this;
    }

    public CsvDocumentBuilder printer(TablePrinter printer) {
        this.printer = Objects.requireNonNull(printer, "printer");
        return this;
    }

    public CsvDocumentBuilder decorate(TableDecoratorFactory decorator) {
        decorators.add(Objects.requireNonNull(decorator, "decorator"));
        return this;
    }

    public CsvDocumentBuilder firstRowIsHeader(boolean firstRowIsHeader) {
        this.firstRowIsHeader = firstRowIsHeader;
        return this;
    }

    public CsvDocument build() {
        if (table != null && source != null) {
            throw new IllegalStateException(
                    "Use from(Path), fromString(String) o fromTable(MutableCsvTable), no ambos");
        }

        if (table != null) {
            return createDocument(table);
        }

        if (source == null) {
            throw new IllegalStateException(
                    "Debe indicar un origen CSV con from(Path), fromString(String) "
                            + "o una tabla con fromTable(MutableCsvTable)");
        }

        CsvReader effectiveReader = reader != null
                ? reader
                : new DefaultCsvReader(parser, firstRowIsHeader);

        try {
            return createDocument(effectiveReader.read(source));
        } catch (IOException exception) {
            throw new CsvLibraryException(
                    "No se pudo leer el CSV: " + source.description(), exception);
        }
    }

    private CsvDocument createDocument(MutableCsvTable data) {
        return new CsvDocument(data, decorators, formatter, printer);
    }
}
