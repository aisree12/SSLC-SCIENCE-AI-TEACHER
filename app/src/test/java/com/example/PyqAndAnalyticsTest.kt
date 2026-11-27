package com.example

import com.example.data.pyq.PyqRepository
import com.example.data.curriculum.SyllabusRepository
import com.example.model.PyqExamType
import com.example.model.ScienceDivision
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PyqAndAnalyticsTest {

    @Test
    fun `pyq bank separates official keys from unkeyed preparatory papers`() {
        val all = PyqRepository.allQuestions
        assertTrue(all.size >= 38)
        assertTrue(all.count { it.paperReferenceUrl != null } >= 24)
        assertTrue(all.all { it.flaggedForReview })
        val unkeyed = all.filter { !it.hasOfficialKey }
        assertTrue(unkeyed.isNotEmpty())
        unkeyed.forEach { q ->
            assertNull(q.officialKeyAnswer)
        }
        val keyed = all.filter { it.hasOfficialKey }
        assertTrue(keyed.all { !it.officialKeyAnswer.isNullOrBlank() })
        assertTrue(all.any { it.examType == PyqExamType.PREPARATORY })
        assertTrue(all.any { it.examType == PyqExamType.MODEL_PAPER })
    }

    @Test
    fun `every PYQ is mapped to an existing chapter and topic`() {
        val chapters = SyllabusRepository.chapters
        val chapterIds = chapters.map { it.id }.toSet()
        val topicIds = chapters.flatMap { it.topics }.map { it.id }.toSet()
        assertTrue(PyqRepository.allQuestions.all {
            it.chapterId in chapterIds && it.topicId in topicIds
        })
    }

    @Test
    fun `paper archive distinguishes past exams from current model papers and answer keys`() {
        val archive = PyqRepository.paperArchive
        assertEquals(13, archive.size)
        assertTrue(archive.any { it.id == "archive_2026_exam1" && it.answerKeyArchiveUrl != null })
        assertTrue(archive.any { it.id == "archive_2026_exam2" && it.answerKeyArchiveUrl != null })
        assertTrue(archive.filter { it.id.startsWith("archive_2024") || it.id.startsWith("archive_2025") }
            .all { it.answerKeyArchiveUrl == null })
        assertTrue(archive.any { it.id == "archive_2026_27_model" })
        assertTrue(archive.all { it.paperArchiveUrl.startsWith("https://") })
    }

    @Test
    fun `pyq analytics summarizes all three science divisions and honest predictions`() {
        val summary = PyqRepository.computeAnalytics()
        assertEquals(PyqRepository.allQuestions.size, summary.totalQuestions)
        assertEquals(0, summary.verifiedOfficialKeyCount)
        assertTrue((summary.divisionCounts[ScienceDivision.PHYSICS] ?: 0) > 0)
        assertTrue((summary.divisionCounts[ScienceDivision.CHEMISTRY] ?: 0) > 0)
        assertTrue((summary.divisionCounts[ScienceDivision.BIOLOGY] ?: 0) > 0)
        assertTrue(summary.probablePredictionTopics.all { it.contains("Not guaranteed", ignoreCase = true) })
    }
}
