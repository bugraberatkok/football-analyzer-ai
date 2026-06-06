# AI Football Analyzer

Yapay zeka destekli, gerçek zamanlı veri entegrasyonuna dayalı futbol analizi ve stratejik maç öngörü sistemi. Bu proje, harici spor veri sağlayıcılarından (Sofascore) akan çok kanallı ham verileri toplar, mimari tasarım kalıplarına uygun olarak işler ve büyük dil modelleri (LLM) aracılığıyla profesyonel taktiksel raporlara dönüştürür.

## 🚀 Özellikler

* **Dinamik Takım Arama ve Otomatik ID Eşleme:** Kullanıcının arayüzden seçtiği takım isimlerini arka planda canlı arama motoruna sokarak Sofascore veritabanındaki benzersiz ID karşılıklarını (Regex tabanlı) otomatik olarak çözer.
* **Çok Kanallı Veri Toplama (Multi-Channel Aggregation):** Seçilen iki takımın son maç listelerini (Form durumu) ve sezon geneli derin metriklerini (xG gol beklentisi, şut hızı, korner, kart yoğunluğu) ardışık asenkron ağ istekleriyle indirir.
* **Yapay Zeka Yorumlama Motoru:** Ham JSON çıktılarını doğrudan işleyerek tarihsel rekabet analizi, taktiksel maç senaryoları ve momentum grafiklerini içeren derinlemesine Türkçe spor raporları üretir.
* **Modern ve Duyarlı (Responsive) UI:** Bootstrap 5 tabanlı, temiz, minimalist ve kullanıcı dostu arayüz tasarımı.

## 🏗️ Mimari Tasarım ve Prensipler

Proje geliştirilirken nesne yönelimli programlama (OOP) ve temiz kod (Clean Code) prensiplerine sadık kalınmıştır:

* **Facade Pattern (`FootballDataFacade`):** Dış dünyaya açılan API bağlantılarını, arama motorunu ve 4 farklı koldan yapılan ağ isteklerini tek bir cephe arkasında toplayarak karmaşıklığı gizler.
* **Strategy Pattern (`MatchAnalysisStrategy`):** Verilerin işlenme ve prompt haline getirilme süreçlerini esnek bir yapıya oturtarak, ileride eklenebilecek farklı analiz modellerinin sisteme kolayca entegre edilmesini sağlar.
* **Shibumi Felsefesi:** Sistem mimarisi gereksiz parse kütüphaneleriyle şişirilmemiş, ham verinin gücü doğrudan LLM zekasıyla birleştirilerek maksimum performans ve sadelik hedeflenmiştir.

## 🛠️ Teknolojiler

* **Backend:** Java 17, Spring Boot 3.x, RestTemplate
* **Frontend:** HTML5, CSS3, Vanilla JavaScript, Bootstrap 5
* **Dış Entegrasyonlar:** Sofascore API (via RapidAPI), Google Gemini API (`gemini-3-flash-preview`)
* **Bağımlılık Yönetimi:** Maven / Lombok

## 🔧 Kurulum ve Çalıştırma

### Gereksinimler
* JDK 17 veya üzeri
* Maven 3.x+
* Aktif internet bağlantısı (API istekleri için)

### Adımlar

1. Projeyi bilgisayarınıza klonlayın veya indirin.
2. Proje kök dizininde terminali açarak uygulamayı derleyin ve çalıştırın:
    mvn clean spring-boot:run
3. Tarayıcınızdan http://localhost:8080 adresine giderek sistemi kullanmaya başlayabilirsiniz.
