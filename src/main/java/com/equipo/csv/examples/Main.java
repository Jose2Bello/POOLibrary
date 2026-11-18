package com.equipo.csv.examples;

import com.equipo.csv.core.CsvDocument;
import com.equipo.csv.decorator.FilteredTableDecorator;
import com.equipo.csv.decorator.UppercaseTableDecorator;

import java.util.List;

/**
 * Ejemplo de uso y extensión de la librería.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        CsvDocument documento = CsvDocument.builder()
                .fromString("""
                        Nombre,Edad,Ciudad
                        Ana,30,Madrid
                        Luis,25,Bogota
                        Marta,28,Lima
                        """)
                .decorate(UppercaseTableDecorator::new)
                .decorate(tabla -> new FilteredTableDecorator(
                        tabla,
                        fila -> !fila.get(1).equals("25")))
                .build();

        System.out.println("Tabla transformada:");
        documento.printTable();

        documento.addRow(List.of("Pedro", "40", "Quito"));
        documento.removeColumn(2);

        System.out.println();
        System.out.println("Tabla editada:");
        documento.printTable();
    }
}
