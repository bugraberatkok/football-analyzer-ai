# ⚽ Football Analyzer AI

An AI-powered pre-match football analysis system that fetches real-time statistics from the Sofascore API and generates professional tactical reports using Google Gemini AI.

---

## ✨ Features

- **Real-time Data Aggregation** — Fetches last matches and season-level statistics for both home and away teams simultaneously via Sofascore RapidAPI (4 data channels)
- **AI-Generated Analysis** — Sends structured football data to Google Gemini and receives a professional Turkish-language match analysis covering form, xG comparison, and tactical outlook
- **Layered Architecture** — Built with Facade and Strategy design patterns for clean separation of concerns and extensibility
- **Responsive Web UI** — React (Vite) frontend with team selector, VS matchup view, and live analysis panel

---

## 🏗️ Architecture & Design Patterns

| Pattern | Where | Purpose |
|---------|-------|---------|
| **Facade** | `FootballDataFacade` | Abstracts 4 Sofascore API calls behind a single interface |
| **Strategy** | `MatchAnalysisStrategy` | Makes the data-processing algorithm interchangeable |
| **MVC** | Controller / Service / DTO | Separates concerns across layers |
| **Dependency Injection** | `MatchPredictionService` | Constructor injection via Spring IoC container |

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 4.0 |
| Frontend | React 18, Vite |
| HTTP Client | Spring `RestTemplate` |
| Build Tool | Maven |
| Boilerplate | Lombok (`@Data`) |
| Data Source | Sofascore via RapidAPI |
| AI Engine | Google Gemini API |

---

## 📁 Project Structure

```
football-analyzer/
├── backend/                             # Spring Boot application
│   ├── src/main/java/com/huseyin/footballanalyzer/
│   │   ├── FootballAnalyzerApplication.java     # Entry point
│   │   ├── controller/
│   │   │   └── MatchPredictionController.java   # GET /api/predictions
│   │   ├── dto/
│   │   │   ├── MatchData.java                   # Raw API data carrier
│   │   │   └── AnalysisResult.java              # Analysis output carrier
│   │   ├── facade/
│   │   │   ├── FootballDataFacade.java          # Interface
│   │   │   └── RealFootballDataFacade.java      # Sofascore API implementation
│   │   ├── service/
│   │   │   ├── MatchPredictionService.java      # Orchestrator
│   │   │   └── AiService.java                  # Gemini AI integration
│   │   └── strategy/
│   │       ├── MatchAnalysisStrategy.java       # Interface
│   │       └── BasicFormAnalysisStrategy.java   # Prompt builder implementation
│   ├── src/main/resources/
│   │   ├── application.properties               # Local config (not committed)
│   │   └── application.properties.example      # Template for setup
│   └── pom.xml
├── frontend/                            # React/Vite application
│   └── src/
│       ├── App.jsx
│       └── components/
│           ├── MatchupHeader.jsx
│           ├── TeamSelector.jsx
│           └── AnalysisPanel.jsx
└── README.md
```

---

## ⚙️ Setup & Installation

### Prerequisites

- JDK 21+
- Maven 3.8+
- Node.js 18+
- A [RapidAPI](https://rapidapi.com/sofascore/api/sofascore) account with Sofascore API access
- A [Google AI Studio](https://aistudio.google.com/apikey) Gemini API key

### 1 — Configure API Keys

Copy the example properties file and fill in your own keys:

```bash
cp backend/src/main/resources/application.properties.example backend/src/main/resources/application.properties
```

Open `backend/src/main/resources/application.properties` and replace the placeholder values:

```properties
rapidapi.key=YOUR_RAPIDAPI_KEY_HERE
gemini.key=YOUR_GEMINI_API_KEY_HERE
```

> `application.properties` is listed in `.gitignore` and will never be committed.

### 2 — Run the Backend

```bash
cd backend
mvn spring-boot:run
```

The backend starts at **http://localhost:8080**

### 3 — Run the Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts at **http://localhost:5173**

---

## 🔌 API Reference

### `GET /api/predictions`

Returns an AI-generated match analysis for the given teams.

**Query Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `leagueId` | String | Sofascore league ID |
| `homeTeamId` | String | Sofascore ID of the home team |
| `homeTeamName` | String | Display name of the home team |
| `awayTeamId` | String | Sofascore ID of the away team |
| `awayTeamName` | String | Display name of the away team |

**Example Request:**

```
GET http://localhost:8080/api/predictions?leagueId=325&homeTeamId=2697&homeTeamName=Galatasaray&awayTeamId=3024&awayTeamName=Fenerbahçe
```

**Example Response:**

```json
{
  "dominantTeam": "AI Kararı",
  "totalExpectedGoals": 0.0,
  "matchPace": "Dinamik Analiz",
  "rawDataSummary": "## FORM VE GÜNCEL MOMENTUM GRAFİĞİ\n..."
}
```

---

## 🔄 How It Works

```
User selects teams in the UI
        ↓
GET /api/predictions (Controller)
        ↓
MatchPredictionService orchestrates:
  1. RealFootballDataFacade  →  4 Sofascore API calls  →  MatchData
  2. BasicFormAnalysisStrategy  →  Merges JSONs into prompt  →  AnalysisResult
  3. AiService  →  Sends prompt to Gemini API  →  Turkish analysis text
  4. Result returned to frontend as JSON
        ↓
Frontend renders the analysis panel
```

---

## 📄 License

This project is for educational purposes.


## Developers

Hüseyin Hünkar
Buğra Berat Kök
