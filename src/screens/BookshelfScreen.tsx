import React, { useState } from 'react';
import { User } from '../types';
import { StudentCard } from '../components/StudentCard';
import { storage } from '../services/storage';
import { Search, BookOpen, Plus, UserPlus } from 'lucide-react';

interface BookshelfScreenProps {
  allUsers: User[];
  onOpenStudentBook: (user: User) => void;
  onAddNewStudent?: () => void;
}

const FILTERS = [
  'All',
  'Ideas',
  'Projects',
  'Development',
  'Thoughts',
  'To-Do',
  'Learning',
  'Research',
];

export const BookshelfScreen: React.FC<BookshelfScreenProps> = ({
  allUsers,
  onOpenStudentBook,
  onAddNewStudent,
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [activeFilter, setActiveFilter] = useState('All');

  const filteredUsers = allUsers.filter((user) => {
    const query = searchQuery.toLowerCase();
    const matchesSearch =
      user.name.toLowerCase().includes(query) ||
      user.field.toLowerCase().includes(query) ||
      user.interests.toLowerCase().includes(query) ||
      user.bio.toLowerCase().includes(query);
    return matchesSearch;
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-8 space-y-6 animate-fade-in">
      {/* Title & Subtitle */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-2 border-b border-[#2e3342]/60">
        <div className="space-y-1">
          <h1 className="text-2xl md:text-3xl font-extrabold font-display text-[#f8fafc] flex items-center gap-2">
            <span>📚</span> Innovara Bookshelf
          </h1>
          <p className="text-xs md:text-sm text-[#94a3b8]">
            Explore live shelves hosted on this device. Open any student's book to read their ideas and builds.
          </p>
        </div>

        {onAddNewStudent && (
          <button
            type="button"
            onClick={onAddNewStudent}
            className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-[#222631] hover:bg-[#f5b041] border border-[#f5b041]/50 text-[#f5b041] hover:text-[#0f1015] font-bold text-xs md:text-sm transition-all shrink-0"
          >
            <UserPlus className="w-4 h-4" />
            <span>+ Open New Student Shelf</span>
          </button>
        )}
      </div>

      {/* Search & Filter Bar */}
      <div className="space-y-3">
        <div className="relative">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-[#f5b041]" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search student books by name, discipline, or research interest..."
            className="w-full pl-11 pr-4 py-3 rounded-xl bg-[#161820] border border-[#2e3342] text-[#f8fafc] text-sm focus:outline-none focus:border-[#f5b041] transition-colors"
          />
        </div>

        {/* Filter Pills */}
        <div className="flex items-center gap-1.5 overflow-x-auto pb-1 scrollbar-none">
          {FILTERS.map((filter) => (
            <button
              key={filter}
              type="button"
              onClick={() => setActiveFilter(filter)}
              className={`px-3 py-1.5 rounded-lg text-xs font-semibold whitespace-nowrap transition-all ${
                activeFilter === filter
                  ? 'bg-[#f5b041] text-[#0f1015] shadow-sm font-bold'
                  : 'bg-[#161820] text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#222631] border border-[#2e3342]'
              }`}
            >
              {filter}
            </button>
          ))}
        </div>
      </div>

      {/* Grid of Student Books */}
      {filteredUsers.length === 0 ? (
        <div className="text-center py-16 bg-[#161820] rounded-2xl border border-[#2e3342] space-y-4 max-w-md mx-auto p-6">
          <BookOpen className="w-12 h-12 text-[#f5b041]/60 mx-auto" />
          <div className="space-y-1">
            <h3 className="text-base font-bold text-[#f8fafc]">
              {allUsers.length === 0 ? 'Your Bookshelf is Ready' : 'No matching student shelves'}
            </h3>
            <p className="text-xs text-[#94a3b8] leading-relaxed">
              {allUsers.length === 0
                ? 'Create the first student shelf to start documenting ideas, projects, and innovation logs.'
                : 'Try adjusting your search keywords.'}
            </p>
          </div>
          {onAddNewStudent && (
            <button
              type="button"
              onClick={onAddNewStudent}
              className="px-5 py-2.5 rounded-xl bg-[#f5b041] hover:bg-[#ffcf66] text-[#0f1015] font-extrabold text-xs transition-all shadow-md"
            >
              + Create Student Shelf
            </button>
          )}
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredUsers.map((user) => {
            const stats = storage.getUserStats(user.id);

            return (
              <StudentCard
                key={user.id}
                user={user}
                ideasCount={stats.ideasCount}
                notesCount={stats.notesCount}
                projectsCount={stats.projectsCount}
                tasksCount={stats.tasksCount}
                onOpenBook={() => onOpenStudentBook(user)}
              />
            );
          })}
        </div>
      )}
    </div>
  );
};
