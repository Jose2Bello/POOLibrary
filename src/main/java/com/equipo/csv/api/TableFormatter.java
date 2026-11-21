package com.equipo.csv.api;

/**
 * Convierte una tabla en texto.
 *
 * <p>Es el punto de extensión para cambiar la representación de salida: texto
 * plano, Markdown, HTML o JSON. No modifica la tabla que recibe.</p>
 */
public interface TableFormatter {

    /**
     * Genera la representación textual de la tabla.
     *
     * @param table tabla de solo lectura a formatear
     * @return texto formateado
     */
    String format(CsvTable table);
}
