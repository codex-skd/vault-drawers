# Vault Drawers - Port Roadmap for Minecraft 26.2 / NeoForge 26.2.0.57

## Project Overview
- **Mod Name**: Vault Drawers
- **Mod ID**: vault_drawers
- **Target Version**: Minecraft 26.2 / NeoForge 26.2.0.57
- **Base Mod**: Storage Drawers 13.11.4 (1.21.1)
- **Repository**: https://gitlab.com/stalking-dragons/minecraft/vault-drawers.git

---

## Phase 1: Project Setup & Core Infrastructure ✅ COMPLETED

- Gradle project, Chameleon library adapted for 26.2, git repo + production branch.

## Phase 2: Java Source Port ✅ COMPLETED (2026-08-25)

All Java source (blocks, items, block entities, containers, capabilities, networking,
config, security, integration stubs, chameleon abstraction layer) ported from
`com.jaquadro.minecraft.storagedrawers` to `com.stalkingdragons.minecraft.vaultdrawers`
and migrated to the Minecraft 26.2 / NeoForge 26.2.0.57 API (see CHANGELOG.md for the
full list of API changes handled: ItemStack CODEC-based (de)serialization,
appendHoverText signature, CompoundTag/ListTag Optional-based NBT reads, BlockEntityType
construction, and more).

- `./gradlew.bat compileJava` and `./gradlew.bat build` both succeed with 0 errors.
- First beta JAR uploaded to CurseForge: `0.0.0-beta.1` (project 1668019).
- Mod icon (128×128 + 64×64) generated and wired into `neoforge.mods.toml`.

## Phase 3: Game Assets & Data ✅ COMPLETED (2026-08-25)

All 1057 asset/data files ported and namespaced (see CHANGELOG). Details below kept for reference.

## Phase 2.5: Main class wiring ✅ COMPLETED (2026-08-25)

`VaultDrawers.java` was a stub with all registry init() calls commented out — the mod
compiled but registered ZERO content at runtime. Fully wired now: config registration,
all registry init() calls, creative tab, capabilities, networking, event listeners.
Also ported 18 previously-unported support classes (ModRecipes + 5 custom recipes,
ModCreativeTabs, PlatformCapabilities + item handlers, ModNetworking, PlayerEventListener,
LocalIntegrationRegistry + integration base classes, PlatformResourceFactory + ServiceLoader
wiring) and fixed several more 26.2 API changes (mods.toml `type=` vs `mandatory=`, missing
`modLoader` field, `BlockBehaviour/Item.Properties.setId()`, `BlockCapability` construction,
`ClientPacketDistributor.sendToServer`, JEI maven repo + version).

**Verified**: `./gradlew.bat compileJava` and `./gradlew.bat runData` both pass with 0
errors — the mod fully constructs and registers all content.

**2026-08-25 update — runClient verified**: fixed a client-only crash
(`NullPointerException: Components not bound yet`, from constructing `ItemStack`s of
vanilla items too early during mod loading in `CompTierRegistry`/`StorageBlacklist`/
`MaterialBlacklist`/`ConversionRegistry`/`ConfigItemList` — fixed by storing `Item`
references and building stacks lazily) and a capability ID collision. `./gradlew.bat
runClient` now boots to the main menu and a singleplayer world loads and runs without
crashing. Released as `0.0.0-beta.3` on CurseForge.

**2026-08-25 update — recipes and item icons fixed**: what looked like minor cosmetic gaps
turned out to be two real, mod-wide bugs:
- All 159 items showed "Missing item model" (no icon anywhere) — 26.2 requires a new
  `assets/vault_drawers/items/<id>.json` "item definition" file per item, introduced after
  the 1.21.1 base. Generated all 159 missing files.
- ALL crafting recipes (121 of 126 files) failed to parse — the 1.21.1 object-form
  ingredient syntax (`{"item": "x"}` / `{"tag": "y"}`) is no longer valid in 26.2; fixed by
  converting to the current plain-string / `#`-prefixed tag shorthand format. Also added an
  empty fallback tag for the optional CoFH Core compat recipe so it doesn't error when CoFH
  Core isn't installed.
- The "missing texture: particle" warnings on ~30 indicator/overlay meta-block models are
  confirmed pre-existing behavior from the original 1.21.1 mod (not a port bug) — no fix
  needed.

Released as `0.0.0-beta.4` on CurseForge. Verified: 0 recipe-parse errors, 0 missing item
icons, no crash — a real playtest session ran cleanly.

### (historical) Phase 3 gap analysis — biggest remaining gap

