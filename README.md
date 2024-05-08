# SeedCrackerX [![Github All Releases](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]

## Readme Language

[中文](readmes/READMEzh.md)
[Русский](readmes/READMEru.md)
[Українська](readmes/READMEuk.md)
[Türkçe](readmes/READMEtr.md)

## I'm active on:

- [My Discord](https://discord.gg/JRmHzqQYfp)
- [Youtube](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA)

# Version Tab

| Minecraft Version | SeedCrackerX version                                                                                       | Dependencies                                                                                                                                                                                   |
|-------------------|------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.20.6            | [2.14.7](https://github.com/19MisterX98/SeedcrackerX/releases/download/prerelease/seedcrackerX-2.14.7.jar) | [Fabric mod loader 0.14.0+](https://fabricmc.net/use/)                                                                                                                                         |
| 1.20.4            | [2.14.6](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.14.6/seedcrackerX-2.14.6.jar)     | [Fabric mod loader 0.14.0+](https://fabricmc.net/use/)                                                                                                                                         |
| 1.20.2            | [2.14.5](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.14.5/seedcrackerX-2.14.5.jar)     | [Fabric mod loader 0.14.0+](https://fabricmc.net/use/)                                                                                                                                         |
| 1.20-1.20.1       | [2.14.4](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.14.4/seedcrackerX-2.14.4.jar)     | [Fabric mod loader 0.14.0+](https://fabricmc.net/use/)                                                                                                                                         |
| 1.19.4            | [2.14.2](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.14.2/seedcrackerX-2.14.2.jar)     | [Fabric mod loader 0.14.0+](https://fabricmc.net/use/)                                                                                                                                         |
| 1.19.3            | [2.13.1](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.13.1/seedcrackerX-2.13.1.jar)     | [Fabric mod loader 0.14.0+](https://fabricmc.net/use/)                                                                                                                                         |
| 1.19-1.19.2       | [2.13](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.13/seedcrackerX-2.13.jar)           | [Fabric mod loader 0.14.0+](https://fabricmc.net/use/)                                                                                                                                         |
| 1.18.2            | [2.12](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.12/seedcrackerX-2.12.jar)           | [Fabric mod loader](https://fabricmc.net/use/)                                                                                                                                                 | 
| 1.18-1.18.1       | [2.11.4](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.11.4/seedcrackerX-2.11.4.jar)     | [Fabric mod loader](https://fabricmc.net/use/)                                                                                                                                                 |
| 1.17-1.17.1       | [2.10.1](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.10.1/seedcrackerX-2.10.1.jar)     | [Fabric mod loader](https://fabricmc.net/use/)                                                                                                                                                 |
| 1.16.5            | [2.7](https://github.com/19MisterX98/SeedcrackerX/releases/download/2.7.1/seedcrackerX-0.2.7.jar)          | [Fabric mod loader](https://fabricmc.net/use/), [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api),  [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu/files) |

## Installation

Download and install the [fabric mod loader](https://fabricmc.net/use/)
 
Download the latest [release or pre-release](https://github.com/19MisterX98/SeedCrackerX/releases) of SeedCrackerX
  
put the .jar file in your mod directory, either %appdata%/.minecraft/mods/ folder for the vanilla launcher or your own instance folder.
  
#### Optional
  
Download the latest release of [Multiconnect](https://github.com/Earthcomputer/multiconnect/releases) or [ViaFabric](https://modrinth.com/mod/viafabric) to connect to servers on lower MC versions
  
## Database

Since the mod is used by many people, I have decided to create a Google sheet for server seeds.
If you enable the database option in the config gui the mod will send cracked seeds from 10+ player servers directly to the Google sheet.
You also need a minecraft license

[The Sheet](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing)

# Usage

## 1.17.X and below

### Overworld

Run around in the world until the mod finds a dungeon. After the mod found one the cracking process starts automatically.
If it doesn't give you a world seed, you may need to find another dungeon.

### The Nether

Run around in the warped fungus biome until the mod finds big fungus and starts cracking.

### The End

You have to find 5 (or more) end cities or end gateways and then return to the center of the end dimension to obtain the pillar seed, after that cracking should begin.

#### Video guides:
- [Structures and Endpillars](https://youtu.be/aUuPSZVPH8E?t=462)
- [warped fungus](https://youtu.be/HKjwgofhKs4)

  ### Supported Decorators
    - Dungeon
    - End Gateway
    - Desert Well
    - Emerald Ore
    - Warped Fungus

## 1.18.X and higher

Dungeon cracking, fungus cracking don't work anymore.

### Overworld

Go to the config menu via "/seedcracker gui" (1.19.3+) or "/seed gui" (before 1.19.3) and make sure that Emeralds, Gateways, Biomes, Desert wells and Warped fungi are disabled since they aren't updated and can give wrong data.

For cracking, you now need to get 40 bits of liftable structures and 32 regular bits from the listed ones:

  ### Supported Structures
  
    - Igloo - 9 regular and liftable bits
    - Desert Pyramid - 9 regular and liftable bits
    - Jungle Temple - 9 regular and liftable bits
    - Swamp Hut - 9 regular and liftable bits
    - Shipwreck - 8 regular and liftable bits
    - Pillager Outpost - 9 liftable bits
    - Ocean Monument - 9 regular bits

Any combination is valid. For example: 3 shipwrecks, 1 pyramid and 1 igloo.
You can track your process with "/seed data bits" (look at the bits count for liftable structures)
A structure is found when there is an outline around it.
After you get enough, the cracking process starts automatically. This process takes around 1-5 mins.
The mod may ask you to find additional structures after this.
It's more likely to happen with fewer bits and structures of the same type.
After reducing your structure seeds, the mod will brute force your world seed via dungeon positions or hashed seed.

### The Nether

Fungus cracking don't work anymore, so there are no way to find nether seed using SeedcrackerX, however you can try to crack it yourself by using [Nether_Bedrock_Cracker](https://github.com/19MisterX98/Nether_Bedrock_Cracker)
    
### The End

You have to find 5 (or more) end cities (fill up regular bits) and then return to the center of the end dimension to obtain the pillar seed, after that cracking should begin.

- You need to disable end gateways!

## Commands

If version of your mod is older than 2.13.1, use prefix
 - `/seed` instead of `/seedcracker`

  ### GUI Command
  - `/seedcracker gui`
  
  Opens the config gui where you can modify settings like the server mc-version, all finders, database and rendermode.
  There are command alternatives for most of this, but they shouldn't be used anymore.
  
  ### Finder Reload Command
  - `/seedcracker finder reload`

  Rescans the loaded Chunks to find structures that weren't found before.

  ### Data Command
  - `/seedcracker data clear`
  
  Clears all the collected data without requiring a relog. This is useful for multi-world servers.
  
  - `/seedcracker data bits`
  
  Display how many bits of information have been collected.
  Normal bits are used for end pillar + structure cracking. Cracking starts at 32 bits.
  Lifting bits are used for liftable structure cracking. Cracking starts at 40 bits.
  
  - `/seed data restore`
  
  When you leave a world, the mod will save currently collected structure information in a file of the .minecraft/config directory.
  After rejoining, you can restore it with this command.
  
  
  ### Debug Command
  - `/seedcracker cracker debug`

  Additional info is shown
  
  ### Database Command
  - `/seedcracker database`
  
  Opens a [google sheet](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing) that is maintained by the mod
  
## Video Tutorials

Neil's:
- [1.15](https://youtu.be/1ChmLi9og8Q)
- [1.16](https://youtu.be/aUuPSZVPH8E)

Dyiing's
- [1.18](https://www.youtube.com/watch?v=tKeEyx7jIE4)

Mine:
- [Dungeon cracking & end pillar cracking](https://youtu.be/8ytfZ2MXosY)
- [Nether cracking](https://youtu.be/HKjwgofhKs4)
- [Structure cracking](https://youtu.be/UXVrBaOR8H0)


## Setting up the Workspace

-Clone the repository.

-Run `gradlew genSources <idea|eclipse>`.

## Building the Mod

-Update the version in `build.gradle` and `fabric.mod.json`.

-Run `gradlew build`.

## API for other mods

- Include seedcracker-api and jitpack in your build.gradle

      repositories {
          mavenCentral()
          maven { url "https://jitpack.io" }
      }
      
      dependencies {
          implementation (include('com.github.19MisterX98.SeedcrackerX:seedcrackerx-api:master-SNAPSHOT')) {transitive = false}
      }

- Add a class that implements the api interface

      package misterx.myMod.seedManagemnet.SeedCrackerEP
      
      import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
    
      public class SeedCrackerEP implements SeedCrackerAPI {
          @Override
          public void pushWorldSeed(long seed) {
              //do something
              Foo.bar(seed)
          }
      }

- Tell fabric.mod.json where your entrypoint is

      "entrypoints": {
        "main": [...],
        "client": [...],
        "server": [...],
        "seedcrackerx": [
          "misterx.myMod.seedManagemnet.SeedCrackerEP"
        ]
      },

## Contributors

[KaptainWutax](https://github.com/KaptainWutax) - Author of the original mod

[neil](https://www.youtube.com/watch?v=aUuPSZVPH8E) - Video Tutorials

[Nekzuris](https://github.com/Nekzuris) and [ItzSkyReed](https://github.com/ItzSkyReed)  - README

[19MisterX98](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA) - Author of SeedCrackerX
