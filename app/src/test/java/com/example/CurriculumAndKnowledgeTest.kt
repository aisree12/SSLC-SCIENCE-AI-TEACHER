package com.example

import com.example.data.curriculum.ScienceKnowledgeEngine
import com.example.data.curriculum.SyllabusRepository
import com.example.model.ScienceDivision
import com.example.model.StudyLanguage
import com.example.model.SubjectFilter
import com.example.model.TeachingMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CurriculumAndKnowledgeTest {

    @Test
    fun `local study catalogue contains 13 bilingual chapters and historical exclusions`() {
        assertEquals(13, SyllabusRepository.chapters.size)
        assertEquals(3, SyllabusRepository.rationalizedDeletions.size)
        assertTrue(SyllabusRepository.chapters.all { it.titleEn.isNotBlank() && it.titleKn.isNotBlank() })
        assertEquals(4, SyllabusRepository.getChaptersForSubject(SubjectFilter.CHEMISTRY).size)
        assertEquals(5, SyllabusRepository.getChaptersForSubject(SubjectFilter.BIOLOGY).size)
        assertEquals(4, SyllabusRepository.getChaptersForSubject(SubjectFilter.PHYSICS).size)
    }

    @Test
    fun `subject guard blocks off-topic chat and accepts English, Kannada, and Kanglish science queries`() {
        assertTrue(ScienceKnowledgeEngine.isLikelyOffTopic("Who won yesterday's IPL cricket match?"))
        assertTrue(ScienceKnowledgeEngine.isLikelyOffTopic("Write me a poem about school"))
        assertTrue(ScienceKnowledgeEngine.isLikelyOffTopic("What is the capital of France?"))
        assertFalse(ScienceKnowledgeEngine.isLikelyOffTopic("Explain this"))
        assertFalse(ScienceKnowledgeEngine.isLikelyOffTopic("Ohm's law andre enu? State its formula"))
        assertFalse(ScienceKnowledgeEngine.isLikelyOffTopic("ವಿದ್ಯುತ್ ಪ್ರವಾಹದ SI ಏಕಮಾನ ಯಾವುದು?"))

        val kanglishRetrieval = ScienceKnowledgeEngine.retrieve(
            query = "Vidyut pravaha and Ohm law andre enu?",
            selectedChapterId = "chem_reactions"
        )
        assertTrue(kanglishRetrieval.isInSyllabus)
        assertEquals(ScienceDivision.PHYSICS, kanglishRetrieval.matchedChapter.division)
        assertEquals("phy_electricity", kanglishRetrieval.matchedChapter.id)

        val offlineAnswer = ScienceKnowledgeEngine.generateOfflineFallbackAnswer(
            query = "Explain Lens formula",
            language = StudyLanguage.BILINGUAL,
            mode = TeachingMode.SIMPLE,
            retrieval = ScienceKnowledgeEngine.retrieve("Explain Lens formula", "phy_light")
        )
        assertTrue(offlineAnswer.contains("Offline Local Science Knowledge Engine"))
    }
}
