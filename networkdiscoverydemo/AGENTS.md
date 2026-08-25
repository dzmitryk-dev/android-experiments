# AGENTS.md — networkdiscoverydemo

Per-project agent guide. See `/AGENTS.md` at the repo root for shared conventions.

## Purpose

Network discovery demo over Compose. Discovers peers/services on the local
network and renders them in a Compose list. Use `INTERNET` and likely
`ACCESS_NETWORK_STATE` permissions plus `ACCESS_WIFI_STATE` for service
discovery.

## Build

```bash
./gradlew :networkdiscoverydemo:assembleDebug
./gradlew :networkdiscoverydemo:lint
```

Or:

```bash
./gradlew -p networkdiscoverydemo assembleDebug
```

## Layout

- `src/main/AndroidManifest.xml` — declares `MainActivity`, requests `INTERNET`.
- `src/main/kotlin/network/discovery/demo/` — Kotlin source (note: `kotlin/`).
- `src/main/res/` — Compose theme + drawables.

## Conventions

- Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`).
- `compileSdk` uses `release(36)` — the Kotlin-DSL pre-release API helper that
  opts into the next Android SDK before it's final. **Do not change this to
  `36` plain** — the `release(...)` form is intentional.
- Java 11 toolchain.
- Uses `kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }` (modern
  Kotlin DSL form), not the deprecated `kotlinOptions` block.
- `buildFeatures.compose = true`.

## Quirks

- If you bump `compileSdk`, also re-evaluate whether to keep the `release(...)`
  helper or switch to the final SDK version.
- Source root is `kotlin/` not `java/`. The `sourceSets` block in `build.gradle.kts`
  configures this — don't move files without updating the source-set config.
