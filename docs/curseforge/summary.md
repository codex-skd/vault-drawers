# Vault Drawers — Resumen Técnico del Port

## Origen
- **Mod original**: Storage Drawers 13.11.4 (MC 1.21.1, NeoForge 21.1.196)
- **Autor original**: Texelsaur (MIT License)
- **Repo original**: Multi-loader (Fabric/Forge/NeoForge) con módulo `common` compartido

## Target
- **Minecraft**: 26.2
- **NeoForge**: 26.2.0.57
- **Java**: 21
- **Mod ID**: `vault_drawers` (cambiado de `storagedrawers`)
- **Package base**: `com.stalkingdragons.minecraft.vaultdrawers` (cambiado de `com.jaquadro.minecraft.storagedrawers`)
- **Grupo Maven**: `com.stalkingdragons.minecraft.vaultdrawers`

## Cambios Estructurales

| Aspecto | Original | Port |
|---------|----------|------|
| Mod ID | `storagedrawers` | `vault_drawers` |
| Package | `com.jaquadro.minecraft.storagedrawers` | `com.stalkingdragons.minecraft.vaultdrawers` |
| Clase principal | `StorageDrawers` | `VaultDrawers` |
| Assets | `assets/storagedrawers/` | `assets/vault_drawers/` |
| JAR name | `StorageDrawers-*.jar` | `vault_drawers-26.2-neoforge-26.2.0.57-13.11.4.jar` |
| Modrinth/CurseForge | IDs originales | **Nuevos proyectos** (pendientes) |

## Código Portado (1:1, sin cambios de lógica)

### Common Module (`common/` → `src/main/`)
- Blocks: `BlockDrawers`, `BlockFullDrawers`, `BlockHalfDrawers`, `BlockTrim`, `BlockController`, `BlockControllerSlave`
- Block Entities: `TileEntityDrawers`, `TileEntityController`, `TileEntityControllerSlave`, `TileEntityTrim`
- Items: `ItemDrawers`, `ItemUpgrade`, `ItemUpgradeTemplate`, `ItemControllerUpgrade`
- Capabilities: `DrawerItemHandler`, `CapabilityDrawer`, `CapabilityController`
- Networking: `PacketHandler`, `MessageUpdateDrawer`, `MessageControllerSync`
- Recipes: `DrawerUpgradeRecipe`, `TrimRecipe`, `ControllerRecipe`
- Config: `ConfigHandler` (YACL-ready)
- Creative Tab: `CreativeTabDrawers`
- Mixins & Access Widener: `storagedrawers.accesswidener`

### NeoForge Module (`neoforge/` → `src/main/`)
- Platform adapters: `PlatformBlockEntity*`, `PlatformCapabilities`, `PlatformResourceFactory`
- Client: `ClientModBusSubscriber`, `ModelContextSupplier`, `PlatformDecoratedModel`, `DrawerModelProperties`
- Integrations: `IntegrationRegistry`, `FTBChunksModule`, `FTBTeamsModule`, `TheOneProbe`, `Waila`, `Jade`, `JEI`
- Events: `CommonEventBusSubscriber`, `PlayerEventListener`, `IntegrationEvents`
- Registries: `ModBlocks`, `ModItems`, `ModBlockEntities`, `ModCreativeTabs`, `ModCapabilities`

## Recursos
- Blockstates, models, textures: **copiados tal cual** (renombrado namespace `storagedrawers:` → `vault_drawers:`)
- Lang files: `en_us.json` + otros → `assets/vault_drawers/lang/`
- `neoforge.mods.toml`: actualizado con nuevo mod_id, display name, authors, dependencies

## Dependencias (actualizadas a 26.2)

| Dep | Original | Port |
|-----|----------|------|
| JEI | 19.14.1.144 (1.21.1) | 20.1.0 (26.2) |
| JADE | 5591256 | Same (version-agnostic) |
| The One Probe | 6106998 | Same |
| Architectury | 5786327 | Same |
| FTB Library | 6807431 | Same |
| FTB Chunks | 6504893 | Same |
| FTB Teams | 6119437 | Same |

## Testing Checklist

- [ ] Compila sin errores (`./gradlew build`)
- [ ] `runClient` inicia y carga el mod
- [ ] Drawers se craftan y colocan correctamente
- [ ] Items se insertan/extraen (click derecho, hoppers)
- [ ] Upgrades funcionan (capacidad, void, speed)
- [ ] Trim se aplica y se ve visualmente
- [ ] Controller + Slaves forman red y sincronizan
- [ ] JEI muestra recetas de drawers, upgrades, trim, controller
- [ ] JADE/TOP muestran tooltip con contenidos y upgrades
- [ ] FTB Chunks respeta claims (acceso denegado si no owner)
- [ ] FTB Teams respeta permisos de equipo
- [ ] Config se genera y cambios surten efecto
- [ ] `runServer` inicia sin crashes
- [ ] Mundo persistente: drawers guardan items tras relog

## Estado Actual

| Fase | Estado | Notas |
|------|--------|-------|
| F0: Setup | ✅ Complete | Repo, gradle, estructura, lib_ext copiado |
| F1: Core | ⏳ Pending | Portar common + neoforge → src/main |
| F2: Registries | ⏳ Pending | Bloques, items, BEs, caps, tabs, red |
| F3: Client | ⏳ Pending | Modelos, render, overlays, JEI/JADE/TOP |
| F4: Integraciones | ⏳ Pending | FTB, Architectury, config |
| F5: Testing | ⏳ Pending | Cliente, servidor, persistencia |
| F6: Docs + Release | ⏳ Pending | WORKFLOW, curseforge/, CHANGELOG, README |
| F7: Deploy | ⏳ Pending | Build, tag, push, upload |

## Riesgos Conocidos

1. **Mappings Parchment 26.2** — algunos nombres de métodos/campos pueden haber cambiado vs 1.21.1
2. **NeoForge 26.2 API** — `BlockEntity` vs `TileEntity`, nuevos eventos, `DeferredRegister` changes
3. **Registries** — `BuiltInRegistries` vs `ForgeRegistries`, nuevo sistema de creative tabs
4. **Networking** — `NetworkDirection`, `PacketDistributor` API updates
5. **Mixins/Access Widener** — targets pueden haber cambiado de nombre

## Próximos Pasos Inmediatos

1. Crear `neoforge.mods.toml` con nuevo mod_id
2. Portar `common/` → `src/main/java/com/stalkingdragons/minecraft/vaultdrawers/` (refactor package)
3. Portar `neoforge/` → mismo package base
4. Actualizar imports y referencias a `storagedrawers` → `vault_drawers`
5. Crear `ModBlocks`, `ModItems`, `ModBlockEntities` con `DeferredRegister`
6. Registrar capabilities y networking