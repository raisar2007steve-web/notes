import React from 'react';
import { Activity, User } from '../types';
import { Flame, Clock, ArrowRight } from 'lucide-react';

interface CommunityFeedScreenProps {
  activities: Activity[];
  allUsers: User[];
  onOpenUserShelf: (user: User) => void;
}

export const CommunityFeedScreen: React.FC<CommunityFeedScreenProps> = ({
  activities,
  allUsers,
  onOpenUserShelf,
}) => {
  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-8 space-y-6 animate-fade-in">
      <div className="space-y-1.5 pb-2 border-b border-[#2e3342]/60">
        <h1 className="text-2xl md:text-3xl font-extrabold font-display text-[#f8fafc] flex items-center gap-2">
          <Flame className="w-7 h-7 text-orange-400" />
          <span>🔥 Recent Innovara Activity</span>
        </h1>
        <p className="text-xs md:text-sm text-[#94a3b8]">
          Real-time telemetry of thoughts, builds, and discoveries across the university.
        </p>
      </div>

      <div className="space-y-3">
        {activities.map((act) => {
          const user = allUsers.find((u) => u.id === act.userId);
          const icon =
            act.targetType === 'IDEA'
              ? '💡'
              : act.targetType === 'PROJECT'
              ? '🚀'
              : act.targetType === 'TASK'
              ? '✅'
              : act.targetType === 'DEV_LOG'
              ? '🛠'
              : '🧠';

          return (
            <div
              key={act.id}
              onClick={() => {
                if (user) onOpenUserShelf(user);
              }}
              className="group flex items-center justify-between p-4 rounded-2xl bg-[#161820] border border-[#2e3342] hover:border-[#f5b041]/50 cursor-pointer transition-all shadow-md hover:shadow-xl"
            >
              <div className="flex items-center gap-4 min-w-0">
                <span className="text-2xl p-2.5 rounded-xl bg-[#121316] border border-[#262a36] shrink-0 group-hover:scale-105 transition-transform">
                  {icon}
                </span>

                <div className="min-w-0">
                  <div className="flex items-center gap-2 flex-wrap">
                    <span className="font-bold text-sm text-[#f8fafc] group-hover:text-[#f5b041] transition-colors">
                      {act.userName}
                    </span>
                    <span className="text-xs text-[#94a3b8]">{act.action}</span>
                  </div>

                  <p className="text-xs md:text-sm font-semibold text-[#f5b041] truncate mt-0.5">
                    “{act.targetTitle}”
                  </p>
                </div>
              </div>

              <div className="flex items-center gap-3 shrink-0 ml-4">
                <span className="text-[11px] text-[#64748b] flex items-center gap-1 font-mono">
                  <Clock className="w-3 h-3" />
                  {new Date(act.timestamp).toLocaleTimeString([], {
                    hour: '2-digit',
                    minute: '2-digit',
                  })}
                </span>
                <ArrowRight className="w-4 h-4 text-[#64748b] group-hover:text-[#f5b041] transition-transform group-hover:translate-x-1" />
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
