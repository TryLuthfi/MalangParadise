package malang.paradise.com.malangparadise.activity;

import androidx.appcompat.app.AppCompatActivity;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.request.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class getStarted extends AppCompatActivity {
    private LinearLayout btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        String id_user = getIdUser();
        if (id_user != "null") {
            gotoCourseActivity();
        }

        btnGetStarted = findViewById(R.id.getStarted);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    private void gotoCourseActivity() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    private String getIdUser(){
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String id_user = preferences.getString("id_user", "null");
        return id_user;
    }
}
