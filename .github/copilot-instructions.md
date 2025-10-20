# Agent Instructions for android-experiments

## Project Overview

This is a **Gradle composite build monorepo** containing multiple independent Android applications. It is structured as a multi-project setup where each sub-project is an independent Gradle build included via `includeBuild` directives in the root `settings.gradle`.

**Repository Root**: `/home/dzkoirn/Projects/android-experiments`

## Architecture

### Composite Build Structure

- **Root build**: Provides aggregate tasks and centralized plugin management
- **Included builds** (independent Gradle builds):
  - `helloshaders` - Android app with shader experiments
  - `whitenoiseapp` - Android app with white noise functionality
  - `hellodreamservice` - Android app with dream service
  - `hellojetpackcompose` - Android app with Jetpack Compose (not auto-included in composite)
  - `MemeViewer` - Android app with meme browsing (Jetpack Compose, Paging, JSoup)

Each included build has its own:
- `settings.gradle` - Local configuration
- `build.gradle` - Local build definition
- `gradlew` / `gradlew.bat` - Local Gradle wrapper
- `src/` directory structure

### Root Build Configuration

**File**: `settings.gradle`
- Centralized plugin management (via Gradle Version Catalog in `gradle/libs.versions.toml`)
- Centralized dependency resolution management with `FAIL_ON_PROJECT_REPOS` mode
- Plugin repositories: `gradlePluginPortal()`, `mavenCentral()`, `google()`
- Dependency repositories: `google()`, `mavenCentral()`
- `includeBuild` directives for composite builds

**File**: `build.gradle`
- Root-level aggregate tasks for orchestrating all included builds
- Helper method `compositeTask()` to create lifecycle tasks

## Build System Details

### Technology Stack

**Check `gradle/libs.versions.toml` in the root for actual versions:**

- **Gradle**: 8.x (minimum)
- **Android Gradle Plugin**: See `agp` in version catalog
- **Kotlin**: See `kotlin` in version catalog
- **Android SDK**: Required (configured via Android Gradle Plugin)
- **JDK**: Required (compatible with Gradle version in use)

The version catalog at `gradle/libs.versions.toml` is the source of truth for all library versions.

### Aggregate Tasks

Available from the repository root:

| Task | Purpose |
|------|---------|
| `buildAll` | Build all included builds (default task) |
| `assembleAll` | Assemble all included builds (APKs/AARs) |
| `cleanAll` | Clean all included builds |
| `checkAll` | Run checks in all included builds |
| `lintAll` | Run lint in all included builds |

### Building

**From root** (runs aggregate tasks across all included builds):
```bash
# Default task (buildAll)
./gradlew

# Explicit aggregate task
./gradlew buildAll
./gradlew assembleAll

# Individual included build task
./gradlew :helloshaders:assemble
```

**From individual subproject directory**:
```bash
cd helloshaders
./gradlew assembleDebug
```

**Using local Gradle installation**:
```bash
gradle buildAll
```

## Version Management

### Gradle Version Catalog

**File**: `gradle/libs.versions.toml`

This is the single source of truth for all library and plugin versions in the monorepo:

- All versions defined in `[versions]` section
- Libraries reference versions using `version.ref`
- Plugins use centralized versions
- Type-safe accessors available via `libs` (e.g., `libs.plugins.android.application`)

### Checking & Updating Versions

**Before making any changes involving libraries or plugins:**

1. Review `gradle/libs.versions.toml` to see current versions
2. Check if updates are needed based on build failures or requirements
3. If updating: modify ONLY in version catalog, not in build files
4. Build files use references like `alias(libs.plugins.kotlin.android)` - no hardcoding needed

**Example - checking Kotlin version:**
```bash
grep "^kotlin = " gradle/libs.versions.toml
```

**Example - updating a version:**
```toml
# In gradle/libs.versions.toml
[versions]
kotlin = "2.2.20"  # Update here only - all builds will use this automatically
```

### Important: No Version Hardcoding

❌ **Don't do this:**
```gradle
plugins {
    id 'org.jetbrains.kotlin.android' version '1.9.10'  // Hardcoded version
}
```

✅ **Do this instead:**
```gradle
plugins {
    alias(libs.plugins.kotlin.android)  // Uses version from catalog
}
```

## Code Change Guidelines

### When Modifying Code

1. **Individual App Changes**: Modify files within the specific subproject directory (e.g., `helloshaders/src/...`)
2. **Shared Configuration**: Changes to `settings.gradle` or root `build.gradle` affect all builds
3. **Plugin/Dependency Versions**: Managed centrally in `gradle/libs.versions.toml` - do NOT hardcode versions
4. **Dependency Resolution**: Managed centrally via `dependencyResolutionManagement` - subprojects cannot define custom repos

### Testing Changes

After modifying code:

1. **Build affected subproject**:
   ```bash
   cd <subproject>
   ./gradlew build
   ```

