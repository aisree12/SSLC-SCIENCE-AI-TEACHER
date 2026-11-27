package com.example.data.lab

import com.example.model.DiagramHotspot
import com.example.model.ScienceDiagram
import com.example.model.ScienceDivision
import com.example.model.ScienceSimulation
import com.example.model.SimulationCategory

object SimulationAndDiagramCatalog {

    val simulations: List<ScienceSimulation> = listOf(
        ScienceSimulation(
            id = "sim_phy_optics",
            chapterId = "phy_light",
            category = SimulationCategory.PHYSICS,
            titleEn = "Mirrors & Lenses Ray Optics Lab",
            titleKn = "ದರ್ಪಣ ಮತ್ತು ಮಸೂರ ಕಿರಣ ದೃಗ್ವಿಜ್ಞಾನ ಪ್ರಯೋಗಾಲಯ",
            summaryEn = "Adjust object distance (u) and focal length (f) for Concave Mirror or Convex Lens to inspect real-time image formation, magnification, and ray paths.",
            summaryKn = "ನಿಮ್ನ ದರ್ಪಣ ಅಥವಾ ಪೀನ ಮಸೂರದ ವಸ್ತು ದೂರ (u) ಮತ್ತು ಸಂಗಮದೂರ (f) ಬದಲಾಯಿಸಿ ಪ್ರತಿಬಿಂಬ ಮತ್ತು ವರ್ಧನೆಯನ್ನು ವೀಕ್ಷಿಸಿ.",
            equationOrLaw = "Mirror: 1/f = 1/v + 1/u  |  Lens: 1/f = 1/v - 1/u"
        ),
        ScienceSimulation(
            id = "sim_phy_ohms_circuits",
            chapterId = "phy_electricity",
            category = SimulationCategory.PHYSICS,
            titleEn = "Ohm's Law & Series/Parallel Circuit Bench",
            titleKn = "ಓಮ್‌ನ ನಿಯಮ ಹಾಗೂ ಸರಣಿ/ಸಮಾಂತರ ಮಂಡಲ ಪ್ರಯೋಗ",
            summaryEn = "Vary supply voltage (V), resistors R1 & R2, and switch between Series and Parallel wiring to observe live current flow, power dissipation, and V–I linearity.",
            summaryKn = "ವೋಲ್ಟೇಜ್ (V) ಮತ್ತು ರೋಧಗಳನ್ನು (R1, R2) ಬದಲಾಯಿಸಿ ಸರಣಿ ಮತ್ತು ಸಮಾಂತರ ಮಂಡಲಗಳಲ್ಲಿ ವಿದ್ಯುತ್ ಪ್ರವಾಹವನ್ನು ಅಳೆಯಿರಿ.",
            equationOrLaw = "V = I × R_eq  |  P = V × I"
        ),
        ScienceSimulation(
            id = "sim_phy_electromagnet",
            chapterId = "phy_magnetism",
            category = SimulationCategory.PHYSICS,
            titleEn = "Solenoid Electromagnet & Fleming's Rule",
            titleKn = "ಸೊಲೆನಾಯ್ಡ್ ವಿದ್ಯುತ್ ಕಾಂತ ಮತ್ತು ಫ್ಲೆಮಿಂಗ್ ನಿಯಮ",
            summaryEn = "Toggle current direction, adjust coil turns and current strength to visualize magnetic field lines, N/S poles, and conductor force direction.",
            summaryKn = "ವಿದ್ಯುತ್ ದಿಕ್ಕು ಮತ್ತು ಸುರುಳಿಗಳ ಸಂಖ್ಯೆಯನ್ನು ಬದಲಾಯಿಸಿ ಕಾಂತಕ್ಷೇತ್ರದ ಬಲರೇಖೆಗಳು ಮತ್ತು ಧ್ರುವಗಳನ್ನು ಗಮನಿಸಿ.",
            equationOrLaw = "B ∝ n × I  (Right-Hand Thumb & Fleming's Left-Hand Rule)"
        ),
        ScienceSimulation(
            id = "sim_phy_eye",
            chapterId = "phy_human_eye",
            category = SimulationCategory.PHYSICS,
            titleEn = "Human Eye & Vision Correction Simulator",
            titleKn = "ಮಾನವನ ಕಣ್ಣು ಮತ್ತು ದೃಷ್ಟಿ ದೋಷ ಸರಿಪಡಿಸುವಿಕೆ",
            summaryEn = "Switch between Normal Vision, Myopia, and Hypermetropia, then apply Concave or Convex corrective lenses to focus rays cleanly onto the retina.",
            summaryKn = "ಸಾಮಾನ್ಯ ದೃಷ್ಟಿ, ಮಯೋಪಿಯಾ ಮತ್ತು ಹೈಪರ್‌ಮೆಟ್ರೋಪಿಯಾ ಸ್ಥಿತಿಗಳಲ್ಲಿ ಮಸೂರಗಳ ಪರಿಣಾಮವನ್ನು ಪರೀಕ್ಷಿಸಿ.",
            equationOrLaw = "P (Dioptre) = 1 / f(m)"
        ),
        ScienceSimulation(
            id = "sim_chem_ph",
            chapterId = "chem_acids_bases",
            category = SimulationCategory.CHEMISTRY,
            titleEn = "Interactive pH Scale & Indicator Lab",
            titleKn = "pH ಮಾಪಕ ಮತ್ತು ಆಮ್ಲ-ಪ್ರತ್ಯಾಮ್ಲ ಸೂಚಕಗಳ ಪ್ರಯೋಗ",
            summaryEn = "Test household and lab solutions across pH 0–14 and observe real-time colour changes in Litmus, Phenolphthalein, Methyl Orange, and Universal Indicator.",
            summaryKn = "pH 0 ರಿಂದ 14 ರವರೆಗೆ ವಿವಿಧ ದ್ರಾವಣಗಳ ಲಿಟ್ಮಸ್ ಮತ್ತು ಫಿನಾಫ್ತಲೀನ್ ಬಣ್ಣ ಬದಲಾವಣೆಯನ್ನು ಗಮನಿಸಿ.",
            equationOrLaw = "Acid: [H+] > [OH-] (pH < 7)  |  Base: [OH-] > [H+] (pH > 7)"
        ),
        ScienceSimulation(
            id = "sim_chem_balancer",
            chapterId = "chem_reactions",
            category = SimulationCategory.CHEMISTRY,
            titleEn = "Chemical Equation Balancer & Atom Counter",
            titleKn = "ರಾಸಾಯನಿಕ ಸಮೀಕರಣ ಸರಿದೂಗಿಸುವ ಪ್ರಯೋಗ",
            summaryEn = "Step through stoichiometric coefficients on key KSEAB board reactions and verify Law of Conservation of Mass atom by atom.",
            summaryKn = "ಪ್ರಮುಖ SSLC ರಾಸಾಯನಿಕ ಕ್ರಿಯೆಗಳ ಗುಣಾಂಕಗಳನ್ನು ಬದಲಾಯಿಸಿ ಪರಮಾಣುಗಳ ಸಂಖ್ಯೆಯನ್ನು ಸಮತೋಲನಗೊಳಿಸಿ.",
            equationOrLaw = "Total LHS Atoms == Total RHS Atoms (Conservation of Mass)"
        ),
        ScienceSimulation(
            id = "sim_chem_reactivity",
            chapterId = "chem_metals",
            category = SimulationCategory.CHEMISTRY,
            titleEn = "Metals Reactivity Series Displacement Tester",
            titleKn = "ಲೋಹಗಳ ಕ್ರಿಯಾಶೀಲತೆಯ ಶ್ರೇಣಿ ಮತ್ತು ಸ್ಥಾನಪಲ್ಲಟ",
            summaryEn = "Dip Zn, Fe, Cu, or Ag strips into metal salt solutions (FeSO4, CuSO4, ZnSO4, AgNO3) to safely observe whether displacement occurs.",
            summaryKn = "ವಿವಿಧ ಲೋಹಗಳನ್ನು ಲವಣ ದ್ರಾವಣಗಳಲ್ಲಿ ಇರಿಸಿ ಸ್ಥಾನಪಲ್ಲಟ ಕ್ರಿಯೆ ನಡೆಯುತ್ತದೆಯೇ ಎಂದು ಪರೀಕ್ಷಿಸಿ.",
            equationOrLaw = "K > Na > Ca > Mg > Al > Zn > Fe > Pb > [H] > Cu > Ag > Au"
        ),
        ScienceSimulation(
            id = "sim_chem_carbon",
            chapterId = "chem_carbon",
            category = SimulationCategory.CHEMISTRY,
            titleEn = "Carbon Functional Groups & Homologous Explorer",
            titleKn = "ಕಾರ್ಬನ್ ಕ್ರಿಯಾ ಗುಂಪುಗಳು ಮತ್ತು ಸಮಜಾತೀಯ ಶ್ರೇಣಿ",
            summaryEn = "Select carbon chain length (1–4) and functional group (-OH, -CHO, -COOH, Alkene) to inspect structural formula, IUPAC name, and flame test.",
            summaryKn = "ಕಾರ್ಬನ್ ಸರಪಳಿ ಮತ್ತು ಕ್ರಿಯಾ ಗುಂಪನ್ನು ಆಯ್ಕೆ ಮಾಡಿ ಅಣುಸೂತ್ರ ಹಾಗೂ IUPAC ಹೆಸರನ್ನು ವೀಕ್ಷಿಸಿ.",
            equationOrLaw = "Alkane CnH2n+2 | Alkene CnH2n | Alkyne CnH2n-2"
        ),
        ScienceSimulation(
            id = "sim_bio_heart",
            chapterId = "bio_life_processes",
            category = SimulationCategory.BIOLOGY,
            titleEn = "Human Heart & Double Circulation Explorer",
            titleKn = "ಮಾನವನ ಹೃದಯ ಮತ್ತು ದ್ವಿ-ಪರಿಚಲನೆ ಮಾದರಿ",
            summaryEn = "Tap heart chambers and vessels or animate blood flow steps to trace deoxygenated (blue) and oxygenated (red) blood circuits.",
            summaryKn = "ಹೃದಯದ ಕೋಣೆಗಳು ಮತ್ತು ರಕ್ತನಾಳಗಳನ್ನು ಆಯ್ಕೆ ಮಾಡಿ ಆಮ್ಲಜನಕರಹಿತ ಮತ್ತು ಆಮ್ಲಜನಕಸಹಿತ ರಕ್ತದ ಹರಿವನ್ನು ಗಮನಿಸಿ.",
            equationOrLaw = "Pulmonary Circuit (Right Ventricle -> Lungs -> Left Atrium) + Systemic Circuit"
        ),
        ScienceSimulation(
            id = "sim_bio_nephron",
            chapterId = "bio_life_processes",
            category = SimulationCategory.BIOLOGY,
            titleEn = "Nephron Filtration & Reabsorption Model",
            titleKn = "ನೆಫ್ರಾನ್ ಶೋಧನೆ ಮತ್ತು ಮರುಹೀರಿಕೆ ಮಾದರಿ",
            summaryEn = "Step through Ultrafiltration, Selective Reabsorption of glucose/water, and Urine Collection in the kidney nephron.",
            summaryKn = "ಬೌಮನ್ ಕೋಶ, ಗ್ಲಾಮರುಲಸ್ ಮತ್ತು ಸಂಗ್ರಾಹಕ ನಾಳದ ಮೂಲಕ ಮೂತ್ರ ನಿರ್ಮಾಣ ಹಂತಗಳನ್ನು ವೀಕ್ಷಿಸಿ.",
            equationOrLaw = "Glomerular Filtration -> Selective Reabsorption -> Tubular Secretion"
        ),
        ScienceSimulation(
            id = "sim_bio_reflex",
            chapterId = "bio_control_coordination",
            category = SimulationCategory.BIOLOGY,
            titleEn = "Reflex Arc & Synapse Signal Path",
            titleKn = "ಪರಾವರ್ತಿತ ಚಾಪ ಮತ್ತು ನರ ಆವೇಗ ಮಾರ್ಗ",
            summaryEn = "Trigger a thermal stimulus and trace the rapid impulse from Receptor -> Sensory Neuron -> Spinal Cord -> Motor Neuron -> Effector.",
            summaryKn = "ಉಷ್ಣ ಪ್ರಚೋದನೆಗೆ ಗ್ರಾಹಕದಿಂದ ಬೆನ್ನುಹುರಿ ಮೂಲಕ ಸ್ನಾಯುವಿಗೆ ಸಾಗುವ ಪರಾವರ್ತಿತ ಕ್ರಿಯೆಯನ್ನು ವೀಕ್ಷಿಸಿ.",
            equationOrLaw = "Receptor -> Sensory Neuron -> Spinal Cord -> Motor Neuron -> Effector"
        ),
        ScienceSimulation(
            id = "sim_bio_ecosystem",
            chapterId = "bio_environment",
            category = SimulationCategory.BIOLOGY,
            titleEn = "10% Energy Pyramid & Biomagnification Lab",
            titleKn = "10% ಶಕ್ತಿ ಪಿರಮಿಡ್ ಮತ್ತು ಜೈವಿಕ ವರ್ಧನೆ",
            summaryEn = "Adjust solar input and producer energy to compute available Joules and toxic pesticide accumulation across 4 trophic levels.",
            summaryKn = "ಉತ್ಪಾದಕರ ಶಕ್ತಿಯನ್ನು ಬದಲಾಯಿಸಿ ಪ್ರತಿ ಪೋಷಣಾ ಸ್ತರದಲ್ಲಿ ಲಭ್ಯವಿರುವ 10% ಶಕ್ತಿಯನ್ನು ಲೆಕ್ಕಹಾಕಿ.",
            equationOrLaw = "E_(n+1) = 0.10 × E_n  (Unidirectional Energy Flow)"
        )
    )

