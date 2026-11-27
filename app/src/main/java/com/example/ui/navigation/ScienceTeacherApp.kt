package com.example.ui.navigation

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.remote.FirestoreRepository
import com.example.firebase.signOutUser
import com.example.ui.screens.AiTeacherScreen
import com.example.ui.screens.ExamAcademyScreen
import com.example.ui.screens.PyqCentreScreen
import com.example.ui.screens.ScienceLabAndDiagramScreen
import com.example.ui.screens.StudentCoachScreen
import com.example.ui.screens.StudyCentreScreen
import com.example.viewmodel.AiTeacherViewModel
import com.example.viewmodel.ExamAcademyViewModel
import com.example.viewmodel.SharedCurriculumViewModel

const val ROUTE_STUDY_CENTRE = "study_centre"
const val ROUTE_AI_TEACH = "ai_teach"
const val ROUTE_PYQS = "pyqs"
const val ROUTE_ACADEMY = "academy"
const val ROUTE_COACH = "coach"
const val ROUTE_LAB_STUDIO = "lab_studio/{chapterId}"

private data class PrimaryDestination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val primaryDestinations = listOf(
    PrimaryDestination(
        route = ROUTE_STUDY_CENTRE,
        label = "Study Centre",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
        testTag = "nav_study_centre"
    ),
    PrimaryDestination(
        route = ROUTE_AI_TEACH,
        label = "AI Teach",
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome,
        testTag = "nav_ai_teach"
    ),
    PrimaryDestination(
        route = ROUTE_PYQS,
        label = "PYQs",
        selectedIcon = Icons.Filled.Quiz,
        unselectedIcon = Icons.Outlined.Quiz,
        testTag = "nav_pyqs"
    ),
    PrimaryDestination(
        route = ROUTE_ACADEMY,
        label = "Academy",
        selectedIcon = Icons.Filled.School,
        unselectedIcon = Icons.Outlined.School,
        testTag = "nav_academy"
    ),
    PrimaryDestination(
        route = ROUTE_COACH,
        label = "Coach",
        selectedIcon = Icons.AutoMirrored.Filled.TrendingUp,
        unselectedIcon = Icons.AutoMirrored.Outlined.TrendingUp,
        testTag = "nav_coach"
    )
)

