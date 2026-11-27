package com.example.ui.components

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.OfficialTextbook
import java.io.File

@Composable
fun OfflinePdfReaderDialog(
    book: OfficialTextbook,
    onProgressChanged: (page: Int, bookmarks: List<Int>) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var currentPage by remember(book.id) { mutableIntStateOf(book.lastReadPage.coerceAtLeast(0)) }
    var bookmarks by remember(book.id) { mutableStateOf(book.bookmarkedPages) }
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var pageCount by remember { mutableIntStateOf(0) }
    var renderedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var renderError by remember { mutableStateOf<String?>(null) }

    val filePath = book.localFilePath
    val pdfHolder = remember(filePath) {
        if (filePath.isNullOrBlank()) {
            null
        } else {
            try {
                val file = File(filePath)
                if (!file.exists()) {
                    null
                } else {
                    val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                    val renderer = PdfRenderer(pfd)
                    pageCount = renderer.pageCount
                    pfd to renderer
                }
            } catch (e: Exception) {
                renderError = e.localizedMessage ?: "Could not open PDF locally."
                null
            }
        }
    }

    DisposableEffect(pdfHolder) {
        onDispose {
            try {
                pdfHolder?.second?.close()
                pdfHolder?.first?.close()
            } catch (_: Exception) {
            }
        }
    }

    LaunchedEffect(pdfHolder, currentPage, zoomScale) {
        val renderer = pdfHolder?.second ?: return@LaunchedEffect
        if (renderer.pageCount <= 0) return@LaunchedEffect
        val safePage = currentPage.coerceIn(0, renderer.pageCount - 1)
        if (safePage != currentPage) currentPage = safePage
        try {
            renderer.openPage(safePage).use { page ->
                val targetW = (page.width * 1.5f * zoomScale).toInt().coerceIn(300, 2400)
                val targetH = (page.height * 1.5f * zoomScale).toInt().coerceIn(400, 3200)
                val bmp = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
                bmp.eraseColor(android.graphics.Color.WHITE)
                page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                renderedBitmap = bmp
            }
            onProgressChanged(safePage, bookmarks)
        } catch (e: Exception) {
            renderError = e.localizedMessage ?: "Failed to render PDF page."
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(tonalElevation = 4.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(book.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (pageCount > 0) "Page ${currentPage + 1} of $pageCount • Offline Reader" else "Offline PDF Reader",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { zoomScale = (zoomScale - 0.25f).coerceAtLeast(0.75f) }) {
                            Icon(Icons.Default.ZoomOut, contentDescription = "Zoom out")
                        }
                        IconButton(onClick = { zoomScale = (zoomScale + 0.25f).coerceAtMost(2.5f) }) {
                            Icon(Icons.Default.ZoomIn, contentDescription = "Zoom in")
                        }
                        val isBookmarked = bookmarks.contains(currentPage)
                        IconButton(
                            onClick = {
                                val next = if (isBookmarked) bookmarks - currentPage else (bookmarks + currentPage).distinct().sorted()
                                bookmarks = next
                                onProgressChanged(currentPage, next)
                            },
                            modifier = Modifier.testTag("toggle_pdf_bookmark")
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark page"
                            )
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_pdf_reader")) {
                            Icon(Icons.Default.Close, contentDescription = "Close reader")
                        }
                    }
                }

                if (bookmarks.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        bookmarks.forEach { pg ->
                            AssistChip(
                                onClick = { currentPage = pg },
                                label = { Text("Bookmark p.${pg + 1}") }
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        renderedBitmap != null -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .horizontalScroll(rememberScrollState()),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = renderedBitmap!!.asImageBitmap(),
                                    contentDescription = "Textbook page ${currentPage + 1}",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                        else -> {
                            Surface(
                                modifier = Modifier.padding(20.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = renderError ?: "Local textbook file is not available yet.",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(onClick = {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(book.portalUrl)))
                                    }) {
                                        Text("Open Official KTBS Portal")
                                    }
                                }
                            }
                        }
                    }
                }

                if (pageCount > 0) {
                    Surface(tonalElevation = 4.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { if (currentPage > 0) currentPage-- },
                                enabled = currentPage > 0,
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Previous")
                            }
                            Text(
                                text = "${currentPage + 1} / $pageCount",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Button(
                                onClick = { if (currentPage < pageCount - 1) currentPage++ },
                                enabled = currentPage < pageCount - 1,
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Text("Next")
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}
