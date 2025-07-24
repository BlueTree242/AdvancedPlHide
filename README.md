![AdvancePlHide](./.github/banner.png)

# Table of Contents

- [Introduction](#introduction)
    - [What is this plugin?](#what-is-this-plugin)
    - [Features](#features)
- [Requirements](#requirements)
- [How to Use](#how-to-use)
- [Notes](#notes)
- [License](#license)

# Introduction

## What is this plugin?

**AdvancePlHide** is a plugin designed to control what the client can see when using tab-completion.

This kind of functionality isn’t widely available in the plugin ecosystem—so this plugin was created to fill that gap.

It doesn’t just hide `plugin:command` entries from the tab list; it also blocks plugin completions from showing up after
commands like `/ver` (try it on your server without this plugin and watch your plugin list!).

The plugin also gives you full control over which commands are visible to clients, so your plugins don’t accidentally
expose themselves.  
You can specify a list of visible commands or block specific ones from showing up.
> **Note:** This only affects tab-completion—it does **not** prevent commands from being executed.

## Features

- Group-based architecture; compatible with any permission manager.
- Hides plugin lists from common commands available on all servers.
- Removes `plugin:command` from the client’s visible command list.
- Automatically blocks commands like `/plugins` and similar.
- Gives you full control over tab-completion behavior.
- Lightweight and performance-focused.
- Cannot be bypassed by custom clients using special commands to reveal plugins.
- Supports **Minecraft versions 1.8 to 1.21.8**, with more coming soon.
- Works on Spigot, Paper (and forks), BungeeCord, and Velocity.
- 100% free and open-source.

# Requirements

### Spigot & Paper

- Supported versions: **1.8 – 1.21.8**+
- Requires: [ProtocolLib](https://github.com/dmulloy2/ProtocolLib)

### Paper 1.20.6+

- Supported versions: **1.20.6 – 1.21.8** (and newer)
- No dependencies required — it works out of the box.

### BungeeCord & Velocity

- Requires: [Protocolize](https://github.com/Exceptionflug/protocolize)

# How to Use

Check out the [documentation](https://github.com/BlueTree242/AdvancedPlHide/wiki) to get started.

# Notes

- This plugin is actively updated for the latest Minecraft versions until v3 is released.
- No new features will be added to v2 — only bug fixes and compatibility updates.
- The plugin works immediately after installation, but advanced features require configuration.
- Sometimes Mojang does some breaking changes and plugin has to be updated to support those newer versions, this is not very common so you should test if plugin works before opening an issue. This is strictly for **Spigot** only (even Paper is unaffected).


> Occasionally, Mojang introduces breaking changes that may require the plugin to be updated for compatibility with newer versions. While this is relatively rare, you should always test the plugin on the target version before reporting any issues.  
> This notice applies **strictly to Spigot**—Paper, Bungee and Velocity are unaffected.

> It is recommended to set the permission `bukkit.command.*` to `false` for all players.

# License

[![License](https://img.shields.io/github/license/Bluetree242/AdvancedPlHide?color=e)](https://github.com/Bluetree242/AdvancedPlHide/blob/master/LICENSE)