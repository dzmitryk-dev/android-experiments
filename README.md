# AndroidExperiments
Collection of my experiments with Android platform

## Composite multi-project setup

This repository is configured as a Gradle composite build. The root build includes the sibling Android sample apps as independent builds so you can build everything from the root without changing directories.

Included builds are auto-discovered: any top-level folder containing its own `settings.gradle` is included.

### Aggregate tasks

From the repository root you can run:

- `cleanAll` – runs `:clean` in every included build
- `assembleAll` – runs `:assemble` in every included build
- `buildAll` – runs `:build` in every included build (default)
- `checkAll` – runs `:check` in every included build
- `lintAll` – runs `:lint` in every included build that has the Android Gradle Plugin

### How to use

1. Ensure you have the Android SDK and JDK installed, and that the Android Gradle Plugin compatible Gradle Wrapper exists in each included build (each sample project already has its own wrapper).
2. From the repo root, use the wrapper from a subproject or install Gradle locally. Example commands:

```bash
# Option A: use a subproject wrapper to run root tasks (works because they invoke included builds)
./hellojetpackcompose/gradlew -p hellojetpackcompose -I ../init.gradle :help

# Option B: install Gradle and run root tasks directly
gradle buildAll
```

Note: The root wrapper script may not be initialized. Use the subproject wrappers to build individual apps, e.g.:

```bash
cd helloshaders
./gradlew assembleDebug
```

### Troubleshooting

- If `gradle` is not found, install it or execute via a subproject's `./gradlew`.
- If an included build fails, you can build projects individually in their own directories to isolate issues.
