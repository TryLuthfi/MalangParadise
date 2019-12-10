package malang.paradise.com.malangparadise.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.adapter.DetailPostinganAdapter;
import malang.paradise.com.malangparadise.json.Gambar;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;
import malang.paradise.com.malangparadise.request.RequestHandler;
import malang.paradise.com.malangparadise.request.Utils;

public class DetailPostingan extends AppCompatActivity {
    private String mPostKeyNama = null, mPostKeyGambar = null, mPostKeyBerita = null,
    mPostKeyRating = null, mPostKeyLokasi = null, mPostKeyIdPostingan = null;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView iv_header;
    private ImageView bintangIcon;
    private TextView berita;
    private TextView rating;
    private TextView lokasi;

    String id_user;

    private static final String TAG = "DetailPostingan";

    List<Gambar> gambarList;
    RecyclerView recyclerViewGambar;

    private AppCompatRatingBar rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_postingan);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        berita = findViewById(R.id.berita);
        rating = findViewById(R.id.rating);
        lokasi = findViewById(R.id.lokasi);
        bintangIcon = findViewById(R.id.bintangIcon);
        iv_header = findViewById(R.id.iv_header);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        rate = findViewById(R.id.rate);

        id_user = getIdUser();

        mPostKeyIdPostingan = getIntent().getExtras().getString("id_postingan");
        mPostKeyNama = getIntent().getExtras().getString("nama");
        mPostKeyGambar = getIntent().getExtras().getString("gambar");
        mPostKeyBerita = getIntent().getExtras().getString("berita");
        mPostKeyRating = getIntent().getExtras().getString("rating");
        mPostKeyLokasi = getIntent().getExtras().getString("lokasi");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        berita.setText(mPostKeyBerita);
        lokasi.setText(mPostKeyLokasi);
        if(mPostKeyRating.equals("null")){
            rating.setText("0.0");
            bintangIcon.setImageDrawable(ContextCompat.getDrawable(DetailPostingan.this, R.drawable.starnull));
        }else {
            bintangIcon.setImageDrawable(ContextCompat.getDrawable(DetailPostingan.this, R.drawable.stars));
            rating.setText(mPostKeyRating);
        }
        collapsingToolbar.setTitle(mPostKeyNama);
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorBlack));
        Glide.with(Objects.requireNonNull(getApplicationContext())).load("http://malang-paradise.000webhostapp.com/" + mPostKeyGambar).apply(requestOptions).into(iv_header);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewGambar = findViewById(R.id.gambarRecyclerView);
        recyclerViewGambar.setHasFixedSize(true);
        recyclerViewGambar.setLayoutManager(new LinearLayoutManager(DetailPostingan.this));
        recyclerViewGambar.setLayoutManager(layoutManager);

        gambarList = new ArrayList<>();

        loadGambar();

        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                final Dialog dialog = new Dialog(DetailPostingan.this);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.rate);
                AppCompatRatingBar rate = dialog.findViewById(R.id.rating);
                Button kirim = dialog.findViewById(R.id.kirim);
                rate.setRating(Float.parseFloat(""+rating));
                kirim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        class AddData extends AsyncTask<Void, Void, String> {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                Log.d(TAG, "onPostExecute: " + s);
                            }

                            @Override
                            protected String doInBackground(Void... v) {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("id_postingan", mPostKeyIdPostingan);
                                params.put("id_user", id_user);
                                params.put("nilai_rating", Float.toString(rating));

                                RequestHandler rh = new RequestHandler();
                                String res = rh.sendPostRequest(konfigurasi.URL_POST_RATING, params);
                                return res;
                            }
                        }

                        AddData ae = new AddData();
                        ae.execute();
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            }
        });


    }

    private void loadGambar() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, konfigurasi.URL_PRODUCTS_GAMBAR,

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
                                gambarList.add(new Gambar(
                                        product.getString("id_gambar"),
                                        product.getString("id_postingan"),
                                        product.getString("gambar")
                                ));
                            }

                            DetailPostinganAdapter adapter = new DetailPostinganAdapter(DetailPostingan.this, gambarList);

                            if (adapter != null){
                                recyclerViewGambar.setAdapter(adapter);

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

}
