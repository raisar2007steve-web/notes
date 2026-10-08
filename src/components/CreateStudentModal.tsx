import React, { useState } from 'react';
import { X, UserPlus, Sparkles, BookOpen } from 'lucide-react';

interface CreateStudentModalProps {
  onClose: () => void;
  onCreate: (name: string, field: string, quote?: string) => void;
}

export const CreateStudentModal: React.FC<CreateStudentModalProps> = ({ onClose, onCreate }) => {
  const [name, setName] = useState('');
  const [field, setField] = useState('');
  const [quote, setQuote] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (name.trim()) {
      onCreate(name.trim(), field.trim(), quote.trim());
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-fade-in">
      <div className="relative w-full max-w-md rounded-2xl bg-[#15171e] border border-[#333846] shadow-2xl overflow-hidden p-6 space-y-5">
        <div className="flex items-center justify-between border-b border-[#2e3342] pb-3">
          <div className="flex items-center gap-2.5">
            <div className="p-2 rounded-xl bg-[#f5b041]/10 text-[#f5b041]">
              <BookOpen className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-bold font-display text-[#f8fafc]">Create Student Shelf</h2>
              <p className="text-xs text-[#94a3b8]">Open a new personal notebook on this device</p>
            </div>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="p-1.5 rounded-lg text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#2e3342]"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Student Full Name *</label>
            <input
              type="text"
              required
              autoFocus
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. Elena Rostova"
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Academic Field / Department</label>
            <input
              type="text"
              value={field}
              onChange={(e) => setField(e.target.value)}
              placeholder="e.g. B.Tech Computer Engineering"
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#cbd5e1] mb-1.5">Personal Motto / Quote</label>
            <input
              type="text"
              value={quote}
              onChange={(e) => setQuote(e.target.value)}
              placeholder="e.g. Building systems that learn and scale."
              className="w-full px-3.5 py-2.5 rounded-xl bg-[#121316] border border-[#333846] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041]"
            />
          </div>

          <div className="flex items-center justify-end gap-3 pt-3 border-t border-[#2e3342]">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-xl text-xs font-semibold text-[#cbd5e1] hover:bg-[#252934]"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={!name.trim()}
              className="px-5 py-2.5 rounded-xl bg-[#f5b041] hover:bg-[#ffcf66] disabled:opacity-50 text-[#0f1015] font-extrabold text-xs transition-all shadow-md"
            >
              Open Shelf
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
