import React, { useState } from 'react';
import { User } from '../types';
import { BookOpen, Search, Home, Flame, Sparkles, ChevronDown, UserPlus, LogOut } from 'lucide-react';

interface HeaderProps {
  currentUser: User | null;
  allUsers: User[];
  currentTab: string;
  onSelectTab: (tab: string) => void;
  onSwitchUser: (user: User) => void;
  onOpenSearch: () => void;
  onAddNewStudent?: () => void;
  onLogout?: () => void;
}

export const Header: React.FC<HeaderProps> = ({
  currentUser,
  allUsers,
  currentTab,
  onSelectTab,
  onSwitchUser,
  onOpenSearch,
  onAddNewStudent,
  onLogout,
}) => {
  const [showSwitchDropdown, setShowSwitchDropdown] = useState(false);

  return (
    <header className="sticky top-0 z-40 w-full glass-panel border-b border-[#2e3342]/80 bg-[#0f1015]/90 backdrop-blur-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between gap-4">
        {/* Brand Logo */}
        <div
          onClick={() => onSelectTab('dashboard')}
          className="flex items-center gap-2.5 cursor-pointer group"
        >
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-[#f5b041] to-[#d49228] flex items-center justify-center text-[#0f1015] font-black text-base shadow-md group-hover:scale-105 transition-transform">
            IN
          </div>
          <div>
            <span className="font-extrabold font-display text-base md:text-lg tracking-wider text-[#f8fafc] group-hover:text-[#f5b041] transition-colors">
              INNOVARA
            </span>
            <span className="text-xs font-mono text-[#8b7cf6] ml-1.5 font-bold">NOTES</span>
          </div>
        </div>

        {/* Desktop Navigation */}
        <nav className="hidden md:flex items-center gap-1 bg-[#15171e]/90 p-1 rounded-xl border border-[#2e3342]">
          <button
            type="button"
            onClick={() => onSelectTab('dashboard')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
              currentTab === 'dashboard'
                ? 'bg-[#f5b041] text-[#0f1015] shadow-sm font-bold'
                : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1f232d]'
            }`}
          >
            <Home className="w-3.5 h-3.5" />
            Home
          </button>

          <button
            type="button"
            onClick={() => onSelectTab('bookshelf')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
              currentTab === 'bookshelf'
                ? 'bg-[#f5b041] text-[#0f1015] shadow-sm font-bold'
                : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1f232d]'
            }`}
          >
            <BookOpen className="w-3.5 h-3.5" />
            Bookshelf
          </button>

          <button
            type="button"
            onClick={() => onSelectTab('my_book')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
              currentTab === 'my_book'
                ? 'bg-[#f5b041] text-[#0f1015] shadow-sm font-bold'
                : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1f232d]'
            }`}
          >
            <Sparkles className="w-3.5 h-3.5" />
            My Shelf
          </button>

          <button
            type="button"
            onClick={() => onSelectTab('feed')}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
              currentTab === 'feed'
                ? 'bg-[#f5b041] text-[#0f1015] shadow-sm font-bold'
                : 'text-[#94a3b8] hover:text-[#f8fafc] hover:bg-[#1f232d]'
            }`}
          >
            <Flame className="w-3.5 h-3.5" />
            Activity
          </button>
        </nav>

        {/* Right Section: Search & Identity Switcher */}
        <div className="flex items-center gap-3">
          <button
            type="button"
            onClick={onOpenSearch}
            className="flex items-center gap-2 px-3 py-1.5 rounded-xl bg-[#1b1e26] hover:bg-[#252934] border border-[#2e3342] text-xs text-[#94a3b8] hover:text-[#f8fafc] transition-colors"
          >
            <Search className="w-3.5 h-3.5 text-[#f5b041]" />
            <span className="hidden sm:inline">Search notes, ideas...</span>
            <kbd className="hidden sm:inline px-1 py-0.5 rounded bg-[#121316] text-[10px] text-[#64748b] border border-[#2e3342]">
              /
            </kbd>
          </button>

          {/* User Identity Pill & Dropdown */}
          {currentUser && (
            <div className="relative">
              <button
                type="button"
                onClick={() => setShowSwitchDropdown(!showSwitchDropdown)}
                className="flex items-center gap-2 pl-2 pr-3 py-1.5 rounded-xl bg-[#1b1e26] hover:bg-[#252934] border border-[#333846] transition-all"
              >
                <div
                  className="w-6 h-6 rounded-lg flex items-center justify-center text-xs font-bold"
                  style={{
                    backgroundColor: `${currentUser.avatarColor}25`,
                    color: currentUser.avatarColor,
                  }}
                >
                  {currentUser.name.charAt(0).toUpperCase()}
                </div>
                <span className="text-xs font-semibold text-[#f8fafc] max-w-[110px] truncate">
                  {currentUser.name.split(' ')[0]}
                </span>
                <ChevronDown className="w-3.5 h-3.5 text-[#94a3b8]" />
              </button>

              {/* Dropdown Menu */}
              {showSwitchDropdown && (
                <div className="absolute right-0 mt-2 w-72 rounded-2xl bg-[#15171e] border border-[#333846] shadow-2xl p-2.5 z-50 animate-fade-in">
                  <div className="px-3 py-2 border-b border-[#2e3342] mb-1.5">
                    <p className="text-[10px] font-semibold text-[#94a3b8] uppercase tracking-wider">
                      Logged In (Remembered)
                    </p>
                    <p className="text-sm font-bold text-[#f5b041] truncate">{currentUser.name}</p>
                    <p className="text-[11px] text-[#8b7cf6] truncate">{currentUser.field}</p>
                  </div>

                  {allUsers.length > 1 && (
                    <>
                      <p className="px-3 py-1 text-[10px] font-semibold text-[#64748b] uppercase tracking-wider">
                        Switch Registered Shelf
                      </p>

                      <div className="max-h-40 overflow-y-auto space-y-1 mb-2">
                        {allUsers.map((user) => (
                          <button
                            key={user.id}
                            type="button"
                            onClick={() => {
                              onSwitchUser(user);
                              setShowSwitchDropdown(false);
                            }}
                            className={`w-full flex items-center gap-2.5 px-3 py-2 rounded-xl text-left text-xs transition-colors ${
                              user.id === currentUser.id
                                ? 'bg-[#f5b041]/15 text-[#f5b041] font-bold'
                                : 'text-[#cbd5e1] hover:bg-[#1f232d]'
                            }`}
                          >
                            <div
                              className="w-5 h-5 rounded-md flex items-center justify-center text-[10px] font-bold"
                              style={{
                                backgroundColor: `${user.avatarColor}20`,
                                color: user.avatarColor,
                              }}
                            >
                              {user.name.charAt(0).toUpperCase()}
                            </div>
                            <span className="truncate">{user.name}</span>
                          </button>
                        ))}
                      </div>
                    </>
                  )}

                  <div className="pt-1.5 border-t border-[#2e3342] space-y-1">
                    {onAddNewStudent && (
                      <button
                        type="button"
                        onClick={() => {
                          setShowSwitchDropdown(false);
                          onAddNewStudent();
                        }}
                        className="w-full flex items-center gap-2 px-3 py-2 rounded-xl text-left text-xs font-semibold text-[#f5b041] hover:bg-[#f5b041]/10 transition-colors"
                      >
                        <UserPlus className="w-4 h-4" />
                        <span>+ Open New Student Shelf</span>
                      </button>
                    )}

                    {onLogout && (
                      <button
                        type="button"
                        onClick={() => {
                          setShowSwitchDropdown(false);
                          onLogout();
                        }}
                        className="w-full flex items-center gap-2 px-3 py-2 rounded-xl text-left text-xs font-medium text-[#94a3b8] hover:text-red-400 hover:bg-red-500/10 transition-colors"
                      >
                        <LogOut className="w-4 h-4" />
                        <span>Sign Out / Switch Device</span>
                      </button>
                    )}
                  </div>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </header>
  );
};
