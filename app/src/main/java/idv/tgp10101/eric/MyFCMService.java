package idv.tgp10101.eric;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFCMService extends FirebaseMessagingService {
    private static final String TAG = "TAG_MyFCMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String title = "";
        String body = "";
        if (notification != null) {
            title = notification.getTitle();
            body = notification.getBody();
        }
        // 取得自訂資料
        Map<String, String> map = remoteMessage.getData();
        String data = map.get("data");
        Log.d(TAG, "onMessageReceived():\ntitle: " + title + ", body: " + body + ", data: " + data);
    }

    @Override
    // 當registration token更新時呼叫，應該將新的token傳送至server
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "onNewToken: " + token);
//        RemoteAccess.sendTokenToServer(token, this);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeletedMessages");
    }
}
