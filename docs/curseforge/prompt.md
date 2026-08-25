# Vault Drawers — CurseForge Logo Image Prompt

## Ready-to-use prompt

```
A clean, modern Minecraft mod icon in isometric voxel style, centered and filling about 80% of the canvas, on a fully transparent background. The subject is a single stylized storage drawer block viewed at a slight 3/4 isometric angle: a 2x2 grid of four equal square compartments carved into warm oak wood (#8B7355 base, #6B5B4A shadows), each compartment framed by a small horizontal drawer-pull handle. The whole block is reinforced with a "vault" aesthetic — thick dark iron/steel banding (#2F2F2F, #4A4A4A) running along all four outer edges and corners, about 3 pixels thick, with a vertical iron strip down the center holding a glowing gold keyhole (#FFD700 metal, #FFAA00 soft glow) at the middle of the block. Lighting comes from the top-left, with subtle ambient occlusion in the seams between compartments and along the metal banding, giving the wood and metal a slight 3D voxel depth. The overall feel is heavy, secure and industrial but still warm and readable at small sizes. No text, no letters, no UI elements, no hands, no particles, no realistic photo textures, no background gradient or scenery — icon artwork only.
```

## Target output

- `icon.png` — 128×128, primary CurseForge/mod logo
- `icon_64.png` — same composition, verify it still reads clearly at 64×64 before finalizing

## Usage

Generate with the `gpt-image-2` skill (or any image model). After generating, save the result to `src/main/resources/assets/vault_drawers/icon.png`, and produce/verify a 64×64 downscale for CurseForge's small-size thumbnail before uploading.

## Reference

- Original Storage Drawers icon (for inspiration only, do not copy 1:1): `lib_ext/StorageDrawers-1.21/StorageDrawers-1.21/common/src/main/resources/assets/storagedrawers/icon.png`
- CurseForge logo requirements: PNG, transparent background, no text, works at both 128×128 and small thumbnail sizes.
