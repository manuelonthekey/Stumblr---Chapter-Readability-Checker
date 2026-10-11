export const Privacy = () => (
  <div className="max-w-3xl mx-auto glass-opaque rounded-3xl p-8 md:p-12 my-8 shadow-sm transition-all text-main">
    <h2 className="text-3xl font-serif font-bold mb-2 text-primary">Privacy Policy</h2>
    <p className="text-sm text-muted font-bold tracking-wide uppercase mb-8">Last updated: Today</p>

    <p className="text-lg leading-relaxed mb-10">
      Stumblr is designed from the ground up for privacy. We know that your unpublished fiction is your most valuable asset.
    </p>

    <div className="space-y-8">
      <div>
        <h3 className="text-xl font-bold font-serif mb-3 text-primary">1. We do not store your text</h3>
        <p className="leading-relaxed text-muted font-medium">
          When you click "Analyze", your text is sent to our server securely, processed entirely in memory, and immediately discarded. 
          It is never written to a database, never saved to a file, and never written to our server logs.
        </p>
      </div>

      <div>
        <h3 className="text-xl font-bold font-serif mb-3 text-primary">2. No AI Training</h3>
        <p className="leading-relaxed text-muted font-medium">
          Because we do not store your text, your work is never used to train Artificial Intelligence models, nor is it sent to third-party APIs like OpenAI. 
          Our analysis engine is 100% rule-based math that runs on our own server.
        </p>
      </div>

      <div>
        <h3 className="text-xl font-bold font-serif mb-3 text-primary">3. No Cookies or Trackers</h3>
        <p className="leading-relaxed text-muted font-medium">
          We don't use marketing cookies, tracking pixels, or third-party analytics that harvest your personal data. 
        </p>
      </div>

      <div>
        <h3 className="text-xl font-bold font-serif mb-3 text-primary">4. Open Source</h3>
        <p className="leading-relaxed text-muted font-medium">
          Don't trust us? You don't have to. The entire codebase for this tool is open-source. You can run it on your own computer completely offline.
        </p>
      </div>
    </div>
  </div>
);
