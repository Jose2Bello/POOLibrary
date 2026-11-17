# POOLibrary

Librería Java extensible para cargar, editar, imprimir y transformar archivos CSV
sin modificar el código base de la librería.

## Estado

Proyecto en fase de implementación inicial. La API pública y las pruebas se
encuentran en construcción.

## Objetivos

- Representar un archivo CSV mediante un objeto con datos de tipo `String`.
- Agregar y quitar filas o columnas.
- Imprimir la tabla en consola.
- Permitir extensiones propias mediante interfaces y composición.
- Aplicar los principios SOLID.
- Documentar el diseño, los patrones y la forma de extender la librería.

## Funcionalidades

- Cargar un CSV desde un archivo o desde un texto.
- Acceder a filas, columnas, celdas y encabezados.
- Agregar, eliminar y modificar filas.
- Agregar, eliminar y modificar columnas.
- Imprimir la tabla con formato de texto.
- Cambiar el formato de salida sin modificar la librería.
- Cambiar el destino de impresión: consola, archivo o memoria.
- Aplicar transformaciones propias mediante decoradores.
- Cambiar la forma de parsear el CSV mediante una nueva implementación de `CsvParser`.

## Stack

- Java 21.
- Maven como herramienta de construcción y dependencias.
- JUnit 5 para pruebas.
- Parser CSV propio.
- Datos representados como `String`.

## Arquitectura

```text
com.equipo.csv
├── api          → interfaces públicas
├── core         → implementaciones base
├── decorator    → extensiones de tabla
├── source       → orígenes de datos
├── exception    → excepciones propias
└── examples     → ejemplos de uso y extensión
```

### Interfaces principales

- `CsvTable`: lectura de filas, columnas, celdas y encabezados.
- `MutableCsvTable`: operaciones de escritura sobre la tabla.
- `CsvSource`: origen del CSV.
- `CsvParser`: conversión de texto CSV a filas.
- `CsvReader`: carga de un CSV.
- `TableFormatter`: representación de la tabla como texto.
- `TablePrinter`: salida del texto generado.
- `TableDecoratorFactory`: creación de decoradores de tabla.

### Principios SOLID

- **SRP:** cada clase tiene una sola responsabilidad: parsear, almacenar, formatear, imprimir o coordinar.
- **OCP:** se pueden agregar nuevos parsers, formateadores, impresores o decoradores sin modificar las clases existentes.
- **LSP:** las implementaciones y decoradores de `CsvTable` pueden sustituirse entre sí sin romper el contrato.
- **ISP:** `CsvTable` es de solo lectura; `MutableCsvTable` agrega escritura; formateador e impresor están separados.
- **DIP:** `CsvDocument` depende de interfaces, no de clases concretas.

### Patrones utilizados

- **Strategy:** `CsvParser`, `TableFormatter` y `TablePrinter`.
- **Builder:** `CsvDocumentBuilder`.
- **Decorator:** transformaciones como recortar espacios, convertir a mayúsculas o filtrar filas.
- **Facade:** `CsvDocument` como objeto principal de uso.
- **Factory Method:** `CsvDocument.builder()` y `CsvLibrary`.
- **Adapter:** adaptación de archivos y textos a `CsvSource`.

## Uso

```java
import com.equipo.csv.core.CsvDocument;
import com.equipo.csv.core.CsvLibrary;
import com.equipo.csv.decorator.UppercaseTableDecorator;

import java.nio.file.Path;
import java.util.List;

CsvDocument documento = CsvDocument.builder()
        .from(Path.of("datos.csv"))
        .decorate(UppercaseTableDecorator::new)
        .build();

documento.addRow(List.of("Ana", "30", "Ventas"));
documento.removeColumn(2);
documento.printTable();
```

También se puede cargar desde un texto:

```java
CsvDocument documento = CsvLibrary.parse("""
        Nombre,Edad,Ciudad
        Ana,30,Madrid
        Luis,25,Bogota
        """);

documento.printTable();
```

## Extensión sin modificar la librería

### Nuevo decorador

```java
import com.equipo.csv.api.CsvTable;
import com.equipo.csv.decorator.AbstractTableDecorator;

public class MaskEmailDecorator extends AbstractTableDecorator {

    public MaskEmailDecorator(CsvTable delegate) {
        super(delegate);
    }

    @Override
    public String cell(int row, int column) {
        String value = delegate.cell(row, column);
        return value.contains("@") ? "***@***" : value;
    }
}
```

