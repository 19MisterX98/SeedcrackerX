# SeedCrackerX [![Github All Releases](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]

## Readme Language

[中文](./READMEzh.md)
[Русский](./READMEru.md)

## I'm active on:

- [My Discord](https://discord.gg/JRmHzqQYfp)
- [Youtube](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA)

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

## Usage

### 1.17.X and below

Run around in the world until the mod finds a dungeon. After the mod found one the cracking process starts automatically.
If it doesn't give you a world seed, you may want to find another dungeon.

This mod also supports cracking the seed via:
- [Structures and Endpillars](https://youtu.be/aUuPSZVPH8E?t=462)
- [warped fungus](https://www.youtu.be/HKjwgofhKs4)

  ### Supported Decorators
    - Dungeon
    - End Gateway
    - Desert Well
    - Emerald Ore
    - Warped Fungus

### 1.18.X and higher

Dungeon cracking, fungus cracking don't work anymore.

Go to the config menu via "/seedcracker gui" (1.19.3+) or "/seed gui" (before 1.19.3) and make sure that Emeralds, Gateways, Biomes, Desert wells and Warped fungi are disabled since they aren't updated and can give wrong data.

For cracking, you now need to find 5 structures from the listed ones:

  ### Supported Structures
    - Ocean Monument
    - End City
    - Buried Treasure
    - Desert Pyramid
    - Jungle Temple
    - Swamp Hut
    - Shipwreck
    - Igloo
    - Pillager Outpost

Any combination is valid. For example: 3 shipwrecks, 1 pyramid and 1 igloo.
You can track your process with "/seed data bits" (look at the bits count for liftable structures)
A structure is found when there is an outline around it.
After you get enough, the cracking process starts automatically. This process takes around 1-5 mins.
The mod may ask you to find additional structures after this.
It's more likely to happen with fewer bits and structures of the same type.
After reducing your structure seeds, the mod will brute force your world seed via dungeon positions or hashed seed.

## Commands

If version of your mod is older than 2.13.1, use prefix
 - "/seed" instead of "/seedcracker"

  ### GUI Command
  - `/seed gui`
  
  Opens the config gui where you can modify settings like the server mc-version, all finders, database and rendermode.
  There are command alternatives for most of this, but they should'nt be used anymore.
  
  ### Finder Reload Command
  - `/seed finder reload`

  Rescans the loaded Chunks to find structures that weren't found before.

  ### Data Command
  - `/seed data clear`
  
  Clears all the collected data without requiring a relog. This is useful for multi-world servers.
  
  - `/seed data bits`
  
  Display how many bits of information have been collected.
  Normal bits are used for end pillar + structure cracking. Cracking starts at 32 bits.
  Lifting bits are used for liftable structure cracking. Cracking starts at 40 bits.
  
  - `/seed data restore`
  
  When you leave a world, the mod will save currently collected structure information in a file of the .minecraft/config directory.
  After rejoining, you can restore it with this command.
  
  
  ### Debug Command
  - `/seed cracker debug`

  Additional info is shown
  
  ### Database Command
  - `/seed database`
  
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
- [Structure cracking](https://www.youtu.be/UXVrBaOR8H0)


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

[KaptainWutax](https://github.com/KaptainWutax) - Author

[neil](https://www.youtube.com/watch?v=aUuPSZVPH8E) - Video Tutorial

[Nekzuris](https://github.com/Nekzuris) - README

[19MisterX98](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA) - Author of this fork

[farkon00](https://github.com/farkon00) - README in russian
