# Sistema visual de Velira TV

Consolidación de código del 1 de noviembre de 2026. Java y vistas Android nativas; sin migración de framework. Las validaciones externas se separan del trabajo implementado.

## Fuente común

`VisualTokens` define la paleta, tipografía base, radios y tiempos; `AuraTheme` compone superficies y estados; `VisualUi` aplica texto, botones, mensajes y ventanas. Los colores XML del tema nativo se verifican contra VisualTokens. `DesignMetrics` calcula una única escala con márgenes seguros para el lienzo 960×540. Son unidades de diseño, no dp físicos.

| Rol | Token / valor |
| --- | --- |
| Fondo / superficie | CARBON #151921 / GRAPHITE #252C36 |
| Borde / auxiliar | SLATE #515D69 |
| Texto / secundario | INK #F5F7FA / MUTED #B8C1CC |
| Foco / Juegos | ACCENT #96C9DD / VIOLET #8375C9 |
| Correcto / aviso / error | #9DEBB5 / #E9C77D / #FFA5A5 |
| Título / diálogo / sección | 30 / 24 / 17 |
| Etiqueta / cuerpo / secundario / detalle | 16 / 14 / 12 / 11 |
| Radio botón / tarjeta | 14 / 22 |
| Foco Inicio | 140 / 190 / 200 ms según nivel |
| Foco Juegos | 160 ms, escala 1,025 |

Estados compartidos: normal, foco, pulsado, seleccionado y deshabilitado. Seleccionado tiene borde propio; foco añade una señal geométrica cuando los efectos lo permiten. Sin efectos se conserva el borde. Los componentes nativos de edición, permisos y WebView conservan su comportamiento accesible de Android.

## Aplicación por apartado

- Inicio y sus tres modos: textos comunes, escala común y variantes propias de composición.
- Centro de control y listas de Ajustes: tema común y ventana configurada antes de mostrarla, sin redimensionarla después.
- Biblioteca: cuadrícula reutilizable, IDs por paquete, selección conservada al cambiar la lista, estados vacío/carga/error y reintento.
- Colecciones y secundarios: GameUi comparte estilos, selección de pestaña y estado deshabilitado; catálogo ausente muestra explicación.
- Galería anterior: fondo y botones comunes, lectura de archivos fuera de UI, cancelación y reentrada recuperables.
- Imagen TCL: botones, texto y superficie comunes; lectura/cambio nativos se conservan.
- Diálogos: VisualUi.Builder unifica ventanas, listas, botones y retorno del foco al control de origen. No sustituye permisos ni teclados del sistema.

## Excepciones de diseño

El tamaño del reloj, tipografía ligera de Ambiente y los títulos hero se conservan para su jerarquía propia. Las coordenadas de composición, tamaños de iconos y proporciones de cada preset siguen locales; no se convierten en constantes globales sin reutilización real. Los sitios web mantienen su propio diseño, y los mensajes Toast y el teclado son nativos. Ambiente conserva dos imágenes y las opciones independientes de movimiento/transición; no adopta las tarjetas de Inicio. Juegos decodifica su fondo RGB565 fuera de UI y no crea otra caché estática.

## Validación y límites

El contrato JVM verifica contraste mínimo 4,5:1 del texto principal, secundario y colores semánticos sobre las superficies sólidas normal y de foco definidas; no certifica cualquier píxel de un wallpaper. Verifica escala acotada a 1080p y 4K, además de formatos no 16:9. Esto es cálculo de geometría, no prueba física 4K.

Las regresiones en C645 comprueban foco de los tres presets, biblioteca/Ajustes, parser real de copias, reentrada de galería y colecciones locales. No utilizan vídeo ni descargas ni cambian ajustes de imagen de TCL. Conservan/restauran preferencias de prueba. [Evidencia de la consolidación](history/0.11.0/2026-11-01-consolidacion/README.md).

La incidencia histórica de system_server no está resuelta por estas pruebas. Tampoco sustituyen un mando físico, señal HDMI externa, otros modelos ni una prueba prolongada de estabilidad. [Modos](MODOS-VISUALES.es.md) · [Archivo por versión](history/README.md).
