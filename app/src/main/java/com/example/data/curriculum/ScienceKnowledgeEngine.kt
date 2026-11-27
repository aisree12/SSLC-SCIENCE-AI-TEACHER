package com.example.data.curriculum

import com.example.data.pyq.PyqRepository
import com.example.model.StudyLanguage
import com.example.model.SyllabusChapter
import com.example.model.TeachingMode

data class KnowledgeRetrievalResult(
    val matchedChapter: SyllabusChapter,
    val contextBlock: String,
    val sourceCitation: String,
    val detectedQuestionType: String,
    val isInSyllabus: Boolean
)

object ScienceKnowledgeEngine {

    private val offTopicKeywords = listOf(
        "cricket", "ipl", "movie", "actor", "actress", "song lyrics", "politics", "election",
        "stock market", "crypto", "bitcoin", "video game", "pubg", "bgmi", "instagram",
        "recipe for biryani", "joke", "horoscope", "astrology", "love letter"
    )

    private val kanglishScienceHints = mapOf(
        "vidyut" to "electricity current",
        "pravaha" to "electric current",
        "belaku" to "light reflection refraction",
        "kannu" to "human eye",
        "amla" to "acids bases",
        "pratyamla" to "bases salts",
        "loha" to "metals non-metals",
        "karban" to "carbon compounds",
        "jeevakriye" to "life processes",
        "hrudaya" to "heart double circulation",
        "medulu" to "brain nervous system",
        "anuvamshikate" to "heredity mendel",
        "parisara" to "our environment ecosystem",
        "andre enu" to "what is definition",
        "vyatyasa" to "difference between",
        "hege" to "how",
        "yake" to "why",
        "sutra" to "formula",
        "samikarana" to "equation",
        "chitra" to "diagram"
    )

    fun normalizeQueryForRetrieval(rawQuery: String): String {
        var normalized = rawQuery.lowercase()
        for ((k, v) in kanglishScienceHints) {
            if (normalized.contains(k)) {
                normalized = "$normalized $v"
            }
        }
        return normalized
    }

    fun isLikelyOffTopic(query: String): Boolean {
        val q = query.lowercase().trim()
        if (q.length < 3) return false
        val hasOffTopic = offTopicKeywords.any { q.contains(it) }
        val hasScienceCue = SyllabusRepository.chapters.any { ch ->
            q.contains(ch.titleEn.lowercase()) ||
                ch.topics.any { t -> t.title.lowercase().split(" ").any { w -> w.length > 4 && q.contains(w) } }
        } || listOf(
            "science", "physics", "chemistry", "biology", "atom", "acid", "base", "salt",
            "metal", "carbon", "ohm", "volt", "ampere", "current", "magnet", "lens", "mirror",
            "eye", "heart", "kidney", "nephron", "neuron", "brain", "mendel", "dna", "ozone",
            "ph", "sslc", "kseab", "vigyana", "ವಿಜ್ಞಾನ", "ವಿದ್ಯುತ್", "ಬೆಳಕು", "ಆಮ್ಲ"
        ).any { q.contains(it) }
        return hasOffTopic && !hasScienceCue
    }

    fun detectQuestionType(query: String): String {
        val q = normalizeQueryForRetrieval(query)
        return when {
            q.contains("difference") || q.contains("compare") || q.contains("distinguish") || q.contains("vyatyasa") -> "Difference Question"
            q.contains("calculate") || q.contains("numerical") || q.contains("find the") || Regex("\\d+\\s*(v|a|ohm|w|cm|m|j|kwh)").containsMatchIn(q) -> "Numerical Problem"
            q.contains("equation") || q.contains("balance") || q.contains("react") || q.contains("samikarana") -> "Chemical Equation"
            q.contains("diagram") || q.contains("draw") || q.contains("label") || q.contains("chitra") -> "Diagram Question"
            q.contains("why") || q.contains("explain why") || q.contains("give reason") || q.contains("yake") -> "Explain-Why Question"
            q.contains("define") || q.contains("what is") || q.contains("andre enu") || q.contains("ಎಂದರೇನು") -> "Definition Question"
            else -> "Concept / Board Exam Question"
        }
    }

