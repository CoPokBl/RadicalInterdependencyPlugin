# Radical Interdependency Plugin
This is a Spigot plugin for 1.21+ that forces players to work together by limiting
what they can do on their own. Each player is given a "role" that lets them do certain things.

- This game must be played with at least 4 players (more will result in multiple players having the same roles).
- This game also must be played with a shared inventory plugin running, such as 
[Minecraft Shared Inventory](https://github.com/Matistan/MinecraftSharedInventory) by Matistan.

## Setup
- Place a built .jar in the plugins folder.
- Have everyone join the server.
- Use `/ri start` to randomly assign roles and start the game.
- You can remove all roles by running `/ri reset`.

## Features
- 4 roles
- Persists roles through server restart and player disconnects.

## Roles
If a role explicitly grants an ability it means that no other player can do that action.
For example miner explicitly permits breaking blocks, so no other players can break blocks.

| Role Name | Description                          |
|-----------|--------------------------------------|
| Miner     | Can break blocks and pickup liquids. |
| Builder   | Can place blocks and liquids.        |
| Mercenary | Can damage entities.                 |
| Cleric    | Can interact with inventories and pickup items. |