Uso:

```java
CsvDocument documento = CsvDocument.builder()
        .from(Path.of("datos.csv"))
        .decorate(MaskEmailDecorator::new)
        .build();

documento.printTable();
```

### Nuevo formateador

```java
public class MarkdownTableFormatter implements TableFormatter {

    @Override
    public String format(CsvTable table) {
        // Convertir la tabla a una tabla Markdown.
        return "...";
    }
}
```

### Nuevo impresor

```java
public class FileTablePrinter implements TablePrinter {

    @Override
    public void print(String text) {
        // Escribir el texto en un archivo.
    }
}
```

### Nuevo parser

```java
public class SemicolonCsvParser implements CsvParser {

    @Override
    public List<List<String>> parse(Reader reader) throws IOException {
        // Parsear un CSV separado por punto y coma.
        return List.of();
    }
}
```

## Estructura del repositorio

```text
POOLibrary/
├── pom.xml
├── README.md
├── .gitignore
├── LICENSE
├── src/
│   ├── main/java/com/equipo/csv/
│   │   ├── api/
│   │   ├── core/
│   │   ├── decorator/
│   │   ├── source/
│   │   ├── exception/
│   │   └── examples/
│   └── test/
│       ├── java/com/equipo/csv/
│       └── resources/
└── docs/
    ├── informe.md
    ├── diagrama-clases.puml
    └── diagrama-clases.md
```

## Equipo y distribución del trabajo

| Integrante | Rol principal | Entregables | Carga |
|---|---|---|---|
| José Bello | Arquitectura y núcleo | Estructura del proyecto, interfaces, `DefaultCsvTable`, parser base, `CsvDocument`, `CsvDocumentBuilder`, integración, JavaDoc y revisión de PR | 35% |
| Integrante 2 | Pruebas de entrada y calidad | Pruebas de `SimpleCsvParser`, `DefaultCsvReader` y `DefaultCsvTable`, casos límite, parser de ejemplo con otro delimitador, sección de pruebas del informe | 33% |
| Integrante 3 | Salida, extensiones y documentación | `MarkdownTableFormatter`, `FileTablePrinter`, pruebas de decoradores, ejemplo de extensión, diagrama de clases y sección de patrones del informe | 32% |

La diferencia de carga corresponde a la integración y revisión general del
repositorio. Todas las secciones del informe se revisan en conjunto.

### Distribución del informe

- **José:** introducción, arquitectura, explicación de SOLID y conclusiones.
- **Integrante 2:** carga y parseo del CSV, pruebas, LSP y resultados.
- **Integrante 3:** patrones de diseño, extensibilidad, diagrama de clases y guía de extensión.

## Tareas pendientes

- [ ] Pruebas unitarias de `SimpleCsvParser` y `DefaultCsvReader` — Integrante 2.
- [ ] Pruebas de `DefaultCsvTable` y validación de índices — Integrante 2.
- [ ] Pruebas de decoradores y sustitución por `CsvTable` (LSP) — Integrante 3.
- [ ] `MarkdownTableFormatter` — Integrante 3.
- [ ] `FileTablePrinter` — Integrante 3.
- [ ] `SemicolonCsvParser` de ejemplo — Integrante 2.
- [ ] `docs/informe.md` — todo el equipo.
- [ ] `docs/diagrama-clases.puml` y su imagen — Integrante 3.
- [ ] Completar JavaDoc de la API pública — José.
- [ ] Ejecutar `mvn test` y actualizar este README — José.
- [ ] Crear las ramas y Pull Requests — todo el equipo.

## Reglas de colaboración

- No hacer commits directamente sobre `main`.
- Crear una rama por funcionalidad: `feature/core`, `feature/parser`, `feature/output`.
- Cada cambio se integra mediante Pull Request.
- Ejecutar `mvn test` antes de abrir un PR.
- Mantener el README actualizado si cambia la API.
- Revisar el código de otro integrante antes de fusionar.

## Documentación

- `README.md`: uso y extensión de la librería.
- `docs/informe.md`: informe del proyecto.
- `docs/diagrama-clases.md`: diagrama de clases en Mermaid.
- `docs/diagrama-clases.puml`: fuente editable del diagrama en PlantUML.
