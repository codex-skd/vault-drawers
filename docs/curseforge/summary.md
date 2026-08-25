# Vault Drawers — Summary

**Port of Storage Drawers 13.11.4 to Minecraft 26.2 / NeoForge 26.2.0.57**

| Property | Value |
|----------|-------|
| **Mod ID** | `vault_drawers` |
| **Display Name** | Vault Drawers |
| **MC Version** | 26.2 |
| **NeoForge** | 26.2.0.57 |
| **Java** | 25 |
| **License** | MIT |
| **Original Author** | Texelsaur (Storage Drawers) |
| **Port Maintainer** | Stalking Dragons |

## Features Ported (1:1)

- **Storage Drawers**: 1/2/4 compartment, full/half blocks, all 11 wood types
- **Trim System**: Wood & metal variants
- **Upgrades**: Capacity (7 tiers), Void, Speed, Redstone, Magnet, Remote, Hopper, Illumination, Fill Level, Balance, Conversion, Portability, One Stack, Creative
- **Controller Network**: Controller + Controller IO + Slaves (512 drawer limit)
- **Integrations**: JEI, JADE, The One Probe, FTB Chunks, FTB Teams, Architectury
- **Configuration**: Full config via TOML (YACL-ready)
- **Creative Tab**: Custom tab with all non-framed items

## Structural Changes

- Package: `com.jaquadro.minecraft.storagedrawers` → `com.stalkingdragons.minecraft.vaultdrawers`
- Assets namespace: `storagedrawers:` → `vault_drawers:`
- Maven group: `com.texelsaurus.minecraft.storagedrawers` → `com.stalkingdragons.minecraft.vaultdrawers`
- JAR: `vault_drawers-26.2-neoforge-26.2.0.57-13.11.4.jar`

## Status

- ✅ F0: Setup (repo, gradle, structure, lib_ext)
- 🔄 F1: Core (porting common + neoforge → src/main)
- ⏳ F2–F7: Pending

## Key Files

- `docs/WORKFLOW_VAULT_DRAWERS_26-2.md` — Full workflow
- `docs/curseforge/project_vars.md` — CurseForge IDs/tokens
- `docs/curseforge/project_description.md` — HTML for CurseForge
- `gradle.properties` / `build.gradle` — Build config