    val diagrams: List<ScienceDiagram> = listOf(
        ScienceDiagram(
            id = "diag_human_heart",
            chapterId = "bio_life_processes",
            division = ScienceDivision.BIOLOGY,
            titleEn = "Sectional View of Human Heart",
            titleKn = "ಮಾನವನ ಹೃದಯದ ಛೇದನ ನೋಟ",
            drawingSteps = listOf(
                "1. Draw a slightly tilted pear-shaped muscular outline divided vertically by a thick septum.",
                "2. Sketch upper Left & Right Atria (thin walls) and lower Left & Right Ventricles (thick left wall).",
                "3. Add Superior/Inferior Vena Cava entering Right Atrium and Pulmonary Artery leaving Right Ventricle.",
                "4. Draw Pulmonary Veins entering Left Atrium and Aorta arching from Left Ventricle with valves."
            ),
            examTip = "Frequently asked 4-mark KSEAB question! Always show thicker muscular wall on the Left Ventricle.",
            hotspots = listOf(
                DiagramHotspot("aorta", "Aorta", "ಮಹಾಧಮನಿ", "Carries oxygenated blood from left ventricle to the entire body.", 0.54f, 0.18f),
                DiagramHotspot("pulm_art", "Pulmonary Artery", "ಶ್ವಾಸಕೋಶದ ಧಮನಿ", "Carries deoxygenated blood from right ventricle to lungs.", 0.36f, 0.24f),
                DiagramHotspot("right_atrium", "Right Atrium", "ಬಲ ಹೃತ್ಕರ್ಣ", "Receives deoxygenated blood from body via vena cava.", 0.28f, 0.42f),
                DiagramHotspot("left_ventricle", "Left Ventricle", "ಎಡ ಹೃತ್ಕುಕ್ಷಿ", "Thick-walled chamber pumping oxygenated blood into aorta.", 0.66f, 0.68f),
                DiagramHotspot("septum", "Septum", "ಹೃದಯ ಭಿತ್ತಿ", "Dividing wall preventing mixing of oxygenated and deoxygenated blood.", 0.49f, 0.65f)
            )
        ),
        ScienceDiagram(
            id = "diag_nephron",
            chapterId = "bio_life_processes",
            division = ScienceDivision.BIOLOGY,
            titleEn = "Structure of a Nephron",
            titleKn = "ನೆಫ್ರಾನ್‌ನ ರಚನೆ",
            drawingSteps = listOf(
                "1. Draw a cup-shaped Bowman's capsule enclosing a coiled capillary tuft (Glomerulus).",
                "2. Extend a coiled proximal tubule, a U-shaped Henle's loop, and a distal tubule.",
                "3. Connect the distal end into a vertical Collecting Duct."
            ),
            examTip = "Label Bowman's capsule, Glomerulus, Tubular part, and Collecting duct clearly.",
            hotspots = listOf(
                DiagramHotspot("bowman", "Bowman's Capsule", "ಬೌಮನ್ ಕೋಶ", "Cup-shaped funnel collecting glomerular filtrate.", 0.25f, 0.32f),
                DiagramHotspot("glomerulus", "Glomerulus", "ಗ್ಲಾಮರುಲಸ್", "Cluster of capillaries performing ultrafiltration under pressure.", 0.25f, 0.24f),
                DiagramHotspot("tubule", "Tubular Part", "ನಾಳೀಯ ಭಾಗ", "Selectively reabsorbs glucose, amino acids, salts, and water.", 0.52f, 0.62f),
                DiagramHotspot("collecting", "Collecting Duct", "ಸಂಗ್ರಾಹಕ ನಾಳ", "Collects concentrated urine towards ureter.", 0.80f, 0.52f)
            )
        ),
        ScienceDiagram(
            id = "diag_neuron",
            chapterId = "bio_control_coordination",
            division = ScienceDivision.BIOLOGY,
            titleEn = "Structure of a Neuron",
            titleKn = "ನರಕೋಶದ ರಚನೆ",
            drawingSteps = listOf(
                "1. Draw a star-shaped Cell Body (Cyton) with central Nucleus and branching Dendrites.",
                "2. Extend a long Axon covered by segmented Myelin sheath.",
                "3. End with branched Nerve Endings (Axon terminals)."
            ),
            examTip = "Remember impulse flow direction: Dendrite -> Cell Body -> Axon -> Nerve Ending -> Synapse.",
            hotspots = listOf(
                DiagramHotspot("dendrite", "Dendrite", "ಡೆಂಡ್ರೈಟ್", "Receives environmental/neural stimulus and initiates electrical impulse.", 0.16f, 0.30f),
                DiagramHotspot("cell_body", "Cell Body (Cyton)", "ಕೋಶಕಾಯ", "Contains nucleus and cytoplasm; integrates incoming signals.", 0.30f, 0.48f),
                DiagramHotspot("axon", "Axon", "ಆಕ್ಸಾನ್", "Long fibre conducting electrical impulse away from cell body.", 0.60f, 0.50f),
                DiagramHotspot("nerve_end", "Nerve Ending", "ನರ ತುದಿ", "Releases chemical neurotransmitters across synapse to next neuron.", 0.86f, 0.50f)
            )
        ),
        ScienceDiagram(
            id = "diag_human_brain",
            chapterId = "bio_control_coordination",
            division = ScienceDivision.BIOLOGY,
            titleEn = "Human Brain (Sagittal Section)",
            titleKn = "ಮಾನವನ ಮೆದುಳಿನ ರಚನೆ",
            drawingSteps = listOf(
                "1. Sketch the folded outer Cerebrum dome enclosed by Cranium.",
                "2. Draw Midbrain and central Pituitary/Hypothalamus below.",
                "3. Add leaf-like Cerebellum at rear bottom and Pons + Medulla leading to Spinal Cord."
            ),
            examTip = "Very common 3–4M KSEAB diagram. Always distinguish Cerebrum (thinking) vs Cerebellum (balance) vs Medulla (involuntary).",
            hotspots = listOf(
                DiagramHotspot("cerebrum", "Cerebrum", "ಮಹಾಮಸ್ತಿಷ್ಕ", "Main thinking part; handles memory, reasoning, and voluntary sensory centres.", 0.45f, 0.25f),
                DiagramHotspot("cerebellum", "Cerebellum", "ಅನುಮಸ್ತಿಷ್ಕ", "Coordinates posture, balance, and precision of voluntary movements.", 0.68f, 0.64f),
                DiagramHotspot("medulla", "Medulla", "ಮೆಡುಲ್ಲಾ", "Controls involuntary actions like blood pressure, salivation, and vomiting.", 0.54f, 0.78f),
                DiagramHotspot("spinal", "Spinal Cord", "ಮೆದುಳುಬಳ್ಳಿ", "Relays messages between brain and body; coordinates reflex actions.", 0.52f, 0.90f)
            )
        ),
        ScienceDiagram(
            id = "diag_electrolysis_water",
            chapterId = "chem_reactions",
            division = ScienceDivision.CHEMISTRY,
            titleEn = "Electrolysis of Water Apparatus",
            titleKn = "ನೀರಿನ ವಿದ್ಯುದ್ವಿಭಜನೆ ಉಪಕರಣ",
            drawingSteps = listOf(
                "1. Draw a plastic mug with two carbon electrodes connected to a 6V battery.",
                "2. Invert two water-filled graduated test tubes over Cathode and Anode.",
                "3. Show gas height in Cathode tube (H2) is double the height in Anode tube (O2)."
            ),
            examTip = "Show H2 volume = 2 × O2 volume clearly on the diagram!",
            hotspots = listOf(
                DiagramHotspot("cathode", "Cathode (-)", "ಕ್ಯಾಥೋಡ್ (-)", "Negative electrode where Hydrogen gas (2 volumes) is liberated.", 0.34f, 0.76f),
                DiagramHotspot("anode", "Anode (+)", "ಆನೋಡ್ (+)", "Positive electrode where Oxygen gas (1 volume) is liberated.", 0.66f, 0.76f),
                DiagramHotspot("h2_gas", "Hydrogen Gas (2V)", "ಹೈಡ್ರೋಜನ್ ಅನಿಲ", "Burns with a pop sound; double the volume of oxygen.", 0.34f, 0.28f),
                DiagramHotspot("o2_gas", "Oxygen Gas (1V)", "ಆಕ್ಸಿಜನ್ ಅನಿಲ", "Rekindles a glowing splinter.", 0.66f, 0.36f)
            )
        ),
        ScienceDiagram(
            id = "diag_ohms_circuit",
            chapterId = "phy_electricity",
            division = ScienceDivision.PHYSICS,
            titleEn = "Ohm's Law Verification Circuit",
            titleKn = "ಓಮ್‌ನ ನಿಯಮ ಪರಿಶೀಲನಾ ವಿದ್ಯುತ್ ಮಂಡಲ",
            drawingSteps = listOf(
                "1. Draw Battery, Plug Key, Ammeter (in series), and Nichrome Resistor XY.",
                "2. Connect Voltmeter strictly in parallel across resistor XY.",
                "3. Mark + and - terminals of Battery, Ammeter, and Voltmeter consistently."
            ),
            examTip = "Ammeter is ALWAYS in series; Voltmeter is ALWAYS in parallel across resistor XY.",
            hotspots = listOf(
                DiagramHotspot("ammeter", "Ammeter (Series)", "ಆಮ್ಮೀಟರ್ (ಸರಣಿ)", "Low-resistance meter connected in series to measure current I (A).", 0.76f, 0.32f),
                DiagramHotspot("voltmeter", "Voltmeter (Parallel)", "ವೋಲ್ಟ್‌ಮೀಟರ್ (ಸಮಾಂತರ)", "High-resistance meter connected in parallel across XY to measure V.", 0.50f, 0.24f),
                DiagramHotspot("resistor", "Nichrome Wire XY", "ನೈಕ್ರೋಮ್ ರೋಧಕ XY", "Metallic conductor obeying V = IR at constant temperature.", 0.50f, 0.48f),
                DiagramHotspot("battery", "Battery & Key", "ಬ್ಯಾಟರಿ ಮತ್ತು ಸ್ವಿಚ್", "Source of EMF and switch completing the circuit.", 0.50f, 0.78f)
            )
        )
    )
}
