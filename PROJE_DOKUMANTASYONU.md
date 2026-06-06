# ⚽ Football Analyzer AI — Proje Dokümantasyonu

> **Hazırlayan:** Antigravity AI  
> **Tarih:** Haziran 2026  
> **Amaç:** Ekip içi kod okuma ve anlama rehberi

---

## 📌 Projeye Genel Bakış

Bu proje, iki futbol takımı arasındaki maç öncesinde **yapay zeka destekli analiz ve tahmin** yapan bir web uygulamasıdır.

**Nasıl çalışır? (3 adımda özet):**
1. Kullanıcı frontend'den ev sahibi ve deplasman takımını seçer.
2. Backend, **Sofascore API**'si üzerinden her iki takımın son maç ve sezon istatistiklerini çeker.
3. Bu ham veriler **Google Gemini AI**'ye gönderilir ve AI, Türkçe profesyonel bir analiz metni üretir.

---

## 🗂️ Proje Klasör Yapısı

```
football-analyst-AI-main/
│
├── src/
│   └── main/java/com/huseyin/footballanalyzer/
│       ├── FootballAnalyzerApplication.java   ← Uygulamanın başladığı yer
│       │
│       ├── controller/
│       │   └── MatchPredictionController.java ← HTTP isteklerini karşılar
│       │
│       ├── dto/
│       │   ├── MatchData.java                 ← Veri taşıma nesnesi (ham veri)
│       │   └── AnalysisResult.java            ← Veri taşıma nesnesi (analiz sonucu)
│       │
│       ├── facade/
│       │   ├── FootballDataFacade.java         ← Arayüz (interface)
│       │   └── RealFootballDataFacade.java     ← Sofascore API bağlantısı
│       │
│       ├── service/
│       │   ├── MatchPredictionService.java     ← Ana iş mantığı koordinatörü
│       │   └── AiService.java                 ← Gemini AI bağlantısı
│       │
│       └── strategy/
│           ├── MatchAnalysisStrategy.java      ← Analiz stratejisi arayüzü
│           └── BasicFormAnalysisStrategy.java  ← Veriyi AI'ya hazırlayan strateji
│
├── src/test/
│   └── FootballAnalyzerApplicationTests.java   ← Otomatik test
│
├── frontend/                                   ← React/Vite arayüz kodu
└── pom.xml                                     ← Maven bağımlılıkları
```

---

## 📄 Java Dosyaları — Detaylı Açıklamalar

---

### 1. `FootballAnalyzerApplication.java`
📂 `src/main/java/com/huseyin/footballanalyzer/`

```java
@SpringBootApplication
public class FootballAnalyzerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FootballAnalyzerApplication.class, args);
    }
}
```

**Ne iş yapar?**
- Bu dosya uygulamanın **ana giriş noktasıdır** (entry point).
- `@SpringBootApplication` anotasyonu, Spring Boot'un tüm konfigürasyonları otomatik yüklemesini sağlar.
- `SpringApplication.run(...)` çağrısı, gömülü Tomcat web sunucusunu başlatır.
- **Kısaca:** "Uygulamayı çalıştır" komutunu veren tek satır buradadır. Bu dosyaya dokunmanıza gerek yoktur.

---

### 2. `MatchPredictionController.java`
📂 `src/main/java/com/huseyin/footballanalyzer/controller/`

```java
@RestController
@RequestMapping("/api/predictions")
public class MatchPredictionController {
    ...
    @GetMapping
    public AnalysisResult getPrediction(
        @RequestParam String leagueId,
        @RequestParam String homeTeamId,
        @RequestParam String homeTeamName,
        @RequestParam String awayTeamId,
        @RequestParam String awayTeamName) { ... }
}
```

**Ne iş yapar?**
- Frontend'den gelen **HTTP GET isteklerini** dinler.
- Dinlediği adres: `GET http://localhost:8080/api/predictions`
- Frontend bu adrese şu parametrelerle istek atar:
  - `leagueId` → Lig ID'si
  - `homeTeamId` / `homeTeamName` → Ev sahibi takım bilgisi
  - `awayTeamId` / `awayTeamName` → Deplasman takım bilgisi
- Parametreleri alır ve işi `MatchPredictionService`'e devreder.
- Gelen `AnalysisResult` nesnesini otomatik olarak **JSON formatına** çevirip frontend'e gönderir.

> 💡 **MVC'deki rolü:** Controller katmanı. Kullanıcı ile iş mantığı arasındaki köprüdür. Hiçbir iş mantığı içermez, sadece yönlendirme yapar.

