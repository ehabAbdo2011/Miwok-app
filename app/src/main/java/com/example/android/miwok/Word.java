package com.example.android.miwok;

public class Word {
    private static final int NO_IMAGE=-1;
    private int mImageId=NO_IMAGE;
    private int mSoundId;
    private String mDefaultTranslation;
    private String mEnglishTranslation;
    public Word(String defaultTranslation,String englishTranslation,int imageId,int soundId){
        mDefaultTranslation=defaultTranslation;
        mEnglishTranslation=englishTranslation;
        mImageId=imageId;
        mSoundId=soundId;
    }
    public Word(String defaultTranslation,String englishTranslation,int soundId){
        mDefaultTranslation=defaultTranslation;
        mEnglishTranslation=englishTranslation;
        mSoundId=soundId;
    }

    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }
    public String getmEnglishTranslation() {
        return mEnglishTranslation;
    }

    public int getmImageId() {
        return mImageId;
    }
    public Boolean hasImage(){
        return mImageId!=NO_IMAGE;
    }

    public int getmSoundId() {
        return mSoundId;
    }
}
