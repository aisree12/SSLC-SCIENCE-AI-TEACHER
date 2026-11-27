package com.example.data.textbook

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import com.example.data.local.TextbookDownloadDao
import com.example.data.local.TextbookDownloadEntity
import com.example.model.DownloadStatus
import com.example.model.OfficialTextbook
import com.example.model.TextbookMedium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

class TextbookRepository(
    private val context: Context,
    private val dao: TextbookDownloadDao
) {
    companion object {
        const val CURRENT_ACADEMIC_YEAR = "2026–2027"
        const val CATALOGUE_EDITION = "2026–27 catalogue — direct Science PDF links pending official verification"
        const val PORTAL_KTBS = "https://textbooks.karnataka.gov.in/"

        val officialCatalogue: List<OfficialTextbook> = listOf(
            OfficialTextbook(
                id = "ktbs_10_sci_en_p1",
                title = "Class 10 Science – Part 1 (English Medium)",
                kannadaTitle = "10ನೇ ತರಗತಿ ವಿಜ್ಞಾನ – ಭಾಗ 1 (ಇಂಗ್ಲಿಷ್ ಮಾಧ್ಯಮ)",
                medium = TextbookMedium.ENGLISH,
                part = 1,
                academicYear = CURRENT_ACADEMIC_YEAR,
                edition = CATALOGUE_EDITION,
                officialUrl = PORTAL_KTBS,
                portalUrl = PORTAL_KTBS,
                sourceAuthority = "KTBS portal — individual 2026–27 Science PDF not yet verified",
                chapterRangeLabel = "Expected chapters 1–7 (provisional)",
                estimatedSizeMb = "Size not verified"
            ),
            OfficialTextbook(
                id = "ktbs_10_sci_en_p2",
                title = "Class 10 Science – Part 2 (English Medium)",
                kannadaTitle = "10ನೇ ತರಗತಿ ವಿಜ್ಞಾನ – ಭಾಗ 2 (ಇಂಗ್ಲಿಷ್ ಮಾಧ್ಯಮ)",
                medium = TextbookMedium.ENGLISH,
                part = 2,
                academicYear = CURRENT_ACADEMIC_YEAR,
                edition = CATALOGUE_EDITION,
                officialUrl = PORTAL_KTBS,
                portalUrl = PORTAL_KTBS,
                sourceAuthority = "KTBS portal — individual 2026–27 Science PDF not yet verified",
                chapterRangeLabel = "Expected chapters 8–13 (provisional)",
                estimatedSizeMb = "Size not verified"
            ),
            OfficialTextbook(
                id = "ktbs_10_sci_kn_p1",
                title = "Class 10 Science – Part 1 (Kannada Medium)",
                kannadaTitle = "10ನೇ ತರಗತಿ ವಿಜ್ಞಾನ – ಭಾಗ 1 (ಕನ್ನಡ ಮಾಧ್ಯಮ)",
                medium = TextbookMedium.KANNADA,
                part = 1,
                academicYear = CURRENT_ACADEMIC_YEAR,
                edition = CATALOGUE_EDITION,
                officialUrl = PORTAL_KTBS,
                portalUrl = PORTAL_KTBS,
                sourceAuthority = "KTBS portal — individual 2026–27 Science PDF not yet verified",
                chapterRangeLabel = "ಅಧ್ಯಾಯ 1–7 (ತಾತ್ಕಾಲಿಕ; ಪರಿಶೀಲನೆ ಬಾಕಿ)",
                estimatedSizeMb = "Size not verified"
            ),
            OfficialTextbook(
                id = "ktbs_10_sci_kn_p2",
                title = "Class 10 Science – Part 2 (Kannada Medium)",
                kannadaTitle = "10ನೇ ತರಗತಿ ವಿಜ್ಞಾನ – ಭಾಗ 2 (ಕನ್ನಡ ಮಾಧ್ಯಮ)",
                medium = TextbookMedium.KANNADA,
                part = 2,
                academicYear = CURRENT_ACADEMIC_YEAR,
                edition = CATALOGUE_EDITION,
                officialUrl = PORTAL_KTBS,
                portalUrl = PORTAL_KTBS,
                sourceAuthority = "KTBS portal — individual 2026–27 Science PDF not yet verified",
                chapterRangeLabel = "ಅಧ್ಯಾಯ 8–13 (ತಾತ್ಕಾಲಿಕ; ಪರಿಶೀಲನೆ ಬಾಕಿ)",
                estimatedSizeMb = "Size not verified"
            )
        )
    }

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    suspend fun ensureCatalogueSeeded() = withContext(Dispatchers.IO) {
        val toUpsert = mutableListOf<TextbookDownloadEntity>()
        for (book in officialCatalogue) {
            val existing = dao.getById(book.id)
            if (existing == null) {
                toUpsert.add(
                    TextbookDownloadEntity(
                        id = book.id,
                        medium = book.medium.name,
                        part = book.part,
                        title = book.title,
                        kannadaTitle = book.kannadaTitle,
                        academicYear = book.academicYear,
                        edition = book.edition,
                        officialUrl = book.officialUrl,
                        localFilePath = null,
                        downloadManagerId = null,
                        status = DownloadStatus.NOT_DOWNLOADED.name,
                        progressPercent = 0,
                        sha256Checksum = null,
                        lastReadPage = 0,
                        bookmarkedPagesCsv = "",
                        statusNote = "Open the official KTBS catalogue. A direct Class 10 Science PDF URL has not yet been verified for this edition.",
                        updatedAtMillis = System.currentTimeMillis()
                    )
                )
            } else if (existing.edition != book.edition || existing.officialUrl != book.officialUrl ||
                existing.academicYear != book.academicYear
            ) {
                val note = if (existing.status == DownloadStatus.DOWNLOADED.name) {
                    "A previously downloaded edition is kept offline. Check the official KTBS catalogue for the current ${book.academicYear} edition."
                } else {
                    "Open the official KTBS catalogue. A direct Class 10 Science PDF URL has not yet been verified for this edition."
                }
                toUpsert.add(
                    existing.copy(
                        academicYear = book.academicYear,
                        edition = book.edition,
                        officialUrl = book.officialUrl,
                        statusNote = note,
                        updatedAtMillis = System.currentTimeMillis()
                    )
                )
            }
        }
        if (toUpsert.isNotEmpty()) {
            dao.upsertAll(toUpsert)
        }
    }

    fun observeTextbooks(): Flow<List<OfficialTextbook>> {
        return dao.observeAll().map { entities ->
            officialCatalogue.map { base ->
                val saved = entities.firstOrNull { it.id == base.id }
                if (saved == null) {
                    base
                } else {
                    val fileExists = saved.localFilePath?.let { File(it).exists() } == true
                    val status = when {
                        saved.status == DownloadStatus.DOWNLOADED.name && !fileExists -> DownloadStatus.NOT_DOWNLOADED
                        else -> DownloadStatus.entries.firstOrNull { it.name == saved.status } ?: DownloadStatus.NOT_DOWNLOADED
                    }
                    base.copy(
                        status = status,
                        progressPercent = saved.progressPercent,
                        localFilePath = if (fileExists) saved.localFilePath else null,
                        sha256Checksum = saved.sha256Checksum,
                        lastReadPage = saved.lastReadPage,
                        bookmarkedPages = saved.bookmarkedPagesCsv
                            .split(",")
                            .mapNotNull { it.trim().toIntOrNull() },
                        statusNote = saved.statusNote
                    )
                }
            }
        }
    }

    suspend fun startDownload(bookId: String): Result<Unit> = withContext(Dispatchers.IO) {
        val book = officialCatalogue.firstOrNull { it.id == bookId }
            ?: return@withContext Result.failure(IllegalArgumentException("Unknown textbook ID"))

        val existing = dao.getById(bookId)
        if (!book.directPdfVerified || !TextbookUrlPolicy.isTrustedHttpsPortalUrl(book.officialUrl)) {
            val note = "Direct PDF link is not verified or is outside the approved KTBS/NCERT domains. Open the official catalogue and choose the current Class 10 Science edition."
            if (existing != null) dao.upsert(existing.copy(status = DownloadStatus.NOT_DOWNLOADED.name, statusNote = note, updatedAtMillis = System.currentTimeMillis()))
            return@withContext Result.failure(IllegalStateException(note))
        }
        if (existing?.status == DownloadStatus.DOWNLOADED.name &&
            existing.localFilePath?.let { File(it).exists() } == true
        ) {
            return@withContext Result.success(Unit)
        }
        if (existing?.status == DownloadStatus.DOWNLOADING.name) {
            return@withContext Result.success(Unit)
        }

        val targetDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val fileName = "${book.id}.pdf"
        val destFile = File(targetDir, fileName)
        if (destFile.exists()) destFile.delete()

        return@withContext try {
            val request = DownloadManager.Request(Uri.parse(book.officialUrl))
                .setTitle(book.title)
                .setDescription("Downloading official SSLC Science textbook PDF")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(false)
                .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOCUMENTS, fileName)

            val downloadId = downloadManager.enqueue(request)
            dao.upsert(
                (existing ?: TextbookDownloadEntity(
                    id = book.id,
                    medium = book.medium.name,
                    part = book.part,
                    title = book.title,
                    kannadaTitle = book.kannadaTitle,
                    academicYear = book.academicYear,
                    edition = book.edition,
                    officialUrl = book.officialUrl,
                    localFilePath = destFile.absolutePath,
                    downloadManagerId = downloadId,
                    status = DownloadStatus.DOWNLOADING.name,
                    progressPercent = 0,
                    sha256Checksum = null,
                    lastReadPage = 0,
                    bookmarkedPagesCsv = "",
                    statusNote = "Downloading from official KTBS portal...",
                    updatedAtMillis = System.currentTimeMillis()
                )).copy(
                    localFilePath = destFile.absolutePath,
                    downloadManagerId = downloadId,
                    status = DownloadStatus.DOWNLOADING.name,
                    progressPercent = 5,
                    statusNote = "Downloading from official KTBS portal...",
                    updatedAtMillis = System.currentTimeMillis()
                )
            )
            monitorDownload(book, downloadId, destFile)
            Result.success(Unit)
        } catch (e: Exception) {
            dao.upsert(
                (existing ?: TextbookDownloadEntity(
                    id = book.id,
                    medium = book.medium.name,
                    part = book.part,
                    title = book.title,
                    kannadaTitle = book.kannadaTitle,
                    academicYear = book.academicYear,
                    edition = book.edition,
                    officialUrl = book.officialUrl,
                    localFilePath = null,
                    downloadManagerId = null,
                    status = DownloadStatus.FAILED.name,
                    progressPercent = 0,
                    sha256Checksum = null,
                    lastReadPage = 0,
                    bookmarkedPagesCsv = "",
                    statusNote = e.localizedMessage ?: "Download failed. Open KTBS portal directly.",
                    updatedAtMillis = System.currentTimeMillis()
                )).copy(
                    status = DownloadStatus.FAILED.name,
                    statusNote = "Direct download unavailable (${e.localizedMessage ?: "error"}). Use Open Official KTBS Portal.",
                    updatedAtMillis = System.currentTimeMillis()
                )
            )
            Result.failure(e)
        }
    }

    /**
     * Downloads a PDF URL selected by the student inside the official KTBS catalogue.
     * The URL is never accepted from an arbitrary host; the completed file must pass
     * the PDF signature check in monitorDownload before it is marked as downloaded.
     */
    suspend fun downloadSelectedOfficialPdf(bookId: String, selectedUrl: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val book = officialCatalogue.firstOrNull { it.id == bookId }
                ?: return@withContext Result.failure(IllegalArgumentException("Unknown textbook ID"))
            if (!TextbookUrlPolicy.isTrustedHttpsPortalUrl(selectedUrl)) {
                return@withContext Result.failure(
                    SecurityException("Choose an HTTPS PDF link hosted on the official KTBS or NCERT textbook domain.")
                )
            }
            val uri = Uri.parse(selectedUrl)

            val existing = dao.getById(bookId)
            if (existing?.status == DownloadStatus.DOWNLOADED.name &&
                existing.localFilePath?.let { File(it).exists() } == true
            ) return@withContext Result.success(Unit)
            if (existing?.status == DownloadStatus.DOWNLOADING.name) return@withContext Result.success(Unit)

            val targetDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
            val destFile = File(targetDir, "${book.id}.pdf")
            if (destFile.exists()) destFile.delete()

            try {
                val downloadId = downloadManager.enqueue(
                    DownloadManager.Request(uri)
                        .setTitle(book.title)
                        .setDescription("Downloading selected PDF from the official KTBS catalogue")
                        .setMimeType("application/pdf")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(false)
                        .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOCUMENTS, destFile.name)
                )
                val entity = existing ?: TextbookDownloadEntity(
                    id = book.id,
                    medium = book.medium.name,
                    part = book.part,
                    title = book.title,
                    kannadaTitle = book.kannadaTitle,
                    academicYear = book.academicYear,
                    edition = book.edition,
                    officialUrl = selectedUrl,
                    localFilePath = destFile.absolutePath,
                    downloadManagerId = downloadId,
                    status = DownloadStatus.DOWNLOADING.name,
                    progressPercent = 0,
                    sha256Checksum = null,
                    lastReadPage = 0,
                    bookmarkedPagesCsv = "",
                    statusNote = "Downloading the PDF selected from the official KTBS catalogue.",
                    updatedAtMillis = System.currentTimeMillis()
                )
                dao.upsert(
                    entity.copy(
                        officialUrl = selectedUrl,
                        localFilePath = destFile.absolutePath,
                        downloadManagerId = downloadId,
                        status = DownloadStatus.DOWNLOADING.name,
                        progressPercent = 5,
                        statusNote = "Downloading selected official PDF…",
                        updatedAtMillis = System.currentTimeMillis()
                    )
                )
                monitorDownload(book, downloadId, destFile)
                Result.success(Unit)
            } catch (e: Exception) {
                val base = existing ?: TextbookDownloadEntity(
                    id = book.id,
                    medium = book.medium.name,
                    part = book.part,
                    title = book.title,
                    kannadaTitle = book.kannadaTitle,
                    academicYear = book.academicYear,
                    edition = book.edition,
                    officialUrl = selectedUrl,
                    localFilePath = null,
                    downloadManagerId = null,
                    status = DownloadStatus.FAILED.name,
                    progressPercent = 0,
                    sha256Checksum = null,
                    lastReadPage = 0,
                    bookmarkedPagesCsv = "",
                    statusNote = "Download could not be started.",
                    updatedAtMillis = System.currentTimeMillis()
                )
                dao.upsert(base.copy(
                    status = DownloadStatus.FAILED.name,
                    statusNote = e.localizedMessage ?: "Download could not be started.",
                    updatedAtMillis = System.currentTimeMillis()
                ))
                Result.failure(e)
            }
        }

    private suspend fun monitorDownload(
        book: OfficialTextbook,
        downloadId: Long,
        destFile: File
    ) {
        var active = true
        while (active) {
            delay(800L)
            val query = DownloadManager.Query().setFilterById(downloadId)
            var cursor: Cursor? = null
            try {
                cursor = downloadManager.query(query)
                if (cursor != null && cursor.moveToFirst()) {
                    val statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val dlBytesIdx = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    val totalBytesIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    val status = if (statusIdx >= 0) cursor.getInt(statusIdx) else DownloadManager.STATUS_FAILED
                    val downloaded = if (dlBytesIdx >= 0) cursor.getLong(dlBytesIdx) else 0L
                    val total = if (totalBytesIdx >= 0) cursor.getLong(totalBytesIdx) else -1L
                    val pct = if (total > 0) ((downloaded * 100) / total).toInt().coerceIn(5, 99) else 25

                    val current = dao.getById(book.id) ?: return
                    when (status) {
                        DownloadManager.STATUS_RUNNING, DownloadManager.STATUS_PENDING, DownloadManager.STATUS_PAUSED -> {
                            dao.upsert(
                                current.copy(
                                    progressPercent = pct,
                                    statusNote = if (status == DownloadManager.STATUS_PAUSED) "Paused — will resume automatically" else "Downloading ($pct%)...",
                                    updatedAtMillis = System.currentTimeMillis()
                                )
                            )
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            active = false
                            val isValidPdf = verifyPdfHeader(destFile)
                            if (isValidPdf) {
                                val hash = computeSha256(destFile)
                                dao.upsert(
                                    current.copy(
                                        status = DownloadStatus.DOWNLOADED.name,
                                        progressPercent = 100,
                                        localFilePath = destFile.absolutePath,
                                        sha256Checksum = hash,
                                        statusNote = "Verified PDF ready offline",
                                        updatedAtMillis = System.currentTimeMillis()
                                    )
                                )
                            } else {
                                destFile.delete()
                                dao.upsert(
                                    current.copy(
                                        status = DownloadStatus.FAILED.name,
                                        progressPercent = 0,
                                        localFilePath = null,
                                        statusNote = "The selected link did not return a valid PDF. Open the official textbook catalogue in your browser and choose the book file.",
                                        updatedAtMillis = System.currentTimeMillis()
                                    )
                                )
                            }
                        }
                        DownloadManager.STATUS_FAILED -> {
                            active = false
                            dao.upsert(
                                current.copy(
                                    status = DownloadStatus.FAILED.name,
                                    progressPercent = 0,
                                    statusNote = "Official textbook server unavailable or link moved. Retry from the catalogue or open it in your browser.",
                                    updatedAtMillis = System.currentTimeMillis()
                                )
                            )
                        }
                    }
                } else {
                    active = false
                    val current = dao.getById(book.id)
                    if (current != null && current.status == DownloadStatus.DOWNLOADING.name) {
                        dao.upsert(current.copy(
                            status = DownloadStatus.FAILED.name,
                            progressPercent = 0,
                            statusNote = "Download task was not found. Please choose the PDF from the official KTBS catalogue again.",
                            updatedAtMillis = System.currentTimeMillis()
                        ))
                    }
                }
            } finally {
                cursor?.close()
            }
        }
    }

    /**
     * Reattaches progress monitoring to DownloadManager tasks that survived an app restart.
     * DownloadManager owns the transfer, so the app can safely resume observing its state.
     */
    suspend fun reconcileActiveDownloads() = withContext(Dispatchers.IO) {
        val active = dao.getDownloading()
        coroutineScope {
            active.forEach { entry ->
                launch {
                    val book = officialCatalogue.firstOrNull { it.id == entry.id }
                    val downloadId = entry.downloadManagerId
                    val file = entry.localFilePath?.let(::File)
                    if (book != null && downloadId != null && file != null) {
                        monitorDownload(book, downloadId, file)
                    } else {
                        val current = dao.getById(entry.id)
                        if (current != null) {
                            dao.upsert(current.copy(
                                status = DownloadStatus.FAILED.name,
                                progressPercent = 0,
                                statusNote = "The saved download task could not be recovered. Please select the PDF again from KTBS.",
                                updatedAtMillis = System.currentTimeMillis()
                            ))
                        }
                    }
                }
            }
        }
    }

    suspend fun updateReadingProgress(bookId: String, page: Int, bookmarkedPages: List<Int>) =
        withContext(Dispatchers.IO) {
            val current = dao.getById(bookId) ?: return@withContext
            dao.upsert(
                current.copy(
                    lastReadPage = page.coerceAtLeast(0),
                    bookmarkedPagesCsv = bookmarkedPages.distinct().sorted().joinToString(","),
                    updatedAtMillis = System.currentTimeMillis()
                )
            )
        }

    fun verifyPdfHeader(file: File): Boolean {
        if (!file.exists() || file.length() < 100) return false
        return try {
            file.inputStream().use { input ->
                val header = ByteArray(5)
                val read = input.read(header)
                read == 5 && String(header) == "%PDF-"
            }
        } catch (_: Exception) {
            false
        }
    }

    fun computeSha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var bytes = input.read(buffer)
            while (bytes > 0) {
                digest.update(buffer, 0, bytes)
                bytes = input.read(buffer)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
