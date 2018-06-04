package com.bevywise.bevywiseplatform_connector.notification;

import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

class BevywiseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Toast.makeText(getApplicationContext(),"Recvd message",Toast.LENGTH_LONG).show();
    }
}
