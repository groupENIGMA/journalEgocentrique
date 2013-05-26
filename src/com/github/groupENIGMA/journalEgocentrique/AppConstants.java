package com.github.groupENIGMA.journalEgocentrique;

/**
 * This file is used as container for all constants of the Application
 */
public class AppConstants {

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

}