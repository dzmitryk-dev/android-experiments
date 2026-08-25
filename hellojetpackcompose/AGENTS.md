# AGENTS.md — hellojetpackcompose

Per-project agent guide. See `/AGENTS.md` at the repo root for shared conventions.

## Purpose

The minimal "hello world" for Jetpack Compose. Single Activity, one Composable,
no dependencies beyond the Compose BOM. Use this as the reference template for
starting a new Compose-based subproject in this monorepo.

## Build

```bash
./gradlew :hellojetpackcompose:assembleDebug
./gradlew :hellojetpackcompose:lint
```

Or:

```bash
./gradlew -p hellojetpackcompose assembleDebug
```

## Layout

- `src/main/AndroidManifest.xml` — declares `MainActivity`.
- `src/main/java/com/github/dzkoirn/hellojetpackcompose/` — only source files.
- `src/main/res/values/themes.xml` — Material3 theme.

## Conventions

- Java 11 toolchain, JVM 11 target.
- `buildFeatures.compose = true` is set; the `kotlin-compose` plugin alias must stay.
- Vector drawables use the support library (`vectorDrawables { useSupportLibrary true }`).
- No custom application class; the Activity is standalone.

## Quirks

- Uses Groovy DSL (`build.gradle`, `settings.gradle`). Don't migrate to Kotlin DSL
  unless you're already editing the file.
- `compileSdk` is 36, `minSdk` 29, `targetSdk` 36. Matches the rest of the modernized apps.
