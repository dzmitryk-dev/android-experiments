# AGENTS.md — hellodreamservice

Per-project agent guide. See `/AGENTS.md` at the repo root for shared conventions.

## Purpose

Android `DreamService` demo (screen-saver). When the user activates the dream
from system settings, this service renders content while the device is charging
or docked.

## Build

```bash
./gradlew :hellodreamservice:assembleDebug
./gradlew :hellodreamservice:lint
```

Or:

```bash
./gradlew -p hellodreamservice assembleDebug
```

To exercise the dream on a device:

```bash
adb shell settings put secure screensaver_components com.github.dzkoirn.hellodreamservice/.dreams.TestDream
adb shell settings put secure screensaver_enabled 1
adb shell dumpsys deviceidle force-idle
```

## Layout

- `src/main/AndroidManifest.xml` — declares `DemoActivity` (launcher) and
  `service .dreams.TestDream` with the `android.service.dreams.DreamService`
  intent-filter and `android.permission.BIND_DREAM_SERVICE` permission.
- `src/main/java/com/github/dzkoirn/hellodreamservice/`
  - `DemoActivity.kt` — a simple launcher to confirm the app installs.
  - `dreams/TestDream.kt` — the `DreamService` subclass.
- `src/main/res/layout/` — the dream's view layout.

## Conventions

- Java 11 toolchain.
- `allowBackup="false"`, `supportsRtl="false"` in the manifest. Don't toggle these
  without a reason — they were set deliberately for this demo.
- The `BIND_DREAM_SERVICE` permission is system-protected; only the system can
  bind to this service. No need for `<uses-permission>`.

## Quirks

- Uses Groovy DSL. Migrate to Kotlin DSL **only** if you're already editing the file.
- All plugin and dependency versions come from the catalog — no hardcoded
  versions. Do not regress this.
- `compileSdk` / `targetSdk` are 36; `minSdk` 26.
- No custom application class. The dream service runs in the app process; the
  activity exists only as a launcher marker.
