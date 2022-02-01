# SeedCrackerX [![Github 所有版本](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]

## Discord

[Discord](https://discord.gg/JRmHzqQYfp)

## 安装

 ### 官方启动器/其他启动器

  下载并安装[Fabric](https://fabricmc.net/use/).

 ### MultiMC
 
  添加一个新的Minecraft实例, 在实例选项中点击 "安装Fabric".

 ### 模组安装
 
  下载 SeedCrackerX 最新[发布版](https://github.com/19MisterX98/SeedCrackerX/releases)
  
  下载 Mod Menu 最新[发布版](https://www.curseforge.com/minecraft/mc-mods/modmenu/files)
  
  下载 Fabric API 最新[发布版](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files)
  
  
  把.jar文件放在你的mods目录下, 或者是%appdata%/.minecraft/mods/文件夹, 用于官方启动器, 或者是你自己的MultiMC实例文件夹.
  
 #### 可选
  
  下载 Multiconnect 最新[发布版](https://github.com/Earthcomputer/multiconnect/releases) 来连接到低版本的MC服务器
  
## 使用方法

  在这个世界上跑来跑去, 直到Mod找到一个地牢. 当Mod找到一个地牢后, 破解过程就会自动开始. 如果它没有获取世界种子, 你可能需要找另一个地牢. 此Mod也支持通过[结构和黑曜石柱](https://youtu.be/aUuPSZVPH8E?t=462)和[诡异菌](https://www.youtube.com/watch?v=HKjwgofhKs4)来破解种子.
  
  ### 支持的结构
    - Ocean Monument(海底神殿)
    - End City(末地城)
    - Buried Treasure(埋藏的宝藏)
    - Desert Pyramid(沙漠神殿)
    - Jungle Temple(丛林神庙)
    - Swamp Hut(沼泽小屋)
    - Shipwreck(沉船)
  
  ### 支持的装饰
    - Dungeon(地牢)
    - End Gateway(地牢)
    - Desert Well(沙漠水井)
    - Emerald Ore(绿宝石矿石)
    - Warped Fungus(诡异菌)

## 命令（已废弃，请使用GUI）.

  这个mod的命令前缀是/seed.
  
  ### 渲染命令
  -`/seed render outlines <ON/OFF/XRAY>`
    
  这个命令只影响到渲染器的反馈. 默认值是'XRAY', 通过方块来显示数据. 你可以将渲染模式设置为'ON', 以获得更标准的渲染效果.
  
  ### 搜索器命令
  -`/seed finder type <FEATURE_TYPE> (ON/OFF)`
  
  -`/seed finder category (BIOMES/ORES/OTHERS/STRUCTURES) (ON/OFF)`
  
  这个命令用于禁用查找器, 以防你意识到数据是错误的. 例如, 在1.14中生成的地图有不同的装饰, 需要你在经过这些区块的时候禁用它们.
  
  -`/seed finder reload`
  
  再次搜索加已加载的区域

  ### 数据指令
  - `/seed data clear`
  
  清除所有收集的数据而不是重新记录. 这对多世界的服务器很有用。
  
  - `/seed data bits`
  
  显示已经收集了多少比特的信息. 尽管这是一个近似值, 但它可以作为一个很好的参考来猜测什么时候应该开始进行暴力破解。
  
  ### 破解器指令
  - `/seed cracker <ON/OFF>`
 
  完全启用或禁用MOD. 与其他命令不同的是, 这个效果在重载时仍然保持。
  
  - `/seed cracker debug`

  显示调试信息
  
## 视频教程

https://youtu.be/1ChmLi9og8Q

https://youtu.be/8ytfZ2MXosY

## 即将推出的功能

我心中的功能清单......它们不一定会按照这个顺序实施.

    - 地牢传送门房间的种子破解器。(替代地牢的地板? )
    - 树木数据收集（可能只有橡树和桦树.将信息放入一个文件，可以在GPU上编译运行)

## 设置工作区

-克隆此仓库.

-运行 `gradlew genSources <idea|eclipse>`.

## 编译此Mod

-在 `build.gradle` 和 `fabric.mod.json` 里更新版本.

-运行 `gradlew build`.
 
## 贡献者

[KaptainWutax](https://github.com/KaptainWutax) - 原作者

[neil](https://www.youtube.com/watch?v=aUuPSZVPH8E) - 视频教程

[Nekzuris](https://github.com/Nekzuris) - README

[19MisterX98](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA) - 作者
