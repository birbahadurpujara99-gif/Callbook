const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const ADMIN_UID = "bir_admin_uid";
const ADMIN_EMAIL = "birbahadurpujara99@gmail.com";

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

test("Unauthenticated user: cannot read posts or users", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("posts").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: can create own profile but CANNOT self-verify", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@example.com" }).firestore();
  const now = new Date();
  
  // Attempting to self-verify during creation should FAIL
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      name: "Alice Sharma",
      email: "alice@example.com",
      phone: "+9779812345678",
      isVerified: true,
      createdAt: now,
      updatedAt: now,
    })
  );

  // Creating normal unverified profile SUCCEEDS
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      name: "Alice Sharma",
      email: "alice@example.com",
      phone: "+9779812345678",
      isVerified: false,
      role: "user",
      createdAt: now,
      updatedAt: now,
    })
  );

  // Alice trying to update her own isVerified should FAIL
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      isVerified: true,
      updatedAt: new Date(),
    })
  );
});

test("Admin Bir Pujara: CAN approve verification for users", async () => {
  // Seed Alice profile as unverified
  const now = new Date();
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      name: "Alice Sharma",
      email: "alice@example.com",
      phone: "+9779812345678",
      isVerified: false,
      role: "user",
      createdAt: now,
      updatedAt: now,
    });
  });

  // Admin Bir Pujara updates Alice's verification status
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, { email: ADMIN_EMAIL }).firestore();
  await assertSucceeds(
    adminDb.collection("users").doc(ALICE_UID).update({
      isVerified: true,
      updatedAt: new Date(),
    })
  );
});

test("Post creation and feed query with badges", async () => {
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, { email: ADMIN_EMAIL }).firestore();
  const now = new Date();
  const postRef = adminDb.collection("posts").doc("post_admin");
  await assertSucceeds(
    postRef.set({
      authorId: ADMIN_UID,
      authorName: "Bir Pujara",
      authorRole: "admin",
      authorIsVerified: true,
      text: "Welcome to CALLBOOK! Enjoy the social platform.",
      visibility: "public",
      likesCount: 0,
      commentsCount: 0,
      createdAt: now,
      updatedAt: now,
    })
  );

  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@example.com" }).firestore();
  await assertSucceeds(
    aliceDb.collection("posts").where("visibility", "==", "public").get()
  );
});