---

### 3. `MatchData.java`
📂 `src/main/java/com/huseyin/footballanalyzer/dto/`

```java
@Data
public class MatchData {
    private String homeTeam;
    private String awayTeam;
    private String homeLastMatchesJson;
    private String awayLastMatchesJson;
    private String homeStatsJson;
    private String awayStatsJson;
}
```

**Ne iş yapar?**
- Bu bir **DTO (Data Transfer Object)** sınıfıdır. İçinde iş mantığı yoktur; sadece veri taşır.
- Sofascore API'sinden çekilen **ham JSON verilerini** bir arada tutar:
  - `homeLastMatchesJson` → Ev sahibinin son maçlarının JSON'ı
  - `awayLastMatchesJson` → Deplasman takımının son maçlarının JSON'ı
  - `homeStatsJson` → Ev sahibinin sezon istatistikleri JSON'ı
  - `awayStatsJson` → Deplasman takımının sezon istatistikleri JSON'ı
- `@Data` anotasyonu (Lombok kütüphanesi), `getter`, `setter`, `equals`, `hashCode` ve `toString` metodlarını **otomatik** üretir — bunları elle yazmak gerekmez.

---

### 4. `AnalysisResult.java`
📂 `src/main/java/com/huseyin/footballanalyzer/dto/`

```java
@Data
public class AnalysisResult {
    private String dominantTeam;
    private double totalExpectedGoals;
    private String matchPace;
    private String rawDataSummary;
}
```

**Ne iş yapar?**
- Bu da bir **DTO** sınıfıdır.
- Analiz sonucunu frontend'e taşıyan "zarftır".
- Alanları:
  - `dominantTeam` → Baskın takım (şu an "AI Kararı" olarak set ediliyor)
  - `totalExpectedGoals` → Toplam beklenen gol sayısı (xG)
  - `matchPace` → Maç temposu (şu an "Dinamik Analiz" olarak set ediliyor)
  - `rawDataSummary` → En kritik alan: başta ham veri özeti olarak kullanılır, sonunda **AI'nın ürettiği Türkçe analiz metni** bu alana yazılır.

---

### 5. `FootballDataFacade.java`
📂 `src/main/java/com/huseyin/footballanalyzer/facade/`

```java
public interface FootballDataFacade {
    MatchData fetchAndPrepareMatchData(String leagueId, String homeTeamId,
        String homeTeamName, String awayTeamId, String awayTeamName);
}
```

**Ne iş yapar?**
- Bu bir **Java interface**'idir (soyut sözleşme).
- "Futbol verisi getir" görevini soyutlar. Gerçek uygulamanın nasıl yapıldığını gizler.
- Bunu kullanan kodlar (`MatchPredictionService`), Sofascore'un nasıl çağrıldığını bilmez. Sadece "ver bana maç verisini" der.

> 💡 **Tasarım deseni:** Facade Pattern. Karmaşık dış API çağrılarını basit bir arayüz arkasına saklar. İleride farklı bir API'ye geçmek istenirse (örneğin API-Football), sadece yeni bir implementasyon yazılır; geri kalan kod değişmez.

---

### 6. `RealFootballDataFacade.java`
📂 `src/main/java/com/huseyin/footballanalyzer/facade/`

**Ne iş yapar?**
- `FootballDataFacade` arayüzünün **gerçek implementasyonudur**.
- **Sofascore RapidAPI**'sine bağlanarak 4 farklı endpoint'e paralel çağrı yapar:

| # | Çağrı | Açıklama |
|---|-------|----------|
| 1 | `teams/get-last-matches?teamId={homeTeamId}` | Ev sahibinin son maçları |
| 2 | `teams/get-last-matches?teamId={awayTeamId}` | Deplasmanın son maçları |
| 3 | `teams/get-statistics?teamId={homeTeamId}` | Ev sahibi sezon istatistikleri |
| 4 | `teams/get-statistics?teamId={awayTeamId}` | Deplasman sezon istatistikleri |

- Her çağrı bir `try-catch` bloğuyla korunur; hata olursa uygulama çökmez, hata mesajlı bir JSON döner.
- `RestTemplate` → Spring'in HTTP istemci aracı. Harici API'lere istek atmak için kullanılır.
- API anahtarı (`apiKey`): RapidAPI'den alınan kimlik doğrulama anahtarı; her istekte `X-RapidAPI-Key` header'ı olarak gönderilir.

