package com.example.data.pyq

import com.example.model.PyqExamType
import com.example.model.PyqQuestion
import com.example.model.QuestionType
import com.example.model.ScienceDivision
import com.example.model.TextbookMedium
import com.example.data.curriculum.SyllabusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

data class PyqAnalyticsSummary(
    val totalQuestions: Int,
    val verifiedOfficialKeyCount: Int,
    val divisionCounts: Map<ScienceDivision, Int>,
    val topRepeatedChapters: List<Pair<String, Int>>,
    val topMarksDistribution: Map<Int, Int>,
    val topDiagrams: List<String>,
    val topReactions: List<String>,
    val topNumericals: List<String>,
    val probablePredictionTopics: List<String>
)

data class PyqDiscoveryStatus(
    val lastSyncedLabel: String,
    val archiveIndexReachable: Boolean,
    val archiveCollectionsCount: Int,
    val message: String
)

data class PyqPaperArchiveEntry(
    val id: String,
    val title: String,
    val subtitle: String,
    val paperArchiveUrl: String,
    val answerKeyArchiveUrl: String?,
    val sourceNote: String
)

object PyqRepository {
    /**
     * Source notes identify whether an entry is a year index, a third-party mirror,
     * or a direct third-party-hosted PDF. The app does not re-host these documents.
     * The 2024–2026 indexes link KSEAB-issued papers; 2026 has Exam-1/Exam-2 model
     * answers. Older entries are labelled mirrors/indexes, not verified official keys.
     */
    val paperArchive: List<PyqPaperArchiveEntry> = listOf(
        PyqPaperArchiveEntry(
            id = "archive_2026_exam1",
            title = "2026 SSLC Science — Exam 1",
            subtitle = "English 83-E + Kannada 83-K • Question papers and model answers",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/2026/",
            answerKeyArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/answer-key/",
            sourceNote = "Archive index links the original KSEAB PDFs; answer keys are available for both mediums."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2026_exam2",
            title = "2026 SSLC Science — Exam 2",
            subtitle = "English 83-E + Kannada 83-K • Question papers and model answers",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/2026/",
            answerKeyArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/answer-key/",
            sourceNote = "Archive index links the original KSEAB PDFs; answer keys are available for both mediums."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2025_exam1",
            title = "2025 SSLC Science — Exam 1",
            subtitle = "English medium 83-E • Question paper",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/2025/",
            answerKeyArchiveUrl = null,
            sourceNote = "KSEAB question paper listed in the archive. No official key is listed for 2025."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2025_exam2",
            title = "2025 SSLC Science — Exam 2",
            subtitle = "English medium 83-E • Question paper",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/2025/",
            answerKeyArchiveUrl = null,
            sourceNote = "KSEAB question paper listed in the archive. No official key is listed for 2025."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2025_exam3",
            title = "2025 SSLC Science — Exam 3",
            subtitle = "English medium 83-E • Question paper",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/2025/",
            answerKeyArchiveUrl = null,
            sourceNote = "KSEAB question paper listed in the archive. No official key is listed for 2025."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2024_exam1",
            title = "2024 SSLC Science — Exam 1",
            subtitle = "English medium 83-E • Question paper",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/2024/",
            answerKeyArchiveUrl = null,
            sourceNote = "KSEAB question paper listed in the archive. No official key is listed for 2024."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2024_exam2",
            title = "2024 SSLC Science — Exam 2",
            subtitle = "English medium 83-E • Question paper",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/2024/",
            answerKeyArchiveUrl = null,
            sourceNote = "KSEAB question paper listed in the archive. No official key is listed for 2024."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2024_exam3",
            title = "2024 SSLC Science — Exam 3",
            subtitle = "English medium 83-E • Question paper",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/2024/",
            answerKeyArchiveUrl = null,
            sourceNote = "KSEAB question paper listed in the archive. No official key is listed for 2024."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2023_exam1",
            title = "2023 SSLC Science — Main Examination",
            subtitle = "English medium 83-E • Question paper mirror",
            paperArchiveUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706",
            answerKeyArchiveUrl = null,
            sourceNote = "Third-party document mirror of the 2023 KSEAB paper. Check the paper code and date on page 1; no official key is linked here."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2022_exam1",
            title = "2022 SSLC Science — Main Examination",
            subtitle = "English medium 83-E • Question paper mirror",
            paperArchiveUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706",
            answerKeyArchiveUrl = null,
            sourceNote = "Third-party document mirror of the 2022 KSEAB paper dated 11 April 2022. No official key is linked here."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2021_exam1",
            title = "2021 SSLC Science — Paper 1",
            subtitle = "Science paper index • choose the required medium and version",
            paperArchiveUrl = "https://www.indcareer.com/schools/karnataka-sslc-science-question-papers/",
            answerKeyArchiveUrl = null,
            sourceNote = "Third-party year-wise paper index. Confirm the first-page paper code, medium and exam date; no official key is linked here."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2020_exam1",
            title = "2020 SSLC Science — Main Examination",
            subtitle = "English medium 83-E • Question paper PDF mirror",
            paperArchiveUrl = "https://static.collegedekho.com/media/uploads/2023/10/06/class-10-science-83-e-set-a-mar-2020.pdf",
            answerKeyArchiveUrl = null,
            sourceNote = "Third-party-hosted PDF whose first page identifies the KSEAB 30 March 2020 paper. Verify the version before practice."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2020_21_model",
            title = "2020–21 SSLC Science — Historical Model Paper 1",
            subtitle = "English medium 83-E • Historical model paper, not a board exam",
            paperArchiveUrl = "https://docs.aglasem.com/view/7e94720c-8ee3-11eb-97f9-02f21f5619c4",
            answerKeyArchiveUrl = null,
            sourceNote = "Third-party mirror of a historical KSEB model paper. It may include content outside the current syllabus; use only for general practice."
        ),
        PyqPaperArchiveEntry(
            id = "archive_2026_27_model",
            title = "2026–27 SSLC Science — Model Papers 1 to 4",
            subtitle = "English medium 83-E • Four model papers and matching key answers",
            paperArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/model-papers/",
            answerKeyArchiveUrl = "https://previousyearquestions.app/karnataka-sslc/model-papers/",
            sourceNote = "These are current model papers for practice, not past board examination papers."
        )
    )

