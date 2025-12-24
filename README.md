# oneChat — Simple & Fast One‑to‑One Chat 🚀

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-orange)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-modern-blue)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-auth%2Fstorage%2FFCM-yellow)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey)](./LICENSE)

Short intro
-----------
oneChat is a lightweight, production-ready Android app for secure one-to-one messaging. Built with Kotlin + Jetpack Compose, DI using Koin, and Firebase for Auth, Storage and FCM. The app supports Light & Dark themes, image attachments, and push notifications via a small Node.js notifier or Firebase Cloud Functions. No group chat, no calling — one-to-one only.

Key features
------------
- One-to-one text and image messages
- Light mode and Dark mode (system + manual toggle)
- Firebase Authentication (email / Google / phone)
- Firebase Storage for media
- Push notifications with Firebase Cloud Messaging (notifier in Node.js or Cloud Function)
- Koin for dependency injection
- Kotlin Coroutines + modern Android architecture
- UI fully implemented in Jetpack Compose

Theme (Light & Dark)
--------------------
- App respects system theme by default.
- Manual toggle available in Profile → Theme to switch Light / Dark.
- Implementation: Compose `MaterialTheme` with two color palettes and saved user preference (DataStore / SharedPreferences).
- Images and icons adapt (tinted appropriately) and message bubble colors switch to maintain contrast and accessibility.

Tech stack (concise)
--------------------
- Android: Kotlin, Jetpack Compose, AndroidX (ViewModel, Navigation, Lifecycle)
- DI: Koin
- Concurrency: Kotlin Coroutines + Flow
- Backend & Services: Firebase Auth, Firestore (or Realtime DB), Storage, Cloud Messaging (FCM)
- Notifier: Node.js small script or Firebase Cloud Functions
- Build: Gradle

What I used (explicit)
----------------------
- Kotlin + Jetpack Compose (UI)
- Koin (dependency injection)
- Firebase Authentication (email / Google / phone)
- Firebase Firestore (messages & profiles) OR Realtime DB (configurable)
- Firebase Storage (image/media uploads)
- Firebase Cloud Messaging (notifications)
- Node.js notifier (simple JS script included in repo) — optional: deploy as Cloud Function
- DataStore (or SharedPreferences) for storing theme preference, auth related stuff

Notifier (example)
------------------
A tiny Node.js script can send FCM when a message is created (deployable or run server-side). Example (use service account or server key in production):

```javascript
// notifier/sendNotification.js (example)
const fetch = require('node-fetch');
const FCM_SERVER_KEY = process.env.FCM_SERVER_KEY;

async function sendNotification(token, title, body, data = {}) {
  const res = await fetch('https://fcm.googleapis.com/fcm/send', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `key=${FCM_SERVER_KEY}`,
    },
    body: JSON.stringify({
      to: token,
      notification: { title, body },
      data,
    }),
  });
  return res.json();
}
module.exports = { sendNotification };
```

Use a server-side trigger (DB write or Cloud Function) to call this — do not embed server keys in the client.

Quick setup
-----------
1. Create Firebase project → enable Authentication, Firestore/Realtime DB, Storage, and Cloud Messaging.  
2. Download `google-services.json` and put it in `app/`.  
3. Configure Firebase rules (see example below).  
4. (Optional) Deploy notifier as Cloud Function or run Node.js notifier with safe secrets (env vars).  
5. Open project in Android Studio → Gradle sync → Run on device.

Recommended Firebase (Firestore) rules (example)
------------------------------------------------
Use these as a starting point — tighten per your model.

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    match /messages/{messageId} {
      allow create: if request.auth != null && request.resource.data.senderId == request.auth.uid;
      allow read: if request.auth != null && (
        resource.data.senderId == request.auth.uid || resource.data.receiverId == request.auth.uid
      );
      allow update, delete: if false;
    }
  }
}
```

Composer / UI notes
-------------------
- Composer is rounded with attachment, camera and mic icons (see Image 4).  
- Messages show timestamp and read receipts (double ticks).  
- Use Coil or image loader for avatars with placeholders.

Limitations
-----------
- One-to-one chats only (no groups).  
- No voice/video calls (no Agora/WebRTC).  
- Notification reliability depends on FCM + notifier setup.  
- Ensure production Firebase rules and secret management are applied.

Security & privacy
------------------
- Never commit `google-services.json` or server keys to a public repo.  
- Use Firebase Cloud Functions + service account to send FCM for production.  
- Secure Storage and Database rules to allow only authorized access to user data.

Contributing
------------
- Fork → feature branch → PR with description and screenshots.  
- Keep commits focused and small. Add tests where possible.

Contact
-------
Author: 7078mohit  
Repo: https://github.com/7078mohit/oneChat
mail: 7078mohit@gmail.com
