# MemeViewer

A simple Android application for browsing memes using Jetpack Compose.

## Structure

This is a **flat single-module Android application** with the following layout:

```
MemeViewer/
├── src/
│   ├── main/
│   │   ├── kotlin/demo/memeviewer/    # Application code
│   │   └── res/                       # Resources (layouts, strings, colors, etc.)
│   ├── test/                          # Unit tests
│   └── androidTest/                   # Instrumentation tests
├── build.gradle.kts                   # Build configuration
├── settings.gradle.kts                # Gradle settings
├── proguard-rules.pro                 # ProGuard/R8 rules
└── gradle/                            # Gradle wrapper files
```

## Tech Stack

- **Kotlin** 1.9.10
- **Jetpack Compose** for UI
- **Android API 29-36** (minSdk 29, targetSdk 36)
- **Hilt** for dependency injection
- **Paging 3** for paginated content
- **JSoup** for HTML parsing
- **Coil** for image loading
- **Timber** for logging

## Building

From the project root:

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## Project Organization

- **data/** - Data layer (paging sources, parsers, data sources)
- **di/** - Hilt dependency injection modules
- **model/** - Data models
- **presentation/** - ViewModels and business logic
- **ui/theme/** - Compose theme and styling
- **ui/view/** - Compose UI components and screens
- **MemeViewerApplication** - Application entry point
- **MemeActivity** - Main activity with Compose integration

## Development

The project uses Gradle with KTS (Kotlin DSL) for build configuration and leverages type-safe accessors via `libs` for dependency management.

### Key Features

- Paginated meme listing
- HTML parsing for content extraction
- Modern Compose UI
- Dependency injection with Hilt
- Comprehensive testing setup
