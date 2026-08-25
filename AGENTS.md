# AGENTS.md

Canonical agent instructions for the `android-experiments` monorepo. This file is the
source of truth for AI coding agents (opencode, Codex, and any other tool that reads
the open `AGENTS.md` standard).

Tool-specific shims (`.github/copilot-instructions.md`, etc.) are kept for IDE
integrations but defer to this file.

## TL;DR

- **Build everything from the root:** `./gradlew buildAll` (default task).
- **One project:** `./gradlew :<projectName>:assembleDebug` or `cd <project> && ./gradlew assembleDebug`.
- **Lint everything:** `./gradlew lintAll`.
- **Version catalog** (`gradle/libs.versions.toml`) is the single source of truth for
  plugin and library versions. **Never hardcode versions** in build scripts.
- **Composite build**: subprojects are wired via `includeBuild` in the root
  `settings.gradle`; the root has the only `gradlew`. There is no per-project wrapper.

## Repository layout

```
android-experiments/                # Repository root
├── AGENTS.md                       # ← this file (canonical agent guide)
├── README.md                       # Human-facing project docs
├── settings.gradle                 # Root composite build (includeBuild list)
├── build.gradle                    # Root aggregate tasks: buildAll, lintAll, ...
├── gradle.properties               # Root Gradle/AndroidX settings
├── gradle/
│   ├── libs.versions.toml          # VERSION CATALOG — source of truth for versions
│   └── wrapper/                    # Gradle wrapper (9.1.0)
├── gradlew, gradlew.bat            # Single wrapper used by every subproject
├── Makefile                        # Canonical command aliases
├── .editorconfig                   # Editor consistency rules
├── .gitattributes                   # Line-ending & linguist overrides
├── .github/
│   ├── copilot-instructions.md     # Copilot-specific addendum (deferred to AGENTS.md)
│   └── workflows/                  # CI: build.yml, pr.yml
└── <subproject>/                   # One folder per Android app (see below)
    ├── AGENTS.md                   # Per-project agent guide
    ├── settings.gradle[.kts]       # from(files("../gradle/libs.versions.toml"))
    ├── build.gradle[.kts]          # Plugin aliases + dependencies from catalog
    ├── gradle.properties           # Optional per-project overrides
    ├── proguard-rules.pro
    ├── local.properties            # NOT committed; points at local Android SDK
    └── src/                        # main / test / androidTest
```

### Included builds (Android apps)

| Folder | App | Purpose |
|---|---|---|
| `MemeViewer/` | `demo.memeviewer` | Compose + Hilt + Paging 3 + JSoup meme browser. The richest example. |
| `hellojetpackcompose/` | `com.github.dzkoirn.hellojetpackcompose` | Minimal Compose "hello world". |
| `helloshaders/` | `io.github.dzkoirn.androidexperiments.helloshaders` | OpenGL ES 2.0 / GLSL playground (Mandelbrot). |
| `hellodreamservice/` | `com.github.dzkoirn.hellodreamservice` | `DreamService` (screen-saver) demo. |
| `networkdiscoverydemo/` | `network.discovery.demo` | Network discovery over Compose. |
| `recyclerview/` | `dzmitryk.codepractice.recyclerview` | Views + Paging + Navigation examples. |
| `whitenoiseapp/` | `com.github.dzkoirn.whitenoiseapp` | White-noise audio app. |

Each project has its own `AGENTS.md` with project-specific notes.

## Build commands

### From the repository root

The root build orchestrates the composite. Aggregate tasks fan out to every included build.

| Command | Purpose |
|---|---|
| `./gradlew` | Default task: `buildAll` (everything below) |
| `./gradlew buildAll` | Run `:build` in every included build |
| `./gradlew assembleAll` | Build debug + release APKs in every included build |
| `./gradlew checkAll` | Run tests in every included build |
| `./gradlew lintAll` | Run Android lint in every included build |
| `./gradlew cleanAll` | Clean every included build |

### Targeting a single project

```bash
# Preferred — Gradle composite path
./gradlew :MemeViewer:assembleDebug
./gradlew :hellojetpackcompose:lint

# Or by entering the subproject (no separate wrapper; uses root gradlew via -p)
./gradlew -p MemeViewer assembleDebug
```

Per-project tasks (e.g. `installDebug`, `connectedAndroidTest`) work the same way.

### Useful diagnostic commands

```bash
./gradlew projects                                # List included builds
./gradlew :MemeViewer:dependencies --configuration releaseRuntimeClasspath
./gradlew help --task buildAll                    # See what buildAll wires up
./gradlew :MemeViewer:tasks --group=verification  # Available verification tasks
```

## Version management — the iron rule

**All plugin and library versions live in `gradle/libs.versions.toml`.** Update the
catalog; everything propagates through type-safe accessors (`libs.plugins.android.application`,
`libs.androidx.compose.bom`, ...).

```toml
# gradle/libs.versions.toml
[versions]
agp = "8.13.2"
kotlin = "2.2.21"
composeBom = "2025.12.01"
```

In build files:

```kotlin
// ✅ DO
plugins { alias(libs.plugins.android.application) }
dependencies { implementation(libs.androidx.compose.material3) }

// ❌ DON'T
plugins { id("com.android.application") version "8.13.2" }
dependencies { implementation("androidx.compose.material3:material3:1.4.0") }
```

If a subproject hardcodes versions, **migrate it to the catalog**. Don't add more
hardcoded versions.

