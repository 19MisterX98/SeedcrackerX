# SeedCrackerX [![Tüm Github Sürümleri](https://img.shields.io/github/downloads/19MisterX98/SeedCrackerX/total.svg)]

## Readme dili

[中文](READMEzh.md)
[Русский](READMEru.md)
[Türkçe](./READMEtr.md)

## Bana ulaşın:

- [Discord Sunucum](https://discord.gg/JRmHzqQYfp)
- [Youtube](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA)

## Kurulum

[Fabric mod yükleyicisi](https://fabricmc.net/use/)'ni indirip kurun
 
SeedCrackerX'in son [sürümünü yada ön-sürümünü](https://github.com/19MisterX98/SeedCrackerX/releases) indirin 
  
.jar dosyalarını mod klasörünüze koyun, Vanilla Başlatıcı'sı için %appdata%/.minecraft/mods/ klasörü yada kendi MultiMC profilinizin konumu.
  
#### İsteğe bağlı
  
Daha düşük Minecraft sürümündeki sunuculara katılmak için [Multiconnect](https://github.com/Earthcomputer/multiconnect/releases) yada [ViaFabric](https://modrinth.com/mod/viafabric)'in son sürümünü indirin
  
## Veri tabanı

Bu mod birçok insan tarafından kullanılması sebebiyle sunucu seed'leri için Google e-tablo'su açmaya karar verdim.
Eğer konfigürasyon arayüzünden veri tabanı ayarını açarsanız mod, 10+ oyunculu sunucu seed'lerini Google e-tablo'suna yollayacak.
Bunun için lisanslı bir minecraft'a sahip olmanız lazım

[e-tablo](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing)

## Kullanım

### 1.17.X ve alt sürümleri

Mod zindan bulana kadar dünyada koşun. Mod 1 tane zindan bulduktan sonra seed bulma işlemini otomatik olarak başlatacak.
Eğer mod, dünya seed'i bulamazsa başka bir zindan aramanız gerekebilir.

Bu mod ayrıca şunlar ile seed bulabilir:
- [Yapılar ve End Kuleleri](https://youtu.be/aUuPSZVPH8E?t=462)
- [Eğri Mantar](https://youtu.be/HKjwgofhKs4)

### 1.18.X ve üst sürümleri

Zindan ve mantarlar ile seed bulma işlemi artık çalışmıyor.

Güncel olmadıkları ve yanlış veri verebilecekleri için "/seed gui" komudu aracılığıyla konfigürasyon menüsüne gidin ve Zümrütlerin, Geçitlerin, Biyomların, Çöl kuyularının ve Eğri Mantarların kapalı olduğuna emin olun

Bulma işlemi için artık aşağıdaki 5 yapıdan bulmanız gerekir:
Çöl Piramitleri, Orman Tapınakları, Cadı Kulübeleri, Gemi Enkazları, İglolar, Pillager Mıntıkaları

Bunların herhangi bir kombinasyonu yeterli. Örnek olarak: 3 Gemi Enkazı, 1 Piramit ve 1 İglo
Şu anki ilerlemenizi "/seed data bits" komudu aracılığıyla görebilirsiniz (Kaldırılabilir yapılar'ın bit sayılarına bakın)
Bir yapı, etrafında dış çizgiler oluştuğunda bulunmuş olur.
Yeterli veri topladıktan sonra, bulma işlemi otomatik olarak başlar. Bu işlem 1 ile 5 dakika arası sürer.

Bu işlemden sonra mod fazladan yapı bulmanızı isteyebilir.
Az bit'ler ile yada aynı tür yapılar ile bunun gerçekleşmesi daha muhtemeldir.
Yapı seed'lerinizi azalttıktan sonra mod, brute force (kaba kuvvet) işlemi aracılığıyla dünyanızın seed'ini zindan konumları yada hash'lanmış seed ile bulacak. 
 
  ### Desteği Olan Yapılar
    - Okyanus Mabetleri
	- End Şehirleri
	- Gömülü Hazineler
	- Çöl Piramitleri
	- Orman Tapınakları
	- Bataklık Kulübeleri
	- Gemi Enkazları
	- İglolar
	- Pillater Mıntıkaları
  
  ### Desteği Olan Dekoratörler
    - Zindanlar
    - End Geçitleri
    - Çöl Kuyuları
    - Zümrüt Cevherleri
    - Eğri Mantarlar

## Komutlar

  1.19.3 sürümü için şu komutu kullanın 
 - `/seedcracker`

  ### GUI (Kullanıcı Arayüzü) Komutu
  - `/seed gui`
  
  Minecraft Sürümünüz, tüm arayıcılar, veri tabanları ve render modunuzu değiştirebileceğiniz bir konfigürasyon gui'ı açar
  Bunların çoğunun komut alternatifleri var, fakat artık kullanılmamalılar.
  
  ### Bulucu'yu Yenileme Komutu
  - `/seed finder reload`

  Yüklü Chunk'ları daha önceden bulunamayan yapıları bulmak için tekrar tarar.

  ### Veri Komutu
  - `/seed data clear`
  
  Tekrar giriş istemeden toplanan tüm verileri temizler. Çok dünyalı sunucular için kullanışlı bir komut.
  
  - `/seed data bits`
  
  Kaç bit verinin toplandığını gösterir.
  Normal bit'ler End Kuleleri ve Yapılar ile bulma işleminde kullanılır. Bulma işlemi 32 bit'te başlar.
  Kaldırılabilir bit'ler kaldırılabilir yapılar ile bulma işleminde kullanılır. Bulma işlemi 40 bit'te başlar.
  
  - `/seed data restore`
  
  Bir dünyadan ayrıldığınızda mod; otomatik olarak, toplanan yapı bilgilerini bir dosyaya kaydeder (.minecraft/config)
  Tekrar katıldığınızda bu komut ile verileri kurtarabilirsiniz.
  
  ### Debug (hata ayıklama) Komutu

  - `/seed cracker debug`

  Detaylı bilgi gösterir
  
  ### Veri Tabanı Komutu
  - `/seed database`
  
  Mod tarafından yönetilen bir [google e-tablo'su](https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing) açar
  
## Video'lu Anlatımlar

Neil'ın:
- [1.15](https://youtu.be/1ChmLi9og8Q)
- [1.16](https://youtu.be/aUuPSZVPH8E)


Dyiing'in
- [1.18](https://www.youtube.com/watch?v=tKeEyx7jIE4)


Proje Sahibinin:
- [Zindan ve End Kuleleri ile bulma](https://youtu.be/8ytfZ2MXosY)
- [Nether ile bulma](https://youtu.be/HKjwgofhKs4)
- [Yapı ile bulma](https://youtu.be/UXVrBaOR8H0)

## -- DEVAMI MOD GELİŞTİRİCİLERİ İÇİN --

## Workspace'i hazırlayın

-repository'i klonlayın.

-`gradlew genSources <idea|eclipse>` komutunu terminal'de çalıştırın.

## Modu inşa edin

-Versionu `build.gradle` ve `fabric.mod.json` dosyanarında güncelleyin.

-`gradlew build` komutunu terminal'de çalıştırın.

## Diğer modlar için API

- seedcracker-api ve jitpack'i build.gradle dosyasında dahil edin

      repositories {
          mavenCentral()
          maven { url "https://jitpack.io" }
      }
      
      dependencies {
          implementation (include('com.github.19MisterX98.SeedcrackerX:seedcrackerx-api:master-SNAPSHOT')) {transitive = false}
      }

- API interface'ini implement'leyen bir class oluşturun

      package misterx.myMod.seedManagemnet.SeedCrackerEP
      
      import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
    
      public class SeedCrackerEP implements SeedCrackerAPI {
          @Override
          public void pushWorldSeed(long seed) {
              //do something
              Foo.bar(seed)
          }
      }

- fabric.mod.json 'a entrypoint'inizin nerede olduğunu yazın

      "entrypoints": {
        "main": [...],
        "client": [...],
        "server": [...],
        "seedcrackerx": [
          "misterx.myMod.seedManagemnet.SeedCrackerEP"
        ]
      },

## Katkıda Bulunanlar

[KaptainWutax](https://github.com/KaptainWutax) - Yazar

[neil](https://www.youtube.com/watch?v=aUuPSZVPH8E) - Video'lu Anlatım

[Nekzuris](https://github.com/Nekzuris) - README dosyası

[19MisterX98](https://www.youtube.com/channel/UCby9ZxEjJCqmccQGF3GSYlA) - Bu fork'un yazarı

[phasenull](https://github.com/phasenull) - Türkçe README ve dil desteği
