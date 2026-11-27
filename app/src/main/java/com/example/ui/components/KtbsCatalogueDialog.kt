package com.example.ui.components

import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.OfficialTextbook
import com.example.data.textbook.TextbookUrlPolicy

@Composable
fun KtbsCatalogueDialog(
    book: OfficialTextbook,
    onDownloadPdf: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var capturedPdfUrl by remember(book.id) { mutableStateOf<String?>(null) }
    var pageTitle by remember(book.id) { mutableStateOf("Official KTBS catalogue") }
    var pageProgress by remember(book.id) { mutableStateOf(0) }
    var blockedExternal by remember(book.id) { mutableStateOf(false) }
    val webViewRef = remember { java.util.concurrent.atomic.AtomicReference<WebView?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Find textbook on KTBS", style = MaterialTheme.typography.titleMedium)
                        Text(book.title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close catalogue")
                    }
                }
                if (pageProgress in 1..99) {
                    LinearProgressIndicator(
                        progress = { pageProgress / 100f },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = if (blockedExternal) "An external link was blocked. Continue only on the official Karnataka textbook portal." else pageTitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                )
                AndroidView(
                    modifier = Modifier.weight(1f).fillMaxWidth().testTag("ktbs_catalogue_webview"),
                    factory = { context ->
                        WebView(context).apply {
                            webViewRef.set(this)
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = false
                            settings.allowContentAccess = false
                            settings.javaScriptCanOpenWindowsAutomatically = false
                            settings.setSupportMultipleWindows(false)
                            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
                            if (android.os.Build.VERSION.SDK_INT >= 26) settings.safeBrowsingEnabled = true
                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    pageProgress = newProgress
                                }
                                override fun onReceivedTitle(view: WebView?, title: String?) {
                                    if (!title.isNullOrBlank()) pageTitle = title.take(90)
                                }
                            }
                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                                    val uri = request.url
                                    if (TextbookUrlPolicy.isPdfUrl(uri.toString())) {
                                        if (TextbookUrlPolicy.isTrustedPdfUrl(uri.toString())) capturedPdfUrl = uri.toString()
                                        else blockedExternal = true
                                        return true
                                    }
                                    if (!uri.scheme.equals("https", true) || !TextbookUrlPolicy.isTrustedPortalHost(uri.host)) {
                                        blockedExternal = true
                                        return true
                                    }
                                    return false
                                }
                            }
                            setDownloadListener { url, _, _, mimeType, _ ->
                                if (TextbookUrlPolicy.isTrustedHttpsPortalUrl(url) &&
                                    (mimeType.equals("application/pdf", true) || TextbookUrlPolicy.isPdfUrl(url))
                                ) {
                                    capturedPdfUrl = url
                                } else {
                                    blockedExternal = true
                                }
                            }
                            loadUrl("https://textbooks.karnataka.gov.in/")
                        }
                    },
                    update = { webViewRef.set(it) }
                )
                capturedPdfUrl?.let { url ->
                    Surface(tonalElevation = 3.dp) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("A PDF link was found", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Confirm that this is ${book.title}. The app will download it only from an official Karnataka government textbook domain and verify that the saved file is a PDF.",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Button(
                                onClick = { onDownloadPdf(url) },
                                modifier = Modifier.fillMaxWidth().testTag("confirm_ktbs_pdf_download")
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Download for this textbook")
                            }
                            OutlinedButton(
                                onClick = { capturedPdfUrl = null },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Keep browsing") }
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef.getAndSet(null)?.let { view ->
                view.stopLoading()
                view.destroy()
            }
        }
    }
}
