# AGENTS.md — recyclerview

Per-project agent guide. See `/AGENTS.md` at the repo root for shared conventions.

## Purpose

Classic Views-based Android samples: RecyclerView + Paging 3 + Navigation
Component + LiveData/ViewModel. The only project in the monorepo without
Jetpack Compose. Useful as a reference for legacy View-system code.

## Build

```bash
./gradlew :recyclerview:assembleDebug
./gradlew :recyclerview:lint
```

Or:

```bash
./gradlew -p recyclerview assembleDebug
```

## Layout

- `src/main/AndroidManifest.xml` — declares `MainActivity`.
- `src/main/java/dzmitryk/codepractice/recyclerview/`
  - `MainActivity.kt` — entry, hosts the nav graph.
  - Fragments, adapters, ViewModels.
- `src/main/res/`
  - `layout/` — XML layouts (used with ViewBinding).
  - `navigation/` — nav graph XML.

## Conventions

- Mixed DSL: `build.gradle.kts` (Kotlin) and `settings.gradle` (Groovy). Don't
  migrate `settings.gradle` to `.kts` unless you're editing it for another reason.
- `buildFeatures.viewBinding = true` — generated `*Binding` classes are used in
  fragments. Don't switch to Compose without rewriting the layouts.
- `freeCompilerArgs` adds `-XXLanguage:+PropertyParamAnnotationDefaultTargetMode`.
  This is an experimental Kotlin compiler flag — **keep it** unless you verify
  the project compiles and tests pass without it.
- Java 11 toolchain via `kotlin { compilerOptions { ... } }`.

## Quirks

- `compileSdk` / `targetSdk` are already 36, `minSdk` 26 (the lowest in the
  monorepo — fine for the legacy Views-only stack).
- All dependencies go through the version catalog; no hardcoded versions.
- The deprecated `legacy-support-v4` artifact is included for backwards
  compatibility with very old code samples. Don't remove without checking
  which layout still references it.
