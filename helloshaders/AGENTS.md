# AGENTS.md — helloshaders

Per-project agent guide. See `/AGENTS.md` at the repo root for shared conventions.

## Purpose

OpenGL ES 2.0 / GLSL playground. Renders a Mandelbrot set via a fragment shader
(`assets/shader.frag` or similar) on a `GLSurfaceView`. The only project focused
on low-level graphics in this monorepo.

## Build

```bash
./gradlew :helloshaders:assembleDebug
./gradlew :helloshaders:lint
```

Or:

```bash
./gradlew -p helloshaders assembleDebug
```

## Layout

- `src/main/AndroidManifest.xml` — declares `MainActivity`, requires GL ES 2.0
  via `<uses-feature android:glEsVersion="0x00020000" android:required="true"/>`.
- `src/main/java/io/github/dzkoirn/androidexperiments/helloshaders/` — `MainActivity`
  + the `GLSurfaceView` + renderer.
- Shaders are loaded from `assets/` at runtime.

## Conventions

- Java 11 toolchain.
- `<application android:hasCode="true">` (default) — the renderer is pure Java/Kotlin,
  no NDK.
- The manifest carries an `android.app.lib_name` meta-data entry (empty) — leave it
  alone, it's a hook for adding a native library later if you extend this project.
- `supportsRtl` is enabled.

## Quirks

- Uses Groovy DSL. Don't migrate to Kotlin DSL unless you're already editing the file.
- `compileSdk` / `targetSdk` are 36; `minSdk` 28.
- All dependency versions come from the catalog — no hardcoded versions. Do not
  regress this.
