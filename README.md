# POOLibrary

Librería Java extensible para cargar, editar, imprimir y transformar archivos CSV
sin modificar el código base de la librería.

## Estado

Proyecto en desarrollo. La API del núcleo está definida y el resto de módulos se
integra por partes mediante Pull Requests.

## Funcionalidades

- Cargar un CSV desde un archivo o desde un texto.
- Representar filas, columnas, celdas y encabezados como `String`.
- Agregar, eliminar y modificar filas.
- Agregar, eliminar y modificar columnas.
- Imprimir la tabla en consola.
- Cambiar el formato de salida sin modificar la librería.
- Cambiar el destino de impresión: consola, archivo o memoria.
- Aplicar transformaciones propias mediante decoradores.
- Cambiar el parseo del CSV con una nueva implementación de `CsvParser`.

## Stack

- Java 21.
- Maven Wrapper y JUnit 5.
- Parser CSV propio.

## Construcción y pruebas

No es necesario instalar Maven.

Windows:

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
```

Linux o macOS:

```bash
./mvnw test
./mvnw clean package
```

## Arquitectura

```text
com.equipo.csv
├── api          → interfaces públicas
├── core         → implementaciones base
├── decorator    → extensiones de tabla
├── source       → orígenes de datos
├── exception    → excepciones propias
└── examples     → ejemplos de uso
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

### SOLID

| Principio | Aplicación |
|---|---|
| SRP | Parsear, almacenar, formatear, imprimir y coordinar son responsabilidades separadas. |
| OCP | Nuevos parsers, formateadores, impresores o decoradores se agregan sin modificar el núcleo. |
| LSP | Las implementaciones y decoradores de `CsvTable` son sustituibles entre sí. |
| ISP | `CsvTable` es de solo lectura; `MutableCsvTable` agrega escritura; formato e impresión están separados. |
| DIP | `CsvDocument` depende de interfaces, no de clases concretas. |

### Patrones

| Patrón | Uso |
|---|---|
| Strategy | `CsvParser`, `TableFormatter` y `TablePrinter`. |
| Builder | `CsvDocumentBuilder`. |
| Decorator | Transformaciones de tabla como recortar, mayúsculas o filtrar filas. |
| Facade | `CsvDocument` como objeto principal. |
| Factory Method | `CsvDocument.builder()` y `CsvLibrary`. |
| Adapter | `PathCsvSource` y `StringCsvSource`. |

## Diagrama de clases

El diagrama editable está en `docs/diagrama-clases.drawio` y se abre con
[draw.io](https://app.diagrams.net/) o la aplicación de escritorio.

## Uso

```java
import com.equipo.csv.core.CsvDocument;
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

También se puede cargar desde texto con `CsvLibrary.parse(...)`.

## Extensión

Un decorador propio se crea extendiendo `AbstractTableDecorator`:

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

Se aplica sin modificar la librería:

```java
CsvDocument documento = CsvDocument.builder()
        .from(Path.of("datos.csv"))
        .decorate(MaskEmailDecorator::new)
        .build();

documento.printTable();
```

Otros puntos de extensión:

- `TableFormatter` para Markdown, HTML o JSON.
- `TablePrinter` para archivo o memoria.
- `CsvParser` para otro delimitador o formato.
- `TableDecoratorFactory` para transformaciones adicionales.

## Estructura del repositorio

```text
POOLibrary/
├── pom.xml
├── README.md
├── mvnw
├── mvnw.cmd
├── .mvn/
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
    └── diagrama-clases.drawio
```

## Equipo y pendientes

| Integrante | Pendientes principales |
|---|---|
| José | Subir parser, documento y extensiones; completar JavaDoc; escribir introducción, arquitectura y SOLID del informe; revisar PRs. |
| Integrante 2 | Pruebas de parser, lector y tabla; crear `SemicolonCsvParser`; escribir pruebas y LSP en el informe. |
| Integrante 3 | Crear `MarkdownTableFormatter` y `FileTablePrinter`; probar decoradores; hacer el diagrama y la sección de patrones. |

## Reglas de colaboración

- No hacer commits directamente sobre `main`.
- Usar una rama por funcionalidad y abrir un Pull Request.
- Ejecutar `.\mvnw.cmd test` o `./mvnw test` antes del PR.
- Mantener el README actualizado si cambia la API.
