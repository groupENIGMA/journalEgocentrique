package com.github.groupENIGMA.journalEgocentrique.model;

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
    public int getId();
    
    /** Returns the path to the image associated to this Mood
     *
     * @return The path of the image associated to this Mood
     */
    public String getEmoteId();
}
