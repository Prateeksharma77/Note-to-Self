package com.example.notetoself;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private boolean mSound;

    public static final int FAST=0;
    public static final int SLOW=1;
    public static final int NONE=2;

    private int mAnimOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        prefs = getSharedPreferences("Note to self", MODE_PRIVATE);
        editor = prefs.edit();

        mSound = prefs.getBoolean("sound", true);
        CheckBox checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);
        if (mSound) {
            checkBoxSound.setChecked(true);
        } else {
            checkBoxSound.setChecked(false);
        }
        checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("sound= ", "" + mSound);
                Log.i("isChecked = ", "" + isChecked);

                //If mSound is true make it false
                //If mSound is false make it true
                mSound = !mSound;
                editor.putBoolean("sound", mSound);
            }

        });

        //Now for the radio buttons
        mAnimOption=prefs.getInt("anim option",FAST);

        RadioGroup radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
        //Deselect all buttons
        radioGroup.clearCheck();
        //which radio button is selected?
        switch (mAnimOption) {
            case FAST:
                radioGroup.check(R.id.radioFast);
                break;
            case SLOW:
                radioGroup.check(R.id.radioSlow);
                break;
            case NONE:
                radioGroup.check(R.id.radioNone);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton) group.findViewById(checkedId);
                if(null !=rb && checkedId > -1 )
                {

                    if (checkedId == R.id.radioFast) {
                        mAnimOption = FAST;
                    } else if (checkedId == R.id.radioSlow) {
                        mAnimOption = SLOW;
                    } else if (checkedId == R.id.radioNone) {
                        mAnimOption = NONE;
                    }

                    editor.putInt("anim option",mAnimOption);
                }
            }
        });
    }

    @Override
 protected void onPause(){
        super.onPause();

        //save the settings here
        editor.commit();
    }

}