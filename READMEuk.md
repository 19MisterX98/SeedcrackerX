# SeedCrackerX [![Github All Releases](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]

## Мої активності:

- [Мій Discord](https://discord.gg/JRmHzqQYfp)
- [Youtube](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA)

## Встановлення

Завантажте та встановіть [fabric](https://fabricmc.net/use/)
 
Завантажте найновіший [реліз або пре-реліз](https://github.com/19MisterX98/SeedCrackerX/releases) SeedCrackerX
  
перемістіть .jar файли в вашу папку з модами, або у папку %appdata%/.minecraft/mods/ для офіційного лаунчера або в вашу папку MultiMC.
  
#### Опціональне
  
Завантажте останній реліз [Multiconnect](https://github.com/Earthcomputer/multiconnect/releases) або [ViaFabric](https://modrinth.com/mod/viafabric), щоб підключатися до серверів на старіших версіях Майнкрафт.
  
## База даних

Через те що мод використовується багатьма людьми, я створив Google таблицю для сідів серверів.
Якщо ви ввімкнете опцію бази даних в графічних налаштуваннях моду, то він відправлятиме знайденні сіди з серверів з 10+ гравцями.
Вам також буде потрібна ліцензія Майнкрафт.

[Таблиця](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing)

## Використання

### 1.17.X та нижче

Побігайте по мапі поки ви не знайдете спавнер. Після того як мод побачив цей спавнер, процес знаходження сіда почнеться автоматично.
Якщо мод не дасть сід, то ви маєте знайти ще один спавнер.

Також мод підтримую знаходження сіда через:
- [Структури й вежі краю](https://youtu.be/aUuPSZVPH8E?t=462)
- [Скажені гриби](https://youtu.be/HKjwgofhKs4)

### 1.18.X та вище

Знаходження за допомогою спавнерів та скажених грибів більше не працює.

Зайдіть в налаштування за допомогою "/seed gui" й перевірте, що ізумруди, портали у Краї, біоми, колодязі в пустелі та скажені гриби вимкненні, тому що вони можуть дати неправильні дані.

Для знаходження сіда, з вас потребується знайти 5 структур з зазначених нижче:\
Пустельні піраміди, Храми у джунглях, Хати відьми, Розбиті кораблі, Іглу, Аванпости Розбійників

Будь-яка комбінація підійде. На приклад: 3 розбитих кораблі, 1 піраміда та 1 іглу.
Ви можете перевірити свій прогрес за допомогою "/seed data bits" (подивіться на кількість біт у "liftable structures")
Структура була знайдена модом, якщо вона обведена.
Після того як ви отримали достатньо автоматично почнеться пошук. Це займе приблизно 1-5 хв.
Мод може запросити знайти ще структур після цього.
Це більш імовірно, якщо у вас менше бітів та однакові структури.
Після того як мод знайшов сід структур, він перебере сід мапи.
 
  ### Підтриманні структури
    - Підводний Монумент
    - Місто Краю
    - Захованні скарби
    - Пустельні Піраміди
    - Храми у джунглях
    - Хати відьми
    - Розбиті Кораблі
    - Іглу
    - Аванпости Розбійників
  
  ### Підтриманні декорації
    - Спавнери
    - Портали у Краї
    - Пустельні Колодязі
    - Ізумрудові руди
    - Скажені Гриби

## Команди

 Для 1.19.3 використовуйте команду 
 - `/seedcracker`

  ### Команда графічного інтерфейсу
  - `/seed gui`
  
  Відкриває графічний інтерфейс з налаштуваннями, наприклад версія сервера, всі шукачі та база даних.
  Для цього всього є альтернативні команди, але не рекомендовано їми користуватися.
  
  ### Команда перезапуску пошукача
  - `/seed finder reload`

  Пересканує завантаженні чанки, щоб знайти структури, які не були знайдені.

  ### Команди даних
  - `/seed data clear`
  
  Видаляє усі зібранні дані. Корисно на мульті-мапових серверах. 
  
  - `/seed data bits`
  
  Відображає скільки бітів інформації було зібрано. 
  Звичайні біти використовуються для веж Краю та структур. Пошук починається, коли було зібрано як найменш 32 біти.
  Підйомні біти використовуються для підйомних структур. Пошук починається, коли було зібрано як найменш 40 біт.
  
  - `/seed data restore`
  
  Коли ви виходите з сервера, мод зберігає зібрані структури в файл у папці .minecraft/config.
  Після перепідключення ви можете вернути цю інформацію цією командою.
  
  
  ### Команди відладки
  - `/seed cracker debug`

  Показ додаткової інформації.
  
  ### Команди бази даних
  - `/seed database`
  
  Відкриває [Google таблицю](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing), у яку мод додає сіди серверів
  
## Відео туторіали(англійська)

Neil:
- [1.15](https://youtu.be/1ChmLi9og8Q)
- [1.16](https://youtu.be/aUuPSZVPH8E)


Dyiing
- [1.18](https://www.youtube.com/watch?v=tKeEyx7jIE4)


Від мене:
- [Знаходження за допомогою веж Краю та спавнерів](https://youtu.be/8ytfZ2MXosY)
- [Знаходження у незері](https://youtu.be/HKjwgofhKs4)
- [Знаходження за структурами](https://youtu.be/UXVrBaOR8H0)


## Підготовка до роботи

-Зклонюйте репозіторій.

-Виконайте `gradlew genSources <idea|eclipse>`.

## Cкомпілюйте мод

-Оновіть версію у `build.gradle` та `fabric.mod.json`.

-Виконайте `gradlew build`.

## API для інших модів

- Включіть seedcracker-api та jitpack у своєму build.gradle

      repositories {
          mavenCentral()
          maven { url "https://jitpack.io" }
      }
      
      dependencies {
          implementation (include('com.github.19MisterX98.SeedcrackerX:seedcrackerx-api:master-SNAPSHOT')) {transitive = false}
      }

- Додайте клас, який реалізує інтерфейс api

      package misterx.myMod.seedManagemnet.SeedCrackerEP
      
      import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
    
      public class SeedCrackerEP implements SeedCrackerAPI {
          @Override
          public void pushWorldSeed(long seed) {
              //do something
              Foo.bar(seed)
          }
      }

- Розкажіть fabric.mod.json де знаходиться початок виконання

      "entrypoints": {
        "main": [...],
        "client": [...],
        "server": [...],
        "seedcrackerx": [
          "misterx.myMod.seedManagemnet.SeedCrackerEP"
        ]
      },

## Контриб'ютори

[KaptainWutax](https://github.com/KaptainWutax) - Автор

[neil](https://www.youtube.com/watch?v=aUuPSZVPH8E) - Відео туторіал

[Nekzuris](https://github.com/Nekzuris) - README

[19MisterX98](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA) - Автор форку
