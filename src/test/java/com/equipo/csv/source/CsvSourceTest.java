package com.equipo.csv.source;

import com.equipo.csv.api.CsvSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Fuentes CSV")
class CsvSourceTest {

    private String leer(CsvSource source) throws IOException {
        try (Reader reader = source.open()) {
            StringBuilder texto = new StringBuilder();
            int caracter;
            while ((caracter = reader.read()) != -1) {
                texto.append((char) caracter);
            }
            return texto.toString();
        }
    }

    @Test
    @DisplayName("StringCsvSource entrega el texto en memoria")
    void stringSourceEntregaTexto() throws Exception {
        CsvSource source = new StringCsvSource("a,b\n1,2");

        assertEquals("a,b\n1,2", leer(source));
        assertEquals("texto en memoria", source.description());
    }

    @Test
    @DisplayName("PathCsvSource lee el archivo con UTF-8")
    void pathSourceLeeArchivo(@TempDir Path directorio) throws Exception {
        Path archivo = directorio.resolve("datos.csv");
        Files.writeString(archivo, "nombre,ciudad\nAna,Bogota\n");

        CsvSource source = new PathCsvSource(archivo);

        assertEquals("nombre,ciudad\nAna,Bogota\n", leer(source));
        assertEquals(archivo.toString(), source.description());
    }

    @Test
    @DisplayName("PathCsvSource falla si el archivo no existe")
    void pathSourceFallaSinArchivo(@TempDir Path directorio) {
        CsvSource source = new PathCsvSource(directorio.resolve("no-existe.csv"));

        assertThrows(IOException.class, source::open);
    }

    @Test
    @DisplayName("description() no queda vacia por defecto")
    void descriptionPorDefectoNoEstaVacia() {
        CsvSource personalizada = () -> new java.io.StringReader("x");

        assertFalse(personalizada.description().isBlank());
    }
}
