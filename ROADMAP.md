# Vault Drawers - Port Roadmap for Minecraft 26.2 / NeoForge 26.2.0.57

## Project Overview
- **Mod Name**: Vault Drawers
- **Mod ID**: vault_drawers
- **Target Version**: Minecraft 26.2 / NeoForge 26.2.0.57
- **Base Mod**: Storage Drawers 13.11.4 (1.21.4)
- **Repository**: https://gitlab.com/stalking-dragons/minecraft/vault-drawers.git

---

## Phase 1: Project Setup & Core Infrastructure ✅ COMPLETED

### ✅ Completed
- [x] Project structure created at `G:/Proyectos/Mods_Minecraft/vault-drawers/neoforge/26.2/`
- [x] Gradle build files (build.gradle, gradle.properties, settings.gradle)
- [x] Git repository initialized with initial commit
- [x] NeoForge mod metadata (neoforge.mods.toml template)
- [x] CI/CD pipeline (.gitlab-ci.yml)
- [x] Chameleon library ported (common + neoforge implementations)
  - [x] Fixed mappings: ResourceLocation → Identifier
  - [x] Fixed package names: com.jaquadro.minecraft.storagedrawers → com.stalkingdragons.minecraft.vaultdrawers
  - [x] Chameleon services (Registry, Config, Networking, Platform, Capabilities, Container)
- [x] Minimal VaultDrawers main class compiles
- [x] Build passes compilation

### 📝 Notes
- Chameleon library adapted for Minecraft 26.2 API changes
- Identifier used instead of ResourceLocation
- FMLEnvironment.dist handling simplified for 26.2

---

## Phase 2: Configuration System 🔄 IN PROGRESS

### Required Files (in client_backup, need porting)
- [ ] ModConstants.java - constants and MOD_ID
- [ ] ModCommonConfig.java - common config (forge config + chameleon)
- [ ] ModClientConfig.java - client config
- [ ] ConfigSpec.java - base config specification
- [ ] ConfigItemList.java - config list handling
- [ ] PlayerConfig.java / PlayerConfigSetting.java - per-player config
- [ ] CompTierRegistry.java - component tier registry
- [ ] ConversionRegistry.java - conversion registry
- [ ] MaterialBlacklist.java / StorageBlacklist.java - blacklists

### API Changes to Address
- [ ] Forge config API changes (ModConfig.Type, ModConfigEvent)
- [ ] Chameleon Config API integration
- [ ] DataComponentInput handling (moved/removed in 26.2)

---

## Phase 3: Core Game Content 🔄 PENDING

### Blocks
- [ ] Block base classes (BlockDrawers, BlockStandardDrawers, BlockCompDrawers, BlockTrim, BlockFramed*)
- [ ] Block entity classes (BlockEntityDrawers, BlockEntityDrawersStandard, BlockEntityDrawersComp, BlockEntityController, BlockEntityControllerIO, BlockEntityFramingTable, BlockEntityTrim)
- [ ] Block variants system (ModBlockVariants)
- [ ] Block meta classes (BlockMetaFacing, etc.)

### Block Entities (Major API Changes)
- [ ] DataComponentInput → DataComponentMap/ValueInput/ValueOutput migration
- [ ] applyImplicitComponents / collectImplicitComponents signatures changed
- [ ] saveWithId → saveWithoutMetadata / saveWithId signature changes
- [ ] BlockEntityType registration

### Items
- [ ] ItemDrawers, ItemDetachedDrawer, ItemKey, ItemKeyring, ItemUpgrade*, ItemUpgradeStorage, ItemUpgradeRemote
- [ ] Creative tab registration
- [ ] Tooltip handling

---

## Phase 4: Inventory & Containers 🔄 PENDING

### Required Files
- [ ] ContainerDrawers (base + 1/2/4/Comp variants)
- [ ] ContainerFramingTable
- [ ] DrawerScreen, FramingTableScreen (client)
- [ ] SlotUpgrade, custom slot handling
- [ ] MenuType registration via chameleon

### API Changes
- [ ] AbstractContainerMenu changes
- [ ] MenuProvider changes
- [ ] Screen registration (ModMenuScreens → MenuScreens)

---

## Phase 5: Capabilities & Networking 🔄 PENDING

### Capabilities
- [ ] IDrawerAttributes / IDrawerGroup / IItemRepository
- [ ] Capabilities registration (Capabilities.java)
- [ ] PlatformCapabilities (Neoforge capability registration)
- [ ] Custom capabilities (DrawerItemRepository, DrawerItemHandler, PlatformDrawerItemHandler)

### Networking
- [ ] MessageHandler / CountUpdateMessage / PlayerBoolConfigMessage
- [ ] NeoforgeNetworking (PacketDistributor API changes in 26.2)
- [ ] StreamCodec / CustomPacketPayload for 26.2

---

## Phase 6: Client Rendering 🔄 PENDING

### Required Files
- [ ] BlockEntityDrawersRenderer, BlockEntityFramingRenderer
- [ ] DrawerModelStore, DrawerModelGeometry
- [ ] Model decorators (DrawerModelDecorator, MaterialModelDecorator, CombinedModelDecorator)
- [ ] ClientModBusSubscriber (model baking, texture stitching, renderer registration)
- [ ] Tooltip components (ClientDetachedDrawerTooltip, ClientKeyringTooltip)

### Major API Changes
- [ ] BlockModelShaper → ModelManager changes
- [ ] BakedModel / ModelResourceLocation / ModelBakery changes
- [ ] RenderType / BufferSource changes
- [ ] TextureAtlasStitchedEvent / ModelEvent.ModifyBakingResult changes

---

## Phase 7: Integration & Compat 🔄 PENDING

