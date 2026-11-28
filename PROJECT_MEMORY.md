# SSLC AI Learning Projects — Project Memory & Handover

_Last updated: 28 November 2026_

This document preserves the project context available from the ChatGPT development conversations. It is a practical handover, not a complete export of every historical chat or every source-code change. Always inspect the current repository before editing; the notes below describe the latest known state from the conversation.

## 1. Project owner / developer branding

- Developer / brand credit: **SVANAM SOFTS** (the user has also typed “Savnam Softs”; use the on-screen brand spelling **SVANAM SOFTS** unless the user requests a correction).
- Primary audience: Karnataka SSLC / Class 10 students.
- Curriculum context: Karnataka State syllabus, with 2026–27 textbook references discussed.
- User's preferred working style: make real repository changes where connected tools permit; do not claim a build, install, test, or deployment succeeded unless its status has actually been checked. Give practical Windows PowerShell commands when needed.
- The user wants genuine screenshots from the apps used in promotional material, not illustrative mockups.

## 2. Main GitHub repositories

1. Maths app: https://github.com/aisree12/SSLC-MATHS-AI-Karnataka
2. Science app: https://github.com/aisree12/SSLC-SCIENCE-AI-TEACHER
3. Promotional website: https://github.com/aisree12/SSLC-Smart-Learning-Promo-Website

Use the repository's current default branch and latest file SHA when committing edits. Fetch a file before updating it. Do not overwrite concurrent changes.

## 3. Promotional website — latest known state

Repository: https://github.com/aisree12/SSLC-Smart-Learning-Promo-Website

### Real app screenshot gallery
The website gallery was updated to use real app screenshots already uploaded to the repository, rather than illustrative mockups. The `index.html` references these root-level image filenames:

**Maths**
- `03_study-center.png`
- `08_video1.png`
- `10_ai-teacher1.png`
- `12_pyq.png`

**Science**
- `19_study-center.png`
- `26_ai-teacher.png`
- `27_pyqs.png`
- `29_lab1.png`
- `31_digrams.png`

The gallery includes captions and click-to-open full-size images. The code references were checked in the fetched HTML; this is not proof that every image was independently browser-tested in the live deployment.

### SVANAM SOFTS contact form and WhatsApp CTA
The website was updated with:
- SVANAM SOFTS app/school enquiry section.
- Contact form fields: Name (required), School/organisation (optional), Enquiry (required).
- Form submit opens WhatsApp with a pre-filled enquiry message.
- Contact number displayed as **+91 63609 53674**.
- WhatsApp destination: https://wa.me/916360953674
- Large fixed floating WhatsApp CTA at the bottom-right, visible throughout the page, with multicolour animated gradient and pulse effect.
- Mobile-responsive sizing and reduced-motion accessibility handling.

Latest known website commit:
- `a3eabab59e23f0479ed190e5dd15b21112671a4d`
- `index.html` blob SHA at that point: `c5b947c2be691dc708121a928bfbb6454ba658fd`
- Commit URL: https://github.com/aisree12/SSLC-Smart-Learning-Promo-Website/commit/a3eabab59e23f0479ed190e5dd15b21112671a4d
- File URL: https://github.com/aisree12/SSLC-Smart-Learning-Promo-Website/blob/main/index.html

### Cloudflare Pages notes
Previously discussed static-site configuration:
- Production branch: `main`
- Framework preset: None
- Build command: blank / no build step
- Build output directory: repository root (`/`)
- Root directory: `/`
- Project name suggestion: `sslc-smart-learning`
- Dashboard: https://dash.cloudflare.com/7b4a7eeb2830177ce9720a102e20dc2c/workers-and-pages

Deployment was not independently verified. If Cloudflare Pages is connected to the `main` branch, a push may trigger deployment, but check the Cloudflare deployment status and live site before saying it is published.

### Website items still to verify / complete
- Verify live desktop and mobile layout, including the fixed WhatsApp pill not covering important content or controls.
- Submit the contact form and verify the WhatsApp message is correctly prefilled on mobile and desktop.
- Confirm all nine screenshot images load on the live site.
- APK download buttons were not yet confirmed to point to published, verified APK release files. Add real release URLs only after APKs are published.
- README had deployment and screenshot notes; update it when website behavior changes.

## 4. Science Android app — known requirements and status

Repository: https://github.com/aisree12/SSLC-SCIENCE-AI-TEACHER

Known package name:
`com.aistudio.sslcscienceai.kaxrmp`

