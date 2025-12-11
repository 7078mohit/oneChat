const { onValueCreated } = require("firebase-functions/v2/database");
const { initializeApp } = require("firebase-admin/app");
const { getDatabase } = require("firebase-admin/database");
const { getMessaging } = require("firebase-admin/messaging");

initializeApp();

exports.sendNotificationOnMessage = onValueCreated(
  "/messages/{chatId}/{messageId}",
  async (event) => {
    const message = event.data.val();
    if (!message) return null;

    const { receiverId, senderId, content, messageType, isRead, isPopUpShowed } = message;
    if (!receiverId || !senderId) return null;

    const db = getDatabase();

    // 1) Already shown → skip
    if (isPopUpShowed === true) {
      console.log("Popup already shown → skipping");
      return null;
    }

    // 2) If message read → no popup
    if (isRead === true) {
      console.log("Message already read → skipping popup");
      return null;
    }

    // 3) Fetch receiver
    const userSnap = await db.ref(`/User/${receiverId}`).get();
    const user = userSnap.val();
    if (!user) return null;

    // 4) User Online? Then skip everything
    const isOnline = user.isOnline === true;
    if (isOnline) {
      console.log("User online → no popup, no notification");
      return null;
    }

    // 5) User Offline → Notification + Popup flag
    const fcmToken = user.fcmToken;
    if (!fcmToken) return null;

    // Fetch sender
    const senderSnap = await db.ref(`/User/${senderId}`).get();
    const sender = senderSnap.val();
    const senderName = sender?.name || "New message";
    const senderImage = sender?.profile || "";

    let bodyText = content || "";
    if (!bodyText) {
      switch (messageType) {
        case "IMAGE": bodyText = "📷 Sent a photo"; break;
        case "AUDIO": bodyText = "🎧 Sent an audio"; break;
        case "VIDEO": bodyText = "🎥 Sent a video"; break;
        case "DOCUMENT": bodyText = "📄 Sent a document"; break;
        default: bodyText = "New message received";
      }
    }

    const payload = {
      token: fcmToken,
      android: { priority: "high" },
      data: {
        title: senderName,
        body: bodyText,
        chatId: event.params.chatId,
        senderId,
        receiverId,
        messageId: event.params.messageId,
        profile: senderImage,
      },
      notification: {
        title: senderName,
        body: bodyText,
      },
    };

    try {
      await getMessaging().send(payload);
      console.log("Notification sent");

      // Mark popup TRUE
      await db
        .ref(`/messages/${event.params.chatId}/${event.params.messageId}/isPopUpShowed`)
        .set(true);

      console.log(`Popup marked TRUE for msg → ${event.params.messageId}`);
    } catch (e) {
      console.error("Error sending notif:", e);
    }

    return null;
  }
);
