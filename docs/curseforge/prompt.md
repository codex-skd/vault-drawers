# Vault Drawers — Logo Generation Prompt for CurseForge

## AI Image Generation Prompt

> **Style**: Clean, modern Minecraft mod icon (64x64 or 128x128), isometric voxel aesthetic, high contrast for small sizes
> **Subject**: A stylized drawer block with vault-style reinforced framing — 2x2 compartment drawer front with thick metal corners and center lock plate, wood texture (oak) with dark iron banding
> **Palette**: Oak wood (#8B7355, #6B5B4A), Dark iron/steel (#2F2F2F, #4A4A4A), Gold accent (#FFD700) for lock/keyhole, subtle glow (#FFAA00) on keyhole
> **Lighting**: Top-left directional light, slight ambient occlusion in compartment seams
> **Background**: Transparent (for CurseForge icon)
> **Composition**: Centered, front-facing with slight 3/4 isometric tilt, fills 80% of canvas
> **Details**: 
> - 4 equal compartments visible (2x2 grid)
> - Reinforced metal frame at corners (3px thick)
> - Center vertical metal strip with keyhole
> - Subtle drawer pull handles on each compartment (small horizontal bars)
> - "Vault" aesthetic: heavy, secure, industrial but warm wood
> **Negative**: No text, no UI elements, no hands, no particles, no realistic photo style, no gradient backgrounds

## Variants

| Variant | Prompt Adjustment |
|---------|-------------------|
| **Main (128x128)** | Full detail as above |
| **Small (64x64)** | Simplified: remove keyhole detail, thicker lines, bolder contrast |
| **Banner (468x60)** | Horizontal: 3 drawers side-by-side, "VAULT DRAWERS" text in pixel font below, dark parchment background |
| **Animated** | Keyhole glows pulse (1s cycle), subtle drawer "breathing" (1px scale) |

## Reference Assets

- Original Storage Drawers icon: `lib_ext/StorageDrawers-1.21/StorageDrawers-1.21/common/src/main/resources/assets/storagedrawers/icon.png`
- Minecraft drawer block texture: `assets/minecraft/textures/block/oak_planks.png` + custom framing overlay
- NeoForge mod icon guidelines: 128x128 PNG, transparent BG, no text

## Generation Notes

- Use **GPT-Image-2** (ChatGPT Images 2.0) via `gpt-image-2` skill
- Iterate: generate → review at 64x64 → refine contrast/readability
- Final deliverable: `icon.png` (128x128) + `icon_64.png` (64x64) in `src/main/resources/assets/vault_drawers/`

## Brand Keywords

`storage` `drawers` `vault` `secure` `organization` `minecraft` `mod` `neoforge` `wood` `metal` `industrial` `clean` `readable`