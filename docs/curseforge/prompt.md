# Vault Drawers — Prompt de Despliegue / Continuidad

## Contexto Rápido para Nueva Sesión

> **Proyecto**: Vault Drawers (port de Storage Drawers a MC 26.2 / NeoForge 26.2.0.57)
> **Mod ID**: `vault_drawers`
> **Package**: `com.stalkingdragons.minecraft.vaultdrawers`
> **Rama**: `minecraft/26.2/neoforge-26.2.0.57/production`
> **Repo**: `https://gitlab.com/stalking-dragons/minecraft/vault-drawers.git`
> **Directorio**: `G:\Proyectos\Mods_Minecraft\vault-drawers\neoforge\26.2\`

## Archivos Clave a Leer

1. `docs/WORKFLOW_VAULT_DRAWERS_26-2.md` — Workflow completo (este archivo es la fuente de verdad)
2. `docs/curseforge/project_vars.md` — IDs, tokens, parámetros de upload
3. `docs/curseforge/summary.md` — Resumen técnico del port (qué se portó, checklist, riesgos)
4. `docs/curseforge/project_description.md` — HTML para CurseForge
5. `gradle.properties` — Versiones, mod_id, group, etc.
5. `build.gradle` — Config NeoForge 26.2.0.57
6. `src/main/resources/META-INF/neoforge.mods.toml` — Metadatos del mod

## Comandos Esenciales

```bash
# Build
./gradlew.bat clean build

# Dev client
./gradlew.bat runClient

# Dev server
./gradlew.bat runServer

# Verificar JAR
ls build/libs/

# Git workflow
git status
git add -A
git commit -m "tipo: descripción

v<version>"
git push

# Tag release
git tag -a 26.2-neoforge-<version> -m "v<version>: descripción"
git push origin <tag>

# Graphify
GRAPHIFY="C:\Users\llagu\AppData\Local\Packages\PythonSoftwareFoundation.Python.3.13_qbz5n2kfra8p0\LocalCache\local-packages\Python313\Scripts\graphify.exe"
"$GRAPHIFY" update . --force
git add graphify-out/ && git commit -m "chore: update knowledge graph" && git push

# CurseForge upload
powershell -File ../codex-docs/scripts/curseforge-upload.ps1
```

## Estado Actual del Port

**Fase**: F1 (Core) — Por empezar
**Bloqueo**: Ninguno
**Próxima tarea**: Portar `lib_ext/neoforge/src` + `lib_ext/common_src` → `src/main/` con refactor de package `com.jaquadro.minecraft.storagedrawers` → `com.stalkingdragons.minecraft.vaultdrawers`

## Referencias Originales (lib_ext/)

- `lib_ext/neoforge/src/` — Código NeoForge original (165 archivos .java)
- `lib_ext/common_src/` — Código common original (mixins, access widener, bloques, items, BEs, caps, red, recetas, config)
- **Úsalo como referencia exacta** — copiar lógica 1:1, solo cambiar package, mod_id, namespace assets

## Decisiones de Diseño (No Cambiar)

- ✅ Port **idéntico** al original — nada de features nuevas
- ✅ Mod ID `vault_drawers` (snake_case)
- ✅ Package `com.stalkingdragons.minecraft.vaultdrawers`
- ✅ Display name "Vault Drawers" (Title Case)
- ✅ JAR: `vault_drawers-26.2-neoforge-26.2.0.57-13.11.4.jar`
- ✅ Assets namespace: `vault_drawers:`
- ✅ NeoForge 26.2.0.57 fijo
- ✅ Java 21
- ✅ MIT License (igual que original)
- ✅ Créditos a Texelsaur en README, description, credits

## Si el Usuario Pide "Continuar Port"

1. Leer `docs/curseforge/summary.md` → sección "Próximos Pasos Inmediatos"
2. Empezar por crear `neoforge.mods.toml` si no existe
3. Refactor package en `src/main/java/...`
4. Registrar bloques/items/BEs con `DeferredRegister`
5. Seguir fases F1→F7 en orden

## Si el Usuario Pide "Deploy a CurseForge"

1. Verificar `project_vars.md` tenga `project_id` y `api_token`
2. `./gradlew.bat clean build`
3. Crear `docs/curseforge/versions/<version>.md` con HTML changelog
4. Actualizar `CHANGELOG.md`
5. Commit + tag + push
6. `powershell -File ../codex-docs/scripts/curseforge-upload.ps1`

## Contacto / Soporte

- Codex Stalking Dragons: https://codex.skdragons.com/
- Scripts compartidos: `../codex-docs/scripts/`
- Referencias: `../codex-docs/reference/` (CURSEFORGE.md, GRAPHIFY.md, REPO_SETUP.md)