import React, { useState } from 'react';
import TeamSelector from './components/TeamSelector';
import MatchupHeader from './components/MatchupHeader';
import AnalysisPanel from './components/AnalysisPanel';
import './App.css';

export default function App() {
  const [homeTeam, setHomeTeam] = useState(null);
  const [awayTeam, setAwayTeam] = useState(null);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeSide, setActiveSide] = useState('home'); // 'home' | 'away'

  const handleAnalyze = async () => {
    if (!homeTeam || !awayTeam) return;

    setLoading(true);
    setResult(null);
    setError(null);

    try {
      const params = new URLSearchParams({
        leagueId: '52', // Süper Lig Sofascore ID
        homeTeamId: homeTeam.sofascoreId,
        homeTeamName: homeTeam.name,
        awayTeamId: awayTeam.sofascoreId,
        awayTeamName: awayTeam.name,
      });

      const response = await fetch(`/api/predictions?${params}`);

      if (!response.ok) {
        const body = await response.json().catch(() => null);
        throw new Error(body?.error ?? `Sunucu hatası: ${response.status}`);
      }

      const data = await response.json();
      setResult(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      {/* Sidebar */}
      <aside className="sidebar">
        {/* Logo */}
        <div className="sidebar-logo">
          <span className="logo-icon">⚽</span>
          <div className="logo-text">
            <span className="logo-title">FootballAI</span>
            <span className="logo-sub">Süper Lig Analizci</span>
          </div>
        </div>

        {/* Side Toggle */}
        <div className="side-toggle">
          <button
            className={`toggle-btn ${activeSide === 'home' ? 'active home' : ''}`}
            onClick={() => setActiveSide('home')}
          >
            🏠 Ev Sahibi
          </button>
          <button
            className={`toggle-btn ${activeSide === 'away' ? 'active away' : ''}`}
            onClick={() => setActiveSide('away')}
          >
            ✈️ Deplasman
          </button>
        </div>

        {/* Team Selector */}
        <div className="selector-wrapper">
          <TeamSelector
            side={activeSide}
            selected={activeSide === 'home' ? homeTeam : awayTeam}
            onSelect={(team) => {
              if (activeSide === 'home') setHomeTeam(team);
              else setAwayTeam(team);
            }}
          />
        </div>

        {/* Sidebar Footer */}
        <div className="sidebar-footer">
          <div className="powered-by">
            <span>Powered by</span>
            <div className="powered-logos">
              <span>🤖 Gemini AI</span>
              <span>•</span>
              <span>📡 Sofascore</span>
            </div>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="main-content">
        {/* Top Nav */}
        <nav className="top-nav">
          <div className="nav-left">
            <span className="nav-crumb">🏠</span>
            <span className="nav-sep">/</span>
            <span className="nav-crumb active">Maç Analizi</span>
          </div>
          <div className="nav-right">
            <div className="live-badge">
              <span className="live-dot" />
              <span>Canlı Veri</span>
            </div>
          </div>
        </nav>

        {/* Matchup Header */}
        <MatchupHeader
          homeTeam={homeTeam}
          awayTeam={awayTeam}
          onAnalyze={handleAnalyze}
          loading={loading}
        />

        {/* Error Message */}
        {error && (
          <div className="error-banner">
            <span>⚠️</span>
            <div>
              <strong>Analiz başarısız</strong>
              <p>{error}</p>
            </div>
            <button onClick={() => setError(null)}>✕</button>
          </div>
        )}

        {/* Analysis Output */}
        <AnalysisPanel
          result={result}
          homeTeam={homeTeam}
          awayTeam={awayTeam}
          loading={loading}
        />
      </main>
    </div>
  );
}
