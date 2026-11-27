package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.video.YouTubeUrlParser
import com.example.model.ScienceVideo
import com.example.model.VideoReviewStatus
import com.example.model.YouTubeResourceType

@Composable
fun VideoResourceCard(
    video: ScienceVideo,
    onWatchClick: (ScienceVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(video.category.label, style = MaterialTheme.typography.labelSmall) }
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (video.reviewStatus) {
                        VideoReviewStatus.APPROVED -> MaterialTheme.colorScheme.secondaryContainer
                        VideoReviewStatus.NEEDS_REVIEW -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = video.reviewStatus.label,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${video.channel} • ${video.durationLabel} • ${video.language.label}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onWatchClick(video) },
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("watch_video_${video.id}")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Watch In App")
                }
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.youtubeUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("open_youtube_${video.id}")
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Open on YouTube", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

private class YouTubeJsBridge(
    private val onReady: () -> Unit,
    private val onPlayerError: (Int) -> Unit,
    private val onStateChanged: (Int) -> Unit,
    private val onJsError: (String) -> Unit,
    private val onAutoplayBlocked: () -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun notifyReady() {
        mainHandler.post { onReady() }
    }

    @JavascriptInterface
    fun notifyError(code: Int) {
        mainHandler.post { onPlayerError(code) }
    }

    @JavascriptInterface
    fun notifyState(state: Int) {
        mainHandler.post { onStateChanged(state) }
    }

    @JavascriptInterface
    fun notifyJsError(message: String) {
        mainHandler.post { onJsError(message.take(180)) }
    }

    @JavascriptInterface
    fun notifyAutoplayBlocked() {
        mainHandler.post { onAutoplayBlocked() }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlayerDialog(
    video: ScienceVideo,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var preferPlaylistMode by remember(video.id) {
        mutableStateOf(video.resourceType == YouTubeResourceType.PLAYLIST)
    }
    val parsed = remember(video.youtubeUrl, preferPlaylistMode) {
        YouTubeUrlParser.parse(video.youtubeUrl, preferPlaylistMode)
    }
    var isReady by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var playbackNotice by remember { mutableStateOf<String?>(null) }
    var reloadNonce by remember { mutableIntStateOf(0) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var customFullscreenView by remember { mutableStateOf<View?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 20.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${video.channel} • ${video.category.label}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_video_player")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close player")
                    }
                }

                if (parsed?.playlistId != null && parsed.videoId != null) {
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = !preferPlaylistMode,
                            onClick = {
                                preferPlaylistMode = false
                                reloadNonce++
                            },
                            label = { Text("Single Video") }
                        )
                        FilterChip(
                            selected = preferPlaylistMode,
                            onClick = {
                                preferPlaylistMode = true
                                reloadNonce++
                            },
                            label = { Text("Playlist Mode") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentAlignment = Alignment.Center
                ) {
                    if (parsed == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Invalid or unsupported YouTube URL.", style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        val html = remember(parsed, reloadNonce) {
                            buildYouTubeIframeHtml(
                                videoId = parsed.videoId,
                                playlistId = if (preferPlaylistMode) parsed.playlistId else null
                            )
                        }
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    layoutParams = FrameLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.mediaPlaybackRequiresUserGesture = false
                                    settings.javaScriptCanOpenWindowsAutomatically = true
                                    setLayerType(View.LAYER_TYPE_HARDWARE, null)
                                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                                    webChromeClient = object : WebChromeClient() {
                                        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                                            customFullscreenView = view
                                        }

                                        override fun onHideCustomView() {
                                            customFullscreenView = null
                                        }
                                    }
                                    webViewClient = object : WebViewClient() {
                                        override fun onReceivedError(
                                            view: WebView?,
                                            request: WebResourceRequest?,
                                            error: WebResourceError?
                                        ) {
                                            if (request?.isForMainFrame == true) {
                                                errorMessage = "Video player error. Check internet or open directly on YouTube."
                                            }
                                        }
                                    }
                                    addJavascriptInterface(
                                        YouTubeJsBridge(
                                            onReady = { isReady = true },
                                            onPlayerError = { code ->
                                                errorMessage = when (code) {
                                                    2 -> "YouTube rejected the video ID or player parameters (Code 2)."
                                                    5 -> "YouTube HTML5 playback failed on this device (Code 5)."
                                                    100 -> "Video unavailable or private (Code 100)."
                                                    101, 150 -> "Channel owner disabled embedded playback (Code $code)."
                                                    153 -> "YouTube did not receive a valid referrer or client identity (Code 153)."
                                                    else -> "Playback error (Code $code)."
                                                }
                                            },
                                            onStateChanged = { st -> isPlaying = (st == 1) },
                                            onJsError = { message -> errorMessage = "Player script error: $message" },
                                            onAutoplayBlocked = { playbackNotice = "Autoplay was blocked. Tap Play to start the video." }
                                        ),
                                        "AndroidYouTube"
                                    )
                                    // Use a stable HTTPS app origin for the YouTube IFrame API.
                                    loadDataWithBaseURL("https://appassets.androidplatform.net", html, "text/html", "UTF-8", null)
                                    webViewRef = this
                                }
                            },
                            update = { wv ->
                                webViewRef = wv
                            }
                        )

                        if (!isReady && errorMessage == null) {
                            CircularProgressIndicator()
                        }

                        if (errorMessage != null) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(errorMessage.orEmpty(), style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(onClick = {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video.youtubeUrl)))
                                    }) {
                                        Text("Open on YouTube")
                                    }
                                }
                            }
                        }
                    }
                }

                if (playbackNotice != null) {
                    Text(
                        playbackNotice.orEmpty(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { webViewRef?.evaluateJavascript("seekBy(-10);", null) }) {
                        Icon(Icons.Default.FastRewind, contentDescription = "Rewind 10s")
                    }
                    IconButton(onClick = {
                        if (isPlaying) {
                            webViewRef?.evaluateJavascript("pauseVideo();", null)
                        } else {
                            webViewRef?.evaluateJavascript("playVideo();", null)
                        }
                    }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play"
                        )
                    }
                    IconButton(onClick = { webViewRef?.evaluateJavascript("seekBy(10);", null) }) {
                        Icon(Icons.Default.FastForward, contentDescription = "Forward 10s")
                    }
                    IconButton(onClick = {
                        isMuted = !isMuted
                        webViewRef?.evaluateJavascript(if (isMuted) "muteVideo();" else "unMuteVideo();", null)
                    }) {
                        Icon(
                            imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = if (isMuted) "Unmute" else "Mute"
                        )
                    }
                    IconButton(onClick = {
                        errorMessage = null
                        isReady = false
                        reloadNonce++
                        webViewRef?.reload()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Retry video")
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video.youtubeUrl)))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open on YouTube App / Browser")
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
        }
    }
}

