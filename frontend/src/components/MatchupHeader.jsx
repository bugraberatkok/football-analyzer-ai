import React from 'react';
import './MatchupHeader.css';

export default function MatchupHeader({ homeTeam, awayTeam, onAnalyze, loading }) {
  const canAnalyze = homeTeam && awayTeam && homeTeam.id !== awayTeam.id;

  return (
    <div className="matchup-header">
      {/* League Badge */}
      <div className="league-badge">
        <span className="league-flag">🇹🇷</span>
        <span className="league-name">Trendyol Süper Lig</span>
        <span className="league-dot">•</span>
        <span className="league-season">2025 / 2026</span>
      </div>

      {/* VS Section */}
      <div className="vs-section">
        {/* Home Team */}
        <div className={`team-display home ${homeTeam ? 'selected' : 'empty'}`}
          style={{ '--accent': homeTeam?.primaryColor || '#444' }}>
          <div className="team-emblem">
            {homeTeam ? (
              <>
                {homeTeam.logo.startsWith('http') ? (
                  <img src={homeTeam.logo} alt={homeTeam.name} className="team-logo-crest" />
                ) : (
                  <span className="team-emoji">{homeTeam.logo}</span>
                )}
                <div className="team-glow" style={{ background: homeTeam.primaryColor }} />
              </>
            ) : (
              <span className="team-placeholder">?</span>
            )}
          </div>
          <div className="team-detail">
            <span className="team-full-name">{homeTeam?.name || 'Ev Sahibi Seçin'}</span>
            <span className="team-role">Ev Sahibi</span>
          </div>
        </div>

        {/* VS Badge */}
        <div className="vs-badge">
          <div className="vs-line left" />
          <div className="vs-circle">
            <span>VS</span>
          </div>
          <div className="vs-line right" />
        </div>

        {/* Away Team */}
        <div className={`team-display away ${awayTeam ? 'selected' : 'empty'}`}
          style={{ '--accent': awayTeam?.primaryColor || '#444' }}>
          <div className="team-detail">
            <span className="team-full-name">{awayTeam?.name || 'Deplasman Seçin'}</span>
            <span className="team-role">Deplasman</span>
          </div>
          <div className="team-emblem">
            {awayTeam ? (
              <>
                {awayTeam.logo.startsWith('http') ? (
                  <img src={awayTeam.logo} alt={awayTeam.name} className="team-logo-crest" />
                ) : (
                  <span className="team-emoji">{awayTeam.logo}</span>
                )}
                <div className="team-glow" style={{ background: awayTeam.primaryColor }} />
              </>
            ) : (
              <span className="team-placeholder">?</span>
            )}
          </div>
        </div>
      </div>

      {/* Analyze Button */}
      <button
        className={`analyze-btn ${canAnalyze ? 'ready' : 'disabled'} ${loading ? 'loading' : ''}`}
        onClick={onAnalyze}
        disabled={!canAnalyze || loading}
      >
        {loading ? (
          <>
            <span className="btn-spinner" />
            <span>AI Analiz Yapıyor...</span>
          </>
        ) : (
          <>
            <span className="btn-icon">⚡</span>
            <span>Maç Analizi Yap</span>
          </>
        )}
      </button>

      {!canAnalyze && !loading && (
        <p className="hint-text">
          {!homeTeam && !awayTeam ? 'Her iki taraf için takım seçin'
            : !homeTeam ? 'Ev sahibi takım seçin'
              : !awayTeam ? 'Deplasman takımı seçin'
                : 'Aynı takımı iki tarafa seçemezsiniz'}
        </p>
      )}
    </div>
  );
}
