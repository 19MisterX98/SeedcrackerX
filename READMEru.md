# SeedCrackerX [![Github All Releases](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]

## Я активный в:

- [Дискорд](https://discord.gg/JRmHzqQYfp)
- [Ютуб](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA)

## Установка

Скачайте и установите [закгрузчик fabric](https://fabricmc.net/use/)
 
Скачайте последний [рилиз или пре-релиз](https://github.com/19MisterX98/SeedCrackerX/releases) SeedCrackerX
  
поместить jar файл в папку с модами, или %appdata%/.minecraft/mods/, или папка вашего лаунчера
  
#### Опцианально
  
Скачайте последний [релиз](https://github.com/Earthcomputer/multiconnect/releases) Multiconnect, что бы заходить на сервера на старых версиях
  
## База данных

Потому что мод используется большим кол-вом людей, я решил создать гугл таблицу сидов серверов.
Если включить базу данных в конфиге мода, то сиды сервер с 10+ игроками использующими мод отправятся прямо в таблицу.

[Таблица](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing)

## Использование

### 1.17.X и меньше

Бегайте по миру пока мод не найдёт спавнер. Двльше процесс пойдёт автоматически.
Если мод не даст сид, найдите другой спавнер.

Мод также поддерживает:
- [структуры и башни в энде](https://youtu.be/aUuPSZVPH8E?t=462)
- [искажённые большие грибы](https://www.youtu.be/HKjwgofhKs4)

### 1.18.X и потенциальные будущие версии

Нахождение спавнерами, грибы теперь не работают.

Зайдите в конфиг с помощью "/seed gui" и выключите поля: Emeralds, Gateways, Biomes, Desert wells и Warped fungi, они могут дать неправильные данные.

Для взлома сида нужно найти 5 структур из указаных ниже:\
Пустынные храмы, джунглевые храмы, домики ведьмь, крушения кораблей, иглу, аванпосты пилыдеров

Любая комбинация подойдёт. На пример: 3 крушения, 1 пирамида и 1 иглу.
Вы можете следить за прогресом с помощью "/seed data bits"
Структура найдена, если она обведена.
После того как вы нашли достаточно, процесс начнётся автоматичесеи. Это займёт 1-5 мин
Мод может запросить больше структур.
Шансы этого больше, когда у вас меньше бит.
 
  ### Поддерживаемые структуры
    - Морской монумент
    - Город энда
    - Сокровище
    - Пустынный храм
    - Джунглевый храм
    - Домик ведьмы
    - Крушение корабля
    - Иглу
    - Аванпост разбойников
  
  ### Поддерживаемые декорации
    - Спавнеры
    - Выходы из дальнего энда
    - Пустыный колодец
    - Изумрудная руда
    - Искажённый большой гриб

## Команды
  
  ### Команда gui
  - `/seed gui`
  
  Открывает визуальный конфиг.
  
  ### Перезагрузка поисковика
  - `/seed finder reload`
  
  Пересканирует загруженые чанки.

  ### Данные
  - `/seed data clear`
  
  Очищает все собрыные данные.
  
  - `/seed data bits`
  
  Отображает количество собранных бит.
  
  - `/seed data restore`
  
  После выхода из мира мод сохраняет собранные данные.
  Вы можете загрузить их этой командой.
  
  
  ### Дебаг
  - `/seed cracker debug`

  Доп. информация
  
  ### База данных
  - `/seed database`
  
  Открывает [google таблицу](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing)
  
## Туториалы на аглийском

Neil:
- [1.15](https://youtu.be/1ChmLi9og8Q)
- [1.16](https://youtu.be/aUuPSZVPH8E)

Мои:
- [Dungeon cracking & end pillar cracking](https://youtu.be/8ytfZ2MXosY)
- [Nether cracking](https://youtu.be/HKjwgofhKs4)
- [Structure cracking](https://www.youtu.be/UXVrBaOR8H0)


## Настройка рабочего пространства

-Клонируй репозиторий.

-Запусти `gradlew genSources <idea|eclipse>`.

## Компиляция

-Обнови версию в `build.gradle` и `fabric.mod.json`.

-Запусти `gradlew build`.

## API для других модов

- Включи seedcracker-api и jitpack в build.gradle

      repositories {
          mavenCentral()
          maven { url "https://jitpack.io" }
      }
      
      dependencies {
          implementation (include('com.github.19MisterX98.SeedcrackerX:seedcrackerx-api:master-SNAPSHOT')) {transitive = false}
      }

- Добавь класа имлементирующий интерфейс

      package misterx.myMod.seedManagemnet.SeedCrackerEP
      
      import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
    
      public class SeedCrackerEP implements SeedCrackerAPI {
          @Override
          public void pushWorldSeed(long seed) {
              //do something
              Foo.bar(seed)
          }
      }

- Скажи fabric.mod.json где твоя входная точка

      "entrypoints": {
        "main": [...],
        "client": [...],
        "server": [...],
        "seedcrackerx": [
          "misterx.myMod.seedManagemnet.SeedCrackerEP"
        ]
      },

## Контрибуторы

[KaptainWutax](https://github.com/KaptainWutax) - Автор

[neil](https://www.youtube.com/watch?v=aUuPSZVPH8E) - Видео туториалы

[Nekzuris](https://github.com/Nekzuris) - README

[19MisterX98](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA) - Автор форка

[farkon00](https://github.com/farkon00) - перевод README на русский
