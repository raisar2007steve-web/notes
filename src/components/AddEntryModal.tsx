import React, { useState } from 'react';
import { Note, Project, Task, Development, NoteType, TaskPriority } from '../types';
import { X, Lightbulb, Brain, Wrench, CheckSquare, Rocket, BookOpen, Microscope, FileText, Lock, Globe } from 'lucide-react';

interface AddEntryModalProps {
  currentUserId: string;
  currentUserName: string;
  onClose: () => void;
  onSaveNote: (note: Note) => void;
  onSaveProject: (project: Project) => void;
  onSaveTask: (task: Task) => void;
  onSaveDevelopment: (dev: Development) => void;
}

type EntryCategory = 'IDEA' | 'THOUGHT' | 'DEVELOPMENT' | 'TASK' | 'PROJECT' | 'RESEARCH' | 'LEARNING' | 'NOTE';

const ENTRY_TYPES = [
  { id: 'IDEA' as EntryCategory, label: 'Idea', icon: '💡', desc: 'Innovation concept, startup idea, or problem statement' },
  { id: 'THOUGHT' as EntryCategory, label: 'Thought', icon: '🧠', desc: 'Philosophical observations, reflections, theories' },
  { id: 'DEVELOPMENT' as EntryCategory, label: 'Development', icon: '🛠', desc: 'Chronological milestone, what worked & failed' },
  { id: 'TASK' as EntryCategory, label: 'Task', icon: '✅', desc: 'Action item with priority and due date' },
  { id: 'PROJECT' as EntryCategory, label: 'Project', icon: '🚀', desc: 'Full system, software repository, or hardware pod' },
  { id: 'RESEARCH' as EntryCategory, label: 'Research', icon: '🔬', desc: 'Academic investigation, benchmark results' },
  { id: 'LEARNING' as EntryCategory, label: 'Learning', icon: '📚', desc: 'Framework insight or engineering takeaway' },
  { id: 'NOTE' as EntryCategory, label: 'General Note', icon: '📝', desc: 'Freeform markdown documentation' },
];

