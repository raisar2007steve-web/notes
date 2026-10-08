import React, { useState, useEffect } from 'react';
import { User, Note, Project, Task, Development, Activity } from './types';
import { storage } from './services/storage';
import { Header } from './components/Header';
import { LandingScreen } from './screens/LandingScreen';
import { DashboardScreen } from './screens/DashboardScreen';
import { BookshelfScreen } from './screens/BookshelfScreen';
import { StudentBookScreen } from './screens/StudentBookScreen';
import { CommunityFeedScreen } from './screens/CommunityFeedScreen';
import { GlobalSearchScreen } from './screens/GlobalSearchScreen';
import { AddEntryModal } from './components/AddEntryModal';
import { CreateStudentModal } from './components/CreateStudentModal';
import { Home, BookOpen, Sparkles, Flame, Plus } from 'lucide-react';

export const App: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [notes, setNotes] = useState<Note[]>([]);
  const [projects, setProjects] = useState<Project[]>([]);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [developments, setDevelopments] = useState<Development[]>([]);
  const [activities, setActivities] = useState<Activity[]>([]);

  const [currentUserId, setCurrentUserId] = useState<string | null>(null);
  const [activeViewingUser, setActiveViewingUser] = useState<User | null>(null);
  const [currentTab, setCurrentTab] = useState<
    'landing' | 'dashboard' | 'bookshelf' | 'my_book' | 'feed' | 'search' | 'student_book'
  >('landing');

  const [showGlobalAddModal, setShowGlobalAddModal] = useState(false);
  const [showCreateStudentModal, setShowCreateStudentModal] = useState(false);

  // Sync state with storage
  const syncState = () => {
    const loadedUsers = storage.getUsers();
    setUsers(loadedUsers);
    setNotes(storage.getNotes());
    setProjects(storage.getProjects());
    setTasks(storage.getTasks());
    setDevelopments(storage.getDevelopments());
    setActivities(storage.getActivities());

    const rememberedId = storage.getRememberedUserId();
    if (rememberedId) {
      setCurrentUserId(rememberedId);
      const user = loadedUsers.find((u) => u.id === rememberedId);
      if (user) {
        setActiveViewingUser((prev) => prev || user);
      }
    }
  };

  useEffect(() => {
    // Initial mount: Check if user is already remembered on this local device
    const loadedUsers = storage.getUsers();
    setUsers(loadedUsers);
    setNotes(storage.getNotes());
    setProjects(storage.getProjects());
    setTasks(storage.getTasks());
    setDevelopments(storage.getDevelopments());
    setActivities(storage.getActivities());

    const rememberedId = storage.getRememberedUserId();
    if (rememberedId) {
      const existingUser = loadedUsers.find((u) => u.id === rememberedId);
      if (existingUser) {
        // PERMANENT LOGGED IN STATE: Remembered till last, never asks again!
        setCurrentUserId(existingUser.id);
        setActiveViewingUser(existingUser);
        setCurrentTab('dashboard');
      }
    }

    const unsubscribe = storage.subscribe(syncState);
    return () => unsubscribe();
  }, []);

  const currentUser = users.find((u) => u.id === currentUserId) || null;

  // Handlers
  const handleEnterFromLanding = (studentName: string, field?: string) => {
    const user = storage.getOrCreateUser(studentName, field);
    setCurrentUserId(user.id);
    setActiveViewingUser(user);
    setCurrentTab('dashboard');
  };

  const handleCreateStudent = (name: string, field: string, quote?: string) => {
    const user = storage.getOrCreateUser(name, field, undefined, quote);
    setCurrentUserId(user.id);
    setActiveViewingUser(user);
    setCurrentTab('student_book');
  };

  const handleOpenUserShelf = (targetUser: User) => {
    setActiveViewingUser(targetUser);
    setCurrentTab('student_book');
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleSwitchUser = (targetUser: User) => {
    storage.setRememberedUserId(targetUser.id);
    setCurrentUserId(targetUser.id);
    setActiveViewingUser(targetUser);
  };

  const handleLogout = () => {
    storage.clearRememberedUser();
    setCurrentUserId(null);
    setActiveViewingUser(null);
    setCurrentTab('landing');
  };

  const handleSelectTab = (tab: string) => {
    if (tab === 'my_book') {
      if (currentUser) {
        setActiveViewingUser(currentUser);
        setCurrentTab('student_book');
      } else {
        setCurrentTab('bookshelf');
      }
    } else {
      setCurrentTab(tab as any);
    }
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen flex flex-col bg-[#0f1015] text-[#f8fafc]">
      {/* Show header on all screens except landing */}
      {currentTab !== 'landing' && (
        <Header
          currentUser={currentUser}
          allUsers={users}
          currentTab={
            currentTab === 'student_book' && activeViewingUser?.id === currentUser?.id
              ? 'my_book'
              : currentTab
          }
          onSelectTab={handleSelectTab}
          onSwitchUser={handleSwitchUser}
          onOpenSearch={() => handleSelectTab('search')}
          onAddNewStudent={() => setShowCreateStudentModal(true)}
          onLogout={handleLogout}
        />
      )}

      {/* Main Content View */}
      <main className="flex-1">
        {currentTab === 'landing' && (
          <LandingScreen
            existingUsers={users}
            onEnter={handleEnterFromLanding}
          />
        )}

        {currentTab === 'dashboard' && currentUser && (
          <DashboardScreen
            currentUser={currentUser}
            allUsers={users}
            notes={notes}
            projects={projects}
            tasks={tasks}
            developments={developments}
            activities={activities}
            onOpenMyShelf={() => {
              setActiveViewingUser(currentUser);
              setCurrentTab('student_book');
            }}
            onOpenUserShelf={handleOpenUserShelf}
            onOpenBookshelf={() => setCurrentTab('bookshelf')}
            onAddEntry={() => setShowGlobalAddModal(true)}
          />
        )}

        {currentTab === 'bookshelf' && (
          <BookshelfScreen
            allUsers={users}
            onOpenStudentBook={handleOpenUserShelf}
            onAddNewStudent={() => setShowCreateStudentModal(true)}
          />
        )}

        {currentTab === 'student_book' && (activeViewingUser || currentUser) && (
          <StudentBookScreen
            user={activeViewingUser || currentUser!}
            currentUser={currentUser}
            notes={storage.getNotesForUser(
              (activeViewingUser || currentUser!).id,
              currentUser?.id === (activeViewingUser || currentUser!).id
            )}
            projects={storage.getProjectsForUser((activeViewingUser || currentUser!).id)}
            tasks={storage.getTasksForUser((activeViewingUser || currentUser!).id)}
            developments={storage.getDevelopmentsForUser((activeViewingUser || currentUser!).id)}
            activities={activities.filter((a) => a.userId === (activeViewingUser || currentUser!).id)}
            onBackToBookshelf={() => setCurrentTab('bookshelf')}
            onSaveNote={(note) => storage.saveNote(note, currentUser?.name || 'Innovator')}
            onDeleteNote={(id) => storage.deleteNote(id)}
            onSaveProject={(project) => storage.saveProject(project, currentUser?.name || 'Innovator')}
            onDeleteProject={(id) => storage.deleteProject(id)}
            onSaveTask={(task) => storage.saveTask(task, currentUser?.name || 'Innovator')}
            onToggleTask={(taskId) => storage.toggleTask(taskId, currentUser?.name || 'Innovator')}
            onDeleteTask={(id) => storage.deleteTask(id)}
            onSaveDevelopment={(dev) => storage.saveDevelopment(dev, currentUser?.name || 'Innovator')}
            onDeleteDevelopment={(id) => storage.deleteDevelopment(id)}
            getVersionsForTarget={(targetId) => storage.getVersionsForTarget(targetId)}
          />
        )}

        {currentTab === 'feed' && (
          <CommunityFeedScreen
            activities={activities}
            allUsers={users}
            onOpenUserShelf={handleOpenUserShelf}
          />
        )}

        {currentTab === 'search' && (
          <GlobalSearchScreen
            allUsers={users}
            notes={notes}
            projects={projects}
            developments={developments}
            onOpenUserShelf={handleOpenUserShelf}
          />
        )}
      </main>

      {/* Global Add Entry Modal */}
      {showGlobalAddModal && currentUser && (
        <AddEntryModal
          currentUserId={currentUser.id}
          currentUserName={currentUser.name}
          onClose={() => setShowGlobalAddModal(false)}
          onSaveNote={(note) => storage.saveNote(note, currentUser.name)}
          onSaveProject={(project) => storage.saveProject(project, currentUser.name)}
          onSaveTask={(task) => storage.saveTask(task, currentUser.name)}
          onSaveDevelopment={(dev) => storage.saveDevelopment(dev, currentUser.name)}
        />
      )}

      {/* Create New Student Shelf Modal */}
      {showCreateStudentModal && (
        <CreateStudentModal
          onClose={() => setShowCreateStudentModal(false)}
          onCreate={handleCreateStudent}
        />
      )}

      {/* Mobile Bottom Navigation Bar */}
      {currentTab !== 'landing' && (
        <div className="md:hidden fixed bottom-0 left-0 right-0 z-40 bg-[#0f1015]/95 backdrop-blur-lg border-t border-[#2e3342] px-3 py-2 flex items-center justify-around shadow-2xl">
          <button
            type="button"
            onClick={() => handleSelectTab('dashboard')}
            className={`flex flex-col items-center gap-1 text-[10px] font-medium transition-colors ${
              currentTab === 'dashboard' ? 'text-[#f5b041] font-bold' : 'text-[#94a3b8]'
            }`}
          >
            <Home className="w-4 h-4" />
            Home
          </button>

          <button
            type="button"
            onClick={() => handleSelectTab('bookshelf')}
            className={`flex flex-col items-center gap-1 text-[10px] font-medium transition-colors ${
              currentTab === 'bookshelf' ? 'text-[#f5b041] font-bold' : 'text-[#94a3b8]'
            }`}
          >
            <BookOpen className="w-4 h-4" />
            Bookshelf
          </button>

          <button
            type="button"
            onClick={() => setShowGlobalAddModal(true)}
            className="flex items-center justify-center w-10 h-10 rounded-full bg-[#f5b041] text-[#0f1015] shadow-lg -mt-3 border-2 border-[#0f1015]"
          >
            <Plus className="w-5 h-5 stroke-[2.5]" />
          </button>

          <button
            type="button"
            onClick={() => handleSelectTab('my_book')}
            className={`flex flex-col items-center gap-1 text-[10px] font-medium transition-colors ${
              currentTab === 'student_book' && activeViewingUser?.id === currentUser?.id
                ? 'text-[#f5b041] font-bold'
                : 'text-[#94a3b8]'
            }`}
          >
            <Sparkles className="w-4 h-4" />
            My Shelf
          </button>

          <button
            type="button"
            onClick={() => handleSelectTab('feed')}
            className={`flex flex-col items-center gap-1 text-[10px] font-medium transition-colors ${
              currentTab === 'feed' ? 'text-[#f5b041] font-bold' : 'text-[#94a3b8]'
            }`}
          >
            <Flame className="w-4 h-4" />
            Feed
          </button>
        </div>
      )}
    </div>
  );
};

export default App;
