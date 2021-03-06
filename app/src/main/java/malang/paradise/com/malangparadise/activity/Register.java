package malang.paradise.com.malangparadise.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.inteface.RegisterApi;
import malang.paradise.com.malangparadise.json.Value;
import malang.paradise.com.malangparadise.request.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    public static final String URL = "https://malang-paradise.000webhostapp.com/";
    private ProgressDialog progress;
    private LinearLayout register;
    private EditText username;
    private EditText password;
    private EditText nama;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        register = findViewById(R.id.register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        nama = findViewById(R.id.nama);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namaS  = nama.getText().toString();
                String usernameS  = username.getText().toString();
                String passwordS  = password.getText().toString();

                progress = new ProgressDialog(Register.this);
                progress.setCancelable(false);
                progress.setMessage("Loading ...");
                progress.show();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RegisterApi api = retrofit.create(RegisterApi.class);
                Call<Value> call = api.daftar(usernameS, passwordS, namaS);
                call.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        progress.dismiss();
                        if (value.equals("1")) {
                            Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(""+message,response.toString());
                            Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(Register.this, Login.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        progress.dismiss();
                    }
                });
            }
        });
    }
}
