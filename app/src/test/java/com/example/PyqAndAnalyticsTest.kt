package com.example

import com.example.data.pyq.PyqRepository
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
        assertTrue(all.size >= 19)
        assertTrue(all.count { it.paperReferenceUrl != null } >= 5)
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
