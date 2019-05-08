package com.example.smarthostel;

public class Effective {
    Preference preference;
    Preferences preferences;

    public Effective(Preference preference, Preferences preferences) {
        this.preference = preference;
        this.preferences = preferences;
    }

    public Effective() {
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
