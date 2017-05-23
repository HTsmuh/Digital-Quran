package com.horizontech.biz.digitalquran.Menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.horizontech.biz.digitalquran.Database.DbBackend;
import com.horizontech.biz.digitalquran.R;

public class SettingActivity extends AppCompatActivity {
    Spinner textSize;
    Spinner displayMode;
    Spinner textScript;
    Button setting_button;
    DbBackend db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        db=new DbBackend(SettingActivity.this);

        textSize = (Spinner) findViewById(R.id.text_size);
        displayMode= (Spinner) findViewById(R.id.display_mode);
        textScript= (Spinner) findViewById(R.id.text_script);

        setting_button = (Button) findViewById(R.id.setting_btn);

        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(textSize.getSelectedItem().toString().trim().equals("Select Text Size")
                    &&displayMode.getSelectedItem().toString().trim().equals("Select Display Mode")
                    &&textScript.getSelectedItem().toString().trim().equals("Select Script")){
                Toast.makeText(SettingActivity.this, "Previous Setting", Toast.LENGTH_SHORT).show();
            }else{
               String size=textSize.getSelectedItem().toString().trim();
                    if(size.equals("Select Text Size")){
                        db.setSize(db.getSize());
                    }else{
                        db.setSize(size);
                    }
               String mode=displayMode.getSelectedItem().toString().trim();
                if (mode.equals("Select Display Mode")){
                    db.setMode(db.getMode());
                }else if (mode.equals("Day Mode")){
                    db.setMode("DayMoodFullScreen");
                }else if (mode.equals("Night Mode")){
                    db.setMode("NightMoodFullScreen");
                }
               String script=textScript.getSelectedItem().toString().trim();
                if (script.equals("Select Script")){
                    db.setScript(db.getScript());
                }else if (script.equals("Me Quran")){
                    db.setScript("me_quran");
                }else {
                    db.setScript("pdms");
                }
                Toast.makeText(SettingActivity.this, "Setting Changed", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}