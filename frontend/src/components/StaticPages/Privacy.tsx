export const Privacy = () => (
  <div className="max-w-3xl mx-auto bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-8 my-8 prose dark:prose-invert">
    <h2>Privacy Policy</h2>
    <p>Last updated: Today</p>

    <p>
      ChapterCheck is designed from the ground up for privacy. We know that your unpublished fiction is your most valuable asset.
    </p>

    <h3>1. We do not store your text</h3>
    <p>
      When you click "Analyze", your text is sent to our server securely, processed entirely in memory, and immediately discarded. 
      It is never written to a database, never saved to a file, and never written to our server logs.
    </p>

    <h3>2. No AI Training</h3>
    <p>
      Because we do not store your text, your work is never used to train Artificial Intelligence models, nor is it sent to third-party APIs like OpenAI. 
      Our analysis engine is 100% rule-based math that runs on our own server.
    </p>

    <h3>3. No Cookies or Trackers</h3>
    <p>
      We don't use marketing cookies, tracking pixels, or third-party analytics that harvest your personal data. 
    </p>

    <h3>4. Open Source</h3>
    <p>
      Don't trust us? You don't have to. The entire codebase for this tool is open-source. You can run it on your own computer completely offline.
    </p>
  </div>
);
