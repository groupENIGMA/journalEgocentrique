package com.github.groupENIGMA.journalEgocentrique.model;

import android.content.Context;

/**
 * This class is used by {@link DBInterface} to model the moods that can
 * be associated with an Entry.
 * <p>
 * You shouldn't create new instance of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DBInterface}
 * 
 * @version 0.1
 * @author groupENIGMA
 *
 */
public interface MoodInterface{

    /** Returns the unique id of the Mood
     * 
     * @return The unique id that identifies this Mood
     */
    public long getId();

    /** Returns the Resource ID for the emote associated with this mood
     * <p>
     * The value returned by this function can be used instead of using
     * the values of R properties (e.g. R.drawable.myImageID)
     *
     * @param context The application context
     * @return The resource ID of the emote of this Mood
     */
    public int getEmoteId(Context context);
}