### User requirements / design direction
- Adapt the polished startup / cinematic intro pattern from the Maths app, with Science-specific title/content and SVANAM SOFTS developer credit.
- Keep language selection strict: English mode should show English content only; Kannada mode should show Kannada content only.
- Support Karnataka 2026–27 official Class 10 Science textbooks, English and Kannada Part 1 and Part 2 PDFs.
- Download only the selected medium's required textbooks; link relevant chapters/topics to the correct textbook content.
- Avoid cluttering the Study Centre with a visible textbook catalogue if textbook access can be handled contextually.
- Make textbook downloads and offline opening functional.
- Study Centre main actions (Topic, Notes, Formula, Videos) should be colourful, bold, compact, clearly arranged, and visually responsive/animated.
- Reorganise PYQ section controls: compact horizontal arrangement where appropriate, readable labels, and clear separation of practice-source/pending items.
- Provide genuine previous-year question content (2015 onward where available); do not label a mere external link as transcribed question content.
- Reaction balancing lab should allow manual coefficient entry, rearranging/reordering reactants and products, tap controls, and live atom counts. Earlier notes say a version had coefficient and atom-count behavior but not full formula editing/reordering.
- Labs and diagrams must display actual scientific diagrams/simulations, not empty placeholders or text-only labels.
- Notes download should work.
- Remove/hide a prominent syllabus-delete shortcut.
- Internal links should have clear tap/selection feedback.

### Most recent known UI/build change
A prior UI pass modified:
- `PyqCentreScreen.kt`
- `ChapterWorkspacePanel.kt`
- `StudyCentreScreen.kt`

The pass made buttons/chips smaller, colourful, bold, and added selected-tab accent animation. A compile issue involving `OutlinedButtonDefaults` was corrected by using `ButtonDefaults.outlinedButtonColors`.

Known commit:
- `80af45c1a53ccbda90ba8e8be5eef73392f2ad6c`

Known GitHub Actions workflow run:
- Run ID: `46048730148`
- URL: https://github.com/aisree12/SSLC-SCIENCE-AI-TEACHER/actions/runs/46048730148
- At the last known check it was queued; its final result is unknown. Check Actions before treating it as successful.
- An earlier failed workflow run was `46048431561`.

Do not assume this is still the latest repository commit or that the build passed. Inspect current GitHub state first.

## 5. Maths Android app — project context

Repository: https://github.com/aisree12/SSLC-MATHS-AI-Karnataka

The Maths app has been the source/reference project for some of the Science app's UI and startup patterns. The user has iterated extensively on its layout and features, and later asked to carry the overall project decisions into the Science app.

Known user expectations discussed across Maths app work:
- Karnataka SSLC Class 10 syllabus alignment.
- Professional, student-friendly UI with colourful bold main buttons and clear horizontal/compact layouts where appropriate.
- Study Centre organisation and improved Topic, Notes, Formula, Videos navigation.
- PYQ section organisation and practice-source clarity.
- AI teacher / learning support, chapter/topic content, videos and textbook references.
- Preserve working functionality when redesigning; avoid regressions and verify builds.

The exact latest Maths app commit/build status is not captured in this handover. Check the repository and Actions history before continuing.

## 6. Windows Android build and device-install notes

Previously identified Android SDK ADB executable:
`C:\Users\Administrator\AppData\Local\Android\Sdk\platform-tools\adb.exe`

If PowerShell says `adb` is not recognized, use the full path:

```powershell
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
& $adb devices
& $adb install -r "C:\Users\Administrator\Desktop\app-debug.apk"
```

A previously connected/authorised device was listed as:
`05981a570405 device`

That device state may have changed; run `adb devices` again before installing. Check the APK exists before installation.

## 7. Working rules for future development

1. Fetch current files and inspect the latest commit before editing.
2. Preserve user-provided screenshots and actual app content; do not replace them with invented mockups.
3. Make changes in the correct repository and explain exactly which files changed.
4. Run or inspect the relevant GitHub Actions workflow after code changes. Report the exact outcome (success, failure, queued, cancelled).
5. Do not claim local installation or physical-device testing unless it actually happened.
6. Do not claim Cloudflare deployment/live-site verification unless it was checked.
7. Keep educational content accurate for Karnataka SSLC Class 10 and separate English/Kannada content correctly.
8. Keep real download URLs and assets; never invent APK links or textbook URLs.
9. Update this `PROJECT_MEMORY.md` after substantial decisions, commits, build fixes, deployments, or new requirements.
10. Treat this file as a handover summary, not a substitute for checking current source code and the complete chat history.

## 8. Immediate next actions

- Verify the latest Science Actions run (`46048730148`) and inspect current repository state.
- Check the current Maths app build status and latest implementation before making more changes.
- Verify the promotional site live deployment and mobile behavior, including WhatsApp form and screenshot gallery.
- Publish verified APK releases and connect website download buttons only after release files are available.
- Continue updating this memory file in all three repositories whenever major project work is completed.
