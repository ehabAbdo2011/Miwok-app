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

public class ColorsActivity extends AppCompatActivity {
private MediaPlayer mediaPlayer;
private AudioManager audioManager;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener ( ) {
        @Override
        public void onCompletion ( MediaPlayer mediaPlayer ) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer ( );
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
        final   ArrayList<Word> colors = new ArrayList<>();
        colors.add(new Word("أحمر", "Red",R.drawable.color_red,R.raw.red));
        colors.add(new Word("أخضر", "Green",R.drawable.color_green,R.raw.green));
        colors.add(new Word("بنى", "Brown",R.drawable.color_brown,R.raw.brown));
        colors.add(new Word("رمادى", "Gray",R.drawable.color_gray,R.raw.gray));
        colors.add(new Word("أسود", "Black",R.drawable.color_black,R.raw.black));
        colors.add(new Word("أبيض", "White",R.drawable.color_white,R.raw.white));
        colors.add(new Word("أصفر", "Yellow",R.drawable.color_mustard_yellow,R.raw.yellow));
        colors.add(new Word("أزرق", "Blue",R.drawable.blue,R.raw.blue));
        WordAdapter itemsAdapter = new WordAdapter(this, colors,R.color.category_colors);

        ListView listView = findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word=colors.get(position);
                int result = audioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback

                releaseMediaPlayer ();

                mediaPlayer=MediaPlayer.create(ColorsActivity.this,word.getmSoundId());
                mediaPlayer.start();
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
