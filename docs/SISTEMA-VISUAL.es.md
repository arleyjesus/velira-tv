# Sistema visual de Velira TV

Estado al 1 de noviembre de 2026: consolidación parcial implementada. No equivale a haber completado las cuatro fases propuestas. Java y vistas nativas; no Compose ni cambio de framework.

## Tokens implementados en AuraTheme

| Token | Valor |
| --- | --- |
| CARBON | #151921 |
| GRAPHITE | #252C36 |
| SLATE | #515D69 |
| INK | #F5F7FA |
| MUTED | #B8C1CC |
| ACCENT | #96C9DD |
| VIOLET | #8375C9 |
| SUCCESS / WARNING / ERROR | #9DEBB5 / #E9C77D / #FFA5A5 |
| DISABLED | #7C8793 |

Tokens semánticos definidos no significan que todos los mensajes ya los utilicen. Las superficies compartidas tienen estados normal, foco, pulsado, seleccionado y deshabilitado; seleccionado conserva la superficie normal y la pantalla indica su selección mediante texto. El foco mantiene borde y escala/elevación cuando están activadas. Deshabilitado atenúa la superficie y Android impide activarla.

Inicio usa focusMs y focusScale centralizados: 140/190/200 ms y escala 1,025/1,05/1,065. Juegos conserva 160 ms y 1,025. Desactivados y el interruptor de animación se respetan. BUTTON_RADIUS=14 y CARD_RADIUS=22 son valores del lienzo; falta migrar todos los radios y tipografías locales. Las unidades del lienzo 960×540 no se presentan como dp físicos.

## Componentes y excepciones

- AuraTheme: superficies, paleta y tiempos compartidos.
- HomeAppSurface: halo estático y sombra de iconos; sin blur en tiempo real.
- GameUi: botones comunes a Juegos y pantallas auxiliares.
- Ambiente conserva dos imágenes, crédito dinámico y reloj propio. No recibe bandejas de Inicio. Fundidos y movimiento dependen de sus ajustes existentes.
- Juegos usa un fondo propio, decodificado una vez por creación en su trabajador y sin otra caché estática; RGB565, aproximadamente 3 MiB de píxeles para 1672×941. El APK añade 2023388 bytes de recurso PNG antes del empaquetado; no se promete un ahorro de RAM.

## Pendientes por fase

1. Fase 1 parcial: falta unificar toda la tipografía, dimensiones, mensajes y estados semánticos; validar contraste sobre más fondos y equipos.
2. Ajustes y biblioteca: reutilizan parte del tema, pero falta una migración completa y prueba de todos sus estados.
3. Galería y secundarios: faltan auditoría completa, restauración de foco en todos los flujos y uniformidad de diálogos.
4. Pruebas reales: las capturas anteriores solo acreditan las pantallas y versión documentadas. No se validó 4K ni una comparación equivalente de rendimiento. La incidencia previa de system_server sigue abierta.

[Particularidades de los modos](MODOS-VISUALES.es.md) · [Archivo por versión](history/README.md).
