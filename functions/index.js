const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.addTimeStamp = functions.firestore
   .document('chats/{chatId}/messages/{messageId}')
   .onCreate((snap, context) => {
     if (snap) {
       return snap.ref.update({
                   timestamp: admin.firestore.FieldValue.serverTimestamp()
               });
     }

     return "snap was null or empty";
   });


exports.sendChatNotifications = functions.firestore
   .document('chats/{chatId}/messages/{messageId}')
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.from + ' sent you a message',
           body: document.text
         },
         //topic: context.params.chatId
         //data.<"mtype"> = document.mtype
         data: {
            mtype: document.mtype,
            mfrom: document.from,
            mteam: document.mteam
         }
       };

       return admin.messaging().sendToDevice(document.token, message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully sent message:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error sending message:', error);
           return error;
         });
     }

     return "document was null or emtpy";
   });


exports.sendProposeWalkNotifications = functions.firestore
      .document('notifications/{chatId}/team_walk/{messageId}')
      .onCreate((snap, context) => {
        // Get an object with the current document value.
        // If the document does not exist, it has been deleted.
        const document = snap.exists ? snap.data() : null;

        if (document) {
          var message = {
            notification: {
              title: document.from + ' proposed a walk',
              body: document.text
            },
         topic: context.params.chatId,
         data: {
            mtype: document.mtype,
            mfrom: document.from,
            mteam: document.mteam
         }
          };

          return admin.messaging().send(message)
            .then((response) => {
              // Response is a message ID string.
              console.log('Successfully sent message:', response);
              return response;
            })
            .catch((error) => {
              console.log('Error sending message:', error);
              return error;
            });
        }

        return "document was null or emtpy";
      });

