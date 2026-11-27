package com.example.data.curriculum

import com.example.model.ActivityEntry
import com.example.model.ChapterQuizQuestion
import com.example.model.ChemicalReactionEntry
import com.example.model.DefinitionEntry
import com.example.model.FaqEntry
import com.example.model.ScienceDivision
import com.example.model.ScienceTopic
import com.example.model.SyllabusChapter

object BiologyChapters {
    val all: List<SyllabusChapter> = listOf(
        SyllabusChapter(
            id = "bio_life_processes",
            chapterNumber = 5,
            textbookPart = 1,
            division = ScienceDivision.BIOLOGY,
            titleEn = "Life Processes",
            titleKn = "ಜೀವಕ್ರಿಯೆಗಳು",
            introduction = "Covers the essential maintenance processes in living organisms: Autotrophic and Heterotrophic Nutrition, Aerobic and Anaerobic Respiration, Transportation in humans (Heart & Double Circulation) and plants (Xylem & Phloem), and Excretion (Nephron).",
            objectives = listOf(
                "Explain photosynthesis steps, equation, and role of stomata.",
                "Trace human digestion enzymes (amylase, pepsin, trypsin, lipase) and villi absorption.",
                "Compare aerobic and anaerobic respiration pathways in pyruvate breakdown.",
                "Describe human heart double circulation, xylem/phloem transport, and nephron urine formation."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "bio_5_nutrition",
                    title = "Nutrition in Plants & Humans",
                    kannadaTitle = "ಸಸ್ಯಗಳಲ್ಲಿ ಮತ್ತು ಮಾನವರಲ್ಲಿ ಪೋಷಣೆ",
                    simpleExplanation = "Plants make glucose from CO2 and H2O using sunlight and chlorophyll (Photosynthesis). Humans digest food along the alimentary canal using enzymes and absorb nutrients in small-intestine villi.",
                    detailedExplanation = "Photosynthesis: 6CO2 + 12H2O --(Chlorophyll/Sunlight)--> C6H12O6 + 6O2 + 6H2O. In human digestion: salivary amylase breaks starch to sugar; gastric HCl activates pepsin for protein; bile emulsifies fats; pancreatic trypsin, amylase, lipase complete digestion in small intestine.",
                    kannadaSummary = "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆಯಿಂದ ಸಸ್ಯಗಳು ಆಹಾರ ತಯಾರಿಸುತ್ತವೆ; ಮಾನವನ ಸಣ್ಣ ಕರುಳಿನಲ್ಲಿ ಜೀರ್ಣಕ್ರಿಯೆ ಪೂರ್ಣಗೊಳ್ಳುತ್ತದೆ.",
                    observations = listOf("Iodine turns starch-containing green leaf regions blue-black."),
                    dailyLifeApplications = listOf("Dental plaque and balanced fibre-rich diet.")
                ),
                ScienceTopic(
                    id = "bio_5_respiration_transport",
                    title = "Respiration, Transportation & Excretion",
                    kannadaTitle = "ಉಸಿರಾಟ, ಸಾಗಾಣಿಕೆ ಮತ್ತು ವಿಸರ್ಜನೆ",
                    simpleExplanation = "Cells release ATP by breaking down glucose. The 4-chambered heart pumps blood twice per cycle (double circulation), while nephrons in kidneys filter nitrogenous wastes like urea.",
                    detailedExplanation = "Pyruvate breaks down anaerobically in yeast (ethanol + CO2 + 2 ATP), in muscle cramps (lactic acid), or aerobically in mitochondria (CO2 + H2O + 38 ATP). Double circulation separates oxygenated and deoxygenated blood for high energy needs. Xylem transports water/minerals upward via transpiration pull; Phloem translocates food bidirectionally using ATP. Nephrons filter blood at Bowman's capsule, reabsorb glucose/amino acids/water, and form urine.",
                    kannadaSummary = "ಮಾನವನ ಹೃದಯದಲ್ಲಿ ದ್ವಿ-ಪರಿಚಲನೆ ನಡೆಯುತ್ತದೆ; ನೆಫ್ರಾನ್ ಮೂತ್ರಪಿಂಡದ ರಚನಾತ್ಮಕ ಮತ್ತು ಕ್ರಿಯಾತ್ಮಕ ಘಟಕವಾಗಿದೆ.",
                    observations = listOf("Exhaled air turns lime water milky much faster than inhaled air."),
                    dailyLifeApplications = listOf("Dialysis (artificial kidney) filters blood when kidneys fail.", "Sphygmomanometer measures normal blood pressure 120/80 mmHg.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Double Circulation", "ದ್ವಿ-ಪರಿಚಲನೆ", "Blood flows through the human heart twice during each complete cycle (pulmonary and systemic circuits).", "ಒಂದು ಪೂರ್ಣ ರಕ್ತ ಪರಿಚಲನೆಯಲ್ಲಿ ರಕ್ತವು ಹೃದಯದ ಮೂಲಕ ಎರಡು ಬಾರಿ ಹರಿಯುವುದು.", "Ch 5, Sec 5.4.1"),
                DefinitionEntry("Translocation", "ಸ್ಥಾನಾಂತರಣ", "Transport of soluble products of photosynthesis from leaves to other plant parts via phloem.", "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆಯ ಉತ್ಪನ್ನಗಳು ಫ್ಲೋಯಂ ಮೂಲಕ ಸಸ್ಯದ ಇತರ ಭಾಗಗಳಿಗೆ ಸಾಗಣೆಯಾಗುವುದು.", "Ch 5, Sec 5.4.2")
            ),
            formulas = emptyList(),
            reactions = listOf(
                ChemicalReactionEntry("Photosynthesis", "6CO2 + 12H2O -> C6H12O6 + 6O2 + 6H2O", "Anabolic Photochemical", "Glucose stored as starch; O2 released from water", "Sunlight + Chlorophyll")
            ),
            activities = listOf(
                ActivityEntry("Activity 5.1", "Chlorophyll is Essential for Photosynthesis", "Test variegated Croton leaf with iodine after boiling in alcohol.", "Only originally green areas turn blue-black.", "Chlorophyll is essential for starch synthesis.")
            ),
            revisionNotes = listOf(
                "Alveoli provide large surface area for O2/CO2 gas exchange; haemoglobin transports O2.",
                "Arteries have thick elastic walls without valves; Veins have thin walls with valves.",
                "Nephron parts: Glomerulus, Bowman's capsule, tubular part, collecting duct."
            ),
            faqs = listOf(
                FaqEntry("Why is it necessary to separate oxygenated and deoxygenated blood in mammals and birds?", "Warm-blooded animals need high energy to maintain constant body temperature, requiring efficient oxygen supply.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c5_q1",
                    question = "The xylem in plants are responsible for:",
                    options = listOf("Transport of water", "Transport of food", "Transport of amino acids", "Transport of oxygen"),
                    correctIndex = 0,
                    explanation = "Xylem transports water and dissolved minerals from roots to aerial parts.",
                    marks = 1,
                    topicId = "bio_5_respiration_transport"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 1, Chapter 5",
            simulationIds = listOf("sim_bio_heart", "sim_bio_nephron"),
            diagramIds = listOf("diag_human_heart", "diag_nephron", "diag_stomata")
        ),
        SyllabusChapter(
            id = "bio_control_coordination",
            chapterNumber = 6,
            textbookPart = 1,
            division = ScienceDivision.BIOLOGY,
            titleEn = "Control and Coordination",
            titleKn = "ನಿಯಂತ್ರಣ ಮತ್ತು ಸಮನ್ವಯತೆ",
            introduction = "Explains how animals coordinate responses via neurons, reflex arcs, and the human brain alongside endocrine hormones, and how plants respond through tropic movements and phytohormones.",
            objectives = listOf(
                "Describe neuron structure, synapse transmission, and reflex arc pathway.",
                "Identify functions of Forebrain (cerebrum), Midbrain, and Hindbrain (cerebellum, pons, medulla).",
                "Explain plant tropisms (phototropism, geotropism, hydrotropism, chemotropism) and plant hormones.",
                "List endocrine glands, hormones (adrenaline, thyroxin, insulin, growth hormone), and feedback regulation."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "bio_6_nervous_brain",
                    title = "Nervous System, Reflex Arc & Human Brain",
                    kannadaTitle = "ನರಮಂಡಲ, ಪರಾವರ್ತಿತ ಚಾಪ ಮತ್ತು ಮಾನವನ ಮೆದುಳು",
                    simpleExplanation = "Neurons carry electrical impulses across synapses using chemical neurotransmitters. Reflex arcs process sudden dangers instantly at the spinal cord before thinking.",
                    detailedExplanation = "Reflex arc: Receptor -> Sensory Neuron -> Spinal Cord (Relay neuron) -> Motor Neuron -> Effector muscle. Human Brain: Forebrain (Cerebrum: thinking, memory, sensory interpretation), Hindbrain (Cerebellum: posture & balance; Medulla: involuntary actions like blood pressure, salivation, vomiting; Pons: respiration regulation).",
                    kannadaSummary = "ನರಕೋಶವು ನರಮಂಡಲದ ಮೂಲ ಘಟಕ. ಪರಾವರ್ತಿತ ಕ್ರಿಯೆಗಳನ್ನು ಮೆದುಳುಬಳ್ಳಿ ನಿಯಂತ್ರಿಸುತ್ತದೆ; ಅನುಮಸ್ತಿಷ್ಕ ಸಮತೋಲನ ಕಾಪಾಡುತ್ತದೆ.",
                    observations = listOf("Pulling hand back immediately from a hot pan."),
                    dailyLifeApplications = listOf("Helmets protect the cranium and cerebrospinal fluid around the brain.")
                ),
                ScienceTopic(
                    id = "bio_6_hormones",
                    title = "Coordination in Plants & Animal Hormones",
                    kannadaTitle = "ಸಸ್ಯಗಳಲ್ಲಿ ಸಮನ್ವಯತೆ ಮತ್ತು ಪ್ರಾಣಿ ಹಾರ್ಮೋನುಗಳು",
                    simpleExplanation = "Plants move by growth (tropisms) guided by hormones like Auxin, Gibberellin, Cytokinin, and Abscisic acid. Animals use endocrine glands releasing hormones directly into blood.",
                    detailedExplanation = "Auxin promotes cell elongation on shaded stem side causing phototropism. Abscisic acid inhibits growth (wilting). Endocrine hormones: Insulin (pancreas, lowers blood sugar; deficiency causes diabetes), Thyroxin (thyroid, needs iodine, regulates metabolism; deficiency causes goitre), Adrenaline (fight-or-flight response), Growth hormone (pituitary).",
                    kannadaSummary = "ಆಕ್ಸಿನ್ ಸಸ್ಯದ ಬೆಳವಣಿಗೆಗೆ, ಇನ್ಸುಲಿನ್ ರಕ್ತದ ಸಕ್ಕರೆ ನಿಯಂತ್ರಣಕ್ಕೆ ಮತ್ತು ಥೈರಾಕ್ಸಿನ್ ಚಯಾಪಚಯ ಕ್ರಿಯೆಗೆ ಅಗತ್ಯ.",
                    observations = listOf("Touch-me-not (Mimosa pudica) leaves fold rapidly via turgor pressure change."),
                    dailyLifeApplications = listOf("Iodised salt prevents goitre by supporting thyroxin synthesis.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Synapse", "ಸಂಸರ್ಗ (ಸಿನಾಪ್ಸ್)", "Microscopic gap between two adjacent neurons across which nerve impulses pass via chemical messengers.", "ಎರಡು ಪಕ್ಕದ ನರಕೋಶಗಳ ನಡುವಿನ ಸೂಕ್ಷ್ಮ ಅಂತರ.", "Ch 6, Sec 6.1"),
                DefinitionEntry("Reflex Arc", "ಪರಾವರ್ತಿತ ಚಾಪ", "Neural pathway taken by nerve impulses in a reflex action from receptor to effector via spinal cord.", "ಪರಾವರ್ತಿತ ಕ್ರಿಯೆಯಲ್ಲಿ ನರ ಆವೇಗಗಳು ಗ್ರಾಹಕದಿಂದ ಕಾರ್ಯನಿರ್ವಾಹಕಕ್ಕೆ ಸಾಗುವ ಮಾರ್ಗ.", "Ch 6, Sec 6.1.1")
            ),
            formulas = emptyList(),
            reactions = emptyList(),
            activities = listOf(
                ActivityEntry("Activity 6.2", "Phototropism & Geotropism in Potted Plant", "Place germinating plant horizontally near sunlight window.", "Shoots bend toward light; roots bend downward with gravity.", "Shoots are positively phototropic; roots are positively geotropic.")
            ),
            revisionNotes = listOf(
                "Cerebellum controls precision of voluntary actions and body balance.",
                "Medulla controls involuntary actions (heartbeat, breathing, BP).",
                "Feedback mechanism regulates insulin secretion according to blood glucose levels."
            ),
            faqs = listOf(
                FaqEntry("Why is the use of iodised salt advisable?", "Iodine is essential for the thyroid gland to synthesize thyroxin hormone; deficiency causes goitre.", "1–2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c6_q1",
                    question = "The gap between two neurons is called a:",
                    options = listOf("Dendrite", "Synapse", "Axon", "Impulse"),
                    correctIndex = 1,
                    explanation = "A synapse is the functional junction gap between the axon terminal of one neuron and dendrite of the next.",
                    marks = 1,
                    topicId = "bio_6_nervous_brain"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 1, Chapter 6",
            simulationIds = listOf("sim_bio_reflex"),
            diagramIds = listOf("diag_neuron", "diag_human_brain")
        ),
        SyllabusChapter(
            id = "bio_reproduction",
            chapterNumber = 7,
            textbookPart = 1,
            division = ScienceDivision.BIOLOGY,
            titleEn = "How do Organisms Reproduce?",
            titleKn = "ಜೀವಿಗಳು ಹೇಗೆ ಸಂತಾನೋತ್ಪತ್ತಿ ನಡೆಸುತ್ತವೆ?",
            introduction = "Explains DNA copying and variation, asexual modes (fission, fragmentation, regeneration, budding, vegetative propagation, spore formation), sexual reproduction in flowering plants, human reproductive systems, and reproductive health.",
            objectives = listOf(
                "Understand why variation is crucial for species survival.",
                "Compare asexual modes with examples (Amoeba, Leishmania, Spirogyra, Planaria, Hydra, Rhizopus).",
                "Label longitudinal section of a flower and trace double fertilization / seed formation.",
                "Explain male & female human reproductive organs, placenta role, and contraception methods."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "bio_7_asexual_plant",
                    title = "Asexual Modes & Sexual Reproduction in Plants",
                    kannadaTitle = "ಅಲೈಂಗಿಕ ಸಂತಾನೋತ್ಪತ್ತಿ ಮತ್ತು ಸಸ್ಯಗಳಲ್ಲಿ ಲೈಂಗಿಕ ಸಂತಾನೋತ್ಪತ್ತಿ",
                    simpleExplanation = "Asexual reproduction produces genetically similar offspring from one parent (binary fission, budding, spore formation). Flowering plants reproduce sexually through pollination and fertilization in the pistil.",
                    detailedExplanation = "Pollen grains from anther reach stigma (self or cross-pollination), grow a pollen tube down the style into ovule. Male germ-cell fuses with egg forming zygote -> embryo; ovule becomes seed; ovary ripens into fruit.",
                    kannadaSummary = "ಅಮೀಬಾದಲ್ಲಿ ದ್ವಿವಿದಳನ, ಹೈಡ್ರಾದಲ್ಲಿ ಮೊಗ್ಗುವಿಕೆ ನಡೆಯುತ್ತದೆ. ಹೂವಿನ ಅಂಡಾಶಯ ಹಣ್ಣಾಗಿ ಮತ್ತು ಅಂಡಕ ಬೀಜವಾಗಿ ಬೆಳೆಯುತ್ತದೆ.",
                    observations = listOf("Bread mould (Rhizopus) grows sporangia containing tiny spores in moist warm conditions."),
                    dailyLifeApplications = listOf("Vegetative propagation (grafting, layering) helps grow seedless banana, rose, and sugarcane faster.")
                ),
                ScienceTopic(
                    id = "bio_7_human_repro",
                    title = "Human Reproduction & Reproductive Health",
                    kannadaTitle = "ಮಾನವನಲ್ಲಿ ಸಂತಾನೋತ್ಪತ್ತಿ ಮತ್ತು ಆರೋಗ್ಯ",
                    simpleExplanation = "Male testes produce sperm and testosterone; female ovaries release eggs and estrogen/progesterone. Fertilization occurs in the fallopian tube, and the embryo receives nutrition via the placenta.",
                    detailedExplanation = "Placenta is a disc-like tissue embedded in uterine wall containing villi on embryo side and blood spaces on mother's side, transferring glucose/O2 in and wastes out. If egg is not fertilized, uterine lining sheds as menstruation. Contraceptive barriers (condoms) prevent both pregnancy and STDs (HIV-AIDS, syphilis, gonorrhoea); IUCD (Copper-T), oral pills, and surgical tubectomy/vasectomy prevent pregnancy.",
                    kannadaSummary = "ಫಲವಂತಿಕೆ ಫ್ಯಾಲೋಪಿಯನ್ ನಾಳದಲ್ಲಿ ನಡೆಯುತ್ತದೆ; ಜರಾಯು ಭ್ರೂಣಕ್ಕೆ ಪೋಷಣೆ ನೀಡುತ್ತದೆ.",
                    observations = listOf("Puberty brings hormonal secondary sexual changes."),
                    dailyLifeApplications = listOf("Awareness of safe spacing and prevention of sexually transmitted infections.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Pollination", "ಪರಾಗಸ್ಪರ್ಶ", "Transfer of pollen grains from the anther to the stigma of a flower.", "ಪರಾಗಕೋಶದಿಂದ ಪರಾಗರೇಣುಗಳು ಶಲಾಕಾಗ್ರಕ್ಕೆ ವರ್ಗಾವಣೆಯಾಗುವ ಕ್ರಿಯೆ.", "Ch 7, Sec 7.3.2"),
                DefinitionEntry("Placenta", "ಜರಾಯು (ಪ್ಲಾಸೆಂಟಾ)", "Special disc-shaped tissue connecting the developing embryo to the uterine wall for nutrient and waste exchange.", "ತಾಯಿಯ ಗರ್ಭಕೋಶದ ಮೂಲಕ ಭ್ರೂಣಕ್ಕೆ ಆಹಾರ ಮತ್ತು ಆಮ್ಲಜನಕ ಪೂರೈಸುವ ವಿಶೇಷ ಅಂಗಾಂಶ.", "Ch 7, Sec 7.3.3")
            ),
            formulas = emptyList(),
            reactions = emptyList(),
            activities = listOf(
                ActivityEntry("Activity 7.2", "Spore Formation in Rhizopus", "Observe moist bread kept in cool dark place under magnifying glass.", "Thread-like hyphae with blob-on-a-stick sporangia appear.", "Rhizopus reproduces asexually via spores protected by thick walls.")
            ),
            revisionNotes = listOf(
                "Binary fission: Amoeba (any plane), Leishmania (definite longitudinal plane). Multiple fission: Plasmodium.",
                "Testes lie outside abdominal cavity in scrotum because sperm formation needs 2–3°C lower temperature.",
                "Ovary -> Fruit, Ovule -> Seed, Zygote -> Embryo."
            ),
            faqs = listOf(
                FaqEntry("What is the role of seminal vesicles and prostate gland?", "They add fluid secretions that provide nutrition to sperm and make transport easier.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c7_q1",
                    question = "Asexual reproduction takes place through budding in:",
                    options = listOf("Amoeba", "Yeast", "Plasmodium", "Leishmania"),
                    correctIndex = 1,
                    explanation = "Yeast and Hydra reproduce asexually by budding.",
                    marks = 1,
                    topicId = "bio_7_asexual_plant"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 1, Chapter 7",
            simulationIds = emptyList(),
            diagramIds = listOf("diag_flower_ls", "diag_pollen_germination")
        ),
        SyllabusChapter(
            id = "bio_heredity",
            chapterNumber = 8,
            textbookPart = 2,
            division = ScienceDivision.BIOLOGY,
            titleEn = "Heredity",
            titleKn = "ಆನುವಂಶಿಕತೆ",
            introduction = "Covers transmission of traits across generations, Mendel's Monohybrid (3:1) and Dihybrid (9:3:3:1) crosses on pea plants (Pisum sativum), dominant vs recessive alleles, gene expression via proteins, and Sex Determination in humans (XX / XY). Note: Evolution portion was dropped in KSEAB rationalization.",
            objectives = listOf(
                "Explain accumulation of variation during reproduction.",
                "Solve Mendel's Monohybrid and Dihybrid Punnett squares with phenotypic & genotypic ratios.",
                "Explain how DNA codes for proteins that control traits.",
                "Illustrate genetic sex determination in humans (50% probability of XX or XY)."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "bio_8_mendel",
                    title = "Mendel's Laws & Inheritance Crosses",
                    kannadaTitle = "ಮೆಂಡಲ್‌ನ ನಿಯಮಗಳು ಮತ್ತು ಆನುವಂಶಿಕ ಸಂಕರಣಗಳು",
                    simpleExplanation = "Gregor Mendel crossed tall (TT) and short (tt) pea plants: F1 was all tall (Tt), and F2 gave a 3:1 phenotypic ratio (1 TT : 2 Tt : 1 tt). Crossing two traits (Round-Yellow x Wrinkled-Green) showed independent assortment with a 9:3:3:1 ratio.",
                    detailedExplanation = "Law of Dominance: Only dominant allele expresses in heterozygous F1. Law of Segregation: Alleles separate during gamete formation. Law of Independent Assortment: Different trait pairs inherit independently in dihybrid cross.",
                    kannadaSummary = "ಏಕತಳಿ ಸಂಕರಣ ಅನುಪಾತ 3:1; ದ್ವಿತಳಿ ಸಂಕರಣ ಅನುಪಾತ 9:3:3:1 ಆಗಿರುತ್ತದೆ.",
                    observations = listOf("F2 generation in dihybrid cross produces new recombinant combinations like Round-Green and Wrinkled-Yellow."),
                    dailyLifeApplications = listOf("Understanding inherited traits like free vs attached earlobes.")
                ),
                ScienceTopic(
                    id = "bio_8_sex_determination",
                    title = "Sex Determination in Humans",
                    kannadaTitle = "ಮಾನವರಲ್ಲಿ ಲಿಂಗ ನಿರ್ಧಾರಣೆ",
                    simpleExplanation = "Humans have 23 pairs of chromosomes; the 23rd pair is XX in females and XY in males. Mother always contributes an X; father contributes either X (daughter, XX) or Y (son, XY).",
                    detailedExplanation = "All children inherit an X chromosome from the mother. Thus, the sex of the child is genetically determined by whether the fertilizing sperm carries an X or a Y chromosome (50% chance each).",
                    kannadaSummary = "ಮಗುವಿನ ಲಿಂಗವು ತಂದೆಯಿಂದ ಬರುವ X ಅಥವಾ Y ಕ್ರೋಮೋಸೋಮ್ ಮೇಲೆ ನಿರ್ಧರಿತವಾಗುತ್ತದೆ.",
                    observations = listOf("Equal 50:50 statistical probability for male and female offspring."),
                    dailyLifeApplications = listOf("Scientific literacy dispelling gender myths in society.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Dominant Trait", "ಪ್ರಬಲ ಗುಣ", "A trait that expresses itself in the F1 heterozygous hybrid generation even in presence of a contrasting allele.", "F1 ಪೀಳಿಗೆಯಲ್ಲಿ ವ್ಯಕ್ತವಾಗುವ ಪ್ರಬಲ ಆನುವಂಶಿಕ ಗುಣ.", "Ch 8, Sec 8.2.2"),
                DefinitionEntry("Gene", "ಜೀನ್ (ವಂಶವಾಹಿ)", "A functional segment of DNA that provides information for making one specific protein/trait.", "ಒಂದು ನಿರ್ದಿಷ್ಟ ಪ್ರೋಟೀನ್ ಸಂಶ್ಲೇಷಣೆಗೆ ಮಾಹಿತಿ ನೀಡುವ DNA ಭಾಗ.", "Ch 8, Sec 8.2.3")
            ),
            formulas = emptyList(),
            reactions = emptyList(),
            activities = listOf(
                ActivityEntry("Activity 8.1", "Earlobes Inheritance Survey", "Survey free vs attached earlobes among classmates and parents.", "Both variants appear across families.", "Traits are inherited through parental genes.")
            ),
            revisionNotes = listOf(
                "Monohybrid phenotypic ratio = 3:1; genotypic ratio = 1:2:1.",
                "Dihybrid phenotypic ratio = 9:3:3:1.",
                "In humans, 22 pairs are autosomes; 1 pair is sex chromosomes (XX female, XY male)."
            ),
            faqs = listOf(
                FaqEntry("How do Mendel's experiments show that traits are inherited independently?", "In a dihybrid cross (RRYY x rryy), F2 yields 9:3:3:1 including new combinations Round-Green and Wrinkled-Yellow.", "3 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c8_q1",
                    question = "A Mendelian experiment consisted of breeding tall pea plants bearing violet flowers with short pea plants bearing white flowers. The progeny all bore violet flowers, but almost half of them were short. This suggests the genetic make-up of the tall parent is:",
                    options = listOf("TTWW", "TTww", "TtWW", "TtWw"),
                    correctIndex = 2,
                    explanation = "All progeny had violet flowers (WW), and half were short (Tt x tt), so the tall parent was TtWW.",
                    marks = 1,
                    topicId = "bio_8_mendel"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 2, Chapter 8 (Rationalized)",
            simulationIds = emptyList(),
            diagramIds = listOf("diag_sex_determination")
        ),
        SyllabusChapter(
            id = "bio_environment",
            chapterNumber = 13,
            textbookPart = 2,
            division = ScienceDivision.BIOLOGY,
            titleEn = "Our Environment",
            titleKn = "ನಮ್ಮ ಪರಿಸರ",
            introduction = "Examines ecosystem components (biotic and abiotic), food chains and food webs, the 10% energy flow law, biological magnification, ozone layer depletion by CFCs, and scientific waste management (biodegradable vs non-biodegradable).",
            objectives = listOf(
                "Distinguish natural vs artificial ecosystems and trophic levels.",
                "Apply the 10% energy transfer law to food-chain numerical problems.",
                "Explain biomagnification of pesticides in top consumers.",
                "Describe ozone formation/depletion by CFCs and segregate biodegradable vs non-biodegradable waste."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "bio_13_ecosystem_energy",
                    title = "Ecosystems, Food Chains & 10% Energy Law",
                    kannadaTitle = "ಪರಿಸರ ವ್ಯವಸ್ಥೆ, ಆಹಾರ ಸರಪಳಿ ಮತ್ತು 10% ಶಕ್ತಿ ನಿಯಮ",
                    simpleExplanation = "Energy flows unidirectionally from Sun -> Producers (1% captured) -> Herbivores -> Carnivores, with only 10% passed to the next trophic level while 90% is lost as heat.",
                    detailedExplanation = "Because 90% energy is lost at each step, food chains rarely exceed 3–4 trophic levels. Non-biodegradable pesticides like DDT accumulate at increasing concentrations at higher trophic levels — this is Biological Magnification.",
                    kannadaSummary = "ಪ್ರತಿ ಪೋಷಣಾ ಸ್ತರದಿಂದ ಮುಂದಿನ ಸ್ತರಕ್ಕೆ ಕೇವಲ 10% ಶಕ್ತಿ ಮಾತ್ರ ವರ್ಗಾವಣೆಯಾಗುತ್ತದೆ.",
                    observations = listOf("Top carnivores (humans, hawks) have maximum pesticide concentration."),
                    dailyLifeApplications = listOf("Separating wet kitchen waste (compost) from dry plastic/metal waste for recycling.")
                ),
                ScienceTopic(
                    id = "bio_13_ozone_waste",
                    title = "Ozone Layer & Waste Management",
                    kannadaTitle = "ಓಝೋನ್ ಪದರ ಮತ್ತು ತ್ಯಾಜ್ಯ ನಿರ್ವಹಣೆ",
                    simpleExplanation = "Stratospheric Ozone (O3) shields Earth from harmful UV radiation that causes skin cancer. CFCs used in old refrigerants destroyed ozone, leading to the Montreal Protocol ban.",
                    detailedExplanation = "UV splits O2 -> O + O, and O + O2 -> O3. Enzymes are specific in action, so microbes break down organic biodegradable waste but cannot digest synthetic plastics.",
                    kannadaSummary = "ಓಝೋನ್ ಪದರ ಹಾನಿಕಾರಕ UV ಕಿರಣಗಳಿಂದ ಭೂಮಿಯನ್ನು ರಕ್ಷಿಸುತ್ತದೆ.",
                    observations = listOf("Paper cups vs disposable plastic cups impact on soil."),
                    dailyLifeApplications = listOf("Using kulhads/steel/cloth bags and CFC-free cooling appliances.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Biological Magnification", "ಜೈವಿಕ ವರ್ಧನೆ", "Progressive increase in concentration of non-biodegradable toxic chemicals at each successive trophic level of a food chain.", "ಆಹಾರ ಸರಪಳಿಯ ಪ್ರತಿ ಹಂತದಲ್ಲೂ ವಿಘಟನೆಯಾಗದ ರಾಸಾಯನಿಕಗಳ ಸಾಂದ್ರತೆ ಹೆಚ್ಚಾಗುವುದು.", "Ch 13, Sec 13.1.2"),
                DefinitionEntry("Trophic Level", "ಪೋಷಣಾ ಸ್ತರ", "Each step or level of a food chain where transfer of food energy takes place.", "ಆಹಾರ ಸರಪಳಿಯಲ್ಲಿ ಶಕ್ತಿ ವರ್ಗಾವಣೆಯಾಗುವ ಪ್ರತಿಯೊಂದು ಹಂತ.", "Ch 13, Sec 13.1.1")
            ),
            formulas = emptyList(),
            reactions = listOf(
                ChemicalReactionEntry("Ozone Formation", "O2 --(UV)--> O + O;  O + O2 -> O3", "Photochemical", "Protects Earth from UV-B radiation", "Stratosphere UV rays")
            ),
            activities = listOf(
                ActivityEntry("Activity 13.5", "Biodegradable vs Non-biodegradable Burial", "Bury fruit peels, paper, plastic bottle, and glass in soil for 30 days.", "Peels and paper decompose; plastic and glass remain unchanged.", "Microbial enzymes act specifically on organic matter.")
            ),
            revisionNotes = listOf(
                "Plants capture only ~1% of incident sunlight.",
                "Energy flow in a food chain is strictly unidirectional.",
                "If producer has 10,000 J, primary consumer gets 1,000 J, secondary consumer gets 100 J."
            ),
            faqs = listOf(
                FaqEntry("Why is damage to the ozone layer a cause for concern?", "Ozone blocks harmful UV radiation from the Sun; depletion increases skin cancer, cataracts, and crop damage.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c13_q1",
                    question = "In a food chain Grass -> Deer -> Lion, if 100 J energy is available to the Lion, how much energy was available at the producer (Grass) level?",
                    options = listOf("10 J", "1000 J", "10,000 J", "1,00,000 J"),
                    correctIndex = 2,
                    explanation = "Working backward by 10x per step: Lion = 100 J -> Deer = 1,000 J -> Grass = 10,000 J.",
                    marks = 1,
                    topicId = "bio_13_ecosystem_energy"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 2, Chapter 13",
            simulationIds = listOf("sim_bio_ecosystem"),
            diagramIds = listOf("diag_trophic_pyramid")
        )
    )
}
