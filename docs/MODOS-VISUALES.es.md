# Modos visuales de Velira TV

Especificación y estado revisados el 1 de noviembre de 2026. La consolidación posterior aplica componentes comunes a Ajustes, biblioteca, galería y diálogos; [estado y evidencia](history/0.11.0/2026-11-01-consolidacion/README.md). Las reglas comunes están en [Sistema visual](SISTEMA-VISUAL.es.md). Los diseños son vistas reales, no capturas incrustadas. Código en desarrollo 0.11, sin release estable.

| Apartado | Implementado | Evidencia y pendientes |
| --- | --- | --- |
| Clásico | Fecha/hora, bandeja redondeada, seis favoritas, cuatro accesos y Ajustes único | Capturas del refinamiento anterior; fondos exactos de referencia no disponibles |
| Moderno | Dashboard, reloj único, seis favoritas y Entradas inferior | Apps y Explorar comparten biblioteca por intención de la tarjeta; no hay segunda barra |
| Inmersivo | Dock centrado sin bandeja, nombre inferior, halo y sombra | Sombras aproximadas, sin reflejos ni blur; probado anteriormente en 1080p |
| Ambiente | Hora y fecha diferenciadas, créditos reales, controles temporales, fondos/colecciones | Esta revisión añade opción de ocultar reloj/fecha e información completa al pulsar Arriba |
| Juegos | HDMI reales, nombres, mandos, panel TCL, navegador y opciones inferiores | Antes usaba desierto; esta revisión integra un fondo específico de mando, nuevo y generado |

## Revisión de los dos encargos anteriores

No estaban completos en todos sus criterios de aceptación. La composición básica y las capturas existen; no equivalen a validación completa de todas las integraciones. El fondo del mando no estaba implementado: el código y la captura archivada mostraban el desierto. La documentación de esa etapa lo indicaba, pero el resumen fue demasiado amplio.

Tampoco se habían completado: reloj opcional de Ambiente, unificación de todos los tokens, pruebas de todas las entradas/dispositivos, contraste en múltiples fondos, 4K ni benchmark equivalente. Se corrigen reloj opcional, información y tokens principales; los restantes se distinguen en la consolidación posterior entre implementación y validación externa.

## Comportamiento que se conserva

Inicio mantiene favoritas, preferencias y navegación con flechas, OK, Atrás e Inicio. Ambiente conserva filtros y fuentes elegidos, créditos permanentes, vídeo opcional y movimiento configurado. Su salvapantallas sigue sin interacción; en la vista previa izquierda/derecha cambia foto, Arriba muestra crédito completo y ayuda, OK/Atrás cierra. Las transiciones no aceptan otra foto hasta terminar, evitando solapamientos de las dos vistas.

Juegos mantiene los estados de Android, nombres personalizados y mantener OK. El fondo nuevo no sustituye ninguna colección de Ambiente ni el wallpaper de Inicio. No se confunde con una recuperación del original de referencia. Las herramientas TCL dependen de que el firmware las exponga.

## Evidencia conservada

[Archivo por versión y apartado](history/README.md). Cada etapa anterior conserva sus capturas y documentos por carpeta o commit. No se sustituyen retrospectivamente por las capturas nuevas.
