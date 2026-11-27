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