2. **Build all projects**:
   ```bash
   ./gradlew buildAll
   ```

3. **Run linting**:
   ```bash
   ./gradlew lintAll
   ```

### Important Notes

- Each subproject is independent; changes in one do not affect others unless explicitly dependent
- Root `build.gradle` uses composite task orchestration (not subprojects {})
- **All plugin and library versions are centralized in `gradle/libs.versions.toml`** - do not hardcode versions
- Dependency resolution is centralized; subprojects inherit repositories from root
- The root build does not contain application code; it only orchestrates included builds

## Project-Specific Details

### Version Catalog (Source of Truth)

**Location**: `gradle/libs.versions.toml`

All library, plugin, and framework versions are managed here. When making changes:

1. **Check versions** - Always reference `gradle/libs.versions.toml` for current versions
2. **Never hardcode** - Do not assume or hardcode version numbers in code or documentation
3. **Update catalog** - If upgrading dependencies, update only in the version catalog
4. **Propagates automatically** - Changes propagate to all builds via type-safe accessors (e.g., `libs.plugins.kotlin.android`)

Key version references:
- `agp` - Android Gradle Plugin version
- `kotlin` - Kotlin compiler version
- `composeBom` - Jetpack Compose Bill of Materials
- `hilt` - Hilt dependency injection
- `ksp` - Kotlin Symbol Processing
- `junit`, `espressoCore`, `mockitoCore`, `timber`, `jsoup` - Other dependencies

### Kotlin Configuration

- Version: Check `kotlin` in `gradle/libs.versions.toml`
- Applied via `org.jetbrains.kotlin.android` plugin in root pluginManagement

### Android Gradle Plugin

- Version: Check `agp` in `gradle/libs.versions.toml`
- Both `com.android.application` and `com.android.library` plugins available

### Development Workflow

1. Make changes in the desired subproject
2. Test locally with that subproject's gradlew
3. Run `buildAll` from root to verify no cross-project issues
4. Commit changes with clear messages indicating which subproject(s) are affected

## Common Issues & Troubleshooting

| Issue | Resolution |
|-------|-----------|
| `gradle` command not found | Use subproject wrapper: `./gradlew` from root, or `cd <subproject> && ./gradlew` |
| Plugin version conflicts | Check `gradle/libs.versions.toml` - update version catalog if needed, not build files |
| Library version incompatibility | Check `gradle/libs.versions.toml` - update version reference there, changes propagate automatically |
| Repository access denied | Check `dependencyResolutionManagement` in root `settings.gradle` - only `google()` and `mavenCentral()` are available |
| Subproject build fails in composite but works standalone | Likely repo or plugin version mismatch - check `gradle/libs.versions.toml` and root `settings.gradle` |
| `includeBuild` not working | Ensure subproject has `settings.gradle`; use `gradle --no-pager includeBuilds` to verify |

## File Locations Reference

```
android-experiments/
├── settings.gradle           # Root composite build configuration
├── build.gradle              # Root composite build tasks
├── README.md                 # Documentation
├── gradle/
│   └── libs.versions.toml    # VERSION CATALOG - source of truth for all library versions
├── local.properties          # Local development settings (not committed)
├── gradlew / gradlew.bat     # Root wrapper (may not be functional)
│
├── helloshaders/
│   ├── settings.gradle
│   ├── build.gradle
│   ├── gradlew / gradlew.bat
│   └── src/
│
├── whitenoiseapp/
│   ├── settings.gradle
│   ├── build.gradle
│   ├── gradlew / gradlew.bat
│   └── src/
│
├── hellodreamservice/
│   ├── settings.gradle
│   ├── build.gradle
│   ├── gradlew / gradlew.bat
│   └── src/
│
├── hellojetpackcompose/
│   ├── settings.gradle
│   ├── build.gradle
│   ├── gradlew / gradlew.bat
│   └── src/
│
└── MemeViewer/               # Flat single-module app structure
    ├── settings.gradle.kts
    ├── build.gradle.kts
    ├── src/
    │   ├── main/
    │   ├── test/
    │   └── androidTest/
    ├── proguard-rules.pro
    ├── README.md
    └── gradle/
```

## When Making Changes

- **Minimal changes only**: Edit only necessary files to achieve the goal
- **Preserve working code**: Do not remove or modify unrelated functionality
- **Test changes**: Always build after modifications using appropriate gradle commands
- **Update documentation**: If changes affect how the project is built or used, update README.md or this file
- **No new tooling**: Only use existing gradle, build, and test mechanisms

## Key Constraints

- ✅ Gradle 8.x required
- ✅ **All versions in `gradle/libs.versions.toml` - never hardcode**
- ✅ Central plugin management via version catalog
- ✅ Central dependency resolution (no per-project repos)
- ✅ Each subproject is independent (can be built standalone)
- ✅ Root build purely orchestrates; no application code in root
- ✅ All linting/building via gradle (no manual compilation)
