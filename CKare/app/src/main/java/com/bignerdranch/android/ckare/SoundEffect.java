package com.bignerdranch.android.ckare;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundEffect {
    private MediaPlayer buttonClickingSoundEffect;

    // load the music file in the raw resource file
    public void loadButtonClickingSoundEffect(Context context) {
        MediaPlayer buttonClickingSoundEffect = MediaPlayer.create(context, R.raw.clicking_sound_effect);
        buttonClickingSoundEffect.start();
    }
}
