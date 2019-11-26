package malang.paradise.com.malangparadise.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;
import malang.paradise.com.malangparadise.request.RequestHandler;

public class Profile extends AppCompatActivity {

    ProgressBar loading;
    LinearLayout backButton,buttonLogOut,background,edit_profile;
    CircleImageView imageProfile;
    TextView username,name;
    public static String  gambarIntent;

    private String mPostKeyId = null, mPostkeyUsername = null, mPostKeyNama = null,
        mPostKeyPassword = null, mPostKeyImage = null;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        mPostKeyId = getIntent().getExtras().getString("id_user");
        mPostkeyUsername = getIntent().getExtras().getString("username");
        mPostKeyNama = getIntent().getExtras().getString("nama");
        mPostKeyPassword = getIntent().getExtras().getString("password");
        mPostKeyImage = getIntent().getExtras().getString("image");

        background = findViewById(R.id.background);
        backButton = findViewById(R.id.backButton);
        buttonLogOut = findViewById(R.id.buttonLogOut);
        imageProfile = findViewById(R.id.imageProfile);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        loading = findViewById(R.id.loading);
        edit_profile = findViewById(R.id.edit_profile);

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

        username.setText(mPostkeyUsername);
        name.setText(mPostKeyNama);
        Glide.with(getApplicationContext()).load(konfigurasi.PROFILE_IMAGE_URL+mPostKeyImage).apply(requestOptions).into(imageProfile);


        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                intent.putExtra("id_userI",mPostKeyId);
                intent.putExtra("usernameI",mPostkeyUsername);
                intent.putExtra("nameI",mPostKeyNama);
                intent.putExtra("passwordI",mPostKeyPassword);
                intent.putExtra("imageI",mPostKeyImage);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),HomePage.class);
        startActivity(intent);
        finish();
    }
}
