<img align="left" width="128" height="128" src="src/main/resources/META-INF/pluginIcon.svg">
<h3>
  Bril language support for IntelliJ-based IDEs
  &nbsp;
  <a href="https://github.com/seclerp/intellij-bril/actions/workflows/build.yml"><img src="https://github.com/seclerp/intellij-bril/workflows/Build/badge.svg" alt="Build" align="absmiddle"></a>
  <a href="https://plugins.jetbrains.com/plugin/MARKETPLACE_ID"><img src="https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg?label=Plugin&colorB=0A7BBB" alt="Version" align="absmiddle"></a>
  <a href="https://plugins.jetbrains.com/plugin/MARKETPLACE_ID"><img src="https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg?label=Downloads&colorB=0A7BBB" alt="Downloads" align="absmiddle"></a>
</h3>

This plugins adds an editor support for Bril – the educational compiler IR language.

<br/>

<!-- Plugin description -->
A plugin for IntelliJ-based IDEs that adds editor support for [Bril](https://capra.cs.cornell.edu/bril/), the
**Big Red Intermediate Language** — a small instruction-representation language built for compilers
education. The plugin understands Bril's human-readable text format (the one produced by `bril2txt`).

### Features

- **File type** — recognizes `.bril` files.
- **Lexing & parsing** — a full lexer and parser for the Bril text format (functions, typed arguments
  and return types, `const`/value/effect operations, labels, and parameterized types such as `ptr<int>`),
  with error highlighting.
- **Syntax highlighting** — comments, literals, `@function` and `.label` identifiers, primitive types
  (`int`, `bool`, `float`, `char`, `ptr`), and core/extension opcodes (`add`, `br`, `call`, `print`,
  `alloc`, `phi`, …). Colors are configurable under <kbd>Settings</kbd> > <kbd>Editor</kbd> >
  <kbd>Color Scheme</kbd> > <kbd>Bril</kbd>.
- **Editing aids** — `#` line-comment toggling (<kbd>Ctrl</kbd>+<kbd>/</kbd>) and brace matching for
  `{}`, `()`, and `<>`.

> Scope is currently lexing, parsing, and highlighting. References, code completion, and other
> navigation features are not implemented yet.
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "intellij-bril"</kbd> >
  <kbd>Install</kbd>

- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/seclerp/intellij-bril/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
