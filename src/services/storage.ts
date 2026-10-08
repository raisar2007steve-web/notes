import { User, Note, Project, Task, Development, Activity, Version } from '../types';

const STORAGE_KEYS = {
  USERS: 'innovara_live_users_v2',
  NOTES: 'innovara_live_notes_v2',
  PROJECTS: 'innovara_live_projects_v2',
  TASKS: 'innovara_live_tasks_v2',
  DEVELOPMENTS: 'innovara_live_developments_v2',
  ACTIVITIES: 'innovara_live_activities_v2',
  VERSIONS: 'innovara_live_versions_v2',
  ACTIVE_USER_ID: 'innovara_remembered_student_id_v2',
  CLEANED_OLD_MOCK: 'innovara_cleaned_mock_v2',
};

class StorageService {
  private listeners: Set<() => void> = new Set();

  constructor() {
    this.init();
  }

  private init() {
    // Clear old mock seed storage keys if they exist from previous runs
    if (!localStorage.getItem(STORAGE_KEYS.CLEANED_OLD_MOCK)) {
      localStorage.removeItem('innovara_users_v1');
      localStorage.removeItem('innovara_notes_v1');
      localStorage.removeItem('innovara_projects_v1');
      localStorage.removeItem('innovara_tasks_v1');
      localStorage.removeItem('innovara_developments_v1');
      localStorage.removeItem('innovara_activities_v1');
      localStorage.removeItem('innovara_versions_v1');
      localStorage.removeItem('innovara_active_user_id_v1');
      localStorage.setItem(STORAGE_KEYS.CLEANED_OLD_MOCK, 'true');
    }

    if (!localStorage.getItem(STORAGE_KEYS.USERS)) {
      localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify([]));
    }
    if (!localStorage.getItem(STORAGE_KEYS.PROJECTS)) {
      localStorage.setItem(STORAGE_KEYS.PROJECTS, JSON.stringify([]));
    }
    if (!localStorage.getItem(STORAGE_KEYS.NOTES)) {
      localStorage.setItem(STORAGE_KEYS.NOTES, JSON.stringify([]));
    }
    if (!localStorage.getItem(STORAGE_KEYS.TASKS)) {
      localStorage.setItem(STORAGE_KEYS.TASKS, JSON.stringify([]));
    }
    if (!localStorage.getItem(STORAGE_KEYS.DEVELOPMENTS)) {
      localStorage.setItem(STORAGE_KEYS.DEVELOPMENTS, JSON.stringify([]));
    }
    if (!localStorage.getItem(STORAGE_KEYS.ACTIVITIES)) {
      localStorage.setItem(STORAGE_KEYS.ACTIVITIES, JSON.stringify([]));
    }
    if (!localStorage.getItem(STORAGE_KEYS.VERSIONS)) {
      localStorage.setItem(STORAGE_KEYS.VERSIONS, JSON.stringify([]));
    }
  }

  subscribe(listener: () => void) {
    this.listeners.add(listener);
    return () => {
      this.listeners.delete(listener);
    };
  }

  private notify() {
    this.listeners.forEach((listener) => listener());
  }

  // Active User session (Permanent local device memory)
  getRememberedUserId(): string | null {
    return localStorage.getItem(STORAGE_KEYS.ACTIVE_USER_ID);
  }

  setRememberedUserId(id: string) {
    localStorage.setItem(STORAGE_KEYS.ACTIVE_USER_ID, id);
    this.notify();
  }

  clearRememberedUser() {
    localStorage.removeItem(STORAGE_KEYS.ACTIVE_USER_ID);
    this.notify();
  }

  // Users
  getUsers(): User[] {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.USERS);
      return data ? JSON.parse(data) : [];
    } catch {
      return [];
    }
  }

  getUserById(id: string): User | undefined {
    return this.getUsers().find((u) => u.id === id);
  }

  getUserByName(name: string): User | undefined {
    return this.getUsers().find((u) => u.name.trim().toLowerCase() === name.trim().toLowerCase());
  }

  getOrCreateUser(name: string, field?: string, bio?: string, quote?: string): User {
    const existing = this.getUserByName(name);
    if (existing) {
      this.setRememberedUserId(existing.id);
      return existing;
    }

    const palette = ['#f5b041', '#8b7cf6', '#38bdf8', '#34d399', '#fb7185', '#f472b6'];
    const color = palette[Math.abs(name.split('').reduce((acc, c) => acc + c.charCodeAt(0), 0)) % palette.length];

    const newUser: User = {
      id: 'user_' + Date.now().toString(36) + '_' + Math.random().toString(36).substring(2, 6),
      name: name.trim(),
      field: field?.trim() || 'Innovator & Collegiate Researcher',
      bio: bio?.trim() || 'My digital shelf on Innovara Notes where I document my ideas and builds.',
      quote: quote?.trim() || 'Every student has a story. Every idea deserves a place.',
      avatarColor: color,
      interests: 'Innovation · Technology · Research',
      createdAt: Date.now(),
      updatedAt: Date.now(),
    };

    const users = this.getUsers();
    users.unshift(newUser);
    localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(users));
    this.setRememberedUserId(newUser.id);

    this.logActivity(newUser.id, newUser.name, 'opened their innovation shelf', 'USER' as any, newUser.name, newUser.id);
    this.notify();
    return newUser;
  }

  updateUserProfile(updatedUser: User) {
    const users = this.getUsers();
    const index = users.findIndex((u) => u.id === updatedUser.id);
    if (index !== -1) {
      users[index] = { ...updatedUser, updatedAt: Date.now() };
      localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(users));
      this.notify();
    }
  }

  // Real-time Dynamic Stats calculation for any user
  getUserStats(userId: string) {
    const notes = this.getNotes().filter((n) => n.userId === userId);
    const projects = this.getProjects().filter((p) => p.userId === userId);
    const tasks = this.getTasks().filter((t) => t.userId === userId);
    const developments = this.getDevelopments().filter((d) => d.userId === userId);

    return {
      ideasCount: notes.filter((n) => n.type === 'IDEA').length,
      notesCount: notes.length,
      thoughtsCount: notes.filter((n) => ['THOUGHT', 'RESEARCH', 'LEARNING'].includes(n.type)).length,
      projectsCount: projects.length,
      tasksCount: tasks.length,
      completedTasksCount: tasks.filter((t) => t.status === 'COMPLETED').length,
      developmentsCount: developments.length,
    };
  }

  // Notes
  getNotes(): Note[] {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.NOTES);
      return data ? JSON.parse(data) : [];
    } catch {
      return [];
    }
  }

  getNotesForUser(userId: string, isOwner: boolean): Note[] {
    const all = this.getNotes().filter((n) => n.userId === userId);
    return isOwner ? all : all.filter((n) => n.visibility === 'PUBLIC');
  }

  saveNote(note: Note, authorName: string) {
    const notes = this.getNotes();
    const index = notes.findIndex((n) => n.id === note.id);
    const isNew = index === -1;

    if (isNew) {
      notes.unshift(note);
    } else {
      notes[index] = { ...note, updatedAt: Date.now() };
    }
    localStorage.setItem(STORAGE_KEYS.NOTES, JSON.stringify(notes));

    // Record non-destructive version
    this.recordVersion('NOTE', note.id, `${note.title}: ${note.content.substring(0, 60)}...`, authorName);

    // Record activity
    const action = isNew ? `added a new ${note.type.toLowerCase()}` : `updated ${note.type.toLowerCase()}`;
    this.logActivity(note.userId, authorName, action, note.type, note.title, note.id);

    this.notify();
  }

  deleteNote(id: string) {
    const notes = this.getNotes().filter((n) => n.id !== id);
    localStorage.setItem(STORAGE_KEYS.NOTES, JSON.stringify(notes));
    this.notify();
  }

  // Projects
  getProjects(): Project[] {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.PROJECTS);
      return data ? JSON.parse(data) : [];
    } catch {
      return [];
    }
  }

  getProjectsForUser(userId: string): Project[] {
    return this.getProjects().filter((p) => p.userId === userId);
  }

  saveProject(project: Project, authorName: string) {
    const projects = this.getProjects();
    const index = projects.findIndex((p) => p.id === project.id);
    const isNew = index === -1;

    if (isNew) {
      projects.unshift(project);
    } else {
      projects[index] = { ...project, updatedAt: Date.now() };
    }
    localStorage.setItem(STORAGE_KEYS.PROJECTS, JSON.stringify(projects));

    this.recordVersion(
      'PROJECT',
      project.id,
      `Progress: ${project.progressPercent}% - ${project.status} (${project.name})`,
      authorName
    );
    const action = isNew ? 'created new project' : 'updated project';
    this.logActivity(project.userId, authorName, action, 'PROJECT', project.name, project.id);

    this.notify();
  }

  deleteProject(id: string) {
    const projects = this.getProjects().filter((p) => p.id !== id);
    localStorage.setItem(STORAGE_KEYS.PROJECTS, JSON.stringify(projects));
    this.notify();
  }

  // Tasks
  getTasks(): Task[] {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.TASKS);
      return data ? JSON.parse(data) : [];
    } catch {
      return [];
    }
  }

  getTasksForUser(userId: string): Task[] {
    return this.getTasks().filter((t) => t.userId === userId);
  }

  saveTask(task: Task, authorName: string) {
    const tasks = this.getTasks();
    const index = tasks.findIndex((t) => t.id === task.id);
    const isNew = index === -1;

    if (isNew) {
      tasks.unshift(task);
    } else {
      tasks[index] = { ...task, updatedAt: Date.now() };
    }
    localStorage.setItem(STORAGE_KEYS.TASKS, JSON.stringify(tasks));

    this.logActivity(task.userId, authorName, isNew ? 'created task' : 'updated task', 'TASK', task.title, task.id);
    this.notify();
  }

  toggleTask(taskId: string, authorName: string) {
    const tasks = this.getTasks();
    const task = tasks.find((t) => t.id === taskId);
    if (!task) return;

    task.status = task.status === 'COMPLETED' ? 'TODO' : 'COMPLETED';
    task.updatedAt = Date.now();
    localStorage.setItem(STORAGE_KEYS.TASKS, JSON.stringify(tasks));

    const action = task.status === 'COMPLETED' ? 'completed a task' : 'reopened a task';
    this.logActivity(task.userId, authorName, action, 'TASK', task.title, task.id);
    this.notify();
  }

  deleteTask(id: string) {
    const tasks = this.getTasks().filter((t) => t.id !== id);
    localStorage.setItem(STORAGE_KEYS.TASKS, JSON.stringify(tasks));
    this.notify();
  }

  // Developments
  getDevelopments(): Development[] {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.DEVELOPMENTS);
      return data ? JSON.parse(data) : [];
    } catch {
      return [];
    }
  }

  getDevelopmentsForUser(userId: string): Development[] {
    return this.getDevelopments().filter((d) => d.userId === userId);
  }

  saveDevelopment(dev: Development, authorName: string) {
    const devs = this.getDevelopments();
    const index = devs.findIndex((d) => d.id === dev.id);
    if (index === -1) {
      devs.unshift(dev);
    } else {
      devs[index] = dev;
    }
    localStorage.setItem(STORAGE_KEYS.DEVELOPMENTS, JSON.stringify(devs));

    this.recordVersion('DEV_LOG', dev.id, dev.whatChanged.substring(0, 80), authorName);
    this.logActivity(dev.userId, authorName, 'logged development milestone', 'DEV_LOG', dev.title, dev.id);
    this.notify();
  }

  deleteDevelopment(id: string) {
    const devs = this.getDevelopments().filter((d) => d.id !== id);
    localStorage.setItem(STORAGE_KEYS.DEVELOPMENTS, JSON.stringify(devs));
    this.notify();
  }

  // Activities
  getActivities(): Activity[] {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.ACTIVITIES);
      return data ? JSON.parse(data) : [];
    } catch {
      return [];
    }
  }

  private logActivity(
    userId: string,
    userName: string,
    action: string,
    targetType: Activity['targetType'],
    targetTitle: string,
    targetId: string
  ) {
    const activities = this.getActivities();
    const newAct: Activity = {
      id: 'act_' + Date.now().toString(36) + '_' + Math.random().toString(36).substring(2, 6),
      userId,
      userName,
      action,
      targetType,
      targetTitle,
      targetId,
      timestamp: Date.now(),
    };
    activities.unshift(newAct);
    localStorage.setItem(STORAGE_KEYS.ACTIVITIES, JSON.stringify(activities.slice(0, 100)));
  }

  // Versions
  getVersionsForTarget(targetId: string): Version[] {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.VERSIONS);
      const all: Version[] = data ? JSON.parse(data) : [];
      return all.filter((v) => v.targetId === targetId).sort((a, b) => b.versionNumber - a.versionNumber);
    } catch {
      return [];
    }
  }

  private recordVersion(
    targetType: Version['targetType'],
    targetId: string,
    contentSnippet: string,
    editedBy: string
  ) {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.VERSIONS);
      const all: Version[] = data ? JSON.parse(data) : [];
      const count = all.filter((v) => v.targetId === targetId).length;

      const newVer: Version = {
        id: 'ver_' + Date.now().toString(36) + '_' + Math.random().toString(36).substring(2, 6),
        targetType,
        targetId,
        versionNumber: count + 1,
        contentSnippet,
        editedBy,
        createdAt: Date.now(),
      };
      all.unshift(newVer);
      localStorage.setItem(STORAGE_KEYS.VERSIONS, JSON.stringify(all));
    } catch {
      // ignore
    }
  }
}

export const storage = new StorageService();
