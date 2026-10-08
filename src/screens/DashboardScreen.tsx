import React from 'react';
import { User, Activity, Note, Project, Task, Development } from '../types';
import { StudentCard } from '../components/StudentCard';
import { storage } from '../services/storage';
import {
  Lightbulb,
  Rocket,
  Brain,
  CheckCircle2,
  Wrench,
  BookOpen,
  ArrowRight,
  Plus,
  Flame,
  Sparkles,
} from 'lucide-react';

interface DashboardScreenProps {
  currentUser: User;
  allUsers: User[];
  notes: Note[];
  projects: Project[];
  tasks: Task[];
  developments: Development[];
  activities: Activity[];
  onOpenMyShelf: () => void;
  onOpenUserShelf: (user: User) => void;
  onOpenBookshelf: () => void;
  onAddEntry: () => void;
}

export const DashboardScreen: React.FC<DashboardScreenProps> = ({
  currentUser,
  allUsers,
  notes,
  projects,
  tasks,
  developments,
  activities,
  onOpenMyShelf,
  onOpenUserShelf,
  onOpenBookshelf,
  onAddEntry,
}) => {
  const currentHour = new Date().getHours();
  const greeting =
    currentHour < 12 ? 'Good morning' : currentHour < 17 ? 'Good afternoon' : 'Good evening';

  const userStats = storage.getUserStats(currentUser.id);
  const ideasCount = userStats.ideasCount;
  const projectsCount = userStats.projectsCount;
  const thoughtsCount = userStats.thoughtsCount;
  const tasksCount = userStats.tasksCount;
  const developmentsCount = userStats.developmentsCount;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-8 space-y-8 animate-fade-in">
      {/* Top Welcome & Quick Action */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 pb-2 border-b border-[#2e3342]/60">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-2xl md:text-3xl font-extrabold font-display text-[#f8fafc]">
              {greeting}, {currentUser.name.split(' ')[0]} 👋
            </h1>
          </div>
          <p className="text-xs md:text-sm text-[#8b7cf6] mt-0.5">{currentUser.field}</p>
        </div>

        <div className="flex items-center gap-3">
          <button
            type="button"
            onClick={onAddEntry}
            className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-[#f5b041] hover:bg-[#ffcf66] text-[#0f1015] font-extrabold text-xs md:text-sm shadow-lg shadow-[#f5b041]/10 transition-all"
          >
            <Plus className="w-4 h-4" />
            <span>+ Add Entry</span>
          </button>
        </div>
      </div>

      {/* Hero Card: Personal Student Shelf Quick Access */}
      <div className="relative rounded-2xl overflow-hidden border border-[#f5b041]/40 bg-gradient-to-r from-[#1b1e26] via-[#161820] to-[#121316] p-6 md:p-8 shadow-xl">
        <div className="relative z-10 flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
          <div className="space-y-2 max-w-xl">
            <div className="flex items-center gap-2">
              <span className="px-2.5 py-0.5 rounded-md bg-[#f5b041]/20 text-[#f5b041] font-mono font-bold text-xs">
                DIGITAL NOTEBOOK
              </span>
              <span className="text-xs text-[#94a3b8]">Personal Innovation Journal</span>
            </div>
            <h2 className="text-xl md:text-2xl font-bold font-display text-[#f8fafc]">
              {currentUser.name}’s Living Book
            </h2>
            <p className="text-xs md:text-sm text-[#cbd5e1] italic">
              “{currentUser.quote || 'Building ideas from imagination into systems.'}”
            </p>
          </div>

          <div className="flex items-center gap-3 shrink-0">
            <button
              type="button"
              onClick={onOpenMyShelf}
              className="flex items-center gap-2 px-5 py-3 rounded-xl bg-[#222631] hover:bg-[#2c3140] border border-[#f5b041] text-[#f5b041] hover:text-[#ffcf66] font-bold text-xs md:text-sm shadow-md transition-all group"
            >
              <BookOpen className="w-4 h-4" />
              <span>Open My Shelf</span>
              <ArrowRight className="w-4 h-4 transition-transform group-hover:translate-x-1" />
            </button>
          </div>
        </div>
      </div>

      {/* YOUR INNOVARA SPACE - Stat Tiles */}
      <div>
        <h2 className="text-xs font-bold text-[#94a3b8] uppercase tracking-wider mb-4 flex items-center gap-2">
          <Sparkles className="w-4 h-4 text-[#f5b041]" />
          Your Innovara Space
        </h2>

        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3 md:gap-4">
          <div
            onClick={onOpenMyShelf}
            className="p-4 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-[#f5b041]/50 cursor-pointer transition-all"
          >
            <div className="flex items-center justify-between text-[#f5b041] mb-2">
              <Lightbulb className="w-5 h-5" />
              <span className="text-xl md:text-2xl font-black font-display">{ideasCount}</span>
            </div>
            <p className="text-xs font-semibold text-[#cbd5e1]">Ideas</p>
            <span className="text-[10px] text-[#64748b]">Inventions & Sparks</span>
          </div>

          <div
            onClick={onOpenMyShelf}
            className="p-4 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-[#38bdf8]/50 cursor-pointer transition-all"
          >
            <div className="flex items-center justify-between text-[#38bdf8] mb-2">
              <Rocket className="w-5 h-5" />
              <span className="text-xl md:text-2xl font-black font-display">{projectsCount}</span>
            </div>
            <p className="text-xs font-semibold text-[#cbd5e1]">Projects</p>
            <span className="text-[10px] text-[#64748b]">Active Builds</span>
          </div>

          <div
            onClick={onOpenMyShelf}
            className="p-4 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-[#8b7cf6]/50 cursor-pointer transition-all"
          >
            <div className="flex items-center justify-between text-[#8b7cf6] mb-2">
              <Brain className="w-5 h-5" />
              <span className="text-xl md:text-2xl font-black font-display">{thoughtsCount}</span>
            </div>
            <p className="text-xs font-semibold text-[#cbd5e1]">Thoughts</p>
            <span className="text-[10px] text-[#64748b]">Reflections & Hypotheses</span>
          </div>

          <div
            onClick={onOpenMyShelf}
            className="p-4 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-emerald-400/50 cursor-pointer transition-all"
          >
            <div className="flex items-center justify-between text-emerald-400 mb-2">
              <CheckCircle2 className="w-5 h-5" />
              <span className="text-xl md:text-2xl font-black font-display">{tasksCount}</span>
            </div>
            <p className="text-xs font-semibold text-[#cbd5e1]">Tasks</p>
            <span className="text-[10px] text-[#64748b]">Action Backlog</span>
          </div>

          <div
            onClick={onOpenMyShelf}
            className="p-4 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-pink-400/50 cursor-pointer transition-all col-span-2 sm:col-span-1"
          >
            <div className="flex items-center justify-between text-pink-400 mb-2">
              <Wrench className="w-5 h-5" />
              <span className="text-xl md:text-2xl font-black font-display">{developmentsCount}</span>
            </div>
            <p className="text-xs font-semibold text-[#cbd5e1]">Developments</p>
            <span className="text-[10px] text-[#64748b]">Milestones Logged</span>
          </div>
        </div>
      </div>

      {/* EXPLORE INNOVATORS - Horizontal Bookshelf preview */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xs font-bold text-[#94a3b8] uppercase tracking-wider flex items-center gap-2">
            <BookOpen className="w-4 h-4 text-[#f5b041]" />
            Explore Innovators
          </h2>
          <button
            type="button"
            onClick={onOpenBookshelf}
            className="text-xs font-semibold text-[#f5b041] hover:text-[#ffcf66] flex items-center gap-1 transition-colors"
          >
            <span>View All Bookshelf</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {allUsers.slice(0, 3).map((user) => {
            const stats = storage.getUserStats(user.id);
            return (
              <StudentCard
                key={user.id}
                user={user}
                ideasCount={stats.ideasCount}
                notesCount={stats.notesCount}
                projectsCount={stats.projectsCount}
                tasksCount={stats.tasksCount}
                onOpenBook={() => onOpenUserShelf(user)}
              />
            );
          })}
        </div>
      </div>

      {/* RECENT INNOVARA ACTIVITY */}
      <div>
        <h2 className="text-xs font-bold text-[#94a3b8] uppercase tracking-wider mb-4 flex items-center gap-2">
          <Flame className="w-4 h-4 text-orange-400" />
          Recent Innovara Activity
        </h2>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          {activities.slice(0, 6).map((act) => {
            const user = allUsers.find((u) => u.id === act.userId);
            const icon =
              act.targetType === 'IDEA'
                ? '💡'
                : act.targetType === 'PROJECT'
                ? '🚀'
                : act.targetType === 'TASK'
                ? '✅'
                : act.targetType === 'DEV_LOG'
                ? '🛠'
                : '🧠';

            return (
              <div
                key={act.id}
                onClick={() => {
                  if (user) onOpenUserShelf(user);
                }}
                className="flex items-center gap-3.5 p-3.5 rounded-xl bg-[#161820] border border-[#2e3342] hover:border-[#f5b041]/40 cursor-pointer transition-all"
              >
                <span className="text-2xl p-2 rounded-lg bg-[#121316] shrink-0">{icon}</span>
                <div className="flex-1 min-w-0">
                  <div className="flex items-center justify-between text-xs mb-0.5">
                    <span className="font-bold text-[#f8fafc] truncate">{act.userName}</span>
                    <span className="text-[10px] text-[#64748b]">
                      {new Date(act.timestamp).toLocaleTimeString([], {
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </span>
                  </div>
                  <p className="text-xs text-[#94a3b8] truncate">
                    {act.action}: <span className="text-[#f5b041] font-medium">“{act.targetTitle}”</span>
                  </p>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
