import React from 'react';
import { User } from '../types';
import { BookOpen, Sparkles, CheckCircle2, Rocket, Lightbulb, ArrowRight } from 'lucide-react';

interface StudentCardProps {
  user: User;
  ideasCount: number;
  notesCount: number;
  projectsCount: number;
  tasksCount: number;
  onOpenBook: () => void;
}

export const StudentCard: React.FC<StudentCardProps> = ({
  user,
  ideasCount,
  notesCount,
  projectsCount,
  tasksCount,
  onOpenBook,
}) => {
  return (
    <div
      onClick={onOpenBook}
      className="group relative flex flex-col sm:flex-row rounded-2xl bg-[#16181f] border border-[#2e3342] hover:border-[#f5b041]/60 transition-all duration-300 shadow-lg hover:shadow-2xl overflow-hidden cursor-pointer"
    >
      {/* Decorative Book Spine */}
      <div
        className="w-full sm:w-3.5 h-2 sm:h-auto shrink-0 transition-all group-hover:brightness-125"
        style={{
          background: `linear-gradient(180deg, ${user.avatarColor} 0%, #f5b041 100%)`,
        }}
      />

      <div className="flex-1 p-5 flex flex-col justify-between gap-4">
        {/* Top: Avatar, Name, Field */}
        <div className="flex items-start justify-between gap-3">
          <div className="flex items-center gap-3.5">
            <div
              className="w-12 h-12 rounded-xl flex items-center justify-center font-bold text-lg font-display shrink-0 border border-white/10 shadow-inner"
              style={{
                backgroundColor: `${user.avatarColor}20`,
                color: user.avatarColor,
                borderColor: `${user.avatarColor}40`,
              }}
            >
              {user.name.charAt(0).toUpperCase()}
            </div>

            <div>
              <h3 className="font-bold text-base md:text-lg text-[#f8fafc] group-hover:text-[#f5b041] transition-colors leading-tight font-display">
                {user.name}
              </h3>
              <p className="text-xs font-medium text-[#8b7cf6] mt-0.5">{user.field}</p>
            </div>
          </div>

          <span className="text-[11px] font-mono text-[#94a3b8] bg-[#1e2129] px-2.5 py-1 rounded-full border border-[#2e3342]">
            Active
          </span>
        </div>

        {/* Quote / Motto */}
        {user.quote && (
          <p className="text-xs text-[#cbd5e1] italic line-clamp-2 pl-3 border-l-2 border-[#f5b041]/40">
            “{user.quote}”
          </p>
        )}

        {/* Metrics Grid */}
        <div className="grid grid-cols-4 gap-2 py-2 px-3 rounded-xl bg-[#121316] border border-[#262a36]">
          <div className="flex flex-col items-center justify-center text-center">
            <div className="flex items-center gap-1 text-[#f5b041]">
              <Lightbulb className="w-3.5 h-3.5" />
              <span className="font-bold text-xs">{ideasCount}</span>
            </div>
            <span className="text-[10px] text-[#94a3b8] mt-0.5">Ideas</span>
          </div>

          <div className="flex flex-col items-center justify-center text-center">
            <div className="flex items-center gap-1 text-[#38bdf8]">
              <BookOpen className="w-3.5 h-3.5" />
              <span className="font-bold text-xs">{notesCount}</span>
            </div>
            <span className="text-[10px] text-[#94a3b8] mt-0.5">Notes</span>
          </div>

          <div className="flex flex-col items-center justify-center text-center">
            <div className="flex items-center gap-1 text-[#a78bfa]">
              <Rocket className="w-3.5 h-3.5" />
              <span className="font-bold text-xs">{projectsCount}</span>
            </div>
            <span className="text-[10px] text-[#94a3b8] mt-0.5">Projects</span>
          </div>

          <div className="flex flex-col items-center justify-center text-center">
            <div className="flex items-center gap-1 text-emerald-400">
              <CheckCircle2 className="w-3.5 h-3.5" />
              <span className="font-bold text-xs">{tasksCount}</span>
            </div>
            <span className="text-[10px] text-[#94a3b8] mt-0.5">Tasks</span>
          </div>
        </div>

        {/* Bottom Bar: Action */}
        <div className="flex items-center justify-between pt-1 border-t border-[#262a36]/60">
          <span className="text-[11px] text-[#64748b]">Open Community Shelf</span>

          <button
            type="button"
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-[#222631] group-hover:bg-[#f5b041] text-[#f5b041] group-hover:text-[#0f1015] font-semibold text-xs transition-all duration-200"
          >
            <BookOpen className="w-3.5 h-3.5" />
            <span>Open Book</span>
            <ArrowRight className="w-3.5 h-3.5 transition-transform group-hover:translate-x-0.5" />
          </button>
        </div>
      </div>
    </div>
  );
};