> ⚠️ **Dikkat:** API anahtarı şu an kod içinde hardcoded (gömülü) durumda. Production ortamında bu güvenlik açığıdır; environment variable olarak taşınmalıdır.

---

### 7. `MatchPredictionService.java`
📂 `src/main/java/com/huseyin/footballanalyzer/service/`

**Ne iş yapar?**
- Uygulamanın **ana orkestratörüdür**. Tüm bileşenleri bir araya getirir.
- 4 adımlı iş akışını yönetir:

```
1. footballDataFacade.fetchAndPrepareMatchData(...)
         ↓  (Sofascore'dan 4 kanal veri geldi)
2. matchAnalysisStrategy.analyze(matchData)
         ↓  (Ham JSON'lar tek bir metin bloğunda birleştirildi)
3. aiService.generateAiCommentary(analysisResult.getRawDataSummary())
         ↓  (Gemini AI analiz metni üretiyor...)
4. analysisResult.setRawDataSummary(aiCommentary)
         ↓  (AI çıktısı sonuca yazılır ve Controller'a döner)
```

- **Loose Coupling (Gevşek Bağlılık):** Constructor üzerinden dependency injection kullanır. Sınıf, bağımlılıklarını kendisi yaratmaz; Spring IoC container'dan hazır alır. Bu sayede test yazarken mock nesnelerle kolayca değiştirilebilir.

---

### 8. `AiService.java`
📂 `src/main/java/com/huseyin/footballanalyzer/service/`

**Ne iş yapar?**
- **Google Gemini AI**'ya bağlanarak futbol analizi metni üretir.
- `generateAiCommentary(String megaFootballData)` metodu:
  1. Ham veri metnini alır.
  2. Türkçe bir **sistem prompt'u** hazırlar (rol: "dünyanın en iyi futbol analisti").
  3. Prompt'u Gemini API'ye POST isteğiyle gönderir.
  4. API'den dönen JSON yanıtından analiz metnini parse eder.
  5. Türkçe analiz metnini string olarak geri döner.

- **Prompt içeriği 3 başlık içerir:**
  1. Form ve Güncel Momentum Grafiği
  2. Derin İstatistik ve xG Kıyaslaması
  3. Taktiksel Maç Öngörüsü

> ⚠️ **Dikkat:** Gemini API anahtarı da hardcoded. Ayrıca model adı (`gemini-3-flash-preview`) güncel Gemini sürümleriyle kontrol edilmelidir.

---

### 9. `MatchAnalysisStrategy.java`
📂 `src/main/java/com/huseyin/footballanalyzer/strategy/`

```java
public interface MatchAnalysisStrategy {
    AnalysisResult analyze(MatchData matchData);
}
```

**Ne iş yapar?**
- Analiz stratejisi için bir **Java interface**'idir.
- "MatchData al, AnalysisResult döndür" sözleşmesini tanımlar.

> 💡 **Tasarım deseni:** Strategy Pattern. İleride farklı analiz algoritmaları eklenebilir (örneğin `MLBasedAnalysisStrategy`, `HeadToHeadAnalysisStrategy`) ve `MatchPredictionService` bunlardan hangisini kullanacağını bilmek zorunda kalmaz.

---

### 10. `BasicFormAnalysisStrategy.java`
📂 `src/main/java/com/huseyin/footballanalyzer/strategy/`

**Ne iş yapar?**
- `MatchAnalysisStrategy`'nin **gerçek implementasyonudur**.
- 4 API kanalından gelen ham JSON verilerini alır ve **tek bir metin bloğunda birleştirir**.
- Bu birleşik metin, AI'ya gönderilecek "mega prompt body" olur.
- `dominantTeam` ve `matchPace` gibi alanları şimdilik sabit değerlerle doldurur ("AI Kararı", "Dinamik Analiz") — bunlar henüz algoritmik olarak hesaplanmıyor.

---

### 11. `FootballAnalyzerApplicationTests.java`
📂 `src/test/java/com/huseyin/footballanalyzer/`

```java
@SpringBootTest
class FootballAnalyzerApplicationTests {
    @Test
    void contextLoads() { }
}
```

**Ne iş yapar?**
- Spring Boot'un standart **smoke test**'idir.
- Uygulama context'inin hatasız yüklenip yüklenmediğini kontrol eder.
- Test içi logic yoktur; sadece uygulama ayağa kalkarsa "geçti" der.
- `mvn test` komutuyla çalıştırılır.

---

