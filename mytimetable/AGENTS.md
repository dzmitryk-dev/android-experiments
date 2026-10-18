# AGENTS.md — mytimetable

Per-project agent guide. See `/AGENTS.md` at the repo root for shared conventions.

## Purpose

"My Timetable" — a trip planning app. Currently an empty Compose shell (single
Activity, one Composable), scaffolded from the `hellojetpackcompose` template.

## Build

```bash
./gradlew :mytimetable:assembleDebug
./gradlew :mytimetable:lint
```

Or:

```bash
./gradlew -p mytimetable assembleDebug
```

## Layout

- `src/main/AndroidManifest.xml` — declares `MainActivity`.
- `src/main/kotlin/com/github/dzkoirn/mytimetable/` — Kotlin source (note: `kotlin/`).
- `src/main/res/values/themes.xml` — `Theme.MyTimetable`.

## Conventions

- Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`).
- `compileSdk` 36, `minSdk` 29, `targetSdk` 36. Java 11 toolchain.
- Uses `kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }`, not the
  deprecated `kotlinOptions` block.
- `buildFeatures.compose = true`; the `kotlin-compose` plugin alias must stay.
- No custom application class; the Activity is standalone.
