package com.github.groupENIGMA.journalEgocentrique.model;

import android.content.Context;
import com.github.groupENIGMA.journalEgocentrique.AppConstants;

public class Mood implements MoodInterface {

    private long id;

    /**
     * Create a new Mood with the given id
     * 
     * @param id The mood id
     */
    protected Mood(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public int getEmoteId(Context context) {
        return context.getResources().getIdentifier(
                AppConstants.MOOD_EMOTE_PREFIX + String.valueOf(this.id),
                "drawable",
                context.getPackageName()
        );
    }

}
