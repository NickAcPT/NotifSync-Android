package me.nickac.notisyncreborn.model;

import androidx.core.app.NotificationCompat;

import java.util.List;
import java.util.stream.Collectors;

public class RemoteMessageInfo {
    private final List<RemoteMessage> messages;
    private final boolean groupConversation;
    private String conversationTitle;
    private RemotePerson user;

    private RemoteMessageInfo(NotificationCompat.MessagingStyle style) {
        messages = style.getMessages().stream().map(RemoteMessage::fromMessage)
                .collect(Collectors.toList());
        groupConversation = style.isGroupConversation();
        if (style.getConversationTitle() != null) {
            conversationTitle = style.getConversationTitle().toString();
        }
        if (style.getUser() != null) {
            user = RemotePerson.fromCompatPerson(style.getUser());
        }
    }

    public static RemoteMessageInfo from(NotificationCompat.MessagingStyle style) {
        return new RemoteMessageInfo(style);
    }

    public boolean isGroupConversation() {
        return groupConversation;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public RemotePerson getUser() {
        return user;
    }

    public List<RemoteMessage> getMessages() {
        return messages;
    }

}
