import React from 'react';
import { Version } from '../types';
import { X, History, GitCommit, UserCheck } from 'lucide-react';

interface VersionHistoryModalProps {
  targetTitle: string;
  versions: Version[];
  onClose: () => void;
}

export const VersionHistoryModal: React.FC<VersionHistoryModalProps> = ({
  targetTitle,
  versions,
  onClose,
}) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-fade-in">
      <div className="relative w-full max-w-lg max-h-[80vh] flex flex-col rounded-2xl bg-[#15171e] border border-[#333846] shadow-2xl overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 bg-[#1b1e26] border-b border-[#333846]">
          <div className="flex items-center gap-2.5">
            <div className="p-2 rounded-lg bg-[#f5b041]/10 text-[#f5b041]">
              <History className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base font-bold font-display text-[#f8fafc]">Version History</h2>
              <p className="text-xs text-[#94a3b8] line-clamp-1">{targetTitle}</p>
            </div>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="p-1.5 rounded-lg text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#2e3342] transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* List of Versions */}
        <div className="flex-1 overflow-y-auto p-6 space-y-3">
          {versions.length === 0 ? (
            <div className="text-center py-8 text-[#64748b] text-sm">
              Initial draft. No previous revisions logged.
            </div>
          ) : (
            versions.map((ver) => (
              <div
                key={ver.id}
                className="p-4 rounded-xl bg-[#1b1e26] border border-[#2e3342] hover:border-[#3b4254] transition-all"
              >
                <div className="flex items-center justify-between text-xs mb-2">
                  <div className="flex items-center gap-1.5">
                    <span className="px-2 py-0.5 rounded-md bg-[#f5b041]/15 text-[#f5b041] font-bold font-mono">
                      v{ver.versionNumber}
                    </span>
                    <span className="font-semibold text-[#cbd5e1] flex items-center gap-1">
                      <UserCheck className="w-3.5 h-3.5 text-[#8b7cf6]" />
                      {ver.editedBy}
                    </span>
                  </div>
                  <span className="text-[#94a3b8] text-[11px]">
                    {new Date(ver.createdAt).toLocaleString(undefined, {
                      month: 'short',
                      day: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit',
                    })}
                  </span>
                </div>
                <p className="text-xs text-[#94a3b8] font-mono leading-relaxed bg-[#121316] p-2.5 rounded-lg border border-[#262a36]">
                  {ver.contentSnippet}
                </p>
              </div>
            ))
          )}
        </div>

        <div className="p-4 bg-[#1b1e26] border-t border-[#333846] flex justify-end">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 rounded-lg bg-[#252934] hover:bg-[#2e3342] text-[#f8fafc] text-xs font-semibold transition-colors"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};
