package me.nickac.notisyncreborn.model;

import android.app.Person;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;

import me.nickac.notisyncreborn.SyncApplication;
import me.nickac.notisyncreborn.utils.IconUtils;
import me.nickac.notisyncreborn.utils.MiscUtils;

public class RemotePerson {

    private String key;
    private String name;
    private Bitmap icon;
    private boolean important;
    private boolean isBot;

    private RemotePerson(Person person) {
        key = person.getKey();
        if (person.getName() != null) {
            name = person.getName().toString();
        }
        if (person.getIcon() != null) {
            Icon ico = person.getIcon();
            if (ico != null) {
                icon = IconUtils.getBitmapFromIcon(SyncApplication.getContext(), ico);
            }
        }
        important = person.isImportant();
        isBot = person.isBot();
    }

    public static RemotePerson fromPerson(Person person) {
        return new RemotePerson(person);
    }

    public static RemotePerson fromCompatPerson(androidx.core.app.Person person) {
        return new RemotePerson(MiscUtils.toAndroidPerson(person));
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public boolean isImportant() {
        return important;
    }

    public boolean isBot() {
        return isBot;
    }

}
