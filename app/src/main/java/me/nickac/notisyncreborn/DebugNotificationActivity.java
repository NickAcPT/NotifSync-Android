package me.nickac.notisyncreborn;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;

import java.util.Random;

import me.nickac.notisyncreborn.utils.NotificationActionWrapper;

import static androidx.media.app.NotificationCompat.MediaStyle;

public class DebugNotificationActivity extends AppCompatActivity {


    private Random random = new Random();

    public void sendNormalNotification(View view) {
        Notification notif = new NotificationCompat.Builder(this, "test-notif")
                .setContentTitle("Normal notification")
                .setContentText("This is the content text")
                .setContentInfo("Content info")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        NotificationManager manager = getSystemService(NotificationManager.class);

        manager.notify(1, notif);
    }

    public void sendMediaNotification(View view) {
        Notification notif = new NotificationCompat.Builder(this, "test-notif")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Track title")
                .setContentText("Artist - Album")
                .setLargeIcon(BitmapFactory
                        .decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setStyle(new MediaStyle()
                        .setMediaSession(SyncApplication.getApplication().getMediaSessionCompat()
                                .getSessionToken()))
                .addAction(R.drawable.ic_navigate_before_black, "Back", NotificationActionWrapper
                        .wrapMulti((context, intent) -> Log.d("NOTISYNC", "Back")))
                .addAction(R.drawable.ic_navigate_next_black, "Next", NotificationActionWrapper
                        .wrapMulti((context, intent) -> Log.d("NOTISYNC", "Next")))
                .build();
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(2, notif);
    }

    public void sendMessageNotification(View view) {
        Notification notif = new NotificationCompat.Builder(this, "test-notif")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.MessagingStyle(
                        SyncApplication.getApplication().getStubPerson())
                        .addMessage("Hi", System.currentTimeMillis(),
                                SyncApplication.getApplication().getStubPerson()))
                .addAction(new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground,
                        "Reply", NotificationActionWrapper.wrapMulti(
                        this::handleNotifWithInput))
                        .addRemoteInput(new RemoteInput.Builder("REPLY").build()).build())
                .build();

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(3, notif);
    }

    public void sendGroupMessageNotification(View view) {
        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(
                SyncApplication.getApplication().getStubPerson())
                .setGroupConversation(true)
                .addMessage("Hi", System.currentTimeMillis(),
                        SyncApplication.getApplication().getStubPerson());

        int number = random.nextInt(5) + 2;

        for (int i = 0; i < number; i++) {
            style.addMessage("Hi from person " + i, System.currentTimeMillis(), new Person.Builder()
                    .setName("Person " + i)
                    .setImportant(true)
                    .setBot(true)
                    .setKey("person-" + i)
                    .build());
        }

        Notification notif = new NotificationCompat.Builder(this, "test-notif")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(style)
                .addAction(new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground,
                        "Reply", NotificationActionWrapper.wrapMulti(
                        this::handleNotifWithInput))
                        .addRemoteInput(new RemoteInput.Builder("REPLY").build()).build())
                .build();

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(3, notif);
    }

    private void handleNotifWithInput(Context context, Intent intent) {
        Bundle results = RemoteInput.getResultsFromIntent(intent);

        if (results != null) {
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            Notification repliedNotification = new NotificationCompat.Builder(context, "test-notif")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("Replied")
                    .build();
            notificationManager.notify(3, repliedNotification);


            Log.d("NOFISYNC", results.getString("REPLY"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_notification);
        SyncApplication.getApplication().setupNotificationChannels();
    }
}
