import React, { useState } from 'react';
import { User } from '../types';
import { BookOpen, ArrowRight, Sparkles, UserCheck, ShieldCheck } from 'lucide-react';

interface LandingScreenProps {
  existingUsers: User[];
  onEnter: (studentName: string, field?: string) => void;
}

export const LandingScreen: React.FC<LandingScreenProps> = ({ existingUsers, onEnter }) => {
  const [showIdentityPrompt, setShowIdentityPrompt] = useState(false);
  const [nameInput, setNameInput] = useState('');
  const [fieldInput, setFieldInput] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (nameInput.trim()) {
      onEnter(nameInput.trim(), fieldInput.trim());
    }
  };

  return (
    <div className="relative min-h-screen flex flex-col items-center justify-center p-6 bg-[#0f1015] overflow-hidden">
      {/* Background ambient radial glow */}
      <div className="absolute top-1/4 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-[#f5b041]/5 rounded-full blur-[140px] pointer-events-none" />
      <div className="absolute bottom-10 right-10 w-[400px] h-[400px] bg-[#8b7cf6]/5 rounded-full blur-[120px] pointer-events-none" />

      {/* Main card */}
      <div className="relative z-10 w-full max-w-xl text-center space-y-8 animate-fade-in">
        {/* Brand Icon Motif */}
        <div className="inline-flex items-center justify-center w-20 h-20 rounded-3xl bg-gradient-to-br from-[#1b1e26] to-[#121316] border border-[#f5b041]/40 shadow-2xl shadow-[#f5b041]/10">
          <BookOpen className="w-10 h-10 text-[#f5b041]" />
        </div>

        {/* Title & Tagline */}
        <div className="space-y-3">
          <h1 className="text-3xl md:text-5xl font-black font-display tracking-wider text-[#f8fafc]">
            INNOVARA NOTES
          </h1>
          <blockquote className="text-lg md:text-xl font-serif italic text-[#ffcf66] font-light max-w-md mx-auto leading-relaxed">
            “Every student has a story.
            <br />
            Every idea deserves a place.”
          </blockquote>
          <p className="text-xs md:text-sm text-[#94a3b8] max-w-md mx-auto pt-2 leading-relaxed">
            A digital bookshelf where students openly document how they think, what they build, and what they discover next.
          </p>
        </div>

        {/* Interactive Steps */}
        {!showIdentityPrompt ? (
          <div className="pt-4 flex flex-col items-center gap-4">
            <button
              type="button"
              onClick={() => setShowIdentityPrompt(true)}
              className="flex items-center gap-3 px-8 py-4 rounded-2xl bg-[#f5b041] hover:bg-[#ffcf66] text-[#0f1015] font-extrabold text-base md:text-lg shadow-xl hover:shadow-2xl hover:scale-[1.02] active:scale-[0.98] transition-all duration-200"
            >
              <span>Enter Innovara Notes</span>
              <ArrowRight className="w-5 h-5" />
            </button>
            <span className="text-xs text-[#64748b]">Open Community Knowledge Shelf</span>
          </div>
        ) : (
          <form
            onSubmit={handleSubmit}
            className="p-6 md:p-8 rounded-2xl bg-[#161820]/95 border border-[#333846] shadow-2xl text-left space-y-5 animate-fade-in"
          >
            <div>
              <h2 className="text-xl font-bold font-display text-[#f8fafc]">Who are you?</h2>
              <p className="text-xs text-[#94a3b8] mt-1">
                Enter your name to open or create your personal digital shelf. You will be remembered on this device.
              </p>
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#cbd5e1] mb-2">
                Your Full Name *
              </label>
              <input
                type="text"
                required
                autoFocus
                value={nameInput}
                onChange={(e) => setNameInput(e.target.value)}
                placeholder="e.g. Alex Morgan, Maya Lin, Steve..."
                className="w-full px-4 py-3 rounded-xl bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm md:text-base focus:outline-none focus:border-[#f5b041] focus:ring-1 focus:ring-[#f5b041]/50 font-medium"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#cbd5e1] mb-2">
                Discipline / Academic Field (Optional)
              </label>
              <input
                type="text"
                value={fieldInput}
                onChange={(e) => setFieldInput(e.target.value)}
                placeholder="e.g. AI & Data Science, IoT Engineering, Design..."
                className="w-full px-4 py-2.5 rounded-xl bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
              />
            </div>

            {/* Quick list of previously registered students on this device if any */}
            {existingUsers.length > 0 && (
              <div className="pt-1">
                <span className="block text-[11px] font-semibold text-[#64748b] mb-2 uppercase tracking-wider">
                  Or continue as an existing shelf on this device:
                </span>
                <div className="flex flex-wrap gap-2">
                  {existingUsers.map((user) => (
                    <button
                      key={user.id}
                      type="button"
                      onClick={() => onEnter(user.name, user.field)}
                      className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-[#1b1e26] hover:bg-[#252934] border border-[#2e3342] hover:border-[#f5b041] text-xs font-medium text-[#cbd5e1] transition-all"
                    >
                      <span
                        className="w-4 h-4 rounded-full flex items-center justify-center text-[10px] font-bold"
                        style={{ backgroundColor: `${user.avatarColor}30`, color: user.avatarColor }}
                      >
                        {user.name.charAt(0)}
                      </span>
                      <span>{user.name}</span>
                    </button>
                  ))}
                </div>
              </div>
            )}

            <div className="flex items-center gap-2 text-[11px] text-[#64748b] pt-1">
              <ShieldCheck className="w-4 h-4 text-emerald-400 shrink-0" />
              <span>Your session and all shelf developments are remembered permanently on this device.</span>
            </div>

            <div className="pt-2">
              <button
                type="submit"
                disabled={!nameInput.trim()}
                className="w-full py-3.5 rounded-xl bg-[#f5b041] hover:bg-[#ffcf66] disabled:opacity-50 text-[#0f1015] font-extrabold text-sm md:text-base transition-all shadow-lg cursor-pointer"
              >
                Enter Notes
              </button>
            </div>
          </form>
        )}
      </div>

      {/* Footer Info */}
      <footer className="absolute bottom-6 text-center text-xs text-[#64748b]">
        Innovara Notes • Living Innovation Notebook Platform
      </footer>
    </div>
  );
};
