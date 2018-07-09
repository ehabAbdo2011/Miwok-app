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

public class FamilyActivity extends AppCompatActivity {
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
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.word_list );
        audioManager = (AudioManager) getSystemService( Context.AUDIO_SERVICE );

        final ArrayList <Word> family = new ArrayList <> ( );
        family.add ( new Word ( "أب" , "Father" , R.drawable.family_father , R.raw.father ) );
        family.add ( new Word ( "أم" , "Mother" , R.drawable.family_mother , R.raw.mother ) );
        family.add ( new Word ( "ابن" , "Son" , R.drawable.family_son , R.raw.son ) );
        family.add ( new Word ( "ابنة" , "Daughter" , R.drawable.family_daughter , R.raw.daughter ) );
        family.add ( new Word ( "الأخ الاكبر" , "Older Brother" , R.drawable.family_older_brother , R.raw.olderbrother ) );
        family.add ( new Word ( "الأخ الأصغر" , "Younger Brother" , R.drawable.family_younger_brother , R.raw.youngerbrother ) );
        family.add ( new Word ( "الأخت الكبرى" , "Older Sister" , R.drawable.family_older_sister , R.raw.oldersister ) );
        family.add ( new Word ( "الأخت الصغرى" , "Younger Sister" , R.drawable.family_younger_sister , R.raw.youngersister ) );
        family.add ( new Word ( "الجد" , "Grandfather" , R.drawable.family_grandfather , R.raw.grandfather ) );
        family.add ( new Word ( "الجدة" , "Grandmother" , R.drawable.family_grandmother , R.raw.grandmother ) );
//        family.add(new Word("عم/خال","Uncle",R.mipmap.ic_launcher));
//        family.add(new Word("عمة/خالة","Aunt",R.mipmap.ic_launcher));
        WordAdapter itemsAdapter = new WordAdapter ( this , family , R.color.category_family );
        ListView listView = findViewById ( R.id.list );
        listView.setAdapter ( itemsAdapter );
        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView <?> adapterView , View view , int position , long l ) {
                Word word = family.get ( position );
                int result = audioManager.requestAudioFocus ( mOnAudioFocusChangeListener , AudioManager.STREAM_MUSIC ,
                        AudioManager.AUDIOFOCUS_GAIN );

                if ( result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED ) {
                    // Start playback

                    releaseMediaPlayer ( );
                    releaseMediaPlayer ( );
                    mediaPlayer = MediaPlayer.create ( FamilyActivity.this , word.getmSoundId ( ) );
                    mediaPlayer.start ( );
                    mediaPlayer.setOnCompletionListener ( mCompletionListener );
                }
            }
        } );
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