## 🔗 Bileşenler Arası İlişki Diyagramı

```
[Frontend (React)]
       │
       │ GET /api/predictions?leagueId=...&homeTeamId=...
       ▼
[MatchPredictionController]
       │
       ▼
[MatchPredictionService]  ←── Spring IoC container bağımlılıkları enjekte eder
       │
       ├──► [FootballDataFacade (interface)]
       │           │
       │           └──► [RealFootballDataFacade]
       │                       │
       │                       ├──► Sofascore API (son maçlar - ev)
       │                       ├──► Sofascore API (son maçlar - dep)
       │                       ├──► Sofascore API (istatistik - ev)
       │                       └──► Sofascore API (istatistik - dep)
       │                                   ↓
       │                           [MatchData nesnesi döner]
       │
       ├──► [MatchAnalysisStrategy (interface)]
       │           │
       │           └──► [BasicFormAnalysisStrategy]
       │                       │
       │                       └── Ham JSON'ları birleştirir → [AnalysisResult]
       │
       └──► [AiService]
                   │
                   └──► Google Gemini API
                               │
                               └── Türkçe analiz metni döner
                                         ↓
                           [AnalysisResult.rawDataSummary'ye yazılır]
                                         ↓
                           [Controller → Frontend'e JSON olarak döner]
```

---

## 🛠️ Kullanılan Teknolojiler ve Kütüphaneler

| Teknoloji | Versiyon | Amaç |
|-----------|----------|------|
| Java | 21 | Backend dili |
| Spring Boot | 4.0.6 | Web framework, IoC container |
| Spring Web MVC | - | REST API, HTTP istemcisi |
| Lombok | - | Boilerplate kod azaltma (`@Data` vb.) |
| Sofascore RapidAPI | - | Futbol maç verileri |
| Google Gemini AI | gemini-3-flash | Doğal dil analizi |
| Maven | - | Bağımlılık yönetimi ve build |

---

## 🧩 Kullanılan Tasarım Desenleri

| Desen | Nerede? | Açıklama |
|-------|---------|----------|
| **Facade Pattern** | `FootballDataFacade` | Karmaşık Sofascore API çağrılarını tek metot arkasına saklar |
| **Strategy Pattern** | `MatchAnalysisStrategy` | Analiz algoritmasını değiştirilebilir yapar |
| **Dependency Injection** | `MatchPredictionService` | Spring IoC ile gevşek bağlılık sağlar |
| **DTO Pattern** | `MatchData`, `AnalysisResult` | Katmanlar arası veri taşıma nesneleri |
| **MVC Pattern** | Controller/Service | Sorumlulukları katmanlara ayırır |

---

## ⚡ Veri Akışı — Bir İstek Nasıl İşlenir?

```
1. Kullanıcı frontend'de "Analiz Et" butonuna tıklar
2. React, GET /api/predictions?... isteği atar
3. MatchPredictionController isteği yakalar
4. MatchPredictionService.getMatchPrediction() çağrılır
5. RealFootballDataFacade, Sofascore'a 4 istek atar
6. 4 JSON yanıtı MatchData nesnesine doldurulur
7. BasicFormAnalysisStrategy bunları tek metne birleştirir
8. AiService bu metni Gemini'ye gönderir
9. Gemini Türkçe analiz metni üretir
10. AnalysisResult.rawDataSummary bu metinle güncellenir
11. JSON olarak frontend'e döner
12. React arayüzde analiz metni gösterilir
```

---

## 🚀 Projeyi Çalıştırma

### Backend (Spring Boot)
```bash
# Proje kök dizininde
mvn spring-boot:run
# Uygulama http://localhost:8080 adresinde çalışır
```

### Frontend (React/Vite)
```bash
cd frontend
npm run dev
# Uygulama http://localhost:5173 adresinde çalışır
```

---

## ❗ Bilinen Eksikler / Geliştirme Önerileri

| # | Sorun | Öneri |
|---|-------|-------|
| 1 | API anahtarları kod içinde gömülü | `.env` dosyası veya `application.properties` kullan |
| 2 | `dominantTeam` ve `matchPace` sabit değer | Gerçek istatistiksel hesaplama eklenebilir |
| 3 | Test coverage çok düşük | Service ve Facade için unit testler yazılabilir |
| 4 | Sofascore API'si ücretli limitlere sahip | Rate limiting ve caching eklenebilir |
| 5 | Hata mesajları kullanıcıya ham gösteriliyor | Daha kullanıcı dostu hata yönetimi |
