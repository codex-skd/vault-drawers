# CurseForge — Variables del proyecto Vault Drawers

> Las siguientes variables son leídas automáticamente por `../codex-docs/scripts/curseforge-upload.ps1`

project_id = 1668019
api_token = ee776b0a-ee95-4850-b554-06be02a8657f
game_versions = 9638, 9639, 16498, 10150
release_type = beta

## Proyecto

| Variable | Valor |
|----------|-------|
| `curseforge_project_id` | `1668019` |
| `mod_id` | `vault_drawers` |
| `display_name` | `Vault Drawers` |

## Tokens

| API | Token | Uso |
|-----|-------|-----|
| Upload | `ee776b0a-ee95-4850-b554-06be02a8657f` | Subir archivos JAR |
| Core (GET) | `$2a$10$yGwryAfmRkS9ZJsJUDf5YOKZpOIsmHB8Fji2D8JVCKBSZEKYlwmaO` | Consultar datos del mod |

Autenticación Upload: cabecera `X-Api-Token`
Autenticación Core: cabecera `x-api-key`

## Versión actual

| Variable | Valor |
|----------|-------|
| `minecraft_version` | `26.2` |
| `framework` | `neoforge` |
| `neo_version` | `26.2.0.57` |
| `java_version` | `21` |
| `environment` | `Client`, `Server` |

## Rama

```
minecraft/26.2/neoforge-26.2.0.57/production
```

## Tag

Formato beta: `<mc-version>-<framework>-beta.<X>` · Formato release: `<mc-version>-<framework>-<X.Y.Z>`
Ejemplo (esta versión): `26.2-neoforge-beta.1`

## Parámetros del upload

| Campo | Valor | Notas |
|-------|-------|-------|
| `displayName` | `Vault Drawers (0.0.0-beta.1)` | Nombre visible: `display_name (version)` |
| `changelog` | HTML (no Markdown) | Ver estructura abajo |
| `changelogType` | `html` | Obligatorio para que se vea bien |
| `releaseType` | `beta` | Primera beta pública del port — aún no probada en juego |
| `gameVersionNames` | `["Client", "Server", "26.2", "NeoForge"]` | Entorno + MC + modloader |

## Estructura del changelog (HTML)

```html
<h2>v0.0.0-beta.1 - Initial 26.2 port (beta)</h2>

<h3>Notes</h3>
<blockquote>First beta of the port of Storage Drawers 13.11.4 from MC 1.21.1 (NeoForge 21.1.196) to MC 26.2 (NeoForge 26.2.0.57). Mod ID changed to <code>vault_drawers</code>. Compiles and packages successfully; not yet verified in-game.</blockquote>

<hr>

<p><strong>JAR</strong>: <code>vault_drawers-26.2-neoforge-26.2.0.57-0.0.0-beta.1.jar</code></p>
```

## Subir archivo (JAR)

Usar el script compartido desde la raíz del proyecto:

```powershell
powershell -File ../codex-docs/scripts/curseforge-upload.ps1
```

El script lee `project_id`, `api_token` y `game_versions` de este archivo, y `mod_id`, `mod_name`, `minecraft_version`, `mod_version` de `gradle.properties`. Sube automáticamente el JAR desde `build/libs/` con el changelog de `docs/curseforge/versions/<version>.md`.

## Verificar con GET

```bash
curl -s "https://api.curseforge.com/v1/mods/<PROJECT_ID>/files/<FILE_ID>" \
  -H "x-api-key: <CORE_TOKEN>"
```

## Changelog

```bash
curl -s "https://api.curseforge.com/v1/mods/<PROJECT_ID>/files/<FILE_ID>/changelog" \
  -H "x-api-key: <CORE_TOKEN>"
```

## Descripcion del proyecto

No hay endpoint API para actualizar la descripcion. Se edita manualmente desde la web de CurseForge pegando el HTML de `docs/curseforge/project_description.md`.

## Flujo completo

1. `./gradlew clean build`
2. Actualizar `docs/curseforge/versions/<version>.md` con HTML
3. Actualizar `CHANGELOG.md`
4. `git commit -m "chore: bump version to <version>\n\nv<version>"` + `git push`
5. `git tag -a 26.2-neoforge-<version> -m "v<version>: Initial 26.2 port"` + `git push origin <tag>`
6. `powershell -File ../codex-docs/scripts/curseforge-upload.ps1` (o manual desde build/libs)
7. Verificar con GET que el changelog se vea bien
8. Liberar manualmente desde la web si es necesario