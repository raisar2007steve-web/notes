import React from 'react';
import { Project } from '../types';
import { MarkdownRenderer } from './MarkdownRenderer';
import { X, ExternalLink, Code2, Rocket, CheckCircle, Target, AlertTriangle, Layers } from 'lucide-react';

interface ProjectDetailModalProps {
  project: Project;
  onClose: () => void;
}

export const ProjectDetailModal: React.FC<ProjectDetailModalProps> = ({ project, onClose }) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/85 backdrop-blur-md animate-fade-in">
      <div className="relative w-full max-w-3xl max-h-[90vh] flex flex-col rounded-2xl bg-[#15171e] border border-[#333846] shadow-2xl overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 bg-[#1b1e26] border-b border-[#333846]">
          <div className="flex items-center gap-3">
            <div className="p-2.5 rounded-xl bg-[#f5b041]/10 text-[#f5b041] border border-[#f5b041]/30">
              <Rocket className="w-6 h-6" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h2 className="text-xl font-bold font-display text-[#f8fafc]">{project.name}</h2>
                <span className="px-2.5 py-0.5 rounded-full text-xs font-semibold bg-[#38bdf8]/15 text-[#38bdf8] border border-[#38bdf8]/30">
                  {project.status}
                </span>
              </div>
              <p className="text-xs text-[#8b7cf6] font-medium">{project.tagline}</p>
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

        {/* Body */}
        <div className="flex-1 overflow-y-auto p-6 space-y-6">
          {/* Progress bar */}
          <div className="p-4 rounded-xl bg-[#121316] border border-[#2e3342]">
            <div className="flex items-center justify-between text-xs font-semibold mb-2">
              <span className="text-[#94a3b8]">Development Maturity</span>
              <span className="text-[#f5b041] font-mono text-sm">{project.progressPercent}%</span>
            </div>
            <div className="w-full h-2.5 rounded-full bg-[#1b1e26] overflow-hidden">
              <div
                className="h-full bg-gradient-to-r from-[#f5b041] to-[#ffcf66] rounded-full transition-all duration-500"
                style={{ width: `${project.progressPercent}%` }}
              />
            </div>
          </div>

          {/* Problem vs Solution */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="p-4 rounded-xl bg-[#1b1e26] border border-[#2e3342]">
              <div className="flex items-center gap-2 text-xs font-bold text-amber-400 mb-2">
                <AlertTriangle className="w-4 h-4" />
                Problem Statement
              </div>
              <p className="text-xs md:text-sm text-[#cbd5e1] leading-relaxed">
                {project.problem || project.description}
              </p>
            </div>

            <div className="p-4 rounded-xl bg-[#1b1e26] border border-[#2e3342]">
              <div className="flex items-center gap-2 text-xs font-bold text-emerald-400 mb-2">
                <CheckCircle className="w-4 h-4" />
                Proposed Solution
              </div>
              <p className="text-xs md:text-sm text-[#cbd5e1] leading-relaxed">
                {project.solution || 'Active engineering in progress with modular service layers.'}
              </p>
            </div>
          </div>

          {/* Goal & Tech Stack */}
          {project.goal && (
            <div className="p-4 rounded-xl bg-[#1b1e26] border border-[#2e3342]">
              <div className="flex items-center gap-2 text-xs font-bold text-[#f5b041] mb-1.5">
                <Target className="w-4 h-4" />
                Primary Goal
              </div>
              <p className="text-xs md:text-sm text-[#cbd5e1] leading-relaxed">{project.goal}</p>
            </div>
          )}

          {/* Tech stack */}
          <div>
            <div className="flex items-center gap-2 text-xs font-bold text-[#94a3b8] mb-2">
              <Layers className="w-4 h-4" />
              Technologies & Frameworks
            </div>
            <div className="flex flex-wrap gap-2">
              {project.technologies.map((tech) => (
                <span
                  key={tech}
                  className="px-3 py-1 rounded-lg bg-[#1b1e26] text-xs font-medium text-[#f8fafc] border border-[#333846]"
                >
                  {tech}
                </span>
              ))}
            </div>
          </div>

          {/* Links */}
          <div className="flex flex-wrap gap-3">
            {project.repoUrl && (
              <a
                href={project.repoUrl}
                target="_blank"
                rel="noreferrer"
                className="flex items-center gap-2 px-4 py-2 rounded-lg bg-[#252934] hover:bg-[#333846] text-[#f8fafc] text-xs font-semibold transition-colors"
              >
                <Code2 className="w-4 h-4" />
                Repository
              </a>
            )}
            {project.demoUrl && (
              <a
                href={project.demoUrl}
                target="_blank"
                rel="noreferrer"
                className="flex items-center gap-2 px-4 py-2 rounded-lg bg-[#f5b041] hover:bg-[#ffcf66] text-[#0f1015] text-xs font-bold transition-colors"
              >
                <ExternalLink className="w-4 h-4" />
                Live Demo
              </a>
            )}
          </div>

          {/* Documentation & Blueprint */}
          {project.markdownDoc && (
            <div className="p-4 rounded-xl bg-[#121316] border border-[#2e3342]">
              <h3 className="text-xs font-bold text-[#8b7cf6] mb-3 uppercase tracking-wider">
                Architecture Blueprint & Notes
              </h3>
              <MarkdownRenderer content={project.markdownDoc} />
            </div>
          )}
        </div>

        <div className="p-4 bg-[#1b1e26] border-t border-[#333846] flex justify-end">
          <button
            type="button"
            onClick={onClose}
            className="px-5 py-2 rounded-lg bg-[#252934] hover:bg-[#2e3342] text-[#f8fafc] text-xs font-semibold transition-colors"
          >
            Close Overview
          </button>
        </div>
      </div>
    </div>
  );
};
