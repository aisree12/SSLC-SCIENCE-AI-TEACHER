package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.textbook.TextbookRepository
import com.example.ui.screens.AuthScreen
import com.example.ui.theme.ScienceTeacherTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `app metadata strings and named firestore database id are configured`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("SSLC Science Teacher", context.getString(R.string.app_name))
        val dbId = context.getString(R.string.firestore_database_id)
        assertTrue("Expected named Firestore database id", dbId.isNotBlank() && dbId != "(default)")
    }

    @Test
    fun `textbook repository seeds all 4 official KTBS PDFs into Room`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            val repo = TextbookRepository(context, db.textbookDownloadDao())
            repo.ensureCatalogueSeeded()
            val books = repo.observeTextbooks().first()
            assertEquals(4, books.size)
            assertTrue(books.any { it.id == "ktbs_10_sci_en_p1" })
            assertTrue(books.any { it.id == "ktbs_10_sci_kn_p2" })
        } finally {
            db.close()
        }
    }

    @Test
    fun `auth screen renders Google Sign-In button`() {
        composeTestRule.setContent {
            ScienceTeacherTheme {
                AuthScreen(
                    authErrorMessage = null,
                    onAuthSuccess = {},
                    onAuthError = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("google_sign_in_button")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
