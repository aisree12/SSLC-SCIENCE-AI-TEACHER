package com.example

import com.example.data.video.YouTubeUrlParser
import com.example.model.YouTubeResourceType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class YouTubeUrlParserTest {

    @Test
    fun `standard watch url extracts videoId`() {
        val parsed = YouTubeUrlParser.parse("https://www.youtube.com/watch?v=2S6e11NBwiw")
        assertNotNull(parsed)
        assertEquals("2S6e11NBwiw", parsed?.videoId)
        assertEquals(YouTubeResourceType.VIDEO, parsed?.resourceType)
    }

    @Test
    fun `combined watch and playlist url prioritizes videoId by default and playlist when requested`() {
        val url = "https://www.youtube.com/watch?v=HsLLq6Rm5tU&list=PL1234567890"
        val singleVideo = YouTubeUrlParser.parse(url, preferPlaylistMode = false)
        assertNotNull(singleVideo)
        assertEquals("HsLLq6Rm5tU", singleVideo?.videoId)
        assertEquals(YouTubeResourceType.VIDEO, singleVideo?.resourceType)

        val playlist = YouTubeUrlParser.parse(url, preferPlaylistMode = true)
        assertNotNull(playlist)
        assertNull(playlist?.videoId)
        assertEquals("PL1234567890", playlist?.playlistId)
        assertEquals(YouTubeResourceType.PLAYLIST, playlist?.resourceType)
    }

    @Test
    fun `short, shorts, live, embed and playlist urls parse cleanly`() {
        assertEquals("abcDEF12345", YouTubeUrlParser.parse("https://youtu.be/abcDEF12345")?.videoId)
        assertEquals(YouTubeResourceType.SHORTS, YouTubeUrlParser.parse("https://youtube.com/shorts/xyz12345678")?.resourceType)
        assertEquals(YouTubeResourceType.LIVE, YouTubeUrlParser.parse("https://m.youtube.com/live/live123456")?.resourceType)
        assertEquals(YouTubeResourceType.EMBED, YouTubeUrlParser.parse("https://www.youtube-nocookie.com/embed/emb1234567")?.resourceType)
        assertEquals(
            YouTubeResourceType.PLAYLIST,
            YouTubeUrlParser.parse("https://www.youtube.com/playlist?list=PLABCDEF12345")?.resourceType
        )
    }

    @Test
    fun `non-youtube or invalid urls return null`() {
        assertNull(YouTubeUrlParser.parse("https://example.com/watch?v=2S6e11NBwiw"))
        assertNull(YouTubeUrlParser.parse(""))
    }
}
