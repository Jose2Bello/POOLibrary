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

    public CsvDocumentBuilder parser(CsvParser parser) {
        this.parser = Objects.requireNonNull(parser, "parser");
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
        if (source == null) {
            throw new IllegalStateException(
                    "Debe indicar un origen CSV con from(Path) o fromString(String)");
        }

        CsvReader reader = new DefaultCsvReader(parser, firstRowIsHeader);
        try {
            MutableCsvTable table = reader.read(source);
            return new CsvDocument(table, decorators, formatter, printer);
        } catch (IOException exception) {
            throw new CsvLibraryException(
                    "No se pudo leer el CSV: " + source.description(), exception);
        }
    }
}
