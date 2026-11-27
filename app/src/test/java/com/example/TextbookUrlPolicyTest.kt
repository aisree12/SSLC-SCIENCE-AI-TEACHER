package com.example

import com.example.data.textbook.TextbookUrlPolicy
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TextbookUrlPolicyTest {
    @Test
    fun acceptsHttpsPdfFromOfficialKtbsDomains() {
        assertTrue(TextbookUrlPolicy.isTrustedPdfUrl("https://textbooks.karnataka.gov.in/books/sslc-science.pdf"))
        assertTrue(TextbookUrlPolicy.isTrustedPdfUrl("https://files.ktbs.kar.nic.in/2026/science.pdf"))
        assertTrue(TextbookUrlPolicy.isTrustedPdfUrl("https://ncert.nic.in/textbook/pdf/jesc1ps.pdf"))
        assertTrue(TextbookUrlPolicy.isTrustedPdfUrl("https://textbooks.karnataka.gov.in/download?format=pdf"))
    }

    @Test
    fun acceptsAllFourOfficial2026ScienceTextbooks() {
        val urls = listOf(
            "https://textbooks.karnataka.gov.in/uploads/Karnataka_Textbook_Society/2026-27%20TEXTBOOKS/10th%20STD/ENGLISH%20MEDIUM%20TEXTBOOKS/10th%20English%20Science%20Part%20-%201%202026-27.pdf",
            "https://textbooks.karnataka.gov.in/uploads/Karnataka_Textbook_Society/2026-27%20TEXTBOOKS/10th%20STD/ENGLISH%20MEDIUM%20TEXTBOOKS/10th%20English%20Science%20Part%20-2%202026-27.pdf",
            "https://textbooks.karnataka.gov.in/uploads/Karnataka_Textbook_Society/2026-27%20TEXTBOOKS/10th%20STD/KANNADA%20MEDIUM%20TEXTBOOKS/10th%20Kannada%20Science%20Part-1%202026-27.pdf",
            "https://textbooks.karnataka.gov.in/uploads/Karnataka_Textbook_Society/2026-27%20TEXTBOOKS/10th%20STD/KANNADA%20MEDIUM%20TEXTBOOKS/10th%20Kannada%20Science%20Part-2%202026-27.pdf"
        )
        assertTrue(urls.all { TextbookUrlPolicy.isTrustedPdfUrl(it) })
    }

    @Test
    fun rejectsNonHttpsAndNonPdfResources() {
        assertFalse(TextbookUrlPolicy.isTrustedPdfUrl("http://textbooks.karnataka.gov.in/science.pdf"))
        assertFalse(TextbookUrlPolicy.isTrustedPdfUrl("https://textbooks.karnataka.gov.in/science.html"))
        assertTrue(TextbookUrlPolicy.isTrustedHttpsPortalUrl("https://textbooks.karnataka.gov.in/download?id=123"))
        assertFalse(TextbookUrlPolicy.isTrustedHttpsPortalUrl("http://textbooks.karnataka.gov.in/download?id=123"))
    }

    @Test
    fun rejectsLookalikeAndUntrustedHosts() {
        assertFalse(TextbookUrlPolicy.isTrustedPdfUrl("https://textbooks.karnataka.gov.in.attacker.example/science.pdf"))
        assertFalse(TextbookUrlPolicy.isTrustedPdfUrl("https://example.com/science.pdf"))
        assertFalse(TextbookUrlPolicy.isTrustedPdfUrl("https://planning.karnataka.gov.in/science.pdf"))
        assertFalse(TextbookUrlPolicy.isTrustedPdfUrl("https://karnataka.gov.in.example.com/science.pdf"))
    }
}