### How to add a new dependency

1. Look up the current version (use Context7 MCP if unsure).
2. Add or update the `[versions]` entry in `gradle/libs.versions.toml`.
3. Add the `[libraries]` entry referencing the version.
4. Use `libs.<accessor>` in the consuming subproject's `build.gradle[.kts]`.
5. Build the affected subproject: `./gradlew :<project>:assembleDebug`.

### How to bump the Gradle wrapper

```bash
./gradlew wrapper --gradle-version 9.X.Y --distribution-type all
# Then commit gradle/wrapper/gradle-wrapper.properties, gradle-wrapper.jar, gradlew, gradlew.bat
```

## Code conventions

- **Language mix:** Build scripts use both Groovy (`*.gradle`) and Kotlin DSL (`*.gradle.kts`).
  Don't migrate one to the other as part of an unrelated change; only migrate when you're
  editing the file anyway. Prefer Kotlin DSL for new files.
- **No hardcoded versions** in build scripts — see above.
- **No per-project repos.** `dependencyResolutionManagement` at the root enforces
  `FAIL_ON_PROJECT_REPOS`. If you need a custom repo, add it to the root.
- **Compose source root:** Several projects use `src/main/kotlin/...` (not `src/main/java/...`).
  This is intentional and is configured via `sourceSets`. Don't move files without also
  updating the source-set config.
- **Kotlin style:** Official (`kotlin.code.style=official` in `gradle.properties`).
  See `.editorconfig` for indentation rules (4 spaces in Kotlin, 4 in Groovy, 2 in XML).
- **No comments in code** unless the user asks for them.

## Agent workflow

When making changes in this repo, prefer this order:

1. **Read** `AGENTS.md` (this file) and the relevant subproject's `AGENTS.md`.
2. **Read** the affected build files (`*.gradle[.kts]`, `settings.gradle[.kts]`) before editing.
3. **Edit** the smallest set of files needed.
4. **Verify** by building the affected subproject, then the whole monorepo:
   ```bash
   ./gradlew :<project>:assembleDebug
   ./gradlew buildAll
   ```
5. **Update `AGENTS.md`** (or the per-project one) if you change build commands,
   structure, or add a project.

### Prefer the Makefile for common operations

A `Makefile` is provided at the repo root with aliases for common operations:

```bash
make help       # List available targets
make build      # ./gradlew buildAll
make lint       # ./gradlew lintAll
make clean      # ./gradlew cleanAll
make memeviewer # ./gradlew :MemeViewer:assembleDebug
```

Agents should prefer these (or `./gradlew <task>`) over calling `gradle` directly.

## Tooling available to agents

- **Context7 MCP** — fetch up-to-date docs for AndroidX, Jetpack Compose, Gradle,
  Hilt, KSP, etc. Use `resolve-library-id` then `query-docs`. Prefer this over
  web search for library documentation.
- **Android CLI skill** (`~/.config/opencode/skills/android-cli/SKILL.md`) — for
  scaffolding new projects, running on devices/emulators, taking screenshots,
  and exploring the SDK.

## Common pitfalls

| Symptom | Cause / Fix |
|---|---|
| `Plugin [id: '…'] was not found` | The subproject's `settings.gradle` must `dependencyResolutionManagement { versionCatalogs { create("libs") { from(files("../gradle/libs.versions.toml")) } } }` |
| `Build was configured to prefer settings repositories over project repositories` | Don't add `repositories {}` to subproject build files. Add repos to the root `settings.gradle`. |
| `SDK location not found` | `local.properties` must exist with `sdk.dir=...`. This file is local-only and not committed. |
| Per-project `./gradlew` is missing | This is expected. Use the root `./gradlew` or `./gradlew -p <project> ...`. |
| Subproject builds in isolation but fails in composite | Almost always a version mismatch — check `gradle/libs.versions.toml`. |
| New file in `src/main/kotlin/...` is not compiled | Confirm the subproject's `sourceSets` block includes `kotlin/` (most already do). |

## When you must change the version catalog

The catalog is shared. Before bumping a version:

1. Check Context7 for current stable versions and breaking changes.
2. Bump only the `[versions]` entry — the catalog `libraries` references stay intact.
3. Build a single subproject first: `./gradlew :MemeViewer:assembleDebug`.
4. Then build everything: `./gradlew buildAll`.

## Adding a new subproject

1. Create `<projectName>/` with `settings.gradle[.kts]` and `build.gradle[.kts]`.
2. In its `settings.gradle[.kts]`, wire the catalog:
   ```kotlin
   // settings.gradle.kts
   dependencyResolutionManagement {
       versionCatalogs {
           create("libs") { from(files("../gradle/libs.versions.toml")) }
       }
   }
   ```
3. In its `build.gradle[.kts]`, use plugin aliases and `libs.*` accessors.
4. Add `includeBuild '<projectName>'` to the root `settings.gradle`.
5. Add a `README.md` and `AGENTS.md` inside `<projectName>/`.
6. Add a row to the "Included builds" table in this file.
7. Run `./gradlew :<projectName>:assembleDebug` to verify, then `./gradlew buildAll`.

## CI

- `.github/workflows/build.yml` runs `buildAll` on every push.
- `.github/workflows/pr.yml` runs `buildAll` on every PR to `main`.
- Both use Temurin JDK 17, `actions/setup-java@v4` with Gradle caching.
- Matrix expansion is configured per-included-build on PRs (see workflow file).
