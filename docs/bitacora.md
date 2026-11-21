# Bitácora de cambios

Registro de las decisiones y modificaciones hechas sobre la librería.
Cada entrada indica qué se hizo y por qué. El historial completo de commits
está en `git log`.

## 2026-11-20

### Suite de pruebas

- Se creó `src/test` con 89 casos en JUnit 5, todos en verde con
  `.\mvnw.cmd test`:
  - `SimpleCsvParserTest`: comillas, comas dentro de comillas, comillas
    escapadas, saltos de línea dentro de un campo, BOM, delimitador `;`, CSV
    vacío, líneas vacías, campo vacío final, CRLF y comilla sin cerrar.
  - `DefaultCsvReaderTest`: encabezado opcional, encabezados generados, relleno
    de filas desiguales, CSV vacío, solo encabezado y error de la fuente.
  - `DefaultCsvTableTest`: lectura de datos, copias inmutables, alta y baja de
    filas y columnas, `setCell` e índices fuera de rango.
  - `PlainTableFormatterTest`: bordes, alineación, tabla sin encabezado, tabla
    sin columnas y ancho según la celda más larga.
  - `TableDecoratorTest`: recorte, mayúsculas, filtrado, composición, orden de
    aplicación y delegado nulo.
  - `CsvDocumentTest`: edición, orden de decoradores, vista actualizada,
    `toText()` y `printTable()` con un printer de prueba.
  - `CsvDocumentBuilderTest`: texto, archivo con `@TempDir`, tabla en memoria,
    lector inyectado, formatter y printer propios, errores de configuración y
    envoltura de `IOException`.
  - `CsvSourceTest`: `StringCsvSource`, `PathCsvSource`, archivo inexistente y
    `description()`.
  - `CsvTableContractTest`: prueba parametrizada sobre las cuatro
    implementaciones de `CsvTable`, para verificar LSP.
  - `CsvLibraryIntegrationTest`: flujo completo, parser con otro delimitador y
    reflejo de las ediciones en la salida.
- Se agregó `src/test/resources/personas.csv` para las pruebas con archivos.

Commits:

- `test(parser): cover quotes, delimiters and edge cases`
- `test(reader): cover headers, padding and empty input`
- `test(table): cover row and column operations`
- `test(formatter): verify table rendering`
- `test(decorator): cover transformations and composition`
- `test(document): cover editing, decorators and output`
- `test(builder): cover sources, reader injection and errors`
- `test(source): cover string and path sources`
- `test(api): verify CsvTable contract for LSP`
- `test(integration): cover end-to-end document flow`

### Extensión del builder

- Se agregó `CsvDocumentBuilder.reader(CsvReader)`.
  Antes, `build()` siempre construía un `DefaultCsvReader`, así que un lector
  propio no se podía usar sin tocar el código base. Con este método el builder
  acepta cualquier implementación de `CsvReader` y deja de aplicar
  `parser(...)` y `firstRowIsHeader(...)`, porque el lector inyectado es el
  responsable de construir la tabla.
- Se agregó `CsvDocumentBuilder.fromTable(MutableCsvTable)`.
  Permite construir un `CsvDocument` desde una tabla que ya está en memoria,
  sin pasar por una fuente CSV. Antes había que usar el constructor público de
  `CsvDocument` y saltarse el builder.
- `build()` ahora distingue tres casos y falla con un mensaje claro:
  - tabla en memoria (`fromTable`), sin fuente;
  - fuente CSV (`from` o `fromString`), opcionalmente con lector propio;
  - error si se combinan `fromTable` con una fuente.

Commits:

- `feat(builder): allow custom readers and in-memory tables`

### Salida como texto

- Se agregó `CsvDocument.toText()`.
  Devuelve el texto ya formateado y con los decoradores aplicados, sin
  imprimirlo. `printTable()` ahora reutiliza ese método:
  `printer.print(toText())`.
  Esto permite probar el formateo sin capturar `System.out` y reutilizar la
  salida en un printer de archivo o de memoria.

Commits:

- `feat(document): expose formatted output as text`

### Ejemplo y documentación

- `examples/Main` muestra el uso de `toText()`.
- El README documenta `toText()`, `fromTable(...)` y `reader(...)`, y agrega
  `CsvReader` a la lista de puntos de extensión.

Commits:

- `docs: document builder extensions in README`

### Diagrama de clases

- Se regeneró `docs/diagrama-clases.drawio` para que coincida con el código:
  - `CsvDocument`: se agregó `+setCell(...)`, `+view()` y `+toText()`.
  - `CsvDocumentBuilder`: se agregaron los atributos `-table` y `-reader`, y los
    métodos `+fromTable(...)` y `+reader(...)`.
- La leyenda ahora se posiciona debajo de la caja más baja en vez de tener una
  coordenada fija, así que no se solapa cuando las cajas crecen.
- Se validó el resultado: 24 clases sin solapamientos, 28 flechas con segmentos
  ortogonales y ninguna ruta que cruce una caja o la leyenda.

Commits:

- `docs(diagram): update class diagram with new builder and document API`

## 2026-11-20 (organización)

- El README ahora indica el reparto real del equipo: José en arquitectura y
  núcleo, Miguel Luzardo en el informe y Santiago Salas en el resto.
- Los commits que solo tocaban el README se reescribieron para que Miguel
  Luzardo quede como autor y committer. El contenido de los archivos no cambió.

## Pendientes

- `MarkdownTableFormatter` y `FileTablePrinter`.
- Validaciones de encabezados duplicados y CSV de una sola fila.
- JavaDoc completo en la API pública.
