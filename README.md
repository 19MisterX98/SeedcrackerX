# SeedCrackerX [![Github All Releases](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]()

## Installation [structures and endpillars](https://github.com/buiawpkgew1/SeedcrackerX/blob/patch-1/READMEzh.md)

 ### Vanilla Launcher

  Download and install the [fabric mod loader](https://fabricmc.net/use/).

 ### MultiMC
 
  Add a new minecraft instance and press "Install Fabric" in the instance options.

 ### Mod Installation
 
  Download the lastest [release](https://github.com/19MisterX98/SeedCrackerX/releases) of SeedCrackerX
  
  Download the lastest [release](https://www.curseforge.com/minecraft/mc-mods/modmenu/files) of ModMenu
  
  Download the lastest [release](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files) of Fabric API
  
  
  put the .jar files in your mods directory, either %appdata%/.minecraft/mods/ folder for the vanilla launcher or your own MultiMC instance folder.
  
 #### Optional
  
  Download the lastest [release](https://github.com/Earthcomputer/multiconnect/releases) of Multiconnect to connect to servers on lower MC versions
  
## Usage

  Run around in the world until the mod finds a dungeon. After the mod found one the cracking process starts automaticly. If it doesnt get you a world seed you may want to find another dungeon. This mod also supports cracking the seed via [structures and endpillars](https://youtu.be/aUuPSZVPH8E?t=462) and [warped fungus](https://www.youtube.com/watch?v=HKjwgofhKs4)
  
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
    - Warped Fungus

## Commands(Deprecated, use the GUI instead)

  The command prefix for this mod is /seed.
  
  ### Render Command  
  -`/seed render outlines <ON/OFF/XRAY>`
    
  This command only affects the renderer feedback. The default value is 'XRAY' and highlights data through blocks. You can set    the render mod to 'ON' for more standard rendering. 
  
  ### Finder Command
  -`/seed finder type <FEATURE_TYPE> (ON/OFF)`
  
  -`/seed finder category (BIOMES/ORES/OTHERS/STRUCTURES) (ON/OFF)`
  
  This command is used to disable finders in case you are aware the data is wrong. For example, a map generated in 1.14 has different decorators and would require you to disable them while going through those chunks.
  
  -`/seed finder reload`
  
  Searches the loaded area again

  ### Data Command
  - `/seed data clear`
  
  Clears all the collected data without requiring a relog. This is useful for multi-world servers.
  
  - `/seed data bits`
  
  Display how many bits of information have been collected. Even though this is an approximate, it serves as a good basis to guess when the brute-forcing should start.
  
  ### Cracker Command
  - `/seed cracker <ON/OFF>`
 
  Enables or disables the mod completely. Unlike the other commands, this one is persistent across reloads.
  
  - `/seed cracker debug`

  Additional info is shown
  
## Video Tutorials

https://youtu.be/1ChmLi9og8Q

https://youtu.be/8ytfZ2MXosY

## Upcoming Features

A list of features I have on my mind... they won't necessarily be implemented in this order if at all.

    - Stronghold portal room cracker. (alternative to dungeon floor?)
    - Tree data collection (probably only oak and birch. Puts info into a file that can be compiled to run on GPU)

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

[19MisterX98](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA) - Author of this fork
