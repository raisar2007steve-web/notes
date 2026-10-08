import React from 'react';
import { Copy, Check } from 'lucide-react';

interface MarkdownRendererProps {
  content: string;
  className?: string;
}

export const MarkdownRenderer: React.FC<MarkdownRendererProps> = ({ content, className = '' }) => {
  const [copiedIndex, setCopiedIndex] = React.useState<number | null>(null);

  const lines = content.split('\n');
  const elements: React.ReactNode[] = [];

  let inCodeBlock = false;
  let codeBlockLines: string[] = [];
  let codeBlockLang = '';
  let codeBlockIdx = 0;

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    const trimmed = line.trim();

    if (trimmed.startsWith('```')) {
      if (inCodeBlock) {
        // End code block
        const codeText = codeBlockLines.join('\n');
        const currentIdx = codeBlockIdx++;
        elements.push(
          <div key={`code-${i}`} className="my-3 rounded-lg overflow-hidden border border-[#2e3342] bg-[#0c0d12]">
            <div className="flex items-center justify-between px-3 py-1.5 bg-[#15171e] border-b border-[#2e3342] text-xs text-[#94a3b8] font-mono">
              <span>{codeBlockLang || 'code'}</span>
              <button
                type="button"
                onClick={() => {
                  navigator.clipboard.writeText(codeText);
                  setCopiedIndex(currentIdx);
                  setTimeout(() => setCopiedIndex(null), 2000);
                }}
                className="flex items-center gap-1 hover:text-[#f5b041] transition-colors"
              >
                {copiedIndex === currentIdx ? (
                  <>
                    <Check className="w-3.5 h-3.5 text-emerald-400" />
                    <span className="text-emerald-400">Copied</span>
                  </>
                ) : (
                  <>
                    <Copy className="w-3.5 h-3.5" />
                    <span>Copy</span>
                  </>
                )}
              </button>
            </div>
            <pre className="p-3 text-xs md:text-sm font-mono text-[#f5b041] overflow-x-auto leading-relaxed">
              <code>{codeText}</code>
            </pre>
          </div>
        );
        codeBlockLines = [];
        codeBlockLang = '';
        inCodeBlock = false;
      } else {
        inCodeBlock = true;
        codeBlockLang = trimmed.substring(3).trim();
        codeBlockLines = [];
      }
      continue;
    }

    if (inCodeBlock) {
      codeBlockLines.push(line);
      continue;
    }

    if (trimmed.startsWith('# ')) {
      elements.push(
        <h1 key={i} className="text-xl md:text-2xl font-bold font-display text-[#f5b041] mt-4 mb-2">
          {trimmed.substring(2)}
        </h1>
      );
    } else if (trimmed.startsWith('## ')) {
      elements.push(
        <h2 key={i} className="text-lg md:text-xl font-bold font-display text-[#8b7cf6] mt-3 mb-1.5">
          {trimmed.substring(3)}
        </h2>
      );
    } else if (trimmed.startsWith('### ')) {
      elements.push(
        <h3 key={i} className="text-base font-semibold text-[#f8fafc] mt-2.5 mb-1">
          {trimmed.substring(4)}
        </h3>
      );
    } else if (trimmed.startsWith('> ')) {
      elements.push(
        <blockquote key={i} className="my-2 pl-3 py-1 border-l-2 border-[#f5b041] bg-[#1b1e26]/50 rounded-r text-sm text-[#cbd5e1] italic">
          {renderInline(trimmed.substring(2))}
        </blockquote>
      );
    } else if (trimmed.startsWith('- [ ] ') || trimmed.startsWith('* [ ] ')) {
      elements.push(
        <div key={i} className="flex items-center gap-2 my-1 text-sm text-[#cbd5e1]">
          <span className="w-4 h-4 rounded border border-[#475569] inline-block shrink-0 bg-[#1e2129]" />
          <span>{renderInline(trimmed.substring(6))}</span>
        </div>
      );
    } else if (trimmed.startsWith('- [x] ') || trimmed.startsWith('* [x] ') || trimmed.startsWith('- [X] ')) {
      elements.push(
        <div key={i} className="flex items-center gap-2 my-1 text-sm text-[#94a3b8] line-through">
          <span className="w-4 h-4 rounded border border-[#f5b041] bg-[#f5b041] text-[#0f1015] inline-flex items-center justify-center shrink-0 text-xs font-bold">✓</span>
          <span>{renderInline(trimmed.substring(6))}</span>
        </div>
      );
    } else if (trimmed.startsWith('- ') || trimmed.startsWith('* ')) {
      elements.push(
        <div key={i} className="flex items-start gap-2 my-1 text-sm text-[#cbd5e1] pl-2">
          <span className="text-[#f5b041] font-bold text-base leading-none select-none">•</span>
          <span className="leading-relaxed">{renderInline(trimmed.substring(2))}</span>
        </div>
      );
    } else if (trimmed.startsWith('✓ ')) {
      elements.push(
        <div key={i} className="flex items-start gap-2 my-1 text-sm text-emerald-400 pl-1">
          <span className="font-bold">✓</span>
          <span className="leading-relaxed text-[#cbd5e1]">{renderInline(trimmed.substring(2))}</span>
        </div>
      );
    } else if (trimmed.startsWith('⚠ ')) {
      elements.push(
        <div key={i} className="flex items-start gap-2 my-1 text-sm text-amber-400 pl-1">
          <span className="font-bold">⚠</span>
          <span className="leading-relaxed text-[#cbd5e1]">{renderInline(trimmed.substring(2))}</span>
        </div>
      );
    } else if (trimmed.startsWith('→ ')) {
      elements.push(
        <div key={i} className="flex items-start gap-2 my-1 text-sm text-[#8b7cf6] pl-1">
          <span className="font-bold">→</span>
          <span className="leading-relaxed text-[#f8fafc] font-medium">{renderInline(trimmed.substring(2))}</span>
        </div>
      );
    } else if (trimmed === '') {
      elements.push(<div key={i} className="h-2" />);
    } else {
      elements.push(
        <p key={i} className="text-sm text-[#cbd5e1] my-1 leading-relaxed">
          {renderInline(line)}
        </p>
      );
    }
  }

  return <div className={`space-y-0.5 ${className}`}>{elements}</div>;
};

