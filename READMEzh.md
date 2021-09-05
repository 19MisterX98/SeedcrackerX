# SeedCrackerX [![Github 所有版本](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]()

## 安装

 ### Vanilla Launcher

  下载并安装[fabric MOD 加载器](https://fabricmc.net/use/).

 ### MultiMC
 
  添加一个新的Minecraft实例，在实例选项中按 "安装织物"。

 ### 模组安装
 
  下载最新的[发布]。(https://github.com/19MisterX98/SeedCrackerX/releases) 的 SeedCrackerX
  
  下载最新的[发布]。(https://www.curseforge.com/minecraft/mc-mods/modmenu/files) 的模组菜单
  
  下载最新的[发布]。(https://www.curseforge.com/minecraft/mc-mods/fabric-api/files) 的 Fabric API
  
  
  把.jar文件放在你的mods目录下，或者是%appdata%/.minecraft/mods/文件夹，用于vanilla启动器，或者是你自己的MultiMC实例文件夹。
  
 #### 可选
  
  下载最新的[发布]。(https://github.com/Earthcomputer/multiconnect/releases) 的Multiconnect连接到较低的MC版本上的服务器
  
## 使用方法

  在这个世界上跑来跑去，直到MOD找到一个地牢。当mod找到一个地牢后，破解过程就会自动开始。如果它没有给你带来世界的种子，你可能想找另一个地牢。这个模型也支持通过[结构和尾巴](https://youtu.be/aUuPSZVPH8E?t=462)和[扭曲的菌类](https://www.youtube.com/watch?v=HKjwgofhKs4)来破解种子。
  
  ### 支持的结构 (机翻)
    - Ocean Monument(海洋纪念碑)
    - End City(终端城市)
    - Buried Treasure(埋藏的宝藏)
    - Desert Pyramid(沙漠金字塔)
    - Jungle Temple(丛林寺庙)
    - Swamp Hut(沼泽小屋)
    - Shipwreck(沉船事件)
  
  ### 支持的装饰公司(机翻)
    - Dungeon(地下城)
    - End Gateway(终端网关)
    - Desert Well(沙漠之井)
    - Emerald Ore(绿宝石矿石)
    - Warped Fungus(翘曲的真菌)

## 命令（已废弃，请使用GUI）. (机翻)

  这个mod的命令前缀是/seed。
  
  ### 渲染命令
  -`/seed render outlines <ON/OFF/XRAY>`
    
  这个命令只影响到渲染器的反馈。默认值是'XRAY'，通过块来突出数据。你可以将渲染模式设置为'ON'，以获得更标准的渲染效果. 
  
  ### 搜索器命令
  -`/seed finder type <FEATURE_TYPE> (ON/OFF)`
  
  -`/seed finder category (BIOMES/ORES/OTHERS/STRUCTURES) (ON/OFF)`
  
  这个命令用于禁用查找器，以防你意识到数据是错误的。例如，在1.14中生成的地图有不同的装饰器，需要你在翻阅这些块的时候禁用它们.
  
  -`/seed finder reload`
  
  再次搜索加载的区域

  ### 数据指令
  - `/seed data clear`
  
  清除所有收集的数据而不需要重新登录.这对多世界的服务器很有用.
  
  - `/seed data bits`
  
  显示已经收集了多少比特的信息。尽管这是一个近似值，但它可以作为一个很好的基础来猜测什么时候应该开始进行暴力破解.
  
  ### 裂解器指令
  - `/seed cracker <ON/OFF>`
 
  完全启用或禁用MOD。与其他命令不同的是，这个命令在重新加载时是持久的。.
  
  - `/seed cracker debug`

  其他信息显示
  
## 视频教程

https://youtu.be/1ChmLi9og8Q

https://youtu.be/8ytfZ2MXosY

## 即将推出的功能

我心中的功能清单......如果有的话，它们不一定会按照这个顺序实施.

    - 堡垒门户房间的裂缝。(替代地牢的地板？)
    - 树木数据收集（可能只有橡树和桦树.将信息放入一个文件，可以在GPU上编译运行)

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
