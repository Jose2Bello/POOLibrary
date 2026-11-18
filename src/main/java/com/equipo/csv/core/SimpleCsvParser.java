package com.equipo.csv.core;

import com.equipo.csv.api.CsvParser;
import com.equipo.csv.exception.CsvParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parser CSV básico con soporte para comillas, comas y saltos de línea.
 */
public class SimpleCsvParser implements CsvParser {

    private static final char QUOTE = '"';

    private final char delimiter;

    public SimpleCsvParser() {
        this(',');
    }

    public SimpleCsvParser(char delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public List<List<String>> parse(Reader reader) throws IOException {
        Objects.requireNonNull(reader, "reader");
        String text = readAll(reader);
        List<List<String>> rows = new ArrayList<>();
        List<String> currentRow = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        boolean rowStarted = false;

        int start = text.startsWith("\uFEFF") ? 1 : 0;

        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);

            if (inQuotes) {
                if (c == QUOTE) {
                    if (i + 1 < text.length() && text.charAt(i + 1) == QUOTE) {
                        field.append(QUOTE);
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    field.append(c);
                }
                continue;
            }

            if (c == QUOTE) {
                inQuotes = true;
                rowStarted = true;
            } else if (c == delimiter) {
                currentRow.add(field.toString());
                field.setLength(0);
                rowStarted = true;
            } else if (c == '\r' || c == '\n') {
                if (c == '\r' && i + 1 < text.length() && text.charAt(i + 1) == '\n') {
                    i++;
                }
                if (rowStarted || !currentRow.isEmpty() || field.length() > 0) {
                    currentRow.add(field.toString());
                    field.setLength(0);
                    rows.add(new ArrayList<>(currentRow));
                    currentRow.clear();
                }
                rowStarted = false;
            } else {
                field.append(c);
                rowStarted = true;
            }
        }

        if (inQuotes) {
            throw new CsvParseException("Comilla sin cerrar en el CSV");
        }

        if (rowStarted || !currentRow.isEmpty() || field.length() > 0) {
            currentRow.add(field.toString());
            rows.add(new ArrayList<>(currentRow));
        }

        return rows;
    }

    private String readAll(Reader reader) throws IOException {
        StringBuilder text = new StringBuilder();
        char[] buffer = new char[4096];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            text.append(buffer, 0, read);
        }
        return text.toString();
    }
}
