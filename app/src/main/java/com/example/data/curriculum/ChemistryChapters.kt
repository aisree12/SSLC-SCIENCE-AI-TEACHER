package com.example.data.curriculum

import com.example.model.ActivityEntry
import com.example.model.ChapterQuizQuestion
import com.example.model.ChemicalReactionEntry
import com.example.model.DefinitionEntry
import com.example.model.FaqEntry
import com.example.model.ScienceDivision
import com.example.model.ScienceTopic
import com.example.model.SyllabusChapter

object ChemistryChapters {
    val all: List<SyllabusChapter> = listOf(
        SyllabusChapter(
            id = "chem_reactions",
            chapterNumber = 1,
            textbookPart = 1,
            division = ScienceDivision.CHEMISTRY,
            titleEn = "Chemical Reactions and Equations",
            titleKn = "ರಾಸಾಯನಿಕ ಕ್ರಿಯೆಗಳು ಮತ್ತು ಸಮೀಕರಣಗಳು",
            introduction = "Chemical changes produce new substances with different properties. In this foundational Karnataka SSLC Chemistry chapter, students learn to represent reactions symbolically, balance equations using the Law of Conservation of Mass, classify reaction types, and explain oxidation, reduction, corrosion, and rancidity.",
            objectives = listOf(
                "Identify signs of a chemical reaction: gas evolution, precipitate, colour or temperature change.",
                "Write and balance chemical equations following the Law of Conservation of Mass.",
                "Classify Combination, Decomposition, Displacement, Double Displacement, and Redox reactions.",
                "Explain real-life effects of oxidation: Corrosion and Rancidity, and their prevention."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "chem_1_balancing",
                    title = "Writing & Balancing Chemical Equations",
                    kannadaTitle = "ರಾಸಾಯನಿಕ ಸಮೀಕರಣಗಳನ್ನು ಬರೆಯುವುದು ಮತ್ತು ಸರಿದೂಗಿಸುವುದು",
                    simpleExplanation = "A balanced chemical equation has the exact same number of atoms of each element on both the reactant (left) and product (right) sides, because mass can neither be created nor destroyed.",
                    detailedExplanation = "By the Law of Conservation of Mass, total mass of reactants equals total mass of products. Skeletal equations show formulas only. To balance, adjust stoichiometric coefficients before formulas (never change subscripts) and add state symbols (s), (l), (g), (aq).",
                    kannadaSummary = "ರಾಶಿ ಸಂರಕ್ಷಣಾ ನಿಯಮದ ಪ್ರಕಾರ ಕ್ರಿಯಾಜನ್ಯಗಳು ಮತ್ತು ಪ್ರತಿಫಲಗಳ ಪರಮಾಣುಗಳ ಸಂಖ್ಯೆ ಸಮನಾಗಿರಬೇಕು.",
                    observations = listOf("Magnesium ribbon burns with a dazzling white flame forming white MgO powder."),
                    dailyLifeApplications = listOf("Combustion of LPG (butane/propane) in kitchen stoves.")
                ),
                ScienceTopic(
                    id = "chem_1_types",
                    title = "Types of Chemical Reactions",
                    kannadaTitle = "ರಾಸಾಯನಿಕ ಕ್ರಿಯೆಗಳ ವಿಧಗಳು",
                    simpleExplanation = "Reactions are grouped by how atoms rearrange: joining together (Combination), breaking apart (Decomposition), swapping one partner (Displacement), exchanging ions (Double Displacement), or transferring oxygen/hydrogen/electrons (Redox).",
                    detailedExplanation = "Combination (A + B -> AB, mostly exothermic like CaO + H2O -> Ca(OH)2); Decomposition (thermal, electrolytic, or photolytic breakdown like 2AgCl -> 2Ag + Cl2 in sunlight); Displacement (more reactive element displaces less reactive one, e.g., Fe + CuSO4 -> FeSO4 + Cu); Double Displacement (exchange of ions forming precipitate like BaSO4).",
                    kannadaSummary = "ಸಂಯೋಗ, ವಿಭಜನೆ, ಸ್ಥಾನಪಲ್ಲಟ, ದ್ವಿ-ಸ್ಥಾನಪಲ್ಲಟ ಮತ್ತು ರೆಡಾಕ್ಸ್ ಕ್ರಿಯೆಗಳು ಪ್ರಮುಖ ವಿಧಗಳಾಗಿವೆ.",
                    observations = listOf("Iron nail in blue CuSO4 turns brownish and the solution turns pale green."),
                    dailyLifeApplications = listOf("Black-and-white photography uses photolytic decomposition of AgBr.", "Whitewashing walls uses slaked lime reacting with CO2 to form shiny CaCO3.")
                ),
                ScienceTopic(
                    id = "chem_1_redox_corrosion",
                    title = "Oxidation, Reduction, Corrosion & Rancidity",
                    kannadaTitle = "ಉತ್ಕರ್ಷಣ, ಅಪಕರ್ಷಣ, ಸಂಕ್ಷಾರಣ ಮತ್ತು ಕಮಟುವಿಕೆ",
                    simpleExplanation = "Gain of oxygen or loss of hydrogen is Oxidation; loss of oxygen or gain of hydrogen is Reduction. Both happen together in Redox reactions. Oxidation of metals causes corrosion (rusting), while oxidation of fats/oils causes rancidity.",
                    detailedExplanation = "In CuO + H2 -> Cu + H2O, CuO is reduced to Cu (acting as oxidizing agent) and H2 is oxidized to H2O (acting as reducing agent). Corrosion weakens iron bridges and railings; prevention includes painting, oiling, galvanization. Rancidity spoils food; prevented by antioxidants, airtight containers, and nitrogen flushing in chip packets.",
                    kannadaSummary = "ಆಮ್ಲಜನಕದ ಸೇರ್ಪಡೆ ಉತ್ಕರ್ಷಣ; ಆಮ್ಲಜನಕದ ನಷ್ಟ ಅಪಕರ್ಷಣ. ಲೋಹಗಳ ಸವೆತ ಸಂಕ್ಷಾರಣ ಮತ್ತು ಎಣ್ಣೆ ಪದಾರ್ಥಗಳ ಕೆಡುವಿಕೆ ಕಮಟುವಿಕೆ.",
                    observations = listOf("Heating copper powder coats it with black CuO; passing H2 restores brown copper."),
                    dailyLifeApplications = listOf("Chips bags are flushed with nitrogen gas to prevent rancidity.", "Galvanized zinc coating protects iron water pipes from rust.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Exothermic Reaction", "ಬಹಿರುಷ್ಣಕ ಕ್ರಿಯೆ", "A chemical reaction in which heat is released along with formation of products.", "ಉತ್ಪನ್ನಗಳ ಜೊತೆಗೆ ಉಷ್ಣವು ಬಿಡುಗಡೆಯಾಗುವ ರಾಸಾಯನಿಕ ಕ್ರಿಯೆ.", "Ch 1, Sec 1.2.1"),
                DefinitionEntry("Precipitation Reaction", "ಅವಕ್ಷೇಪನ ಕ್ರಿಯೆ", "Any reaction that produces an insoluble solid (precipitate) separated from solution.", "ನೀರಿನಲ್ಲಿ ಕರಗದ ಘನವಸ್ತು (ಅವಕ್ಷೇಪ) ಉಂಟಾಗುವ ಕ್ರಿಯೆ.", "Ch 1, Sec 1.2.4"),
                DefinitionEntry("Rancidity", "ಕಮಟುವಿಕೆ", "Aerial oxidation of fats and oils in food causing bad smell and taste.", "ಆಹಾರದಲ್ಲಿನ ಕೊಬ್ಬು ಮತ್ತು ಎಣ್ಣೆಗಳು ಉತ್ಕರ್ಷಣಗೊಂಡು ಕೆಟ್ಟ ವಾಸನೆ ಮತ್ತು ರುಚಿ ಬರುವುದು.", "Ch 1, Sec 1.3.2")
            ),
            formulas = emptyList(),
            reactions = listOf(
                ChemicalReactionEntry("Slaking of Lime", "CaO(s) + H2O(l) -> Ca(OH)2(aq) + Heat", "Combination & Exothermic", "Hissing sound and beaker becomes hot", "Room temperature"),
                ChemicalReactionEntry("Thermal Decomposition of Ferrous Sulphate", "2FeSO4(s) -> Fe2O3(s) + SO2(g) + SO3(g)", "Thermal Decomposition", "Green crystals turn reddish-brown with burning sulphur smell", "Heat (Δ)"),
                ChemicalReactionEntry("Electrolysis of Water", "2H2O(l) -> 2H2(g) + O2(g)", "Electrolytic Decomposition", "H2 volume at cathode is double O2 volume at anode", "Electric current + few drops of dil. H2SO4"),
                ChemicalReactionEntry("Iron + Copper Sulphate", "Fe(s) + CuSO4(aq) -> FeSO4(aq) + Cu(s)", "Displacement", "Blue solution fades to pale green; brown Cu deposits on iron", "Aqueous medium"),
                ChemicalReactionEntry("Sodium Sulphate + Barium Chloride", "Na2SO4(aq) + BaCl2(aq) -> BaSO4(s) + 2NaCl(aq)", "Double Displacement", "White insoluble precipitate of BaSO4 forms", "Aqueous medium")
            ),
            activities = listOf(
                ActivityEntry("Activity 1.7", "Electrolysis of Water", "Verify 2:1 volume ratio of Hydrogen and Oxygen gas.", "Gas collected at cathode is twice the volume at anode.", "Water decomposes into H2 and O2 in 2:1 molar ratio."),
                ActivityEntry("Activity 1.9", "Iron Nail in Copper Sulphate", "Demonstrate single displacement reaction.", "Blue CuSO4 turns pale green and nail turns reddish-brown.", "Iron is more reactive than copper and displaces it.")
            ),
            revisionNotes = listOf(
                "Respiration is an exothermic process: C6H12O6(aq) + 6O2(aq) -> 6CO2(aq) + 6H2O(l) + Energy.",
                "Cathode collects H2 (2 volumes); Anode collects O2 (1 volume) during water electrolysis.",
                "Silver chloride (white) turns grey in sunlight: 2AgCl -> 2Ag + Cl2.",
                "Flushing food packets with unreactive Nitrogen (N2) prevents rancidity."
            ),
            faqs = listOf(
                FaqEntry("Why should a magnesium ribbon be cleaned before burning in air?", "To remove the protective layer of basic magnesium oxide/carbonate formed on its surface so it burns smoothly.", "1–2 Marks"),
                FaqEntry("Why is respiration considered an exothermic reaction?", "During digestion, carbohydrates break into glucose which reacts with oxygen in cells to release energy.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c1_q1",
                    question = "In the reaction Fe2O3 + 2Al -> Al2O3 + 2Fe, which type of reaction occurs?",
                    options = listOf("Combination reaction", "Double displacement reaction", "Decomposition reaction", "Displacement reaction"),
                    correctIndex = 3,
                    explanation = "More reactive Aluminium displaces Iron from Iron(III) oxide.",
                    marks = 1,
                    topicId = "chem_1_types"
                ),
                ChapterQuizQuestion(
                    id = "c1_q2",
                    question = "During electrolysis of water, the ratio of gas volume collected at cathode to anode is:",
                    options = listOf("1 : 2", "2 : 1", "1 : 1", "4 : 1"),
                    correctIndex = 1,
                    explanation = "2H2O -> 2H2 (cathode) + O2 (anode), so H2:O2 is 2:1.",
                    marks = 1,
                    topicId = "chem_1_types"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 1, Chapter 1",
            simulationIds = listOf("sim_chem_balancer"),
            diagramIds = listOf("diag_electrolysis_water")
        ),
        SyllabusChapter(
            id = "chem_acids_bases",
            chapterNumber = 2,
            textbookPart = 1,
            division = ScienceDivision.CHEMISTRY,
            titleEn = "Acids, Bases and Salts",
            titleKn = "ಆಮ್ಲಗಳು, ಪ್ರತ್ಯಾಮ್ಲಗಳು ಮತ್ತು ಲವಣಗಳು",
            introduction = "Explores the chemical behaviour of acids and bases in aqueous solutions, the pH scale in daily life and biology, neutralization, and preparation/uses of industrial salts like Bleaching Powder, Baking Soda, Washing Soda, and Plaster of Paris.",
            objectives = listOf(
                "Test acids and bases using natural, synthetic, and olfactory indicators.",
                "Write equations for acid reactions with metals, metal carbonates, and bases.",
                "Interpret the pH scale (0–14) and biological importance of pH.",
                "Recall formulas, preparation, and uses of NaOH, CaOCl2, NaHCO3, Na2CO3.10H2O, and CaSO4.1/2H2O."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "chem_2_indicators",
                    title = "Indicators & Reactions of Acids and Bases",
                    kannadaTitle = "ಸೂಚಕಗಳು ಹಾಗೂ ಆಮ್ಲ-ಪ್ರತ್ಯಾಮ್ಲಗಳ ಕ್ರಿಯೆಗಳು",
                    simpleExplanation = "Acids turn blue litmus red and produce H+ (H3O+) ions in water; bases turn red litmus blue and produce OH- ions in water.",
                    detailedExplanation = "Metal + Acid -> Salt + H2 gas (tested by pop sound). Metal carbonate/bicarbonate + Acid -> Salt + CO2 + H2O (CO2 turns lime water milky). Acid + Base -> Salt + Water (Neutralization). Dry HCl gas does not change dry litmus because ions form only in water.",
                    kannadaSummary = "ಆಮ್ಲಗಳು ನೀರಿನಲ್ಲಿ H+ ಅಯಾನುಗಳನ್ನು ಮತ್ತು ಪ್ರತ್ಯಾಮ್ಲಗಳು OH- ಅಯಾನುಗಳನ್ನು ಬಿಡುಗಡೆ ಮಾಡುತ್ತವೆ.",
                    observations = listOf("Burning candle makes a 'pop' sound in H2 gas.", "CO2 turns clear lime water milky due to CaCO3 formation."),
                    dailyLifeApplications = listOf("Antacids like Milk of Magnesia Mg(OH)2 neutralize excess stomach HCl.")
                ),
                ScienceTopic(
                    id = "chem_2_ph_salts",
                    title = "pH Scale & Important Salts",
                    kannadaTitle = "pH ಮಾಪಕ ಮತ್ತು ಪ್ರಮುಖ ಲವಣಗಳು",
                    simpleExplanation = "The pH scale measures H+ concentration from 0 (strongly acidic) to 14 (strongly basic), with 7 as neutral. Common salts from NaCl power baking, disinfection, water softening, and fracture casts.",
                    detailedExplanation = "Chlor-alkali process electrolyzes brine (NaCl aq) to give NaOH, Cl2 (anode), and H2 (cathode). Bleaching powder (CaOCl2) is made from dry slaked lime + Cl2. Baking soda (NaHCO3) releases CO2 on heating making cakes spongy. Plaster of Paris (CaSO4·1/2H2O) sets into hard Gypsum (CaSO4·2H2O) with water.",
                    kannadaSummary = "pH 7 ತಟಸ್ಥ, <7 ಆಮ್ಲೀಯ, >7 ಕ್ಷಾರೀಯ. ಬೇಕಿಂಗ್ ಸೋಡಾ, ವಾಷಿಂಗ್ ಸೋಡಾ, ಪ್ಲಾಸ್ಟರ್ ಆಫ್ ಪ್ಯಾರಿಸ್ ಮುಖ್ಯ ಲವಣಗಳು.",
                    observations = listOf("Tooth decay begins when mouth pH drops below 5.5."),
                    dailyLifeApplications = listOf("Baking soda in fire extinguishers and antacids.", "Plaster of Paris for setting fractured bones.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Neutralization Reaction", "ತಟಸ್ಥೀಕರಣ ಕ್ರಿಯೆ", "Reaction between an acid and a base to produce salt and water.", "ಆಮ್ಲ ಮತ್ತು ಪ್ರತ್ಯಾಮ್ಲಗಳು ವರ್ತಿಸಿ ಲವಣ ಮತ್ತು ನೀರು ಉಂಟಾಗುವ ಕ್ರಿಯೆ.", "Ch 2, Sec 2.1.4"),
                DefinitionEntry("Water of Crystallisation", "ಸ್ಫಟಿಕೀಕರಣದ ನೀರು", "Fixed number of water molecules present in one formula unit of a salt (e.g., CuSO4·5H2O).", "ಲವಣದ ಒಂದು ಘಟಕ ಸೂತ್ರದಲ್ಲಿರುವ ನಿಶ್ಚಿತ ನೀರಿನ ಅಣುಗಳ ಸಂಖ್ಯೆ.", "Ch 2, Sec 2.4.4")
            ),
            formulas = emptyList(),
            reactions = listOf(
                ChemicalReactionEntry("Chlor-Alkali Process", "2NaCl(aq) + 2H2O(l) -> 2NaOH(aq) + Cl2(g) + H2(g)", "Electrolytic Decomposition", "Cl2 at anode, H2 at cathode, NaOH near cathode", "Electricity"),
                ChemicalReactionEntry("Bleaching Powder Formation", "Ca(OH)2 + Cl2 -> CaOCl2 + H2O", "Chemical Synthesis", "Dry slaked lime absorbs chlorine", "Room temp"),
                ChemicalReactionEntry("Setting of Plaster of Paris", "CaSO4·1/2H2O + 1.5H2O -> CaSO4·2H2O", "Hydration", "Sets into hard solid gypsum mass in 30 mins", "Mix with water")
            ),
            activities = listOf(
                ActivityEntry("Activity 2.3", "Zinc Granules with Dilute H2SO4", "Test gas evolved when metal reacts with acid.", "Soap bubbles filled with gas burn with a pop sound.", "Active metals liberate Hydrogen gas from dilute acids.")
            ),
            revisionNotes = listOf(
                "Always add acid slowly to water with stirring (dilution is highly exothermic), never water to acid.",
                "pH < 5.5 causes tooth enamel (calcium hydroxyapatite) corrosion.",
                "Bee sting injects methanoic acid; baking soda gives relief."
            ),
            faqs = listOf(
                FaqEntry("Why does distilled water not conduct electricity whereas rainwater does?", "Distilled water has no free ions, whereas rainwater dissolves atmospheric CO2/SO2 forming carbonic/acidic ions.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c2_q1",
                    question = "A solution turns red litmus blue. Its pH is likely to be:",
                    options = listOf("1", "4", "5", "10"),
                    correctIndex = 3,
                    explanation = "Bases turn red litmus blue and have pH greater than 7.",
                    marks = 1,
                    topicId = "chem_2_ph_salts"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 1, Chapter 2",
            simulationIds = listOf("sim_chem_ph", "sim_chem_balancer"),
            diagramIds = listOf("diag_acid_zinc_h2")
        ),
        SyllabusChapter(
            id = "chem_metals",
            chapterNumber = 3,
            textbookPart = 1,
            division = ScienceDivision.CHEMISTRY,
            titleEn = "Metals and Non-metals",
            titleKn = "ಲೋಹಗಳು ಮತ್ತು ಅಲೋಹಗಳು",
            introduction = "Compares physical and chemical properties of metals and non-metals, introduces ionic bond formation via electron transfer, the Reactivity Series, metallurgy (Roasting, Calcination, Thermit reaction, Electrolytic refining), and corrosion prevention.",
            objectives = listOf(
                "Compare physical & chemical properties (amphoteric oxides like Al2O3, ZnO).",
                "Draw electron-dot structures for ionic compounds (NaCl, MgCl2).",
                "Extract metals based on reactivity: low (Hg, Cu), medium (Zn, Fe), high (Na, Al).",
                "Explain electrolytic refining of copper and corrosion prevention."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "chem_3_properties_ionic",
                    title = "Chemical Properties & Ionic Compounds",
                    kannadaTitle = "ರಾಸಾಯನಿಕ ಗುಣಗಳು ಮತ್ತು ಅಯಾನಿಕ್ ಸಂಯುಕ್ತಗಳು",
                    simpleExplanation = "Metals lose electrons to form positive ions (cations) and basic/amphoteric oxides; non-metals gain electrons to form negative ions (anions) and acidic oxides.",
                    detailedExplanation = "Amphoteric oxides (Al2O3, ZnO) react with both acids and bases to form salt and water. Ionic compounds have high melting points due to strong electrostatic attraction and conduct electricity only in molten or aqueous states when ions are free to move.",
                    kannadaSummary = "ಲೋಹಗಳು ಎಲೆಕ್ಟ್ರಾನ್ ಕಳೆದುಕೊಂಡು ಧನ ಅಯಾನುಗಳಾಗುತ್ತವೆ; ಅಲೋಹಗಳು ಎಲೆಕ್ಟ್ರಾನ್ ಪಡೆದು ಋಣ ಅಯಾನುಗಳಾಗುತ್ತವೆ.",
                    observations = listOf("Sodium reacts vigorously with cold water and catches fire; stored under kerosene."),
                    dailyLifeApplications = listOf("Thermit reaction (Fe2O3 + 2Al -> 2Fe + Al2O3 + Heat) welds cracked railway tracks.")
                ),
                ScienceTopic(
                    id = "chem_3_metallurgy",
                    title = "Reactivity Series & Extraction of Metals",
                    kannadaTitle = "ಕ್ರಿಯಾಶೀಲತೆಯ ಶ್ರೇಣಿ ಮತ್ತು ಲೋಹಗಳ ಉದ್ಧರಣ",
                    simpleExplanation = "Highly reactive metals (K, Na, Ca, Mg, Al) are extracted by electrolytic reduction; moderately reactive metals (Zn, Fe, Pb) by roasting/calcination and carbon reduction; least reactive metals (Au, Ag) occur free.",
                    detailedExplanation = "Roasting heats sulphide ores strongly in excess air (2ZnS + 3O2 -> 2ZnO + 2SO2). Calcination heats carbonate ores in limited air (ZnCO3 -> ZnO + CO2). Electrolytic refining uses impure metal as anode, pure metal strip as cathode, and metal salt solution as electrolyte.",
                    kannadaSummary = "ಸಲ್ಫೈಡ್ ಅದಿರುಗಳನ್ನು ಹುರಿಯುವಿಕೆ (Roasting) ಮತ್ತು ಕಾರ್ಬೋನೇಟ್ ಅದಿರುಗಳನ್ನು ಕಾಸುವಿಕೆ (Calcination) ಮೂಲಕ ಆಕ್ಸೈಡ್ ಆಗಿ ಪರಿವರ್ತಿಸಲಾಗುತ್ತದೆ.",
                    observations = listOf("During copper refining, insoluble impurities settle as anode mud."),
                    dailyLifeApplications = listOf("Alloys like Brass (Cu+Zn), Bronze (Cu+Sn), Solder (Pb+Sn) improve strength and lower melting point.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Amphoteric Oxides", "ಉಭಯವರ್ತಿ ಆಕ್ಸೈಡ್‌ಗಳು", "Metal oxides that react with both acids and bases to produce salts and water (e.g., Al2O3, ZnO).", "ಆಮ್ಲ ಮತ್ತು ಪ್ರತ್ಯಾಮ್ಲ ಎರಡರ ಜೊತೆಗೂ ವರ್ತಿಸಿ ಲವಣ ಮತ್ತು ನೀರು ಕೊಡುವ ಲೋಹದ ಆಕ್ಸೈಡ್‌ಗಳು.", "Ch 3, Sec 3.2.1"),
                DefinitionEntry("Galvanisation", "ಗ್ಯಾಲ್ವನೀಕರಣ", "Coating steel and iron with a thin layer of zinc to protect from rusting.", "ಕಬ್ಬಿಣ ತುಕ್ಕು ಹಿಡಿಯದಂತೆ ತಡೆಯಲು ತೆಳುವಾದ ಸತುವಿನ ಲೇಪನ ಮಾಡುವ ವಿಧಾನ.", "Ch 3, Sec 3.5.1")
            ),
            formulas = emptyList(),
            reactions = listOf(
                ChemicalReactionEntry("Thermit Reaction", "Fe2O3(s) + 2Al(s) -> 2Fe(l) + Al2O3(s) + Heat", "Displacement & Exothermic", "Molten iron produced for welding rail tracks", "Ignition"),
                ChemicalReactionEntry("Roasting of Zinc Sulphide", "2ZnS + 3O2 -> 2ZnO + 2SO2", "Oxidation / Roasting", "Sulphide ore converted to oxide in excess air", "Strong Heat")
            ),
            activities = listOf(
                ActivityEntry("Activity 3.14", "Conditions for Rusting of Iron", "Show both air (O2) and water (moisture) are necessary for rusting.", "Only the nail exposed to both air and water rusts.", "Rusting requires both oxygen and moisture.")
            ),
            revisionNotes = listOf(
                "Reactivity order: K > Na > Ca > Mg > Al > Zn > Fe > Pb > [H] > Cu > Hg > Ag > Au.",
                "Mercury is the only liquid metal at room temperature; Bromine is the only liquid non-metal.",
                "24-carat gold is too soft for jewellery, so 22-carat gold alloys 22 parts Au with 2 parts Cu or Ag."
            ),
            faqs = listOf(
                FaqEntry("Why do ionic compounds have high melting points?", "Considerable energy is required to break the strong inter-ionic electrostatic attraction between oppositely charged ions.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c3_q1",
                    question = "Which of the following pairs will give displacement reactions?",
                    options = listOf("NaCl solution and copper metal", "MgCl2 solution and aluminium metal", "FeSO4 solution and silver metal", "AgNO3 solution and copper metal"),
                    correctIndex = 3,
                    explanation = "Copper is above Silver in the reactivity series, so Cu displaces Ag from AgNO3.",
                    marks = 1,
                    topicId = "chem_3_metallurgy"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 1, Chapter 3",
            simulationIds = listOf("sim_chem_reactivity"),
            diagramIds = listOf("diag_electrolytic_refining")
        ),
        SyllabusChapter(
            id = "chem_carbon",
            chapterNumber = 4,
            textbookPart = 1,
            division = ScienceDivision.CHEMISTRY,
            titleEn = "Carbon and its Compounds",
            titleKn = "ಕಾರ್ಬನ್ ಮತ್ತು ಅದರ ಸಂಯುಕ್ತಗಳು",
            introduction = "Explains covalent bonding in carbon, catenation and tetravalency, saturated and unsaturated hydrocarbons, functional groups, homologous series, IUPAC naming, properties of ethanol and ethanoic acid, and cleansing action of soaps vs detergents.",
            objectives = listOf(
                "Explain covalent bonding, tetravalency, and catenation of carbon.",
                "Differentiate alkanes, alkenes, alkynes, and structural isomers.",
                "Identify functional groups (-OH, -CHO, >C=O, -COOH, -Cl/-Br).",
                "Explain esterification, saponification, and micelle formation by soap."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "chem_4_covalent_homologous",
                    title = "Covalent Bonding, Allotropes & Homologous Series",
                    kannadaTitle = "ಸಹವೇಲೆನ್ಸೀಯ ಬಂಧ ಮತ್ತು ಸಮಜಾತೀಯ ಶ್ರೇಣಿ",
                    simpleExplanation = "Carbon shares 4 valence electrons to form strong covalent bonds and long chains (catenation). A homologous series is a family of compounds with the same functional group where successive members differ by a -CH2- unit.",
                    detailedExplanation = "Carbon forms neither C4+ nor C4- ions due to huge energy/stability barriers. Saturated hydrocarbons (alkanes, CnH2n+2) have single bonds; unsaturated hydrocarbons have double (alkenes, CnH2n) or triple (alkynes, CnH2n-2) bonds. Isomers have same molecular formula but different structures (e.g., n-butane and isobutane).",
                    kannadaSummary = "ಕಾರ್ಬನ್ ಎಲೆಕ್ಟ್ರಾನ್‌ಗಳನ್ನು ಹಂಚಿಕೊಂಡು ಸಹವೇಲೆನ್ಸೀಯ ಬಂಧಗಳನ್ನು ಉಂಟುಮಾಡುತ್ತದೆ. ಕೆಟನೇಷನ್ ಮತ್ತು ಚತುರ್ವೇಲೆನ್ಸಿ ಇದರ ವಿಶೇಷ ಗುಣಗಳು.",
                    observations = listOf("Saturated hydrocarbons burn with a clean blue flame; unsaturated burn with a yellow sooty flame."),
                    dailyLifeApplications = listOf("Diamond (hardest, insulator) and Graphite (soft, conductor) are crystalline allotropes of carbon.")
                ),
                ScienceTopic(
                    id = "chem_4_ethanol_soap",
                    title = "Ethanol, Ethanoic Acid & Soaps/Detergents",
                    kannadaTitle = "ಎಥೆನಾಲ್, ಎಥನೋಯಿಕ್ ಆಮ್ಲ ಮತ್ತು ಸಾಬೂನುಗಳು",
                    simpleExplanation = "Ethanol (C2H5OH) reacts with Ethanoic acid (CH3COOH) to form sweet-smelling esters. Soap molecules have a water-loving ionic head and oil-loving hydrocarbon tail forming micelles to lift dirt.",
                    detailedExplanation = "Alkaline KMnO4 oxidizes ethanol to ethanoic acid. Conc. H2SO4 dehydrates ethanol at 443K to ethene. Esterification forms ester used in perfumes; Saponification reverses it with NaOH to make soap. Soaps form insoluble scum with Ca2+/Mg2+ in hard water, whereas detergents remain effective.",
                    kannadaSummary = "ಸಾಬೂನು ಅಣುಗಳು ಮಿಸೆಲ್ ರಚನೆ ಮಾಡಿ ಎಣ್ಣೆ ಕೊಳೆಯನ್ನು ಸ್ವಚ್ಛಗೊಳಿಸುತ್ತವೆ. ಕಠಿಣ ನೀರಿನಲ್ಲಿ ಡಿಟರ್ಜೆಂಟ್‌ಗಳು ಚೆನ್ನಾಗಿ ನೊರೆ ಕೊಡುತ್ತವೆ.",
                    observations = listOf("Ethanoic acid freezes in cold winter (Glacial acetic acid, m.p. 290 K)."),
                    dailyLifeApplications = listOf("5–8% solution of acetic acid in water is Vinegar.", "Detergents clean effectively even in hard borewell water.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Catenation", "ಕೆಟನೇಷನ್ (ಸರಣೀಕರಣ)", "Unique ability of carbon atoms to form covalent bonds with other carbon atoms giving rise to long chains, branched chains, or rings.", "ಕಾರ್ಬನ್ ಪರಮಾಣುಗಳು ಇತರ ಕಾರ್ಬನ್ ಪರಮಾಣುಗಳೊಂದಿಗೆ ಬಂಧ ಏರ್ಪಡಿಸಿ ಉದ್ದ ಸರಪಳಿ ರಚಿಸುವ ವಿಶಿಷ್ಟ ಗುಣ.", "Ch 4, Sec 4.2"),
                DefinitionEntry("Homologous Series", "ಸಮಜಾತೀಯ ಶ್ರೇಣಿ", "A series of carbon compounds with the same functional group where successive members differ by -CH2- (14 u mass).", "ಒಂದೇ ಕ್ರಿಯಾ ಗುಂಪನ್ನು ಹೊಂದಿದ್ದು ಅನುಕ್ರಮ ಸದಸ್ಯರ ನಡುವೆ -CH2- ವ್ಯತ್ಯಾಸವಿರುವ ಸಂಯುಕ್ತಗಳ ಶ್ರೇಣಿ.", "Ch 4, Sec 4.2.3")
            ),
            formulas = emptyList(),
            reactions = listOf(
                ChemicalReactionEntry("Esterification", "CH3COOH + C2H5OH --(Acid)--> CH3COOC2H5 + H2O", "Condensation / Esterification", "Sweet-smelling fruity ester formed", "Conc. H2SO4 catalyst + warm"),
                ChemicalReactionEntry("Hydrogenation of Vegetable Oils", "R2C=CR2 + H2 --(Ni/Pd)--> R2CH-CHR2", "Addition Reaction", "Unsaturated liquid oil turns into saturated fat", "Nickel catalyst + Heat")
            ),
            activities = listOf(
                ActivityEntry("Activity 4.10", "Soap vs Detergent in Hard Water", "Compare lather formation in distilled and CaCl2/MgSO4 water.", "Soap forms curdy white scum; detergent forms rich lather.", "Detergents do not form insoluble precipitates with Ca2+ and Mg2+ ions.")
            ),
            revisionNotes = listOf(
                "General formulas: Alkanes CnH2n+2, Alkenes CnH2n, Alkynes CnH2n-2.",
                "Functional groups: Alcohol (-OH), Aldehyde (-CHO), Ketone (>C=O), Carboxylic acid (-COOH).",
                "Soap micelle: hydrophilic ionic end faces water outside, hydrophobic tail traps oil inside."
            ),
            faqs = listOf(
                FaqEntry("Why are detergents effective in hard water while soaps are not?", "Charged ends of detergents do not form insoluble precipitates (scum) with calcium and magnesium ions in hard water.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c4_q1",
                    question = "Ethane, with the molecular formula C2H6, has:",
                    options = listOf("6 covalent bonds", "7 covalent bonds", "8 covalent bonds", "9 covalent bonds"),
                    correctIndex = 1,
                    explanation = "Ethane has 1 C–C bond and 6 C–H bonds, totaling 7 covalent bonds.",
                    marks = 1,
                    topicId = "chem_4_covalent_homologous"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 1, Chapter 4",
            simulationIds = listOf("sim_chem_carbon"),
            diagramIds = listOf("diag_soap_micelle")
        )
    )
}
