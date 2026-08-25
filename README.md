# AndroidExperiments

[![Build](https://github.com/dzmitryk-dev/android-experiments/actions/workflows/build.yml/badge.svg)](https://github.com/dzmitryk-dev/android-experiments/actions/workflows/build.yml)

A collection of Android apps I built at different times — some educational, some
solving small problems of my own. They live together in this Gradle composite
build so a single config bump (Gradle, AGP, Kotlin, Compose, etc.) propagates to
every project.

## Layout

```
android-experiments/
├── AGENTS.md                  ← canonical guide for AI coding agents (opencode / Codex)
├── README.md                  ← this file
├── settings.gradle            ← composite includeBuild list
├── build.gradle               ← aggregate tasks: buildAll, lintAll, ...
├── gradle/libs.versions.toml  ← VERSION CATALOG (single source of truth)
├── gradlew, gradlew.bat       ← single Gradle wrapper (9.1.0)
├── Makefile                   ← convenience aliases: make build, make lint, ...
├── .editorconfig
├── .gitattributes
└── <subproject>/
    ├── AGENTS.md              ← per-project agent guide
    ├── settings.gradle[.kts]
    ├── build.gradle[.kts]
    └── src/
```

### Apps

| Folder | App ID | Purpose |
|---|---|---|
| [MemeViewer](./MemeViewer/) | `demo.memeviewer` | Compose + Hilt + Paging 3 + JSoup meme browser |
| [hellojetpackcompose](./hellojetpackcompose/) | `com.github.dzkoirn.hellojetpackcompose` | Minimal Compose "hello world" |
| [helloshaders](./helloshaders/) | `io.github.dzkoirn.androidexperiments.helloshaders` | OpenGL ES 2.0 / GLSL playground (Mandelbrot) |
| [hellodreamservice](./hellodreamservice/) | `com.github.dzkoirn.hellodreamservice` | `DreamService` (screen-saver) demo |
| [networkdiscoverydemo](./networkdiscoverydemo/) | `network.discovery.demo` | Network discovery over Compose |
| [recyclerview](./recyclerview/) | `dzmitryk.codepractice.recyclerview` | Views + Paging + Navigation examples |
| [whitenoiseapp](./whitenoiseapp/) | `com.github.dzkoirn.whitenoiseapp` | White-noise audio app |

Each subproject is independent: it has its own `settings.gradle[.kts]`,
`build.gradle[.kts]`, `proguard-rules.pro`, and `src/`. There is **no
per-project Gradle wrapper** — they share the root one.

## Build commands

### Prerequisites

- JDK 17 or newer (the project compiles with JDK 17 in CI).
- Android SDK installed locally; each developer creates a `local.properties`
  with `sdk.dir=...`. This file is git-ignored.

### From the repository root

```bash
./gradlew buildAll      # build everything (default)
./gradlew assembleAll   # assemble debug + release APKs
./gradlew checkAll      # run all tests
./gradlew lintAll       # run Android lint
./gradlew cleanAll      # clean every subproject
```

### One project

```bash
# Via composite path (preferred)
./gradlew :MemeViewer:assembleDebug
./gradlew :hellojetpackcompose:lint

# Or via -p from the subproject directory (uses the root wrapper)
./gradlew -p MemeViewer assembleDebug
```

### Make targets

A `Makefile` is provided for convenience:

```bash
make help           # list targets
make build          # ./gradlew buildAll
make lint           # ./gradlew lintAll
make clean          # ./gradlew cleanAll
make memeviewer     # ./gradlew :MemeViewer:assembleDebug
make install-memeviewer  # ./gradlew :MemeViewer:installDebug
make test-memeviewer     # ./gradlew :MemeViewer:testDebugUnitTest
make lint-memeviewer     # ./gradlew :MemeViewer:lintDebug
```

## Version management

**All plugin and library versions are in `gradle/libs.versions.toml`.** This is
the single source of truth. Build files use type-safe catalog accessors
(`libs.plugins.android.application`, `libs.androidx.compose.material3`,
etc.) and never hardcode versions.

To add or bump a dependency:

1. Update the `[versions]` entry in `gradle/libs.versions.toml`.
2. Add or update the matching `[libraries]` entry (e.g. `androidx-foo = { ... }`).
3. Use `libs.androidx.foo` in the consuming `build.gradle[.kts]`.
4. Verify with `./gradlew :<project>:assembleDebug`, then `./gradlew buildAll`.

## Agentic programming

This repo is set up for AI coding agents (opencode, Codex, etc.). The canonical
guide is [`AGENTS.md`](./AGENTS.md). Per-project notes live in each subproject's
`AGENTS.md`.

Highlights:

- Composite build + version catalog are first-class — agents should bump versions
  in the catalog, not in build scripts.
- `./gradlew` works without per-project wrappers — agents should not create them.
- The `Makefile` provides canonical aliases agents should prefer over raw `gradle`.

## CI

- `.github/workflows/build.yml` — runs `buildAll` on every push.
- `.github/workflows/pr.yml` — runs `buildAll` on PRs to `main`.

Both use Temurin JDK 17 with Gradle caching.

## License

BSD 2-Clause. See [LICENSE](./LICENSE).