### Mods to Support
- [ ] FTB Chunks / FTB Teams
- [ ] The One Probe / Waila (Jade)
- [ ] Curios / Baubles
- [ ] JEI / REI
- [ ] Create / Mekanism / Thermal / AE2 / RS

### Integration Framework
- [ ] IntegrationRegistry / LocalIntegrationRegistry
- [ ] IntegrationModule base class
- [ ] IMC handling for mod detection

---

## Phase 8: World Gen & Data 🔄 PENDING

### Features
- [ ] Loot tables for drawers
- [ ] Recipes (upgrade, framing, detached drawer)
- [ ] Tags (blocks, items)
- [ ] Advancements

---

## Phase 9: Testing & Polish 🔄 PENDING

### Testing Checklist
- [ ] Dedicated server startup
- [ ] Client + server connection
- [ ] Single player world creation
- [ ] Drawer placement & interaction
- [ ] Upgrade installation
- [ ] Framing table usage
- [ ] Controller / Controller IO functionality
- [ ] Key / Keyring functionality
- [ ] Creative tab items
- [ ] JEI integration
- [ ] Config sync (client ↔ server)

### Performance
- [ ] Block entity ticking optimization
- [ ] Network packet optimization
- [ ] Rendering optimization

---

## Minecraft 26.2 Specific Migration Notes

### Key API Changes (from 1.21.4 to 26.2)
| Old (1.21.4) | New (26.2) |
|--------------|------------|
| ResourceLocation | Identifier |
| DataComponentInput | DataComponentMap / ValueInput / ValueOutput |
| saveWithId(RegistryAccess) | saveWithId(RegistryAccess) / saveWithoutMetadata(RegistryAccess) |
| BlockModelShaper.stateToModelLocation | ModelManager.getModelLocation |
| BakedModel / ModelBakery | ModelManager / BakedModel (changed packages) |
| PacketDistributor.sendToServer | PacketDistributor.sendToServer (signature changed) |
| FMLEnvironment.dist | Removed/changed - use Dist.CLIENT check |
| ItemInteractionResult | InteractionResult |
| DirectionProperty | EnumProperty<Direction> |
| BlockStateProperties.HORIZONTAL_FACING | Still exists but as EnumProperty |
| displayClientMessage | sendSystemMessage |
| inventory.selected | inventory.getSelectedSlot() |
| level.random | level.getRandom() |
| level.isClientSide | level.isClientSide() |
| CompoundTag.getList(name, type) | getList(name) returns ListTag |

### Chameleon Library Adaptations
- All `com.jaquadro.minecraft.storagedrawers` → `com.stalkingdragons.minecraft.vaultdrawers`
- `com.texelsaurus.minecraft.chameleon` → `com.stalkingdragons.minecraft.vaultdrawers.chameleon` (common) / `chameleon_neoforge` (neoforge)
- ConfigSpec uses Forge's ModConfig API
- ChameleonConfig.Type.COMMON / CLIENT for config types

---

## File Status Summary

### ✅ Compiling (Minimal)
- VaultDrawers.java (main mod class)
- ModConstants.java
- Chameleon library (common + neoforge)
  - Registry, Config, Networking, Platform, Capabilities, Container
  - Neoforge implementations (NeoforgeRegistry, NeoforgeConfig, NeoforgeContainer, NeoforgeCapabilities, NeoforgePlatform, NeoforgeRegistries, NeoforgeNetworking)

### 📦 In client_backup (Need Porting)
All core mod files moved to `client_backup/` directory:
- Core: ModBlocks, ModItems, ModBlockEntities, ModContainers, ModBlockEntities, ModDataComponents, ModRecipes, ModNetworking, ModBlockVariants, ModCreativeTabs, ModServices, ModCommonConfig, ModClientConfig, ModSecurity, PlayerEventListener, ModBlockVariants
- Blocks: Block*, BlockMeta*, BlockEntity*
- Items: Item*
- Inventory: Container*, *Screen
- Client: ClientModBusSubscriber, *Renderer, *Model*, *Gui*, *Tooltip
- Capabilities: Capabilities, PlatformCapabilities, *ItemHandler
- Network: *Message*, *Handler
- Storage: StorageUtil, *Data
- Integration: *Registry, *Events, *Module, Waila, TheOneProbe, FTB*
- Util: *Helper, *Formatter, WorldUtils, ComponentUtil
- API: storage, framing, security, config interfaces
- Components: *Data components

---

## Next Steps (Priority Order)

1. **Port Configuration System** - ModCommonConfig, ModClientConfig, ConfigSpec
2. **Port Core Registry Classes** - ModBlocks, ModItems, ModBlockEntities (stubs first)
3. **Port Block/Item Base Classes** - Minimal implementations to register
4. **Port Block Entities** - With 26.2 DataComponent API changes
5. **Port Inventory/Containers** - Basic container + screen
6. **Port Capabilities/Networking** - Core functionality
7. **Port Client Rendering** - Model system, renderers
8. **Port Integration Framework** - Mod compatibility
9. **Full Testing & Polish**

---

## Build Commands

```bash
# Compile only
./gradlew.bat compileJava --no-daemon

# Full build
./gradlew.bat build --no-daemon

# Run client
./gradlew.bat runClient --no-daemon

# Run server
./gradlew.bat runServer --no-daemon
```

---

## Resources

- [NeoForge 26.2 Documentation](https://docs.neoforged.net/docs/26.2/)
- [Minecraft 26.2 Mappings](https://github.com/NeoForged/Structurize/wiki/26.2-Mappings)
- [Storage Drawers Source](https://gitlab.com/stalking-dragons/minecraft/storage-drawers)
- [Chameleon Library](https://github.com/jaquadro/Chameleon)

---

*Last Updated: $(date)*
*Status: Phase 1 Complete - Minimal Build Successful*