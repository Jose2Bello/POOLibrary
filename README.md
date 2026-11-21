# POOLibrary

Librería Java extensible para cargar, editar, imprimir y transformar archivos CSV
sin modificar el código base de la librería.

## Estado

Proyecto en desarrollo. La API del núcleo está definida, el flujo completo está
cubierto por pruebas y los módulos restantes se integran por partes mediante
Pull Requests.

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
- Inyectar un lector propio con `CsvReader`.
- Construir un documento desde una tabla ya cargada en memoria.
- Obtener la tabla como texto sin imprimirla.

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

### Pruebas

Las pruebas están en `src/test/java` y usan JUnit 5. Cada comando
`mvnw test` ejecuta 89 casos:

| Suite | Cubre |
|---|---|
| `SimpleCsvParserTest` | comillas, comas dentro de comillas, comillas escapadas, saltos de línea, BOM, delimitadores, comilla sin cerrar y líneas vacías. |
| `DefaultCsvReaderTest` | encabezado opcional, relleno de filas desiguales, CSV vacío y errores de la fuente. |
| `DefaultCsvTableTest` | filas, columnas, celdas, copias inmutables e índices fuera de rango. |
| `PlainTableFormatterTest` | bordes, alineación, encabezados y tabla sin columnas. |
| `TableDecoratorTest` | recorte, mayúsculas, filtrado, composición y orden de aplicación. |
| `CsvDocumentTest` | edición, aplicación de decoradores, `view()`, `toText()` y `printTable()`. |
| `CsvDocumentBuilderTest` | texto, archivo, tabla en memoria, lector propio y errores de configuración. |
| `CsvSourceTest` | `StringCsvSource`, `PathCsvSource` y archivos inexistentes. |
| `CsvTableContractTest` | contrato común de `CsvTable` para verificar LSP. |
| `CsvLibraryIntegrationTest` | flujo completo de lectura, decoración y salida, y extensión con otro parser. |

Los datos de prueba están en `src/test/resources`.

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

### Salida como texto

`toText()` aplica los decoradores y el formateador, pero no imprime:

```java
String texto = documento.toText();
```

### Documento desde una tabla en memoria

Si la tabla ya existe, no hace falta una fuente CSV:

```java
DefaultCsvTable tabla = new DefaultCsvTable(
        List.of("Nombre", "Ciudad"),
        List.of(List.of("Ana", "Madrid")));

CsvDocument documento = CsvDocument.builder()
        .fromTable(tabla)
        .build();
```

### Lector propio

El builder usa `DefaultCsvReader` por defecto. Para cambiarlo:

```java
CsvDocument documento = CsvDocument.builder()
        .from(Path.of("datos.csv"))
        .reader(new MiCsvReader())
        .build();
```

Al inyectar un `CsvReader`, el `parser(...)` y el `firstRowIsHeader(...)`
del builder ya no se usan: el lector recibido es el que construye la tabla.

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
- `CsvReader` para una carga completa distinta, inyectada con `reader(...)`.
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

| Integrante | Responsabilidad actual |
|---|---|
| José | Arquitectura, núcleo, parser, documento y extensiones; JavaDoc. |
| Miguel Luzardo | Redacción del informe. |
| Santiago Salas | Formatters y printers adicionales, validaciones, secciones técnicas pendientes y revisión del diagrama. |

## Reglas de colaboración

- No hacer commits directamente sobre `main`.
- Usar una rama por funcionalidad y abrir un Pull Request.
- Ejecutar `.\mvnw.cmd test` o `./mvnw test` antes del PR.
- Mantener el README actualizado si cambia la API.
