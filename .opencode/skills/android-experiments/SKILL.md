---
name: android-experiments
description: Repo-local skill for the android-experiments monorepo. Use when working on any subproject, the version catalog, the composite build, or Gradle wrappers. Activates for build commands, version bumps, adding a new subproject, or any task involving AGENTS.md / Makefile.
---

This repo is a Gradle composite build of independent Android apps. The canonical
agent guide is `AGENTS.md` at the repo root; per-project notes are in each
subproject's `AGENTS.md`. Read both before making changes.

## Quick reference

```bash
# Build everything (default task)
./gradlew buildAll

# One project — preferred form
./gradlew :MemeViewer:assembleDebug
./gradlew :hellojetpackcompose:lint

# Or by entering the subproject (uses the root wrapper)
./gradlew -p MemeViewer assembleDebug

# Make targets
make help
make build          # buildAll
make lint           # lintAll
make memeviewer     # ./gradlew :MemeViewer:assembleDebug
make install-memeviewer
make test-memeviewer
```

## Iron rule: version catalog

All plugin and library versions live in `gradle/libs.versions.toml`. Update
the catalog; never hardcode versions in `build.gradle[.kts]`. Use type-safe
accessors: `libs.plugins.android.application`, `libs.androidx.compose.material3`,
etc. To find an accessor for a library, read `gradle/libs.versions.toml`.

## Composite build model

- The root `settings.gradle` does `includeBuild` for each subproject.
- The root has the **only** `gradlew`. Subprojects do not have their own
  wrapper. Don't create per-project wrappers.
- Subprojects' `settings.gradle[.kts]` load the catalog via:
  ```groovy
  dependencyResolutionManagement {
      versionCatalogs {
          libs { from(files('../gradle/libs.versions.toml')) }
      }
  }
  ```

## Adding a new subproject

1. Create `<name>/` with `settings.gradle[.kts]` and `build.gradle[.kts]`.
2. Wire the catalog in the subproject's `settings.gradle[.kts]`.
3. Use plugin aliases (`alias(libs.plugins.*)`) and `libs.*` accessors.
4. Add `includeBuild '<name>'` to the root `settings.gradle`.
5. Add a `README.md` and `AGENTS.md` in `<name>/`.
6. Add the subproject to the `PROJECTS` list in the root `Makefile`.
7. Verify with `./gradlew :<name>:assembleDebug` then `./gradlew buildAll`.

## Library docs

For AndroidX, Compose, Gradle, Hilt, KSP, etc., use the Context7 MCP:
`resolve-library-id` → `query-docs`. Don't rely on training data for current
versions or APIs.

## Common pitfalls

- `Plugin not found` → the subproject's `settings.gradle` must load the catalog.
- `RepositoriesMode.FAIL_ON_PROJECT_REPOS` → don't add `repositories {}` blocks
  inside subproject `build.gradle[.kts]`; add repos at the root.
- `SDK location not found` → `local.properties` is local-only and not committed.
- Per-project `./gradlew` is intentionally absent.
