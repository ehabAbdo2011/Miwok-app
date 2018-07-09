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

public class PhrasesActivity extends AppCompatActivity {
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
                Word word = phrases.get ( position );
                int result = audioManager.requestAudioFocus ( mOnAudioFocusChangeListener , AudioManager.STREAM_MUSIC ,
                        AudioManager.AUDIOFOCUS_GAIN );

                if ( result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED ) {
                    // Start playback

                    releaseMediaPlayer ( );
                    releaseMediaPlayer ( );
                    mediaPlayer = MediaPlayer.create ( PhrasesActivity.this , word.getmSoundId ( ) );
                    mediaPlayer.start ( );
                    mediaPlayer.setOnCompletionListener ( mCompletionListener );
                }
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
            audioManager.abandonAudioFocus (mOnAudioFocusChangeListener  );
        }
    }
}