function renderInline(text: string): React.ReactNode {
  const parts: React.ReactNode[] = [];
  let buffer = '';
  let i = 0;

  while (i < text.length) {
    if (text.startsWith('**', i)) {
      if (buffer) {
        parts.push(buffer);
        buffer = '';
      }
      const end = text.indexOf('**', i + 2);
      if (end !== -1) {
        parts.push(
          <strong key={`b-${i}`} className="font-bold text-[#f8fafc]">
            {text.substring(i + 2, end)}
          </strong>
        );
        i = end + 2;
        continue;
      }
    } else if (text[i] === '`') {
      if (buffer) {
        parts.push(buffer);
        buffer = '';
      }
      const end = text.indexOf('`', i + 1);
      if (end !== -1) {
        parts.push(
          <code key={`c-${i}`} className="px-1.5 py-0.5 rounded bg-[#1b1e26] text-[#f5b041] font-mono text-xs border border-[#2e3342]">
            {text.substring(i + 1, end)}
          </code>
        );
        i = end + 1;
        continue;
      }
    } else if (text[i] === '*' && i + 1 < text.length && text[i + 1] !== '*') {
      if (buffer) {
        parts.push(buffer);
        buffer = '';
      }
      const end = text.indexOf('*', i + 1);
      if (end !== -1) {
        parts.push(
          <em key={`i-${i}`} className="italic text-[#94a3b8]">
            {text.substring(i + 1, end)}
          </em>
        );
        i = end + 1;
        continue;
      }
    }

    buffer += text[i];
    i++;
  }

  if (buffer) {
    parts.push(buffer);
  }

  return parts;
}
