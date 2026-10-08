import React, { useState, useEffect } from 'react';
import { MarkdownRenderer } from './MarkdownRenderer';
import { Bold, Italic, Code, Quote, List, CheckSquare, Eye, Edit3, Columns, Save } from 'lucide-react';

interface MarkdownEditorProps {
  value: string;
  onChange: (value: string) => void;
  onSave?: () => void;
  autosaveStatus?: string;
  placeholder?: string;
  minHeight?: string;
}

export const MarkdownEditor: React.FC<MarkdownEditorProps> = ({
  value,
  onChange,
  onSave,
  autosaveStatus = 'Saved ✓',
  placeholder = 'Write in Markdown...',
  minHeight = '320px',
}) => {
  const [mode, setMode] = useState<'edit' | 'preview' | 'split'>('edit');
  const textareaRef = React.useRef<HTMLTextAreaElement>(null);

  const insertSnippet = (prefix: string, suffix: string = '') => {
    if (!textareaRef.current) return;
    const start = textareaRef.current.selectionStart;
    const end = textareaRef.current.selectionEnd;
    const selected = value.substring(start, end);
    const replacement = `${prefix}${selected || 'text'}${suffix}`;
    const newValue = value.substring(0, start) + replacement + value.substring(end);
    onChange(newValue);

    setTimeout(() => {
      if (textareaRef.current) {
        textareaRef.current.focus();
        textareaRef.current.setSelectionRange(start + prefix.length, start + replacement.length - suffix.length);
      }
    }, 10);
  };

  return (
    <div className="flex flex-col rounded-xl border border-[#333846] bg-[#15171e] overflow-hidden shadow-lg">
      {/* Top Toolbar */}
      <div className="flex flex-wrap items-center justify-between px-3 py-2 bg-[#1b1e26] border-b border-[#333846] gap-2">
        {/* Formatting Actions */}
        <div className="flex items-center gap-1">
          <button
            type="button"
            onClick={() => insertSnippet('**', '**')}
            title="Bold"
            className="p-1.5 rounded hover:bg-[#252934] text-[#cbd5e1] hover:text-[#f5b041] transition-colors"
          >
            <Bold className="w-4 h-4" />
          </button>
          <button
            type="button"
            onClick={() => insertSnippet('*', '*')}
            title="Italic"
            className="p-1.5 rounded hover:bg-[#252934] text-[#cbd5e1] hover:text-[#f5b041] transition-colors"
          >
            <Italic className="w-4 h-4" />
          </button>
          <button
            type="button"
            onClick={() => insertSnippet('`', '`')}
            title="Inline Code"
            className="p-1.5 rounded hover:bg-[#252934] text-[#cbd5e1] hover:text-[#f5b041] transition-colors"
          >
            <Code className="w-4 h-4" />
          </button>
          <button
            type="button"
            onClick={() => insertSnippet('> ')}
            title="Quote"
            className="p-1.5 rounded hover:bg-[#252934] text-[#cbd5e1] hover:text-[#f5b041] transition-colors"
          >
            <Quote className="w-4 h-4" />
          </button>
          <button
            type="button"
            onClick={() => insertSnippet('- ')}
            title="List"
            className="p-1.5 rounded hover:bg-[#252934] text-[#cbd5e1] hover:text-[#f5b041] transition-colors"
          >
            <List className="w-4 h-4" />
          </button>
          <button
            type="button"
            onClick={() => insertSnippet('- [ ] ')}
            title="Task list"
            className="p-1.5 rounded hover:bg-[#252934] text-[#cbd5e1] hover:text-[#f5b041] transition-colors"
          >
            <CheckSquare className="w-4 h-4" />
          </button>
          <button
            type="button"
            onClick={() => insertSnippet('\n```python\n', '\n```\n')}
            title="Code Block"
            className="px-2 py-1 rounded hover:bg-[#252934] text-xs font-mono text-[#cbd5e1] hover:text-[#f5b041] transition-colors"
          >
            ```
          </button>
        </div>

        {/* View Mode Toggle & Autosave Indicator */}
        <div className="flex items-center gap-3">
          <span className="text-xs font-medium text-emerald-400 flex items-center gap-1.5">
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
            {autosaveStatus}
          </span>

          <div className="flex items-center bg-[#15171e] rounded-lg p-0.5 border border-[#2e3342]">
            <button
              type="button"
              onClick={() => setMode('edit')}
              className={`flex items-center gap-1 px-2.5 py-1 rounded text-xs font-medium transition-all ${
                mode === 'edit'
                  ? 'bg-[#f5b041] text-[#0f1015] font-bold shadow'
                  : 'text-[#94a3b8] hover:text-[#f8fafc]'
              }`}
            >
              <Edit3 className="w-3.5 h-3.5" />
              Edit
            </button>
            <button
              type="button"
              onClick={() => setMode('preview')}
              className={`flex items-center gap-1 px-2.5 py-1 rounded text-xs font-medium transition-all ${
                mode === 'preview'
                  ? 'bg-[#f5b041] text-[#0f1015] font-bold shadow'
                  : 'text-[#94a3b8] hover:text-[#f8fafc]'
              }`}
            >
              <Eye className="w-3.5 h-3.5" />
              Preview
            </button>
            <button
              type="button"
              onClick={() => setMode('split')}
              className={`hidden md:flex items-center gap-1 px-2.5 py-1 rounded text-xs font-medium transition-all ${
                mode === 'split'
                  ? 'bg-[#f5b041] text-[#0f1015] font-bold shadow'
                  : 'text-[#94a3b8] hover:text-[#f8fafc]'
              }`}
            >
              <Columns className="w-3.5 h-3.5" />
              Split
            </button>
          </div>

          {onSave && (
            <button
              type="button"
              onClick={onSave}
              className="flex items-center gap-1.5 px-3 py-1 bg-[#f5b041] hover:bg-[#ffcf66] text-[#0f1015] font-bold text-xs rounded-lg transition-all"
            >
              <Save className="w-3.5 h-3.5" />
              Save
            </button>
          )}
        </div>
      </div>

      {/* Editor & Preview Area */}
      <div className="relative" style={{ minHeight }}>
        {mode === 'edit' && (
          <textarea
            ref={textareaRef}
            value={value}
            onChange={(e) => onChange(e.target.value)}
            placeholder={placeholder}
            style={{ minHeight }}
            className="w-full h-full p-4 bg-[#121316] text-[#f8fafc] font-mono text-sm leading-relaxed resize-y focus:outline-none focus:ring-1 focus:ring-[#f5b041]/50 placeholder-[#475569]"
          />
        )}

        {mode === 'preview' && (
          <div className="p-4 bg-[#121316] overflow-y-auto" style={{ minHeight }}>
            {value.trim() ? (
              <MarkdownRenderer content={value} />
            ) : (
              <p className="text-[#475569] italic text-sm">Nothing to preview yet. Switch to Edit to write Markdown.</p>
            )}
          </div>
        )}

        {mode === 'split' && (
          <div className="grid grid-cols-2 divide-x divide-[#333846] h-full" style={{ minHeight }}>
            <textarea
              ref={textareaRef}
              value={value}
              onChange={(e) => onChange(e.target.value)}
              placeholder={placeholder}
              className="w-full h-full p-4 bg-[#121316] text-[#f8fafc] font-mono text-sm leading-relaxed resize-none focus:outline-none placeholder-[#475569]"
            />
            <div className="p-4 bg-[#15171e] overflow-y-auto" style={{ maxHeight: '500px' }}>
              {value.trim() ? (
                <MarkdownRenderer content={value} />
              ) : (
                <p className="text-[#475569] italic text-sm">Preview will render live as you type...</p>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
