package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersAvtivity extends AppCompatActivity {
private MediaPlayer mediaPlayer;
private AudioManager audioManager;
private MediaPlayer.OnCompletionListener mCompletionListener=new MediaPlayer.OnCompletionListener ( ) {
    @Override
    public void onCompletion ( MediaPlayer mediaPlayer ) {
  releaseMediaPlayer ();
    }
};
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        audioManager = (AudioManager) getSystemService( Context.AUDIO_SERVICE );
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
                Word word = numbers.get ( position );
                int result = audioManager.requestAudioFocus ( mOnAudioFocusChangeListener , AudioManager.STREAM_MUSIC ,
                        AudioManager.AUDIOFOCUS_GAIN );

                if ( result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED ) {
                    // Start playback

                    releaseMediaPlayer ( );
                    mediaPlayer = MediaPlayer.create ( NumbersAvtivity.this , word.getmSoundId ( ) );
                    mediaPlayer.start ( );
                    mediaPlayer.setOnCompletionListener ( mCompletionListener );
                }
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
            audioManager.abandonAudioFocus (mOnAudioFocusChangeListener  );
        }
    }
}
