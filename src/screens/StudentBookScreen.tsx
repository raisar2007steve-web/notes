import React, { useState } from 'react';
import { User, Note, Project, Task, Development, Activity, Version } from '../types';
import { MarkdownRenderer } from '../components/MarkdownRenderer';
import { MarkdownEditor } from '../components/MarkdownEditor';
import { AddEntryModal } from '../components/AddEntryModal';
import { VersionHistoryModal } from '../components/VersionHistoryModal';
import { ProjectDetailModal } from '../components/ProjectDetailModal';
import {
  ArrowLeft,
  Lightbulb,
  Brain,
  Wrench,
  CheckCircle2,
  Rocket,
  BookOpen,
  BarChart3,
  Plus,
  History,
  Lock,
  Globe,
  Trash2,
  ExternalLink,
  Code2,
  Calendar,
  Tag,
  Check,
} from 'lucide-react';
import confetti from 'canvas-confetti';

interface StudentBookScreenProps {
  user: User;
  currentUser: User | null;
  notes: Note[];
  projects: Project[];
  tasks: Task[];
  developments: Development[];
  activities: Activity[];
  onBackToBookshelf: () => void;
  onSaveNote: (note: Note) => void;
  onDeleteNote: (id: string) => void;
  onSaveProject: (project: Project) => void;
  onDeleteProject: (id: string) => void;
  onSaveTask: (task: Task) => void;
  onToggleTask: (taskId: string) => void;
  onDeleteTask: (id: string) => void;
  onSaveDevelopment: (dev: Development) => void;
  onDeleteDevelopment: (id: string) => void;
  getVersionsForTarget: (targetId: string) => Version[];
}

