# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.0.0-beta.7] - 2026-08-28

### Corregido
- **FerriteCore crash**: `IntegerSetProperty.getInternalIndex()` ahora retorna índice secuencial (0,1,2...) en lugar del valor real (1,2,4), compatible con `FastMap` de FerriteCore.
- **Crash en cascada**: El crash de FerriteCore durante registro de bloques impedía que bloques como `keybutton_drawer` y `meta_indicator` se registraran, causando errores "unbound value" en Items y BlockEntities. Al fixear el crash raíz, el registro completa correctamente.

## [0.0.0-beta.6] - 2026-08-28

### Corregido
- **Crash al iniciar con FerriteCore**: `IllegalStateException` en `BlockMeta.<init>` causado por `IntegerSetProperty` con orden no determinista. Fix: `getPossibleValues()` ahora retorna lista ordenada.
- **Crash por acceso a bloques no registrados**: `NullPointerException: Trying to access unbound value` para `keybutton_drawer` y `meta_indicator`. Fix: Items y BlockEntities ahora usan lookup perezoso (lazy) dentro del supplier de DeferredRegister, resolviendo bloques después de que estén registrados.

### Cambiado
- `ModItems.init()`: registro de BlockItems diferido vía `registerBlockLazy()`.
- `ModBlockEntities`: registro de block entity types con lookup de bloques dentro del supplier.

## [0.0.0-beta.5] - 2026-08-27

### Eliminado
- **Integraciones TOP y FTB (Chunks/Teams/Library)**: no existen builds para Minecraft 26.2 / NeoForge 26.2.0.57. Eliminadas declaraciones en `neoforge.mods.toml`, config (`ModCommonConfig.INTEGRATION`), y código stub en `LocalIntegrationRegistry`.
- **Referencias a WAILA** en config renombradas a **Jade** (nombre real del mod).

### Cambiado
- `ModCommonConfig.Integration` simplificado: solo sección `Jade` (antes `Waila` + `FTBChunks` + `FTBTeams`).
- `build.gradle` comentarios actualizados: FTB/TOP marcados como no disponibles en 26.2, Architectury solo transitivo vía Jade.

## [0.0.0-beta.4] - 2026-08-25

### Corregido
- **Todas las recetas de crafteo funcionan**: el formato de receta de 1.21.1 ya no lo acepta el parser de 26.2 — ninguna receta cargaba en las betas anteriores. Convertidas todas al formato de ingredientes actual
- **Todos los items muestran su icono correcto** en inventario/mano — 26.2 exige un fichero de "item definition" por item que faltaba para todos

### Estado
- ✅ Verificado: sesión de juego real con 0 errores de parseo de recetas y 0 iconos de item faltantes

## [0.0.0-beta.3] - 2026-08-25

### Corregido
- **Crítico**: el cliente crasheaba al arrancar (`NullPointerException: Components not bound yet`) — la beta.2 nunca llegaba al menú principal. Causado por construir `ItemStack` de items vanilla demasiado pronto durante la carga del mod; arreglado guardando referencias a `Item` y construyendo el stack de forma perezosa en el primer uso real
- Colisión de ID en el registro de una capability que podía crashear su setup

### Estado
- ✅ Verificado: el mod arranca al menú principal y una partida de un jugador carga y funciona sin crashear
- ⚠️ Quedan huecos cosméticos menores (texturas de algunos bloques indicador/overlay e iconos de varios tiers de upgrade de almacenamiento) — no afectan a la funcionalidad, pendiente de pulido

## [0.0.0-beta.2] - 2026-08-25

### Añadido
- Todos los assets y datos del mod portados desde Storage Drawers 1.21.1: blockstates, modelos, texturas, lang, recetas, loot tables, tags y advancements (1057 ficheros, namespace `vault_drawers`)
- Logo del mod

### Corregido
- **Crítico**: la clase principal tenía todo el registro de contenido comentado — el mod compilaba pero no registraba ningún bloque/item/receta/capacidad en runtime. Cableado completo ahora
- Varios cambios más de API de 26.2 descubiertos al ejecutar el mod (no solo compilarlo): dependencias opcionales que crasheaban el arranque, resolución de JEI, registro de capabilities, envío de paquetes cliente→servidor

### Estado
- ⚠️ El mod carga y registra todo su contenido (verificado con generación de datos automatizada), pero aún no se ha probado con el juego real (`runClient`)

## [0.0.0-beta.1] - 2026-08-25

### Añadido
- Primera beta pública en CurseForge del port de Storage Drawers 13.11.4 a Minecraft 26.2 / NeoForge 26.2.0.57
- El mod compila y empaqueta correctamente tras completar la migración de API a 26.2 (`ItemStack` CODEC, `appendHoverText`, NBT `Optional`, `BlockEntityType`, etc.)

### Estado
- ⚠️ Aún no verificado en juego (cliente/servidor, colocación de bloques, upgrades, red de controladores) — beta de compilación, no de funcionalidad probada

## [13.11.4] - 2024-08-21

### Added
- Initial port of Storage Drawers 13.11.4 to Minecraft 26.2 / NeoForge 26.2.0.57
- Mod ID changed to `vault_drawers` (was `storagedrawers`)
- Package refactored to `com.stalkingdragons.minecraft.vaultdrawers`
- All original features preserved:
  - Storage Drawers (1, 2, 4 compartments; full/half blocks; all wood types)
  - Trim system (wood/metal variants)
  - Upgrades (Capacity, Void, Speed; 1-4 slots)
  - Controller + Controller Slave network (512 drawer limit)
  - JEI integration (recipes for all items)
  - **Jade integration (block tooltips)** — *The One Probe / FTB integrations removed: not available for 26.2*
  - **Architectury** — *transitive via Jade only*
  - Full configuration system

### Changed
- Updated all mappings to Parchment 2024.07.07 for MC 26.2
- Updated NeoForge to 26.2.0.57
- Updated JEI to 20.1.0 for 26.2
- Namespace for assets changed from `storagedrawers:` to `vault_drawers:`
- Maven group changed to `com.stalkingdragons.minecraft.vaultdrawers`

### Credits
- Original mod: **Storage Drawers** by **Texelsaur** (MIT License)
- Port & maintenance: **Stalking Dragons**

---

*This is an independent port for NeoForge 26.2. All original assets, code logic, and design belong to Texelsaur.*