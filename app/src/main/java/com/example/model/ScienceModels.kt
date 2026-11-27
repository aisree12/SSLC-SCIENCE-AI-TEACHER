package com.example.model

enum class ScienceDivision(
    val displayName: String,
    val kannadaName: String
) {
    PHYSICS("Physics", "ಭೌತಶಾಸ್ತ್ರ"),
    CHEMISTRY("Chemistry", "ರಸಾಯನಶಾಸ್ತ್ರ"),
    BIOLOGY("Biology", "ಜೀವಶಾಸ್ತ್ರ")
}

enum class SubjectFilter(val label: String, val division: ScienceDivision?) {
    ALL("All Science", null),
    PHYSICS("Physics", ScienceDivision.PHYSICS),
    CHEMISTRY("Chemistry", ScienceDivision.CHEMISTRY),
    BIOLOGY("Biology", ScienceDivision.BIOLOGY)
}

enum class StudyLanguage(val label: String, val code: String) {
    ENGLISH("English", "en"),
    KANNADA("ಕನ್ನಡ (Kannada)", "kn"),
    BILINGUAL("Bilingual (EN + KN)", "en-kn")
}

enum class TeachingMode(
    val title: String,
    val shortDescription: String,
    val promptInstruction: String
) {
    SIMPLE("Simple Explanation", "Easy-to-grasp everyday language", "Explain clearly in simple language suitable for a Class 10 student."),
    DETAILED("Detailed Explanation", "In-depth textbook-level clarity", "Provide a thorough, structured textbook-level explanation covering underlying principles."),
    CLASSROOM_TEACHER("Teach Like a Classroom Teacher", "Warm interactive classroom style", "Teach step by step like an experienced Karnataka SSLC Science teacher in class, checking understanding."),
    STEP_BY_STEP("Step-by-Step Answer", "Numbered logical breakdown", "Break the answer into clear numbered steps with Given, Formula, Substitution, and Answer for numericals."),
    DIAGRAM_EXPLANATION("Diagram Explanation", "Parts, labels & drawing guide", "Explain the labelled diagram structure, key parts, functions, and exam drawing tips."),
    BOARD_EXAM("Board Exam Answer", "KSEAB scheme-of-valuation points", "Format strictly as a crisp Karnataka SSLC Board Exam scoring answer with bullet points and key terms."),
    ONE_MARK("One-Mark Revision", "1-sentence crisp recall", "Give a precise 1-mark Karnataka SSLC board-style answer in 1–2 sentences."),
    THREE_MARK("Three-Mark Answer", "6-point structured 3M answer", "Write a 3-mark KSEAB answer with 3–4 well-explained scientific points or balanced equation + explanation."),
    FIVE_MARK("Five-Mark Answer", "Long-answer / 4-5M pattern", "Write an exhaustive 4/5-mark KSEAB board answer with heading, labeled concepts, diagram points, and conclusion."),
    QUICK_REVISION("Quick Revision", "Rapid bullet summary", "Summarize key points, formulas, definitions, and exceptions in rapid-revision bullets."),
    CONCEPT_COMPARISON("Concept Comparison", "Side-by-side differences", "Present a clear point-by-point comparison table with scientific examples."),
    REAL_LIFE_APP("Real-Life Application", "Everyday Karnataka & daily life", "Connect the concept to real-life examples, technology, and daily observations."),
    PRACTICAL_ACTIVITY("Practical Activity Explanation", "Lab apparatus, procedure & result", "Explain the NCERT/KTBS textbook activity: Aim, Apparatus, Procedure, Observation, and Inference."),
    DOUBT_CLEARING("Doubt Clearing", "Resolve misconceptions directly", "Identify common student misconceptions and clear the doubt directly with examples.")
}

data class ScienceTopic(
    val id: String,
    val title: String,
    val kannadaTitle: String,
    val simpleExplanation: String,
    val detailedExplanation: String,
    val kannadaSummary: String,
    val observations: List<String>,
    val dailyLifeApplications: List<String>
)

data class DefinitionEntry(
    val term: String,
    val kannadaTerm: String,
    val definition: String,
    val kannadaDefinition: String,
    val textbookRef: String
)

data class FormulaEntry(
    val name: String,
    val formula: String,
    val variables: String,
    val siUnit: String,
    val textbookRef: String
)

data class ChemicalReactionEntry(
    val title: String,
    val balancedEquation: String,
    val reactionType: String,
    val observation: String,
    val conditions: String
)

data class ActivityEntry(
    val activityNumber: String,
    val title: String,
    val aim: String,
    val observation: String,
    val inference: String
)

data class FaqEntry(
    val question: String,
    val answer: String,
    val marksHint: String
)

data class ChapterQuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val marks: Int = 1,
    val topicId: String = ""
)

data class SyllabusChapter(
    val id: String,
    val chapterNumber: Int,
    val textbookPart: Int,
    val division: ScienceDivision,
    val titleEn: String,
    val titleKn: String,
    val introduction: String,
    val objectives: List<String>,
    val topics: List<ScienceTopic>,
    val definitions: List<DefinitionEntry>,
    val formulas: List<FormulaEntry>,
    val reactions: List<ChemicalReactionEntry>,
    val activities: List<ActivityEntry>,
    val revisionNotes: List<String>,
    val faqs: List<FaqEntry>,
    val quizQuestions: List<ChapterQuizQuestion>,
    val textbookSourceRef: String,
    val simulationIds: List<String>,
    val diagramIds: List<String>
)

