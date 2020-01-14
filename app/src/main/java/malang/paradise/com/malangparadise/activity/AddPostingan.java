package malang.paradise.com.malangparadise.activity;

import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

    //Declaring views
    @BindView(R.id.spinner_category)
    Spinner category_spinner;
    @BindView(R.id.choose_image)
    Button image_choose;
    @BindView(R.id.view_image)
    ImageView image_view;
    @BindView(R.id.nama)
    EditText name;
    @BindView(R.id.berita)
    EditText news;

    private File f;
    private Bitmap imageUri;
    private Uri contentUri;
    private static final int PICK_IMAGE = 100;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    private String ConvertImage;
    private Context mCtx;

    Kategori kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_postingan);
        ButterKnife.bind(this);

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

//        image_choose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final String nama = name.getText().toString().trim();
//                final String berita = news.getText().toString().trim();
//
//                final AlertDialog.Builder loginDialog = new AlertDialog.Builder(new ContextThemeWrapper(AddPostingan.this, android.R.style.Theme_DeviceDefault_Light_Dialog));
//                LayoutInflater factory = LayoutInflater.from(AddPostingan.this);
//                final View view = factory.inflate(R.layout.activity_add_postingan, null);
//
//                if (nama.isEmpty()) {
//                    name.setError("Nama Tidak Boleh Kosong");
//                } else if (berita.isEmpty()) {
//                    news.setError("No Telepon Tidak Boleh Kosong");
//                } else {
//
//                    if (imageUri != null) {
//                        imageUri.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
//                        byteArray = byteArrayOutputStream.toByteArray();
//                        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
//
//                        class Upload extends AsyncTask<Void, Void, String> {
//
//                            ProgressDialog loading;
//
//                            @Override
//                            protected void onPreExecute() {
//                                super.onPreExecute();
//                                loading = ProgressDialog.show(AddPostingan.this, "Sedang Diproses...", "Tunggu...", false, false);
//                            }
//
//                            @Override
//                            protected void onPostExecute(String s) {
//                                super.onPostExecute(s);
//                                loading.dismiss();
//                                Toast.makeText(AddPostingan.this, s, Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            protected String doInBackground(Void... v) {
//                                HashMap<String, String> params = new HashMap<>();
//
//                                String id_user = getId_user();
//
//                                params.put(konfigurasi.id_user, id_user);
//                                params.put(konfigurasi.namagambar, f.getName());
//                                params.put(konfigurasi.judul, judull);
//                                params.put(konfigurasi.deskripsi, deskripsii);
//                                params.put(konfigurasi.ADD_BUKTI, ConvertImage);
//
//                                RequestHandler rh = new RequestHandler();
//                                String res = rh.sendPostRequest(konfigurasi.URL_GET_UPLOAD_IMAGE, params);
//                                return res;
//                            }
//                        }
//
//                        Upload ae = new Upload();
//                        ae.execute();
//                    } else {
//                        Toast.makeText(AddPostingan.this, "Lampirkan Foto", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
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
                    f = new File(selectedPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddPostingan.this, "Failed!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private String getId_user() {
        SharedPreferences preferences = Objects.requireNonNull(AddPostingan.this.getSharedPreferences("Settings", Context.MODE_PRIVATE));
        String id_user = preferences.getString("id_user", "null");
        return id_user;
    }

}
