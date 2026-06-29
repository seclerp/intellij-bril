<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Intellij Bril Changelog

## [Unreleased]
### Added
- **Go to Declaration** — operands now resolve to their definitions: variables to their `arg`/`const`/value destinations, `@`-operands to functions, and `.`-operands to labels.
- **Find Usages** — support for finding usages of Bril definitions.

## [0.1.0]
### Added
- Initial release: Bril text-format file type, lexer, parser, and syntax highlighting, plus line-comment and brace-matching support.

[Unreleased]: https://github.com/seclerp/intellij-bril/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/seclerp/intellij-bril/releases/tag/v0.1.0
