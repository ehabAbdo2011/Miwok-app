package com.example.android.miwok;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersAvtivity extends AppCompatActivity {
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
        final ArrayList<Word> numbers= new ArrayList<>();
       numbers.add(new Word("واحد","one",R.drawable.number_one,R.raw.one));
        numbers.add(new Word("اثنان","two",R.drawable.number_two,R.raw.two));
        numbers.add(new Word("ثلاثة","three",R.drawable.number_three,R.raw.three));
        numbers.add(new Word("أربعة","four",R.drawable.number_four,R.raw.four));
        numbers.add(new Word("خمسة","five",R.drawable.number_five,R.raw.five));
        numbers.add(new Word("ستة","six",R.drawable.number_six,R.raw.six));
        numbers.add(new Word("سبعة","seven",R.drawable.number_seven,R.raw.seven));
        numbers.add(new Word("ثمانية","eight",R.drawable.number_eight,R.raw.eight));
        numbers.add(new Word("تسعة","nine",R.drawable.number_nine,R.raw.nine));
        numbers.add(new Word("عشرة","ten",R.drawable.number_ten,R.raw.ten));
        WordAdapter itemsAdapter = new WordAdapter(this, numbers,R.color.category_numbers);
        ListView listView = findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word=numbers.get(position);
                releaseMediaPlayer ();
                mediaPlayer=MediaPlayer.create(NumbersAvtivity.this,word.getmSoundId());
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener ( mCompletionListener );
            }
        });
        listView.setAdapter(itemsAdapter);
    }
    @Override
    protected void onStop ( ) {
        super.onStop ( );
        releaseMediaPlayer ();
    }
    /**
     * Clean up the media player by releasing its resources.
     */
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
