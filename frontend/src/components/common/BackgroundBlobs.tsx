export const BackgroundBlobs = () => {
  return (
    <div className="fixed inset-0 overflow-hidden pointer-events-none -z-10 bg-gradient-to-br from-[var(--bg-grad-start)] via-[var(--bg-grad-mid)] to-[var(--bg-grad-end)] transition-colors duration-500">
      <div 
        className="blob bg-[var(--blob-1)] w-[60vw] h-[60vw] max-w-[800px] max-h-[800px] top-[-10%] left-[-10%]" 
        style={{ animationDelay: '0s' }} 
      />
      <div 
        className="blob bg-[var(--blob-2)] w-[50vw] h-[50vw] max-w-[700px] max-h-[700px] bottom-[-10%] right-[-5%]" 
        style={{ animationDelay: '-5s' }} 
      />
      <div 
        className="blob bg-[var(--blob-3)] w-[40vw] h-[40vw] max-w-[600px] max-h-[600px] top-[30%] left-[40%]" 
        style={{ animationDelay: '-10s' }} 
      />
    </div>
  );
};
