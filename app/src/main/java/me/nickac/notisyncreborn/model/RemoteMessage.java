package me.nickac.notisyncreborn.model;

import androidx.core.app.NotificationCompat;

import java.util.Map;

import me.nickac.notisyncreborn.utils.MiscUtils;

public class RemoteMessage {

    private RemotePerson person;
    private String text;
    private long timestamp;
    private String dataUri;
    private String dataMimeType;
    private Map<String, Object> extras;

    private RemoteMessage(NotificationCompat.MessagingStyle.Message message) {
        person = RemotePerson.fromCompatPerson(message.getPerson());
        extras = MiscUtils.bundleToMap(message.getExtras());
        text = message.getText().toString();
        timestamp = message.getTimestamp();
        if (message.getDataUri() != null) {
            dataUri = message.getDataUri().toString();
        }
        dataMimeType = message.getDataMimeType();
    }

    public static RemoteMessage fromMessage(NotificationCompat.MessagingStyle.Message message) {
        return new RemoteMessage(message);
    }

    public RemotePerson getPerson() {
        return person;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDataUri() {
        return dataUri;
    }

    public String getDataMimeType() {
        return dataMimeType;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

}
