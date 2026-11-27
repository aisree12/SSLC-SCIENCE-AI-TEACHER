package com.example.data.video

import com.example.model.ScienceVideo
import com.example.model.StudyLanguage
import com.example.model.VideoCategory
import com.example.model.VideoReviewStatus
import com.example.model.YouTubeResourceType

object VideoRepository {
    /**
     * Curated Karnataka SSLC Science links with title/channel metadata checked against
     * the YouTube result pages on 2026-11-27. DISCOVERED means the video metadata was
     * found; playback, embedding permission, exact chapter completeness and duration
     * have NOT been tested in this app. Do not promote these entries to APPROVED
     * until playback and syllabus review are completed on a device.
     */
    val catalog: List<ScienceVideo> = listOf(
        ScienceVideo(
            id = "vid_chem1_parikshe",
            chapterId = "chem_reactions",
            topicId = "chem_1_balancing",
            title = "Chemical Reactions and Equations - Full Chapter in One Shot | SSLC 2025 Karnataka",
            channel = "SSLC @ PARIKSHE",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=njzlDeGqW5Y",
            category = VideoCategory.CHEMICAL_REACTIONS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_chem2_pw",
            chapterId = "chem_acids_bases",
            topicId = "chem_2_ph_salts",
            title = "Acids, Bases and Salts in One Shot | Chemistry | SSLC Karnataka Board",
            channel = "PW Karnataka 9th & SSLC",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=G5MKKQxn1oI",
            category = VideoCategory.FULL_CHAPTER,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_chem3_parikshe",
            chapterId = "chem_metals",
            topicId = "chem_3_metallurgy",
            title = "Metals and Non-metals | One Shot Revision | Karnataka SSLC Class 10",
            channel = "SSLC @ PARIKSHE",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=JF6xm_gGPCc",
            category = VideoCategory.TOPIC_WISE,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_chem4_parikshe",
            chapterId = "chem_carbon",
            topicId = "chem_4_covalent_homologous",
            title = "Carbon and its Compounds | SSLC Karnataka | Parikshe One Shot",
            channel = "SSLC @ PARIKSHE",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=wnjBVHSq0l0",
            category = VideoCategory.KANNADA_EXPLANATION,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_bio5_pw",
            chapterId = "bio_life_processes",
            topicId = "bio_5_nutrition",
            title = "Life Processes in One Shot | Biology | SSLC Karnataka Board",
            channel = "PW Karnataka 9th & SSLC",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=umAd1q1sKdM",
            category = VideoCategory.FULL_CHAPTER,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_bio6_parikshe",
            chapterId = "bio_control_coordination",
            topicId = "bio_6_nervous_brain",
            title = "Control and Coordination | Niyantrana Mattu Sahabhagitva | SSLC Karnataka",
            channel = "SSLC @ PARIKSHE",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=gKdynHDDWi8",
            category = VideoCategory.BOARD_QUESTIONS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_bio7_pw",
            chapterId = "bio_reproduction",
            topicId = "bio_7_human_repro",
            title = "How do Organisms Reproduce in One Shot | Biology | SSLC Karnataka Board",
            channel = "PW Karnataka 9th & SSLC",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=TZVNOtu9X_g",
            category = VideoCategory.FULL_CHAPTER,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_bio8_pw",
            chapterId = "bio_heredity",
            topicId = "bio_8_mendel",
            title = "Heredity in One Shot | Biology | SSLC Karnataka Board",
            channel = "PW Karnataka 9th & SSLC",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=5vWac59gT6g",
            category = VideoCategory.QUICK_REVISION,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_phy9_parikshe",
            chapterId = "phy_light",
            topicId = "phy_9_mirrors",
            title = "Light Reflection and Refraction | Science One Shot | SSLC Karnataka",
            channel = "SSLC @ PARIKSHE",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=2jHKDO7Hofg",
            category = VideoCategory.PHYSICS_NUMERICALS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_phy10_pw",
            chapterId = "phy_human_eye",
            topicId = "phy_10_eye_defects",
            title = "The Human Eye and Its Colourful World in One Shot | SSLC Karnataka Board",
            channel = "PW Karnataka 9th & SSLC",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=b3vyRYAALBs",
            category = VideoCategory.FULL_CHAPTER,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_phy11_pw",
            chapterId = "phy_electricity",
            topicId = "phy_11_ohms_resistance",
            title = "Electricity in One Shot | Physics | SSLC Karnataka Board",
            channel = "PW Karnataka 9th & SSLC",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=MTLNJyqtusY",
            category = VideoCategory.PHYSICS_NUMERICALS,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_phy12_parikshe",
            chapterId = "phy_magnetism",
            topicId = "phy_12_fields_solenoid",
            title = "Magnetic Effects of Electric Current | SSLC Karnataka | Parikshe",
            channel = "SSLC @ PARIKSHE",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=y8ESc5oJNkc",
            category = VideoCategory.FULL_CHAPTER,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        ),
        ScienceVideo(
            id = "vid_bio13_parikshe",
            chapterId = "bio_environment",
            topicId = "bio_13_ecosystem_energy",
            title = "Our Environment | Namma Parisara | Science One Shot | SSLC Karnataka",
            channel = "SSLC @ PARIKSHE",
            language = StudyLanguage.KANNADA,
            youtubeUrl = "https://www.youtube.com/watch?v=p2qTgDL6sfQ",
            category = VideoCategory.FULL_CHAPTER,
            resourceType = YouTubeResourceType.VIDEO,
            reviewStatus = VideoReviewStatus.DISCOVERED,
            lastVerifiedDate = "2026-11-27 metadata checked; playback pending",
            durationLabel = "Not checked"
        )
    )

    fun videosForChapter(chapterId: String, category: VideoCategory? = null): List<ScienceVideo> {
        return catalog.filter { it.chapterId == chapterId && (category == null || it.category == category) }
    }

    fun availableCategoriesForChapter(chapterId: String): List<VideoCategory> {
        return catalog.filter { it.chapterId == chapterId }.map { it.category }.distinct()
    }
}
