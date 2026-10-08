import React, { useState } from 'react';
import { User, Note, Project, Development } from '../types';
import { Search, X, ArrowRight, UserCheck, Rocket, Lightbulb, Brain, Wrench, BookOpen } from 'lucide-react';

interface GlobalSearchScreenProps {
  allUsers: User[];
  notes: Note[];
  projects: Project[];
  developments: Development[];
  onOpenUserShelf: (user: User) => void;
}

export const GlobalSearchScreen: React.FC<GlobalSearchScreenProps> = ({
  allUsers,
  notes,
  projects,
  developments,
  onOpenUserShelf,
}) => {
  const [query, setQuery] = useState('');

  const cleanQuery = query.trim().toLowerCase();

  const matchedUsers = cleanQuery
    ? allUsers.filter(
        (u) =>
          u.name.toLowerCase().includes(cleanQuery) ||
          u.field.toLowerCase().includes(cleanQuery) ||
          u.interests.toLowerCase().includes(cleanQuery)
      )
    : [];

  const matchedProjects = cleanQuery
    ? projects.filter(
        (p) =>
          p.name.toLowerCase().includes(cleanQuery) ||
          p.description.toLowerCase().includes(cleanQuery) ||
          p.technologies.some((t) => t.toLowerCase().includes(cleanQuery))
      )
    : [];

  const matchedNotes = cleanQuery
    ? notes.filter(
        (n) =>
          n.title.toLowerCase().includes(cleanQuery) ||
          n.content.toLowerCase().includes(cleanQuery) ||
          n.tags.some((t) => t.toLowerCase().includes(cleanQuery))
      )
    : [];

  const matchedDevelopments = cleanQuery
    ? developments.filter(
        (d) =>
          d.title.toLowerCase().includes(cleanQuery) ||
          d.whatChanged.toLowerCase().includes(cleanQuery) ||
          d.whatWorked.toLowerCase().includes(cleanQuery)
      )
    : [];

  const totalResults =
    matchedUsers.length + matchedProjects.length + matchedNotes.length + matchedDevelopments.length;

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-8 space-y-6 animate-fade-in">
      <div className="space-y-1.5 pb-2 border-b border-[#2e3342]/60">
        <h1 className="text-2xl md:text-3xl font-extrabold font-display text-[#f8fafc] flex items-center gap-2">
          <span>🔎</span> Global Knowledge Search
        </h1>
        <p className="text-xs md:text-sm text-[#94a3b8]">
          Find ideas, projects, architectural blueprints, and research across every student’s book.
        </p>
      </div>

      {/* Search Input */}
      <div className="relative">
        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#f5b041]" />
        <input
          type="text"
          autoFocus
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder='Try searching "AI memory", "ESP32", "Consensus", "Raft", or a student name...'
          className="w-full pl-12 pr-10 py-3.5 rounded-xl bg-[#161820] border border-[#2e3342] text-[#f8fafc] text-sm md:text-base focus:outline-none focus:border-[#f5b041] transition-all"
        />
        {query && (
          <button
            type="button"
            onClick={() => setQuery('')}
            className="absolute right-3.5 top-1/2 -translate-y-1/2 p-1 text-[#94a3b8] hover:text-[#f8fafc]"
          >
            <X className="w-4 h-4" />
          </button>
        )}
      </div>

      {/* Suggested Search Chips */}
      {!cleanQuery && (
        <div className="p-6 rounded-2xl bg-[#161820] border border-[#2e3342] space-y-3">
          <p className="text-xs font-bold text-[#94a3b8] uppercase tracking-wider">Suggested Searches</p>
          <div className="flex flex-wrap gap-2">
            {['AI memory', 'KATE AI', 'Smart Waste', 'Raft', 'ESP32', 'Zero-Trust', 'Steve', 'Priya'].map(
              (term) => (
                <button
                  key={term}
                  type="button"
                  onClick={() => setQuery(term)}
                  className="px-3 py-1.5 rounded-lg bg-[#1b1e26] hover:bg-[#252934] border border-[#2e3342] text-xs font-medium text-[#cbd5e1] hover:text-[#f5b041] transition-colors"
                >
                  {term}
                </button>
              )
            )}
          </div>
        </div>
      )}

      {/* Results List */}
      {cleanQuery && totalResults === 0 && (
        <div className="text-center py-16 bg-[#161820] rounded-2xl border border-[#2e3342]">
          <p className="text-sm font-semibold text-[#f8fafc]">No results found for “{query}”</p>
          <p className="text-xs text-[#94a3b8] mt-1">Try another keyword or search by student name.</p>
        </div>
      )}

      {cleanQuery && totalResults > 0 && (
        <div className="space-y-6">
          {/* Matched Students */}
          {matchedUsers.length > 0 && (
            <div className="space-y-2">
              <h3 className="text-xs font-bold text-[#f5b041] uppercase tracking-wider">
                Students & Innovators ({matchedUsers.length})
              </h3>
              <div className="space-y-2">
                {matchedUsers.map((u) => (
                  <div
                    key={u.id}
                    onClick={() => onOpenUserShelf(u)}
                    className="flex items-center justify-between p-3.5 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-[#f5b041] cursor-pointer transition-all"
                  >
                    <div className="flex items-center gap-3">
                      <div
                        className="w-9 h-9 rounded-lg flex items-center justify-center font-bold text-sm"
                        style={{ backgroundColor: `${u.avatarColor}25`, color: u.avatarColor }}
                      >
                        {u.name.charAt(0)}
                      </div>
                      <div>
                        <h4 className="font-bold text-sm text-[#f8fafc]">{u.name}</h4>
                        <p className="text-xs text-[#8b7cf6]">{u.field}</p>
                      </div>
                    </div>
                    <ArrowRight className="w-4 h-4 text-[#94a3b8]" />
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Matched Projects */}
          {matchedProjects.length > 0 && (
            <div className="space-y-2">
              <h3 className="text-xs font-bold text-[#38bdf8] uppercase tracking-wider">
                Projects ({matchedProjects.length})
              </h3>
              <div className="space-y-2">
                {matchedProjects.map((p) => {
                  const owner = allUsers.find((u) => u.id === p.userId);
                  return (
                    <div
                      key={p.id}
                      onClick={() => {
                        if (owner) onOpenUserShelf(owner);
                      }}
                      className="flex items-center justify-between p-3.5 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-[#38bdf8] cursor-pointer transition-all"
                    >
                      <div className="flex items-center gap-3">
                        <span className="p-2 rounded-lg bg-[#38bdf8]/10 text-[#38bdf8]">
                          <Rocket className="w-4 h-4" />
                        </span>
                        <div>
                          <h4 className="font-bold text-sm text-[#f8fafc]">
                            {p.name}{' '}
                            <span className="text-xs font-normal text-[#94a3b8]">— {owner?.name}</span>
                          </h4>
                          <p className="text-xs text-[#cbd5e1] line-clamp-1">{p.tagline || p.description}</p>
                        </div>
                      </div>
                      <ArrowRight className="w-4 h-4 text-[#94a3b8]" />
                    </div>
                  );
                })}
              </div>
            </div>
          )}

          {/* Matched Notes */}
          {matchedNotes.length > 0 && (
            <div className="space-y-2">
              <h3 className="text-xs font-bold text-[#f5b041] uppercase tracking-wider">
                Notes & Ideas ({matchedNotes.length})
              </h3>
              <div className="space-y-2">
                {matchedNotes.map((n) => {
                  const owner = allUsers.find((u) => u.id === n.userId);
                  return (
                    <div
                      key={n.id}
                      onClick={() => {
                        if (owner) onOpenUserShelf(owner);
                      }}
                      className="flex items-center justify-between p-3.5 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-[#f5b041] cursor-pointer transition-all"
                    >
                      <div className="flex items-center gap-3">
                        <span className="p-2 rounded-lg bg-[#f5b041]/10 text-[#f5b041]">
                          <Lightbulb className="w-4 h-4" />
                        </span>
                        <div>
                          <h4 className="font-bold text-sm text-[#f8fafc]">
                            {n.title}{' '}
                            <span className="text-xs font-normal text-[#94a3b8]">— {owner?.name}</span>
                          </h4>
                          <p className="text-xs text-[#94a3b8] line-clamp-1">{n.content}</p>
                        </div>
                      </div>
                      <span className="text-[10px] px-2 py-0.5 rounded bg-[#1e2129] text-[#cbd5e1] font-mono border border-[#2e3342]">
                        {n.type}
                      </span>
                    </div>
                  );
                })}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
