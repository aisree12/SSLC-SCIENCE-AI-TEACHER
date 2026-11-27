package com.example.data.textbook

import java.net.URI
import java.util.Locale

/**
 * Restricts textbook downloads to HTTPS PDF resources hosted by Karnataka government
 * textbook domains. The downloaded bytes are still checked for a real PDF signature.
 */
object TextbookUrlPolicy {
    fun isTrustedPortalHost(host: String?): Boolean {
        val normalized = host?.lowercase(Locale.ROOT) ?: return false
        return normalized == "karnataka.gov.in" ||
            normalized.endsWith(".karnataka.gov.in") ||
            normalized == "ktbs.kar.nic.in" ||
            normalized.endsWith(".ktbs.kar.nic.in")
    }

    fun isPdfUrl(rawUrl: String): Boolean {
        val uri = runCatching { URI(rawUrl) }.getOrNull() ?: return false
        val path = uri.path.orEmpty().lowercase(Locale.ROOT)
        val query = uri.rawQuery.orEmpty().lowercase(Locale.ROOT)
        return path.endsWith(".pdf") || query.contains("format=pdf") || query.contains("type=pdf")
    }

    fun isTrustedHttpsPortalUrl(rawUrl: String): Boolean {
        val uri = runCatching { URI(rawUrl) }.getOrNull() ?: return false
        return uri.scheme.equals("https", ignoreCase = true) && isTrustedPortalHost(uri.host)
    }

    fun isTrustedPdfUrl(rawUrl: String): Boolean =
        isTrustedHttpsPortalUrl(rawUrl) && isPdfUrl(rawUrl)
}
