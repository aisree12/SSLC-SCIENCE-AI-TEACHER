package com.example.data.video

import android.net.Uri
import com.example.model.YouTubeResourceType

data class ParsedYouTubeTarget(
    val videoId: String?,
    val playlistId: String?,
    val resourceType: YouTubeResourceType,
    val isPlayableInEmbed: Boolean
)

/**
 * Robust YouTube URL parser supporting standard watch URLs, Shorts, Live, Embeds,
 * youtu.be short links, playlist-only URLs, and combined v=+list= watch URLs.
 *
 * CRITICAL RULE: When both `v=VIDEO_ID` and `list=PLAYLIST_ID` exist, individual
 * `VIDEO_ID` is prioritized unless `preferPlaylistMode == true`.
 */
object YouTubeUrlParser {

    private val idPattern = Regex("^[a-zA-Z0-9_-]{6,30}$")
    private val playlistPattern = Regex("^[a-zA-Z0-9_-]{8,64}$")

    fun parse(rawUrl: String, preferPlaylistMode: Boolean = false): ParsedYouTubeTarget? {
        val trimmed = rawUrl.trim()
        if (trimmed.isEmpty()) return null
        val uri = try {
            Uri.parse(trimmed)
        } catch (_: Exception) {
            return null
        }

        val host = uri.host?.lowercase()?.removePrefix("www.")?.removePrefix("m.") ?: return null
        val isYouTubeDomain = host == "youtube.com" ||
            host == "youtu.be" ||
            host == "youtube-nocookie.com"
        if (!isYouTubeDomain) return null

        val pathSegments = uri.pathSegments ?: emptyList()
        val queryVideoId = uri.getQueryParameter("v")?.takeIf { idPattern.matches(it) }
        val queryPlaylistId = uri.getQueryParameter("list")?.takeIf { playlistPattern.matches(it) }

        // Handle youtu.be/VIDEO_ID
        if (host == "youtu.be") {
            val shortId = pathSegments.firstOrNull()?.takeIf { idPattern.matches(it) } ?: return null
            val usePlaylist = preferPlaylistMode && queryPlaylistId != null
            return ParsedYouTubeTarget(
                videoId = if (usePlaylist) null else shortId,
                playlistId = if (usePlaylist) queryPlaylistId else queryPlaylistId,
                resourceType = if (usePlaylist) YouTubeResourceType.PLAYLIST else YouTubeResourceType.VIDEO,
                isPlayableInEmbed = true
            )
        }

        val firstSeg = pathSegments.getOrNull(0)?.lowercase()
        val secondSeg = pathSegments.getOrNull(1)

        return when {
            firstSeg == "shorts" && secondSeg != null && idPattern.matches(secondSeg) -> {
                ParsedYouTubeTarget(
                    videoId = secondSeg,
                    playlistId = queryPlaylistId,
                    resourceType = YouTubeResourceType.SHORTS,
                    isPlayableInEmbed = true
                )
            }
            firstSeg == "live" && secondSeg != null && idPattern.matches(secondSeg) -> {
                ParsedYouTubeTarget(
                    videoId = secondSeg,
                    playlistId = queryPlaylistId,
                    resourceType = YouTubeResourceType.LIVE,
                    isPlayableInEmbed = true
                )
            }
            firstSeg == "embed" && secondSeg != null && idPattern.matches(secondSeg) -> {
                ParsedYouTubeTarget(
                    videoId = secondSeg,
                    playlistId = queryPlaylistId,
                    resourceType = YouTubeResourceType.EMBED,
                    isPlayableInEmbed = true
                )
            }
            firstSeg == "playlist" && queryPlaylistId != null -> {
                ParsedYouTubeTarget(
                    videoId = null,
                    playlistId = queryPlaylistId,
                    resourceType = YouTubeResourceType.PLAYLIST,
                    isPlayableInEmbed = true
                )
            }
            queryVideoId != null -> {
                // Prioritize individual videoId when both v= and list= are present, unless playlist mode requested
                val type = if (preferPlaylistMode && queryPlaylistId != null) {
                    YouTubeResourceType.PLAYLIST
                } else {
                    YouTubeResourceType.VIDEO
                }
                ParsedYouTubeTarget(
                    videoId = if (type == YouTubeResourceType.PLAYLIST) null else queryVideoId,
                    playlistId = queryPlaylistId,
                    resourceType = type,
                    isPlayableInEmbed = true
                )
            }
            queryPlaylistId != null -> {
                ParsedYouTubeTarget(
                    videoId = null,
                    playlistId = queryPlaylistId,
                    resourceType = YouTubeResourceType.PLAYLIST,
                    isPlayableInEmbed = true
                )
            }
            else -> null
        }
    }
}
