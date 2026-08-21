# Flujo de trabajo — Vault Drawers (NeoForge 26.2)

> **Versión del workflow**: 1.0.0
> Este archivo pertenece al proyecto **Vault Drawers**. Cambios aquí solo afectan a este proyecto.
> **Trabaja directamente con este archivo**: es el workflow operativo del mod, autocontenido.

## Específico del mod

| Dato | Valor |
|---|---|
| Mod ID (`gradle.properties`) | `vault_drawers` |
| Clase principal | `VaultDrawers` |
| Display name (Title Case) | `Vault Drawers` |
| Versiones de Minecraft | `26.2` |
| Rama | `minecraft/26.2/neoforge-26.2.0.57/production` |
| Repo GitLab | `https://gitlab.com/stalking-dragons/minecraft/vault-drawers.git` |
| Port base | Storage Drawers 13.11.4 (MC 1.21.1 NeoForge 21.1.196) |

## Convenciones de nomenclatura

| Convención | Uso | Ejemplo |
|---|---|---|
| **snake_case** | `mod_id`, assets/, packages Java | `vault_drawers` |
| **PascalCase** | Clases Java principales | `VaultDrawers` |
| **camelCase** | Variables, métodos, config keys | `vaultDrawersConfig` |
| **Title Case** | Display name (README, CHANGELOG, docs, CurseForge) | `Vault Drawers` |

## Organización y ramas

- Un repo GitLab por mod, una rama `minecraft/<mc>/neoforge-<neo>/production` por versión. Este clon local trabaja en la rama `production` de esta versión.
- Carpetas: `<mod_id>/<framework>/<mc-version>/` — este clon vive en `vault-drawers/neoforge/26.2/`.
- `*/main` y CI/CD: setup único al crear el repo — no releer ni modificar.

## Estructura del proyecto

`build.gradle` · `gradle.properties` (mod_id, mod_version, mod_group_id, mod_framework) · `settings.gradle` · `src/main/java/com/stalkingdragons/minecraft/vaultdrawers/` · `src/main/resources/assets/vault_drawers/` · `META-INF/neoforge.mods.toml` · `libs/` (versionado) · `lib_ext/` y `temp/` (no versionados) · `docs/` (WORKFLOW + curseforge/) · `CHANGELOG.md` · `README.md` · `graphify-out/` (versionado).

## Versionado

- Beta `0.0.0-beta.X` · Release `X.Y.Z` (SemVer: MAJOR breaking / MINOR feature / PATCH fix)
- `mod_version` y `mod_framework` en `gradle.properties`. JAR: `<mod_id>-<mc>-<framework>-<loader>-<version>.jar`

## Commits (Conventional Commits)

`<tipo>[<ámbito>]: <descripción>` · tipos `feat fix refactor docs chore style perf test` · el mensaje incluye la versión (`v<version>`).

## Tags

Cada subida a CurseForge crea tag: beta `<mc>-neoforge-beta.X` · release `<mc>-neoforge-X.Y.Z`.

## Flujo por tarea

**0. Alcance** — si el mod tiene varias versiones, preguntar con la herramienta `question`: **"Todas"** o una versión. No asumir.

**1. Desarrollo**

```bash
git checkout minecraft/26.2/neoforge-26.2.0.57/production
./gradlew.bat build
git add -A
git commit -m "feat: <descripción>

v<version>"
git push
```

**2. CurseForge** — solo si el usuario confirma:
- Bump `mod_version` en gradle.properties → `./gradlew.bat clean build`
- Release notes `docs/curseforge/versions/<version>.md` (HTML) + actualizar `CHANGELOG.md`
- Commit `chore: bump version to <version>` → tag `<mc>-neoforge-<version>` → push
- Subir JAR: `powershell -File ../../codex-docs/scripts/curseforge-upload.ps1` (desde este repo)
- Formato HTML de descripciones/changelog: `codex-docs/reference/CURSEFORGE.md`

**3. Release estable** — bump `X.Y.Z` + tag.

**4. Graphify** — tras cada push a remoto. Versión 0.9.12: **`build` no existe**, usar `extract` (1ª vez) o `update . --force` (tras cambios):

```bash
GRAPHIFY="C:\Users\llagu\AppData\Local\Packages\PythonSoftwareFoundation.Python.3.13_qbz5n2kfra8p0\LocalCache\local-packages\Python313\Scripts\graphify.exe"
"$GRAPHIFY" update . --force
git add graphify-out/ && git commit -m "chore: update knowledge graph" && git push
```

Leer siempre `GRAPH_REPORT.md`, nunca `graph.json`/`graph.html` (pesan >1MB). Sin copias fechadas de `graphify-out/`. Backend LLM: `codex-docs/reference/GRAPHIFY.md`.

## Buenas prácticas

- Un commit por cambio lógico · commit+push tras cada cambio funcional y de docs
- `clean build` antes del JAR final · versionar antes de CurseForge · CHANGELOG al día
- Graphify actualizado tras cada release · nomenclatura consistente · sin basura en repo (`nul`, `*_errors.txt`, `TEMPLATE_LICENSE.txt`) · `.gitignore` excluye `temp/` y `lib_ext/`
- README en inglés siempre actualizado · sin residuos de mod original (paquetes, clases, toml, lang, assets) · atribución de fork explícita (README, project_description, credits)
- **Port idéntico al original** — nada de inventar features, solo adaptar a 26.2/NeoForge 26.2.0.57

## Idioma

| Ámbito | Idioma |
|---|---|
| código, logs, commits | en-US |
| README.md | en-US |
| docs internas (docs/, CHANGELOG, este archivo) | es-ES |
| CurseForge | en-US |

## Fases del Port (Roadmap)

| Fase | Tarea | Estado |
|---|---|---|
| **F0: Setup** | Repo, gradle, estructura, lib_ext copiado | ✅ Done |
| **F1: Core** | Portar common + neoforge → src/main (código, recursos, mixins, access wideners) | ⏳ Pending |
| **F2: Registries** | Bloques, items, block entities, capabilities, creative tabs, redes | ⏳ Pending |
| **F3: Client** | Modelos, render, overlays, JEI/JADE/TOP integration | ⏳ Pending |
| **F4: Integraciones** | FTB Chunks/Teams, Architectury, config, compat | ⏳ Pending |
| **F5: Testing** | runClient, runServer, pruebas de inventario, upgrade, trim, controller | ⏳ Pending |
| **F6: Docs + Release** | WORKFLOW, curseforge/ (description, vars, summary, prompt, v13.11.4.md), CHANGELOG, README | ⏳ Pending |
| **F7: Deploy** | Bump version → build → tag → push → upload script | ⏳ Pending |