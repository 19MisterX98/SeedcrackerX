# SeedCracker [![Github All Releases](https://img.shields.io/github/downloads/KaptainWutax/SeedCracker/total.svg)]()

## Installation

 ### Vanilla Launcher

  Download and install the [fabric mod loader](https://fabricmc.net/use/).

 ### MultiMC

  Add a new minecraft instance and press "Install Fabric" in the instance options.


  Then download the lastest [release](https://github.com/KaptainWutax/SeedCracker/releases) of SeedCracker and put the `.jar` file    in your mods directory, either `%appdata%/.minecraft/mods/` folder for the vanilla launcher or your own MultiMC instance folder.

## Usage

  Run minecraft with the mod installed and run around in the world. Once the mod has collected enough data, it will start the cracking process automatically and output the seed in chat. For the process to start, the amount of data that needs to be collected varies depending on the type of feature. `/seed data bits` can be used to see how much progress has been done. 
  
  ### Supported Structures
    - Ocean Monument
    - End City
    - Buried Treasure
    - Desert Pyramid
    - Jungle Temple
    - Swamp Hut
    - Shipwreck
  
  ### Supported Decorators
    - Dungeon
    - End Gateway
    - Desert Well
    - Emerald Ore

## Commands

  The command prefix for this mod is /seed.
  
  ### Render Command  
  -`/seed render outlines <ON/OFF/XRAY>`
    
  This command only affects the renderer feedback. The default value is 'XRAY' and highlights data through blocks. You can set    the render mod to 'ON' for more standard rendering. 
  
  ### Finder Command
  -`/seed finder type <FEATURE_TYPE> (ON/OFF)`
  
  -`/seed finder category (BIOMES/ORES/OTHERS/STRUCTURES) (ON/OFF)`
  
  This command is used to disable finders in case you are aware the data is wrong. For example, a map generated in 1.14 has different decorators and would require you to disable them while going through those chunks.

  ### Data Command
  - `/seed data clear`
  
  Clears all the collected data without requiring a relog. This is useful for multi-world servers.
  
  - `/seed data bits`
  
  Display how many bits of information have been collected. Even though this is an approximate, it serves as a good basis to guess when the brute-forcing should start.
  
  ### Cracker Command
  - `/seed cracker <ON/OFF>`
 
  Enables or disables the mod completely. Unlike the other commands, this one is persistent across reloads.
  
## Video Tutorial

https://youtu.be/1ChmLi9og8Q

## Upcoming Features

A list of features I have on my mind... they won't necessarily be implemented in this order if at all.

    - SHA2 brute-forcing, auxiliary to biomes search. /implemented
    - Dungeon floor cracker, fast lattice reversal. /implemented
    - Stronghold portal room cracker. (alternative to dungeon floor?)
    - Faster brute-forcing by reorganizing located features list. /implemented
    - End and nether biome finders. (nether would mostly be in preparation for 1.16) /implemented

## Setting up the Workspace

-Clone the repository.

-Run `gradlew genSources <idea|eclipse>`.

## Building the Mod

-Update the version in `build.gradle` and `fabric.mod.json`.

-Run `gradlew build`.
 
## Contributors

[KaptainWutax](https://github.com/KaptainWutax) - Author

[neil](https://www.youtube.com/watch?v=aUuPSZVPH8E) - Video Tutorial

[Nekzuris](https://github.com/Nekzuris) - README
