package com.github.groupENIGMA.journalEgocentrique;

/**
 * This file is used as container for all constants of the Application
 */
public class AppConstants {

    /**
     * Format used to display the dates
     */
    public static final String DISPLAY_DATE_FORMAT = "d MMMMMMMMMM y";

    /**
     * SharedPreferences file name
     */
    public static final String SHARED_PREFERENCES_FILENAME =
            "journalEgocentrique_SharedPreferences";

    /**
     * SharedPreferences key name for the Entry timeout.
     * <p>
     * Each Entry can be modified or deleted for a limited time set by the
     * final user in the settings activity.
     *
     * The key name must be equal to the one specified as android:key in the
     * res/xml/preferences.xml file
     */
    public static final String PREFERENCES_KEY_ENTRY_TIMEOUT = "entryTimeout";

    /**
     * The file name prefix for the Mood images saved in res/drawable
     * <p>
     * This is used by model.Mood.getEmoteId() method to load the correct emote
     * id.
     * Each mood image must be saved using this prefix. For example the emote
     * for the Mood with id=1 an image must be named:
     *      MOOD_EMOTE_PREFIX_1.ext
     */
    public static final String MOOD_EMOTE_PREFIX = "mood_emote_";

    /**
     * The directory name in the External Storage where the photos will be
     * saved
     */
    public static final String EXTERNAL_STORAGE_PHOTO_DIR =
            "JournalEgocentrique Photos";

    /**
     * Photo filename prefix.
     * The final filename will be: prefix + id + ".jpg"
     */
    public static final String PHOTO_FILENAME_PREFIX = "";

    /**
     * Thumb filename prefix.
     * The final filename will be: prefix + id + ".jpg"
     */
    public static final String THUMB_FILENAME_PREFIX = "thumb_";
}
