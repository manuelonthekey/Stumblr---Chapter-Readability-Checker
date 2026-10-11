export const HowItWorks = () => (
  <div className="max-w-3xl mx-auto glass-opaque rounded-3xl p-8 md:p-12 my-8 shadow-sm transition-all text-main">
    <h2 className="text-3xl font-serif font-bold mb-6 text-primary">How Stumblr Works</h2>
    <p className="text-lg leading-relaxed mb-8">
      Stumblr analyzes your fiction chapter using standard readability formulas 
      and a custom rule-based scoring engine to find exactly where readers might slow down.
    </p>
    
    <h3 className="text-xl font-bold font-serif mb-4 text-primary">The Hardest Sentences</h3>
    <p className="leading-relaxed mb-4">
      We don't use AI to guess what's hard. Instead, every sentence is graded from 0 to 100 based on five concrete factors:
    </p>
    <ul className="list-disc pl-6 space-y-3 mb-8">
      <li><strong className="font-bold">Length:</strong> Sentences over 25 words start to drag.</li>
      <li><strong className="font-bold">Syllable density:</strong> Too many multi-syllable words packed together.</li>
      <li><strong className="font-bold">Complex words:</strong> Words with 3 or more syllables (excluding common suffixes).</li>
      <li><strong className="font-bold">Clause density:</strong> Sentences with many commas, semicolons, and dashes.</li>
      <li><strong className="font-bold">Passive voice:</strong> A basic check for forms of "to be" followed by past participles.</li>
    </ul>

    <h3 className="text-xl font-bold font-serif mb-4 text-primary">The Formulas</h3>
    <p className="leading-relaxed mb-8">
      Your overall score is based on the <strong className="font-bold">Flesch Reading Ease</strong> formula, which is the industry standard for readability. 
      We also provide Flesch-Kincaid Grade Level, Gunning Fog, and other academic metrics for context.
    </p>

    <h3 className="text-xl font-bold font-serif mb-4 text-primary">Why "Exclude Dialogue"?</h3>
    <p className="leading-relaxed">
      In fiction, dialogue is often choppy, short, and intentionally fragmentary. This can artificially lower your reading grade, making a complex chapter look like a children's book. Checking the "Exclude Dialogue" option strips out everything inside quotation marks before running the math.
    </p>
  </div>
);
