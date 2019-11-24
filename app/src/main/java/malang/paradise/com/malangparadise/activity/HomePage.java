package malang.paradise.com.malangparadise.activity;

import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.adapter.KategoriAdapter;
import malang.paradise.com.malangparadise.adapter.PostinganAdapter;
import malang.paradise.com.malangparadise.json.Kategori;
import malang.paradise.com.malangparadise.json.Postingan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePage extends AppCompatActivity {
    private static final String URL_PRODUCTS_POSTINGAN = "https://malang-paradise.000webhostapp.com/postingan.php";
    private static final String URL_PRODUCTS_KATEGORI = "https://malang-paradise.000webhostapp.com/kategori.php";
    List<Kategori> kategoriList;
    List<Postingan> postinganList;
    RecyclerView recyclerViewKategori;
    RecyclerView recyclerViewPostingan;
    private String JSON_STRING;
    private ProgressBar loading;

    private static final int PERIOD = 2000;
    private long lastPressedTime;
//    boolean doubleBackToExitPressedOnce = false;

    private LinearLayout profile;
    private LinearLayout search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        profile = findViewById(R.id.profile);
        search = findViewById(R.id.search);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewKategori = findViewById(R.id.kategorirRecyclerView);
        recyclerViewKategori.setHasFixedSize(true);
        recyclerViewKategori.setLayoutManager(new LinearLayoutManager(HomePage.this));
        recyclerViewKategori.setLayoutManager(layoutManager);

        recyclerViewPostingan = findViewById(R.id.postingantRecyler);
        recyclerViewPostingan.setHasFixedSize(true);
        recyclerViewPostingan.setLayoutManager(new LinearLayoutManager(HomePage.this));
//        recyclerViewPostingan.setLayoutManager(layoutManager);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        kategoriList = new ArrayList<>();
        postinganList = new ArrayList<>();

        loadKategori();
        loadPostingan();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Tekan sekali lagi untuk keluar dari Aplikasi.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

    private void loadPostingan() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS_POSTINGAN,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);


                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                postinganList.add(new Postingan(
                                        product.getString("id_postingan"),
                                        product.getString("id_user"),
                                        product.getString("id_kategori"),
                                        product.getString("rating"),
                                        product.getString("id_gambar"),
                                        product.getString("gambar"),
                                        product.getString("nama"),
                                        product.getString("berita"),
                                        product.getString("lokasi"),
                                        product.getString("tanggal_upload")
                                ));
                            }

                            PostinganAdapter adapter = new PostinganAdapter(HomePage.this, postinganList);

                            if (adapter != null){
                                recyclerViewPostingan.setAdapter(adapter);

                                loading.setVisibility(View.INVISIBLE);
                            }else {
                                Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                            }

//                            loading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext())).add(stringRequest);
    }

    private void loadKategori() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS_KATEGORI,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);


                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                kategoriList.add(new Kategori(
                                            product.getString("id_kategori"),
                                            product.getString("nama"),
                                            product.getString("gambar")
                                    ));
                            }

                            KategoriAdapter adapter = new KategoriAdapter(HomePage.this, kategoriList);

                            if (adapter != null){
                                recyclerViewKategori.setAdapter(adapter);

                                loading.setVisibility(View.INVISIBLE);
                            }else {
                                Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                            }

//                            loading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext())).add(stringRequest);
    }
}
