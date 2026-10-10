export const Skeleton = ({ className = "" }: { className?: string }) => {
  return (
    <div className={`skeleton-shimmer rounded-md ${className}`} />
  );
};

export const SkeletonResults = () => {
  return (
    <div className="space-y-8 animate-in fade-in duration-300 pb-20">
      
      {/* Score Card Skeleton */}
      <div className="bg-surface rounded-xl shadow-sm border border-brand p-6 flex flex-col md:flex-row items-center justify-between gap-6">
        <div className="flex-1 flex flex-col items-center">
          <Skeleton className="w-32 h-20 mb-2" />
        </div>
        <div className="flex-1 border-l border-brand pl-6 space-y-4 w-full">
          <div>
            <Skeleton className="w-3/4 h-6 mb-2" />
            <Skeleton className="w-1/2 h-4" />
          </div>
          <div>
            <Skeleton className="w-1/3 h-4 mb-2" />
            <Skeleton className="w-full h-4" />
          </div>
        </div>
      </div>
      
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
        {/* Left Column Skeletons */}
        <div className="lg:col-span-4 space-y-8">
          <div className="bg-surface rounded-xl shadow-sm border border-brand p-6">
            <Skeleton className="w-1/4 h-4 mb-6" />
            <div className="grid grid-cols-2 gap-y-6 gap-x-4">
              {[...Array(6)].map((_, i) => (
                <div key={i} className="flex flex-col gap-2">
                  <Skeleton className="w-16 h-3" />
                  <Skeleton className="w-20 h-5" />
                </div>
              ))}
            </div>
          </div>
          
          <div className="bg-surface rounded-xl shadow-sm border border-brand p-6 space-y-4">
            <Skeleton className="w-1/2 h-6 mb-4" />
            {[...Array(3)].map((_, i) => (
              <div key={i} className="p-4 rounded-lg border border-brand bg-main space-y-3">
                <Skeleton className="w-full h-12" />
                <Skeleton className="w-24 h-4" />
              </div>
            ))}
          </div>
        </div>
        
        {/* Right Column Skeletons (Text) */}
        <div className="lg:col-span-8">
          <div className="bg-surface rounded-xl shadow-sm border border-brand p-8 space-y-4">
            {[...Array(12)].map((_, i) => (
              <Skeleton 
                key={i} 
                className={`h-6 ${i % 3 === 0 ? 'w-3/4' : i % 2 === 0 ? 'w-full' : 'w-5/6'}`} 
              />
            ))}
          </div>
        </div>
      </div>
      
    </div>
  );
};