**The mod currently has ZERO game assets or data.** `src/main/resources/assets/vault_drawers/`
only contains `icon.png`/`icon_64.png`. Without this phase the mod will not actually work
in-game (missing-texture purple/black checkerboard on every block, raw translation keys
instead of names, no crafting recipes).

Everything needs to be ported from the 1.21.1 reference source and re-namespaced from
`storagedrawers` to `vault_drawers` (plus checked for any 26.2 JSON schema changes —
block model / blockstate / recipe formats have changed across MC versions before, so this
is not a pure find-and-replace):

| Source (`lib_ext/StorageDrawers-1.21/.../assets/storagedrawers/`) | Count | Target |
|---|---|---|
| `blockstates/` | 144 files | `src/main/resources/assets/vault_drawers/blockstates/` |
| `models/` | 427 files | `src/main/resources/assets/vault_drawers/models/` |
| `textures/` | 267 files | `src/main/resources/assets/vault_drawers/textures/` |
| `lang/` | 12 files | `src/main/resources/assets/vault_drawers/lang/` |

| Source (`lib_ext/StorageDrawers-1.21/.../data/storagedrawers/`) | Count | Target |
|---|---|---|
| `recipe/` | 377 files | `src/main/resources/data/vault_drawers/recipe/` |
| `advancement/` | 52 files | `src/main/resources/data/vault_drawers/advancement/` |
| `tags/` | 13 files | `src/main/resources/data/vault_drawers/tags/` |
| `loot_table/` | 14 files | `src/main/resources/data/vault_drawers/loot_table/` |

**Total: ~1,306 files.** Namespace references inside the JSON files (`storagedrawers:...`)
must all become `vault_drawers:...`, and block/item IDs must match whatever registry names
`ModBlocks.java`/`ModItems.java` actually register in the ported Java code (verify, don't
assume 1:1 naming — the Java port may have renamed some entries).

## Phase 4: Client Rendering 🔄 PENDING

Beyond static models/textures: dynamic drawer face rendering (item preview on drawer
front), custom block entity renderers, model decorators. Java classes for this exist in
`src/main/java/.../client/`, `.../common/gui/` but were not verified for correctness
during the Phase 2 compile-only pass — needs runtime testing once Phase 3 assets exist.

## Phase 5: Integration & Compat 🔄 PENDING

JEI, JADE, The One Probe, FTB Chunks, FTB Teams — Java integration classes exist but are
untested. Recipes for JEI display depend on Phase 3's `data/vault_drawers/recipe/` being
in place first.

## Phase 6: Testing 🔄 PENDING

- `runClient` / `runServer` launch without crashing
- Drawer placement, item insertion/extraction, upgrades, trim, controller network
- Config sync client↔server
- No log errors/warnings for missing resources

## Phase 7: Release 🔄 IN PROGRESS

- [x] `0.0.0-beta.1` uploaded to CurseForge (compile-only beta, not yet in-game tested)
- [ ] CurseForge project page: summary + full description + logo pasted in manually (files ready in `docs/curseforge/`)
- [ ] Subsequent betas as Phases 3–6 complete
- [ ] First stable `X.Y.Z` release once fully tested

---

## Next Steps (priority order)

1. **Phase 6 full testing checklist** — drawer placement, item insert/extract, upgrades,
   trim, controller network, config sync, JEI recipe display. Needs real interactive
   play (manual or desktop-automation), not just log inspection.
2. **Phase 5 integrations** (JEI/JADE/The One Probe/FTB Chunks/FTB Teams) — currently
   NOT implemented at all beyond 2 stub interfaces; real compat modules exist unported in
   `client_backup/integration/`, and their compile dependencies (Jade, TOP, FTB, Architectury)
   aren't even resolved in `build.gradle` yet (only JEI is). Sizeable follow-up, optional/
   non-blocking for core functionality.
3. **Phase 4 client rendering deep-dive** — dynamic drawer face rendering, custom block
   entity renderers, model decorators; loads without error but not yet visually verified
   in detail (e.g. does the drawer front actually preview the stored item?).
4. **Next CurseForge beta** as the above complete.

---

## Build Commands

```bash
./gradlew.bat compileJava --no-daemon
./gradlew.bat build --no-daemon
./gradlew.bat runClient --no-daemon
./gradlew.bat runServer --no-daemon
```

---

*Last updated: 2026-08-25 — Phases 1-3 complete, main class wired, runClient verified
playable (recipes + item icons fixed). Phases 4 (deep verification), 5 (integrations), 6
(full gameplay testing checklist) remain.*
