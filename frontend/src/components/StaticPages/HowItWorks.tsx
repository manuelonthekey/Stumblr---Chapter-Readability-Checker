export const HowItWorks = () => (
  <div className="max-w-3xl mx-auto bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-8 my-8 prose dark:prose-invert">
    <h2>How ChapterCheck Works</h2>
    <p>
      ChapterCheck analyzes your fiction chapter using standard readability formulas 
      and a custom rule-based scoring engine to find exactly where readers might slow down.
    </p>
    
    <h3>The Hardest Sentences</h3>
    <p>
      We don't use AI to guess what's hard. Instead, every sentence is graded from 0 to 100 based on five concrete factors:
    </p>
    <ul>
      <li><strong>Length:</strong> Sentences over 25 words start to drag.</li>
      <li><strong>Syllable density:</strong> Too many multi-syllable words packed together.</li>
      <li><strong>Complex words:</strong> Words with 3 or more syllables (excluding common suffixes).</li>
      <li><strong>Clause density:</strong> Sentences with many commas, semicolons, and dashes.</li>
      <li><strong>Passive voice:</strong> A basic check for forms of "to be" followed by past participles.</li>
    </ul>

    <h3>The Formulas</h3>
    <p>
      Your overall score is based on the <strong>Flesch Reading Ease</strong> formula, which is the industry standard for readability. 
      We also provide Flesch-Kincaid Grade Level, Gunning Fog, and other academic metrics for context.
    </p>

    <h3>Why "Exclude Dialogue"?</h3>
    <p>
      In fiction, dialogue is often choppy, short, and intentionally fragmentary. This can artificially lower your reading grade, making a complex chapter look like a children's book. Checking the "Exclude Dialogue" option strips out everything inside quotation marks before running the math.
    </p>
  </div>
);