export const AddEntryModal: React.FC<AddEntryModalProps> = ({
  currentUserId,
  currentUserName,
  onClose,
  onSaveNote,
  onSaveProject,
  onSaveTask,
  onSaveDevelopment,
}) => {
  const [selectedType, setSelectedType] = useState<EntryCategory | null>(null);

  // Common Note / Entry Fields
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [category, setCategory] = useState('Innovation');
  const [tags, setTags] = useState('');
  const [isPrivate, setIsPrivate] = useState(false);
  const [ideaStatus, setIdeaStatus] = useState('💭 Idea');

  // Task Fields
  const [taskPriority, setTaskPriority] = useState<TaskPriority>('MEDIUM');
  const [taskDueDate, setTaskDueDate] = useState('Oct 15');

  // Development Fields
  const [devDate, setDevDate] = useState('October 8, 2026');
  const [devWhatChanged, setDevWhatChanged] = useState('✓ \n✓ \n⚠ \n→ Next: ');
  const [devWhatWorked, setDevWhatWorked] = useState('');
  const [devWhatFailed, setDevWhatFailed] = useState('');
  const [devNextStep, setDevNextStep] = useState('');

  // Project Fields
  const [projTagline, setProjTagline] = useState('');
  const [projGoal, setProjGoal] = useState('');
  const [projProblem, setProjProblem] = useState('');
  const [projSolution, setProjSolution] = useState('');
  const [projTechnologies, setProjTechnologies] = useState('Python, Rust, React');
  const [projProgress, setProjProgress] = useState(25);
  const [projRepoUrl, setProjRepoUrl] = useState('');
  const [projDemoUrl, setProjDemoUrl] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedType) return;

    if (selectedType === 'TASK') {
      if (!title.trim()) return;
      onSaveTask({
        id: 'task_' + Math.random().toString(36).substring(2, 9),
        userId: currentUserId,
        title: title.trim(),
        description: content.trim(),
        priority: taskPriority,
        status: 'TODO',
        dueDate: taskDueDate.trim(),
        category: category.trim() || 'General',
        createdAt: Date.now(),
        updatedAt: Date.now(),
      });
      onClose();
    } else if (selectedType === 'DEVELOPMENT') {
      if (!title.trim()) return;
      onSaveDevelopment({
        id: 'dev_' + Math.random().toString(36).substring(2, 9),
        userId: currentUserId,
        title: title.trim(),
        dateStr: devDate.trim(),
        whatChanged: devWhatChanged.trim(),
        whatWorked: devWhatWorked.trim(),
        whatFailed: devWhatFailed.trim(),
        nextStep: devNextStep.trim(),
        status: 'In Progress',
        createdAt: Date.now(),
      });
      onClose();
    } else if (selectedType === 'PROJECT') {
      if (!title.trim()) return;
      onSaveProject({
        id: 'proj_' + Math.random().toString(36).substring(2, 9),
        userId: currentUserId,
        name: title.trim(),
        tagline: projTagline.trim(),
        description: content.trim(),
        goal: projGoal.trim(),
        problem: projProblem.trim(),
        solution: projSolution.trim(),
        technologies: projTechnologies.split(',').map((t) => t.trim()).filter(Boolean),
        progressPercent: projProgress,
        status: 'Building',
        repoUrl: projRepoUrl.trim(),
        demoUrl: projDemoUrl.trim(),
        markdownDoc: `# ${title}\n${content}`,
        createdAt: Date.now(),
        updatedAt: Date.now(),
      });
      onClose();
    } else {
      if (!title.trim() && !content.trim()) return;
      const finalTitle = title.trim() || `Untitled ${selectedType}`;
      const noteType: NoteType =
        selectedType === 'NOTE' ? 'GENERAL' : (selectedType as NoteType);

      onSaveNote({
        id: 'note_' + Math.random().toString(36).substring(2, 9),
        userId: currentUserId,
        type: noteType,
        title: finalTitle,
        content: content.trim(),
        category: category.trim() || 'General',
        status: ideaStatus,
        tags: tags.split(',').map((t) => t.trim()).filter(Boolean),
        visibility: isPrivate ? 'PRIVATE' : 'PUBLIC',
        createdAt: Date.now(),
        updatedAt: Date.now(),
      });
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-fade-in">
      <div className="relative w-full max-w-2xl max-h-[90vh] flex flex-col rounded-2xl bg-[#15171e] border border-[#333846] shadow-2xl overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 bg-[#1b1e26] border-b border-[#333846]">
          <div>
            <h2 className="text-lg md:text-xl font-bold font-display text-[#f5b041]">
              {selectedType ? `Add ${selectedType}` : 'What do you want to add?'}
            </h2>
            <p className="text-xs text-[#94a3b8]">Documenting to {currentUserName}’s innovation space</p>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="p-1.5 rounded-lg text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#2e3342] transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content Body */}
        <div className="flex-1 overflow-y-auto p-6">
          {!selectedType ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {ENTRY_TYPES.map((type) => (
                <button
                  key={type.id}
                  type="button"
                  onClick={() => setSelectedType(type.id)}
                  className="flex items-start gap-3.5 p-4 rounded-xl bg-[#1b1e26] hover:bg-[#252934] border border-[#2e3342] hover:border-[#f5b041] transition-all text-left group"
                >
                  <span className="text-2xl p-2 rounded-lg bg-[#121316] group-hover:scale-110 transition-transform">
                    {type.icon}
                  </span>
                  <div>
                    <h3 className="font-bold text-sm text-[#f8fafc] group-hover:text-[#f5b041] transition-colors">
                      {type.label}
                    </h3>
                    <p className="text-xs text-[#94a3b8] mt-1 leading-snug">{type.desc}</p>
                  </div>
                </button>
              ))}
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-4">
              {/* Specialized Form Elements */}
              {selectedType === 'TASK' ? (
                <>
                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Task Title *</label>
                    <input
                      type="text"
                      required
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                      placeholder="e.g. Implement Raft leader heartbeat timer"
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Details & Subtasks</label>
                    <textarea
                      value={content}
                      onChange={(e) => setContent(e.target.value)}
                      rows={2}
                      placeholder="Notes on requirements, benchmark criteria..."
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Priority</label>
                      <select
                        value={taskPriority}
                        onChange={(e) => setTaskPriority(e.target.value as TaskPriority)}
                        className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                      >
                        <option value="HIGH">🔴 High Priority</option>
                        <option value="MEDIUM">🟡 Medium Priority</option>
                        <option value="LOW">🟢 Low Priority</option>
                      </select>
                    </div>

                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Due Date</label>
                      <input
                        type="text"
                        value={taskDueDate}
                        onChange={(e) => setTaskDueDate(e.target.value)}
                        placeholder="e.g. Oct 18"
                        className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                      />
                    </div>
                  </div>
                </>
              ) : selectedType === 'DEVELOPMENT' ? (
                <>
                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Development Milestone / Feature Title *</label>
                    <input
                      type="text"
                      required
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                      placeholder="e.g. KATE AI Memory Integration"
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Date</label>
                    <input
                      type="text"
                      value={devDate}
                      onChange={(e) => setDevDate(e.target.value)}
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">What Changed? (Supports ✓, ⚠, → bullets) *</label>
                    <textarea
                      required
                      value={devWhatChanged}
                      onChange={(e) => setDevWhatChanged(e.target.value)}
                      rows={4}
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm font-mono focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="block text-xs font-semibold text-emerald-400 mb-1.5">✓ What worked?</label>
                      <input
                        type="text"
                        value={devWhatWorked}
                        onChange={(e) => setDevWhatWorked(e.target.value)}
                        placeholder="e.g. Recalls in <18ms"
                        className="w-full px-3.5 py-2 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-xs focus:outline-none focus:border-emerald-400"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-semibold text-amber-400 mb-1.5">⚠ What failed / blocked?</label>
                      <input
                        type="text"
                        value={devWhatFailed}
                        onChange={(e) => setDevWhatFailed(e.target.value)}
                        placeholder="e.g. Async lock contention"
                        className="w-full px-3.5 py-2 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-xs focus:outline-none focus:border-amber-400"
                      />
                    </div>
                  </div>

                  <div>
                    <label className="block text-xs font-semibold text-[#8b7cf6] mb-1.5">→ Next Step</label>
                    <input
                      type="text"
                      value={devNextStep}
                      onChange={(e) => setDevNextStep(e.target.value)}
                      placeholder="e.g. Implement multi-agent dispatcher"
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#8b7cf6]"
                    />
                  </div>
                </>
              ) : selectedType === 'PROJECT' ? (
                <>
                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Project Name *</label>
                    <input
                      type="text"
                      required
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                      placeholder="e.g. KATE AI"
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Tagline / Short Hook</label>
                    <input
                      type="text"
                      value={projTagline}
                      onChange={(e) => setProjTagline(e.target.value)}
                      placeholder="e.g. Personal Autonomous Cognitive OS"
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Overview & Goal</label>
                    <textarea
                      value={content}
                      onChange={(e) => setContent(e.target.value)}
                      rows={2}
                      placeholder="Describe what the system accomplishes..."
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Problem</label>
                      <input
                        type="text"
                        value={projProblem}
                        onChange={(e) => setProjProblem(e.target.value)}
                        placeholder="Why is this needed?"
                        className="w-full px-3.5 py-2 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-xs focus:outline-none"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Solution</label>
                      <input
                        type="text"
                        value={projSolution}
                        onChange={(e) => setProjSolution(e.target.value)}
                        placeholder="How it solves it?"
                        className="w-full px-3.5 py-2 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-xs focus:outline-none"
                      />
                    </div>
                  </div>

                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Technologies (comma separated)</label>
                    <input
                      type="text"
                      value={projTechnologies}
                      onChange={(e) => setProjTechnologies(e.target.value)}
                      placeholder="e.g. Python, Rust, Qt, Chroma"
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none"
                    />
                  </div>

                  <div>
                    <div className="flex justify-between text-xs mb-1">
                      <span className="font-semibold text-[#cbd5e1]">Progress Percentage</span>
                      <span className="font-bold text-[#f5b041]">{projProgress}%</span>
                    </div>
                    <input
                      type="range"
                      min={0}
                      max={100}
                      value={projProgress}
                      onChange={(e) => setProjProgress(Number(e.target.value))}
                      className="w-full accent-[#f5b041]"
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Repository URL</label>
                      <input
                        type="url"
                        value={projRepoUrl}
                        onChange={(e) => setProjRepoUrl(e.target.value)}
                        placeholder="https://github.com/..."
                        className="w-full px-3.5 py-2 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-xs focus:outline-none"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Demo URL</label>
                      <input
                        type="url"
                        value={projDemoUrl}
                        onChange={(e) => setProjDemoUrl(e.target.value)}
                        placeholder="https://demo..."
                        className="w-full px-3.5 py-2 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-xs focus:outline-none"
                      />
                    </div>
                  </div>
                </>
              ) : (
                /* Note / Idea / Thought / Learning / Research Form */
                <>
                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Title *</label>
                    <input
                      type="text"
                      required
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                      placeholder={`e.g. My new ${selectedType.toLowerCase()}`}
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Content (Markdown supported) *</label>
                    <textarea
                      required
                      value={content}
                      onChange={(e) => setContent(e.target.value)}
                      rows={5}
                      placeholder="# Heading&#10;Write detailed notes, formulas, or bullet points..."
                      className="w-full px-3.5 py-2.5 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm font-mono focus:outline-none focus:border-[#f5b041]"
                    />
                  </div>

                  {selectedType === 'IDEA' && (
                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Status</label>
                      <div className="flex flex-wrap gap-2">
                        {['💭 Idea', '🔬 Researching', '🛠 Building', '🧪 Testing', '🚀 Deployed', '✅ Completed'].map((s) => (
                          <button
                            key={s}
                            type="button"
                            onClick={() => setIdeaStatus(s)}
                            className={`px-3 py-1 rounded-full text-xs font-medium transition-all ${
                              ideaStatus === s
                                ? 'bg-[#f5b041] text-[#0f1015] font-bold'
                                : 'bg-[#1b1e26] text-[#cbd5e1] hover:bg-[#252934]'
                            }`}
                          >
                            {s}
                          </button>
                        ))}
                      </div>
                    </div>
                  )}

                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Category</label>
                      <input
                        type="text"
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        placeholder="e.g. AI, Edge, Systems"
                        className="w-full px-3.5 py-2 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-xs focus:outline-none"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Tags (comma separated)</label>
                      <input
                        type="text"
                        value={tags}
                        onChange={(e) => setTags(e.target.value)}
                        placeholder="e.g. Algorithms, LoRa, Quantization"
                        className="w-full px-3.5 py-2 rounded-lg bg-[#121316] border border-[#333846] text-[#f8fafc] text-xs focus:outline-none"
                      />
                    </div>
                  </div>

                  {/* Public / Private toggle */}
                  <div
                    onClick={() => setIsPrivate(!isPrivate)}
                    className="flex items-center justify-between p-3 rounded-xl bg-[#1b1e26] border border-[#2e3342] cursor-pointer hover:border-[#3f4658] transition-colors"
                  >
                    <div className="flex items-center gap-3">
                      {isPrivate ? (
                        <div className="p-2 rounded-lg bg-amber-500/10 text-amber-400">
                          <Lock className="w-4 h-4" />
                        </div>
                      ) : (
                        <div className="p-2 rounded-lg bg-indigo-500/10 text-[#8b7cf6]">
                          <Globe className="w-4 h-4" />
                        </div>
                      )}
                      <div>
                        <span className="text-xs font-bold text-[#f8fafc]">
                          {isPrivate ? '🔒 Private Entry' : '🌐 Public Knowledge'}
                        </span>
                        <p className="text-[11px] text-[#94a3b8]">
                          {isPrivate ? 'Only you can view this page' : 'Openly visible to all Innovara students'}
                        </p>
                      </div>
                    </div>
                    <span className="text-xs font-semibold text-[#f5b041]">
                      {isPrivate ? 'Private' : 'Public'}
                    </span>
                  </div>
                </>
              )}

              {/* Form Footer Buttons */}
              <div className="flex items-center justify-between pt-4 border-t border-[#333846]">
                <button
                  type="button"
                  onClick={() => setSelectedType(null)}
                  className="px-4 py-2 rounded-lg text-xs font-semibold text-[#cbd5e1] hover:bg-[#252934] transition-colors"
                >
                  Back to Type Selection
                </button>
                <button
                  type="submit"
                  className="px-5 py-2.5 rounded-lg bg-[#f5b041] hover:bg-[#ffcf66] text-[#0f1015] font-bold text-xs transition-all shadow-md"
                >
                  Save to Shelf
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};
