package malang.paradise.com.malangparadise.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.math.BigInteger;

import de.hdodenhof.circleimageview.CircleImageView;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.inteface.UpdateProfileApi;
import malang.paradise.com.malangparadise.json.Value;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;
import malang.paradise.com.malangparadise.request.Utils;
import malang.paradise.com.malangparadise.request.encryptMd5;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {

    public static final String URL = "https://malang-paradise.000webhostapp.com/";

    String username,password,nama,id,image;

    private String hasilmd5;

    CircleImageView imageProfile;
    EditText Ename,Eusername,Epassword;
    LinearLayout save;
    ImageView back,cancel;
    LinearLayout buttonOption,cancel_button;
    TextView figuran;

    private ProgressDialog progressDialog;

    private String mPostKeyId = null, mPostkeyUsername = null, mPostKeyNama = null,
            mPostKeyPassword = null, mPostKeyImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        progressDialog = new ProgressDialog(EditProfile.this);
        Ename = findViewById(R.id.name);
        Eusername = findViewById(R.id.username);
        Epassword = findViewById(R.id.password);
        imageProfile = findViewById(R.id.imageProfile);
        save = findViewById(R.id.save_data);
        back = findViewById(R.id.back);
        cancel = findViewById(R.id.cancel);
        buttonOption = findViewById(R.id.buttonoption);
        cancel_button = findViewById(R.id.cancel_button);
        figuran = findViewById(R.id.figuran);

        mPostKeyId = getIntent().getExtras().getString("id_userI");
        mPostkeyUsername = getIntent().getExtras().getString("usernameI");
        mPostKeyNama = getIntent().getExtras().getString("nameI");
        mPostKeyPassword = getIntent().getExtras().getString("passwordI");
        mPostKeyImage = getIntent().getExtras().getString("imageI");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
                finish();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        Ename.setText(mPostKeyNama);
        Eusername.setText(mPostkeyUsername);
        figuran.setText(mPostKeyPassword);

        Eusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                back.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                buttonOption.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Ename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                back.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                buttonOption.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });Epassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                back.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                buttonOption.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.colorBlack);
        Glide.with(getApplicationContext()).load(konfigurasi.PROFILE_IMAGE_URL+mPostKeyImage).apply(requestOptions).into(imageProfile);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        btnMD5();
    }

    private void dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(EditProfile.this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Discard your changes ?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(getApplicationContext(),Profile.class);
//                        intent.putExtra("id_user",id);
//                        intent.putExtra("username",mPostkeyUsername);
//                        intent.putExtra("nama",mPostKeyNama);
//                        intent.putExtra("password",mPostKeyPassword);
//                        intent.putExtra("image",mPostKeyImage);
//                        startActivity(intent);
//                        finish();
                        onBackPressed();
                    }
                });
        alertDialog.show();
    }

    private void editProfile() {
        username = Eusername.getText().toString().trim();
        nama = Ename.getText().toString().trim();
        password = Epassword.getText().toString().trim();
        id = mPostKeyId;
        image = mPostKeyImage;

        progressDialog.setMessage("Edit Process...");
        showDialog();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UpdateProfileApi api = retrofit.create(UpdateProfileApi.class);
        Call<Value> call = api.editprofile(username,password,nama,id);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progressDialog.dismiss();
                if(value.equals("1")){
                    Toast.makeText(EditProfile.this, "Berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditProfile.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(EditProfile.this,HomePage.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfigurasi.URL_EDIT_PROFILE,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response.contains(konfigurasi.LOGIN_SUCCESS)) {
//                            hideDialog();
//                            Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
//                        } else {
//                            hideDialog();
//                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideDialog();
//                Toast.makeText(getApplicationContext(), "The server unreachable", Toast.LENGTH_LONG).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put(konfigurasi.KEY_USERNAME, username);
//                params.put(konfigurasi.KEY_PASSWORD, password);
//                params.put(konfigurasi.KEY_NAMA, nama);
//
//                return params;
//            }
//        };
//        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void btnMD5(){
        byte[] md5input = mPostKeyPassword.toString().getBytes();
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
        Epassword.setText(hasilmd5);

    }
}