    // The bank combines starter practice items with question stems transcribed from cited paper mirrors.
    // Keep every item flagged until the original KSEAB paper and any official key are independently matched.
    val allQuestions: List<PyqQuestion> = listOf(
        PyqQuestion(
            id = "pyq_2025_m_c1_1",
            year = 2025,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_reactions",
            topicId = "chem_1_types",
            topicName = "Types of Chemical Reactions",
            marks = 2,
            questionType = QuestionType.CHEMICAL_EQUATION,
            questionNumber = "Q18",
            questionText = "Ferrous sulphate crystals are heated strongly in a dry boiling tube. Write the balanced chemical equation and mention two observations.",
            kannadaQuestionText = "ಫೆರಸ್ ಸಲ್ಫೇಟ್ ಹರಳುಗಳನ್ನು ಒಣ ಪ್ರನಾಳದಲ್ಲಿ ಬಿಸಿ ಮಾಡಿದಾಗ ನಡೆಯುವ ರಾಸಾಯನಿಕ ಕ್ರಿಯೆಯ ಸಮತೋಲಿತ ಸಮೀಕರಣ ಮತ್ತು ಎರಡು ವೀಕ್ಷಣೆಗಳನ್ನು ಬರೆಯಿರಿ.",
            officialKeyAnswer = "Equation: 2FeSO4(s) -> Fe2O3(s) + SO2(g) + SO3(g). Observations: (1) Green crystals turn reddish-brown (Fe2O3). (2) Characteristic smell of burning sulphur due to SO2 and SO3 gases.",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Exam-1 2025 Scheme of Valuation (83-E)",
            officialPdfUrl = "",
            extractionConfidence = 0.99f
        ),
        PyqQuestion(
            id = "pyq_2024_m_c2_1",
            year = 2024,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_acids_bases",
            topicId = "chem_2_ph_salts",
            topicName = "pH Scale & Important Salts",
            marks = 3,
            questionType = QuestionType.DESCRIPTIVE,
            questionNumber = "Q25",
            questionText = "Write the chemical name, molecular formula, and two uses of Plaster of Paris. How is it converted into Gypsum?",
            kannadaQuestionText = "ಪ್ಲಾಸ್ಟರ್ ಆಫ್ ಪ್ಯಾರಿಸ್‌ನ ರಾಸಾಯನಿಕ ಹೆಸರು, ಅಣುಸೂತ್ರ ಮತ್ತು ಎರಡು ಉಪಯೋಗಗಳನ್ನು ಬರೆಯಿರಿ. ಇದನ್ನು ಜಿಪ್ಸಮ್ ಆಗಿ ಹೇಗೆ ಪರಿವರ್ತಿಸಲಾಗುತ್ತದೆ?",
            officialKeyAnswer = "Chemical name: Calcium sulphate hemihydrate (CaSO4·1/2H2O). Uses: (1) Setting fractured bones, (2) Making toys/statues/smooth surfaces. Conversion: CaSO4·1/2H2O + 1.5H2O -> CaSO4·2H2O (Gypsum).",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Exam-1 2024 Scheme of Valuation (83-E)",
            officialPdfUrl = "",
            extractionConfidence = 0.98f
        ),
        PyqQuestion(
            id = "pyq_2024_e2_c3_1",
            year = 2024,
            examType = PyqExamType.EXAM_2,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_metals",
            topicId = "chem_3_metallurgy",
            topicName = "Reactivity Series & Extraction of Metals",
            marks = 2,
            questionType = QuestionType.DIFFERENCE,
            questionNumber = "Q20",
            questionText = "Differentiate between Roasting and Calcination with one balanced equation each.",
            kannadaQuestionText = "ಹುರಿಯುವಿಕೆ (Roasting) ಮತ್ತು ಕಾಸುವಿಕೆ (Calcination) ನಡುವಿನ ವ್ಯತ್ಯಾಸಗಳನ್ನು ಒಂದೊಂದು ಸಮತೋಲಿತ ಸಮೀಕರಣದೊಂದಿಗೆ ಬರೆಯಿರಿ.",
            officialKeyAnswer = "Roasting: Heating sulphide ores strongly in excess air (2ZnS + 3O2 -> 2ZnO + 2SO2). Calcination: Heating carbonate ores strongly in limited air (ZnCO3 -> ZnO + CO2).",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Exam-2 2024 Official Key",
            officialPdfUrl = "",
            extractionConfidence = 0.97f
        ),
        PyqQuestion(
            id = "pyq_2025_m_c4_1",
            year = 2025,
            examType = PyqExamType.MODEL_PAPER,
            paperCode = "83-E-MQP",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_carbon",
            topicId = "chem_4_ethanol_soap",
            topicName = "Ethanol, Ethanoic Acid & Soaps",
            marks = 3,
            questionType = QuestionType.EXPLAIN_WHY,
            questionNumber = "Q29",
            questionText = "Explain the mechanism of the cleansing action of soaps. Why do soaps not form lather easily in hard water?",
            kannadaQuestionText = "ಸಾಬೂನಿನ ಸ್ವಚ್ಛಗೊಳಿಸುವ ಕ್ರಿಯೆಯನ್ನು ವಿವರಿಸಿ. ಕಠಿಣ ನೀರಿನಲ್ಲಿ ಸಾಬೂನು ಸುಲಭವಾಗಿ ನೊರೆ ಕೊಡುವುದಿಲ್ಲ ಏಕೆ?",
            officialKeyAnswer = "Soap molecules form spherical micelles: hydrophobic hydrocarbon tails attach to oily dirt inside, while hydrophilic ionic heads face water outside. Emulsion washes away dirt on rinsing. In hard water, Ca2+ and Mg2+ ions react with soap to form insoluble precipitate (scum).",
            hasOfficialKey = true,
            officialSource = "KSEAB Model Question Paper 2025 Key Answer",
            officialPdfUrl = "",
            extractionConfidence = 0.98f
        ),
        PyqQuestion(
            id = "pyq_2023_main_b5_1",
            year = 2023,
            examType = PyqExamType.MAIN,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.BIOLOGY,
            chapterId = "bio_life_processes",
            topicId = "bio_5_respiration_transport",
            topicName = "Human Heart & Double Circulation",
            marks = 4,
            questionType = QuestionType.DIAGRAM,
            questionNumber = "Q36",
            questionText = "Draw a neat labelled diagram of the sectional view of the human heart and label: (a) Aorta, (b) Left ventricle, (c) Pulmonary vein, (d) Vena cava.",
            kannadaQuestionText = "ಮಾನವನ ಹೃದಯದ ಛೇದನ ನೋಟದ ಅಂದವಾದ ಚಿತ್ರ ಬರೆದು ಕೆಳಗಿನ ಭಾಗಗಳನ್ನು ಗುರುತಿಸಿ: (a) ಮಹಾಧಮನಿ, (b) ಎಡ ಹೃತ್ಕುಕ್ಷಿ, (c) ಶ್ವಾಸಕೋಶದ ಅಭಿಧಮನಿ, (d) ಮಹಾ ಅಭಿಧಮನಿ.",
            officialKeyAnswer = "Diagram marking rubric: Clean outline (2 marks) + 4 accurate labels (4 × 0.5 = 2 marks). Ensure oxygenated left side and deoxygenated right side septum separation is clearly shown.",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Main 2023 Scheme of Valuation",
            officialPdfUrl = "",
            extractionConfidence = 0.99f
        ),
        PyqQuestion(
            id = "pyq_2024_e1_b6_1",
            year = 2024,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-K",
            medium = TextbookMedium.KANNADA,
            division = ScienceDivision.BIOLOGY,
            chapterId = "bio_control_coordination",
            topicId = "bio_6_nervous_brain",
            topicName = "Human Brain & Reflex Arc",
            marks = 3,
            questionType = QuestionType.DIAGRAM,
            questionNumber = "Q31",
            questionText = "Draw the diagram showing the structure of the human brain. Label Cerebrum and Medulla, and state one function of Cerebellum.",
            kannadaQuestionText = "ಮಾನವನ ಮೆದುಳಿನ ರಚನೆಯನ್ನು ತೋರಿಸುವ ಚಿತ್ರವನ್ನು ಬರೆದು 'ಮಹಾಮಸ್ತಿಷ್ಕ' ಮತ್ತು 'ಮೆಡುಲ್ಲಾ' ಭಾಗಗಳನ್ನು ಗುರುತಿಸಿ ಹಾಗೂ ಅನುಮಸ್ತಿಷ್ಕದ ಒಂದು ಕಾರ್ಯವನ್ನು ತಿಳಿಸಿ.",
            officialKeyAnswer = "Diagram (1.5M) + Cerebrum & Medulla labels (1M). Function of Cerebellum: Controls precision of voluntary actions and maintains posture and balance of the body (0.5M).",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Exam-1 2024 Kannada Scheme (83-K)",
            officialPdfUrl = "",
            extractionConfidence = 0.96f
        ),
        PyqQuestion(
            id = "pyq_2022_supp_b7_1",
            year = 2022,
            examType = PyqExamType.SUPPLEMENTARY,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.BIOLOGY,
            chapterId = "bio_reproduction",
            topicId = "bio_7_human_repro",
            topicName = "Placenta & Reproductive Health",
            marks = 2,
            questionType = QuestionType.DESCRIPTIVE,
            questionNumber = "Q23",
            questionText = "What is the role of the placenta in the development of a human embryo?",
            kannadaQuestionText = "ಮಾನವನ ಭ್ರೂಣದ ಬೆಳವಣಿಗೆಯಲ್ಲಿ ಜರಾಯುವಿನ (ಪ್ಲಾಸೆಂಟಾ) ಪಾತ್ರವೇನು?",
            officialKeyAnswer = "Placenta is a special disc-like tissue embedded in the uterine wall. It supplies glucose and oxygen from the mother's blood to the embryo via villi and removes metabolic wastes generated by the embryo.",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Supplementary 2022 Key",
            officialPdfUrl = "",
            extractionConfidence = 0.97f
        ),
        PyqQuestion(
            id = "pyq_2025_prep_b8_1",
            year = 2025,
            examType = PyqExamType.PREPARATORY,
            paperCode = "83-E-PREP",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.BIOLOGY,
            chapterId = "bio_heredity",
            topicId = "bio_8_mendel",
            topicName = "Mendel's Dihybrid Cross",
            marks = 3,
            questionType = QuestionType.DESCRIPTIVE,
            questionNumber = "Q30",
            questionText = "Write the checker board (Punnett square) for the F2 generation of Mendel's dihybrid cross between Round-Yellow (RRYY) and Wrinkled-Green (rryy) pea plants and state the phenotypic ratio.",
            kannadaQuestionText = "ದುಂಡನೆಯ-ಹಳದಿ (RRYY) ಮತ್ತು ಸುಕ್ಕಾದ-ಹಸಿರು (rryy) ಬಟಾಣಿ ಸಸ್ಯಗಳ ನಡುವಿನ ಮೆಂಡಲ್‌ನ ದ್ವಿತಳಿ ಸಂಕರಣದ F2 ಪೀಳಿಗೆಯ ಚೆಕರ್‌ಬೋರ್ಡ್ ಬರೆದು ವ್ಯಕ್ತರೂಪ ಅನುಪಾತ ತಿಳಿಸಿ.",
            officialKeyAnswer = null,
            hasOfficialKey = false,
            officialSource = "Karnataka State Preparatory Science Paper 2025 (Official key not published)",
            officialPdfUrl = "",
            extractionConfidence = 0.91f
        ),
        PyqQuestion(
            id = "pyq_2024_e1_p9_1",
            year = 2024,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_light",
            topicId = "phy_9_refraction_lenses",
            topicName = "Lens Formula Numerical",
            marks = 3,
            questionType = QuestionType.NUMERICAL,
            questionNumber = "Q14",
            questionText = "A concave lens has focal length of 15 cm. At what distance should the object from the lens be placed so that it forms an image at 10 cm from the lens? Also find the magnification produced.",
            kannadaQuestionText = "ಒಂದು ನಿಮ್ನ ಮಸೂರದ ಸಂಗಮದೂರ 15 cm ಆಗಿದೆ. ಮಸೂರದಿಂದ 10 cm ದೂರದಲ್ಲಿ ಪ್ರತಿಬಿಂಬ ಉಂಟಾಗಲು ವಸ್ತುವನ್ನು ಮಸೂರದಿಂದ ಎಷ್ಟು ದೂರದಲ್ಲಿ ಇರಿಸಬೇಕು? ವರ್ಧನೆಯನ್ನು ಕಂಡುಹಿಡಿಯಿರಿ.",
            officialKeyAnswer = "Given: f = -15 cm, v = -10 cm. Lens formula: 1/u = 1/v - 1/f = -1/10 - (-1/15) = -1/30 => u = -30 cm. Magnification m = v/u = (-10)/(-30) = +1/3 = +0.33 (virtual, erect, diminished).",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Exam-1 2024 Scheme of Valuation (83-E)",
            officialPdfUrl = "",
            extractionConfidence = 0.99f
        ),
        PyqQuestion(
            id = "pyq_2023_main_p10_1",
            year = 2023,
            examType = PyqExamType.MAIN,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_human_eye",
            topicId = "phy_10_dispersion_scattering",
            topicName = "Atmospheric Refraction & Scattering",
            marks = 2,
            questionType = QuestionType.EXPLAIN_WHY,
            questionNumber = "Q10",
            questionText = "Why do stars twinkle while planets do not twinkle?",
            kannadaQuestionText = "ನಕ್ಷತ್ರಗಳು ಮಿನುಗುತ್ತವೆ, ಆದರೆ ಗ್ರಹಗಳು ಮಿನುಗುವುದಿಲ್ಲ ಏಕೆ?",
            officialKeyAnswer = "Stars are point-sized distant sources; continuous atmospheric refraction fluctuates starlight entering our eyes causing twinkling. Planets are much closer and act as extended collections of many point sources whose total variation averages to zero.",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Main 2023 Scheme of Valuation",
            officialPdfUrl = "",
            extractionConfidence = 0.98f
        ),
        PyqQuestion(
            id = "pyq_2025_e1_p11_1",
            year = 2025,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_electricity",
            topicId = "phy_11_circuits_power",
            topicName = "Joule Heating & Commercial Energy",
            marks = 3,
            questionType = QuestionType.NUMERICAL,
            questionNumber = "Q15",
            questionText = "An electric refrigerator rated 400 W operates 8 hours/day. What is the cost of energy to operate it for 30 days at Rs 3.00 per kWh?",
            kannadaQuestionText = "400 W ಸಾಮರ್ಥ್ಯದ ರೆಫ್ರಿಜರೇಟರ್ ದಿನಕ್ಕೆ 8 ಗಂಟೆಗಳ ಕಾಲ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತದೆ. ಪ್ರತಿ kWh ಗೆ ರೂ. 3.00 ದರದಲ್ಲಿ 30 ದಿನಗಳಿಗೆ ತಗಲುವ ವಿದ್ಯುತ್ ಶಕ್ತಿಯ ವೆಚ್ಚವನ್ನು ಕಂಡುಹಿಡಿಯಿರಿ.",
            officialKeyAnswer = "Energy = 400 W × 8 h/day × 30 days = 96,000 Wh = 96 kWh. Cost = 96 × Rs 3.00 = Rs 288.00.",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Exam-1 2025 Scheme of Valuation",
            officialPdfUrl = "",
            extractionConfidence = 0.99f
        ),
        PyqQuestion(
            id = "pyq_2024_e3_p12_1",
            year = 2024,
            examType = PyqExamType.EXAM_3,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_magnetism",
            topicId = "phy_12_force_domestic",
            topicName = "Domestic Electric Circuits",
            marks = 2,
            questionType = QuestionType.EXPLAIN_WHY,
            questionNumber = "Q12",
            questionText = "What is the function of an earth wire? Why is it necessary to earth metallic appliances?",
            kannadaQuestionText = "ಅರ್ಥ್ ತಂತಿಯ ಕಾರ್ಯವೇನು? ಲೋಹದ ಹೊರಕವಚವಿರುವ ವಿದ್ಯುತ್ ಉಪಕರಣಗಳನ್ನು ಅರ್ಥ್ ಮಾಡುವುದು ಏಕೆ ಅವಶ್ಯಕ?",
            officialKeyAnswer = "Earth wire (green insulation) connects the metallic body of appliances to a metal plate buried deep in the earth. If live wire leaks current to the metallic body, earth wire provides a low-resistance path to ground keeping user potential equal to earth and preventing electric shock.",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Exam-3 2024 Official Key",
            officialPdfUrl = "",
            extractionConfidence = 0.97f
        ),
        PyqQuestion(
            id = "pyq_2025_mqp_b13_1",
            year = 2025,
            examType = PyqExamType.MODEL_PAPER,
            paperCode = "83-E-MQP",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.BIOLOGY,
            chapterId = "bio_environment",
            topicId = "bio_13_ozone_waste",
            topicName = "Ozone Layer & Biomagnification",
            marks = 2,
            questionType = QuestionType.DEFINITION,
            questionNumber = "Q28",
            questionText = "What is biological magnification? Name the chemical compound mainly responsible for ozone layer depletion.",
            kannadaQuestionText = "ಜೈವಿಕ ವರ್ಧನೆ ಎಂದರೇನು? ಓಝೋನ್ ಪದರದ ಸವಕಳಿಗೆ ಮುಖ್ಯವಾಗಿ ಕಾರಣವಾಗುವ ರಾಸಾಯನಿಕ ಸಂಯುಕ್ತವನ್ನು ಹೆಸರಿಸಿ.",
            officialKeyAnswer = "Biological magnification is the progressive increase in concentration of non-biodegradable harmful chemicals at each successive trophic level of a food chain. Main compound causing ozone depletion: Chlorofluorocarbons (CFCs).",
            hasOfficialKey = true,
            officialSource = "KSEAB Model Question Paper 2025 Key Answer",
            officialPdfUrl = "",
            extractionConfidence = 0.98f
        ),
        PyqQuestion(
            id = "pyq_2023_main_p11_mcq",
            year = 2023,
            examType = PyqExamType.MAIN,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_electricity",
            topicId = "phy_11_ohms_resistance",
            topicName = "SI Units & Ohm's Law",
            marks = 1,
            questionType = QuestionType.MCQ,
            questionNumber = "Q1",
            questionText = "The SI unit of electric potential difference is: (A) Ampere (B) Volt (C) Ohm (D) Watt",
            kannadaQuestionText = "ವಿದ್ಯುತ್ ವಿಭವಾಂತರದ SI ಏಕಮಾನ: (A) ಆಂಪಿಯರ್ (B) ವೋಲ್ಟ್ (C) ಓಮ್ (D) ವ್ಯಾಟ್",
            officialKeyAnswer = "(B) Volt",
            hasOfficialKey = true,
            officialSource = "KSEAB SSLC Main 2023 Key Answer",
            officialPdfUrl = "",
            extractionConfidence = 1.0f
        ),
        // Transcribed from the 2025 SSLC Science paper mirror. Kept under review until
        // each question is checked against the original KSEAB-hosted PDF and scheme.
        PyqQuestion(
            id = "pyq_2025_exam1_phy_q1",
            year = 2025,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_light",
            topicId = "phy_9_mirrors",
            topicName = "Uses of Convex Mirrors",
            marks = 1,
            questionType = QuestionType.MCQ,
            questionNumber = "Q1",
            questionText = "Convex mirrors are used in: (A) torches (B) rear-view mirrors of vehicles (C) search-lights (D) shaving mirrors.",
            kannadaQuestionText = "ಉಬ್ಬು ಕನ್ನಡಿಗಳನ್ನು ಯಾವುದರಲ್ಲಿ ಬಳಸುತ್ತಾರೆ? (A) ಟಾರ್ಚ್‌ಗಳು (B) ವಾಹನಗಳ ಹಿಂಬದಿ ನೋಟದ ಕನ್ನಡಿಗಳು (C) ಶೋಧ ದೀಪಗಳು (D) ಕ್ಷೌರ ಕನ್ನಡಿಗಳು.",
            officialKeyAnswer = null,
            hasOfficialKey = false,
            officialSource = "2025 SSLC Science paper mirror — AglaSem; original KSEAB PDF match pending",
            officialPdfUrl = "",
            extractionConfidence = 0.90f,
            paperReferenceUrl = "https://docs.aglasem.com/view/f2be977c-7c0d-11f0-8bbd-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2025_exam1_phy_q2",
            year = 2025,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_human_eye",
            topicId = "phy_10_dispersion_scattering",
            topicName = "Atmospheric Refraction",
            marks = 1,
            questionType = QuestionType.MCQ,
            questionNumber = "Q2",
            questionText = "The Sun is visible to us about two minutes before the actual sunrise and about two minutes after the actual sunset because of atmospheric: (A) refraction (B) reflection (C) scattering (D) dispersion of light.",
            kannadaQuestionText = "ನಿಜವಾದ ಸೂರ್ಯೋದಯಕ್ಕೆ ಸುಮಾರು ಎರಡು ನಿಮಿಷ ಮೊದಲು ಮತ್ತು ಸೂರ್ಯಾಸ್ತದ ನಂತರವೂ ಸೂರ್ಯ ಕಾಣಲು ಕಾರಣ ವಾತಾವರಣದ ಯಾವ ವಿದ್ಯಮಾನ?",
            officialKeyAnswer = null,
            hasOfficialKey = false,
            officialSource = "2025 SSLC Science paper mirror — AglaSem; original KSEAB PDF match pending",
            officialPdfUrl = "",
            extractionConfidence = 0.90f,
            paperReferenceUrl = "https://docs.aglasem.com/view/f2be977c-7c0d-11f0-8bbd-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2025_exam1_phy_q3",
            year = 2025,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_magnetism",
            topicId = "phy_12_force_domestic",
            topicName = "Force on a Current-Carrying Conductor",
            marks = 1,
            questionType = QuestionType.MCQ,
            questionNumber = "Q3",
            questionText = "For maximum displacement of a current-carrying rod placed between the poles of a horseshoe magnet, the angle between current and magnetic field must be: (A) 0° (B) 45° (C) 90° (D) 180°.",
            kannadaQuestionText = "ಕುದುರೆಲಾಳದ ಆಯಸ್ಕಾಂತದ ಧ್ರುವಗಳ ನಡುವೆ ಇಟ್ಟಿರುವ ವಿದ್ಯುತ್ ಪ್ರವಾಹವಿರುವ ದಂಡದ ಗರಿಷ್ಠ ಚಲನೆಗೆ ಪ್ರವಾಹ ಮತ್ತು ಆಯಸ್ಕಾಂತ ಕ್ಷೇತ್ರದ ನಡುವಿನ ಕೋನ ಎಷ್ಟು?",
            officialKeyAnswer = null,
            hasOfficialKey = false,
            officialSource = "2025 SSLC Science paper mirror — AglaSem; original KSEAB PDF match pending",
            officialPdfUrl = "",
            extractionConfidence = 0.90f,
            paperReferenceUrl = "https://docs.aglasem.com/view/f2be977c-7c0d-11f0-8bbd-0a5e36bc6706"
        ),
        // Transcribed from the 2024 SSLC Science paper mirror; answer keys are deliberately
        // left blank until the official scheme of valuation is matched.
        PyqQuestion(
            id = "pyq_2024_main_phy_q2",
            year = 2024,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_electricity",
            topicId = "phy_11_ohms_resistance",
            topicName = "Series and Parallel Resistance",
            marks = 1,
            questionType = QuestionType.MCQ,
            questionNumber = "Q2",
            questionText = "Four resistors of 2 Ω each are first connected in series and then in parallel. The ratio of equivalent resistances Rs/Rp is: (A) 16:1 (B) 2:1 (C) 4:1 (D) 8:1.",
            kannadaQuestionText = "2 Ω ಪ್ರತಿರೋಧದ ನಾಲ್ಕು ಪ್ರತಿರೋಧಕಗಳನ್ನು ಮೊದಲು ಸರಣಿಯಲ್ಲಿ ಮತ್ತು ನಂತರ ಸಮಾಂತರವಾಗಿ ಜೋಡಿಸಿದಾಗ ಸಮಾನ ಪ್ರತಿರೋಧಗಳ ಅನುಪಾತ Rs/Rp ಎಷ್ಟು?",
            officialKeyAnswer = null,
            hasOfficialKey = false,
            officialSource = "2024 SSLC Science paper mirror — AglaSem; original KSEAB PDF match pending",
            officialPdfUrl = "",
            extractionConfidence = 0.90f,
            paperReferenceUrl = "https://docs.aglasem.com/view/5463975c-c291-11ef-b8aa-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2024_main_phy_q3",
            year = 2024,
            examType = PyqExamType.EXAM_1,
            paperCode = "83-E",
            medium = TextbookMedium.ENGLISH,
            division = ScienceDivision.PHYSICS,
            chapterId = "phy_human_eye",
            topicId = "phy_10_dispersion_scattering",
            topicName = "Scattering of Light",
            marks = 1,
            questionType = QuestionType.MCQ,
            questionNumber = "Q3",
            questionText = "Which statement regarding the colour of scattered sunlight and the size of atmospheric particles is correct?",
            kannadaQuestionText = "ಚದುರಿದ ಸೂರ್ಯಪ್ರಕಾಶದ ಬಣ್ಣ ಮತ್ತು ವಾತಾವರಣದ ಕಣಗಳ ಗಾತ್ರದ ಕುರಿತು ಸರಿಯಾದ ಹೇಳಿಕೆ ಯಾವುದು?",
            officialKeyAnswer = null,
            hasOfficialKey = false,
            officialSource = "2024 SSLC Science paper mirror — AglaSem; original KSEAB PDF match pending",
            officialPdfUrl = "",
            extractionConfidence = 0.88f,
            paperReferenceUrl = "https://docs.aglasem.com/view/5463975c-c291-11ef-b8aa-0a5e36bc6706"
        ),

        // Transcribed question stems from the 2023 SSLC Science 83-E paper mirror.
        PyqQuestion(
            id = "pyq_2023_q1_ammeter",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.PHYSICS,
            chapterId = "phy_electricity", topicId = "phy_11_ohms_resistance",
            topicName = "Measuring Electric Current", marks = 1, questionType = QuestionType.MCQ,
            questionNumber = "Q1",
            questionText = "The device used to measure the rate of current in a circuit is: (A) Ammeter (B) Voltmeter (C) Galvanometer (D) Battery.",
            kannadaQuestionText = "ವಿದ್ಯುತ್ ಮಂಡಲದಲ್ಲಿ ವಿದ್ಯುತ್ ಪ್ರವಾಹವನ್ನು ಅಳೆಯುವ ಸಾಧನ ಯಾವುದು?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.95f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2023_q3_refraction",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.PHYSICS,
            chapterId = "phy_light", topicId = "phy_9_refraction_lenses",
            topicName = "Refraction from Denser to Rarer Medium", marks = 1,
            questionType = QuestionType.MCQ, questionNumber = "Q3",
            questionText = "A light ray enters a rarer medium from a denser medium. What happens to its speed and direction?",
            kannadaQuestionText = "ಬೆಳಕಿನ ಕಿರಣವು ಸಾಂದ್ರ ಮಾಧ್ಯಮದಿಂದ ವಿರಳ ಮಾಧ್ಯಮಕ್ಕೆ ಪ್ರವೇಶಿಸಿದಾಗ ಅದರ ವೇಗ ಮತ್ತು ದಿಕ್ಕಿನಲ್ಲಿ ಏನು ಬದಲಾವಣೆ ಆಗುತ್ತದೆ?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.94f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2023_q12_solenoid",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.PHYSICS,
            chapterId = "phy_magnetism", topicId = "phy_12_fields_solenoid",
            topicName = "Solenoid and Alternating Current", marks = 4,
            questionType = QuestionType.DESCRIPTIVE, questionNumber = "Q12",
            questionText = "What is a solenoid? Write the properties of magnetic field lines around a current-carrying solenoid. What is alternating current?",
            kannadaQuestionText = "ಸೊಲೆನಾಯ್ಡ್ ಎಂದರೇನು? ವಿದ್ಯುತ್ ಪ್ರವಾಹವಿರುವ ಸೊಲೆನಾಯ್ಡ್ ಸುತ್ತಲಿನ ಕಾಂತಕ್ಷೇತ್ರ ರೇಖೆಗಳ ಗುಣಗಳನ್ನು ಬರೆಯಿರಿ. ಪರ್ಯಾಯ ವಿದ್ಯುತ್ ಎಂದರೇನು?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.93f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2023_q18_cuso4",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_reactions", topicId = "chem_1_types",
            topicName = "Displacement Reaction", marks = 1,
            questionType = QuestionType.EXPLAIN_WHY, questionNumber = "Q18",
            questionText = "An iron nail is dropped into a test tube containing copper sulphate solution. The iron nail gradually turns brownish. Why?",
            kannadaQuestionText = "ತಾಮ್ರದ ಸಲ್ಫೇಟ್ ದ್ರಾವಣವಿರುವ ಪ್ರನಾಳದಲ್ಲಿ ಕಬ್ಬಿಣದ ಮೊಳೆಯನ್ನು ಹಾಕಿದಾಗ ಅದು ಕಂದು ಬಣ್ಣಕ್ಕೆ ತಿರುಗುತ್ತದೆ. ಏಕೆ?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.95f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2023_q19_hydrogenation",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_carbon", topicId = "chem_4_covalent_homologous",
            topicName = "Hydrogenation", marks = 1,
            questionType = QuestionType.DEFINITION, questionNumber = "Q19",
            questionText = "What is hydrogenation?",
            kannadaQuestionText = "ಹೈಡ್ರೋಜನೀಕರಣ ಎಂದರೇನು?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.96f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2023_q33_foodchain",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.BIOLOGY,
            chapterId = "bio_environment", topicId = "bio_13_ecosystem_energy",
            topicName = "Food Chain and Trophic Levels", marks = 2,
            questionType = QuestionType.DESCRIPTIVE, questionNumber = "Q33",
            questionText = "Give an example of a food chain in a grassland ecosystem. If the number of organisms at the second trophic level increases, how does this affect the food chain?",
            kannadaQuestionText = "ಹುಲ್ಲುಗಾವಲು ಪರಿಸರ ವ್ಯವಸ್ಥೆಯ ಆಹಾರ ಸರಪಳಿಗೆ ಒಂದು ಉದಾಹರಣೆ ನೀಡಿ. ಎರಡನೇ ಪೋಷಣಾ ಮಟ್ಟದ ಜೀವಿಗಳ ಸಂಖ್ಯೆ ಹೆಚ್ಚಾದರೆ ಆಹಾರ ಸರಪಳಿಯ ಮೇಲೆ ಏನು ಪರಿಣಾಮ ಉಂಟಾಗುತ್ತದೆ?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.94f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2023_q34_pollination",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.BIOLOGY,
            chapterId = "bio_reproduction", topicId = "bio_7_asexual_plant",
            topicName = "Pollination and Changes in a Flower", marks = 3,
            questionType = QuestionType.DESCRIPTIVE, questionNumber = "Q34",
            questionText = "What is pollination? What changes occur in a flower after pollination?",
            kannadaQuestionText = "ಪರಾಗಸ್ಪರ್ಶ ಎಂದರೇನು? ಪರಾಗಸ್ಪರ್ಶದ ನಂತರ ಹೂವಿನಲ್ಲಿ ಉಂಟಾಗುವ ಬದಲಾವಣೆಗಳು ಯಾವುವು?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.95f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2023_q37_brain",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.BIOLOGY,
            chapterId = "bio_control_coordination", topicId = "bio_6_nervous_brain",
            topicName = "Human Brain Diagram", marks = 4,
            questionType = QuestionType.DIAGRAM, questionNumber = "Q37",
            questionText = "Draw the diagram showing the structure of the human brain and label (i) Hypothalamus and (ii) Pons.",
            kannadaQuestionText = "ಮಾನವನ ಮೆದುಳಿನ ರಚನೆಯ ಚಿತ್ರವನ್ನು ಬಿಡಿಸಿ (i) ಹೈಪೋಥಾಲಮಸ್ ಮತ್ತು (ii) ಪಾನ್ಸ್ ಭಾಗಗಳನ್ನು ಗುರುತಿಸಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.96f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2023_q38_digestion",
            year = 2023, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.BIOLOGY,
            chapterId = "bio_life_processes", topicId = "bio_5_nutrition",
            topicName = "Digestion in the Stomach and Small Intestine", marks = 4,
            questionType = QuestionType.DESCRIPTIVE, questionNumber = "Q38",
            questionText = "Explain the digestion of food materials in the stomach and small intestine.",
            kannadaQuestionText = "ಜಠರ ಮತ್ತು ಸಣ್ಣ ಕರುಳಿನಲ್ಲಿ ಆಹಾರದ ಜೀರ್ಣಕ್ರಿಯೆಯನ್ನು ವಿವರಿಸಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2023 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.96f,
            paperReferenceUrl = "https://docs.aglasem.com/view/bc851700-9bfc-11ee-afd5-0a5e36bc6706"
        ),
        // Transcribed question stems from the 2022 SSLC Science 83-E paper mirror.
        PyqQuestion(
            id = "pyq_2022_q2_ohmslaw",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.PHYSICS,
            chapterId = "phy_electricity", topicId = "phy_11_ohms_resistance",
            topicName = "Ohm's Law", marks = 1,
            questionType = QuestionType.MCQ, questionNumber = "Q2",
            questionText = "Choose the correct formula showing the relationship between potential difference, electric current and resistance in an electric circuit.",
            kannadaQuestionText = "ವಿದ್ಯುತ್ ಮಂಡಲದಲ್ಲಿ ವಿಭವಾಂತರ, ವಿದ್ಯುತ್ ಪ್ರವಾಹ ಮತ್ತು ಪ್ರತಿರೋಧಗಳ ನಡುವಿನ ಸಂಬಂಧವನ್ನು ತೋರಿಸುವ ಸರಿಯಾದ ಸೂತ್ರವನ್ನು ಆಯ್ಕೆಮಾಡಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.94f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q5_fieldlines",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.PHYSICS,
            chapterId = "phy_magnetism", topicId = "phy_12_fields_solenoid",
            topicName = "Magnetic Field Lines", marks = 1,
            questionType = QuestionType.EXPLAIN_WHY, questionNumber = "Q5",
            questionText = "Magnetic field lines do not intersect each other. Why?",
            kannadaQuestionText = "ಕಾಂತಕ್ಷೇತ್ರ ರೇಖೆಗಳು ಒಂದನ್ನೊಂದು ಛೇದಿಸುವುದಿಲ್ಲ. ಏಕೆ?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.97f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q17_pop",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_acids_bases", topicId = "chem_2_ph_salts",
            topicName = "Plaster of Paris", marks = 1,
            questionType = QuestionType.DESCRIPTIVE, questionNumber = "Q17",
            questionText = "Write any two uses of Plaster of Paris.",
            kannadaQuestionText = "ಪ್ಲಾಸ್ಟರ್ ಆಫ್ ಪ್ಯಾರಿಸ್‌ನ ಯಾವುದೇ ಎರಡು ಉಪಯೋಗಗಳನ್ನು ಬರೆಯಿರಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.97f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q18_ethene",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_carbon", topicId = "chem_4_covalent_homologous",
            topicName = "Ethene Structure", marks = 1,
            questionType = QuestionType.CHEMICAL_EQUATION, questionNumber = "Q18",
            questionText = "Write the structural formula of the ethene molecule.",
            kannadaQuestionText = "ಈಥೀನ್ ಅಣುವಿನ ರಚನಾತ್ಮಕ ಸೂತ್ರವನ್ನು ಬರೆಯಿರಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.97f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q19_redox",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_metals", topicId = "chem_3_metallurgy",
            topicName = "Oxidation and Reduction", marks = 1,
            questionType = QuestionType.DESCRIPTIVE, questionNumber = "Q19",
            questionText = "For the reaction ZnO + C → Zn + CO, name the reactant that is oxidised and the reactant that is reduced.",
            kannadaQuestionText = "ZnO + C → Zn + CO ಕ್ರಿಯೆಯಲ್ಲಿ ಉತ್ಕರ್ಷಣಗೊಳ್ಳುವ ಮತ್ತು ಅಪಕರ್ಷಣಗೊಳ್ಳುವ ಅಭಿಕಾರಕಗಳನ್ನು ಹೆಸರಿಸಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.95f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q20_ph",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_acids_bases", topicId = "chem_2_ph_salts",
            topicName = "pH and Acidity", marks = 2,
            questionType = QuestionType.DESCRIPTIVE, questionNumber = "Q20",
            questionText = "Solutions A, B and C have pH values 5, 6 and 7 respectively. Which solution is more acidic? Give a reason.",
            kannadaQuestionText = "A, B ಮತ್ತು C ದ್ರಾವಣಗಳ pH ಮೌಲ್ಯಗಳು ಕ್ರಮವಾಗಿ 5, 6 ಮತ್ತು 7. ಯಾವ ದ್ರಾವಣ ಹೆಚ್ಚು ಆಮ್ಲೀಯವಾಗಿದೆ? ಕಾರಣ ನೀಡಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.96f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q25_equations",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.CHEMISTRY,
            chapterId = "chem_reactions", topicId = "chem_1_balancing",
            topicName = "Balancing Chemical Equations", marks = 3,
            questionType = QuestionType.CHEMICAL_EQUATION, questionNumber = "Q25",
            questionText = "Write balanced equations for: (i) thermal decomposition of calcium carbonate, (ii) hydrogen reacting with chlorine, and (iii) magnesium reacting with hydrochloric acid.",
            kannadaQuestionText = "ಕ್ಯಾಲ್ಸಿಯಂ ಕಾರ್ಬೊನೇಟ್‌ನ ಉಷ್ಣ ವಿಭಜನೆ, ಹೈಡ್ರೋಜನ್ ಮತ್ತು ಕ್ಲೋರಿನ್ ಕ್ರಿಯೆ, ಹಾಗೂ ಮೆಗ್ನೀಷಿಯಂ ಮತ್ತು ಹೈಡ್ರೋಕ್ಲೋರಿಕ್ ಆಮ್ಲದ ಕ್ರಿಯೆಗಳಿಗೆ ಸಮತೋಲಿತ ಸಮೀಕರಣಗಳನ್ನು ಬರೆಯಿರಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.95f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q29_decomposers",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.BIOLOGY,
            chapterId = "bio_environment", topicId = "bio_13_ecosystem_energy",
            topicName = "Decomposers in an Ecosystem", marks = 1,
            questionType = QuestionType.DEFINITION, questionNumber = "Q29",
            questionText = "What is the role of decomposers in an ecosystem?",
            kannadaQuestionText = "ಪರಿಸರ ವ್ಯವಸ್ಥೆಯಲ್ಲಿ ವಿಘಟಕಗಳ ಪಾತ್ರವೇನು?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.97f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q30_testes",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.BIOLOGY,
            chapterId = "bio_reproduction", topicId = "bio_7_human_repro",
            topicName = "Human Reproductive System", marks = 1,
            questionType = QuestionType.EXPLAIN_WHY, questionNumber = "Q30",
            questionText = "In males, testes are located outside the abdominal cavity in the scrotum. Why?",
            kannadaQuestionText = "ಪುರುಷರಲ್ಲಿ ವೃಷಣಗಳು ಉದರ ಕುಹರದ ಹೊರಗೆ ವೃಷಣಕೋಶದಲ್ಲಿರುತ್ತವೆ. ಏಕೆ?",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.97f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        ),
        PyqQuestion(
            id = "pyq_2022_q31_hormones",
            year = 2022, examType = PyqExamType.MAIN, paperCode = "83-E",
            medium = TextbookMedium.ENGLISH, division = ScienceDivision.BIOLOGY,
            chapterId = "bio_control_coordination", topicId = "bio_6_hormones",
            topicName = "Plant Hormones", marks = 2,
            questionType = QuestionType.DESCRIPTIVE, questionNumber = "Q31",
            questionText = "Mention the functions of the plant hormones (i) Auxin and (ii) Cytokinin.",
            kannadaQuestionText = "(i) ಆಕ್ಸಿನ್ ಮತ್ತು (ii) ಸೈಟೋಕೈನಿನ್ ಸಸ್ಯ ಹಾರ್ಮೋನುಗಳ ಕಾರ್ಯಗಳನ್ನು ತಿಳಿಸಿ.",
            officialKeyAnswer = null, hasOfficialKey = false,
            officialSource = "2022 SSLC Science 83-E paper mirror; answer key not matched",
            officialPdfUrl = "", extractionConfidence = 0.97f,
            paperReferenceUrl = "https://docs.aglasem.com/view/447726fc-5f41-11ed-8152-0a5e36bc6706"
        )
    ).map { it.copy(flaggedForReview = true) }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(6, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS)
            .build()
    }

    suspend fun checkPaperArchiveIndexLive(): PyqDiscoveryStatus = withContext(Dispatchers.IO) {
        val archiveCount = paperArchive.size
        try {
            val req = Request.Builder()
                .url("https://previousyearquestions.app/karnataka-sslc/2026/")
                .head()
                .build()
            httpClient.newCall(req).execute().use { resp ->
                val reachable = resp.isSuccessful || resp.code in 200..399
                PyqDiscoveryStatus(
                    lastSyncedLabel = if (reachable) "PYQ archive index reachable" else "Offline archive catalogue available",
                    archiveIndexReachable = reachable,
                    archiveCollectionsCount = archiveCount,
                    message = if (reachable) {
                        "$archiveCount archive entries cover 2020–2026: KSEAB paper indexes for 2024–2026, third-party document mirrors for 2020–2023, and separate historical/current model-paper entries. 2026 includes English/Kannada papers with model answers; 2024/2025 indexes do not list official keys."
                    } else {
                        "$archiveCount paper collections are saved in the app. The online archive index returned HTTP ${resp.code}; open a collection when connected."
                    }
                )
            }
        } catch (_: Exception) {
            PyqDiscoveryStatus(
                lastSyncedLabel = "Offline archive catalogue available",
                archiveIndexReachable = false,
                archiveCollectionsCount = archiveCount,
                message = "$archiveCount paper collections are saved in the app. Connect to the internet to open the linked paper PDFs and answer keys."
            )
        }
    }

    fun computeAnalytics(questions: List<PyqQuestion> = allQuestions): PyqAnalyticsSummary {
        val divisionCounts = ScienceDivision.entries.associateWith { div ->
            questions.count { it.division == div }
        }
        val chapterFreq = questions.groupingBy { it.chapterId }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { it.key to it.value }
        val marksDist = questions.groupingBy { it.marks }.eachCount().toSortedMap()
        fun topLabelsFor(type: QuestionType): List<String> =
            questions.filter { it.questionType == type }
                .groupingBy { it.topicName }
                .eachCount()
                .entries
                .sortedByDescending { it.value }
                .take(4)
                .map { "${it.key} (${it.value} question(s))" }

        val limitedSamplePredictions = chapterFreq.take(3).map { (chapterId, count) ->
            val chapterTitle = SyllabusRepository.chapters.firstOrNull { it.id == chapterId }?.titleEn ?: chapterId
            "Prediction (Not guaranteed; limited local sample): $chapterTitle appears in $count question-bank entries."
        }.ifEmpty {
            listOf("Prediction (Not guaranteed): insufficient question data for a useful pattern.")
        }

        return PyqAnalyticsSummary(
            totalQuestions = questions.size,
            verifiedOfficialKeyCount = questions.count { it.hasOfficialKey && !it.flaggedForReview },
            divisionCounts = divisionCounts,
            topRepeatedChapters = chapterFreq,
            topMarksDistribution = marksDist,
            topDiagrams = topLabelsFor(QuestionType.DIAGRAM).ifEmpty { listOf("No diagram questions tagged yet.") },
            topReactions = topLabelsFor(QuestionType.CHEMICAL_EQUATION).ifEmpty { listOf("No chemical-equation questions tagged yet.") },
            topNumericals = topLabelsFor(QuestionType.NUMERICAL).ifEmpty { listOf("No numerical questions tagged yet.") },
            probablePredictionTopics = limitedSamplePredictions
        )
    }
}
