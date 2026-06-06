import React, { useState } from 'react';
import { superligTeams } from '../data/superligTeams';
import './TeamSelector.css';

export default function TeamSelector({ side, selected, onSelect }) {
  const [search, setSearch] = useState('');

  const filtered = superligTeams.filter(t =>
    t.name.toLowerCase().includes(search.toLowerCase())
  );

  const label = side === 'home' ? 'EV SAHİBİ' : 'DEPLASMAN';
  const accentClass = side === 'home' ? 'home-accent' : 'away-accent';

  return (
    <div className={`team-selector ${accentClass}`}>
      <div className="selector-header">
        <span className="selector-badge">{label}</span>
        {selected && (
          <div className="selected-preview">
            <span className="selected-logo">
              {selected.logo.startsWith('http') ? (
                <img src={selected.logo} alt={selected.name} className="selected-logo-img" />
              ) : (
                selected.logo
              )}
            </span>
            <span className="selected-name">{selected.name}</span>
          </div>
        )}
      </div>

      <div className="search-box">
        <span className="search-icon">🔍</span>
        <input
          type="text"
          placeholder="Takım ara..."
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
      </div>

      <div className="teams-list">
        {filtered.map(team => (
          <button
            key={team.id}
            className={`team-item ${selected?.id === team.id ? 'active' : ''}`}
            onClick={() => onSelect(team)}
            style={{
              '--team-color': team.primaryColor,
            }}
          >
            <span className="team-logo">
              {team.logo.startsWith('http') ? (
                <img src={team.logo} alt={team.name} className="team-logo-img" />
              ) : (
                team.logo
              )}
            </span>
            <div className="team-info">
              <span className="team-name">{team.name}</span>
              <span className="team-city">{team.city}</span>
            </div>
            <span className="team-short">{team.shortName}</span>
            {selected?.id === team.id && <span className="check-icon">✓</span>}
          </button>
        ))}
      </div>
    </div>
  );
}