    fun retrieve(query: String, selectedChapterId: String): KnowledgeRetrievalResult {
        val normalized = normalizeQueryForRetrieval(query)
        val tokens = normalized.split(Regex("[^\\p{L}\\p{N}]+")).filter { it.length >= 3 }

        val bestChapter = SyllabusRepository.chapters.maxByOrNull { ch ->
            var score = if (ch.id == selectedChapterId) 2 else 0
            val blob = buildString {
                append(ch.titleEn).append(' ')
                append(ch.titleKn).append(' ')
                ch.topics.forEach { append(it.title).append(' ').append(it.simpleExplanation).append(' ') }
                ch.definitions.forEach { append(it.term).append(' ').append(it.definition).append(' ') }
                ch.formulas.forEach { append(it.name).append(' ').append(it.formula).append(' ') }
                ch.reactions.forEach { append(it.title).append(' ').append(it.balancedEquation).append(' ') }
            }.lowercase()
            tokens.forEach { token ->
                if (blob.contains(token)) score += 3
            }
            score
        } ?: SyllabusRepository.getChapterById(selectedChapterId)

        val matchedPyqs = PyqRepository.allQuestions
            .filter { it.chapterId == bestChapter.id }
            .take(2)

        val contextBlock = buildString {
            appendLine("=== VERIFIED KARNATAKA SSLC SYLLABUS CONTEXT ===")
            appendLine("Chapter ${bestChapter.chapterNumber} (${bestChapter.division.displayName}): ${bestChapter.titleEn} / ${bestChapter.titleKn}")
            appendLine("Official Reference: ${bestChapter.textbookSourceRef}")
            appendLine("Summary: ${bestChapter.introduction}")
            appendLine("Key Topics:")
            bestChapter.topics.forEach { t ->
                appendLine("- ${t.title} (${t.kannadaTitle}): ${t.detailedExplanation}")
            }
            if (bestChapter.definitions.isNotEmpty()) {
                appendLine("Definitions:")
                bestChapter.definitions.forEach { d ->
                    appendLine("- ${d.term} (${d.kannadaTerm}): ${d.definition} [${d.textbookRef}]")
                }
            }
            if (bestChapter.formulas.isNotEmpty()) {
                appendLine("Formulas & Units:")
                bestChapter.formulas.forEach { f ->
                    appendLine("- ${f.name}: ${f.formula} (${f.variables}; Unit: ${f.siUnit})")
                }
            }
            if (bestChapter.reactions.isNotEmpty()) {
                appendLine("Balanced Chemical Equations:")
                bestChapter.reactions.forEach { r ->
                    appendLine("- ${r.title}: ${r.balancedEquation} | Type: ${r.reactionType} | Observation: ${r.observation}")
                }
            }
            if (matchedPyqs.isNotEmpty()) {
                appendLine("Related KSEAB PYQs:")
                matchedPyqs.forEach { p ->
                    appendLine("- [${p.year} ${p.examType.label}, ${p.marks}M]: ${p.questionText}")
                }
            }
        }

        val citation = "${bestChapter.textbookSourceRef} — ${bestChapter.titleEn}"
        return KnowledgeRetrievalResult(
            matchedChapter = bestChapter,
            contextBlock = contextBlock,
            sourceCitation = citation,
            detectedQuestionType = detectQuestionType(query),
            isInSyllabus = !isLikelyOffTopic(query)
        )
    }

    fun buildGeminiSystemPrompt(
        language: StudyLanguage,
        mode: TeachingMode,
        questionType: String,
        ragContext: String
    ): String {
        val langRule = when (language) {
            StudyLanguage.ENGLISH -> "Respond in clear academic English. Include Kannada scientific terms in brackets only if helpful."
            StudyLanguage.KANNADA -> "Respond in natural, accurate Karnataka SSLC textbook Kannada (ಕನ್ನಡ), keeping standard chemical symbols, SI units, and formulas clearly readable."
            StudyLanguage.BILINGUAL -> "Respond bilingually: give clear English explanations paired with accurate Kannada scientific terms and a concise Kannada summary so both mediums benefit."
        }
        val formattingRule = when (questionType) {
            "Definition Question" -> "Format with: 1) Precise Definition, 2) Key Scientific Terms, 3) 1–2 Mark Board-Ready Sentence."
            "Difference Question" -> "Format as a clear point-by-point Comparison Table with scientific examples."
            "Numerical Problem" -> "Format strictly as: Given -> Required -> Formula -> Substitution -> Calculation -> Final Answer with SI Unit."
            "Chemical Equation" -> "Format with: Reactants -> Products -> Balanced Chemical Equation with state symbols -> Reaction Type -> Observation."
            "Diagram Question" -> "Format with: Diagram Title -> Key Labels & Functions -> Step-by-Step Exam Drawing Tips."
            "Explain-Why Question" -> "Format with: Scientific Principle -> Step-by-Step Reasoning -> Board-Exam Conclusion."
            else -> "Format cleanly with headings, bullet points, formulas/equations where relevant, and exam tips."
        }
        return """
You are the Karnataka SSLC Science AI Teacher exclusively for Class 10 (SSLC) Physics, Chemistry, and Biology under KSEAB & KTBS.
STRICT BOUNDARIES:
1. Only answer questions related to Karnataka SSLC Class 10 Science (13 rationalized chapters). If a question is off-topic (sports, movies, politics, coding, general chat), politely decline and guide the student back to SSLC Science chapters.
2. Understand English, Kannada (ಕನ್ನಡ), and Kanglish (Kannada typed in English letters, e.g. 'Current andre enu?').
3. Never fabricate scientific facts or claim an AI explanation is an official KSEAB key answer. Label your response clearly as an AI Teacher explanation grounded in the verified textbook context below.
4. Teaching Mode: ${mode.title} — ${mode.promptInstruction}
5. Question Type Detected: $questionType — $formattingRule
6. Language Preference: $langRule

$ragContext
        """.trimIndent()
    }

