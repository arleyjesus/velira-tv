# Cómo seguir creciendo Velira TV

## Implementado en 0.12

- Diseño: selector de presets con esquemas ligeros, manteniendo tres composiciones distintas.
- Biblioteca: consulta local, filtro de favoritas y densidad ajustable, sin servicios de fondo.
- Arquitectura: LibraryQuery independiente de Android; LibraryOptions comparte el contrato de preferencias/copia; PresetPicker conserva los componentes comunes. Una prueba JVM de consulta se incorpora a CI.

## Siguientes líneas de producto

1. Organización local: estudiar carpetas o colecciones manuales de apps y accesos contextuales. Diseñar primero su navegación con mando y su formato de copia.
2. Personalización: acentos de color y ajustes de tamaño de texto, con contraste comprobado por cada combinación. Evitar temas que multipliquen fondos decodificados o capas.
3. Distribución: cerrar primero la incidencia nativa y ampliar la matriz real de compatibilidad antes de promover una candidata. Mantener APK firmada, notas y capturas por versión.

Son propuestas para versiones futuras, no funciones ya implementadas. Vídeo adaptativo, integración oficial con YouTube o widgets remotos requieren decisiones y mediciones separadas. No se añaden servicios por anticipado.

Cada función nueva debe definir su comportamiento sin red, foco al entrar/salir, migración de preferencias, límites de memoria y una prueba concreta. Conservar Java/vistas nativas mientras otra tecnología no resuelva una necesidad medida.

[Funciones de 0.12 y evidencia](history/0.12.0/2026-11-02-biblioteca/README.md).
