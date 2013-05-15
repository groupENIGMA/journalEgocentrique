package journalEgocentrique.model;

public interface MoodInterface{
    
    /**
     * @return The unique id that identifies this Mood
     */
    public int getId();
    
    /**
     * @return The path of the image associated to this Mood
     */
    public String getPathImage();
}
