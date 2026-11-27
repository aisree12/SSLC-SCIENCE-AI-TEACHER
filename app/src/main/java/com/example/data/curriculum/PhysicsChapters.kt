package com.example.data.curriculum

import com.example.model.ActivityEntry
import com.example.model.ChapterQuizQuestion
import com.example.model.DefinitionEntry
import com.example.model.FaqEntry
import com.example.model.FormulaEntry
import com.example.model.ScienceDivision
import com.example.model.ScienceTopic
import com.example.model.SyllabusChapter

object PhysicsChapters {
    val all: List<SyllabusChapter> = listOf(
        SyllabusChapter(
            id = "phy_light",
            chapterNumber = 9,
            textbookPart = 2,
            division = ScienceDivision.PHYSICS,
            titleEn = "Light – Reflection and Refraction",
            titleKn = "ಬೆಳಕು – ಪ್ರತಿಫಲನ ಮತ್ತು ವಕ್ರೀಭವನ",
            introduction = "Covers laws of reflection, spherical mirrors (concave & convex), ray diagrams, mirror formula & magnification, refraction through glass slab, Snell's Law, refractive index, spherical lenses, lens formula, and power of a lens (Dioptre).",
            objectives = listOf(
                "Apply New Cartesian Sign Convention cleanly for mirrors and lenses.",
                "Draw ray diagrams for all object positions for concave/convex mirrors and convex/concave lenses.",
                "Solve numericals using 1/f = 1/v + 1/u (Mirror) and 1/f = 1/v - 1/u (Lens).",
                "Calculate refractive index n = c/v and lens power P = 1/f(m)."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "phy_9_mirrors",
                    title = "Spherical Mirrors, Ray Diagrams & Mirror Formula",
                    kannadaTitle = "ಗೋಳೀಯ ದರ್ಪಣಗಳು, ಕಿರಣ ಚಿತ್ರಗಳು ಮತ್ತು ದರ್ಪಣ ಸೂತ್ರ",
                    simpleExplanation = "Concave mirrors converge light (used in headlights, dentist mirrors, solar furnaces); convex mirrors diverge light giving wide upright views (used as rear-view mirrors).",
                    detailedExplanation = "Radius of curvature R = 2f. Mirror formula: 1/f = 1/v + 1/u. Magnification m = h'/h = -v/u. Negative m indicates real & inverted image; positive m indicates virtual & erect image.",
                    kannadaSummary = "ದರ್ಪಣ ಸೂತ್ರ: 1/f = 1/v + 1/u. ನಿಮ್ನ ದರ್ಪಣ ಬೆಳಕನ್ನು ಕೇಂದ್ರೀಕರಿಸುತ್ತದೆ; ಪೀನ ದರ್ಪಣ ವಾಹನಗಳ ಹಿನ್ನೋಟ ಕನ್ನಡಿಯಾಗಿದೆ.",
                    observations = listOf("Sun's rays converge at principal focus of a concave mirror igniting paper."),
                    dailyLifeApplications = listOf("Convex rear-view mirror shows wider field of view and always forms erect diminished images.")
                ),
                ScienceTopic(
                    id = "phy_9_refraction_lenses",
                    title = "Refraction, Snell's Law, Lenses & Power",
                    kannadaTitle = "ಬೆಳಕಿನ ವಕ್ರೀಭವನ, ಸ್ನೆಲ್ ನಿಯಮ, ಮಸೂರಗಳು ಮತ್ತು ಸಾಮರ್ಥ್ಯ",
                    simpleExplanation = "Light bends when changing optical medium due to speed change. Convex lenses converge rays (positive power +D); concave lenses diverge rays (negative power -D).",
                    detailedExplanation = "Snell's Law: sin(i)/sin(r) = n21 (constant). Absolute refractive index n = c/v (diamond n = 2.42). Lens formula: 1/f = 1/v - 1/u; m = v/u. Power P = 1/f (f in metres), SI unit Dioptre (D).",
                    kannadaSummary = "ಮಸೂರ ಸೂತ್ರ: 1/f = 1/v - 1/u. ಮಸೂರದ ಸಾಮರ್ಥ್ಯದ SI ಏಕಮಾನ ಡಯಾಪ್ಟರ್ (D).",
                    observations = listOf("Pencil partly immersed in water appears bent at the air-water interface."),
                    dailyLifeApplications = listOf("Opticians prescribe corrective spectacle powers in Dioptres (+ for convex, - for concave).")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Principal Focus", "ಪ್ರಧಾನ ಸಂಗಮ", "Point on principal axis where rays parallel to the axis converge (or appear to diverge from) after reflection/refraction.", "ಪ್ರಧಾನ ಅಕ್ಷಕ್ಕೆ ಸಮಾನಾಂತರ ಕಿರಣಗಳು ಪ್ರತಿಫಲನದ ನಂತರ ಸೇರುವ ಬಿಂದು.", "Ch 9, Sec 9.2"),
                DefinitionEntry("1 Dioptre", "1 ಡಯಾಪ್ಟರ್", "Power of a lens whose focal length is exactly 1 metre (1 D = 1 m^-1).", "1 ಮೀಟರ್ ಸಂಗಮದೂರ ಹೊಂದಿರುವ ಮಸೂರದ ಸಾಮರ್ಥ್ಯ.", "Ch 9, Sec 9.3.8")
            ),
            formulas = listOf(
                FormulaEntry("Mirror Formula", "1/f = 1/v + 1/u", "f: focal length, v: image distance, u: object distance", "m / cm", "Ch 9, Eq 9.1"),
                FormulaEntry("Mirror Magnification", "m = h'/h = -v/u", "h': image height, h: object height", "Dimensionless", "Ch 9, Eq 9.2"),
                FormulaEntry("Lens Formula", "1/f = 1/v - 1/u", "f: focal length, v: image distance, u: object distance", "m / cm", "Ch 9, Eq 9.8"),
                FormulaEntry("Power of a Lens", "P = 1 / f(in metres)", "P: power, f: focal length in m", "Dioptre (D)", "Ch 9, Eq 9.11"),
                FormulaEntry("Refractive Index", "n = c / v", "c = 3×10^8 m/s, v = speed in medium", "Dimensionless", "Ch 9, Eq 9.5")
            ),
            reactions = emptyList(),
            activities = listOf(
                ActivityEntry("Activity 9.10", "Refraction through Rectangular Glass Slab", "Trace incident and emergent rays through a glass slab using pins.", "Emergent ray is parallel to incident ray but laterally displaced.", "Light bends towards normal in denser medium and away in rarer medium.")
            ),
            revisionNotes = listOf(
                "Object distance u is ALWAYS negative in New Cartesian Sign Convention.",
                "Concave mirror & concave lens have negative focal length (-f); Convex mirror & convex lens have positive (+f).",
                "For concave mirror, only when object is between P and F is image virtual, erect, and magnified."
            ),
            faqs = listOf(
                FaqEntry("Why do we prefer a convex mirror as a rear-view mirror in vehicles?", "It always forms an erect, though diminished, image and has a much wider field of view because it is curved outwards.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c9_q1",
                    question = "Where should an object be placed in front of a convex lens to get a real image of the size of the object?",
                    options = listOf("At the principal focus", "At twice the focal length (2F1)", "At infinity", "Between optical centre and focus"),
                    correctIndex = 1,
                    explanation = "Placing an object at 2F1 produces a real, inverted image of the same size at 2F2.",
                    marks = 1,
                    topicId = "phy_9_refraction_lenses"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 2, Chapter 9",
            simulationIds = listOf("sim_phy_optics"),
            diagramIds = listOf("diag_concave_mirror_rays", "diag_convex_lens_rays")
        ),
        SyllabusChapter(
            id = "phy_human_eye",
            chapterNumber = 10,
            textbookPart = 2,
            division = ScienceDivision.PHYSICS,
            titleEn = "The Human Eye and the Colourful World",
            titleKn = "ಮಾನವನ ಕಣ್ಣು ಮತ್ತು ವರ್ಣಮಯ ಜಗತ್ತು",
            introduction = "Explains the anatomy of the human eye, power of accommodation, defects of vision (Myopia, Hypermetropia, Presbyopia) and their lens corrections, refraction and dispersion of white light through a glass prism, atmospheric refraction (twinkling stars), and scattering of light (Tyndall effect, blue sky).",
            objectives = listOf(
                "Identify parts of the human eye (Cornea, Iris, Pupil, Crystalline lens, Retina, Optic nerve).",
                "Differentiate Myopia, Hypermetropia, and Presbyopia with corrective ray diagrams.",
                "Explain Newton's prism dispersion (VIBGYOR) and rainbow formation.",
                "Explain twinkling of stars, advanced sunrise, blue sky, and red danger signals."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "phy_10_eye_defects",
                    title = "Human Eye & Vision Defects",
                    kannadaTitle = "ಮಾನವನ ಕಣ್ಣು ಮತ್ತು ದೃಷ್ಟಿ ದೋಷಗಳು",
                    simpleExplanation = "Ciliary muscles adjust lens focal length (Power of Accommodation) between 25 cm (near point) and infinity. Myopia (near-sightedness) is corrected by Concave lens; Hypermetropia (far-sightedness) by Convex lens.",
                    detailedExplanation = "In Myopia, far objects focus in front of retina (elongated eyeball or excessive lens curvature) -> corrected by diverging concave lens. In Hypermetropia, near objects focus behind retina (shortened eyeball or long focal length) -> corrected by converging convex lens. Presbyopia occurs in old age due to weakening ciliary muscles -> bifocal lens.",
                    kannadaSummary = "ಸಮೀಪದೃಷ್ಟಿ (ಮಯೋಪಿಯಾ) ಸರಿಪಡಿಸಲು ನಿಮ್ನ ಮಸೂರ ಮತ್ತು ದೂರದೃಷ್ಟಿ (ಹೈಪರ್‌ಮೆಟ್ರೋಪಿಯಾ) ಸರಿಪಡಿಸಲು ಪೀನ ಮಸೂರ ಬಳಸಲಾಗುತ್ತದೆ.",
                    observations = listOf("Blind spot has no rods or cones; least distance of distinct vision is 25 cm."),
                    dailyLifeApplications = listOf("Bifocal glasses combine upper concave (distance) and lower convex (reading) lenses.")
                ),
                ScienceTopic(
                    id = "phy_10_dispersion_scattering",
                    title = "Prism Dispersion, Atmospheric Refraction & Scattering",
                    kannadaTitle = "ಅಶ್ರಗದ ಮೂಲಕ ವರ್ಣವಿಭಜನೆ ಮತ್ತು ಬೆಳಕಿನ ಚದುರುವಿಕೆ",
                    simpleExplanation = "A glass prism splits white light into VIBGYOR because violet bends most and red bends least. Fine air molecules scatter short blue wavelengths making the sky look blue.",
                    detailedExplanation = "Atmospheric refraction causes stars to twinkle (planets are extended sources so variations average out) and makes Sun visible ~2 mins before actual sunrise and after sunset. Tyndall effect is scattering of light by colloidal particles. Red light has longest wavelength, scatters least in fog/smoke, and is visible from far.",
                    kannadaSummary = "ಕಡಿಮೆ ತರಂಗಾಂತರದ ನೀಲಿ ಬಣ್ಣ ಹೆಚ್ಚು ಚದುರುವುದರಿಂದ ಆಕಾಶ ನೀಲಿಯಾಗಿ ಕಾಣುತ್ತದೆ; ಕೆಂಪು ಬಣ್ಣ ಕಡಿಮೆ ಚದುರುತ್ತದೆ.",
                    observations = listOf("Sunbeam entering a dusty room becomes visible due to Tyndall effect."),
                    dailyLifeApplications = listOf("Red traffic/railway danger signals travel farthest through fog without scattering.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Power of Accommodation", "ಕಣ್ಣಿನ ಹೊಂದಾಣಿಕೆ ಸಾಮರ್ಥ್ಯ", "Ability of the human eye lens to adjust its focal length with the help of ciliary muscles to focus near and distant objects.", "ಸಿಲಿಯರಿ ಸ್ನಾಯುಗಳ ಸಹಾಯದಿಂದ ಕಣ್ಣಿನ ಮಸೂರವು ತನ್ನ ಸಂಗಮದೂರವನ್ನು ಹೊಂದಿಸಿಕೊಳ್ಳುವ ಸಾಮರ್ಥ್ಯ.", "Ch 10, Sec 10.1.1"),
                DefinitionEntry("Tyndall Effect", "ಟಿಂಡಾಲ್ ಪರಿಣಾಮ", "Scattering of a beam of light by colloidal particles making the path of light visible.", "ಕಲಿಲ ಕಣಗಳಿಂದ ಬೆಳಕಿನ ಕಿರಣವು ಚದುರಿ ಬೆಳಕಿನ ಪಥ ಗೋಚರವಾಗುವ ವಿದ್ಯಮಾನ.", "Ch 10, Sec 10.6.1")
            ),
            formulas = listOf(
                FormulaEntry("Corrective Lens Power", "P = 1 / f(m)", "For Myopia f = -(far point); Hypermetropia uses 1/f = 1/v - 1/u", "Dioptre (D)", "Ch 10, Sec 10.2")
            ),
            reactions = emptyList(),
            activities = listOf(
                ActivityEntry("Activity 10.2", "Newton's Double Prism Recombination", "Place second inverted prism behind first dispersing prism.", "Seven VIBGYOR colours recombine into white light.", "White light consists of seven component colours.")
            ),
            revisionNotes = listOf(
                "Near point of normal young adult eye = 25 cm; Far point = Infinity.",
                "Violet bends the most (shortest wavelength); Red bends the least (longest wavelength).",
                "Rainbow formation involves Refraction, Dispersion, and Internal Reflection inside water droplets."
            ),
            faqs = listOf(
                FaqEntry("Why does the sky appear dark instead of blue to an astronaut?", "At high altitudes in space there is no atmosphere to scatter sunlight, so the sky appears dark.", "1–2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c10_q1",
                    question = "The human eye forms the image of an object at its:",
                    options = listOf("Cornea", "Iris", "Pupil", "Retina"),
                    correctIndex = 3,
                    explanation = "The light-sensitive retina acts as the screen where the real inverted image is formed.",
                    marks = 1,
                    topicId = "phy_10_eye_defects"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 2, Chapter 10",
            simulationIds = listOf("sim_phy_eye"),
            diagramIds = listOf("diag_human_eye", "diag_myopia_correction", "diag_hypermetropia_correction")
        ),
        SyllabusChapter(
            id = "phy_electricity",
            chapterNumber = 11,
            textbookPart = 2,
            division = ScienceDivision.PHYSICS,
            titleEn = "Electricity",
            titleKn = "ವಿದ್ಯುತ್",
            introduction = "Teaches electric charge and current, electric potential difference, circuit symbols, Ohm's law, resistance and resistivity, series and parallel combinations of resistors, Joule's law of heating, and electric power & commercial energy (kWh).",
            objectives = listOf(
                "Define 1 Ampere, 1 Volt, 1 Ohm, and 1 Watt with SI units.",
                "State and verify Ohm's Law (V = IR) and factors affecting resistance R = ρL/A.",
                "Calculate equivalent resistance in series (Rs = R1+R2) and parallel (1/Rp = 1/R1+1/R2).",
                "Solve numericals on Joule heating H = I^2Rt and electrical energy in kWh (units)."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "phy_11_ohms_resistance",
                    title = "Current, Potential Difference & Ohm's Law",
                    kannadaTitle = "ವಿದ್ಯುತ್ ಪ್ರವಾಹ, ವಿಭವಾಂತರ ಮತ್ತು ಓಮ್ ನಿಯಮ",
                    simpleExplanation = "Current I = Q/t (Ampere) is flow of charge per second. Potential difference V = W/Q (Volt). Ohm's law states V is directly proportional to I at constant temperature: V = IR.",
                    detailedExplanation = "Ammeter measures current (low resistance, connected in series); Voltmeter measures potential difference (high resistance, connected in parallel). Resistance R = ρ(L/A) depends on length (∝ L), cross-section area (∝ 1/A), material resistivity ρ (Ω·m), and temperature. Alloys like Nichrome have high resistivity and don't oxidize easily.",
                    kannadaSummary = "ಸ್ಥಿರ ತಾಪಮಾನದಲ್ಲಿ ವಾಹಕದ ಮೂಲಕ ಹರಿಯುವ ವಿದ್ಯುತ್ ಪ್ರವಾಹವು ಅದರ ತುದಿಗಳ ನಡುವಿನ ವಿಭವಾಂತರಕ್ಕೆ ನೇರ ಅನುಪಾತದಲ್ಲಿರುತ್ತದೆ (V = IR).",
                    observations = listOf("V-I graph for an ohmic conductor is a straight line passing through the origin."),
                    dailyLifeApplications = listOf("Tungsten filament (m.p. 3380°C) in bulbs; Nichrome in electric irons and toasters.")
                ),
                ScienceTopic(
                    id = "phy_11_circuits_power",
                    title = "Series & Parallel Circuits, Heating & Power",
                    kannadaTitle = "ಸರಣಿ ಮತ್ತು ಸಮಾಂತರ ಜೋಡಣೆ, ಉಷ್ಣೋತ್ಪಾದನೆ ಮತ್ತು ಸಾಮರ್ಥ್ಯ",
                    simpleExplanation = "In series, current is same through all resistors (Rs = R1+R2+...). In parallel, voltage is same across each branch (1/Rp = 1/R1+1/R2+...). Domestic circuits use parallel wiring so each appliance gets full 220 V independently.",
                    detailedExplanation = "Joule's Law of Heating: H = I^2Rt = VIt. Electric Power P = VI = I^2R = V^2/R. Commercial unit 1 kWh (1 unit) = 3.6 × 10^6 Joules. Electric fuse (low melting alloy like Pb-Sn) protects appliances in series on the live wire.",
                    kannadaSummary = "ಜೌಲ್‌ನ ಉಷ್ಣೋತ್ಪಾದನಾ ನಿಯಮ H = I^2Rt. ಗೃಹೋಪಯೋಗಿ ಮಂಡಲಗಳಲ್ಲಿ ಸಮಾಂತರ ಜೋಡಣೆ ಬಳಸುತ್ತಾರೆ.",
                    observations = listOf("Adding resistors in parallel decreases total equivalent resistance below the smallest resistor."),
                    dailyLifeApplications = listOf("Calculating monthly BESCOM/HESCOM electricity bills using kWh = (Watts × hours)/1000.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Ohm's Law", "ಓಮ್‌ನ ನಿಯಮ", "At constant temperature, potential difference V across a metallic conductor is directly proportional to current I flowing through it (V = IR).", "ಸ್ಥಿರ ತಾಪಮಾನದಲ್ಲಿ ವಾಹಕದ ತುದಿಗಳ ನಡುವಿನ ವಿಭವಾಂತರವು ವಿದ್ಯುತ್ ಪ್ರವಾಹಕ್ಕೆ ನೇರ ಅನುಪಾತದಲ್ಲಿರುತ್ತದೆ.", "Ch 11, Sec 11.4"),
                DefinitionEntry("1 Kilowatt-hour (1 Unit)", "1 ಕಿಲೋವ್ಯಾಟ್-ಗಂಟೆ", "Energy consumed when 1000 W appliance runs for 1 hour: 1 kWh = 3.6 × 10^6 J.", "1000 ವ್ಯಾಟ್ ಉಪಕರಣ 1 ಗಂಟೆ ಬಳಸಿದಾಗ ಖರ್ಚಾಗುವ ವಿದ್ಯುತ್ ಶಕ್ತಿ (3.6 × 10^6 J).", "Ch 11, Sec 11.8")
            ),
            formulas = listOf(
                FormulaEntry("Electric Current", "I = Q / t", "Q in Coulombs, t in seconds", "Ampere (A)", "Ch 11, Eq 11.1"),
                FormulaEntry("Potential Difference", "V = W / Q", "W in Joules, Q in Coulombs", "Volt (V)", "Ch 11, Eq 11.2"),
                FormulaEntry("Ohm's Law", "V = I × R", "V in Volts, I in Amperes, R in Ohms", "Ohm (Ω)", "Ch 11, Eq 11.5"),
                FormulaEntry("Resistivity", "R = ρ × (L / A)", "ρ: resistivity, L: length, A: area", "ρ in Ω·m", "Ch 11, Eq 11.10"),
                FormulaEntry("Joule's Law of Heating", "H = I² × R × t", "I in A, R in Ω, t in s", "Joule (J)", "Ch 11, Eq 11.21"),
                FormulaEntry("Electric Power", "P = V×I = I²R = V²/R", "P: power", "Watt (W)", "Ch 11, Eq 11.22")
            ),
            reactions = emptyList(),
            activities = listOf(
                ActivityEntry("Activity 11.1", "Verification of Ohm's Law", "Vary cells 1 to 4 across Nichrome wire XY and record Ammeter & Voltmeter.", "V/I remains constant across readings.", "V is directly proportional to I.")
            ),
            revisionNotes = listOf(
                "Charge on 1 electron e = 1.6 × 10^-19 C; 1 C has 6.25 × 10^18 electrons.",
                "Parallel circuit advantages: independent switches, same 220 V, lower total resistance.",
                "1 kWh = 3.6 × 10^6 J."
            ),
            faqs = listOf(
                FaqEntry("Why are coils of electric toasters and electric irons made of an alloy rather than a pure metal?", "Alloys have higher resistivity than constituent metals and do not oxidize (burn) readily at high temperatures.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c11_q1",
                    question = "An electric bulb is rated 220 V and 100 W. When operated on 110 V, the power consumed will be:",
                    options = listOf("100 W", "75 W", "50 W", "25 W"),
                    correctIndex = 3,
                    explanation = "R = V^2/P = 220^2/100 = 484 Ω. At 110 V, P' = 110^2 / 484 = 25 W.",
                    marks = 1,
                    topicId = "phy_11_circuits_power"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 2, Chapter 11",
            simulationIds = listOf("sim_phy_ohms_circuits"),
            diagramIds = listOf("diag_ohms_circuit", "diag_resistors_series_parallel")
        ),
        SyllabusChapter(
            id = "phy_magnetism",
            chapterNumber = 12,
            textbookPart = 2,
            division = ScienceDivision.PHYSICS,
            titleEn = "Magnetic Effects of Electric Current",
            titleKn = "ವಿದ್ಯುತ್ ಪ್ರವಾಹದ ಕಾಂತೀಯ ಪರಿಣಾಮಗಳು",
            introduction = "Explores magnetic field lines around bar magnets and current-carrying conductors, Right-Hand Thumb Rule, Solenoid & Electromagnets, Force on a conductor (Fleming's Left-Hand Rule), AC vs DC, and Domestic Electric Circuits (Live, Neutral, Earth wires, Overloading & Short-circuiting).",
            objectives = listOf(
                "State properties of magnetic field lines (closed loops, never intersect).",
                "Apply Right-Hand Thumb Rule for straight wires/loops and Fleming's Left-Hand Rule for force.",
                "Describe solenoid magnetic field and factors strengthening an electromagnet.",
                "Explain domestic wiring safety: Live (Red), Neutral (Black), Earth (Green), Fuse & Short-circuit."
            ),
            topics = listOf(
                ScienceTopic(
                    id = "phy_12_fields_solenoid",
                    title = "Magnetic Fields, Right-Hand Thumb Rule & Solenoid",
                    kannadaTitle = "ಕಾಂತಕ್ಷೇತ್ರ, ಬಲಗೈ ಹೆಬ್ಬೆರಳು ನಿಯಮ ಮತ್ತು ಸೊಲೆನಾಯ್ಡ್",
                    simpleExplanation = "Every current-carrying wire produces a magnetic field around it (Oersted). Coiling insulated wire into a cylinder forms a solenoid whose field resembles a bar magnet.",
                    detailedExplanation = "Right-Hand Thumb Rule: Thumb points in current direction, curled fingers show magnetic field lines. Inside a solenoid, field lines are parallel and uniform. Inserting a soft iron core inside creates a strong temporary Electromagnet. Field strength increases with current and number of turns.",
                    kannadaSummary = "ಸೊಲೆನಾಯ್ಡ್ ಒಳಗೆ ಕಾಂತಕ್ಷೇತ್ರ ಏಕರೂಪವಾಗಿರುತ್ತದೆ. ಮೃದು ಕಬ್ಬಿಣದ ದಿಂಡು ಹಾಕಿದರೆ ಪ್ರಬಲ ವಿದ್ಯುತ್ ಕಾಂತವಾಗುತ್ತದೆ.",
                    observations = listOf("Iron filings around a bar magnet align along curved field lines from North to South outside."),
                    dailyLifeApplications = listOf("Electromagnets in scrapyard cranes, electric bells, and MRI scanners.")
                ),
                ScienceTopic(
                    id = "phy_12_force_domestic",
                    title = "Fleming's Left-Hand Rule & Domestic Electric Circuits",
                    kannadaTitle = "ಫ್ಲೆಮಿಂಗ್ ಎಡಗೈ ನಿಯಮ ಮತ್ತು ಗೃಹ ವಿದ್ಯುತ್ ಮಂಡಲ",
                    simpleExplanation = "A current-carrying wire in a magnetic field experiences a force perpendicular to both field and current (Fleming's Left-Hand Rule: Forefinger-Field, Center-Current, Thumb-Force/Motion). Domestic homes receive 220 V AC at 50 Hz.",
                    detailedExplanation = "Domestic supply has Live wire (red insulation, 220 V), Neutral wire (black, 0 V), and Earth wire (green, connected to metal plate in ground to prevent shock from metallic appliance bodies). Overloading occurs if too many high-power appliances run on one socket; Short-circuit occurs when live and neutral wires touch directly. A fuse melts by Joule heating to break the circuit.",
                    kannadaSummary = "ಗೃಹ ವಿದ್ಯುತ್ ಪೂರೈಕೆ 220V, 50Hz AC ಆಗಿದ್ದು ಲೈವ್, ನ್ಯೂಟ್ರಲ್ ಮತ್ತು ಅರ್ಥ್ ತಂತಿಗಳನ್ನು ಹೊಂದಿರುತ್ತದೆ.",
                    observations = listOf("Aluminium rod AB jumps perpendicular to horseshoe magnet field when switch closes."),
                    dailyLifeApplications = listOf("Earthing protects users from shocks on electric iron/geyser bodies; MCBs/fuses stop fires.")
                )
            ),
            definitions = listOf(
                DefinitionEntry("Right-Hand Thumb Rule", "ಬಲಗೈ ಹೆಬ್ಬೆರಳು ನಿಯಮ", "Imagine holding a current-carrying straight conductor in your right hand with thumb pointing along current; curled fingers indicate magnetic field direction.", "ವಿದ್ಯುತ್ ವಾಹಕವನ್ನು ಬಲಗೈಯಲ್ಲಿ ಹಿಡಿದಾಗ ಹೆಬ್ಬೆರಳು ವಿದ್ಯುತ್ ದಿಕ್ಕನ್ನು ಮತ್ತು ಬೆರಳುಗಳು ಕಾಂತಕ್ಷೇತ್ರದ ದಿಕ್ಕನ್ನು ಸೂಚಿಸುತ್ತವೆ.", "Ch 12, Sec 12.2.1"),
                DefinitionEntry("Short-Circuiting", "ಹ್ರಸ್ವಮಂಡಲ (ಶಾರ್ಟ್ ಸರ್ಕ್ಯೂಟ್)", "Sudden huge current flow when live wire and neutral wire come into direct contact due to damaged insulation or appliance fault.", "ಲೈವ್ ಮತ್ತು ನ್ಯೂಟ್ರಲ್ ತಂತಿಗಳು ನೇರ ಸಂಪರ್ಕಕ್ಕೆ ಬಂದಾಗ ಅತಿಯಾದ ವಿದ್ಯುತ್ ಹರಿಯುವುದು.", "Ch 12, Sec 12.4")
            ),
            formulas = emptyList(),
            reactions = emptyList(),
            activities = listOf(
                ActivityEntry("Activity 12.7", "Force on Current-Carrying Conductor", "Pass current through aluminium rod suspended between poles of horseshoe magnet.", "Rod displaces; reversing current or magnet poles reverses displacement.", "Force acts perpendicular to both magnetic field and current.")
            ),
            revisionNotes = listOf(
                "Magnetic field lines emerge from North pole and merge at South pole outside a magnet; never intersect.",
                "AC in India changes direction every 1/100 s (frequency = 50 Hz, 220 V).",
                "Earth wire provides a low-resistance conducting path to ground for leaked current."
            ),
            faqs = listOf(
                FaqEntry("Why don't two magnetic field lines intersect each other?", "If they intersected, at the point of intersection a compass needle would point in two directions simultaneously, which is impossible.", "2 Marks")
            ),
            quizQuestions = listOf(
                ChapterQuizQuestion(
                    id = "c12_q1",
                    question = "The magnetic field inside a long straight solenoid carrying current:",
                    options = listOf("Is zero", "Decreases towards its ends", "Increases towards its ends", "Is the same at all points"),
                    correctIndex = 3,
                    explanation = "Magnetic field lines inside a long straight solenoid are parallel and equidistant, indicating a uniform magnetic field.",
                    marks = 1,
                    topicId = "phy_12_fields_solenoid"
                )
            ),
            textbookSourceRef = "KTBS Karnataka SSLC Science Part 2, Chapter 12",
            simulationIds = listOf("sim_phy_electromagnet"),
            diagramIds = listOf("diag_domestic_circuit", "diag_solenoid_field")
        )
    )
}