private fun buildYouTubeIframeHtml(videoId: String?, playlistId: String?): String {
    val playerConfig = if (!playlistId.isNullOrBlank()) {
        "playerVars: { listType: 'playlist', list: '$playlistId', playsinline: 1, rel: 0, modestbranding: 1, enablejsapi: 1, origin: 'https://appassets.androidplatform.net' }"
    } else {
        "videoId: '${videoId.orEmpty()}', playerVars: { playsinline: 1, rel: 0, modestbranding: 1, enablejsapi: 1, origin: 'https://appassets.androidplatform.net' }"
    }
    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <meta name="referrer" content="origin">
          <style>
            html, body, #player { margin: 0; padding: 0; width: 100%; height: 100%; background-color: #000000; overflow: hidden; }
          </style>
        </head>
        <body>
          <div id="player"></div>
          <script src="https://www.youtube.com/iframe_api"></script>
          <script>
            var player;
            window.onerror = function(message) {
              try { AndroidYouTube.notifyJsError(String(message)); } catch (ignored) {}
            };
            function onYouTubeIframeAPIReady() {
              player = new YT.Player('player', {
                width: '100%',
                height: '100%',
                $playerConfig,
                events: {
                  'onReady': function() { AndroidYouTube.notifyReady(); },
                  'onError': function(e) { AndroidYouTube.notifyError(e.data); },
                  'onStateChange': function(e) { AndroidYouTube.notifyState(e.data); },
                  'onAutoplayBlocked': function() { AndroidYouTube.notifyAutoplayBlocked(); }
                }
              });
            }
            function playVideo() { if (player && player.playVideo) player.playVideo(); }
            function pauseVideo() { if (player && player.pauseVideo) player.pauseVideo(); }
            function muteVideo() { if (player && player.mute) player.mute(); }
            function unMuteVideo() { if (player && player.unMute) player.unMute(); }
            function seekBy(delta) {
              if (player && player.getCurrentTime && player.seekTo) {
                player.seekTo(Math.max(0, player.getCurrentTime() + delta), true);
              }
            }
          </script>
        </body>
        </html>
    """.trimIndent()
}
