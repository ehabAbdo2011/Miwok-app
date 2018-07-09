package com.example.android.miwok;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
private MediaPlayer mediaPlayer;
private MediaPlayer.OnCompletionListener mCompletionListener=new MediaPlayer.OnCompletionListener ( ) {
    @Override
    public void onCompletion ( MediaPlayer mediaPlayer ) {
        releaseMediaPlayer ();
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        final ArrayList<Word> phrases= new ArrayList<>();
        phrases.add(new Word("إلى أين تذهب؟","Where are you going?",R.raw.where_are_you_going));
        phrases.add(new Word("ما اسمك؟","What is your name?",R.raw.whatisyourname));
        phrases.add(new Word("اسمي هو...","My name is...",R.raw.mynameis));
        phrases.add(new Word("كيف تشعر؟","How are you feeling?",R.raw.howareyoufeeling));
        phrases.add(new Word("اشعر بشعور جيد.","I’m feeling good.",R.raw.imfeelinggood));
        phrases.add(new Word("هل انت قادم؟","Are you coming?",R.raw.areyoucoming));
        phrases.add(new Word("نعم ، أنا قادم.","Yes, I’m coming.",R.raw.yesimcoming));
        phrases.add(new Word("أنا قادم.","I’m coming.",R.raw.imcoming));
        phrases.add(new Word("لنذهب.","Let’s go.",R.raw.letsgo));
        phrases.add(new Word("تعال الى هنا.","Come here.",R.raw.comehere));
        WordAdapter itemsAdapter = new WordAdapter(this, phrases,R.color.category_phrases);

        ListView listView = findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word=phrases.get(position);
                releaseMediaPlayer ();
                mediaPlayer=MediaPlayer.create(PhrasesActivity.this,word.getmSoundId());
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener ( mCompletionListener );
            }
        });
    }
    @Override
    protected void onStop ( ) {
        super.onStop ( );
        releaseMediaPlayer ();
    }
    private void releaseMediaPlayer ( ) {
        // If the media player is not null, then it may be currently playing a sound.
        if ( mediaPlayer != null ) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release ( );

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
        }
    }
}