data class RationalizedDeletion(
    val oldChapterTitle: String,
    val oldChapterNumber: Int,
    val status: String,
    val notes: String,
    val sourceUrl: String
)

data class SyllabusVerificationInfo(
    val academicYear: String,
    val boardName: String,
    val editionLabel: String,
    val lastVerifiedDate: String,
    val officialSources: List<String>,
    val activeChapterCount: Int,
    val statusMessage: String,
    val liveCheckSucceeded: Boolean
)

enum class TextbookMedium(val label: String) {
    ENGLISH("English Medium"),
    KANNADA("Kannada Medium")
}

enum class DownloadStatus {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    FAILED
}

data class OfficialTextbook(
    val id: String,
    val title: String,
    val kannadaTitle: String,
    val medium: TextbookMedium,
    val part: Int,
    val academicYear: String,
    val edition: String,
    val officialUrl: String,
    val portalUrl: String,
    val sourceAuthority: String,
    val chapterRangeLabel: String,
    val estimatedSizeMb: String,
    val status: DownloadStatus = DownloadStatus.NOT_DOWNLOADED,
    val progressPercent: Int = 0,
    val localFilePath: String? = null,
    val sha256Checksum: String? = null,
    val lastReadPage: Int = 0,
    val bookmarkedPages: List<Int> = emptyList(),
    val statusNote: String = ""
)

enum class PyqExamType(val label: String) {
    MAIN("Main Exam"),
    EXAM_1("Exam 1"),
    EXAM_2("Exam 2"),
    EXAM_3("Exam 3"),
    SUPPLEMENTARY("Supplementary"),
    PREPARATORY("State Preparatory"),
    MODEL_PAPER("Official Model Paper")
}

enum class QuestionType(val label: String) {
    MCQ("MCQ"),
    DEFINITION("Definition"),
    DIFFERENCE("Difference"),
    EXPLAIN_WHY("Explain Why"),
    NUMERICAL("Numerical"),
    DIAGRAM("Diagram"),
    CHEMICAL_EQUATION("Chemical Equation"),
    DESCRIPTIVE("Descriptive")
}

data class PyqQuestion(
    val id: String,
    val year: Int,
    val examType: PyqExamType,
    val paperCode: String,
    val medium: TextbookMedium,
    val division: ScienceDivision,
    val chapterId: String,
    val topicId: String,
    val topicName: String,
    val marks: Int,
    val questionType: QuestionType,
    val questionNumber: String,
    val questionText: String,
    val kannadaQuestionText: String,
    val officialKeyAnswer: String?,
    val hasOfficialKey: Boolean,
    val officialSource: String,
    val officialPdfUrl: String,
    val extractionConfidence: Float,
    val flaggedForReview: Boolean = false
)

enum class VideoCategory(val label: String) {
    FULL_CHAPTER("Full Chapter"),
    TOPIC_WISE("Topic-wise"),
    PHYSICS_NUMERICALS("Physics Numericals"),
    CHEMICAL_REACTIONS("Chemical Reactions"),
    BIOLOGY_DIAGRAMS("Biology Diagrams"),
    BOARD_QUESTIONS("Board Questions"),
    PYQ_SOLUTIONS("PYQ Solutions"),
    QUICK_REVISION("Quick Revision"),
    KANNADA_EXPLANATION("Kannada Explanation"),
    ENGLISH_EXPLANATION("English Explanation")
}

enum class VideoReviewStatus(val label: String) {
    APPROVED("Verified"),
    NEEDS_REVIEW("Needs Review"),
    DISCOVERED("Discovered"),
    REJECTED("Rejected"),
    UNAVAILABLE("Unavailable")
}

enum class YouTubeResourceType {
    VIDEO,
    SHORTS,
    LIVE,
    EMBED,
    PLAYLIST
}

data class ScienceVideo(
    val id: String,
    val chapterId: String,
    val topicId: String,
    val title: String,
    val channel: String,
    val language: StudyLanguage,
    val youtubeUrl: String,
    val category: VideoCategory,
    val resourceType: YouTubeResourceType,
    val reviewStatus: VideoReviewStatus,
    val lastVerifiedDate: String,
    val durationLabel: String
)

enum class SimulationCategory(val label: String) {
    PHYSICS("Physics Lab"),
    CHEMISTRY("Chemistry Lab"),
    BIOLOGY("Biology Models")
}

data class ScienceSimulation(
    val id: String,
    val chapterId: String,
    val category: SimulationCategory,
    val titleEn: String,
    val titleKn: String,
    val summaryEn: String,
    val summaryKn: String,
    val equationOrLaw: String
)

data class DiagramHotspot(
    val id: String,
    val labelEn: String,
    val labelKn: String,
    val functionDesc: String,
    val normalizedX: Float,
    val normalizedY: Float
)

data class ScienceDiagram(
    val id: String,
    val chapterId: String,
    val division: ScienceDivision,
    val titleEn: String,
    val titleKn: String,
    val drawingSteps: List<String>,
    val examTip: String,
    val hotspots: List<DiagramHotspot>
)
