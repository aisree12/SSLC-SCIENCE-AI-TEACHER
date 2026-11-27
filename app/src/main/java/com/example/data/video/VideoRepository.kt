package com.example.data.video

import com.example.model.ScienceVideo
import com.example.model.StudyLanguage
import com.example.model.VideoCategory
import com.example.model.VideoReviewStatus
import com.example.model.YouTubeResourceType

object VideoRepository {
    val catalog: List<ScienceVideo> = listOf(
        ScienceVideo(
            id = "vid_chem1_en",
            chapterId = "chem_reactions",
            topicId = "chem_1_balancing",
            title = "Balancing Chemical Equations & Reaction Types | SSLC Class 10",
            channel = "Karnataka Digital Classroom Science",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=2S6e11NBwiw",
            category = VideoCategory.CHEMICAL_REACTIONS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "14:20"
        ),
        ScienceVideo(
            id = "vid_chem1_kn",
            chapterId = "chem_reactions",
            topicId = "chem_1_types",
            title = "ರಾಸಾಯನಿಕ ಕ್ರಿಯೆಗಳು ಮತ್ತು ಸಮೀಕರಣಗಳು | SSLC Science Kannada",
            channel = "DIKSHA Karnataka Science",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=yA3ETfX4x4Y",
            category = VideoCategory.KANNADA_EXPLANATION,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.NEEDS_REVIEW,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "18:10"
        ),
        ScienceVideo(
            id = "vid_chem2_ph",
            chapterId = "chem_acids_bases",
            topicId = "chem_2_ph_salts",
            title = "Acids, Bases, pH Scale & Salts Full Revision",
            channel = "NCERT Class 10 Visual Science",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=mnbS56HQbaU",
            category = VideoCategory.FULL_CHAPTER,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "21:45"
        ),
        ScienceVideo(
            id = "vid_chem3_metals",
            chapterId = "chem_metals",
            topicId = "chem_3_metallurgy",
            title = "Reactivity Series & Metallurgy (Roasting vs Calcination)",
            channel = "SSLC Chemistry Academy",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=8s0L8X7v7A8",
            category = VideoCategory.TOPIC_WISE,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "16:30"
        ),
        ScienceVideo(
            id = "vid_chem4_carbon",
            chapterId = "chem_carbon",
            topicId = "chem_4_covalent_homologous",
            title = "Carbon and its Compounds – Homologous Series & Micelles",
            channel = "Class 10 Chemistry Hub",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=Z8f2rG8nL1k",
            category = VideoCategory.ENGLISH_EXPLANATION,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.NEEDS_REVIEW,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "19:05"
        ),
        ScienceVideo(
            id = "vid_bio5_heart",
            chapterId = "bio_life_processes",
            topicId = "bio_5_respiration_transport",
            title = "Human Heart Double Circulation & Nephron Diagram",
            channel = "Biology Visual Studio",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=oHMmtqKgs50",
            category = VideoCategory.BIOLOGY_DIAGRAMS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "12:15"
        ),
        ScienceVideo(
            id = "vid_bio6_brain",
            chapterId = "bio_control_coordination",
            topicId = "bio_6_nervous_brain",
            title = "Reflex Arc & Human Brain Parts – Board Exam Guide",
            channel = "SSLC Biology Karnataka",
            language = StudyLanguage.BILINGUAL,
            youtubeUrl = "https://www.youtube.com/watch?v=kMKc8nfPATI",
            category = VideoCategory.BOARD_QUESTIONS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "15:40"
        ),
        ScienceVideo(
            id = "vid_bio7_repro",
            chapterId = "bio_reproduction",
            topicId = "bio_7_asexual_plant",
            title = "Pollination & Fertilization in Flowering Plants",
            channel = "Biology Visual Studio",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=HLYOZJk4QQ0",
            category = VideoCategory.TOPIC_WISE,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "11:50"
        ),
        ScienceVideo(
            id = "vid_bio8_heredity",
            chapterId = "bio_heredity",
            topicId = "bio_8_mendel",
            title = "Mendel's Monohybrid & Dihybrid Cross + Sex Determination",
            channel = "Genetics Made Simple",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=Mehz7tCxjSE",
            category = VideoCategory.QUICK_REVISION,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "13:10"
        ),
        ScienceVideo(
            id = "vid_phy9_optics",
            chapterId = "phy_light",
            topicId = "phy_9_mirrors",
            title = "Mirror & Lens Formula Numericals – KSEAB PYQ Solutions",
            channel = "SSLC Physics Lab",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=Oh4m8Ees-3Q",
            category = VideoCategory.PHYSICS_NUMERICALS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "17:25"
        ),
        ScienceVideo(
            id = "vid_phy10_eye",
            chapterId = "phy_human_eye",
            topicId = "phy_10_eye_defects",
            title = "Myopia, Hypermetropia & Atmospheric Scattering Explained",
            channel = "Physics Animated",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=syaQGMxb5i0",
            category = VideoCategory.TOPIC_WISE,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "10:45"
        ),
        ScienceVideo(
            id = "vid_phy11_ohms",
            chapterId = "phy_electricity",
            topicId = "phy_11_ohms_resistance",
            title = "Ohm's Law, Series & Parallel Circuits Numericals",
            channel = "SSLC Physics Lab",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=HsLLq6Rm5tU",
            category = VideoCategory.PHYSICS_NUMERICALS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.APPROVED,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "14:55"
        ),
        ScienceVideo(
            id = "vid_phy12_mag",
            chapterId = "phy_magnetism",
            topicId = "phy_12_fields_solenoid",
            title = "Solenoid, Fleming's Left-Hand Rule & Domestic Circuits",
            channel = "Physics Animated",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=9Q4p1wP8bYk",
            category = VideoCategory.PYQ_SOLUTIONS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.NEEDS_REVIEW,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "15:10"
        ),
        ScienceVideo(
            id = "vid_bio13_env",
            chapterId = "bio_environment",
            topicId = "bio_13_ecosystem_energy",
            title = "10% Energy Law, Biomagnification & Ozone Depletion",
            channel = "Ecology Class 10",
            language = StudyLanguage.ENGLISH,
            youtubeUrl = "https://www.youtube.com/watch?v=Ln5kL5nF4rE",
            category = VideoCategory.QUICK_REVISION,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.NEEDS_REVIEW,
            lastVerifiedDate = "2026-11-01",
            durationLabel = "11:20"
        )
    ).map { video ->
        // Catalogue URLs have not yet been individually checked on-device and against
        // current syllabus coverage; do not present seeded links as verified.
        video.copy(reviewStatus = VideoReviewStatus.NEEDS_REVIEW)
    }

    fun videosForChapter(chapterId: String, category: VideoCategory? = null): List<ScienceVideo> {
        return catalog.filter { it.chapterId == chapterId && (category == null || it.category == category) }
    }

    fun availableCategoriesForChapter(chapterId: String): List<VideoCategory> {
        return catalog.filter { it.chapterId == chapterId }.map { it.category }.distinct()
    }
}
