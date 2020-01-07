package malang.paradise.com.malangparadise.activity;

import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.adapter.KategoriAdapter;
import malang.paradise.com.malangparadise.fragment.AllFragment;
import malang.paradise.com.malangparadise.fragment.KategoriFragment;
import malang.paradise.com.malangparadise.inteface.RecyclerViewListClicked;
import malang.paradise.com.malangparadise.json.Kategori;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;
import malang.paradise.com.malangparadise.request.RequestHandler;
import malang.paradise.com.malangparadise.request.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HomePage extends AppCompatActivity implements RecyclerViewListClicked {
    List<Kategori> kategoriList;
    RecyclerView recyclerViewKategori;
    private String JSON_STRING;
    private String id_userS,usernameS,namaS,passwordS,imageS;
    public static String id_userr, usernamee, namaa, passwordd, imagee;
    private ProgressBar loading;

    public static String kategoriIntent = "Semua";

    private static final int PERIOD = 2000;
    private long lastPressedTime;

    private LinearLayout profile;
    private LinearLayout line;
    private LinearLayout search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        profile = findViewById(R.id.profile);
        search = findViewById(R.id.search);
        line = findViewById(R.id.line);

        getJSON();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewKategori = findViewById(R.id.kategorirRecyclerView);
        recyclerViewKategori.setHasFixedSize(true);
        recyclerViewKategori.setLayoutManager(new LinearLayoutManager(HomePage.this));
        recyclerViewKategori.setLayoutManager(layoutManager);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        kategoriList = new ArrayList<>();

        FragmentManager fm =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.frame_container, new AllFragment());
        fragmentTransaction.commit();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("id_user", id_userr);
                intent.putExtra("username", usernamee);
                intent.putExtra("nama", namaa);
                intent.putExtra("password", passwordd);
                intent.putExtra("image", imagee);
                startActivity(intent);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

    }

    private void getJSON() {

        class getJSON extends AsyncTask<Void,Void,String> {

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
                    id_userr = id_userS;
                    usernamee = usernameS;
                    namaa = namaS;
                    passwordd = passwordS;
                    imagee = imageS;
                }

            }
            loadKategori();
        }catch (JSONException e) {
            e.printStackTrace();
        }
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


    private void loadKategori() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, konfigurasi.URL_PRODUCTS_KATEGORI,

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

                            KategoriAdapter adapter = new KategoriAdapter(HomePage.this, kategoriList, HomePage.this);

                            if (adapter != null){
                                recyclerViewKategori.setAdapter(adapter);

                                loading.setVisibility(View.INVISIBLE);
                                line.setVisibility(View.VISIBLE);
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

    private String getIdUser(){
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String id_user = preferences.getString("id_user", "null");
        return id_user;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        kategoriIntent = kategoriList.get(position).getId_kategori();

        if(kategoriList.get(position).getNama().equals("Semua")){
            FragmentManager fm =  getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.replace(R.id.frame_container, new AllFragment());
            fragmentTransaction.commit();
        } else if(kategoriList.get(position).getNama().equals("Pendakian")){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id. frame_container , new KategoriFragment());
            fragmentTransaction.commit();
        } else if (kategoriList.get(position).getNama().equals("Pemandian")){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id. frame_container , new KategoriFragment());
            fragmentTransaction.commit();
        } else if (kategoriList.get(position).getNama().equals("Edukasi")){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id. frame_container , new KategoriFragment());
            fragmentTransaction.commit();
        } else if (kategoriList.get(position).getNama().equals("Makanan")){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id. frame_container , new KategoriFragment());
            fragmentTransaction.commit();
        }
    }
}
