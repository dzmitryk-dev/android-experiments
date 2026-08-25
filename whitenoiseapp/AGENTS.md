# AGENTS.md — whitenoiseapp

Per-project agent guide. See `/AGENTS.md` at the repo root for shared conventions.

## Purpose

Minimal white-noise audio app. Single Activity, plays looping noise from a
raw resource or generated PCM buffer. Useful as a starting point for any
audio-focused Android sample.

## Build

```bash
./gradlew :whitenoiseapp:assembleDebug
./gradlew :whitenoiseapp:lint
```

Or:

```bash
./gradlew -p whitenoiseapp assembleDebug
```

## Layout

- `src/main/AndroidManifest.xml` — declares `MainActivity`. No audio-related
  permissions yet (foreground-service permission will be needed if/when this
  app plays audio while backgrounded).
- `src/main/java/com/github/dzkoirn/whitenoiseapp/` — `MainActivity`, audio
  playback code.

## Conventions

- Groovy DSL (`build.gradle`, `settings.gradle`). Don't migrate to Kotlin DSL
  unless you're already editing the file.
- Java 11 toolchain.

## Quirks

- All dependency versions come from the catalog — no hardcoded versions. Do not
  regress this.
- `compileSdk` / `targetSdk` are 36; `minSdk` 29.
- No audio library is declared in `build.gradle` yet — audio handling lives in
  code/resources. If you add `MediaPlayer`/`ExoPlayer` work, consider whether
  to add the foreground-service permission to the manifest.
