# SeedCrackerX [![Github 所有版本](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]
<!-- PS:请以后更新翻译的同学参照en_us.json改的地方并在同样的位置添加，让后人修改时更方便。-->
最好看英文版,中文版可能较旧

## 其他语言自述

[English](../README.md)
[Русский](READMEru.md)

## 我活跃在:

- [我的 Discord](https://discord.gg/JRmHzqQYfp)
- [Youtube](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA)

## 安装
下载并安装[fabric模组加载器](https://fabricmc.net/use/)
 
下载并安装SeedCrackerX的[发布或预发布](https://github.com/19MisterX98/SeedCrackerX/releases)版
  
把.jar文件放在你的mods目录下, 或者是%appdata%/.minecraft/mods/文件夹, 用于官方启动器, 或者是你自己的MultiMC实例文件夹.

#### 可选

下载最新的Multiconnect[发布版](https://github.com/Earthcomputer/multiconnect/releases)用于连接到低版本的MC服务器

## 数据库

由于mod被很多人使用,我决定为服务器种子创建一个Google表格.
如果您在配置gui中启用数据库选项,该mod将直接发送被10个以上玩家破解的服务器的种子到Google表格.

[表格](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing)

## 使用方法
### 1.17.X及以下

在这个世界上跑来跑去，直到Mod找到一个地牢.当Mod找到一个地牢后，破解过程就会自动开始.
如果它没有获取世界种子，你可能需要找另一个地牢.

此Mod也支持通过以下方式来破解种子.
- [结构和黑曜石柱](https://youtu.be/aUuPSZVPH8E?t=462)
- [诡异菌](https://youtu.be/HKjwgofhKs4)

### 1.18.X及未来版本

地牢破解，诡异菌破解不再起作用。

通过“/seed gui”进入配置菜单并确保绿宝石矿石、末地折跃门、生物群系、沙漠水井和诡异菌被禁用,因为它们没有更新并且可能提供错误的数据.

为了破解你现在需要找到下列结构中至少 5 个：
沙漠神殿、丛林神殿、女巫小屋、沉船、雪屋、掠夺者前哨站。

任何组合都是有效的。例如：3 艘沉船、1 座金字塔和 1 座冰屋。
您可以使用“/seed data bits”查看您的进程。(查看可挖掘结构的位数)
当结构周围有轮廓时，就代表Mod找到了结构。
在你得到足够的数据后，破解过程会自动开始。需要 1-5 分钟。
在这之后，mod可能会要求你寻找额外的结构。
相同类型的比特和结构越少，就越有可能找到。
减少你的结构种子后,该mod将通过地牢位置或散列的种子对你的世界种子进行暴力破解.

  ### 支持的结构
    - Ocean Monument(海底神殿)
    - End City(末地城)
    - Buried Treasure(埋藏的宝藏)
    - Desert Pyramid(沙漠神殿)
    - Jungle Temple(丛林神庙)
    - Swamp Hut(沼泽小屋)
    - Shipwreck(沉船)
    - Igloo(雪屋)
    - Pillager Outpost(掠夺者前哨站)
    
  ### 支持的装饰
    - Dungeon(地牢)
    - End Gateway(末地折跃门)
    - Desert Well(沙漠水井)
    - Emerald Ore(绿宝石矿石)
    - Warped Fungus(诡异菌)

## 命令.

  ### GUI命令
  - `/seed gui`
  打开配置gui，您可以在其中修改服务器MC版本等设置,所有搜索器、数据库和渲染模式.
  大多数命令都有替代品，所以它们不应该再被使用了
  
  ### 搜索器重新搜索命令
  - `/seed finder reload`

  重新扫描加载的区块以查找以前未找到的结构.

  ### 数据指令
  - `/seed data clear`
  
  无需重新登录即可清除所有收集的数据. 这对多世界的服务器很有用.
  
  - `/seed data bits`
  
  显示已经收集了多少比特的信息. 尽管这是一个近似值, 但它可以作为一个很好的参考来猜测什么时候应该开始进行暴力破解。
  普通比特用于黑曜石柱和结构破解. 破解从 32 位开始.
  挖掘比特用于可挖掘结构破解. 破解从 40 位开始.
  
  - `/seed data restore`
  
  当你离开一个世界时, mod会将当前收集的结构信息保存在一个文件中,在 .minecraft/config 目录.
  重新进入后, 你可以用此命令恢复它.
  
  ### 调试命令
  - `/seed cracker debug`

  显示调试信息
  
  ### 数据库命令
  - `/seed database`
  
  打开一个[google表格](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing)由mod维护

## 视频教程

Neil的:
- [1.15](https://youtu.be/1ChmLi9og8Q)
- [1.16](https://youtu.be/aUuPSZVPH8E)


我的:
- [地牢破解和黑曜石柱破解](https://youtu.be/8ytfZ2MXosY)
- [下界破解](https://youtu.be/HKjwgofhKs4)
- [结构破解](https://youtu.be/UXVrBaOR8H0)
## 设置工作区

-克隆此仓库.

-运行 `gradlew genSources <idea|eclipse>`.

## 编译此Mod

-在 `build.gradle` 和 `fabric.mod.json` 里更新版本.

-运行 `gradlew build`.

## 对于其他mod的API接口

- 在你的build.gradle中包含seedcracker-api和jitpack

      repositories {
          mavenCentral()
          maven { url "https://jitpack.io" }
      }
      
      dependencies {
          implementation (include('com.github.19MisterX98.SeedcrackerX:seedcrackerx-api:master-SNAPSHOT')) {transitive = false}
      }

- 添加一个实现api接口的类

      package misterx.myMod.seedManagemnet.SeedCrackerEP
      
      import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
    
      public class SeedCrackerEP implements SeedCrackerAPI {
          @Override
          public void pushWorldSeed(long seed) {
              //do something
              Foo.bar(seed)
          }
      }

- 告诉fabric.mod.json你的入口点在哪里

      "entrypoints": {
        "main": [...],
        "client": [...],
        "server": [...],
        "seedcrackerx": [
          "misterx.myMod.seedManagemnet.SeedCrackerEP"
        ]
      },

## 贡献者

[KaptainWutax](https://github.com/KaptainWutax) - 原作者

[neil](https://www.youtube.com/watch?v=aUuPSZVPH8E) - 视频教程

[Nekzuris](https://github.com/Nekzuris) - README

[19MisterX98](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA) - 作者
