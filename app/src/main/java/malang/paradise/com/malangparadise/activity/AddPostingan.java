package malang.paradise.com.malangparadise.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.json.Kategori;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;
import malang.paradise.com.malangparadise.request.RequestHandler;
import malang.paradise.com.malangparadise.request.Utils;

public class AddPostingan extends AppCompatActivity {


    private File f;
    private Bitmap imageUri;
    private Uri contentUri;
    private static final int PICK_IMAGE = 100;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    private String ConvertImage;
    private Context mCtx;

    String lat, longg;

    public String selectedItemText;

    private final static int MY_REQUEST_CODE = 1;

    private EditText nama;
    private EditText berita;
    private TextView lokasi;
    private ImageView image_view;
    private Button image_choose;
    private Button pick_location;
    private CardView simpan;
    String id_user;
    int id_kategori = 0;
    String nama_kategori = null;
    private Spinner kategori_spinner;
    String lokasiS;

    String latAkhir, longAkhir;

    String[] separated, separated2;

    private LinearLayout lokasiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_postingan);

        nama = findViewById(R.id.nama);
        berita = findViewById(R.id.berita);
        lokasi = findViewById(R.id.lokasi);
        image_view = findViewById(R.id.view_image);
        id_user = getId_user();
        image_choose = findViewById(R.id.choose_image);
        lokasiss = findViewById(R.id.lokasiss);
        pick_location = findViewById(R.id.pickLocation);
        kategori_spinner = findViewById(R.id.kategori_spinner);

        String [] countries ={"PILIH KATEGORI","Pendakian","Pemandian","Edukasi","Makanan"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,countries
        );
        kategori_spinner.setAdapter(adapter);
        kategori_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);

                if(selectedItemText.equals("PILIH CHAPTER")){
                    id_kategori = 1;
                } else if(selectedItemText.equals("Pendakian")){
                    nama_kategori = "pendakian";
                    id_kategori = 2;
                } else if(selectedItemText.equals("Pemandian")){
                    nama_kategori = "pemandian";
                    id_kategori = 3;
                } else if(selectedItemText.equals("Edukasi")) {
                    nama_kategori = "edukasi";
                    id_kategori = 4;
                } else if(selectedItemText.equals("Makanan")) {
                    nama_kategori = "makanan";
                    id_kategori = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Utils.darkenStatusBar(this, R.color.colorPrimary);

        byteArrayOutputStream = new ByteArrayOutputStream();

        if (imageUri != null) {
            imageUri.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {
            Toast.makeText(AddPostingan.this, "Lampirkan Foto", Toast.LENGTH_SHORT).show();
        }

        image_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        pick_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PickLocation.class);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });
        simpan = findViewById(R.id.simpan);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String beritaS = berita.getText().toString().trim();
                final String namaS = nama.getText().toString().trim();

                if (namaS.isEmpty()) {
                    nama.setError("Nama Tidak Boleh Kosong");
                    Toast.makeText(AddPostingan.this, "Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else if (beritaS.isEmpty()) {
                    Toast.makeText(AddPostingan.this, "Berita Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                    berita.setError("No Telepon Tidak Boleh Kosong");
                }else if(lokasiS.isEmpty()){
                    Toast.makeText(AddPostingan.this, "Lokasi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                    lokasi.setError("Lokasi Tidak Boleh Kosong");
                }else if(id_kategori == 0){
                    Toast.makeText(AddPostingan.this, "Harap Pilih Kategori", Toast.LENGTH_SHORT).show();
                } else{
                    if (imageUri != null) {
                        imageUri.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

//                        Toast.makeText(AddPostingan.this, id_user+","+id_kategori+","+nama_kategori+","+f.getName()+","+namaS+","+beritaS+","+lokasiS+","+latAkhir+","+longAkhir, Toast.LENGTH_SHORT).show();

                        class Upload extends AsyncTask<Void, Void, String> {

                            ProgressDialog loading;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                loading = ProgressDialog.show(AddPostingan.this, "Sedang Diproses...", "Tunggu...", false, false);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                loading.dismiss();
                                Toast.makeText(AddPostingan.this, s, Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            protected String doInBackground(Void... v) {
                                HashMap<String, String> params = new HashMap<>();


                                params.put("id_user", id_user);
                                params.put("id_kategori", String.valueOf(id_kategori));
                                params.put("kategori", String.valueOf(nama_kategori));
                                params.put("ImageName", f.getName());
                                params.put("gambar", ConvertImage);
                                params.put("nama", namaS);
                                params.put("berita", beritaS);
                                params.put("lokasi", lokasiS);
                                params.put("lat", latAkhir);
                                params.put("lng", longAkhir);

                                RequestHandler rh = new RequestHandler();
                                String res = rh.sendPostRequest(konfigurasi.URL_POST_POSTINGAN, params);
                                return res;
                            }
                        }

                        Upload ae = new Upload();
                        ae.execute();
                    } else {
                        Toast.makeText(AddPostingan.this, "Lampirkan Foto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = AddPostingan.this.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {

            if (data != null) {
                contentUri = data.getData();

                try {
                    imageUri = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(AddPostingan.this).getContentResolver(), contentUri);
                    String selectedPath = getPath(contentUri);
                    image_view.setImageBitmap(imageUri);
                    image_view.setVisibility(View.VISIBLE);
                    f = new File(selectedPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddPostingan.this, "Failed!", Toast.LENGTH_SHORT).show();
                }

            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                if (data != null)
                    lokasi.setText(data.getStringExtra("nama"));
                    lokasiss.setVisibility(View.VISIBLE);
                assert data != null;
                lokasiS = data.getStringExtra("address");

                separated = data.getStringExtra("latlng").trim().split(",");
                separated2 = separated[0].trim().split(":");

                lat = ""+separated2[1];
                longg = ""+separated[1];

                latAkhir = lat.substring(2);
                longAkhir = longg.replaceFirst(".$","");



//                lokasi.setText(latAkhir+"||"+longAkhir);
            }
        }
    }

    private String getId_user() {
        SharedPreferences preferences = Objects.requireNonNull(AddPostingan.this.getSharedPreferences("Settings", Context.MODE_PRIVATE));
        String id_user = preferences.getString("id_user", "null");
        return id_user;
    }

}
