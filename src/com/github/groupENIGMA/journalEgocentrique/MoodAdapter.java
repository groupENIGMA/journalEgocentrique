package com.github.groupENIGMA.journalEgocentrique;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Mood;

public class MoodAdapter extends BaseAdapter {
    private Context mContext;
    private List<Mood> moods;
    private DB database;

    /**
     * A custom adapter that displays the moods
     * that the user can choose
     * @param c Context in which we operate
     */
    public MoodAdapter(Context c) {
        mContext = c;
        database = new DB(c);
        database.open();
        moods = database.getAvailableMoods();
    }

    @Override
    public int getCount() {
        return moods.size();
    }

    @Override
    public Object getItem(int position){
        return moods.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(moods.get(position).getEmoteId(mContext));
        return imageView;
    }

	@Override
	public long getItemId(int position) {
		return moods.get(position).getId();
	}

}