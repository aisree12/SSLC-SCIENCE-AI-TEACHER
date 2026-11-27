package com.example

import com.example.data.curriculum.SyllabusRepository
import com.example.data.video.VideoRepository
import com.example.model.VideoReviewStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VideoRepositoryTest {

    @Test
    fun `every local Science chapter has a curated YouTube lesson link`() {
        val chapterIds = SyllabusRepository.chapters.map { it.id }.toSet()
        val linkedChapterIds = VideoRepository.catalog.map { it.chapterId }.toSet()
        val topicIds = SyllabusRepository.chapters.flatMap { it.topics }.map { it.id }.toSet()
        assertEquals(chapterIds, linkedChapterIds)
        assertTrue(VideoRepository.catalog.all { it.topicId in topicIds })
        assertEquals(13, VideoRepository.catalog.size)
    }

    @Test
    fun `catalogue links are real YouTube watch URLs and are not falsely playback approved`() {
        assertTrue(VideoRepository.catalog.all {
            it.youtubeUrl.startsWith("https://www.youtube.com/watch?v=") &&
                it.youtubeUrl.substringAfter("v=").isNotBlank() &&
                it.durationLabel == "Not checked" &&
                it.lastVerifiedDate.contains("playback pending")
        })
        assertTrue(VideoRepository.catalog.all { it.reviewStatus == VideoReviewStatus.DISCOVERED })
        assertFalse(VideoRepository.catalog.any { it.channel.contains("Visual Studio", ignoreCase = true) })
    }
}
