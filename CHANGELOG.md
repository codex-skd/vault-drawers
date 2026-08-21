# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
  - JADE / The One Probe integration (tooltips)
  - FTB Chunks integration (claim-based access)
  - FTB Teams integration (team-based access)
  - Architectury API compatibility layer
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