# AGENTS.md — MemeViewer

Per-project agent guide. See `/AGENTS.md` at the repo root for shared conventions.

## Purpose

Compose-based meme browser that scrapes a meme site via JSoup and streams results
through Paging 3 + Coil. The richest example in this monorepo — uses Hilt, KSP,
Paging, Coil, JSoup, Timber, and Coroutines test infra.

## Build

From the repo root (preferred):

```bash
./gradlew :MemeViewer:assembleDebug
./gradlew :MemeViewer:test
./gradlew :MemeViewer:lint
```

Or by entering the subproject (uses the root `gradlew`):

```bash
./gradlew -p MemeViewer assembleDebug
```

## Layout

- `src/main/kotlin/demo/memeviewer/` — Kotlin source (note: `kotlin/`, not `java/`).
- `src/main/AndroidManifest.xml` — declares `MemeViewerApplication` + `MemeActivity`.
- `src/test/kotlin/...` — JUnit 4 + AssertJ + Mockito-Kotlin + Turbine tests.
- `src/androidTest/kotlin/...` — Espresso + Compose UI tests.
- `proguard-rules.pro` — minimal; R8 default rules cover this app.

## Module packages

```
demo.memeviewer
├── MemeViewerApplication     Hilt @HiltAndroidApp entry point
├── MemeActivity              Single Compose activity
├── data                      DataSource, PagingSource, HTML page parser
├── di                        Hilt @Module + @Qualifier providers
├── model                     Plain DTOs (PageData)
├── presentation              MemeViewModel (StateFlow)
└── ui
    ├── theme                 Color, Theme, Type
    └── view                  MemeListScreen (Compose)
```

## Quirks

- Kotlin source root is `src/main/kotlin` (not `java/`) — configured in `sourceSets`.
- Hilt requires `MemeViewerApplication` as the `android:name` of the `<application>`
  tag. Don't rename without updating the manifest.
- `INTERNET` permission is required (JSoup fetches HTML pages).
- Tests use `MockWebServer`-style patterns? No — pure unit tests against fake
  data sources. `androidx.paging.testing` is wired but not heavily used yet.
- The catalog plugin aliases `kotlin.compose`, `hilt`, and `ksp` are all required.
  Don't drop any.
