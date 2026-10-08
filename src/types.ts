export type NoteType = 'IDEA' | 'THOUGHT' | 'RESEARCH' | 'LEARNING' | 'GENERAL';

export type TaskPriority = 'HIGH' | 'MEDIUM' | 'LOW';
export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'COMPLETED';

export interface User {
  id: string;
  name: string;
  field: string;
  bio: string;
  quote: string;
  avatarColor: string;
  interests: string;
  createdAt: number;
  updatedAt: number;
}

export interface Note {
  id: string;
  userId: string;
  type: NoteType;
  title: string;
  content: string; // Markdown text
  category: string;
  status: string; // e.g. "💭 Idea", "🔬 Researching", "🛠 Building", "🧪 Testing", "🚀 Deployed", "✅ Completed", "⏸ Paused"
  tags: string[];
  visibility: 'PUBLIC' | 'PRIVATE';
  attachmentName?: string;
  attachmentType?: 'IMAGE' | 'PDF' | 'CODE';
  createdAt: number;
  updatedAt: number;
}

export interface Project {
  id: string;
  userId: string;
  name: string;
  tagline: string;
  description: string;
  goal: string;
  problem: string;
  solution: string;
  technologies: string[];
  progressPercent: number;
  status: 'Building' | 'Researching' | 'Testing' | 'Deployed' | 'Completed';
  repoUrl: string;
  demoUrl: string;
  markdownDoc: string;
  createdAt: number;
  updatedAt: number;
}

export interface Task {
  id: string;
  userId: string;
  title: string;
  description: string;
  priority: TaskPriority;
  status: TaskStatus;
  dueDate: string;
  category: string;
  createdAt: number;
  updatedAt: number;
}

export interface Development {
  id: string;
  userId: string;
  projectId?: string;
  title: string;
  dateStr: string;
  whatChanged: string; // Checklist or bullet points
  whatWorked: string;
  whatFailed: string;
  nextStep: string;
  status: string;
  createdAt: number;
}

export interface Activity {
  id: string;
  userId: string;
  userName: string;
  action: string;
  targetType: 'IDEA' | 'PROJECT' | 'TASK' | 'THOUGHT' | 'DEV_LOG' | 'NOTE';
  targetTitle: string;
  targetId: string;
  timestamp: number;
}

export interface Version {
  id: string;
  targetType: 'NOTE' | 'PROJECT' | 'DEV_LOG';
  targetId: string;
  versionNumber: number;
  contentSnippet: string;
  editedBy: string;
  createdAt: number;
}
