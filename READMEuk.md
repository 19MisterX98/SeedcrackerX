# SeedCrackerX [![Github All Releases](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]

## Моя активность:

- [Мій Discord](https://discord.gg/JRmHzqQYfp)
- [Youtube](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA)

## Установлення

Завантажте та встановіть [fabric](https://fabricmc.net/use/)
 
Завантажте найновіший [реліз або пре-реліз](https://github.com/19MisterX98/SeedCrackerX/releases) SeedCrackerX
  
перемістіть .jar файли в вашу папку з модами, або у папку %appdata%/.minecraft/mods/ для офіційного лаунчера або в вашу папку MultiMC.
  
#### Опціональне
  
Завантажте останній реліз [Multiconnect](https://github.com/Earthcomputer/multiconnect/releases) або [ViaFabric](https://modrinth.com/mod/viafabric), щоб підключатися до серверів на старіших версіях Майнкрафт.
  
## База даних

Через те що мод використовується багатьма людьми, я створив Google таблицю для зерен серверів.
Якщо ви ввімкнете опцію бази даних в графічних налаштуваннях моду, то він відправлятиме знайдені зерна з серверів з 10+ гравцями.
Вам також буде потрібна ліцензія Майнкрафт.

[Таблиця](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing)

## Використання

### 1.17.X та нижче

Побігайте по мапі поки ви не знайдете спавнер. Після того як мод побачив цей спавнер, процес знаходження зерна почнеться автоматично.
Якщо мод не дасть сід, то ви маєте знайти ще один спавнер.

Також мод підтримую знаходження зерна через:
- [Структури й вежі Енду](https://youtu.be/aUuPSZVPH8E?t=462)
- [Химерні гриби](https://www.youtu.be/HKjwgofhKs4)

### 1.18.X та вище

Знаходження за допомогою спавнерів та химерних грибів більше не працює.

Зайдіть в налаштування за допомогою "/seed gui" й перевірте, що смарагди, портали в Енд, біоми, пустельні колодязі та химерні гриби вимкненні, тому що вони можуть дати неправильні дані.

Для знаходження зерна, з вас потребується знайти 5 структур з зазначених нижче:\
Храми в пустелі, храми у джунглях, хатинки відьми, уламки кораблів, іглу, аванпости розбійників

Будь-яка комбінація підійде. На приклад: 3 уламки кораблів, 1 піраміда та 1 іглу.
Ви можете перевірити свій прогрес за допомогою "/seed data bits" (подивіться на кількість біт у "підйомні структури")
Структура була знайдена модом, якщо вона обведена.
Після того як ви отримали достатньо автоматично почнеться пошук. Це займе приблизно 1-5 хв.
Мод може запросити знайти ще структур після цього.
Це більш імовірно, якщо у вас менше бітів та однакові структури.
Після того як мод знайшов зерно структур, він перебере зерно мапи.
 
  ### Підтриманні структури
    - Океанічний монумент
    - Місто Енду
    - Заховані скарби
    - Храми в пустелі
    - Храми у джунглях
    - Хатинки відьми
    - Уламки кораблів
    - Іглу
    - Аванпости розбійників
  
  ### Підтриманні декоратори
    - Спавнери
    - Портали у Енд
    - Пустельні колодязі
    - Смарагдові руди
    - Химерні гриби

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
- [Знаходження за структурами](https://www.youtu.be/UXVrBaOR8H0)


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
