# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project state

This is an IntelliJ Platform plugin (`com.github.seclerp.bril`, display name "Bril") freshly generated from the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template). All current code under `com.github.seclerp.intellijbril` is **template sample code** (a tool window, a project service, a startup activity, an example test) — every sample logs a reminder to remove it. The README still contains the template's TODO checklist. Expect to replace, not extend, the `My*` classes when building actual plugin functionality.

## Commands

Use the Gradle wrapper (`./gradlew`). Key tasks:

- `./gradlew build` — compile and assemble the plugin
- `./gradlew check` — run all tests + verifications (this is what the "Run Tests" config invokes)
- `./gradlew test` — run unit tests only
- `./gradlew test --tests "com.github.seclerp.intellijbril.MyPluginTest"` — run a single test class
- `./gradlew test --tests "*.MyPluginTest.testRename"` — run a single test method
- `./gradlew runIde` — launch a sandbox IDE (2025.2.6.2) with the plugin loaded
- `./gradlew verifyPlugin` — run the IntelliJ Plugin Verifier
- `./gradlew buildPlugin` — produce the distributable plugin zip
- `./gradlew patchChangelog` — fold the `[Unreleased]` changelog section into a versioned release

The same tasks are wired into the `.run/` configurations (Run Plugin → `runIde`, Run Tests → `check`, Run Verifications → `verifyPlugin`).

## Architecture

- **Build system**: Gradle Kotlin DSL using the IntelliJ Platform Gradle Plugin 2.x. Configuration is split: `settings.gradle.kts` declares the `intellijPlatform` repositories and pinned plugin versions; `build.gradle.kts` declares the target IDE (`intellijIdea("2025.2.6.2")`), the Platform test framework, and JUnit 4; `gradle.properties` holds `group`/`version`/`pluginRepositoryUrl` and Gradle flags (configuration cache and build cache are **on** — keep build logic configuration-cache compatible).
- **Plugin descriptor**: `src/main/resources/META-INF/plugin.xml` registers all extensions (`<toolWindow>`, `<postStartupActivity>`) and points at the `messages.MyBundle` resource bundle. Any new extension point, service, or action must be declared here.
- **i18n**: User-facing strings live in `src/main/resources/messages/MyBundle.properties` and are looked up via the `MyBundle` object (`MyBundle["key", args...]`).
- **Tests**: `BasePlatformTestCase` (JUnit 4) with the Platform test framework. Test data lives under `src/test/testData/` (e.g. `rename/foo.xml` + `rename/foo_after.xml` pairs drive `myFixture.testRename`); `getTestDataPath()` points the fixture at the right subdirectory.
