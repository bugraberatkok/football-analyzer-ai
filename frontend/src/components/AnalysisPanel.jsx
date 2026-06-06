import React from 'react';
import './AnalysisPanel.css';

function parseAiSections(text) {
  if (!text) return [];
  const sections = [];

  // Numbered section headers: 1) TITLE or 1. TITLE
  const sectionRegex = /(\d+[\)\.]\s*[A-ZÇĞİÖŞÜa-zçğışöşü\s\/\(\)]+)\n([\s\S]*?)(?=\d+[\)\.]\s*[A-ZÇĞİÖŞÜ]|$)/g;
  let match;
  let hasMatches = false;

  while ((match = sectionRegex.exec(text)) !== null) {
    hasMatches = true;
    sections.push({
      title: match[1].trim(),
      content: match[2].trim(),
    });
  }

  if (!hasMatches) {
    // fallback: just show as raw text
    sections.push({ title: null, content: text });
  }

  return sections;
}

function BoldedText({ text }) {
  // Replace **bold** with <strong>
  const parts = text.split(/\*\*(.*?)\*\*/g);
  return (
    <>
      {parts.map((part, i) =>
        i % 2 === 1 ? <strong key={i}>{part}</strong> : part
      )}
    </>
  );
}

function renderContent(content) {
  const lines = content.split('\n').filter(l => l.trim());
  return lines.map((line, i) => {
    const trimmed = line.trim();
    if (trimmed.startsWith('* ') || trimmed.startsWith('- ')) {
      return (
        <div key={i} className="ai-bullet">
          <span className="bullet-dot">▸</span>
          <BoldedText text={trimmed.slice(2)} />
        </div>
      );
    }
    if (trimmed.startsWith('#')) {
      return <p key={i} className="ai-subheader"><BoldedText text={trimmed.replace(/^#+\s*/, '')} /></p>;
    }
    return <p key={i} className="ai-paragraph"><BoldedText text={trimmed} /></p>;
  });
}

function getSectionIcon(title) {
  const t = title?.toUpperCase() || '';
  if (t.includes('FORM') || t.includes('MOMENTUM')) return '📈';
  if (t.includes('İSTATİSTİK') || t.includes('XG')) return '📊';
  if (t.includes('TAKTİK') || t.includes('ÖNGÖRÜ')) return '🧠';
  return '⚡';
}

export default function AnalysisPanel({ result, homeTeam, awayTeam, loading }) {
  if (loading) {
    return (
      <div className="analysis-panel loading-state">
        <div className="loading-content">
          <div className="loading-field">
            <div className="field-lines">
              <div className="fl fl1" /><div className="fl fl2" /><div className="fl fl3" />
              <div className="ball">⚽</div>
            </div>
          </div>
          <h3>AI Analiz Yapıyor</h3>
          <p>Sofascore'dan veriler çekiliyor ve Gemini AI analiz ediyor...</p>
          <div className="loading-steps">
            <div className="step active">📡 Sofascore API</div>
            <div className="step-arrow">→</div>
            <div className="step pulse">🤖 Gemini AI</div>
            <div className="step-arrow">→</div>
            <div className="step">📊 Analiz</div>
          </div>
        </div>
      </div>
    );
  }

  if (!result) {
    return (
      <div className="analysis-panel empty-state">
        <div className="empty-content">
          <div className="empty-icon">🏟️</div>
          <h3>Maç Analizi Bekleniyor</h3>
          <p>Sol panelden iki takım seçin ve <strong>"Maç Analizi Yap"</strong> butonuna tıklayın.</p>
          <div className="feature-chips">
            <span>📈 Form Analizi</span>
            <span>📊 xG & İstatistikler</span>
            <span>🧠 Taktik Öngörü</span>
            <span>🤖 Gemini AI</span>
          </div>
        </div>
      </div>
    );
  }

  const sections = parseAiSections(result.rawDataSummary || '');

  return (
    <div className="analysis-panel result-state">
      {/* Result Banner */}
      <div className="result-banner">
        <div className="banner-teams">
          <span className="banner-team home">
            {homeTeam?.logo?.startsWith('http') ? (
              <img src={homeTeam.logo} alt={homeTeam.name} className="banner-team-crest" />
            ) : (
              homeTeam?.logo
            )}{' '}
            {homeTeam?.name}
          </span>
          <span className="banner-vs">VS</span>
          <span className="banner-team away">
            {awayTeam?.name}{' '}
            {awayTeam?.logo?.startsWith('http') ? (
              <img src={awayTeam.logo} alt={awayTeam.name} className="banner-team-crest" />
            ) : (
              awayTeam?.logo
            )}
          </span>
        </div>
        <div className="banner-meta">
          <span className="meta-tag">🤖 Gemini AI Analizi</span>
          <span className="meta-tag">📡 Sofascore Verisi</span>
          <span className="meta-tag">🇹🇷 Süper Lig</span>
        </div>
      </div>

      {/* AI Output */}
      <div className="ai-sections">
        {sections.map((section, i) => (
          <div key={i} className={`ai-section section-${i}`}>
            {section.title && (
              <div className="section-header">
                <span className="section-icon">{getSectionIcon(section.title)}</span>
                <span className="section-title">{section.title}</span>
                <div className="section-line" />
              </div>
            )}
            <div className="section-body">
              {renderContent(section.content)}
            </div>
          </div>
        ))}
      </div>

      {/* Footer */}
      <div className="result-footer">
        <span className="footer-note">
          ⚠️ Bu analiz yapay zeka tarafından oluşturulmuştur. Bahis kararı için kullanmayın.
        </span>
      </div>
    </div>
  );
}