export const StudentBookScreen: React.FC<StudentBookScreenProps> = ({
  user,
  currentUser,
  notes,
  projects,
  tasks,
  developments,
  activities,
  onBackToBookshelf,
  onSaveNote,
  onDeleteNote,
  onSaveProject,
  onDeleteProject,
  onSaveTask,
  onToggleTask,
  onDeleteTask,
  onSaveDevelopment,
  onDeleteDevelopment,
  getVersionsForTarget,
}) => {
  const [activeTab, setActiveTab] = useState<
    'ideas' | 'thoughts' | 'developments' | 'tasks' | 'projects' | 'notebook' | 'stats'
  >('ideas');

  const [showAddModal, setShowAddModal] = useState(false);
  const [selectedHistoryTarget, setSelectedHistoryTarget] = useState<{ id: string; title: string } | null>(null);
  const [selectedProjectModal, setSelectedProjectModal] = useState<Project | null>(null);

  // General Markdown Notebook State
  const generalNotes = notes.filter((n) => n.type === 'GENERAL');
  const [notebookTitle, setNotebookTitle] = useState(
    generalNotes[0]?.title || 'Research & Engineering Scratchpad'
  );
  const [notebookContent, setNotebookContent] = useState(
    generalNotes[0]?.content ||
      `# Innovara Research Scratchpad\n\nDocument ideas, mathematical proofs, and system designs.\n\n## Core Principles\n- Keep microservices zero-dependency\n- Verify all peer states before gossip\n- Memory consolidation occurs every 4 hours`
  );
  const [autosaveStatus, setAutosaveStatus] = useState('Saved ✓');

  const isOwner = currentUser?.id === user.id;

  const handleNotebookChange = (val: string) => {
    setNotebookContent(val);
    setAutosaveStatus('Saving...');
    setTimeout(() => {
      setAutosaveStatus('Saved ✓');
    }, 400);
  };

  const handleSaveNotebook = () => {
    const existing = generalNotes[0];
    const updatedNote: Note = {
      id: existing ? existing.id : 'note_' + Math.random().toString(36).substring(2, 9),
      userId: user.id,
      type: 'GENERAL',
      title: notebookTitle.trim() || 'General Scratchpad',
      content: notebookContent,
      category: 'General',
      status: '💭 Idea',
      tags: ['Notebook', 'Scratchpad'],
      visibility: 'PUBLIC',
      createdAt: existing ? existing.createdAt : Date.now(),
      updatedAt: Date.now(),
    };
    onSaveNote(updatedNote);
    setAutosaveStatus('Saved ✓');
  };

  const ideas = notes.filter((n) => n.type === 'IDEA');
  const thoughts = notes.filter((n) => ['THOUGHT', 'RESEARCH', 'LEARNING'].includes(n.type));

  const triggerTaskConfetti = () => {
    confetti({
      particleCount: 40,
      spread: 60,
      origin: { y: 0.8 },
      colors: ['#f5b041', '#38bdf8', '#8b7cf6', '#34d399'],
    });
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-6 space-y-6 animate-fade-in pb-20">
      {/* Top Header Card */}
      <div className="rounded-2xl p-6 bg-gradient-to-r from-[#1b1e26] via-[#161820] to-[#121316] border border-[#2e3342] shadow-xl space-y-4">
        {/* Navigation row & Editing/Viewing status */}
        <div className="flex items-center justify-between">
          <button
            type="button"
            onClick={onBackToBookshelf}
            className="flex items-center gap-2 text-xs font-bold text-[#f5b041] hover:text-[#ffcf66] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
            <span>← Bookshelf</span>
          </button>

          <div className="flex items-center gap-2">
            <span
              className={`px-3 py-1 rounded-full text-xs font-semibold flex items-center gap-1.5 ${
                isOwner
                  ? 'bg-[#f5b041]/15 text-[#f5b041] border border-[#f5b041]/30'
                  : 'bg-[#8b7cf6]/15 text-[#8b7cf6] border border-[#8b7cf6]/30'
              }`}
            >
              <Globe className="w-3.5 h-3.5" />
              <span>{isOwner ? 'Editing: My Notes' : `Community View: ${currentUser?.name || 'Guest'}`}</span>
            </span>
          </div>
        </div>

        {/* Profile Details */}
        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pt-2">
          <div className="flex items-center gap-4">
            <div
              className="w-16 h-16 rounded-2xl flex items-center justify-center text-2xl font-black font-display shrink-0 border border-white/10 shadow-lg"
              style={{
                backgroundColor: `${user.avatarColor}25`,
                color: user.avatarColor,
                borderColor: `${user.avatarColor}40`,
              }}
            >
              {user.name.charAt(0).toUpperCase()}
            </div>

            <div className="space-y-0.5">
              <h1 className="text-xl md:text-2xl font-black font-display text-[#f8fafc]">
                {user.name}
              </h1>
              <p className="text-xs md:text-sm font-semibold text-[#8b7cf6]">{user.field}</p>
              {user.quote && (
                <p className="text-xs text-[#cbd5e1] italic pt-1">“{user.quote}”</p>
              )}
            </div>
          </div>

          <div className="flex items-center gap-3">
            <button
              type="button"
              onClick={() => setShowAddModal(true)}
              className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-[#f5b041] hover:bg-[#ffcf66] text-[#0f1015] font-extrabold text-xs md:text-sm shadow-md hover:scale-[1.02] transition-all"
            >
              <Plus className="w-4 h-4" />
              <span>+ Add Entry</span>
            </button>
          </div>
        </div>
      </div>

      {/* Book Tabs Navigation */}
      <div className="flex items-center gap-1.5 overflow-x-auto pb-1 border-b border-[#2e3342] scrollbar-none">
        <button
          type="button"
          onClick={() => setActiveTab('ideas')}
          className={`flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap ${
            activeTab === 'ideas'
              ? 'bg-[#f5b041] text-[#0f1015] shadow-md'
              : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1b1e26]'
          }`}
        >
          <Lightbulb className="w-4 h-4" />
          <span>💡 Ideas ({ideas.length})</span>
        </button>

        <button
          type="button"
          onClick={() => setActiveTab('thoughts')}
          className={`flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap ${
            activeTab === 'thoughts'
              ? 'bg-[#f5b041] text-[#0f1015] shadow-md'
              : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1b1e26]'
          }`}
        >
          <Brain className="w-4 h-4" />
          <span>🧠 Thoughts ({thoughts.length})</span>
        </button>

        <button
          type="button"
          onClick={() => setActiveTab('developments')}
          className={`flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap ${
            activeTab === 'developments'
              ? 'bg-[#f5b041] text-[#0f1015] shadow-md'
              : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1b1e26]'
          }`}
        >
          <Wrench className="w-4 h-4" />
          <span>🛠 Developments ({developments.length})</span>
        </button>

        <button
          type="button"
          onClick={() => setActiveTab('tasks')}
          className={`flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap ${
            activeTab === 'tasks'
              ? 'bg-[#f5b041] text-[#0f1015] shadow-md'
              : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1b1e26]'
          }`}
        >
          <CheckCircle2 className="w-4 h-4" />
          <span>✅ To-Do ({tasks.length})</span>
        </button>

        <button
          type="button"
          onClick={() => setActiveTab('projects')}
          className={`flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap ${
            activeTab === 'projects'
              ? 'bg-[#f5b041] text-[#0f1015] shadow-md'
              : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1b1e26]'
          }`}
        >
          <Rocket className="w-4 h-4" />
          <span>🚀 Projects ({projects.length})</span>
        </button>

        <button
          type="button"
          onClick={() => setActiveTab('notebook')}
          className={`flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap ${
            activeTab === 'notebook'
              ? 'bg-[#f5b041] text-[#0f1015] shadow-md'
              : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1b1e26]'
          }`}
        >
          <BookOpen className="w-4 h-4" />
          <span>📝 Notebook</span>
        </button>

        <button
          type="button"
          onClick={() => setActiveTab('stats')}
          className={`flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap ${
            activeTab === 'stats'
              ? 'bg-[#f5b041] text-[#0f1015] shadow-md'
              : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1b1e26]'
          }`}
        >
          <BarChart3 className="w-4 h-4" />
          <span>📊 Overview & Stats</span>
        </button>
      </div>

      {/* Tab Panels */}
      {/* 1. IDEAS TAB */}
      {activeTab === 'ideas' && (
        <div className="space-y-4">
          {ideas.length === 0 ? (
            <div className="text-center py-16 bg-[#161820] rounded-2xl border border-[#2e3342] space-y-3">
              <span className="text-4xl">💡</span>
              <h3 className="text-base font-bold text-[#f8fafc]">This book is waiting for its first idea page.</h3>
              <p className="text-xs text-[#94a3b8]">Have an idea? Write it down.</p>
              <button
                type="button"
                onClick={() => setShowAddModal(true)}
                className="px-4 py-2 rounded-xl bg-[#f5b041] text-[#0f1015] font-bold text-xs"
              >
                + Add First Idea
              </button>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {ideas.map((idea) => (
                <div
                  key={idea.id}
                  className="rounded-2xl p-5 bg-[#161820] border border-[#2e3342] hover:border-[#f5b041]/50 transition-all flex flex-col justify-between space-y-4"
                >
                  <div className="space-y-2">
                    <div className="flex items-center justify-between text-xs">
                      <span className="px-2.5 py-1 rounded-full bg-[#f5b041]/15 text-[#f5b041] font-bold border border-[#f5b041]/30">
                        {idea.status}
                      </span>

                      <div className="flex items-center gap-2">
                        {idea.visibility === 'PRIVATE' ? (
                          <span className="flex items-center gap-1 text-[11px] text-amber-400">
                            <Lock className="w-3 h-3" /> Private
                          </span>
                        ) : (
                          <span className="flex items-center gap-1 text-[11px] text-[#8b7cf6]">
                            <Globe className="w-3 h-3" /> Public
                          </span>
                        )}

                        <button
                          type="button"
                          onClick={() => setSelectedHistoryTarget({ id: idea.id, title: idea.title })}
                          title="Version history"
                          className="p-1 text-[#94a3b8] hover:text-[#f8fafc] transition-colors"
                        >
                          <History className="w-3.5 h-3.5" />
                        </button>

                        <button
                          type="button"
                          onClick={() => onDeleteNote(idea.id)}
                          title="Delete note"
                          className="p-1 text-[#94a3b8] hover:text-red-400 transition-colors"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      </div>
                    </div>

                    <h3 className="text-base font-bold font-display text-[#f8fafc]">{idea.title}</h3>

                    <div className="text-xs md:text-sm text-[#cbd5e1] leading-relaxed">
                      <MarkdownRenderer content={idea.content} />
                    </div>
                  </div>

                  {idea.tags && idea.tags.length > 0 && (
                    <div className="flex flex-wrap gap-1.5 pt-2 border-t border-[#262a36]">
                      {idea.tags.map((tag) => (
                        <span
                          key={tag}
                          className="px-2 py-0.5 rounded bg-[#1e2129] text-[10px] text-[#94a3b8] font-mono border border-[#2e3342]"
                        >
                          #{tag}
                        </span>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* 2. THOUGHTS TAB */}
      {activeTab === 'thoughts' && (
        <div className="space-y-4">
          {thoughts.length === 0 ? (
            <div className="text-center py-16 bg-[#161820] rounded-2xl border border-[#2e3342] space-y-3 max-w-md mx-auto p-6">
              <span className="text-4xl">🧠</span>
              <h3 className="text-base font-bold text-[#f8fafc]">Freeform Thoughts & Learning Space</h3>
              <p className="text-xs text-[#94a3b8]">Document questions, hypotheses, engineering insights, and theoretical observations.</p>
              <button
                type="button"
                onClick={() => setShowAddModal(true)}
                className="px-4 py-2 rounded-xl bg-[#f5b041] text-[#0f1015] font-bold text-xs"
              >
                + Add First Thought
              </button>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {thoughts.map((th) => (
                <div
                  key={th.id}
                  className="rounded-2xl p-5 bg-[#161820] border border-[#2e3342] hover:border-[#8b7cf6]/50 transition-all space-y-3"
                >
                  <div className="flex items-center justify-between text-xs">
                    <span className="font-bold text-[#8b7cf6] font-mono text-[11px] uppercase tracking-wider">
                      {th.type} • {th.category}
                    </span>

                    <div className="flex items-center gap-2">
                      <button
                        type="button"
                        onClick={() => setSelectedHistoryTarget({ id: th.id, title: th.title })}
                        className="p-1 text-[#94a3b8] hover:text-[#f8fafc]"
                      >
                        <History className="w-3.5 h-3.5" />
                      </button>
                      <button
                        type="button"
                        onClick={() => onDeleteNote(th.id)}
                        className="p-1 text-[#94a3b8] hover:text-red-400"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    </div>
                  </div>

                  <h3 className="text-base font-bold font-display text-[#f8fafc]">{th.title}</h3>

                  <div className="text-xs md:text-sm text-[#cbd5e1] leading-relaxed">
                    <MarkdownRenderer content={th.content} />
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* 3. DEVELOPMENTS TIMELINE TAB */}
      {activeTab === 'developments' && (
        <div className="space-y-4">
          {developments.length === 0 ? (
            <div className="text-center py-16 bg-[#161820] rounded-2xl border border-[#2e3342] space-y-3 max-w-md mx-auto p-6">
              <span className="text-4xl">🛠</span>
              <h3 className="text-base font-bold text-[#f8fafc]">Development Timeline</h3>
              <p className="text-xs text-[#94a3b8]">Log what changed, what worked, what failed, and what you are building next.</p>
              <button
                type="button"
                onClick={() => setShowAddModal(true)}
                className="px-4 py-2 rounded-xl bg-[#f5b041] text-[#0f1015] font-bold text-xs"
              >
                + Log First Development Milestone
              </button>
            </div>
          ) : (
            <div className="space-y-4 relative before:absolute before:inset-0 before:left-5 before:w-0.5 before:bg-[#2e3342]">
              {developments.map((dev) => (
                <div key={dev.id} className="relative pl-12">
                  <div className="absolute left-3.5 top-5 w-3.5 h-3.5 rounded-full bg-[#f5b041] border-2 border-[#0f1015] shadow" />

                  <div className="rounded-2xl p-5 bg-[#161820] border border-[#2e3342] space-y-3 hover:border-[#f5b041]/40 transition-all">
                    <div className="flex items-center justify-between text-xs">
                      <div className="flex items-center gap-2">
                        <span className="font-bold text-[#f5b041] font-mono">{dev.dateStr}</span>
                        <span className="px-2 py-0.5 rounded bg-[#1e2129] text-[10px] text-[#94a3b8]">
                          {dev.status}
                        </span>
                      </div>

                      <div className="flex items-center gap-2">
                        <button
                          type="button"
                          onClick={() => setSelectedHistoryTarget({ id: dev.id, title: dev.title })}
                          className="p-1 text-[#94a3b8] hover:text-[#f8fafc]"
                        >
                          <History className="w-3.5 h-3.5" />
                        </button>
                        <button
                          type="button"
                          onClick={() => onDeleteDevelopment(dev.id)}
                          className="p-1 text-[#94a3b8] hover:text-red-400"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      </div>
                    </div>

                    <h3 className="text-base font-bold font-display text-[#f8fafc]">{dev.title}</h3>

                    {/* What changed */}
                    <div className="p-3 rounded-xl bg-[#121316] border border-[#262a36] text-xs">
                      <MarkdownRenderer content={dev.whatChanged} />
                    </div>

                    {dev.whatWorked && (
                      <p className="text-xs text-emerald-400">
                        <strong className="font-semibold text-[#f8fafc]">✓ What worked:</strong> {dev.whatWorked}
                      </p>
                    )}

                    {dev.whatFailed && (
                      <p className="text-xs text-amber-400">
                        <strong className="font-semibold text-[#f8fafc]">⚠ What failed:</strong> {dev.whatFailed}
                      </p>
                    )}

                    {dev.nextStep && (
                      <p className="text-xs text-[#8b7cf6] font-medium">
                        <strong className="font-semibold text-[#f8fafc]">→ Next:</strong> {dev.nextStep}
                      </p>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* 4. TO-DO LIST TAB */}
      {activeTab === 'tasks' && (
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <p className="text-xs text-[#94a3b8]">
              Interactive tasks checklist. Click checkbox to toggle status.
            </p>
          </div>

          {tasks.length === 0 ? (
            <div className="text-center py-16 bg-[#161820] rounded-2xl border border-[#2e3342] space-y-3 max-w-md mx-auto p-6">
              <span className="text-4xl">✅</span>
              <h3 className="text-base font-bold text-[#f8fafc]">Task Backlog Empty</h3>
              <p className="text-xs text-[#94a3b8]">Add action items, due dates, and priority levels to organize your work.</p>
              <button
                type="button"
                onClick={() => setShowAddModal(true)}
                className="px-4 py-2 rounded-xl bg-[#f5b041] text-[#0f1015] font-bold text-xs"
              >
                + Add First Task
              </button>
            </div>
          ) : (
            <div className="space-y-2.5">
            {tasks.map((task) => {
              const isDone = task.status === 'COMPLETED';
              const priorityCol =
                task.priority === 'HIGH'
                  ? 'text-red-400 border-red-500/30 bg-red-500/10'
                  : task.priority === 'LOW'
                  ? 'text-emerald-400 border-emerald-500/30 bg-emerald-500/10'
                  : 'text-amber-400 border-amber-500/30 bg-amber-500/10';

              return (
                <div
                  key={task.id}
                  className={`flex items-center justify-between p-4 rounded-xl border transition-all ${
                    isDone
                      ? 'bg-[#121316] border-[#222631] opacity-60'
                      : 'bg-[#161820] border-[#2e3342] hover:border-[#3b4254]'
                  }`}
                >
                  <div className="flex items-center gap-3.5 flex-1 min-w-0">
                    <button
                      type="button"
                      onClick={() => {
                        onToggleTask(task.id);
                        if (!isDone) triggerTaskConfetti();
                      }}
                      className={`w-5 h-5 rounded-md flex items-center justify-center transition-all ${
                        isDone
                          ? 'bg-[#f5b041] text-[#0f1015]'
                          : 'border-2 border-[#475569] hover:border-[#f5b041]'
                      }`}
                    >
                      {isDone && <Check className="w-3.5 h-3.5 stroke-[3]" />}
                    </button>

                    <div className="min-w-0">
                      <p
                        className={`text-sm font-semibold truncate ${
                          isDone ? 'line-through text-[#64748b]' : 'text-[#f8fafc]'
                        }`}
                      >
                        {task.title}
                      </p>
                      {task.description && (
                        <p className="text-xs text-[#94a3b8] line-clamp-1">{task.description}</p>
                      )}
                    </div>
                  </div>

                  <div className="flex items-center gap-3 shrink-0 ml-4">
                    <span className={`px-2 py-0.5 rounded text-[10px] font-bold border ${priorityCol}`}>
                      {task.priority}
                    </span>

                    {task.dueDate && (
                      <span className="text-[11px] text-[#94a3b8] font-mono hidden sm:inline">
                        Due: {task.dueDate}
                      </span>
                    )}

                    <button
                      type="button"
                      onClick={() => onDeleteTask(task.id)}
                      className="p-1 text-[#64748b] hover:text-red-400 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
          )}
        </div>
      )}

      {/* 5. PROJECTS TAB */}
      {activeTab === 'projects' && (
        <div className="space-y-4">
          {projects.length === 0 ? (
            <div className="text-center py-16 bg-[#161820] rounded-2xl border border-[#2e3342] space-y-3 max-w-md mx-auto p-6">
              <span className="text-4xl">🚀</span>
              <h3 className="text-base font-bold text-[#f8fafc]">Project Library Empty</h3>
              <p className="text-xs text-[#94a3b8]">Create your first project blueprint with problem, solution, technologies, and repository links.</p>
              <button
                type="button"
                onClick={() => setShowAddModal(true)}
                className="px-4 py-2 rounded-xl bg-[#f5b041] text-[#0f1015] font-bold text-xs"
              >
                + Create First Project
              </button>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            {projects.map((proj) => (
              <div
                key={proj.id}
                className="rounded-2xl p-6 bg-[#161820] border border-[#2e3342] hover:border-[#f5b041]/50 transition-all flex flex-col justify-between space-y-4"
              >
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <Rocket className="w-5 h-5 text-[#f5b041]" />
                      <h3 className="text-lg font-bold font-display text-[#f8fafc]">{proj.name}</h3>
                    </div>
                    <span className="px-2.5 py-0.5 rounded-full text-xs font-semibold bg-[#38bdf8]/15 text-[#38bdf8] border border-[#38bdf8]/30">
                      {proj.status}
                    </span>
                  </div>

                  {proj.tagline && (
                    <p className="text-xs text-[#8b7cf6] font-semibold">{proj.tagline}</p>
                  )}

                  <p className="text-xs md:text-sm text-[#cbd5e1] line-clamp-3 leading-relaxed">
                    {proj.description}
                  </p>

                  {/* Progress bar */}
                  <div className="space-y-1 pt-1">
                    <div className="flex justify-between text-xs">
                      <span className="text-[#94a3b8]">Progress</span>
                      <span className="font-bold text-[#f5b041] font-mono">{proj.progressPercent}%</span>
                    </div>
                    <div className="w-full h-2 rounded-full bg-[#121316] overflow-hidden">
                      <div
                        className="h-full bg-gradient-to-r from-[#f5b041] to-[#ffcf66] rounded-full"
                        style={{ width: `${proj.progressPercent}%` }}
                      />
                    </div>
                  </div>

                  {/* Tech stack */}
                  <div className="flex flex-wrap gap-1.5 pt-1">
                    {proj.technologies.map((t) => (
                      <span
                        key={t}
                        className="px-2.5 py-0.5 rounded-md bg-[#121316] text-[10px] text-[#cbd5e1] font-mono border border-[#2e3342]"
                      >
                        {t}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Card Actions */}
                <div className="flex items-center justify-between pt-3 border-t border-[#262a36]">
                  <div className="flex items-center gap-2">
                    {proj.repoUrl && (
                      <a
                        href={proj.repoUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="p-1.5 rounded-lg text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#222631] transition-colors"
                      >
                        <Code2 className="w-4 h-4" />
                      </a>
                    )}
                    {proj.demoUrl && (
                      <a
                        href={proj.demoUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="p-1.5 rounded-lg text-[#94a3b8] hover:text-[#f5b041] hover:bg-[#222631] transition-colors"
                      >
                        <ExternalLink className="w-4 h-4" />
                      </a>
                    )}
                    <button
                      type="button"
                      onClick={() => setSelectedHistoryTarget({ id: proj.id, title: proj.name })}
                      className="p-1.5 rounded-lg text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#222631]"
                    >
                      <History className="w-4 h-4" />
                    </button>
                  </div>

                  <button
                    type="button"
                    onClick={() => setSelectedProjectModal(proj)}
                    className="px-3 py-1.5 rounded-lg bg-[#222631] hover:bg-[#f5b041] text-[#f5b041] hover:text-[#0f1015] font-bold text-xs transition-all"
                  >
                    Open Project
                  </button>
                </div>
              </div>
            ))}
          </div>
          )}
        </div>
      )}

      {/* 6. GENERAL MARKDOWN NOTEBOOK TAB */}
      {activeTab === 'notebook' && (
        <div className="space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
            <input
              type="text"
              value={notebookTitle}
              onChange={(e) => setNotebookTitle(e.target.value)}
              placeholder="Notebook Page Title"
              className="text-lg font-bold font-display text-[#f8fafc] bg-transparent border-b border-[#2e3342] pb-1 focus:outline-none focus:border-[#f5b041]"
            />
          </div>

          <MarkdownEditor
            value={notebookContent}
            onChange={handleNotebookChange}
            onSave={handleSaveNotebook}
            autosaveStatus={autosaveStatus}
            minHeight="420px"
          />
        </div>
      )}

      {/* 7. STATS & ANALYTICS TAB */}
      {activeTab === 'stats' && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="p-4 rounded-xl bg-[#161820] border border-[#2e3342]">
              <span className="text-xs text-[#94a3b8]">💡 Total Ideas</span>
              <p className="text-2xl font-bold font-display text-[#f5b041] mt-1">{ideas.length}</p>
            </div>
            <div className="p-4 rounded-xl bg-[#161820] border border-[#2e3342]">
              <span className="text-xs text-[#94a3b8]">🚀 Active Projects</span>
              <p className="text-2xl font-bold font-display text-[#38bdf8] mt-1">{projects.length}</p>
            </div>
            <div className="p-4 rounded-xl bg-[#161820] border border-[#2e3342]">
              <span className="text-xs text-[#94a3b8]">✅ Completed Tasks</span>
              <p className="text-2xl font-bold font-display text-emerald-400 mt-1">
                {tasks.filter((t) => t.status === 'COMPLETED').length} / {tasks.length}
              </p>
            </div>
            <div className="p-4 rounded-xl bg-[#161820] border border-[#2e3342]">
              <span className="text-xs text-[#94a3b8]">🛠 Milestones Logged</span>
              <p className="text-2xl font-bold font-display text-pink-400 mt-1">{developments.length}</p>
            </div>
          </div>

          <div className="p-6 rounded-2xl bg-[#161820] border border-[#2e3342] space-y-3">
            <h3 className="text-sm font-bold font-display text-[#f8fafc]">About this Innovator</h3>
            <p className="text-xs md:text-sm text-[#cbd5e1] leading-relaxed">{user.bio}</p>
            <div className="pt-2">
              <span className="text-xs font-semibold text-[#8b7cf6]">Core Interests: </span>
              <span className="text-xs text-[#94a3b8]">{user.interests}</span>
            </div>
          </div>
        </div>
      )}

      {/* Modals */}
      {showAddModal && (
        <AddEntryModal
          currentUserId={user.id}
          currentUserName={user.name}
          onClose={() => setShowAddModal(false)}
          onSaveNote={onSaveNote}
          onSaveProject={onSaveProject}
          onSaveTask={onSaveTask}
          onSaveDevelopment={onSaveDevelopment}
        />
      )}

      {selectedHistoryTarget && (
        <VersionHistoryModal
          targetTitle={selectedHistoryTarget.title}
          versions={getVersionsForTarget(selectedHistoryTarget.id)}
          onClose={() => setSelectedHistoryTarget(null)}
        />
      )}

      {selectedProjectModal && (
        <ProjectDetailModal
          project={selectedProjectModal}
          onClose={() => setSelectedProjectModal(null)}
        />
      )}
    </div>
  );
};