    fun generateOfflineFallbackAnswer(
        query: String,
        language: StudyLanguage,
        mode: TeachingMode,
        retrieval: KnowledgeRetrievalResult
    ): String {
        if (!retrieval.isInSyllabus) {
            return if (language == StudyLanguage.KANNADA) {
                "ನಾನು ಕರ್ನಾಟಕ SSLC 10ನೇ ತರಗತಿಯ ವಿಜ್ಞಾನ (ಭೌತಶಾಸ್ತ್ರ, ರಸಾಯನಶಾಸ್ತ್ರ, ಜೀವಶಾಸ್ತ್ರ) AI ಶಿಕ್ಷಕನಾಗಿದ್ದೇನೆ. ದಯವಿಟ್ಟು ನಿಮ್ಮ SSLC ವಿಜ್ಞಾನ ಪಠ್ಯಕ್ರಮಕ್ಕೆ ಸಂಬಂಧಿಸಿದ ಪ್ರಶ್ನೆಗಳನ್ನು ಕೇಳಿ."
            } else {
                "I am your Karnataka SSLC Class 10 Science AI Teacher. Please ask questions from Physics, Chemistry, or Biology in the SSLC Science syllabus."
            }
        }
        val ch = retrieval.matchedChapter
        val normalized = normalizeQueryForRetrieval(query)
        val matchingTopic = ch.topics.firstOrNull { t ->
            normalized.split(" ").any { w -> w.length > 3 && t.title.lowercase().contains(w) }
        } ?: ch.topics.firstOrNull()

        return buildString {
            appendLine("[Offline Local Science Knowledge Engine — Verified Syllabus Notes]")
            appendLine("Mode: ${mode.title} | Type: ${retrieval.detectedQuestionType}")
            appendLine("Chapter ${ch.chapterNumber}: ${ch.titleEn} (${ch.titleKn})")
            appendLine()
            if (matchingTopic != null) {
                if (language == StudyLanguage.KANNADA) {
                    appendLine("• ${matchingTopic.kannadaTitle}: ${matchingTopic.kannadaSummary}")
                    appendLine("• ವಿವರಣೆ: ${matchingTopic.simpleExplanation}")
                } else {
                    appendLine("Topic: ${matchingTopic.title}")
                    appendLine(if (mode == TeachingMode.SIMPLE || mode == TeachingMode.ONE_MARK) matchingTopic.simpleExplanation else matchingTopic.detailedExplanation)
                    if (language == StudyLanguage.BILINGUAL) {
                        appendLine("ಕನ್ನಡ ಸಾರಾಂಶ: ${matchingTopic.kannadaSummary}")
                    }
                }
                appendLine()
            }
            if (ch.definitions.isNotEmpty()) {
                appendLine("Key Definitions:")
                ch.definitions.take(2).forEach {
                    appendLine("- ${it.term} (${it.kannadaTerm}): ${if (language == StudyLanguage.KANNADA) it.kannadaDefinition else it.definition}")
                }
                appendLine()
            }
            if (ch.formulas.isNotEmpty()) {
                appendLine("Formulas & SI Units:")
                ch.formulas.take(3).forEach {
                    appendLine("- ${it.name}: ${it.formula} [Unit: ${it.siUnit}]")
                }
                appendLine()
            }
            if (ch.reactions.isNotEmpty()) {
                appendLine("Key Balanced Equations:")
                ch.reactions.take(2).forEach {
                    appendLine("- ${it.title}: ${it.balancedEquation} (${it.reactionType})")
                }
                appendLine()
            }
            appendLine("Source: ${retrieval.sourceCitation}")
            appendLine("Note: Connect to internet for full conversational Gemini AI Teacher reasoning.")
        }
    }
}
