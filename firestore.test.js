const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_sslc_10";
const BOB_UID = "bob_sslc_10";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated users cannot read or write any user profile or progress", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  const now = new Date(Date.now() - 1000);
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      preferredMedium: "ENGLISH",
      selectedSubject: "ALL",
      selectedChapterId: "chem_1",
      dailyTargetMinutes: 45,
      streakDays: 1,
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("chapter_progress").get()
  );
});

test("Owner can create and partially update UserProfile; shadow fields & cross-user access fail", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const created = new Date(Date.now() - 10000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Aarav K",
      preferredMedium: "BILINGUAL",
      selectedSubject: "PHYSICS",
      selectedChapterId: "phy_light",
      dailyTargetMinutes: 45,
      streakDays: 3,
      createdAt: created,
      updatedAt: created,
    })
  );

  // Partial update allowed fields
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).update({
      selectedChapterId: "phy_electricity",
      dailyTargetMinutes: 60,
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Reject ghost/shadow field
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      isAdmin: true,
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Reject immutable createdAt modification
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      createdAt: new Date(Date.now() - 500),
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Reject future timestamp
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      displayName: "Bob",
      preferredMedium: "ENGLISH",
      selectedSubject: "ALL",
      selectedChapterId: "chem_1",
      dailyTargetMinutes: 30,
      streakDays: 0,
      createdAt: new Date(Date.now() + 60000),
      updatedAt: new Date(Date.now() + 60000),
    })
  );

  // Cross-user read & list denied
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(aliceDb.collection("users").get());
});

test("ChapterProgress: owner create, partial update, list, and cross-user denial", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const created = new Date(Date.now() - 5000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("chapter_progress").doc("phy_electricity").set({
      userId: ALICE_UID,
      chapterId: "phy_electricity",
      subject: "PHYSICS",
      completedTopicIds: ["ohms_law", "resistance"],
      masteryPercent: 50,
      notesBookmarked: true,
      lastOpenedTopicId: "ohms_law",
      createdAt: created,
      updatedAt: created,
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("chapter_progress").get()
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("chapter_progress").doc("phy_electricity").update({
      completedTopicIds: ["ohms_law", "resistance", "electric_power"],
      masteryPercent: 75,
      lastRevisedAt: new Date(Date.now() - 1000),
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Reject @DocumentId "id" key injection
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("chapter_progress").doc("chem_acids").set({
      id: "chem_acids",
      userId: ALICE_UID,
      chapterId: "chem_acids",
      subject: "CHEMISTRY",
      completedTopicIds: [],
      masteryPercent: 0,
      notesBookmarked: false,
      lastOpenedTopicId: "",
      createdAt: created,
      updatedAt: created,
    })
  );

  // Cross-user isolation
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("chapter_progress").get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("chapter_progress").doc("phy_electricity").get()
  );
});

test("ExamAttempts, SavedNotes, and AiConversations enforce schema and user isolation", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const now = new Date(Date.now() - 2000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("exam_attempts").doc("attempt_1").set({
      userId: ALICE_UID,
      chapterId: "chem_1",
      examType: "CHAPTER_TEST",
      subject: "CHEMISTRY",
      score: 18,
      maxScore: 20,
      weakTopics: ["redox_reactions"],
      feedbackSummary: "Strong equation balancing; review oxidizing vs reducing agent definitions.",
      createdAt: now,
      updatedAt: now,
    })
  );

  // Exam attempts are immutable once created
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("exam_attempts").doc("attempt_1").update({
      score: 20,
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("saved_notes").doc("note_1").set({
      userId: ALICE_UID,
      chapterId: "bio_life_processes",
      title: "Double Circulation in Human Heart",
      content: "Blood passes twice through the heart in one complete cycle.",
      sourceType: "OFFICIAL_TEXTBOOK",
      medium: "ENGLISH",
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("ai_conversations").doc("msg_1").set({
      userId: ALICE_UID,
      chapterId: "phy_electricity",
      question: "Current andre enu?",
      answer: "Electric current (ವಿದ್ಯುತ್ ಪ್ರವಾಹ) is the rate of flow of electric charge through a conductor: I = Q / t (Unit: Ampere).",
      teachingMode: "Simple Explanation",
      language: "BILINGUAL",
      sourceCitation: "KSEAB SSLC Science Part 2, Chapter 11: Electricity",
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("saved_notes").get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("ai_conversations").get()
  );
});
