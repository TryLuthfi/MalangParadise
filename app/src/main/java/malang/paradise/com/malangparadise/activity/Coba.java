package malang.paradise.com.malangparadise.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.adapter.DetailPostinganAdapter;
import malang.paradise.com.malangparadise.json.Gambar;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;
import malang.paradise.com.malangparadise.request.RequestHandler;
import malang.paradise.com.malangparadise.request.Utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Coba extends FragmentActivity  implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private String mPostKeyNama = null, mPostKeyGambar = null, mPostKeyBerita = null
            , mPostKeyLokasi = null, mPostKeyIdPostingan = null, mPostKeyLat = null, mPostKeyLong = null;
    private String mPostKeyRating;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView iv_header;
    private ImageView bintangIcon;
    private TextView berita;
    private TextView rating;
    private TextView lokasi;

    private Double latD, longD;

    private static final int PERMISSION_REQUEST_CODE = 7001;
    private static final int PLAY_SERVICE_REQUEST = 7002;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    Dialog dialog;

    String JSON_STRING;
    String id_user;

    private static final String TAG = "DetailPostingan";

    List<Gambar> gambarList;
    RecyclerView recyclerViewGambar;

    private static final int UPDATE_INTERVAL = 5000;//5 detik
    private static final int FASTEST_INTERVAL = 3000;//3detik
    private static final int DISPLACEMENT = 10;
    private TextView marker;

    private AppCompatRatingBar rate;

    String id_rating, id_postingan, id_userr;
    String nilai_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        berita = findViewById(R.id.berita);
        rating = findViewById(R.id.rating);
        lokasi = findViewById(R.id.lokasi);
        marker = findViewById(R.id.marker);
        bintangIcon = findViewById(R.id.bintangIcon);
        iv_header = findViewById(R.id.iv_header);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        rate = findViewById(R.id.rate);

        getJSON();
        id_user = getIdUser();

        setUpLocation();

        mPostKeyIdPostingan = getIntent().getExtras().getString("id_postingan");
        mPostKeyNama = getIntent().getExtras().getString("nama");
        mPostKeyGambar = getIntent().getExtras().getString("gambar");
        mPostKeyBerita = getIntent().getExtras().getString("berita");
        mPostKeyRating = getIntent().getExtras().getString("rating");
        mPostKeyLokasi = getIntent().getExtras().getString("lokasi");
        mPostKeyLat = getIntent().getExtras().getString("lat");
        mPostKeyLong = getIntent().getExtras().getString("longg");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        berita.setText("    "+mPostKeyBerita);
        lokasi.setText("    "+mPostKeyLokasi);

        if(mPostKeyRating.equals("null")){
            rating.setText("0.0");
            bintangIcon.setImageDrawable(ContextCompat.getDrawable(Coba.this, R.drawable.starnull));
        }else {
            bintangIcon.setImageDrawable(ContextCompat.getDrawable(Coba.this, R.drawable.stars));
            rating.setText(mPostKeyRating);
        }
        collapsingToolbar.setTitle(mPostKeyNama);
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorBlack));
        Glide.with(Objects.requireNonNull(getApplicationContext())).load("https://malang-paradise.000webhostapp.com/" + mPostKeyGambar).apply(requestOptions).into(iv_header);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewGambar = findViewById(R.id.gambarRecyclerView);
        recyclerViewGambar.setHasFixedSize(true);
        recyclerViewGambar.setLayoutManager(new LinearLayoutManager(Coba.this));
        recyclerViewGambar.setLayoutManager(layoutManager);

        gambarList = new ArrayList<>();

        loadGambar();

        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                dialog = new Dialog(Coba.this);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.rate);
                AppCompatRatingBar rrt = dialog.findViewById(R.id.rating);
                Button kirim = dialog.findViewById(R.id.kirim);
                rrt.setRating(Float.parseFloat(""+rating));
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
                                if(s.equals("Berhasil Update") || s.equals("Berhasil")){
                                    dialog.dismiss();
                                }
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnMarkerClickListener(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);

        marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latD = Double.parseDouble(mPostKeyLat);
                longD = Double.parseDouble(mPostKeyLong);
                LatLng sydney = new LatLng(latD, longD);
                mMap.addMarker(new MarkerOptions().position(sydney).title(""+mPostKeyNama));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(10).build();
                cameraPosition = new CameraPosition.Builder()
                        .target(sydney)
                        .zoom(14)
                        .tilt(15)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        latD = Double.parseDouble(mPostKeyLat);
        longD = Double.parseDouble(mPostKeyLong);
        LatLng sydney = new LatLng(latD, longD);
        mMap.addMarker(new MarkerOptions().position(sydney).title(""+mPostKeyNama));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(10).build();
        cameraPosition = new CameraPosition.Builder()
                .target(sydney)
                .zoom(14)
                .tilt(15)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
//                displayLocation();
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_REQUEST).show();
            else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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

                            DetailPostinganAdapter adapter = new DetailPostinganAdapter(Coba.this, gambarList);

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
                String s = rh.sendGetRequest(konfigurasi.URL_GET_DATARATING);
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
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY_RATING);

            for (int i = 0 ; i < result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                id_rating = jo.getString("id_rating");
                id_postingan = jo.getString("id_postingan");
                id_userr = jo.getString("id_user");
                nilai_rating = jo.getString("nilai_rating");

                HashMap<String,String> data = new HashMap<>();

                data.put("id_user",id_userr);

                if(id_userr.equals(id_user) && id_postingan.equals(mPostKeyIdPostingan)){
                    rate.setRating(Float.parseFloat(""+nilai_rating));
//                    Toast.makeText(this, ""+nilai_rating, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                list.add(data);


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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