@Composable
fun ScienceTeacherApp(
    userId: String,
    userDisplayName: String,
    userEmail: String
) {
    val context = LocalContext.current
    val appContext = context.applicationContext as Application
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }
    val firestoreRepository = remember(context) { FirestoreRepository(appContext) }

    val sharedFactory = remember(userId) {
        viewModelFactory {
            initializer {
                SharedCurriculumViewModel(
                    appContext = appContext,
                    firestoreRepository = firestoreRepository,
                    currentUserId = userId,
                    displayName = userDisplayName
                )
            }
            initializer {
                AiTeacherViewModel(
                    appContext = appContext,
                    firestoreRepository = firestoreRepository,
                    currentUserId = userId
                )
            }
            initializer {
                ExamAcademyViewModel(
                    appContext = appContext,
                    firestoreRepository = firestoreRepository,
                    currentUserId = userId
                )
            }
        }
    }

    val curriculumVm: SharedCurriculumViewModel = viewModel(key = "curr_$userId", factory = sharedFactory)
    val aiVm: AiTeacherViewModel = viewModel(key = "ai_$userId", factory = sharedFactory)
    val academyVm: ExamAcademyViewModel = viewModel(key = "acad_$userId", factory = sharedFactory)

    val curriculumState by curriculumVm.uiState.collectAsStateWithLifecycle()
    val textbooks by curriculumVm.textbooks.collectAsStateWithLifecycle()
    val chapterProgressMap by curriculumVm.chapterProgressMap.collectAsStateWithLifecycle()
    val examAttempts by curriculumVm.examAttempts.collectAsStateWithLifecycle()
    val savedNotes by curriculumVm.savedNotes.collectAsStateWithLifecycle()

    val selectedMode by aiVm.selectedMode.collectAsStateWithLifecycle()
    val messages by aiVm.messages.collectAsStateWithLifecycle()
    val aiUiState by aiVm.uiState.collectAsStateWithLifecycle()
    val draftQuestion by aiVm.draftQuestion.collectAsStateWithLifecycle()
    val providerStatus by aiVm.providerStatus.collectAsStateWithLifecycle()
    val savedHistory by aiVm.savedHistory.collectAsStateWithLifecycle()

    val academyState by academyVm.uiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isInLabOverlay = currentDestination?.route?.startsWith("lab_studio") == true

    fun navigateToPrimary(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun routeToAiWithPrompt(chapterId: String, prompt: String) {
        curriculumVm.selectChapter(chapterId)
        aiVm.updateDraftQuestion(prompt)
        navigateToPrimary(ROUTE_AI_TEACH)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 600.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (!useRail && !isInLabOverlay) {
                    NavigationBar {
                        primaryDestinations.forEach { dest ->
                            val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateToPrimary(dest.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                        contentDescription = dest.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = dest.label,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                modifier = Modifier.testTag(dest.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useRail && !isInLabOverlay) {
                    NavigationRail(modifier = Modifier.fillMaxHeight()) {
                        primaryDestinations.forEach { dest ->
                            val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateToPrimary(dest.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                        contentDescription = dest.label
                                    )
                                },
                                label = { Text(dest.label, maxLines = 1) },
                                modifier = Modifier.testTag(dest.testTag)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_STUDY_CENTRE,
                        modifier = Modifier.widthIn(max = 860.dp).fillMaxSize()
                    ) {
                        composable(ROUTE_STUDY_CENTRE) {
                            StudyCentreScreen(
                                uiState = curriculumState,
                                textbooks = textbooks,
                                chapterProgressMap = chapterProgressMap,
                                onSelectSubject = curriculumVm::selectSubject,
                                onSelectChapter = curriculumVm::selectChapter,
                                onSelectTopic = curriculumVm::selectTopic,
                                onSelectLanguage = curriculumVm::selectLanguage,
                                onUpdateSearch = curriculumVm::updateSearchQuery,
                                onResetSearch = curriculumVm::resetSearch,
                                onToggleTopicCompleted = curriculumVm::toggleTopicCompleted,
                                onSaveChapterNotesOffline = curriculumVm::saveChapterNotesOffline,
                                onDownloadBook = curriculumVm::downloadTextbook,
                                onDownloadAllBooks = curriculumVm::downloadAllTextbooks,
                                onUpdateBookReading = curriculumVm::updateBookReadingState,
                                onRefreshVerification = curriculumVm::verifyOfficialSyllabusAndPyqs,
                                onAskAiTeacher = ::routeToAiWithPrompt,
                                onOpenLabForChapter = { chapterId ->
                                    navController.navigate("lab_studio/$chapterId")
                                },
                                onOpenPyqTab = { navigateToPrimary(ROUTE_PYQS) },
                                onDismissBanner = curriculumVm::dismissBanner
                            )
                        }

                        composable(ROUTE_AI_TEACH) {
                            AiTeacherScreen(
                                curriculumState = curriculumState,
                                selectedMode = selectedMode,
                                messages = messages,
                                uiState = aiUiState,
                                draftQuestion = draftQuestion,
                                providerStatus = providerStatus,
                                savedHistory = savedHistory,
                                onSelectSubject = curriculumVm::selectSubject,
                                onSelectChapter = curriculumVm::selectChapter,
                                onSelectTopic = curriculumVm::selectTopic,
                                onSelectLanguage = curriculumVm::selectLanguage,
                                onSelectMode = aiVm::selectTeachingMode,
                                onDraftChanged = aiVm::updateDraftQuestion,
                                onSendQuestion = aiVm::askQuestion,
                                onClearError = aiVm::clearError
                            )
                        }

                        composable(ROUTE_PYQS) {
                            PyqCentreScreen(
                                curriculumState = curriculumState,
                                onSelectSubject = curriculumVm::selectSubject,
                                onSelectChapter = curriculumVm::selectChapter,
                                onSelectTopic = curriculumVm::selectTopic,
                                onSelectLanguage = curriculumVm::selectLanguage,
                                onRefreshPyqDiscovery = curriculumVm::verifyOfficialSyllabusAndPyqs,
                                onAskAiForPyqSolution = ::routeToAiWithPrompt
                            )
                        }

                        composable(ROUTE_ACADEMY) {
                            ExamAcademyScreen(
                                curriculumState = curriculumState,
                                academyState = academyState,
                                descriptivePrompts = academyVm.descriptivePrompts,
                                questionsProvider = academyVm::questionsForChapterOrMock,
                                onSelectSubject = curriculumVm::selectSubject,
                                onSelectChapter = curriculumVm::selectChapter,
                                onSelectTopic = curriculumVm::selectTopic,
                                onSelectLanguage = curriculumVm::selectLanguage,
                                onSelectMode = academyVm::setTestMode,
                                onSelectMcqOption = academyVm::selectMcqOption,
                                onSubmitMcqTest = academyVm::submitMcqTest,
                                onSelectDescriptivePrompt = academyVm::selectDescriptivePrompt,
                                onUpdateDescriptiveInput = academyVm::updateDescriptiveInput,
                                onEvaluateDescriptive = academyVm::evaluateDescriptive
                            )
                        }

                        composable(ROUTE_COACH) {
                            StudentCoachScreen(
                                userDisplayName = userDisplayName,
                                userEmail = userEmail,
                                curriculumState = curriculumState,
                                chapterProgressMap = chapterProgressMap,
                                examAttempts = examAttempts,
                                savedNotes = savedNotes,
                                onUpdateDailyTarget = curriculumVm::updateDailyTarget,
                                onOpenChapterInStudyCentre = { chId ->
                                    curriculumVm.selectChapter(chId)
                                    navigateToPrimary(ROUTE_STUDY_CENTRE)
                                },
                                onSignOutClick = {
                                    signOutUser(credentialManager = credentialManager, scope = scope)
                                }
                            )
                        }

                        composable(
                            route = ROUTE_LAB_STUDIO,
                            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
                        ) { entry ->
                            val chId = entry.arguments?.getString("chapterId") ?: curriculumState.selectedChapterId
                            ScienceLabAndDiagramScreen(
                                initialChapterId = chId,
                                language = curriculumState.selectedLanguage,
                                onAskAiTeacherAboutLab = { chapterId, prompt ->
                                    navController.popBackStack()
                                    routeToAiWithPrompt(chapterId, prompt)
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
