package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    private int mColors;
    public WordAdapter(Activity context, ArrayList<Word> numbers,int colors) {
        super(context, 0, numbers);
        mColors=colors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Word currentWord=getItem(position);
        TextView englishTranslation=listItemView.findViewById(R.id.english_text_view);
        englishTranslation.setText(currentWord.getmEnglishTranslation());
        TextView defaultTranslation=listItemView.findViewById(R.id.default_text_view);
        defaultTranslation.setText(currentWord.getmDefaultTranslation());
        ImageView imageId=listItemView.findViewById(R.id.image_view);
        if (currentWord.hasImage()) {
            imageId.setImageResource(currentWord.getmImageId());
            imageId.setVisibility(View.VISIBLE);
        }
        else {
            imageId.setVisibility(View.GONE);
        }
        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColors);
        textContainer.setBackgroundColor(color);
        return listItemView;
    }
}
