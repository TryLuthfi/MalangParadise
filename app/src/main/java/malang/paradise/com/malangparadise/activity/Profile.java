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
    LinearLayout backButton,buttonLogOut,background;
    CircleImageView imageProfile;
    TextView username,name;

    String id_userS,usernameS,namaS,passwordS,imageS;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        background = findViewById(R.id.background);
        backButton = findViewById(R.id.backButton);
        buttonLogOut = findViewById(R.id.buttonLogOut);
        imageProfile = findViewById(R.id.imageProfile);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        loading = findViewById(R.id.loading);

        getJSON();

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getJSON() {

        class getJSON extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_DATAUSER);
                return s;
            }
        }
        getJSON gj = new getJSON();
        gj.execute();
    }

    private void showData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for (int i = 0 ; i < result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                id_userS = jo.getString("id_user");
                usernameS = jo.getString("username");
                namaS = jo.getString("nama");
                passwordS = jo.getString("password");
                imageS = jo.getString("image");

                HashMap<String,String> data = new HashMap<>();

                data.put("id_user",id_userS);

                list.add(data);

                String id_user = getIdUser();

                RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

                if(id_user.equals(id_userS)){
                    username.setText(usernameS);
                    name.setText(namaS);
                    Glide.with(getApplicationContext()).load(konfigurasi.PROFILE_IMAGE_URL+imageS).apply(requestOptions).into(imageProfile);
                }

                background.setVisibility(View.VISIBLE);
                loading.setVisibility(View.INVISIBLE);

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getIdUser(){
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String id_user = preferences.getString("id_user", "null");
        return id_user;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),HomePage.class);
        startActivity(intent);
        finish();
    }
}
