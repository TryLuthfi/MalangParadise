package malang.paradise.com.malangparadise.activity;

import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;
import malang.paradise.com.malangparadise.request.Utils;
import malang.paradise.com.malangparadise.request.encryptMd5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private LinearLayout login;
    private LinearLayout facebookAuth;
    private LinearLayout googleAuth;
    private TextView register;
    private EditText usernameE;
    private EditText passwordE;
    private ProgressDialog progressDialog;
    private String hasilmd5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        String id_user = getIdUser();
        if (id_user != "null") {
            gotoCourseActivity();
        }

        facebookAuth = findViewById(R.id.facebookAuth);
        googleAuth = findViewById(R.id.googleAuth);
        progressDialog = new ProgressDialog(Login.this);
        usernameE = findViewById(R.id.username);
        passwordE = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMD5();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        facebookAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Not yet", Toast.LENGTH_SHORT).show();
            }
        });

        googleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Not Yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
//        Toast.makeText(this, hasilmd5, Toast.LENGTH_SHORT).show();
        final String username = usernameE.getText().toString().trim();
        final String password = passwordE.getText().toString().trim();

        progressDialog.setMessage("Login Process...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfigurasi.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains(konfigurasi.LOGIN_SUCCESS)) {
                            hideDialog();
                            String id_user = response.toString().split(";")[1];
                            Log.e("iniidpelanggan", id_user);
                            setPreference(id_user);
                            gotoCourseActivity();

                        } else {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "The server unreachable", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_USERNAME, username);
                params.put(konfigurasi.KEY_PASSWORD, hasilmd5);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void gotoCourseActivity() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    void setPreference(String id_user){
        SharedPreferences mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("id_user", id_user);
        editor.apply();
}
    private String getIdUser(){
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String id_user = preferences.getString("id_user", "null");
        return id_user;
    }

    public void btnMD5(){
        byte[] md5input = passwordE.getText().toString().getBytes();
        BigInteger md5Data = null;

        try{
            md5Data =new BigInteger(1, encryptMd5.encryptMD5(md5input));
        }catch (Exception e){
            e.printStackTrace();
        }

        String md5Str = md5Data.toString(16);
        if(md5Str.length() < 32){
            md5Str = 0 + md5Str;
        }

        hasilmd5 = md5Str;
        login();

    }
}
