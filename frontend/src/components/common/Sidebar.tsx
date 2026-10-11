import { useState } from 'react';
import { BookOpen, Info, Shield, MessageSquare, ChevronLeft, ChevronRight } from 'lucide-react';

interface SidebarProps {
  view: 'app' | 'how' | 'privacy';
  setView: (view: 'app' | 'how' | 'privacy') => void;
  onClear: () => void;
}

export const Sidebar = ({ view, setView, onClear }: SidebarProps) => {
  const [isCollapsed, setIsCollapsed] = useState(false);

  const NavItem = ({ icon: Icon, label, active, onClick }: any) => (
    <button 
      onClick={onClick}
      className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 cursor-pointer ${
        active 
          ? 'bg-[var(--brand-primary)] text-white shadow-md' 
          : 'text-muted hover:bg-[var(--glass-surface)] hover:text-main'
      } active:scale-95`}
    >
      <Icon size={20} className={active ? 'text-white' : 'text-primary'} />
      {!isCollapsed && <span className="font-medium whitespace-nowrap">{label}</span>}
    </button>
  );

  return (
    <>
      {/* Mobile Bottom Bar */}
      <div className="md:hidden fixed bottom-0 left-0 right-0 glass-sidebar flex justify-around items-center p-3 z-50">
        <button onClick={() => { setView('app'); onClear(); }} className={`p-2 rounded-full cursor-pointer active:scale-95 transition-transform ${view === 'app' ? 'bg-[var(--brand-primary)] text-white' : 'text-primary'}`}>
          <BookOpen size={24} />
        </button>
        <button onClick={() => setView('how')} className={`p-2 rounded-full cursor-pointer active:scale-95 transition-transform ${view === 'how' ? 'bg-[var(--brand-primary)] text-white' : 'text-primary'}`}>
          <Info size={24} />
        </button>
        <button onClick={() => setView('privacy')} className={`p-2 rounded-full cursor-pointer active:scale-95 transition-transform ${view === 'privacy' ? 'bg-[var(--brand-primary)] text-white' : 'text-primary'}`}>
          <Shield size={24} />
        </button>
      </div>

      {/* Desktop Sidebar */}
      <aside className={`hidden md:flex flex-col glass-sidebar h-full relative transition-all duration-300 ${isCollapsed ? 'w-20' : 'w-64'}`}>
        <button 
          onClick={() => setIsCollapsed(!isCollapsed)}
          className="absolute -right-3 top-8 bg-[var(--glass-surface)] border border-[var(--border-light)] rounded-full p-1 text-primary hover:text-main shadow-sm z-10 cursor-pointer active:scale-90 transition-transform"
        >
          {isCollapsed ? <ChevronRight size={16} /> : <ChevronLeft size={16} />}
        </button>

        <div className="p-6 flex items-center space-x-3 cursor-pointer mt-4" onClick={() => { setView('app'); onClear(); }}>
          <div className="w-10 h-10 bg-[var(--brand-primary)] rounded-xl flex items-center justify-center text-white font-bold font-serif text-xl shadow-md shrink-0">
            S
          </div>
          {!isCollapsed && <span className="font-bold text-2xl tracking-tight text-main font-serif">Stumblr</span>}
        </div>

        <nav className="flex-1 px-4 space-y-2 mt-4">
          <NavItem icon={BookOpen} label="Analyze" active={view === 'app'} onClick={() => { setView('app'); onClear(); }} />
          <NavItem icon={Info} label="How it works" active={view === 'how'} onClick={() => setView('how')} />
          <NavItem icon={Shield} label="Privacy" active={view === 'privacy'} onClick={() => setView('privacy')} />
          <a href="mailto:feedback@chaptercheck.app" className="w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 text-muted hover:bg-[var(--glass-surface)] hover:text-main cursor-pointer active:scale-95">
            <MessageSquare size={20} className="text-primary" />
            {!isCollapsed && <span className="font-medium whitespace-nowrap">Feedback</span>}
          </a>
        </nav>
      </aside>
    </>
  );
};
